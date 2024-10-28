package com.zans.mms.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.PatrolConstants;
import com.zans.mms.config.PermissionConstans;
import com.zans.mms.dao.mms.BaseVfsDao;
import com.zans.mms.dao.mms.PatrolTaskCheckResultMapper;
import com.zans.mms.model.*;
import com.zans.mms.service.IDataPermService;
import com.zans.mms.service.IPatrolTaskCheckResultService;
import com.zans.mms.vo.patrol.*;
import com.zans.mms.vo.perm.DataPermVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.base.util.DateHelper.yyyy_MM_dd;
import static com.zans.mms.config.PatrolConstants.*;

/**
 *  PatrolTaskCheckResultServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("patrolTaskCheckResultService")
public class PatrolTaskCheckResultServiceImpl extends BaseServiceImpl<PatrolTaskCheckResult> implements IPatrolTaskCheckResultService {

	@Autowired
	private PatrolTaskCheckResultMapper patrolTaskCheckResultMapper;


	@Autowired
    private BaseVfsDao vfsMapper;


    @Autowired
    IDataPermService dataPermService;

    @Resource
    public void setPatrolTaskCheckResultMapper(PatrolTaskCheckResultMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.patrolTaskCheckResultMapper = baseMapper;
    }


    private String getDataEndTime(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @Override
    public List<PatrolDevicePointResVO> searchDevicePoint(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession) {
        Map<String,Object> params = new HashMap(8);

        setPatrolPointParams(patrolDevicePointReqVO, userSession, params);

        Integer limitFlag = patrolDevicePointReqVO.getLimitFlag();
        if (limitFlag != null && PATROL_LIMIT_FLAG.equals(limitFlag)) {
            params.put("limitFlag", limitFlag);
        }
        String areaIdStr = userSession.getAreaIdStr();
        //用户层的区域id字符串，逗号分割
        if (!StringUtils.isEmpty(areaIdStr)){
            params.put("areaIdStr",areaIdStr);
        }

        return patrolTaskCheckResultMapper.searchDevicePoint(params);
    }

    @Override
    public List<PatrolDevicePointResVO> searchDevicePointByPointId(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession) {
        Map<String,Object> params = new HashMap(8);

        setPatrolPointParams(patrolDevicePointReqVO, userSession, params);

        Integer limitFlag = patrolDevicePointReqVO.getLimitFlag();
        if (limitFlag != null && PATROL_LIMIT_FLAG.equals(limitFlag)) {
            params.put("limitFlag", limitFlag);
        }
        String areaIdStr = userSession.getAreaIdStr();
        //用户层的区域id字符串，逗号分割
        if (!StringUtils.isEmpty(areaIdStr)){
            params.put("areaIdStr",areaIdStr);
        }

        return patrolTaskCheckResultMapper.searchDevicePoint(params);
    }

    /**
    * @Author beiming
    * @Description
    * @Date  4/15/21
    * @Param 设置app巡检查询参数
    * @return
    **/
    public void setPatrolPointParams(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession, Map params) {
        Integer radius = patrolDevicePointReqVO.getRadius();
        if (radius == null || radius.intValue() < MIN_RADIUS) {
            radius = MIN_RADIUS;
        } else if (radius.intValue() > MAX_RADIUS) {
            radius = MAX_RADIUS;
        }
        String lastEndTime = null;
        List<Integer> dayList = patrolDevicePointReqVO.getRemainingDayList();
        Integer remainingDays = patrolDevicePointReqVO.getRemainingDays();
        if (remainingDays != null && remainingDays < PatrolConstants.REMAINING_DAY_TWOPLUS && remainingDays >= PatrolConstants.REMAINING_DAY_ONE) {
            lastEndTime = getDataEndTime(LocalDate.now().plusDays(remainingDays));
        }
        params.put("containCompleted", patrolDevicePointReqVO.getContainCompleted());
        params.put("selfGis", String.format(FORMAT_FUNC_POINT, patrolDevicePointReqVO.getSelfLongitude(),
                patrolDevicePointReqVO.getSelfLatitude()));
        params.put("moveGis", String.format(FORMAT_FUNC_POINT, patrolDevicePointReqVO.getMoveLongitude(),
                patrolDevicePointReqVO.getMoveLatitude()));

        params.put("gisFlag",PATROL_GIS_FLAG);

        String[] deviceTypes = patrolDevicePointReqVO.getDeviceTypes();
        if (deviceTypes != null && deviceTypes.length == 0) {
            deviceTypes = null;
        }
        String[] areaIds = patrolDevicePointReqVO.getAreaIds();
        if (areaIds != null && areaIds.length == 0) {
            areaIds = null;
        }
        params.put("areaIds",areaIds);
        // 剩余时间单选 先注释掉
///        params.put("lastEndTime", lastEndTime);
        params.put("radius", radius);
        params.put("deviceTypes",deviceTypes);
        if (dayList != null && dayList.size()>0) {
            List<Integer> remainingDayList = new ArrayList<>();
            for (Integer i : dayList) {
                //datediff(date_format(r.end_time,'%Y%m%d'),now()) = #{item}  需要天数-1
               remainingDayList.add(i-1);
            }
            params.put("remainingDayList",remainingDayList);
        }

        params.put("pointId",patrolDevicePointReqVO.getPointId());

        if (params.getOrDefault("removePermData",0).equals(PATROL_REMOVE_DATA_PERM_FLAG)){
            params.put("orgId", patrolDevicePointReqVO.getOrgId());
            return;
        }


        //处理数据权限
        DataPermVO dataPerm = dataPermService.getAppPatrolPerm(userSession);
        //判断是否所有权限 如果不是全部的话 做权限判断
        if(!dataPerm.selectAll()){
            for(String s : dataPerm.getDataPermList()){
                if (Integer.parseInt(s) == PermissionConstans.PERM_ORG) {
                    //单位权限 2&dataPermValue >=2
                    params.put("orgId", userSession.getOrgId());
                    break;
                }
            }
        }
    }

    @Override
    public PatrolAggTopDataResVO aggregateData(UserSession userSession) {
        Map params = new HashMap(1);
        //处理数据权限
        DataPermVO dataPerm = dataPermService.getAppPatrolTopAggPerm(userSession);
        //判断是否所有权限 如果不是全部的话 做权限判断
        if(!dataPerm.selectAll()){
            for(String s : dataPerm.getDataPermList()){
                if (Integer.parseInt(s) == PermissionConstans.PERM_ORG) {
                    //单位权限 2&dataPermValue >=2
                    params.put("orgId", userSession.getOrgId());
                    break;
                }
            }
        }
        List<PatrolAggData> patrolAggData = patrolTaskCheckResultMapper.aggTopData(params);
        PatrolAggTopDataResVO topAgg = new PatrolAggTopDataResVO();
        patrolAggData.stream().forEach(item -> topAgg.processModelData(item));
        topAgg.doCalcCompletedStatus();
        return topAgg;
    }

    @Override
    public PatrolAppCheckInfo getCheckInfoWithApp(Integer pointId) {
        PatrolAppCheckInfo checkInfo = patrolTaskCheckResultMapper.getCheckInfoWithApp(pointId);
        String adjunctUuid = checkInfo.getAdjunctUuid();
        if (!StringUtils.isEmpty(adjunctUuid)) {
            BaseVfs condition = new BaseVfs();
            condition.setAdjunctId(adjunctUuid);
            List<BaseVfs> adjunctList = vfsMapper.queryAll(condition);
            if (adjunctList != null && adjunctList.size() > 0) {
                checkInfo.setAdjunctList(adjunctList);
            }
        }
        return checkInfo;
    }

    @Override
    public Integer updateClockIn(PatrolClockIn clockIn) {
        clockIn.setCheckGis(String.format(FORMAT_FUNC_POINT, clockIn.getCheckLongitude(), clockIn.getCheckLatitude()));
        return patrolTaskCheckResultMapper.updateClockIn(clockIn);
    }

    @Override
    public List<PatrolAllDevicePointResVO> appSearchAllDevicePoint(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession) {
        Map<String,Object> params = new HashMap(8);
        setPatrolPointParams(patrolDevicePointReqVO, userSession, params);
        params.remove("gisFlag");
        String areaIdStr = userSession.getAreaIdStr();
        //用户层的区域id字符串，逗号分割
        if (!StringUtils.isEmpty(areaIdStr)){
            params.put("areaIdStr",areaIdStr);
        }

        List<PatrolAllDevicePointResVO> resVOList = patrolTaskCheckResultMapper.searchAllDevicePoint(params);
        if (resVOList != null && resVOList.size() > 0) {
            int i = 0;
            for (PatrolAllDevicePointResVO vo : resVOList) {
                //设置序号
                vo.setId(i++);
            }
        }
        return resVOList;
    }

    @Override
    public PatrolRefreshResVO searchPatrolRefresh(PatrolDevicePointReqVO reqVO, UserSession userSession) {
        Map<String,Object> params = new HashMap(8);
        PatrolRefreshResVO resVO = new PatrolRefreshResVO();
        setPatrolPointParams(reqVO, userSession, params);

        params.put("lastTime",reqVO.getLastTime());
        params.put("nowTime",reqVO.getNowTime());
        List<PatrolPointRefreshVO> pointIds = patrolTaskCheckResultMapper.searchPatrolRefresh(params);
///        PatrolPointRefreshVO vo1 = new PatrolPointRefreshVO();
///        vo1.setPointId(789L);
///        vo1.setDeviceType("24");
///        PatrolPointRefreshVO vo2 = new PatrolPointRefreshVO();
///        vo2.setPointId(5214L);
///        vo2.setDeviceType("24");
///
///        pointIds.add(vo1);
///        pointIds.add(vo2);

        //未巡检+巡检
        JSONObject jsonObject = getPatrolStatistical(params);

        resVO.setPoints(pointIds);
        resVO.setStatistical(jsonObject);
        return resVO;
    }

    public JSONObject getPatrolStatistical(Map<String, Object> params) {
       // params.remove("containCompleted");
        // 移除天数统计
        params.remove("remainingDayList");
        List<PatrolAllDevicePointResVO> resVOList = patrolTaskCheckResultMapper.searchAllDevicePoint(params);
        long threeDay = resVOList.stream().filter(vo -> vo.getRDays().intValue() >= 3 && PATROL_CHECK_STATUS_NO_DONE.equals(vo.getCheStatus())).count();
        long twoDay = resVOList.stream().filter(vo -> vo.getRDays().intValue() == 2 && PATROL_CHECK_STATUS_NO_DONE.equals(vo.getCheStatus())).count();
        long oneDay = resVOList.stream().filter(vo -> vo.getRDays().intValue() == 1 && PATROL_CHECK_STATUS_NO_DONE.equals(vo.getCheStatus())).count();

        long doneToday = resVOList.stream().filter(vo -> {
            String checkTime = vo.getCheckTime();
            String  startTime = DateHelper.formatDate(new Date(),yyyy_MM_dd)+PATROL_START_TIME_SUFFIX;
            String  endTime = DateHelper.formatDate(new Date(),yyyy_MM_dd)+PATROL_END_TIME_SUFFIX;
            //先判断打卡状态是否为已打卡 然后进行打卡时间比较
            if (STATUS_COMPLETED_DONE.equals(vo.getCheStatus()) && checkTime.compareTo(startTime)>0 && checkTime.compareTo(endTime)<0){
                return true;
            }
            return false;
        }).count();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("threeDay",threeDay);
        jsonObject.put("twoDay",twoDay);
        jsonObject.put("oneDay",oneDay);
        jsonObject.put("doneToday",doneToday);
        jsonObject.put("total",resVOList.size());


        // 统计 各设备类型数量
        if (params.getOrDefault("statsDeviceType",1).equals(1)){
            Map<String, Long> deviceTypeCountMap = resVOList.stream().collect(Collectors.groupingBy(PatrolAllDevicePointResVO::getDType, Collectors.counting()));
            jsonObject.put("statsDeviceType",deviceTypeCountMap);
        }

        return jsonObject;
    }

    @Override
    public PatrolRefreshResVO getPatrolStatistical(PatrolDevicePointReqVO reqVO, UserSession userSession) {
        Map<String,Object> params = new HashMap(8);
        PatrolRefreshResVO resVO = new PatrolRefreshResVO();
        setPatrolPointParams(reqVO, userSession, params);

        String areaIdStr = userSession.getAreaIdStr();
        //用户层的区域id字符串，逗号分割
        if (!StringUtils.isEmpty(areaIdStr)){
            params.put("areaIdStr",areaIdStr);
        }

        //未巡检+巡检
        JSONObject jsonObject = getPatrolStatistical(params);
        resVO.setStatistical(jsonObject);

        return resVO;
    }

    @Override
    public ApiResult pcSearchAllDevicePoint(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession) {
        Map<String,Object> params = new HashMap(8);
        //不pc不加数据权限
        params.put("removePermData",PATROL_REMOVE_DATA_PERM_FLAG);
        setPatrolPointParams(patrolDevicePointReqVO, userSession, params);
        //pc 不显示距离 不展示
        params.remove("gisFlag");

        List<PatrolAllDevicePointResVO> resVOList = patrolTaskCheckResultMapper.searchAllDevicePoint(params);
        if (resVOList != null && resVOList.size() > 0) {
            int i = 0;
            for (PatrolAllDevicePointResVO vo : resVOList) {
                //设置序号
                vo.setId(i++);
            }
        }

        params.put("statsDeviceType",1);
        //未巡检+巡检
        JSONObject jsonObject = getPatrolStatistical(params);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put("pointList",resVOList)
                .put("statistical",jsonObject)
                .build();


        return ApiResult.success(result);
    }

    @Override
    public List<PatrolDevicePointResVO> pcSearchDevicePointByPointId(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession) {
        Map<String,Object> params = new HashMap(8);
        //不pc不加数据权限
        params.put("removePermData",PATROL_REMOVE_DATA_PERM_FLAG);
        setPatrolPointParams(patrolDevicePointReqVO, userSession, params);
        //pc 不显示距离 不展示
        params.remove("gisFlag");

        Integer limitFlag = patrolDevicePointReqVO.getLimitFlag();
        if (limitFlag != null && PATROL_LIMIT_FLAG.equals(limitFlag)) {
            params.put("limitFlag", limitFlag);
        }

        return patrolTaskCheckResultMapper.searchDevicePoint(params);
    }
}
