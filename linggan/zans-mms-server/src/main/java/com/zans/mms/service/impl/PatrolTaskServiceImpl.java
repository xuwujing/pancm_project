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
import com.zans.mms.dao.*;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.model.PatrolTask;
import com.zans.mms.model.PatrolTaskCheckResult;
import com.zans.mms.service.IDataPermService;
import com.zans.mms.service.IPatrolTaskService;
import com.zans.mms.service.IWeChatReqService;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.patrol.*;
import com.zans.mms.vo.perm.DataPermVO;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import com.zans.mms.vo.ticket.AppTicketDailyReportVO;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.zans.base.util.DateHelper.formatDate;
import static com.zans.base.util.DateHelper.plusDays;
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
    private TicketDao ticketDao;

    @Resource
    private WorkflowTaskInfoMapper workflowTaskInfoMapper;


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
            //关联上次巡检的状态
            String pointId = detailVO.getPointId().toString();
            //根据pointId查询上次巡检的状态
            List<String> checkResultList = patrolTaskMapper.getRecentCheckResult(pointId);
            if(checkResultList!=null && checkResultList.size()==2){
                detailVO.setPrevCheckResult(checkResultList.get(1));
            }
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

          /*  //关联上次巡检的状态
            String pointId = detailVO.getPointId();
            //根据pointId查询上次巡检的状态
            List<String> checkResultList = patrolTaskMapper.getRecentCheckResult(pointId);
            if(checkResultList!=null && checkResultList.size()==2){
                detailVO.setPrevCheckResult(checkResultList.get(1));
            }*/
            //图片查询
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
        keywords.add("巡检未完成提醒"+ formatDate(new Date(),DateHelper.yyyyMMdd));
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



    private static CircleUnit parseCircleUnit(Map patrolTotalMap, String name, String chineName) {
        return CircleUnit.builder().val(patrolTotalMap.get(name)).name(name).chineName(chineName).build();
    }

    @Override
    public List<CircleUnit> getPcPatrolTotal(UserSession userSession) {
        return getAppPatrolTotal(userSession);
    }

    /**
     * 推送工单报表信息
     */
    @Override
    public Boolean pushTicketMessageTask() {
        //待推送unionId列表
        List<String> usernameList = new ArrayList<>();
        Collections.addAll(usernameList,"oyb","liuxiang","lgwy-test","cy","lht","lls","jyh","sk","fangy");
        List<String> unionidList = workflowTaskInfoMapper.getUserByApproved(usernameList);
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        weChatPushReqVO.setTemplateType(TICKET_TEMPLATE_RECORD);
        weChatPushReqVO.setUnionIdList(unionidList);
        weChatPushReqVO.setCreator("lgwy");
        List<String> keywords = new ArrayList<>();
        keywords.add(DateHelper.getNow());
        keywords.add("交管局科技处");
        keywords.add("工单数据统计");
        weChatPushReqVO.setKeywords(keywords);

        JSONObject jsonObject = new JSONObject();

        String breakdownCountMsg="当日未发起故障工单";
        String breakdownCompleteCountMsg="暂无工单完工";
        String dispatchMoneyMsg="未发起派工单";
        String dispatchTicketCountMsg="派工单全部审核完成";
        String acceptanceMoneyMsg="未发起验收单";
        String acceptanceTicketCountMsg="验收单全部审核完成";

        //查询当日统计信息
        //故障工单数量
        Integer breakDownCount=ticketDao.getBreakdownTicketWithoutOrgId(1);
        if(null!=breakDownCount&&0!=breakDownCount){
            breakdownCountMsg="当日发起故障工单"+breakDownCount+"个";
        }
        //故障完工数量
        Integer breakDownCompleteCount=ticketDao.getBreakdownTicketCompleteWithoutOrgId(1);
        if(null!=breakDownCompleteCount&&0!=breakDownCompleteCount){
            breakdownCompleteCountMsg="工单完工"+breakDownCompleteCount+"个";
        }
        //发起的派工单金额
        String dispatchMoney=ticketDao.getDispatchMoney(1);
        //发起的派工单数量
        Integer dispatchNum=ticketDao.getDispatchNum(1);
        if(null!=dispatchNum&&0!=dispatchNum){
            dispatchMoneyMsg="据实结算派工单共"+dispatchMoney+"元("+dispatchNum+")笔";
        }
      /*  //待审批派工单数量
        Integer approvedDispatchCount=ticketDao.getApprovedDispatchCount(1);
        //待审批验收单数量
        Integer approvedAcceptanceCount=ticketDao.getApprovedAcceptanceCount(1);
        if(null!=approvedDispatchCount&&0!=approvedDispatchCount){
            dispatchTicketCountMsg="其中"+approvedDispatchCount+"笔待审批";
        }
        if(null!=approvedAcceptanceCount&&0!=approvedAcceptanceCount){
            acceptanceTicketCountMsg="其中"+approvedAcceptanceCount+"笔待审批";
        }*/
        //发起的验收单金额
        String acceptanceMoney=ticketDao.getAcceptanceMoney(1);
        //发起的验收单数量
        Integer acceptanceNum=ticketDao.getAcceptanceNum(1);
        if(null!=acceptanceNum&&0!=acceptanceNum){
            acceptanceMoneyMsg="验收单共"+acceptanceMoney+"元("+acceptanceNum+")笔";
        }

        jsonObject.put("breakdownCount", breakdownCountMsg);
        jsonObject.put("breakdownCompleteCount", breakdownCompleteCountMsg);
        jsonObject.put("dispatchMoney", dispatchMoneyMsg);
        jsonObject.put("dispatchTicketCount", dispatchTicketCountMsg);
        jsonObject.put("acceptanceMoney", acceptanceMoneyMsg);
        jsonObject.put("acceptanceTicketCount", acceptanceTicketCountMsg);

        weChatPushReqVO.setJsonObject(jsonObject);
        try {
            weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
        }catch (Exception e){
            log.error("工单数据推送失败!");
        }

        return true;
    }


    /**
     * 巡检报表table
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public List<AppPortalTaskTableRepVO> getPoTable(AppTicketCharReqVO appTicketCharReqVO) {
        List<AppPortalTaskTableRepVO> appPortalTaskTableRepVOList = new ArrayList<>();
        AppPortalTaskTableRepVO appPortalTaskTableRepVO = new AppPortalTaskTableRepVO();
        List<String> deviceTypeList = new ArrayList<>();
        Collections.addAll(deviceTypeList,"信号机","诱导屏","电警","卡口","监控");
        appPortalTaskTableRepVO.setDeviceType(deviceTypeList);
        List<String> areaList = new ArrayList<>();
        Collections.addAll(areaList,"10001","10002","10003");
        List<String> areaNameList = new ArrayList<>();
        Collections.addAll(areaNameList,"汉口片区","武昌片区","汉阳片区");
        List<Map<String,Object>> data = new ArrayList<>();
        for(int i=0;i<areaList.size();i++){
            //汉口片区
            Map<String, Object> hk = new HashMap<>();
            //汉口巡检
            Map<String, Object> hkxj = new HashMap<>();
            //查询汉口的巡检数据
            appTicketCharReqVO.setOrgId(areaList.get(i));
            List<Integer> hkxjData = patrolTaskMapper.getCheckCount(appTicketCharReqVO);
            hkxj.put("巡检",hkxjData);
            //汉口巡检
            Map<String, Object> hkyq = new HashMap<>();
            //查询汉口逾期数据
            List<Integer> hkyqData = patrolTaskMapper.getTimeOutCount(appTicketCharReqVO);
            hkyq.put("逾期",hkyqData);
            //汉口故障
            Map<String, Object> hkgz = new HashMap<>();
            //汉口故障数据
            List<Integer> hkgzData = patrolTaskMapper.getBreakDownCount(appTicketCharReqVO);
            hkgz.put("故障",hkgzData);
            int breakDownCount=0;
            for(int j =0;j<hkgzData.size();j++){
                breakDownCount+=hkgzData.get(j);
            }
            appPortalTaskTableRepVO.setBreakDownCount(breakDownCount);
            BigDecimal rate = patrolTaskMapper.getRate(appTicketCharReqVO);
            appPortalTaskTableRepVO.setRate(rate.multiply(MMSConstants.ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
            List<Map<String,Object>> hkpq = new ArrayList<>();
            hkpq.add(hkxj);
            hkpq.add(hkyq);
            hkpq.add(hkgz);
            hk.put(areaNameList.get(i),hkpq);
            data.add(hk);
            ///todo 完成率计算  已完成数量/今日应完成的数量
            appPortalTaskTableRepVO.setData(data);
            appPortalTaskTableRepVOList.add(appPortalTaskTableRepVO);
        }

        return appPortalTaskTableRepVOList;
    }


    /**
     * 删除巡检任务及巡检任务下的点位
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult deleteTask(Long id) {
        //先删除点位 再删除巡检任务
        patrolTaskMapper.deleteCheckResult(id);
        patrolTaskMapper.deleteByUniqueId(id);
        return ApiResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanPatrolResult(Integer cleanMonth) {
        Date date=new Date();
        String dateTime = formatDate(plusDays(date, -cleanMonth), "yyyy-MM-dd")+" 00:00:00";

        Long maxRecordId = patrolTaskCheckResultMapper.getMaxRecord(dateTime);
        if (maxRecordId != null && maxRecordId.intValue()>0){
            patrolTaskCheckResultMapper.saveBak(maxRecordId);
            patrolTaskCheckResultMapper.deleteHis(maxRecordId);
        }
    }
}
