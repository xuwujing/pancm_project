package com.zans.mms.controller.applet;

import com.zans.base.controller.BaseController;
import com.zans.base.exception.BusinessException;
import com.zans.base.util.DateHelper;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.PatrolAppCheckInfo;
import com.zans.mms.model.PatrolClockIn;
import com.zans.mms.service.IPatrolTaskCheckResultService;
import com.zans.mms.service.IPatrolTaskService;
import com.zans.mms.service.ITicketService;
import com.zans.mms.vo.patrol.*;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.Ticket;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.zans.base.util.DateHelper.yyyy_MM_dd;
import static com.zans.mms.config.PatrolConstants.PATROL_LIMIT_FLAG;


/**
* @Title: AppPatrolController
* @Description:  微信小程序的巡检Controller
* @Version:1.0.0
* @Since:jdk1.8
* @author beiso
* @Date  2021/3/9
**/

@Api(value = "/小程序巡检", tags = {"/小程序巡检~ 巡检点位/打卡"})
@RestController
@Validated
@Slf4j
@RequestMapping("app/patrol")
public class AppPatrolController extends BaseController {
    @Autowired
    private IPatrolTaskCheckResultService patrolTaskCheckResultService;

    @Autowired
    ITicketService ticketService;

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private IPatrolTaskService patrolTaskService;


    @ApiOperation(value = "查询作为的点位", notes = "查询作为的点位")
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public ApiResult search(HttpServletRequest req, @RequestBody PatrolDevicePointReqVO patrolDevicePointReqVO) {
        UserSession userSession = httpHelper.getUser(req);
        patrolDevicePointReqVO.setOrgId(userSession.getOrgId());
        patrolDevicePointReqVO.setLimitFlag(PATROL_LIMIT_FLAG);
        //只查未巡检的10条
        patrolDevicePointReqVO.setContainCompleted(0);
        List<PatrolDevicePointResVO> patrolDevicePointVO =
                patrolTaskCheckResultService.searchDevicePoint(patrolDevicePointReqVO,userSession);
        return ApiResult.success(patrolDevicePointVO);
    }

    @ApiOperation(value = "查看汇总数据", notes = "查询作为的点位")
    @RequestMapping(value = "topAggData", method = RequestMethod.POST)
    public ApiResult aggregateData(HttpServletRequest req) {
        UserSession userSession = httpHelper.getUser(req);
        PatrolAggTopDataResVO aggData = patrolTaskCheckResultService.aggregateData(userSession);
        return ApiResult.success(aggData);
    }


    @ApiOperation(value = "获取点位详情", notes = "获取点位详情")
    @RequestMapping(value = "getCheckInfo", method = RequestMethod.POST)
    public ApiResult getCheckInfo(HttpServletRequest req,@RequestBody PatrolDevicePointReqVO patrolDevicePointReqVO) {
        PatrolAppCheckInfo checkInfoWithApp =
                patrolTaskCheckResultService.getCheckInfoWithApp(patrolDevicePointReqVO.getPointId());
        return ApiResult.success(checkInfoWithApp);
    }


    @ApiOperation(value = "工单打卡", notes = "工单打卡")
    @RequestMapping(value = "clockIn", method = RequestMethod.POST)
    public ApiResult clockIn(HttpServletRequest req, @RequestBody PatrolClockIn clockIn) {
        Integer count = patrolTaskCheckResultService.updateClockIn(clockIn);
        if(clockIn.getCheckResult()==0){
            //发起工单
            ticketService.createTicket(clockIn);
        }
        SelectVO vo = new SelectVO();
        vo.setItemKey("affectedRows");
        vo.setItemValue(String.valueOf(count));
        return ApiResult.success(vo);
    }

    @ApiOperation(value = "查询全部点位", notes = "查询全部点位")
    @RequestMapping(value = "searchAll", method = RequestMethod.POST)
    public ApiResult searchAll(HttpServletRequest req, @RequestBody PatrolDevicePointReqVO patrolDevicePointReqVO) {
        UserSession userSession = httpHelper.getUser(req);
        patrolDevicePointReqVO.setOrgId(userSession.getOrgId());
        List<PatrolAllDevicePointResVO> patrolDevicePointVO =
                patrolTaskCheckResultService.appSearchAllDevicePoint(patrolDevicePointReqVO,userSession);
        return ApiResult.success(patrolDevicePointVO);
    }

    @ApiOperation(value = "查询巡检点位byId", notes = "查询巡检点位byId")
    @RequestMapping(value = "searchPatrolByPointId", method = RequestMethod.POST)
    public ApiResult searchPatrolByPointId(HttpServletRequest req, @RequestBody PatrolDevicePointReqVO patrolDevicePointReqVO) {
        UserSession userSession = httpHelper.getUser(req);
        //查询单个点位，可以查询已巡检 和未巡检的
        patrolDevicePointReqVO.setContainCompleted(null);
        List<PatrolDevicePointResVO> patrolDevicePointVO =
                patrolTaskCheckResultService.searchDevicePointByPointId(patrolDevicePointReqVO,userSession);
        if (patrolDevicePointVO != null && patrolDevicePointVO.size()>0){
            return ApiResult.success(patrolDevicePointVO.get(0));
        }
        return ApiResult.error("巡检点位不存在或已被打卡");
    }

    @ApiOperation(value = "查询巡检点位增量更新数据", notes = "查询巡检点位增量更新数据")
    @RequestMapping(value = "searchPatrolRefresh", method = RequestMethod.POST)
    public ApiResult searchPatrolRefresh(HttpServletRequest req, @RequestBody PatrolDevicePointReqVO reqVO) {
        UserSession userSession = httpHelper.getUser(req);
        if (StringUtils.isEmpty(reqVO.getLastTime()) || StringUtils.isEmpty(reqVO.getNowTime()) || reqVO.getLastTime().equals(reqVO.getNowTime())) {
            PatrolRefreshResVO resVO =patrolTaskCheckResultService.getPatrolStatistical(reqVO,userSession);
            return ApiResult.success(resVO);
        }

        PatrolRefreshResVO resVO = patrolTaskCheckResultService.searchPatrolRefresh(reqVO,userSession);
        return ApiResult.success(resVO);
    }

    /**
     *  获取所有数据
     *
     * @param vo  PatrolTaskReqVO
     * @return     ApiResult
     */
    @ApiOperation(value = "获取巡检任务数据列表", notes = "获取巡检任务数据列表")
    @PostMapping(value = "/list",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getList(@Valid @RequestBody PatrolTaskQueryVO vo, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        super.checkPageParams(vo);
        if (StringUtils.isEmpty(vo.getPatrolDate()) ) {
            vo.setPatrolDate(DateHelper.formatDate(new Date(),yyyy_MM_dd));
        }
        return patrolTaskService.getList(vo,userSession);
    }


    /**----------------------------------报表开始------------------------------------------**/
    @ApiOperation(value = "巡检统计", notes = "巡检统计")
    @RequestMapping(value = "/getPatrolTable", method = RequestMethod.POST)
    public ApiResult getPoTable(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        List<AppPortalTaskTableRepVO> appPortalTaskTableRepVOList = patrolTaskService.getPoTable(appTicketCharReqVO);
        return ApiResult.success(appPortalTaskTableRepVOList);
    }



    /**---------------------------------报表结束----------------------------------------**/


}
