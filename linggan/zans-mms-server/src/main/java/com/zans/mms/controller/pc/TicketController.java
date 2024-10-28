package com.zans.mms.controller.pc;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.exception.BusinessException;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.util.*;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.dao.DevicePointMapper;
import com.zans.mms.dao.TicketDao;
import com.zans.mms.dao.WorkflowTaskInfoMapper;
import com.zans.mms.model.*;
import com.zans.mms.service.*;
import com.zans.mms.service.impl.BaseOrgServiceImpl;
import com.zans.mms.vo.basemaintaionfacility.BaseMaintaionFacilityTicketReqVO;
import com.zans.mms.vo.common.TreeSelect;
import com.zans.mms.vo.devicepoint.DevicePointAddReqVO;
import com.zans.mms.vo.devicepoint.map.DevicePointMapQueryVO;
import com.zans.mms.vo.devicepoint.map.TicketForMapResVO;
import com.zans.mms.vo.ticket.*;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.ApiInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MAX_SIZE_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_MIME_ERROR;
import static com.zans.base.config.GlobalConstants.UPLOAD_FILE_MAX_SIZE;
import static com.zans.mms.config.MMSConstants.*;
import static com.zans.mms.config.TicketConstants.*;
import static com.zans.mms.config.TicketConstants.DEAL_WAY;

/**
 * 工单表(Tickets)表控制层
 *
 * @author beixing
 * @since 2021-01-13 18:14:24
 */
@Api(tags = "工单管理")
@Slf4j
@RestController
@RequestMapping("ticket")
public class TicketController extends BaseController {
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
    private BaseOrgServiceImpl baseOrgService;


    @Autowired
    private IDevicePointService devicePointService;

    @Autowired
    private IBaseMaintaionFacilityService baseMaintaionFacilityService;

    @Resource
    private DevicePointMapper devicePointMapper;

    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Resource
    private TicketDao ticketDao;


    @Autowired
    IFileService fileService;


    /**
     * ---------------------------- 工单 start   ---------------------------------
     */


    @ApiOperation(value = "工单初始化", notes = "工单初始化")
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public ApiResult init(HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        List<SelectVO> ticketStatusList = constantItemService.findItemsByDict(MODULE_TICKET_STATUS);
        List<SelectVO> ticketDispatchStatusList = constantItemService.findItemsByDict(MODULE_TICKETS_DISPATCH_STATUS);
        List<SelectVO> ticketAcceptStatusList = constantItemService.findItemsByDict(MODULE_TICKETS_ACCEPT_STATUS);
        List<SelectVO> ticketTypeList = constantItemService.findItemsByDict(MODULE_TICKET_TYPE);
        List<SelectVO> ticketIssueSourceList = constantItemService.findItemsByDict(MODULE_ISSUE_SOURCE);

        List<TreeSelect> issueSourceTree  = constantItemService.findIssueSourceTree(MODULE_ISSUE_SOURCE);

        List<SelectVO> ticketIssueTypeList = constantItemService.findItemsByDict(MODULE_ISSUE_TYPE);
        List<SelectVO> ticketIssueLevelList = constantItemService.findItemsByDict(MODULE_ISSUE_LEVEL);
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        List<SelectVO> baseOrg = baseOrgService.queryBaseOrg();
        List<BaseOrg> baseOrgInfoList = baseOrgService.queryBaseOrgInfo();
        List<SelectVO> dispatchInit = ticketService.getDispatchInit();
        List<SelectVO> acceptanceInit = ticketService.getAcceptanceInit();
        List<SelectVO> dealWayList = constantItemService.findItemsByDict(DEAL_WAY);
        int tsCount = 0;
        if (userSession != null) {
            tsCount = ticketService.queryDraftByUser(userSession.getUserName());
        }
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_TICKET_STATUS, ticketStatusList)
                .put(MODULE_TICKETS_DISPATCH_STATUS, ticketDispatchStatusList)
                .put(MODULE_TICKETS_ACCEPT_STATUS, ticketAcceptStatusList)
                .put(MODULE_TICKET_TYPE, ticketTypeList)
                .put(MODULE_ISSUE_SOURCE, ticketIssueSourceList)
                .put(MODULE_ISSUE_TYPE, ticketIssueTypeList)
                .put(MODULE_ISSUE_LEVEL, ticketIssueLevelList)
                .put(MODULE_TICKET_OP, ticketsOpStatusList)
                .put(MODULE_BASE_ORG, baseOrg)
                .put(MODULE_TICKET_TS, tsCount)
                .put("dispatch",dispatchInit)
                .put("acceptance",acceptanceInit)
                .put("base_org_info",baseOrgInfoList)
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
        return ticketService.ticketList(ticketSearchReqVO, userSession);
    }


    /**
     * 地图创建新增
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "新增", notes = "新增")
    @RequestMapping(value = "pointSave", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_SAVE)
    public ApiResult<DevicePoint> insert(@RequestBody DevicePointAddReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        reqVO.setCreator(userSession.getUserName());
        int result = devicePointService.insertDevicePoint(reqVO);
        if (result > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", reqVO.getId());
            return ApiResult.success(jsonObject);
        }
        return ApiResult.error("新增失败");
    }


    @ApiOperation(value = "工单创建", notes = "工单创建")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_CREATE)
    public ApiResult create(@RequestBody TicketSaveReqVO ticketSaveReqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        ticketSaveReqVO.setCreator(userSession.getUserName());
        return ticketService.ticketCreate(ticketSaveReqVO);
    }

    @ApiOperation(value = "工单提交", notes = "工单提交")
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET, operation = LOG_OPERATION_SAVE)
    public ApiResult submit(@RequestBody TicketSaveReqVO ticketSaveReqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        ticketSaveReqVO.setCreator(userSession.getUserName());
        return ticketService.ticketSubmit(ticketSaveReqVO);
    }


    @ApiOperation(value = "工单添加点位", notes = "工单添加点位")
    @RequestMapping(value = "point/add", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_ADD_POINT)
    public ApiResult pointAdd(@RequestBody TicketSaveReqVO ticketSaveReqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        ticketSaveReqVO.setCreator(userSession.getUserName());
        return ticketService.ticketAddPoint(ticketSaveReqVO);
    }


    @ApiOperation(value = "工单删除点位", notes = "工单删除点位")
    @RequestMapping(value = "point/del", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_DEL_POINT)
    public ApiResult pointDel(@RequestBody TicketSaveReqVO ticketSaveReqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        ticketSaveReqVO.setCreator(userSession.getUserName());
        return ticketService.ticketDelPoint(ticketSaveReqVO);
    }


    @ApiOperation(value = "工单删除", notes = "工单删除")
    @RequestMapping(value = "del", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET, operation = LOG_OPERATION_DELETE)
    public ApiResult del(@RequestBody TicketSaveReqVO ticketSaveReqVO, HttpServletRequest httpRequest) {
        return ticketService.deleteById(ticketSaveReqVO.getId());
    }

    /**
     * 工单详情查询
     */
    @ApiOperation(value = "工单表详情", notes = "工单表详情查询")
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ApiResult view(@RequestParam("id") Long id) {
        return ticketService.ticketView(id);
    }


    @ApiOperation(value = "派工单详情编辑", notes = "派工单详情编辑")
    @RequestMapping(value = "viewEdit", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET, operation = LOG_OPERATION_EDIT)
    public ApiResult viewEdit(@RequestBody @Validated TicketViewEditVO tickets, HttpServletRequest httpRequest) {
        return ticketService.viewEdit(tickets);
    }

    /**
     * 工单添加评论
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "工单添加评论", notes = "工单添加评论")
    @RequestMapping(value = "addLogs", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET, operation = LOG_OPERATION_ADD_LOGS)
    public ApiResult addLogs(@RequestBody @Validated TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(TICKET_OP_TYPE);
        return ticketService.saveLogs(tickets);
    }


    /**
     * 工单分配
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "工单分配", notes = "工单分配")
    @RequestMapping(value = "assign", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET, operation = LOG_OPERATION_EDIT)
    public ApiResult assign(@RequestBody TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setTicketStatus(TICKET_STATUS_START);
        tickets.setOpType(TICKET_OP_TYPE);
        return ticketService.assign(tickets);
    }

    /**
     * 工单验收完成
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "工单验收", notes = "工单验收")
    @RequestMapping(value = "succeed", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET, operation = LOG_OPERATION_VERIFY)
    public ApiResult succeed(@RequestBody @Validated TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setTicketStatus(TICKET_STATUS_SUC);
        tickets.setOpType(TICKET_OP_TYPE);
        return ticketService.succeed(tickets);
    }

    /**
     * 获取工单价格数据列表
     *
     * @param vo BaseMaintaionFacilityReqVO
     * @return ApiResult
     */
    @ApiOperation(value = "获取工单价格数据列表", notes = "获取工单数据列表")
    @PostMapping(value = "baseMaintaionFacility/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getTicketList(@Valid @RequestBody BaseMaintaionFacilityTicketReqVO vo) {
        super.checkPageParams(vo);
        return baseMaintaionFacilityService.getTicketList(vo);
    }

    /**  ---------------------------- 工单 end   ---------------------------------*/


    /**  ------------------------   ---- 派工单 start   ---------------------------------*/

    /**
     * 派工单详情查询
     */
    @ApiOperation(value = "派工单详情", notes = "派工单详情查询")
    @RequestMapping(value = "dispatch/view", method = RequestMethod.GET)
    public ApiResult viewDispatch(@RequestParam("id") Long id) {
        return ticketService.viewDispatch(id);
    }


    /**
     * 添加派工单
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "派工单增加", notes = "派工单增加")
    @RequestMapping(value = "dispatch/add", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_DISPATCH, operation = LOG_OPERATION_SAVE)
    public ApiResult addDispatch(@RequestBody @Validated TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(TICKET_OP_TYPE);
        tickets.setDispatchStatus(DISPATCH_STATUS_EDIT);
        tickets.setAcceptStatus(ACCEPT_STATUS_EDIT);
        tickets.setIsCost(IS_COST_YES);
        return ticketService.addDispatch(tickets);
    }


    /**
     * 删除派工单
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "派工单删除", notes = "派工单删除")
    @RequestMapping(value = "dispatch/del", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_DISPATCH, operation = LOG_OPERATION_DELETE)
    public ApiResult delDispatch(@RequestBody @Validated TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(TICKET_OP_TYPE);
        tickets.setDispatchStatus(TICKET_STATUS_START);
        tickets.setTicketStatus(DISPATCH_STATUS_START);
        tickets.setAcceptStatus(ACCEPT_STATUS_START);
        return ticketService.delDispatch(tickets);
    }


    /**
     * 修改一条数据
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "派工单详情编辑", notes = "派工单详情编辑")
    @RequestMapping(value = "dispatch/viewEdit", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_DISPATCH, operation = LOG_OPERATION_EDIT)
    public ApiResult viewDispatchEdit(@RequestBody @Validated TicketDispatchViewEditVO tickets, HttpServletRequest httpRequest) {
      /*  UserSession userSession = httpHelper.getUser(httpRequest);
        tickets.setCreator(userSession.getUserName());
        tickets.setOpIpaddr(super.getIpAddress(httpRequest));
        tickets.setOpPlatform(PC);
        tickets.setOpType(DISPATCH_OP_TYPE);
        tickets.setDispatchStatus(DISPATCH_STATUS_EDIT);*/
        //做ticket_code_result字段校验

        return ticketService.viewDispatchEdit(tickets);
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
        tickets.setOpPlatform(PC);
        tickets.setOpType(DISPATCH_OP_TYPE);
//        tickets.setDispatchStatus(DISPATCH_STATUS_EDIT);
        return ticketService.editDispatch(tickets);
    }


    /**
     * 工单据实结算详情查询
     */
    @ApiOperation(value = "派工单提交审核", notes = "派工单提交审核")
    @RequestMapping(value = "dispatch/submit", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_DISPATCH, operation = LOG_OPERATION_SUBMIT)
    public ApiResult submitDispatch(@RequestBody @Validated TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(DISPATCH_OP_TYPE);
        //此接口只传了一个id  由于有工单驳回后 再次发起工单的问题  在此修复此问题

        return ticketService.startDispatch(tickets);
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
     * 派工单审批
     */
    @ApiOperation(value = "派工单退回修改", notes = "派工单退回修改")
    @RequestMapping(value = "dispatch/reject", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_DISPATCH, operation = LOG_OPERATION_REJECT)
    public ApiResult<Ticket> rejectDispatch(@RequestBody TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(DISPATCH_OP_TYPE);
        tickets.setAgree(TICKET_AGREE_RETURN);
        //todo 派工单回退到初始状态
        return ticketService.circulationDispatch(tickets);
    }


    /**
     * 导出
     */
    @ApiOperation(value = "工单派工单导出", notes = "工单派工单导出")
    @RequestMapping(value = "dispatch/export", method = RequestMethod.GET)
    @Record(module = MODULE_TICKET_DISPATCH, operation = LOG_OPERATION_EXPORT)
    public void exportDispatch(@RequestParam("ticketCode") String ticketCode, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
//        String templatePath = "E:/GD2021031612250964183.pdf";
        String fileName = ticketService.exportDispatch(ticketCode);
        this.download(exportFolder + fileName, fileName, request, response);
    }


    /**  ---------------------------- 派工单 end   ---------------------------------*/


    /**  ------------------------   ---- 验收单 start   ---------------------------------*/

    /**
     * 派工单详情查询
     */
    @ApiOperation(value = "验收单详情", notes = "验收单详情查询")
    @RequestMapping(value = "accept/view", method = RequestMethod.GET)
    public ApiResult viewAccept(@RequestParam("id") Long id) {
        return ticketService.viewAccept(id);
    }


    /**
     * 修改一条数据
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "验收单详情编辑", notes = "验收详情编辑")
    @RequestMapping(value = "accept/viewEdit", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_ACCEPT, operation = LOG_OPERATION_EDIT)
    public ApiResult viewEditAccept(@RequestBody @Validated TicketAcceptViewEditVO tickets, HttpServletRequest httpRequest) {
       /* UserSession userSession = httpHelper.getUser(httpRequest);
        tickets.setCreator(userSession.getUserName());
        tickets.setOpIpaddr(super.getIpAddress(httpRequest));
        tickets.setOpPlatform(PC);
        tickets.setOpType(ACCEPT_OP_TYPE);
        tickets.setAcceptStatus(ACCEPT_STATUS_EDIT);*/
        return ticketService.viewEditAccept(tickets);
    }


    /**
     * 修改一条数据
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "编号验重", notes = "编号验重")
    @RequestMapping(value = "checkCode", method = RequestMethod.POST)
    public ApiResult checkCode(@RequestBody @Validated TicketAcceptViewEditVO tickets) {
        return ticketService.checkCode(tickets);
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
        tickets.setOpPlatform(PC);
        tickets.setOpType(ACCEPT_OP_TYPE);
//        int acceptStatus = ACCEPT_STATUS_EDIT;
//        if (tickets.getUserRoleType() != null && tickets.getUserRoleType() == SUPERVISION_ROLE_TYPE) {
//            acceptStatus = ACCEPT_STATUS_SUP_CHECK;
//        }
//        tickets.setAcceptStatus(acceptStatus);
        return ticketService.editAccept(tickets);
    }


    /**
     * 验收单提交审核
     */
    @ApiOperation(value = "验收单提交审核", notes = "验收单提交审核")
    @RequestMapping(value = "accept/submit", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_ACCEPT, operation = LOG_OPERATION_SUBMIT)
    public ApiResult submitAccept(@RequestBody @Validated TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(ACCEPT_OP_TYPE);

       // todo 发起验收单流程，需要工单id，然后调用发起流程方法

        return ticketService.startAccept(tickets);
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
     * 派工单审批
     */
    @ApiOperation(value = "验收单退回修改", notes = "验收单退回修改")
    @RequestMapping(value = "accept/reject", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_ACCEPT, operation = LOG_OPERATION_REJECT)
    public ApiResult<Ticket> rejectAccept(@RequestBody TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(ACCEPT_OP_TYPE);
        tickets.setAgree(TICKET_AGREE_RETURN);
        return ticketService.circulationAcceptance(tickets);
    }


    /**
     * 导出
     */
    @ApiOperation(value = "工单验收单导出", notes = "工单验收单导出")
    @RequestMapping(value = "accept/export", method = RequestMethod.GET)
    @Record(module = MODULE_TICKET_ACCEPT, operation = LOG_OPERATION_EXPORT)
    public void exportAccept(@RequestParam("ticketCode") String ticketCode, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
//        String templatePath = "/home/release/file/upload/template/pdf/派工单.pdf";
        String fileName = ticketService.exportAccept(ticketCode);
        this.download(exportFolder + fileName, fileName, request, response);

    }


    /**
     * ---------------------------- 验收单 end   ---------------------------------
     */


    private void setDefault(TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        tickets.setCreator(userSession.getUserName());
        tickets.setOpIpaddr(super.getIpAddress(httpRequest));
        tickets.setOpPlatform(PC);
    }

    /**  ------------------------------------------------------一键工单start----------------------------------------------------------------*/
    /**
     * 一键工单
     */
    @ApiOperation(value = "一键工单", notes = "一键工单")
    @RequestMapping(value = "createByAsset", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET_AUTO, operation = LOG_OPERATION_SAVE)
    public ApiResult<Ticket> createByAsset(@RequestBody TicketCreateByAssetReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        reqVO.setCreator(userSession.getUserName());
        return ticketService.createByAsset(reqVO);
    }


    /**  ------------------------------------------------------一键工单end------------------------------------------------------------------*/



    /** ------------------------------------工单删除方法---------------------------------------**/
    @ApiOperation(value = "工单删除方法", notes = "工单删除方法")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET, operation = LOG_OPERATION_DELETE)
    public ApiResult delete(@RequestBody TicketSaveReqVO ticketSaveReqVO) {
        return ticketService.delete(ticketSaveReqVO.getId());
    }




    /** -----------------------------------工单删除方法结束----------------------------------**/

    /** ----------------------------------一键导出已验收的工单的派工单验收单--------------------**/
    @ApiOperation(value = "一键导出已验收的工单", notes = "一键导出已验收的工单")
    @RequestMapping(value = "/export/ticket", method = {RequestMethod.POST})
    @Record(module = MMSConstants.MODULE_POINT,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void exportFlag(@RequestBody TicketBatchExportVO ticketBatchExport, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "派工单验收单文件导出_" + date+".zip";
        if(!StringUtils.isEmpty(ticketBatchExport.getType())){
            if(ticketBatchExport.getType().equals("dispatch")){
                fileName = "派工单文件导出_" + date+".zip";
            }
            if(ticketBatchExport.getType().equals("acceptance")){
                fileName = "验收单文件导出_" + date+".zip";
            }

        }
        String filePath=ticketService.exportTicket(ticketBatchExport);
        this.download(filePath, fileName, request, response);
    }





    /** ----------------------------------一键导出已验收的工单的派工单验收单结束--------------------**/

    /** ------------------------------------工单详情编辑开始------------------------------------**/
    @ApiOperation(value = "工单基本信息修改", notes = "工单基本信息修改")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @Record(module = MODULE_TICKET,operation = LOG_OPERATION_EDIT)
    public ApiResult update(@RequestBody Ticket ticket) {
        ticketService.update(ticket);
        return ApiResult.success();
    }





    /** ------------------------------------工单详情编辑结束------------------------------------**/



    /** ----------------------------------从工单日志中选图片开始-----------------------------------**/
    @ApiOperation(value = "获取工单日志中的图片", notes = "获取工单日志中的图片")
    @PostMapping(value = "/getTicketImg")
    public ApiResult getTicketImg(@RequestBody Ticket ticket) {
        if(null == ticket||null==ticket.getId()){
            return ApiResult.success();
        }
        List<TicketImgVO> adjunctList = ticketService.getTicketImg(ticket.getId());
        return ApiResult.success(adjunctList);
    }

    @ApiOperation(value = "获取工单日志及派工单中的图片", notes = "获取工单日志中的图片")
    @PostMapping(value = "/getTicketAndDispatchImg")
    public ApiResult getTicketAndDispatchImg(@RequestBody Ticket ticket) {
        if(null == ticket||null==ticket.getId()){
            return ApiResult.success();
        }
        List<TicketImgVO> adjunctList = ticketService.getTicketAndDispatchImg(ticket.getId());
        return ApiResult.success(adjunctList);
    }


    @ApiOperation(value = "从工单日志中选取图片", notes = "从工单日志中选取图片")
    @PostMapping(value = "/selectTicketImg")
    public ApiResult selectTicketImg(@RequestBody TicketSelectImg ticketSelectImg) {
        if(null == ticketSelectImg||ticketSelectImg.getIds()==null||ticketSelectImg.getIds().size()<1){
            return ApiResult.success();
        }
        return ticketService.selectTicketImg(ticketSelectImg);
    }


    @ApiOperation(value = "图片排序", notes = "图片排序")
    @PostMapping(value = "/ticketImgSort")
    public ApiResult ticketImgSort(@RequestBody List<TicketImgSortReqVO> ticketImgSortReqVOList) {
        if(null == ticketImgSortReqVOList||ticketImgSortReqVOList.size()==0){
            return ApiResult.success();
        }
        return ticketService.ticketImgSort(ticketImgSortReqVOList);
    }





    /** ----------------------------------从工单日志中选图片结束-----------------------------------**/



    /** ----------------------------------工单新建点位选择地图------------------------------------**/
    /**
     * 获取所有数据
     *
     * @param vo DevicePointReqVo
     * @return ApiResult<PageInfo < DevicePointResVo>>
     */
    @ApiOperation(value = "点位地图", notes = "点位地图")
    @PostMapping(value = "/map/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getMapList(@RequestBody TicketDevicePointMapQueryVO vo) {
        return ticketService.getMapList(vo);
    }



    /** ----------------------------------工单新建点位选择地图------------------------------------**/



    /** ---------------------------------派工单、验收单退回操作------------------------------------**/

    /**
     * 撤销申请
     * @param vo
     * @return
     */
    @ApiOperation(value = "工单撤销申请", notes = "工单撤销申请")
    @PostMapping(value = "/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult ticketWithdraw(@RequestBody TicketSearchReqVO vo ,HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        vo.setCreator(userSession.getUserName());
        TicketOpLogsReqVO ticketOpLogsReqVO = new TicketOpLogsReqVO();
        setDefault(ticketOpLogsReqVO,request);
        return ticketService.ticketWithdraw(vo,ticketOpLogsReqVO);
    }

    /**
     * 派工单合并
     * 传多个工单id 以第一个工单为主工单 作为信息显示数据
     * 其他部分信息做数据合并
     * @param vo
     * @return
     */
    @ApiOperation(value = "派工单合并", notes = "派工单合并")
    @PostMapping(value = "/dispatchMerge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult dispatchMerge(@RequestBody TicketDispatchMergeVO vo,HttpServletRequest request) {
        UserSession user = httpHelper.getUser(request);
        vo.setCreator(user.getUserName());
        return ticketService.dispatchMerge(vo);
    }


    /**
     * 派工单取消合并
     * 传多个工单id 以第一个工单为主工单 作为信息显示数据
     * 其他部分信息做数据合并
     * @param id
     * @return
     */
    @ApiOperation(value = "派工单取消合并", notes = "派工单取消合并")
    @GetMapping(value = "/unDispatchMerge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult unDispatchMerge(@RequestParam("id") Long id) {
        if(null == id){
            return ApiResult.error("传参id为空！");
        }
        Ticket ticket = ticketDao.getTicket(id);
        if(null ==ticket.getIsMerge() || 1!=ticket.getIsMerge()){
            throw new BusinessException("此工单不是一个合并工单");
        }
        return ticketService.unDispatchMerge(id);
    }



    /**
     * 合并后子工单 单个取消合并
     * @param id
     * @return
     */
    @ApiOperation(value = "单个取消合并", notes = "单个取消合并")
    @GetMapping(value = "/unDispatchMergeOne", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult unDispatchMergeOne(@RequestParam("id") Long id) {
        if(null == id){
            return ApiResult.error("传参id为空！");
        }
        Ticket ticket = ticketDao.getTicket(id);
        if(null ==ticket.getPid()){
            throw new BusinessException("此工单不是一个子工单");
        }

        return ticketService.unDispatchMergeOne(id);
    }



    /** -------------------------------派工单、验收单退回操作------------------------------------**/


    /** --------------------------------工单导入开始-----------------------------------------**/
    /**
     * 资产数据导入接口 如电警、卡口、诱导屏等
     * @param file 上传的excel文件
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "/upload", notes = "上传文件，导入工单数据")
    @PostMapping("/upload")
    @ResponseBody
    @Record(module = MODULE_TICKET,operation = MMSConstants.LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
        if (!ExcelHelper.checkExtension(file)) {
            return ApiResult.error(SERVER_UPLOAD_MIME_ERROR).appendMessage("不是Excel类型");
        }
        long size = file.getSize();
        if (size > UPLOAD_FILE_MAX_SIZE) {
            return ApiResult.error(SERVER_UPLOAD_MAX_SIZE_ERROR)
                    .appendMessage("最大" + FileHelper.getSizeInMb(UPLOAD_FILE_MAX_SIZE) + "MB");
        }
        // 上传文件，持久化到本地，写数据库
        String originName = file.getOriginalFilename();
        String newFileName = fileService.getNewFileName(originName);
        boolean saved = FileHelper.saveFile(file, uploadFolder, newFileName);
        if (saved){
            UserSession userSession =  httpHelper.getUser(request);
            return ticketService.batchAddTicket(newFileName, originName,userSession);
        }
        return ApiResult.error("file save error");
    }

    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile")
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "工单信息上传失败" + date+".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }

    /**
     * 资产通用导入模板下载
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "/download/template", notes = "工单导入模板下载")
    @GetMapping("/download/template")
    @Record(module = MMSConstants.MODULE_ASSET,operation = MMSConstants.LOG_OPERATION_DOWNLOAD)
    public void downloadTemplate ( HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder+"template/工单导入-excel模板.xlsx";
        UserSession userSession = httpHelper.getUser(request);
        if(userSession.getOrgId().equals("10002")){
             filePath = this.uploadFolder+"template/工单导入-10002-excel模板.xlsx";
        }else if(userSession.getOrgId().equals("10003")){
            filePath = this.uploadFolder+"template/工单导入-10003-excel模板.xlsx";
        }
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "工单导入模板" + date+".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    @GetMapping("/complete")
    public ApiResult complete(@RequestParam("id") Long id){
        ticketDao.complete(id);
        return ApiResult.success();
    }

    /**
     * 批量完成
     * @param ticketSelectImg
     * @return
     */
    @PostMapping("/batchComplete")
    public ApiResult batchComplete(@RequestBody TicketSelectImg ticketSelectImg){
        if(null!=ticketSelectImg.getIds()&&ticketSelectImg.getIds().size()>0){
            ticketService.batchComplete(ticketSelectImg.getIds());
            return ApiResult.success();
        }else{
            return ApiResult.error("传参为空！");
        }


    }

    @PostMapping("completeAll")
    public ApiResult completeAll(HttpServletRequest request){
        UserSession userSession = httpHelper.getUser(request);
        ticketService.completeAll(userSession.getUserName());
        return ApiResult.success();
    }


    /**
     * 分页查询
     */
    @ApiOperation(value = "导入数据工单查询", notes = "导入数据工单查询")
    @RequestMapping(value = "importList", method = RequestMethod.POST)
    public ApiResult importList(@RequestBody TicketSearchReqVO tickets,HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        tickets.setCreator(userSession.getUserName());
        return ticketService.importTicketList(tickets);
    }

    /**
     * 分页查询
     */
    @ApiOperation(value = "导入数据校验", notes = "导入数据校验")
    @RequestMapping(value = "importCheck", method = RequestMethod.POST)
    public ApiResult importCheck(@RequestBody TicketSearchReqVO tickets,HttpServletRequest request) {
        return ticketService.importCheck(tickets);
    }

    /**
     * 粘贴项目
     */
    @ApiOperation(value = "完成上传数据", notes = "完成上传数据")
    @RequestMapping(value = "completeData", method = RequestMethod.POST)
    public ApiResult completeData(@RequestBody  TicketSearchReqVO tickets,HttpServletRequest request ) {
        return ticketService.completeData(tickets);
    }

    //TicketSearchRespVO
    /**
     * 异步修改导入数据
     */
    @ApiOperation(value = "异步修改导入数据", notes = "异步修改导入数据")
    @RequestMapping(value = "updateImport", method = RequestMethod.POST)
    public ApiResult updateImport(@RequestBody TicketSearchRespVO tickets,HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        return ticketService.updateImport(tickets,userSession);
    }

    /**
     * 获取所有点位数据
     */
    @ApiOperation(value = "所有点位数据", notes = "所有点位数据")
    @RequestMapping(value = "getAllPoint", method = RequestMethod.POST)
    public ApiResult getAllPoint() {
        return ticketService.getAllPoint();
    }

    /**
     * 根据点位获取设备
     */
    @ApiOperation(value = "根据点位获取设备", notes = "根据点位获取设备")
    @RequestMapping(value = "getAsset", method = RequestMethod.GET)
    public ApiResult getAsset(@RequestParam("pointId") Long pointId ) {
        return ticketService.getAsset(pointId);
    }
    /** -----------------------------工单导入结束-------------------------------------------**/

    /**
     * 工单添加评论
     *
     * @param tickets 实体类
     * @return Response对象
     */
    @ApiOperation(value = "工单添加评论并修改状态", notes = "工单添加评论并修改状态")
    @RequestMapping(value = "changeStatus", method = RequestMethod.POST)
    @Record(module = MODULE_TICKET, operation = LOG_OPERATION_ADD_LOGS)
    public ApiResult changeStatus(@RequestBody @Validated TicketOpLogsReqVO tickets, HttpServletRequest httpRequest) {
        setDefault(tickets, httpRequest);
        tickets.setOpType(TICKET_OP_TYPE);
        return ticketService.saveLogs1(tickets);
    }



    /** -----------------------------工单导出开始------------------------------------------**/
    @ApiOperation(value = "导出工单", notes = "导出工单")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void export(@RequestBody TicketSearchReqVO ticketSearchReqVO, HttpServletRequest request,  HttpServletResponse response ) throws Exception {
        UserSession userSession = httpHelper.getUser(request);
        ticketSearchReqVO.setCreator(userSession.getUserName());
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileName = "工单数据导出" + date+".xls";
        if(!StringHelper.isEmpty(ticketSearchReqVO.getType())){
            if("dispatch".equals(ticketSearchReqVO.getType())){
                fileName = "派工单数据导出" + date+".xls";
            }
            if("acceptance".equals(ticketSearchReqVO.getType())){
                fileName = "验收单数据导出" + date+".xls";
            }
        }
        String filePath=ticketService.exportTicketData(ticketSearchReqVO, userSession,fileName);

        this.download(filePath, fileName, request, response);
    }




    /** -----------------------------工单导出结束------------------------------------------**/

}
