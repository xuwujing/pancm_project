package com.zans.mms.controller.applet;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.exception.BusinessException;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.BaseOrgMapper;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.model.Ticket;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IDevicePointService;
import com.zans.mms.service.ITicketService;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.common.TreeSelect;
import com.zans.mms.vo.devicepoint.DevicePointAddReqVO;
import com.zans.mms.vo.ticket.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.mms.config.MMSConstants.*;
import static com.zans.mms.config.TicketConstants.*;


/**
 * 工单表(Tickets)表控制层
 *
 * @author beixing
 * @since 2021-01-13 18:14:24
 */
@Api(tags = "工单管理(app)")
@RestController
@RequestMapping("app/ticket")
public class AppTicketController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private ITicketService ticketService;

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private IConstantItemService constantItemService;

    @Autowired
    private BaseOrgMapper baseOrgMapper;

    @Autowired
    private IDevicePointService devicePointService;

    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;


    /**  ---------------------------- 工单 start   ---------------------------------*/



    @ApiOperation(value = "工单初始化", notes = "工单初始化")
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public ApiResult init() {
        List<SelectVO> ticketStatusList = constantItemService.findItemsByDict(MODULE_TICKET_STATUS);
        List<SelectVO> ticketDispatchStatusList = constantItemService.findItemsByDict(MODULE_TICKETS_DISPATCH_STATUS);
        List<SelectVO> ticketAcceptStatusList = constantItemService.findItemsByDict(MODULE_TICKETS_ACCEPT_STATUS);
        List<SelectVO> ticketMaintenanceStatusList = constantItemService.findItemsByDict(MODULE_TICKETS_MAINTENANCE_STATUS);
        List<SelectVO> ticketTypeList = constantItemService.findItemsByDict(MODULE_TICKET_TYPE);
        List<SelectVO> ticketIssueSourceList = constantItemService.findItemsByDict(MODULE_ISSUE_SOURCE);
        List<SelectVO> ticketIssueTypeList = constantItemService.findItemsByDict(MODULE_ISSUE_TYPE);
        List<SelectVO> ticketIssueLevelList = constantItemService.findItemsByDict(MODULE_ISSUE_LEVEL);
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        List<SelectVO> baseOrg =  baseOrgMapper.queryBaseOrg();
        List<SelectVO> dealWayList = constantItemService.findItemsByDict(DEAL_WAY);
        List<TreeSelect> issueSourceTree  = constantItemService.findIssueSourceTree(MODULE_ISSUE_SOURCE);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_TICKET_STATUS,ticketStatusList)
                .put(MODULE_TICKETS_DISPATCH_STATUS,ticketDispatchStatusList)
                .put(MODULE_TICKETS_ACCEPT_STATUS,ticketAcceptStatusList)
                .put(MODULE_TICKETS_MAINTENANCE_STATUS,ticketMaintenanceStatusList)
                .put(MODULE_TICKET_TYPE,ticketTypeList)
                .put(MODULE_ISSUE_SOURCE,ticketIssueSourceList)
                .put(MODULE_ISSUE_TYPE,ticketIssueTypeList)
                .put(MODULE_ISSUE_LEVEL,ticketIssueLevelList)
                .put(MODULE_TICKET_OP,ticketsOpStatusList)
                .put(MODULE_BASE_ORG,baseOrg)
                .put(DEAL_WAY,dealWayList)
                .put("issueSourceTree",issueSourceTree)
                .build();
        return ApiResult.success(result);
    }


    /**
     * 分页查询
     */
    @ApiOperation(value = "工单查询", notes = "工单表查询")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult list(@RequestBody TicketSearchReqVO ticketSearchReqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        ticketSearchReqVO.setCreator(userSession.getUserName());
        return ticketService.appList(ticketSearchReqVO,userSession);
    }


    /**
     * 列表筛选条件
     */
    @ApiOperation(value = "工单查询初始化", notes = "工单查询初始化")
    @RequestMapping(value = "listInit", method = RequestMethod.GET)
    public ApiResult listInit( HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        return ticketService.appListInit(userSession.getUserName());
    }






    /**
     * 工单详情查询
     */
    @ApiOperation(value = "工单表详情", notes = "工单表详情查询")
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ApiResult view(@RequestParam("id") Long id) {
        return ticketService.appView(id);
    }


    /**
     * 工单打卡
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "工单打卡", notes = "工单打卡")
    @RequestMapping(value = "clockIn", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_CLOCK_IN,opPlatform = LOG_APP)
    public ApiResult clockIn(@RequestBody @Validated TicketReportReqVO tickets, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        setDefault(tickets, httpRequest);
        tickets.setOpType(APP_OP_TYPE);
        return ticketService.appClockIn(tickets,userSession);
    }


    /**
     * 工单维修上报
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "工单维修上报", notes = "工单维修上报")
    @RequestMapping(value = "report", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_EDIT,opPlatform = LOG_APP)
    public ApiResult report(@RequestBody @Validated TicketReportReqVO tickets, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        setDefault(tickets, httpRequest);
        tickets.setOpType(APP_OP_TYPE);

        // todo 工单维修上报，创建app维修上报流程，如果维修状态是已完成，那么该流程创建及结束


        return ticketService.appMaintenanceStatusReport(tickets,userSession);
    }


    @ApiOperation(value = "工单创建", notes = "工单创建")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_SAVE,opPlatform = LOG_APP)
    public ApiResult save(@RequestBody AppTicketSaveReqVO ticketSaveReqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        ticketSaveReqVO.setCreator(userSession.getUserName());
        return ticketService.appSaveTicket(ticketSaveReqVO,userSession);
    }

    /**
     *
     * 地图创建新增
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "新增", notes = "新增")
    @RequestMapping(value = "pointSave", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_SAVE,opPlatform = LOG_APP)
    public ApiResult<DevicePoint> insert(@RequestBody DevicePointAddReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        reqVO.setCreator(userSession.getUserName());
        int result = devicePointService.insertDevicePoint(reqVO);
        if (result > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",reqVO.getId());
            return ApiResult.success(jsonObject);
        }
        return ApiResult.error("新增失败");
    }

    private void setDefault(TicketReportReqVO tickets, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        tickets.setCreator(userSession.getUserName());
        tickets.setOpIpaddr(super.getIpAddress(httpRequest));
        tickets.setOpPlatform(APP);
    }




// ------------------------------------------------监理、业主（待审批列表、已审批列表、全部列表）start------------------------

    private void setDefault(TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        tickets.setCreator(userSession.getUserName());
        tickets.setOpIpaddr(super.getIpAddress(httpRequest));
        tickets.setOpPlatform(APP);
    }

 /*   *//**
     * 待审批、审批列表
     *//*
    @ApiOperation(value = "待审批、审批列表", notes = "待审批、审批列表")
    @RequestMapping(value = "myPendingAndApprovalTicketList", method = RequestMethod.POST)
    public ApiResult myPendingAndApprovalTicketList(@RequestBody AppPendingApprovalReqVO reqVO, HttpServletRequest httpRequest) {
        super.checkPageParams(reqVO);
        UserSession userSession = httpHelper.getUser(httpRequest);
        return ticketService.myPendingAndApprovalTicketList(reqVO,userSession);
    }*/


    /**
     * 新版待审批接口 开工日期：2021/10/14 15：17 修改内容 添加过滤项，添加筛选项
     * 待审批、审批列表
     */
    @ApiOperation(value = "待审批、审批列表", notes = "待审批、审批列表")
    @RequestMapping(value = "myPendingAndApprovalTicketList", method = RequestMethod.POST)
    public ApiResult myPendingAndApprovalTicketList(@RequestBody AppPendingApprovalReqVO reqVO, HttpServletRequest httpRequest) {
        super.checkPageParams(reqVO);
        UserSession userSession = httpHelper.getUser(httpRequest);
        if(StringHelper.isEmpty(userSession)||StringHelper.isEmpty(userSession.getUserName())){
            return ApiResult.error("请先登录!");
        }
        return ticketService.myPendingAndApprovalTicketList(reqVO,userSession);
    }


    /**
     * 派工单详情查询
     */
    @ApiOperation(value = "派工单详情", notes = "派工单详情查询")
    @RequestMapping(value = "dispatch/view", method = RequestMethod.GET)
    public ApiResult viewDispatch(@RequestParam("id") Long id) {
        return ticketService.viewDispatch(id);
    }

    /**
     * 派工单审批
     */
    @ApiOperation(value = "派工单审批", notes = "派工单审批")
    @RequestMapping(value = "dispatch/approval", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_DISPATCH, operation = LOG_OPERATION_VERIFY)
    public ApiResult<Ticket> approvalDispatch(@RequestBody TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(DISPATCH_OP_TYPE);
        return ticketService.circulationDispatch(tickets);
    }

    /**
     * 修改一条数据
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "派工单项目编辑", notes = "派工单项目编辑")
    @RequestMapping(value = "dispatch/edit", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_DISPATCH, operation = LOG_OPERATION_EDIT)
    public ApiResult editDispatch(@RequestBody @Validated TicketDispatchEditVO tickets, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        tickets.setCreator(userSession.getUserName());
        tickets.setOpIpaddr(super.getIpAddress(httpRequest));
        tickets.setOpPlatform(APP);
        tickets.setOpType(DISPATCH_OP_TYPE);
        tickets.setDispatchStatus(DISPATCH_STATUS_EDIT);
        return ticketService.editDispatch(tickets);
    }



    /**
     * 验收单详情查询
     */
    @ApiOperation(value = "验收单详情", notes = "验收单详情查询")
    @RequestMapping(value = "accept/view", method = RequestMethod.GET)
    public ApiResult viewAccept(@RequestParam("id") Long id) {
        return ticketService.viewAccept(id);
    }

    /**
     * 验收单审批
     */
    @ApiOperation(value = "验收单审批", notes = "验收单审批")
    @RequestMapping(value = "accept/approval", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_ACCEPT, operation = LOG_OPERATION_VERIFY)
    public ApiResult<Ticket> approvalAccept(@RequestBody TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(ACCEPT_OP_TYPE);
        return ticketService.circulationAcceptance(tickets);
    }

    /**
     * 修改一条数据
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "验收单项目编辑", notes = "验收单项目编辑")
    @RequestMapping(value = "accept/edit", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_ACCEPT, operation = LOG_OPERATION_EDIT)
    public ApiResult editAccept(@RequestBody @Validated TicketAcceptEditVO tickets, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        tickets.setCreator(userSession.getUserName());
        tickets.setOpIpaddr(super.getIpAddress(httpRequest));
        tickets.setOpPlatform(APP);
        tickets.setOpType(ACCEPT_OP_TYPE);
        int acceptStatus = ACCEPT_STATUS_EDIT;
        if (tickets.getUserRoleType() != null && tickets.getUserRoleType() == SUPERVISION_ROLE_TYPE) {
            acceptStatus = ACCEPT_STATUS_SUP_CHECK;
        }
        tickets.setAcceptStatus(acceptStatus);
        return ticketService.editAccept(tickets);
    }


// ------------------------------------------------监理、业主（待审批列表、已审批列表、全部列表）end--------------------------






}
