package com.zans.mms.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PagePlusResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.config.PermissionConstans;
import com.zans.mms.dao.mms.BaseVfsDao;
import com.zans.mms.dao.mms.PatrolTaskCheckResultMapper;
import com.zans.mms.dao.mms.PatrolTaskMapper;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.model.PatrolTask;
import com.zans.mms.service.IDataPermService;
import com.zans.mms.service.IPatrolTaskService;
import com.zans.mms.service.IWeChatReqService;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.patrol.*;
import com.zans.mms.vo.perm.DataPermVO;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.zans.mms.config.MMSConstants.*;
import static com.zans.mms.config.PatrolConstants.*;

/**
 *  PatrolTaskServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("patrolTaskService")
public class PatrolTaskServiceImpl extends BaseServiceImpl<PatrolTask> implements IPatrolTaskService {


	@Autowired
	private PatrolTaskMapper patrolTaskMapper;

	@Autowired
    private BaseVfsDao baseVfsDao;

    @Autowired
    private IWeChatReqService weChatReqService;

    @Autowired
    IDataPermService dataPermService;


    @Resource
    public void setPatrolTaskMapper(PatrolTaskMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.patrolTaskMapper = baseMapper;
    }

    @Autowired
    private PatrolTaskCheckResultMapper patrolTaskCheckResultMapper;

    @Override
    public ApiResult getList(PatrolTaskQueryVO vo,UserSession userSession) {
        DataPermVO dataPerm = dataPermService.getPcPatrolTaskPerm(userSession);
        if (!dataPerm.selectAll()) {
            List<String> orgIds = vo.getOrgIds();
            // 清空单位查询条件 用数据权限
            vo.setOrgIds(null);
            for (String s : dataPerm.getDataPermList()) {
                if (Integer.parseInt(s) == PermissionConstans.PERM_ORG) {
                    //单位权限 2&dataPermValue >=2
                    vo.setOrgIdPerm(userSession.getOrgId());
                } else if (Integer.parseInt(s) == PermissionConstans.PERM_SELF) {
                    //个人权限 1&dataPermValue =1
                    vo.setUserNamePerm(userSession.getUserName());
                }
            }
            // 查询条件不包含orgId无记录
            if (!orgIds.contains(userSession.getOrgId()) && orgIds!= null && orgIds.size()>0){
                return ApiResult.success(new PageResult<PatrolTaskResVO>(0, new ArrayList<>(), vo.getPageSize(), vo.getPageNum()));
            }

        }

        int pageNum = vo.getPageNum();
        int pageSize = vo.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<PatrolTaskResVO> result = patrolTaskMapper.getList(vo);
        String nowDate = DateHelper.getNow();
        //查询完成信息数据
        List<PatrolFinshInfoVO> patrolFinshInfoVOList  =patrolTaskMapper.getPatrolFinshInfo();
        for (PatrolTaskResVO resVO : result) {
            String startDate = resVO.getStartDate()+PATROL_START_TIME_SUFFIX;
            String endDate = resVO.getEndDate()+PATROL_END_TIME_SUFFIX;
            Integer s = DateHelper.calculateIntervalTimeTwo(DateHelper.parseDatetime(endDate));
            resVO.setSurplusDays(s);
            if (nowDate.compareTo(startDate)<0){
                resVO.setExecuteStatus(PATROL_NOT_START_STR);
            } else if (nowDate.compareTo(endDate)>0){
                resVO.setExecuteStatus(PATROL_FINISH_STR);
            }else {
                resVO.setExecuteStatus(PATROL_RUNNING_STR);
            }
            for(PatrolFinshInfoVO patrolFinshInfoVO :patrolFinshInfoVOList){
                if(patrolFinshInfoVO.getPatrolTaskId().equals(resVO.getId())){
                    resVO.setFinishedCount(patrolFinshInfoVO.getFinishedCount());
                    //在线率 *100 +百分号
                    resVO.setFinishedRate(patrolFinshInfoVO.getFinishedRate().multiply(MMSConstants.ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
                    continue;
                }
            }
        }



        return ApiResult.success(new PageResult<PatrolTaskResVO>(page.getTotal(), result, pageSize, pageNum));
     }

	@Override
	public PatrolTaskDetailResVO getViewById(Long id) {
		return null;
	}


	@Override
	public int deleteById(Long id) {
		return patrolTaskMapper.deleteByUniqueId(id);
	}

    @Override
    public ApiResult getView(PatrolTaskQueryVO vo) {
        List<PatrolTaskResVO> result = patrolTaskMapper.getList(vo);
        PatrolTaskResVO patrolTaskResVO = result.get(0);
        PatrolTaskDetailResVO resVo = new PatrolTaskDetailResVO();
        BeanUtils.copyProperties(patrolTaskResVO,resVo);

        int pageNum = vo.getPageNum();
        int pageSize = vo.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TaskDetailVO> taskDetailList = patrolTaskMapper.getPatrolResult(vo);
        //查询完成信息数据
        List<PatrolFinshInfoVO> patrolFinshInfoVOList  =patrolTaskMapper.getPatrolFinshInfo();
        for(PatrolFinshInfoVO patrolFinshInfoVO : patrolFinshInfoVOList){
            if(patrolFinshInfoVO.getPatrolTaskId().equals(resVo.getId())){
                resVo.setFinishedCount(patrolFinshInfoVO.getFinishedCount());
                //在线率 *100 +百分号
                resVo.setFinishedRate(patrolFinshInfoVO.getFinishedRate().multiply(MMSConstants.ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
                continue;
            }
        }
        for (TaskDetailVO detailVO : taskDetailList) {
            BaseVfs vfs;
            if (StringUtils.isNotBlank(detailVO.getAdjunctUuid())){
                vfs = new BaseVfs();
                vfs.setAdjunctId(detailVO.getAdjunctUuid());
                List<BaseVfs> baseVfs = baseVfsDao.queryAll(vfs);
                detailVO.setBaseVfsList(baseVfs);
            }
        }
        resVo.setTaskList(taskDetailList);

        return ApiResult.success(new PagePlusResult<TaskDetailVO>(page.getTotal(), taskDetailList, pageSize, pageNum,null,resVo));
    }

    @Override
    public ApiResult getPatrolLogList(PatrolLogQueryVO vo,  UserSession userSession) {

        DataPermVO dataPerm = dataPermService.getPcPatrolLogPerm(userSession);

        if (!dataPerm.selectAll()) {
            List<String> orgIds = vo.getOrgIds();
            // 清空单位查询条件 用数据权限
            vo.setOrgIds(null);
            for (String s : dataPerm.getDataPermList()) {
                if (Integer.parseInt(s) == PermissionConstans.PERM_ORG) {
                    //单位权限 2&dataPermValue >=2
                    vo.setOrgIdPerm(userSession.getOrgId());
                } else if (Integer.parseInt(s) == PermissionConstans.PERM_SELF) {
                    //个人权限 1&dataPermValue =1
                    vo.setUserNamePerm(userSession.getUserName());
                }
            }
            // 查询条件不包含orgId无记录
            if (!orgIds.contains(userSession.getOrgId()) && orgIds!= null && orgIds.size()>0){
                return ApiResult.success(new PageResult<PatrolLogRespVO>(0, new ArrayList<>(), vo.getPageSize(), vo.getPageNum()));
            }
        }


        int pageNum = vo.getPageNum();
        int pageSize = vo.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<PatrolLogRespVO> respVOList = patrolTaskMapper.getPatrolLogList(vo);
        for (PatrolLogRespVO detailVO : respVOList) {
            BaseVfs vfs;
            if (StringUtils.isNotBlank(detailVO.getAdjunctUuid())){
                vfs = new BaseVfs();
                vfs.setAdjunctId(detailVO.getAdjunctUuid());
                List<BaseVfs> baseVfs = baseVfsDao.queryAll(vfs);
                detailVO.setBaseVfsList(baseVfs);
            }
        }

        return ApiResult.success(new PageResult<PatrolLogRespVO>(page.getTotal(), respVOList, pageSize, pageNum));
    }

    @Override
    public Boolean pushPatrolMessage() {
        List<PatrolPushMessageData>  dataList =patrolTaskMapper.getPushPatrolData();
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        weChatPushReqVO.setTemplateType(PATROL_TEMPLATE_ACCEPT_UN_DONE);


        List<String> keywords = new ArrayList<>();
        keywords.add("巡检未完成提醒"+DateHelper.formatDate(new Date(),DateHelper.yyyyMMdd));
        keywords.add(DateHelper.getNow());
        keywords.add("巡检未完内容");
        weChatPushReqVO.setKeywords(keywords);

        JSONObject jsonObject = new JSONObject();

        for (PatrolPushMessageData pushMessageData : dataList) {
            weChatPushReqVO.setOrgId(pushMessageData.getOrgId());

            jsonObject.put("nowTime", DateHelper.getNow());
            jsonObject.put("orgName", pushMessageData.getOrgName());
            jsonObject.put("pointTotal", pushMessageData.getUnCheckCount());
            weChatPushReqVO.setJsonObject(jsonObject);

            //发外场
            weChatPushReqVO.setRoleNum(OUTFIELD_ROLE);
            weChatReqService.weChatPush(weChatPushReqVO);

            //发内场
            weChatPushReqVO.setRoleNum(INSIDE_ROLE);
            weChatReqService.weChatPush(weChatPushReqVO);
        }

        return true;
    }

    @Override
    public List<CircleUnit> getAppPatrolTotal(UserSession userSession) {
        DataPermVO dataPerm = dataPermService.getTopDataPerm(userSession);
        PatrolTaskQueryVO vo  = new PatrolTaskQueryVO();

        if(dataPerm.getDataPerm() == 2){
            vo.setOrgIdPerm(userSession.getOrgId());
        }

        //判断是否所有权限 如果不是全部的话 做权限判断
//        if(!dataPerm.selectAll()){
//            for(String s : dataPerm.getDataPermList()){
//                if (Integer.parseInt(s) == PermissionConstans.PERM_ORG) {
//                    //单位权限 2&dataPermValue >=2
//                    vo.setOrgIdPerm(userSession.getOrgId());
//                    break;
//                }
//            }
//        }
        Map patrolTotal = patrolTaskMapper.getPatrolTotal(vo);

        return  Arrays.asList(
                parseCircleUnit(patrolTotal,"total","总数"),
                parseCircleUnit(patrolTotal,"todayCompleted","今日完成"),
                parseCircleUnit(patrolTotal,"todayMustCompleted","今日待完成")
        );
    }


    private static CircleUnit parseCircleUnit(Map patrolTotalMap,String name,String chineName) {
        return CircleUnit.builder().val(patrolTotalMap.get(name)).name(name).chineName(chineName).build();
    }

    @Override
    public List<CircleUnit> getPcPatrolTotal(UserSession userSession) {
        return getAppPatrolTotal(userSession);
    }
}
