package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.PermissionConstans;
import com.zans.mms.dao.guard.SysUserDao;
import com.zans.mms.dao.mms.*;
import com.zans.mms.model.*;
import com.zans.mms.service.*;
import com.zans.mms.vo.SeriesVO;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.perm.DataPermVO;
import com.zans.mms.vo.ticket.*;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.base.config.GlobalConstants.*;
import static com.zans.mms.config.TicketConstants.*;


/**
 * 工单表(Tickets)表服务实现类
 *
 * @author beixing
 * @since 2021-01-13 18:14:23
 */
@Service("ticketService")
@Slf4j
public class TicketServiceImpl implements ITicketService {

    @Resource
    private TicketDao ticketDao;

    @Resource
    private TicketPointDao ticketPointDao;

    @Resource
    private TicketPointDeviceDao ticketPointDeviceDao;

    @Resource
    private TicketDetailDao ticketDetailDao;


    @Resource
    private TicketOpLogsDao ticketOpLogsDao;

    @Resource
    private BaseFaultTypeMapper baseFaultTypeMapper;

    @Autowired
    private IConstantItemService constantItemService;


    @Autowired
    private IWeChatReqService weChatReqService;

    @Autowired
    private ISysUserService sysUserService;

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private ISerialNumService serialNumService;

    @Autowired
    private IPdfService pdfService;

    @Autowired
    private IDataPermService dataPermService;

    @Autowired
    private  IDevicePointService devicePointService;

    @Autowired
    IAssetService assetService;


    private final String TICKET_INFO = "ticket";
    private final String TICKET_POINT = "ticket_point";
    private final String TICKET_LOGS_INFO = "ticket_logs";
    private final String TICKET_LOGS_VIEW = "ticket_view";
    private final String TICKETS_DISPATCH_INFO = "ticket_dispatch";
    private final String TICKETS_DISPATCH_VIEW_INFO = "tickets_dispatch_view";
    private final String TICKETS_DISPATCH_LOGS_INFO = "tickets_dispatch_logs";
    private final String TICKETS_ACCEPT_INFO = "ticket_accept";
    private final String TICKETS_ACCEPT_VIEW_INFO = "tickets_accept_view";
    private final String TICKETS_ACCEPT_LOGS_INFO = "tickets_accept_logs";

    private static String FORMAT_MSG_PREDICT = "预算金额: %s";
    private static String FORMAT_MSG_PREDICT_COST = "预算金额: %s 核算金额: %s";
    private static String FORMAT_MSG_CLOCK = "地址: %s ";
    private static String FORMAT_MSG_REPORT = "更新状态: %s ";



    @Override
    public int queryDraftByUser(String user) {
        return ticketDao.queryDraftByUser(user);
    }

    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult ticketList(TicketSearchReqVO tickets, UserSession userSession) {
        DataPermVO dataPerm = dataPermService.getPcTicketPerm(userSession);
        tickets.setOrgId(userSession.getOrgId());
        tickets.setCreator(userSession.getUserName());
        if (!dataPerm.selectAll()) {
            for (String s : dataPerm.getDataPermList()) {
                if (Integer.parseInt(s) == PermissionConstans.PERM_ORG) {
                    //单位权限 2&dataPermValue >=2
                    tickets.setPermOrg(STATUS_ENABLE);

                } else if (Integer.parseInt(s) == PermissionConstans.PERM_SELF) {
                    tickets.setPermSelf(STATUS_ENABLE);
                    //个人权限 1&dataPermValue =1

                } else if (Integer.parseInt(s) == PermissionConstans.PERM_UNALLOCATED_TICKET) {
                    //工单未分配权限
                    tickets.setPermUnAlloc(STATUS_ENABLE);

                }
            }
            //工单权限sql注入
            setTicketPermSql(tickets, userSession);
        }
        int pageNum = tickets.getPageNum();
        int pageSize = tickets.getPageSize();
        if (StringUtils.isEmpty(tickets.getSortOrder())) {
            tickets.setSortOrder("desc");
        }
        tickets.setEditStatus(EDIT_STATUS_SUC);
        if (tickets.getTicketStatus() != null && tickets.getTicketStatus() == TICKET_STATUS_DRAFT) {
            tickets.setEditStatus(EDIT_STATUS_START);
        }
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TicketSearchRespVO> list = ticketDao.queryAll(tickets);
        return ApiResult.success(new PageResult(page.getTotal(), list, pageSize, pageNum));
    }

    /**
    * @Author beiming
    * @Description   工单权限sql注入
    * @Date  4/21/21
    * @Param
    * @return
    **/
    public void setTicketPermSql(TicketSearchReqVO tickets, UserSession userSession) {
        if ( STATUS_ENABLE.equals(tickets.getPermOrg())  || STATUS_ENABLE.equals(tickets.getPermSelf())
                || STATUS_ENABLE.equals(tickets.getPermUnAlloc())  ){
            String permSql = " and (";
            if ( STATUS_ENABLE.equals(tickets.getPermOrg())   ) {
                permSql = permSql + " t.alloc_department_num ="+"'"+userSession.getOrgId()+"' or";
            }
            if (STATUS_ENABLE.equals(tickets.getPermSelf())){
                permSql = permSql + " t.creator ="+"'"+userSession.getUserName()+"' or";
            }
            if (STATUS_ENABLE.equals(tickets.getPermUnAlloc()) ) {
                permSql = permSql + " t.ticket_status = 0";
            }
            permSql =permSql+")";
            permSql = permSql.replace("or)", ")");
            tickets.setPermSql(permSql);

        }
    }

    @Override
    public ApiResult appList(TicketSearchReqVO tickets, UserSession userSession) {
        DataPermVO dataPerm = dataPermService.getAppTicketPerm(userSession);
        tickets.setOrgId(userSession.getOrgId());
        tickets.setCreator(userSession.getUserName());
        if (!dataPerm.selectAll()) {
            for (String s : dataPerm.getDataPermList()) {
                if (Integer.parseInt(s) == PermissionConstans.PERM_ORG) {
                    //单位权限 2&dataPermValue >=2
                    tickets.setPermOrg(STATUS_ENABLE);
                } else if (Integer.parseInt(s) == PermissionConstans.PERM_SELF) {
                    tickets.setPermSelf(STATUS_ENABLE);
                    //个人权限 1&dataPermValue =1
                } else if (Integer.parseInt(s) == PermissionConstans.PERM_UNALLOCATED_TICKET) {
                    //工单未分配权限
                    tickets.setPermUnAlloc(STATUS_ENABLE);
                }
            }
            //工单权限sql注入
            setTicketPermSql(tickets, userSession);
        }
        int pageNum = tickets.getPageNum();
        int pageSize = tickets.getPageSize();
        if (StringUtils.isEmpty(tickets.getSortName())) {
            tickets.setSortName("create_time");
        }
        if (StringUtils.isEmpty(tickets.getSortOrder())) {
            tickets.setSortOrder("desc");
        }
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TicketSearchRespVO> list = ticketDao.queryAppAll(tickets);
        return ApiResult.success(new PageResult(page.getTotal(), list, pageSize, pageNum));
    }


    /**
     * @return void
     * @Author pancm
     * @Description 工单创建推送
     * @Date 2021/3/11
     * @Param [ticketSaveReqVO, ticket]
     **/
    private void ticketCreateMsgWeChatPush(TicketSaveReqVO ticketSaveReqVO, Ticket ticket) {
        Map<Object, String> map = constantItemService.findItemsMapByDict(MODULE_TICKET_TYPE);
        String creator = ticketSaveReqVO.getCreator();
        String typeName = map.get(ticket.getTicketType());
        SysUser sysUser = sysUserDao.queryByIdOrUsername(null, creator);
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        weChatPushReqVO.setCreator(creator);
        weChatPushReqVO.setTemplateType(TICKET_TEMPLATE_CREATE);
        weChatPushReqVO.setRoleNum(INSIDE_ROLE);
        List<String> keywords = new ArrayList<>();
        keywords.add(ticket.getTicketCode());
        keywords.add("新建工单");
        keywords.add(typeName);
        keywords.add(sysUser.getNickName());
        keywords.add(DateHelper.getNow());
        weChatPushReqVO.setKeywords(keywords);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickName", sysUser.getNickName());
        jsonObject.put("orgName", sysUser.getMaintainName());
        jsonObject.put("issueName", typeName);
        weChatPushReqVO.setJsonObject(jsonObject);
        weChatReqService.weChatPush(weChatPushReqVO);
    }


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ticketSaveReqVO 实例对象
     * @return 对象列表
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiResult ticketSubmit(TicketSaveReqVO ticketSaveReqVO) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketSaveReqVO, ticket);
        //如果是零星工程，默认则创建派工/验收单  并启用核算
        if (ticket.getTicketType() != null && ticket.getTicketType() == TICKET_TYPE_SPORADIC_STATUS) {
            ticket.setIsCost(IS_COST_YES);
            ticket.setDispatchStatus(DISPATCH_STATUS_EDIT);
            ticket.setAcceptStatus(ACCEPT_STATUS_EDIT);
        }
        ticketDao.update(ticket);
        //如果是提交则进行消息推送
        if (ticket.getTicketType() != null && ticket.getEditStatus() == EDIT_STATUS_SUC) {
            String ticketCode = ticketSaveReqVO.getTicketCode();
            ticket.setTicketCode(ticketCode);
            ticketCreateMsgWeChatPush(ticketSaveReqVO, ticket);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult ticketAddPoint(TicketSaveReqVO ticketSaveReqVO) {
        List<TicketPoint> ticketPoints = ticketSaveReqVO.getTicketPoints();
        if (StringHelper.isEmpty(ticketPoints)) {
            return ApiResult.error("请选择点位!");
        }
        if (StringHelper.isEmpty(ticketSaveReqVO.getId())) {
            return ApiResult.error("该工单未被创建或已被删除!");
        }
        long id = ticketSaveReqVO.getId();
        ticketPoints.forEach(ticketPoint -> {
            ticketPoint.setTicketId(id);
            ticketPoint.setCreator(ticketSaveReqVO.getCreator());
        });
        ticketPointDao.insertBatch(ticketPoints);
        List<TicketPointDevice> ticketPointDevices = ticketSaveReqVO.getTicketPointDevices();
        if (!StringHelper.isEmpty(ticketPointDevices)) {
            ticketPointDevices.forEach(ticketPointDevice -> {
                ticketPointDevice.setTicketId(id);
                ticketPointDevice.setCreator(ticketSaveReqVO.getCreator());
            });
            ticketPointDeviceDao.insertBatch(ticketPointDevices);
        }
        return ApiResult.success();
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiResult ticketDelPoint(TicketSaveReqVO tickets) {
        ticketPointDao.deleteByTicketId(tickets.getId(), tickets.getPointId());
        ticketPointDeviceDao.deleteByTicketId(tickets.getId(), tickets.getPointId());
        return ApiResult.success();
    }


    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 创建工单，返回主键ID
     * @Date 2021/3/10
     * @Param []
     **/
    @Override
    public ApiResult ticketCreate(TicketSaveReqVO ticketSaveReqVO) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketSaveReqVO, ticket);
        ticket.setTicketCode(serialNumService.generateTicketSerialNum());
        ticketDao.create(ticket);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", ticket.getId());
        jsonObject.put("ticketCode", ticket.getTicketCode());
        return ApiResult.success(jsonObject);
    }


    /**
     * 工单查看
     *
     * @param id
     */
    @Override
    public ApiResult ticketView(Long id) {
        //1.查询工单表详情
        //2.查询工单点位详情
        //3.查询工单日志详情
        TicketSearchRespVO tickets = ticketDao.queryById(id);
        if (tickets == null) {
            return ApiResult.error("该工单不存在!id：" + id);
        }
        List<TicketPointResVO> pointResVOList = ticketPointDao.queryByTicketId(id);
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(TICKET_INFO, tickets)
                .put(TICKET_POINT, pointResVOList)
                .put(TICKET_LOGS_VIEW, getLogs(id, STATUS_DISABLE, null, ticketsOpStatusList))
                .put(TICKET_LOGS_INFO, getLogs(id, STATUS_ENABLE, null, ticketsOpStatusList))
                .build();
        return ApiResult.success(result);
    }

    @Override
    public ApiResult viewEdit(TicketViewEditVO vo) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(vo, ticket);
        ticketDao.update(ticket);
        return ApiResult.success();
    }


    /**
     * 工单查看
     *
     * @param id
     */
    @Override
    public ApiResult appView(Long id) {
        //1.查询工单表详情
        //2.查询工单点位详情
        //3.查询工单日志详情
        TicketSearchRespVO tickets = ticketDao.queryById(id);
        if (tickets == null) {
            return ApiResult.error("该工单不存在!id：" + id);
        }
        TicketViewRespVO ticketViewRespVO = new TicketViewRespVO();
        BeanUtils.copyProperties(tickets, ticketViewRespVO);
        List<TicketPointResVO> pointResVOList = ticketPointDao.queryByTicketId(id);
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(TICKET_INFO, ticketViewRespVO)
                .put(TICKET_POINT, pointResVOList)
                .put(TICKET_LOGS_VIEW, getLogs(id, STATUS_DISABLE, APP_OP_TYPE, ticketsOpStatusList))
                .put(TICKET_LOGS_INFO, getLogs(id, STATUS_ENABLE, APP_OP_TYPE, ticketsOpStatusList))
                .build();
        return ApiResult.success(result);
    }


    /**
     * @return java.util.List<com.zans.mms.vo.ticket.TicketOpLogsRespVO>
     * @Author pancm
     * @Description 获取日志以及详情
     * @Date 2021/3/2
     * @Param [ticketId, type, opType, ticketsOpStatusList]
     **/
    private List<TicketOpLogsRespVO> getLogs(Long ticketId, int type, Integer opType, List<SelectVO> ticketsOpStatusList) {
        List<TicketOpLogsRespVO> logsViewList = ticketOpLogsDao.queryTicketOpLogs(ticketId, opType);
        if (StringHelper.isEmpty(logsViewList)) {
            return new ArrayList<>();
        }
        if (type == STATUS_DISABLE) {
            return logsViewList;
        }
        List<TicketOpLogsRespVO> logsList = new ArrayList<>();
        Map<Object, String> map = ticketsOpStatusList.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
        for (TicketOpLogsRespVO ticketsOpLogs : logsViewList) {
            Integer status = ticketsOpLogs.getOpCode();
            if (status != null && status > TICKET_OP_TYPE) {
                String op = map.get(status);
                String user = ticketsOpLogs.getCreator();
                if (StringUtils.isEmpty(user)) {
                    user = "";
                }
                String remark = user.concat(":").concat(op);
                ticketsOpLogs.setMsg(remark);
                logsList.add(ticketsOpLogs);
            }
        }
        return logsList;
    }


    /**
     * 新增数据
     *
     * @param tickets 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(Ticket tickets) {
        return ticketDao.insert(tickets);
    }


    @Override
    public ApiResult dispose(TicketOpLogsReqVO ticketsOpReqVO) {

        try {
            ApiResult apiResult = deal(ticketsOpReqVO);
            if (apiResult.getCode() != RESP_CODE_SUCCESS) {
                return apiResult;
            }
            Ticket tickets = new Ticket();
            BeanUtils.copyProperties(ticketsOpReqVO, tickets);
            saveLogsAndUpdateStatus(ticketsOpReqVO, tickets);
        } catch (Exception e) {
            log.error("工单状态更新失败！请求数据:{},原因是:", ticketsOpReqVO, e);
            return ApiResult.error("状态更新失败!");
        }

        return ApiResult.success();
    }


    /**
     * @return com.zans.mms.vo.tickets.TicketsOpLogsReqVO
     * @Author pancm
     * @Description 根据提供的状态进行工单状态的改变
     * @Date 2021/1/18n
     * @Param [ticketsOpReqVO]
     **/
    private ApiResult deal(TicketOpLogsReqVO ticketOpLogsReqVO) {
        TicketSearchRespVO ticketSearchRespVO = ticketDao.queryById(ticketOpLogsReqVO.getId());
        if (ticketSearchRespVO == null) {
            return ApiResult.error("工单id: " + ticketOpLogsReqVO.getId() + ",不存在或已被删除！");
        }
        SysUser sysUser = sysUserService.findUserByName(ticketOpLogsReqVO.getCreator());
        if (sysUser != null) {
            ticketOpLogsReqVO.setRoleId(sysUser.getRoleNum());
        }
        //进行操作
        Integer opType = ticketOpLogsReqVO.getOpType();
        Integer agree = ticketOpLogsReqVO.getAgree();

        Integer ticketStatus = ticketOpLogsReqVO.getTicketStatus();
        Integer dispatchStatus = ticketOpLogsReqVO.getDispatchStatus();
        Integer acceptStatus = ticketOpLogsReqVO.getAcceptStatus();
        Integer maintenanceStatus = ticketOpLogsReqVO.getMaintenanceStatus();

        Integer oldTicketStatus = ticketSearchRespVO.getTicketStatus();
        Integer oldDispatchStatus = ticketSearchRespVO.getDispatchStatus();
        Integer oldAcceptStatus = ticketSearchRespVO.getAcceptStatus();
        Integer oldMaintenanceStatus = ticketSearchRespVO.getMaintenanceStatus();


        /**
         * @Author pancm
         * @Description 工单状态变更操作流程
         * 工单状态对应关系飞书地址: https://fgr44sks34.feishu.cn/docs/doccn9EiE6t1ducebvdlKshiwac#
         *  模块锚地址:  4.2
         * @Date 2021/3/4
         *  opType= 0
         *  工单操作:
         *    1.派工单添加
         *    2.派工单删除
         *    3.派工单验收
         *
         *  opType= 1
         *  派工单操作:
         *      1.详情/项目编辑
         *      2.提交
         *      3.监理审批(同意或驳回)
         *      4.业主审批(同意或驳回)
         *      5.退回修改
         *
         *  opType= 2
         *  验工单操作：
         *      1.详情/项目编辑
         *      2.提交
         *      3.监理审批(同意或驳回)
         *      4.业主审批(同意或驳回)
         *      5.退回修改
         *  opType= 3
         *  小程序操作：
         *
         *
         * opCode 见字典 sys_constant_item表 ticket_op
         **/

        String msg = ticketOpLogsReqVO.getMsg();
        ApiResult ticketApiResult = getTicketApiResult(ticketOpLogsReqVO, opType, ticketStatus, dispatchStatus, acceptStatus, oldTicketStatus, oldDispatchStatus, oldAcceptStatus, oldMaintenanceStatus, msg);
        if (ticketApiResult != null) {
            return ticketApiResult;
        }

        ApiResult dispatchApiResult = getDispatchApiResult(ticketOpLogsReqVO, opType, agree, dispatchStatus, oldDispatchStatus, oldAcceptStatus, msg, ticketSearchRespVO);
        if (dispatchApiResult != null) {
            return dispatchApiResult;
        }

        ApiResult acceptApiResult = getAcceptApiResult(ticketOpLogsReqVO, opType, agree, acceptStatus, oldDispatchStatus, oldAcceptStatus, msg, ticketSearchRespVO);
        if (acceptApiResult != null) {
            return acceptApiResult;
        }


        ApiResult appApiResult = getAppApiResult(ticketOpLogsReqVO, opType, maintenanceStatus, oldMaintenanceStatus, msg, oldDispatchStatus, oldAcceptStatus, ticketSearchRespVO);
        if (appApiResult != null) {
            return appApiResult;
        }

        return ApiResult.success();
    }

    private ApiResult getAppApiResult(TicketOpLogsReqVO ticketOpLogsReqVO, Integer opType, Integer maintenanceStatus, Integer oldMaintenanceStatus, String msg, Integer oldDispatchStatus, Integer oldAcceptStatus, TicketSearchRespVO ticketSearchRespVO) {
        int opCode;
        if (opType == APP_OP_TYPE) {
            Integer oldTicketStatus = ticketSearchRespVO.getTicketStatus();
            final boolean isClockIn = ticketOpLogsReqVO.getIsClockIn() != null && ticketOpLogsReqVO.getIsClockIn() == STATUS_ENABLE && oldTicketStatus != null && oldTicketStatus > TICKET_STATUS_INIT;
            final boolean isMaintenance = maintenanceStatus != null && oldMaintenanceStatus < MAINTENANCE_STATUS_SUC && oldTicketStatus != null && oldTicketStatus > TICKET_STATUS_INIT;

            if (isClockIn) {
                opCode = 31;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, ticketOpLogsReqVO.getExMsg()));
                return ApiResult.success();
            }

            if (isMaintenance) {
                opCode = 3;
                //如果为添加派工单，并且该工单在小程序中完成了维修的话，工单状态则为待验收
                // 2021-04-23  亚平 北授、北星确认 后需求注释掉
///                if (ticketSearchRespVO.getTicketType() != null && ticketSearchRespVO.getTicketType() == TICKET_TYPE_ISSUE_STATUS && oldDispatchStatus != null && oldDispatchStatus == DISPATCH_STATUS_START && oldAcceptStatus != null && oldAcceptStatus == ACCEPT_STATUS_START) {
///                    ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_VERIFY);
///                }
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, ticketOpLogsReqVO.getExMsg()));
                return ApiResult.success();
            }
            return ApiResult.error("该按钮已点击或您无权限!");
        }
        return null;
    }

    private ApiResult getAcceptApiResult(TicketOpLogsReqVO ticketOpLogsReqVO, Integer opType, Integer agree, Integer acceptStatus, Integer oldDispatchStatus, Integer oldAcceptStatus, String msg, TicketSearchRespVO ticketSearchRespVO) {
        int opCode;
        if (opType == ACCEPT_OP_TYPE) {
            Double acceptPredictCost = ticketSearchRespVO.getAcceptPredictCost();
            Double acceptAdjustCost = ticketSearchRespVO.getAcceptAdjustCost();
            String roleId = ticketOpLogsReqVO.getRoleId();
            final boolean isEdit = ((isVerifyRole(roleId, INSIDE_ROLE) && acceptStatus != null && acceptStatus == ACCEPT_STATUS_EDIT) || (isVerifyRole(roleId, SUPERVISION_ROLE) && acceptStatus != null && acceptStatus == ACCEPT_STATUS_SUP_CHECK)) && agree == null && ticketOpLogsReqVO.getUserRoleType() != null;
            final boolean isSubmit = isVerifyRole(roleId, INSIDE_ROLE) && acceptStatus != null && acceptStatus == ACCEPT_STATUS_SUP_CHECK && oldAcceptStatus == ACCEPT_STATUS_EDIT && ticketOpLogsReqVO.getUserRoleType() == null;
            final boolean isAgreeApproval = isVerifyRole(roleId, SUPERVISION_ROLE) && agree != null && agree == TICKET_AGREE_YES && acceptStatus == ACCEPT_STATUS_OWNER_CHECK && oldAcceptStatus == ACCEPT_STATUS_SUP_CHECK;
            final boolean isNotAgreeApproval = isVerifyRole(roleId, SUPERVISION_ROLE) && agree != null && agree == TICKET_AGREE_NO && acceptStatus == ACCEPT_STATUS_OWNER_CHECK && oldAcceptStatus == ACCEPT_STATUS_SUP_CHECK;
            final boolean isAgreeApprovalOwner = isVerifyRole(roleId, OWNER_ROLE) && agree != null && agree == TICKET_AGREE_YES && acceptStatus == ACCEPT_STATUS_SUC && oldAcceptStatus == ACCEPT_STATUS_OWNER_CHECK;
            final boolean isNotAgreeApprovalOwner = isVerifyRole(roleId, OWNER_ROLE) && agree != null && agree == TICKET_AGREE_NO && acceptStatus == ACCEPT_STATUS_SUC && oldAcceptStatus == ACCEPT_STATUS_OWNER_CHECK;
            final boolean isReject = isVerifyRole(roleId, OWNER_ROLE) && agree != null && agree == TICKET_AGREE_RETURN && acceptStatus == ACCEPT_STATUS_EDIT && oldAcceptStatus == ACCEPT_STATUS_SUC;
            if (isEdit) {
                opCode = 22;
                ticketOpLogsReqVO.setOpCode(opCode);
                //2020-3-16 和北授确认不写日志
                ticketOpLogsReqVO.setOpType(null);
                return ApiResult.success();
            }

            if (isSubmit) {
                opCode = 23;
                ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_CHECK);
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setExMsg(String.format(FORMAT_MSG_PREDICT_COST, acceptPredictCost, acceptAdjustCost));
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, ticketOpLogsReqVO.getExMsg()));
                packageAcceptSubmitWeChatMsg(ticketOpLogsReqVO, SUPERVISION_ROLE, acceptPredictCost, acceptAdjustCost);
                return ApiResult.success();
            }

            if (isAgreeApproval) {
                opCode = 24;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                packageAcceptSubmitWeChatMsg(ticketOpLogsReqVO, OWNER_ROLE, acceptPredictCost, acceptAdjustCost);
                return ApiResult.success();
            }

            if (isNotAgreeApproval) {
                opCode = 26;
                ticketOpLogsReqVO.setAcceptStatus(ACCEPT_STATUS_EDIT);
                ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_START);
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isAgreeApprovalOwner) {
                opCode = 25;
                if (oldDispatchStatus == DISPATCH_STATUS_SUC) {
                    ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_VERIFY);
                }
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isNotAgreeApprovalOwner) {
                opCode = 27;
                ticketOpLogsReqVO.setAcceptStatus(ACCEPT_STATUS_EDIT);
                ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_START);
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isReject) {
                opCode = 29;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }
            return ApiResult.error("该按钮已点击或您无权限!");
        }
        return null;
    }

    private ApiResult getTicketApiResult(TicketOpLogsReqVO ticketOpLogsReqVO, Integer opType, Integer ticketStatus, Integer dispatchStatus, Integer acceptStatus, Integer oldTicketStatus, Integer oldDispatchStatus, Integer oldAcceptStatus, Integer oldMaintenanceStatus, String msg) {
        int opCode;
        if (opType == TICKET_OP_TYPE) {
            String roleId = ticketOpLogsReqVO.getRoleId();
            final boolean isAssign = ticketStatus != null && ticketStatus == TICKET_STATUS_START && oldTicketStatus == TICKET_STATUS_INIT;
            final boolean isAdd = isVerifyRole(roleId, INSIDE_ROLE) && dispatchStatus != null && dispatchStatus == DISPATCH_STATUS_EDIT && acceptStatus != null && acceptStatus == ACCEPT_STATUS_EDIT;
            final boolean isDel = isVerifyRole(roleId, INSIDE_ROLE) && dispatchStatus != null && dispatchStatus == DISPATCH_STATUS_START && acceptStatus != null && acceptStatus == ACCEPT_STATUS_START
                    && oldDispatchStatus == DISPATCH_STATUS_EDIT && oldAcceptStatus == ACCEPT_STATUS_EDIT;
            final boolean isSuc = isVerifyRole(roleId, INSIDE_ROLE) && ticketStatus != null && ticketStatus == TICKET_STATUS_SUC && oldTicketStatus != TICKET_STATUS_SUC && ((oldDispatchStatus == DISPATCH_STATUS_START && oldAcceptStatus == ACCEPT_STATUS_START) ||
                    (oldDispatchStatus == DISPATCH_STATUS_SUC && oldAcceptStatus == ACCEPT_STATUS_SUC ));

            if (isAssign) {
                opCode = 0;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isAdd) {
                opCode = 1;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isDel) {
                opCode = 2;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isSuc) {
                opCode = 10;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }
            return ApiResult.error("该按钮已点击或您无权限!");
        }
        return null;
    }

    private ApiResult getDispatchApiResult(TicketOpLogsReqVO ticketOpLogsReqVO, Integer opType, Integer agree, Integer dispatchStatus, Integer oldDispatchStatus, Integer oldAcceptStatus, String msg, TicketSearchRespVO ticketSearchRespVO) {
        int opCode;
        if (opType == DISPATCH_OP_TYPE) {
            Double predictCost = ticketSearchRespVO.getPredictCost();
            String roleId = ticketOpLogsReqVO.getRoleId();
            final boolean isEdit = isVerifyRole(roleId, INSIDE_ROLE) && dispatchStatus != null && dispatchStatus == DISPATCH_STATUS_EDIT && agree == null;
            final boolean isSubmit = isVerifyRole(roleId, INSIDE_ROLE) && dispatchStatus != null && dispatchStatus == DISPATCH_STATUS_SUP_CHECK && oldDispatchStatus == DISPATCH_STATUS_EDIT;
            final boolean isAgreeApproval = isVerifyRole(roleId, SUPERVISION_ROLE) && agree != null && agree == TICKET_AGREE_YES && dispatchStatus == DISPATCH_STATUS_OWNER_CHECK && oldDispatchStatus == DISPATCH_STATUS_SUP_CHECK;
            final boolean isNotAgreeApproval = isVerifyRole(roleId, SUPERVISION_ROLE) && agree != null && agree == TICKET_AGREE_NO && dispatchStatus == DISPATCH_STATUS_OWNER_CHECK && oldDispatchStatus == DISPATCH_STATUS_SUP_CHECK;
            final boolean isAgreeApprovalOwner = isVerifyRole(roleId, OWNER_ROLE) && agree != null && agree == TICKET_AGREE_YES && dispatchStatus == DISPATCH_STATUS_SUC && oldDispatchStatus == DISPATCH_STATUS_OWNER_CHECK;
            final boolean isNotAgreeApprovalOwner = isVerifyRole(roleId, OWNER_ROLE) && agree != null && agree == TICKET_AGREE_NO && dispatchStatus == DISPATCH_STATUS_SUC && oldDispatchStatus == DISPATCH_STATUS_OWNER_CHECK;
            final boolean isReject = isVerifyRole(roleId, OWNER_ROLE) && agree != null && agree == TICKET_AGREE_RETURN && dispatchStatus == DISPATCH_STATUS_EDIT && oldDispatchStatus == DISPATCH_STATUS_SUC;
            if (isEdit) {
                opCode = 12;
                ticketOpLogsReqVO.setOpCode(opCode);
                //2020-3-16 和北授确认不写日志
                ticketOpLogsReqVO.setOpType(null);
                return ApiResult.success();
            }

            if (isSubmit) {
                opCode = 13;
                ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_CHECK);
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setExMsg(String.format(FORMAT_MSG_PREDICT, predictCost));
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, ticketOpLogsReqVO.getExMsg()));
                packageDisPatchSubmitWeChatMsg(ticketOpLogsReqVO, SUPERVISION_ROLE, predictCost);
                return ApiResult.success();
            }

            if (isAgreeApproval) {
                opCode = 14;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                packageDisPatchSubmitWeChatMsg(ticketOpLogsReqVO, OWNER_ROLE, predictCost);
                return ApiResult.success();
            }

            if (isNotAgreeApproval) {
                opCode = 16;
                ticketOpLogsReqVO.setDispatchStatus(DISPATCH_STATUS_EDIT);
                ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_START);
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isAgreeApprovalOwner) {
                opCode = 15;
                if (oldAcceptStatus == ACCEPT_STATUS_SUC) {
                    ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_VERIFY);
                }
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isNotAgreeApprovalOwner) {
                opCode = 17;
                ticketOpLogsReqVO.setDispatchStatus(DISPATCH_STATUS_EDIT);
                ticketOpLogsReqVO.setTicketStatus(TICKET_STATUS_START);
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            if (isReject) {
                opCode = 19;
                ticketOpLogsReqVO.setOpCode(opCode);
                ticketOpLogsReqVO.setMsg(getMsg(msg, opCode, null));
                return ApiResult.success();
            }

            return ApiResult.error("该按钮已点击或您无权限!");
        }
        return null;
    }

    /**
     * @return boolean
     * @Author pancm
     * @Description 判断该用户是否有权限
     * @Date 2021/3/16
     * @Param [roleId, type]
     **/
    private boolean isVerifyRole(String roleId, String role) {
        /// 自测返回true，测试完成之后需要注释
        // return true;
        if (StringUtils.isEmpty(roleId)) {
            return false;
        }
        return role.equals(roleId);
    }

    /**
     * @return void
     * @Author pancm
     * @Description 组装派工单提交微信推送消息
     * @Date 2021/3/15
     * @Param [ticketOpLogsReqVO, ticketSearchRespVO, predictCost]
     **/
    private void packageDisPatchSubmitWeChatMsg(TicketOpLogsReqVO ticketOpLogsReqVO, String roleId, Double predictCost) {
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        String creator = ticketOpLogsReqVO.getCreator();
        SysUser sysUser = sysUserDao.queryByIdOrUsername(null, creator);
        weChatPushReqVO.setCreator(creator);
        weChatPushReqVO.setTemplateType(TICKET_TEMPLATE_DISPATCH_SUBMIT);
        weChatPushReqVO.setRoleNum(roleId);
        List<String> keywords = new ArrayList<>();
        keywords.add("提交工单");
        keywords.add(sysUser.getNickName());
        keywords.add(DateHelper.getNow());
        keywords.add("工单提交->派工单审批");
        weChatPushReqVO.setKeywords(keywords);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickName", sysUser.getNickName());
        jsonObject.put("orgName", sysUser.getMaintainName());
        jsonObject.put("predictCost", predictCost);
        weChatPushReqVO.setJsonObject(jsonObject);
        weChatReqService.weChatPush(weChatPushReqVO);
    }

    /**
     * @return void
     * @Author pancm
     * @Description 组装验收单提交微信推送消息
     * @Date 2021/3/15
     * @Param [ticketOpLogsReqVO, ticketSearchRespVO, predictCost]
     **/
    private void packageAcceptSubmitWeChatMsg(TicketOpLogsReqVO ticketOpLogsReqVO, String roleId, Double acceptPredictCost, Double acceptAdjustCost) {
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        String creator = ticketOpLogsReqVO.getCreator();
        SysUser sysUser = sysUserDao.queryByIdOrUsername(null, creator);
        weChatPushReqVO.setCreator(creator);
        weChatPushReqVO.setTemplateType(TICKET_TEMPLATE_ACCEPT_SUBMIT);
        weChatPushReqVO.setRoleNum(roleId);
        List<String> keywords = new ArrayList<>();
        keywords.add("提交工单");
        keywords.add(sysUser.getNickName());
        keywords.add(DateHelper.getNow());
        keywords.add("工单提交->验收单审批");
        weChatPushReqVO.setKeywords(keywords);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickName", sysUser.getNickName());
        jsonObject.put("orgName", sysUser.getMaintainName());
        jsonObject.put("acceptPredictCost", acceptPredictCost);
        jsonObject.put("acceptAdjustCost", acceptAdjustCost);
        weChatPushReqVO.setJsonObject(jsonObject);
        weChatReqService.weChatPush(weChatPushReqVO);
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 组装日志表msg内容
     * @Date 2021/3/4
     * @Param [msg, opCode, ex]
     **/
    private String getMsg(String msg, Integer opCode, String ex) {
        if (StringHelper.isEmpty(msg)) {
            msg = "";
        } else {
            msg = msg.concat(": ");
        }
        Map<Object, String> mapByDict = constantItemService.findItemsMapByDict(MODULE_TICKET_OP);
        String opValue = mapByDict.get(opCode);
        String resultMsg = msg.concat(opValue);
        if (!StringHelper.isEmpty(ex)) {
            return resultMsg.concat(" ").concat(ex);
        }
        return resultMsg;
    }


    @Override
    public ApiResult saveLogs(TicketOpLogsReqVO tickets) {
        try {
            saveLogsAndUpdateStatus(tickets, null);
        } catch (Exception e) {
            log.error("新增评论日志失败！请求数据:{},原因是:", tickets, e);
            return ApiResult.error("新增失败!");
        }
        return ApiResult.success();
    }


    @Override
    public ApiResult appClockIn(TicketReportReqVO reportReqVO, UserSession userSession) {
        TicketSearchRespVO ticketSearchRespVO = ticketDao.queryById(reportReqVO.getId());
        if (ticketSearchRespVO == null) {
            return ApiResult.error("工单id: " + reportReqVO.getId() + ",不存在或已被删除！");
        }
//        if(!StringHelper.equals(ticketSearchRespVO.getAllocDepartmentNum(),userSession.getOrgId())){
//            return ApiResult.error("工单id: " + reportReqVO.getId() + ",不允许打卡！因为非属于自己的单位！");
//        }

        Ticket tickets = new Ticket();
        TicketOpLogsReqVO ticketOpLogsReqVO = new TicketOpLogsReqVO();
        BeanUtils.copyProperties(reportReqVO, ticketOpLogsReqVO);
        if (StringHelper.isEmpty(ticketSearchRespVO.getPracticalArriveTime())) {
            tickets.setPracticalArriveTime(DateHelper.getNow());
        }
        ticketOpLogsReqVO.setGis(String.format(MSG_FORMAT_GIS, reportReqVO.getLongitude(), reportReqVO.getLatitude()));
        ticketOpLogsReqVO.setIsClockIn(STATUS_ENABLE);
//        ticketOpLogsReqVO.setExMsg(String.format(FORMAT_MSG_CLOCK, reportReqVO.getAddress()));
        ticketOpLogsReqVO.setOperGpsaddr(reportReqVO.getAddress());
        ticketOpLogsReqVO.setMaintenanceStatus(MAINTENANCE_STATUS_INIT);
        ApiResult apiResult = deal(ticketOpLogsReqVO);
        if (apiResult.getCode() != RESP_CODE_SUCCESS) {
            return apiResult;
        }
        if (StringHelper.isEmpty(ticketSearchRespVO.getMaintenanceStatus())) {
            tickets.setMaintenanceStatus(MAINTENANCE_STATUS_START);
        }
        tickets.setAdjunctId(null);
        BeanUtils.copyProperties(reportReqVO, tickets);
        saveLogsAndUpdateStatus(ticketOpLogsReqVO, tickets);
        return ApiResult.success();
    }


    /**
     * @param reportReqVO
     * @param userSession
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 工单维修上报
     * @Date 2021/3/9
     * @Param [tickets]
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiResult appMaintenanceStatusReport(TicketReportReqVO reportReqVO, UserSession userSession) {
        TicketReportReqVO clockInReportReqVO = new TicketReportReqVO();
        clockInReportReqVO.setIsClockIn(reportReqVO.getIsClockIn());
        clockInReportReqVO.setOpType(reportReqVO.getOpType());
        clockInReportReqVO.setCreator(reportReqVO.getCreator());
        clockInReportReqVO.setAddress(reportReqVO.getAddress());
        clockInReportReqVO.setAdjunctId(reportReqVO.getAdjunctId());
        clockInReportReqVO.setLatitude(reportReqVO.getLatitude());
        clockInReportReqVO.setLongitude(reportReqVO.getLongitude());
        clockInReportReqVO.setOpPlatform(reportReqVO.getOpPlatform());
        clockInReportReqVO.setOpIpaddr(reportReqVO.getOpIpaddr());
        clockInReportReqVO.setId(reportReqVO.getId());
        //如果点击打卡，则进行先进行打卡
        if (clockInReportReqVO.getIsClockIn() == STATUS_ENABLE) {
            ApiResult apiResult = appClockIn(clockInReportReqVO, userSession);
            if (apiResult.getCode() != RESP_CODE_SUCCESS) {
                return apiResult;
            }
        }
        Ticket tickets = new Ticket();
        TicketOpLogsReqVO ticketOpLogsReqVO = new TicketOpLogsReqVO();
        BeanUtils.copyProperties(reportReqVO, tickets);
        BeanUtils.copyProperties(reportReqVO, ticketOpLogsReqVO);
        ticketOpLogsReqVO.setIsClockIn(STATUS_DISABLE);
        Map<Object, String> map = constantItemService.findItemsMapByDict(MODULE_TICKETS_MAINTENANCE_STATUS);
//        ticketOpLogsReqVO.setExMsg(map.get(tickets.getMaintenanceStatus()));
        ticketOpLogsReqVO.setOperGpsaddr(reportReqVO.getAddress());
        ticketOpLogsReqVO.setMaintenanceStatus(tickets.getMaintenanceStatus());
        ApiResult apiResult = deal(ticketOpLogsReqVO);
        if (apiResult.getCode() != RESP_CODE_SUCCESS) {
            return apiResult;
        }
        if (reportReqVO.getMaintenanceStatus() == MAINTENANCE_STATUS_SUC) {
            tickets.setPracticalCompleteTime(DateHelper.getNow());
        }
        tickets.setAdjunctId(null);
        tickets.setTicketStatus(ticketOpLogsReqVO.getTicketStatus());
        saveLogsAndUpdateStatus(ticketOpLogsReqVO, tickets);
        return ApiResult.success();
    }

    /**
     * 小程序工单新增
     * 必定为故障工单，且是未分配状态
     *
     * @param appTicketSaveReqVO
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiResult appSaveTicket(AppTicketSaveReqVO appTicketSaveReqVO) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(appTicketSaveReqVO, ticket);
        ticket.setTicketCode(serialNumService.generateTicketSerialNum());
        ticket.setTicketStatus(TICKET_STATUS_INIT);
        ticket.setTicketType(TICKET_TYPE_ISSUE_STATUS);
        ticket.setIsCost(IS_COST_NO);
        ticket.setEditStatus(EDIT_STATUS_SUC);
        ticketDao.appSave(ticket);
        long id = ticket.getId();
        List<TicketPoint> ticketPoints = appTicketSaveReqVO.getTicketPoints();
        if (!StringHelper.isEmpty(ticketPoints)) {
            ticketPoints.forEach(ticketPoint -> {
                ticketPoint.setTicketId(id);
                ticketPoint.setCreator(appTicketSaveReqVO.getCreator());
            });
            ticketPointDao.insertBatch(ticketPoints);
        }
        return ApiResult.success(ticket);
    }


    /**
     * @param issueNum
     * @param user
     * @return boolean
     * @Author pancm
     * @Description 故障转工单接口
     * @Date 2021/1/14
     * @Param [issueNum, user]
     */
    @Override
    public boolean saveAndTransformTickets(String issueNum, String user) {
        //得到工单编号
        String ticketsNum = serialNumService.generateTicketSerialNum();
        log.info("用户:{},故障转换工单成功！故障编号:{},工单编号:{},ticketsProfileId:{},ticketId:{}", user, issueNum, ticketsNum);
        return true;
    }

    /**
     * @param ticketOpLogsReqVO
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 工单分配
     * @Date 2021/3/3
     * @Param [ticketOpLogsReqVO]
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiResult assign(TicketOpLogsReqVO ticketOpLogsReqVO) {
        ApiResult apiResult = deal(ticketOpLogsReqVO);
        if (apiResult.getCode() != RESP_CODE_SUCCESS) {
            return apiResult;
        }
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketOpLogsReqVO, ticket);
        ticket.setTicketStatus(TICKET_STATUS_START);
        saveLogsAndUpdateStatus(ticketOpLogsReqVO, ticket);
        return ApiResult.success();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void saveLogsAndUpdateStatus(TicketOpLogsReqVO ticketOpLogsReqVO, Ticket ticket) {
        if (ticket != null) {
            ticketDao.update(ticket);
        }
        TicketOpLogs ticketOpLogs = new TicketOpLogs();
        BeanUtils.copyProperties(ticketOpLogsReqVO, ticketOpLogs);
        ticketOpLogs.setTicketId(ticketOpLogsReqVO.getId());
        ticketOpLogs.setId(null);
        if (ticketOpLogs.getOpType() == null) {
            return;
        }
        if (StringUtils.isEmpty(ticketOpLogs.getGis())) {
            ticketOpLogsDao.insertSelective(ticketOpLogs);
            return;
        }
        ticketOpLogsDao.insert(ticketOpLogs);
    }


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public ApiResult deleteById(Long id) {
        TicketSearchRespVO ticketSearchRespVO = ticketDao.queryById(id);
        if (ticketSearchRespVO.getEditStatus() != EDIT_STATUS_START) {
            return ApiResult.success("该工单不是草稿！不允许删除!");
        }
        ticketDao.deleteById(id);
        return ApiResult.success();
    }


    @Override
    public List<CircleUnit> getPcTicketTotal(UserSession userSession) {
        DataPermVO dataPerm = dataPermService.getTopDataPerm(userSession);
        TicketSearchReqVO ticketSearchReqVO = new TicketSearchReqVO();
        ticketSearchReqVO.setDataPerm(dataPerm.getDataPerm());
        ticketSearchReqVO.setOrgId(userSession.getOrgId());
        return ticketDao.getPcTicketTotal(ticketSearchReqVO);
    }

    @Override
    public List<CircleUnit> getAppTicketTotal(UserSession userSession) {
        DataPermVO dataPerm = dataPermService.getTopDataPerm(userSession);
        TicketSearchReqVO ticketSearchReqVO = new TicketSearchReqVO();
        ticketSearchReqVO.setDataPerm(dataPerm.getDataPerm());
        ticketSearchReqVO.setOrgId(userSession.getOrgId());
        return ticketDao.getAppTicketTotal(ticketSearchReqVO);
    }

    @Override
    public List<CountUnit> getPcFaultType(UserSession userSession) {
        List<CountUnit> pcFaultType = ticketDao.getPcFaultType();
        List<String> list = null;
        if (!StringHelper.isEmpty(pcFaultType)) {
            list = pcFaultType.stream().map(CountUnit::getCountName).collect(Collectors.toList());
        }else {
            pcFaultType = new ArrayList<>();
        }
        List<CountUnit> countUnits = baseFaultTypeMapper.getDefaultFaultType(list);
        pcFaultType.addAll(countUnits);
        return pcFaultType;
    }

    @Override
    public List<CountUnit> getPcTicketSource(UserSession userSession) {
        return ticketDao.getPcTicketSource();
    }

    @Override
    public List<CircleUnit> getPcMaintainFacility(UserSession userSession) {
        return ticketDao.getPcMaintainFacility();
    }


    @Override
    public ApiResult viewDispatch(Long id) {
        TicketDispatchViewRespVO ticketsDispatchViewRespVO = ticketDao.queryTicketsDispatchViewById(id);
        if (ticketsDispatchViewRespVO == null) {
            return ApiResult.error("没有此工单号!id: " + id);
        }
        List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(id, DISPATCH_OP_TYPE);
        if (!StringHelper.isEmpty(baseMfRespVO)) {
            ticketsDispatchViewRespVO.setBaseMfRespVOList(baseMfRespVO);
        }
        TicketPoint ticketPoint = ticketPointDao.findOne(id);
        if (ticketPoint != null) {
            ticketsDispatchViewRespVO.setPointId(ticketPoint.getPointId());
        }
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(TICKETS_DISPATCH_INFO, ticketsDispatchViewRespVO)
                .put(TICKETS_DISPATCH_VIEW_INFO, getLogs(id, STATUS_DISABLE, DISPATCH_OP_TYPE, ticketsOpStatusList))
                .put(TICKETS_DISPATCH_LOGS_INFO, getLogs(id, STATUS_ENABLE, DISPATCH_OP_TYPE, ticketsOpStatusList))
                .build();
        return ApiResult.success(result);
    }


    @Override
    public ApiResult viewDispatchEdit(TicketDispatchViewEditVO ticketsDispatchViewEditVO) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketsDispatchViewEditVO, ticket);
        ticketDao.update(ticket);
        return ApiResult.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult editDispatch(TicketDispatchEditVO ticketsDispatchEditVO) {
        List<TicketBaseMfRespVO> baseMfRespVOList = ticketsDispatchEditVO.getBaseMfRespVOList();
        if (StringHelper.isEmpty(baseMfRespVOList)) {
            ticketDetailDao.deleteByTicketId(ticketsDispatchEditVO.getId(), null);
            Ticket tickets = new Ticket();
            BeanUtils.copyProperties(ticketsDispatchEditVO, tickets);
            tickets.setPredictCost(BigDecimal.ZERO.doubleValue());
            TicketOpLogsReqVO ticketsOpReqVO = new TicketOpLogsReqVO();
            BeanUtils.copyProperties(ticketsDispatchEditVO, ticketsOpReqVO);
            saveLogsAndUpdateStatus(ticketsOpReqVO, tickets);
            return ApiResult.success();
        }
        BigDecimal predictPriceTotal = BigDecimal.ZERO;
        List<TicketDetail> ticketPredictDetails = new ArrayList<>();
        List<TicketDetail> ticketAdjustDetails = new ArrayList<>();
        for (TicketBaseMfRespVO ticketBaseMfRespVO : baseMfRespVOList) {
            TicketDetail ticketPredict = new TicketDetail();
            TicketDetail ticketAdjust = new TicketDetail();
            BeanUtils.copyProperties(ticketBaseMfRespVO, ticketPredict);
            ticketPredict.setCreator(ticketsDispatchEditVO.getCreator());
            ticketPredict.setTicketId(ticketsDispatchEditVO.getId());
            ticketPredict.setFacilityId(ticketBaseMfRespVO.getId());
            ticketPredict.setType(DISPATCH_OP_TYPE);
            ticketPredict.setAdjustPrice(BigDecimal.ZERO);
            ticketPredict.setIsAdj(ticketPredict.getIsAdj() == null?0:ticketPredict.getIsAdj());
            ticketPredict.setAdjAmount(ticketPredict.getAdjAmount() == null?0:ticketPredict.getAdjAmount());
            BigDecimal bigDecimal = ticketPredict.getPredictPrice();
            predictPriceTotal = predictPriceTotal.add(bigDecimal);
            BeanUtils.copyProperties(ticketPredict, ticketAdjust);
            ticketAdjust.setType(ACCEPT_OP_TYPE);
            ticketPredictDetails.add(ticketPredict);
            ticketAdjustDetails.add(ticketAdjust);
        }

        TicketOpLogsReqVO ticketsOpReqVO = new TicketOpLogsReqVO();
        BeanUtils.copyProperties(ticketsDispatchEditVO, ticketsOpReqVO);
        ticketsOpReqVO.setExMsg(String.format(FORMAT_MSG_PREDICT, predictPriceTotal.doubleValue()));
        ApiResult apiResult = deal(ticketsOpReqVO);
        if (apiResult.getCode() != RESP_CODE_SUCCESS) {
            return apiResult;
        }
        ticketDetailDao.deleteByTicketId(ticketsDispatchEditVO.getId(), null);
        ticketDetailDao.insertBatch(ticketPredictDetails);
        ticketDetailDao.insertBatch(ticketAdjustDetails);
        Ticket tickets = new Ticket();
        BeanUtils.copyProperties(ticketsDispatchEditVO, tickets);
        tickets.setPredictCost(predictPriceTotal.doubleValue());
        saveLogsAndUpdateStatus(ticketsOpReqVO, tickets);
        return ApiResult.success();
    }


    /**
     * @param id
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 验收单详情
     * @Date 2021/1/16
     * @Param [id]
     */
    @Override
    public ApiResult viewAccept(Long id) {
        TicketAcceptViewRespVO ticketsAcceptViewRespVO = ticketDao.queryTicketsAcceptViewById(id);
        if (ticketsAcceptViewRespVO == null) {
            return ApiResult.error("没有此工单号!id: " + id);
        }
        List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(id, ACCEPT_OP_TYPE);
        if (!StringHelper.isEmpty(baseMfRespVO)) {
            ticketsAcceptViewRespVO.setBaseMfRespVOList(baseMfRespVO);
        }
        TicketPoint ticketPoint = ticketPointDao.findOne(id);
        if (ticketPoint != null) {
            ticketsAcceptViewRespVO.setPointId(ticketPoint.getPointId());
        }
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(TICKETS_ACCEPT_INFO, ticketsAcceptViewRespVO)
                .put(TICKETS_ACCEPT_VIEW_INFO, getLogs(id, STATUS_DISABLE, ACCEPT_OP_TYPE, ticketsOpStatusList))
                .put(TICKETS_ACCEPT_LOGS_INFO, getLogs(id, STATUS_ENABLE, ACCEPT_OP_TYPE, ticketsOpStatusList))
                .build();
        return ApiResult.success(result);
    }

    @Override
    public ApiResult viewEditAccept(TicketAcceptViewEditVO ticketAcceptEditVO) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketAcceptEditVO, ticket);
        ticketDao.update(ticket);
        return ApiResult.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult editAccept(TicketAcceptEditVO ticketAcceptEditVO) {
        List<TicketBaseMfRespVO> baseMfRespVOList = ticketAcceptEditVO.getBaseMfRespVOList();
        if (StringHelper.isEmpty(baseMfRespVOList)) {
            return ApiResult.error(String.format("工单ID:#%s# 项目内容不能为空！", ticketAcceptEditVO.getId()));
        }
        BigDecimal predictPriceTotal = BigDecimal.ZERO;
        BigDecimal adjustPriceTotal = BigDecimal.ZERO;
        List<TicketDetail> ticketAdjustDetails = new ArrayList<>();
        for (TicketBaseMfRespVO ticketBaseMfRespVO : baseMfRespVOList) {
            TicketDetail ticketAdjust = new TicketDetail();
            BeanUtils.copyProperties(ticketBaseMfRespVO, ticketAdjust);
            ticketAdjust.setCreator(ticketAcceptEditVO.getCreator());
            ticketAdjust.setTicketId(ticketAcceptEditVO.getId());
            ticketAdjust.setFacilityId(ticketBaseMfRespVO.getId());
            ticketAdjust.setType(ACCEPT_OP_TYPE);
            BigDecimal bigDecimal = ticketAdjust.getPredictPrice();
            predictPriceTotal = predictPriceTotal.add(bigDecimal);

            BigDecimal bigDecimal1 = ticketAdjust.getAdjustPrice();
            adjustPriceTotal = adjustPriceTotal.add(bigDecimal1);
            ticketAdjustDetails.add(ticketAdjust);
        }

        TicketOpLogsReqVO ticketsOpReqVO = new TicketOpLogsReqVO();
        BeanUtils.copyProperties(ticketAcceptEditVO, ticketsOpReqVO);
        ticketsOpReqVO.setExMsg(String.format(FORMAT_MSG_PREDICT_COST, predictPriceTotal.doubleValue(), adjustPriceTotal.doubleValue()));
        ApiResult apiResult = deal(ticketsOpReqVO);
        if (apiResult.getCode() != RESP_CODE_SUCCESS) {
            return apiResult;
        }
        ticketDetailDao.deleteByTicketId(ticketAcceptEditVO.getId(), ACCEPT_OP_TYPE);
        ticketDetailDao.insertBatch(ticketAdjustDetails);
        Ticket tickets = new Ticket();
        BeanUtils.copyProperties(ticketAcceptEditVO, tickets);
        tickets.setAcceptPredictCost(predictPriceTotal.doubleValue());
        tickets.setAcceptAdjustCost(adjustPriceTotal.doubleValue());
        saveLogsAndUpdateStatus(ticketsOpReqVO, tickets);
        return ApiResult.success();
    }


    @Override
    public String exportDispatch(String ticketCode) {
        TicketsDispatchPdfVO pdfVO = ticketDao.queryTicketsDispatchPdfByCode(ticketCode);
        List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(pdfVO.getId(), DISPATCH_OP_TYPE);
        if (!StringHelper.isEmpty(baseMfRespVO)) {
            pdfVO.setBaseMfPdfRespVOList(baseMfRespVO);
        }
        return pdfService.generateDispatchPdf(pdfVO);
    }

    @Override
    public String exportAccept(String ticketCode) {
        TicketsDispatchPdfVO pdfVO = ticketDao.queryTicketsDispatchPdfByCode(ticketCode);
        List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(pdfVO.getId(), ACCEPT_OP_TYPE);
        if (!StringHelper.isEmpty(baseMfRespVO)) {
            pdfVO.setBaseMfPdfRespVOList(baseMfRespVO);
        }
        return pdfService.generateAcceptPdf(pdfVO);
    }

    /**
     * 工单按故障类型统计,用于在图表中显示
     *
     * @return
     */
    @Override
    public ApiResult statisticsByFaultType() {
        //通过故障类型查询出来的数据列表
        List<TicketByFaultTypeVO> statisticsList = ticketDao.statisticsByFaultType();
        //折线列表
        List<String> legendList = new ArrayList<>();
        Collections.addAll(legendList, "近一月故障数", "总故障数");
        //横坐标列表
        List<String> xAxis = new ArrayList<>();
        //数据展示列表
        List<SeriesVO> seriesList = new ArrayList();
        //数据展示列表及横坐标赋值
        for (TicketByFaultTypeVO ticketByFaultTypeVO : statisticsList) {
            xAxis.add(ticketByFaultTypeVO.getFaultName());
            SeriesVO seriesVO = new SeriesVO();
            seriesVO.setName(ticketByFaultTypeVO.getFaultName());
            List<String> data = new ArrayList<>();
            data.add(ticketByFaultTypeVO.getMonthCount().toString());
            data.add(ticketByFaultTypeVO.getTotalCount().toString());
            seriesVO.setData(data);
            seriesList.add(seriesVO);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("legendList", legendList);
        map.put("xAxis", xAxis);
        map.put("seriesList", seriesList);
        return ApiResult.success(map);
    }

    /**
     * 工单数量统计
     *
     * @return
     */
    @Override
    public ApiResult ticketStatistics() {
        //获取所有的数据
        List<TicketCountVO> ticketCountVOS = ticketDao.ticketStatistics();
        //定义折线列表
        List<String> legendList = new ArrayList<>();
        //横坐标列表
        List<String> xAxis = new ArrayList<>();
        //横坐标数据填充
        Collections.addAll(xAxis, "工单总数", "近一个月工单数", "完成数量", "抵达超时工单数", "完成超时工单数", "未完成", "超过三天未完成");
        //数据展示列表
        List<SeriesVO> seriesList = new ArrayList();
        for (TicketCountVO ticketCountVO : ticketCountVOS) {
            legendList.add(ticketCountVO.getOrgName());
            SeriesVO seriesVO = new SeriesVO();
            seriesVO.setName(ticketCountVO.getOrgName());
            List<String> data = new ArrayList<>();
            data.add(ticketCountVO.getTotalCount().toString());
            data.add(ticketCountVO.getMonthCount().toString());
            data.add(ticketCountVO.getCompleteCount().toString());
            data.add(ticketCountVO.getArriveTimeCount().toString());
            data.add(ticketCountVO.getCompleteTimeCount().toString());
            data.add(ticketCountVO.getNotCompleteCount().toString());
            data.add(ticketCountVO.getOverTimeComplete().toString());
            seriesVO.setData(data);
            seriesList.add(seriesVO);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("legendList", legendList);
        map.put("xAxis", xAxis);
        map.put("seriesList", seriesList);
        return ApiResult.success(map);
    }

    /**
     * 通过设备类型统计工单
     *
     * @return
     */
    @Override
    public ApiResult statisticsTicketByDeviceType() {
        return null;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public ApiResult<Ticket> createByAsset(TicketCreateByAssetReqVO reqVO) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(reqVO, ticket);
        ticket.setTicketCode(serialNumService.generateTicketSerialNum());
        ticketDao.create(ticket);
        Long ticketId = ticket.getId();

        List<Asset> assetList = assetService.getListByCondition(new HashMap<String,Object>(){{
            put("assetIds",reqVO.getAssetIds());
        }});

        Set<TicketPoint> ticketPoints = new HashSet<>(assetList.size());
        List<TicketPointDevice> ticketPointDevices = new ArrayList<>(assetList.size());
        TicketPoint ticketPoint ;
        TicketPointDevice ticketPointDevice ;
        if (assetList != null && assetList.size() > 0) {
            for (Asset asset : assetList) {
                ticketPoint = new TicketPoint();
                ticketPoint.setCreator(reqVO.getCreator());
                ticketPoint.setTicketId(ticketId);
                ticketPoint.setPointId(asset.getPointId());
                ticketPoints.add(ticketPoint);

                ticketPointDevice = new TicketPointDevice();
                ticketPointDevice.setCreator(reqVO.getCreator());
                ticketPointDevice.setTicketId(ticketId);
                ticketPointDevice.setAssetId(asset.getId());
                ticketPointDevice.setPointId(asset.getPointId());
                ticketPointDevices.add(ticketPointDevice);

            }
            List<TicketPoint> ticketPointList = new ArrayList<>(ticketPoints);
            ticketPointDao.insertBatch(ticketPointList);
            ticketPointDeviceDao.insertBatch(ticketPointDevices);
        }
        //一键工单生成故障工单
        ticket.setTicketType(TICKET_TYPE_ISSUE_STATUS);
        //工单编辑已完成
        ticket.setEditStatus(EDIT_STATUS_SUC);
        resetPredictArriveAndCompleteTime(ticket);
        ticketDao.update(ticket);

        //要微信消息推送
        TicketSaveReqVO ticketSaveReqVO = new TicketSaveReqVO();
        ticketSaveReqVO.setCreator(reqVO.getCreator());
        ticketCreateMsgWeChatPush(ticketSaveReqVO, ticket);

        return ApiResult.success().message("一键工单成功");
    }

    /**
    * @Author beiming
    * @Description  设置预计抵达时间、预计完成时间
    * 严重 抵达30分钟 完成 3天
    * 紧急 抵达30分钟 完成3天
    * 一般 抵达60分钟 完成 3天
    * 轻微 抵达90分钟 完成3天严重
    * @Date  4/13/21
    * @Param
    * @return
    **/
    private void resetPredictArriveAndCompleteTime(Ticket ticket) {
        Integer issueLevel = ticket.getIssueLevel();
        long currentTime = System.currentTimeMillis() ;
        Date now = new Date();
        ticket.setPredictCompleteTime(DateHelper.getDateTime(DateHelper.plusDays(now,3)) );
        if (TICKET_ISSUE_LEVEL_SERIOUS.equals(issueLevel)){
           currentTime +=30*60*1000;
            Date date = new Date(currentTime);
            ticket.setPredictArriveTime(DateHelper.getDateTime(date));

        }else if (TICKET_ISSUE_LEVEL_URGENT.equals(issueLevel)){
            currentTime +=30*60*1000;
            Date date = new Date(currentTime);
            ticket.setPredictArriveTime(DateHelper.getDateTime(date));

        }else if (TICKET_ISSUE_LEVEL_COMMONLY.equals(issueLevel)){
            currentTime +=60*60*1000;
            Date date = new Date(currentTime);
            ticket.setPredictArriveTime(DateHelper.getDateTime(date));

        }else if (TICKET_ISSUE_LEVEL_SLIGHT.equals(issueLevel)) {
            currentTime +=90*60*1000;
            Date date = new Date(currentTime);
            ticket.setPredictArriveTime(DateHelper.getDateTime(date));
        }

    }
}
