package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.exception.BusinessException;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.config.PermissionConstans;
import com.zans.mms.dao.*;
import com.zans.mms.dto.workflow.*;
import com.zans.mms.model.*;
import com.zans.mms.service.*;
import com.zans.mms.util.ExcelExportUtil;
import com.zans.mms.util.FileZip;
import com.zans.mms.util.WriteErrorMsgUtil;
import com.zans.mms.vo.SeriesVO;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.devicepoint.map.DevicePointForMapResVO;
import com.zans.mms.vo.devicepoint.map.DevicePointMapQueryVO;
import com.zans.mms.vo.devicepoint.map.TicketForMapResVO;
import com.zans.mms.vo.perm.DataPermVO;
import com.zans.mms.vo.po.PoManagerRepVO;
import com.zans.mms.vo.po.PoManagerRepeatMarkVO;
import com.zans.mms.vo.ticket.*;
import com.zans.mms.vo.ticket.chart.*;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bouncycastle.util.io.pem.PemObject;
import org.snmp4j.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.base.config.GlobalConstants.*;
import static com.zans.base.util.FileHelper.createMultilayerFile;
import static com.zans.mms.config.MMSConstants.TICKET_TEMPLATE_RECORD;
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
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final String dayBegin = " 00:00:00";

    private final String dayEnd = " 23:59:59";

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
    private IDevicePointService devicePointService;

    @Autowired
    IAssetService assetService;

    @Autowired
    private ITicketWorkflowService ticketWorkflowService;

    @Autowired
    private WorkFlowServiceImpl workFlowService;

    @Resource
    private WorkflowTaskInfoMapper workflowTaskInfoMapper;

    @Value("${api.export.folder}")
    String exportFolder;

    @Autowired
    private BaseVfsDao baseVfsDao;

    @Resource
    private DevicePointMapper devicePointMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private BaseOrgServiceImpl baseOrgService;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;

    @Resource
    BaseAreaMapper baseAreaMapper;



    @Resource
    AssetMapper assetMapper;

    @Resource
    BaseDeviceTypeMapper baseDeviceTypeMapper;

    @Resource
    BaseFaultProfileMapper baseFaultProfileMapper;

    @Resource
    SysConstantItemMapper sysConstantItemMapper;

    @Autowired
    WriteErrorMsgUtil writeErrorMsgUtil;

    @Resource
    private PatrolTaskCheckResultMapper  patrolTaskCheckResultMapper;

    @Resource
    private  PoManagerDao poManagerDao;

    @Autowired
    private ExcelExportUtil excelExportUtil;

    /**********工作流常量***********************/
    /**
     * 派工单流程文件id
     */
    private final String DISPATCH_WORKFLOW_ID = "dispatch";

    /**
     * 验收单流程文件id
     */
    private final String ACCEPTANCE_WORKFLOW_ID = "acceptance";

    /**
     * 维修单流程文件id
     */
    private final String MAINTAIN_WORKFLOW_ID = "maintain";
    /**********工作流常量结束*****************/


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

    private final String TICKETS_DISPATCH_FLOW = "tickets_dispatch_flow";
    private final String TICKETS_ACCEPT_FLOW = "tickets_accept_flow";

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
        for (TicketSearchRespVO vo : list) {
            vo.setDeleteAble(0);
            if (vo.getAcceptStatus() <= 2 && vo.getDispatchStatus() <= 2 && INSIDE_ROLE.equals(userSession.getRoleId()) && 2 != vo.getTicketStatus()) {
                vo.setDeleteAble(1);
            }
            vo.setPointName(devicePointMapper.selectPointName(vo.getPointId()));
        }
        return ApiResult.success(new PageResult(page.getTotal(), list, pageSize, pageNum));
    }

    /**
     * @return
     * @Author beiming
     * @Description 工单权限sql注入
     * @Date 4/21/21
     * @Param
     **/
    public void setTicketPermSql(TicketSearchReqVO tickets, UserSession userSession) {
        if (STATUS_ENABLE.equals(tickets.getPermOrg()) || STATUS_ENABLE.equals(tickets.getPermSelf())
                || STATUS_ENABLE.equals(tickets.getPermUnAlloc())) {
            String permSql = " and (";
            if (STATUS_ENABLE.equals(tickets.getPermOrg())) {
                permSql = permSql + " t.alloc_department_num =" + "'" + userSession.getOrgId() + "' or";
            }
            if (STATUS_ENABLE.equals(tickets.getPermSelf())) {
                permSql = permSql + " t.creator =" + "'" + userSession.getUserName() + "' or";
            }
            if (STATUS_ENABLE.equals(tickets.getPermUnAlloc())) {
                permSql = permSql + " t.ticket_status = 0";
            }
            permSql = permSql + ")";
            permSql = permSql.replace("or)", ")");
            tickets.setPermSql(permSql);

        }
    }

    @Override
    public ApiResult appList(TicketSearchReqVO tickets, UserSession userSession) {
      /*  //查询之前先进行筛选条件的维护
        Screen screen = new Screen();
        String ticketStatus = null==tickets.getTicketStatus()?"":tickets.getTicketStatus().toString();
        String deviceType = StringUtils.isEmpty(tickets.getDeviceType())?"":tickets.getDeviceType();
        String sortName = StringUtils.isEmpty(tickets.getSortName())?"":tickets.getSortName();
        screen.setItems("ticketStatus,deviceType,sortName");
        screen.setItemsValues(ticketStatus+","+deviceType+","+sortName);
        screen.setType("ticket");
        screen.setCreator(userSession.getUserName());
        this.updateScreen(screen);*/
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

    private void updateScreen(Screen screen) {
        //先判断有没有对应的数据 如果没有 插入 如果有 新增
        if (ticketDao.isExist(screen) > 0) {
            ticketDao.updateScreen(screen);
        } else {
            ticketDao.insertScreen(screen);
        }
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
        weChatPushReqVO.setOrgId(ticketSaveReqVO.getAllocDepartmentNum());
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
        weChatPushReqVO.setRoleNum(OUTFIELD_ROLE);
        weChatReqService.weChatPush(weChatPushReqVO);
        weChatPushReqVO.setRoleNum(BUILD_ROLE);
        weChatReqService.weChatPush(weChatPushReqVO);
    }

    @Override
    public ApiResult appListInit(String userName) {
        Screen screen = ticketDao.getScreen(userName);
        Map<String, Object> map = new HashMap<>();
        //对item 和value进行解析
        //items items_values 如果没有对应的数据 给默认值
        if (null == screen || StringUtils.isEmpty(screen.getItems()) || StringUtils.isEmpty(screen.getItemsValues())) {
            return ApiResult.success();
        }
        String[] items = screen.getItems().split(",");
        String[] itemsValues = screen.getItemsValues().split(",");
        for (int i = 0; i < items.length; i++) {
            map.put(items[i], itemsValues[i]);
        }
        return ApiResult.success(map);


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
        //如果责任人为空的话
        if (StringUtils.isEmpty(ticketSaveReqVO.getDutyContact()) && !StringUtils.isEmpty(ticketSaveReqVO.getAllocDepartmentNum())) {
            String dutyContact = baseOrgService.queryDutyInfo(ticketSaveReqVO.getAllocDepartmentNum());
            ticket.setDutyContact(dutyContact);
        }

            String contact = baseOrgService.queryContactInfo(ticket.getAllocDepartmentNum());
            ticket.setApplyContact(contact);
            // 根据联系人 查询手机号
            String contactPhone = sysUserDao.getPhone(contact);
            if(!StringUtils.isEmpty(contactPhone)){
                ticket.setApplyPhone(contactPhone);
            }



        if(!StringUtils.isEmpty(ticket.getRemark())){
            ticket.setAcceptanceInstructions(ticket.getRemark().replaceAll("需",""));
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
        ticket.setTicketCodeResult(ticket.getTicketCode());
        ticketDao.create(ticket);
        //2021-07-16 为了防止草稿和正式工单出现相同的工单编号，此处对id和ticketCode的尾号做一次校验
        int idLength = ticket.getId().toString().length();
        int ticketCodeLength = ticket.getTicketCode().length();
        //如果ticketCode的流水尾数和id不相同 则对ticket进行再次维护
        if (!ticket.getTicketCode().substring(ticketCodeLength - idLength).equals(ticket.getId().toString())) {
            ticket.setTicketCode(ticket.getTicketCode().substring(0, ticketCodeLength - idLength) + ticket.getId());
            ticket.setTicketCodeResult(ticket.getTicketCode());
            ticketDao.update(ticket);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", ticket.getId());
        jsonObject.put("ticketCode", ticket.getTicketCode());
        jsonObject.put("ticketCodeResult", ticket.getTicketCodeResult());
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
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        if (tickets == null) {
            return ApiResult.error("该工单不存在!id：" + id);
        }
        if (null != tickets.getPid()) {
            tickets.setPCode(ticketDao.getCode(tickets.getPid()));
        }
        if (null != tickets.getIsMerge() && 1 == tickets.getIsMerge()) {
            //先查询工单下的所有子工单
            List<Ticket> ticketList = ticketDao.getByPid(tickets.getId());
            if (null != ticketList && ticketList.size() > 0) {
                tickets.setChildList(ticketList);
            }
        }
        List<TicketPointResVO> pointResVOList = ticketPointDao.queryByTicketId(id);
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
            // if (status != null && status > TICKET_OP_TYPE) {
            if (status != null) {
//                String op = map.get(status);
                String op = ticketsOpLogs.getMsg();
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


    @Override
    public ApiResult succeed(TicketOpLogsReqVO ticketOpLogsReqVO) {
        Ticket tickets = new Ticket();
        tickets.setMaintenanceStatus(20);
        BeanUtils.copyProperties(ticketOpLogsReqVO, tickets);
        ticketOpLogsReqVO.setMsg("工单#验收");
        saveLogsAndUpdateStatus(ticketOpLogsReqVO, tickets);
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
                    (oldDispatchStatus == DISPATCH_STATUS_SUC && oldAcceptStatus == ACCEPT_STATUS_SUC));

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
     * @return void
     * @Author beiming
     * @Description 组装派工单提交微信推送消息
     * @Date 2021/5/114
     * @Param [ticketOpLogsReqVO, ticketSearchRespVO, predictCost]
     **/
    private void packageDisPatchSubmitWeChatMsgWorkFlow(TicketOpLogsReqVO ticketOpLogsReqVO, List<String> unionidList, Double predictCost) {
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        String creator = ticketOpLogsReqVO.getCreator();
        //获取流程发起人
        String draft = ticketWorkflowService.getDraftByTaskId(ticketOpLogsReqVO.getTaskId());
        if (StringUtils.isEmpty(draft)) {
            draft = creator;
        }
        SysUser sysUser = sysUserDao.queryByIdOrUsername(null, draft);
        weChatPushReqVO.setCreator(draft);
        weChatPushReqVO.setTemplateType(TICKET_TEMPLATE_DISPATCH_SUBMIT);
        weChatPushReqVO.setUnionIdList(unionidList);
        List<String> keywords = new ArrayList<>();
        keywords.add("提交工单");
        keywords.add(sysUser.getNickName() + " " + sysUser.getMaintainName());
        keywords.add(DateHelper.getNow());
        /* keywords.add("工单提交->派工单审批");*/
        weChatPushReqVO.setKeywords(keywords);
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("nickName", sysUser.getNickName());
//        jsonObject.put("orgName", sysUser.getMaintainName());
        jsonObject.put("predictCost", predictCost);
        weChatPushReqVO.setJsonObject(jsonObject);
        weChatPushReqVO.setSuffix("id=" + ticketOpLogsReqVO.getId() + "&status=待审批");
        weChatPushReqVO.setType("dispatch");
        weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
    }

    /**
     * @return void
     * @Author beiming
     * @Description 组装验收单提交微信推送消息
     * @Date 2021/5/114
     * @Param [ticketOpLogsReqVO, ticketSearchRespVO, predictCost]
     **/
    private void packageAcceptSubmitWeChatMsgWorkFlow(TicketOpLogsReqVO ticketOpLogsReqVO, List<String> unionidList, Double acceptPredictCost, Double acceptAdjustCost) {
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        //获取流程发起人
        String draft = ticketWorkflowService.getDraftByTaskId(ticketOpLogsReqVO.getTaskId());
        if (StringUtils.isEmpty(draft)) {
            draft = ticketOpLogsReqVO.getCreator();
        }
        SysUser sysUser = sysUserDao.queryByIdOrUsername(null, draft);
        weChatPushReqVO.setCreator(draft);
        weChatPushReqVO.setTemplateType(TICKET_TEMPLATE_ACCEPT_SUBMIT);
        weChatPushReqVO.setUnionIdList(unionidList);
        List<String> keywords = new ArrayList<>();
        keywords.add("提交工单");
        keywords.add(sysUser.getNickName() + " " + sysUser.getMaintainName());
        keywords.add(DateHelper.getNow());
        /* keywords.add("工单提交->验收单审批");*/
        weChatPushReqVO.setKeywords(keywords);
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("nickName", sysUser.getNickName());
//        jsonObject.put("orgName", sysUser.getMaintainName());
        jsonObject.put("acceptPredictCost", acceptPredictCost);
        jsonObject.put("acceptAdjustCost", acceptAdjustCost);
        weChatPushReqVO.setJsonObject(jsonObject);
        weChatPushReqVO.setSuffix("id=" + ticketOpLogsReqVO.getId() + "&status=待审批");
        weChatPushReqVO.setType("acceptance");
        weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
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
        BeanUtils.copyProperties(reportReqVO, tickets);
        tickets.setAdjunctId(null);
        tickets.setUpdateTime(DateHelper.getNow());
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
        tickets.setCreator(null);
        BeanUtils.copyProperties(reportReqVO, ticketOpLogsReqVO);
        ticketOpLogsReqVO.setIsClockIn(STATUS_DISABLE);
//        Map<Object, String> map = constantItemService.findItemsMapByDict(MODULE_TICKETS_MAINTENANCE_STATUS);
//        ticketOpLogsReqVO.setExMsg(map.get(tickets.getMaintenanceStatus()));
        ticketOpLogsReqVO.setOperGpsaddr(reportReqVO.getAddress());
        ticketOpLogsReqVO.setMaintenanceStatus(tickets.getMaintenanceStatus());
        ApiResult apiResult = deal(ticketOpLogsReqVO);
        if (apiResult.getCode() != RESP_CODE_SUCCESS) {
            return apiResult;
        }
        if (reportReqVO.getMaintenanceStatus() == MAINTENANCE_STATUS_SUC) {
            tickets.setPracticalCompleteTime(DateHelper.getNow());
            //返回来去查询工单信息是否是据实结算 如果不是据实结算 将工单状态修改为维修完成状态
            //TicketWorkflowDto ticketWorkflowDto = getById(reportReqVO.getId().toString());
            //如果不是据实结算 直接变为维修完成状态 11/17修改
            /*if (ticketWorkflowDto.getIsCost() == 0) {*/
            ticketOpLogsReqVO.setTicketStatus(4);
            /*}*/
            String dealWay = sysConstantItemMapper.getItemValue(reportReqVO.getDealWay(),DEAL_WAY);
            ticketOpLogsReqVO.setMsg(ticketOpLogsReqVO.getMsg()+" #处理方式："+dealWay);

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
    public ApiResult appSaveTicket(AppTicketSaveReqVO appTicketSaveReqVO, UserSession userSession) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(appTicketSaveReqVO, ticket);
        ticket.setTicketCode(serialNumService.generateTicketSerialNum());
        ticket.setTicketCodeResult(ticket.getTicketCode());
        //判断当前用户的角色 如果是角色是运维单位 自动进行单位分配
        if (userSession.getRoleId().equals(INSIDE_ROLE) || userSession.getRoleId().equals(OUTFIELD_ROLE) ||userSession.getRoleId().equals(BUILD_ROLE)) {
            ticket.setTicketStatus(TICKET_STATUS_START);
            ticket.setAllocDepartmentNum(userSession.getOrgId());
            //设置故障级别
            ticket.setIssueLevel(TICKET_ISSUE_LEVEL_COMMONLY);
            //如果责任人为空的话
            if (StringUtils.isEmpty(ticket.getDutyContact()) && !StringUtils.isEmpty(ticket.getAllocDepartmentNum())) {
                String dutyContact = baseOrgService.queryDutyInfo(ticket.getAllocDepartmentNum());
                ticket.setDutyContact(dutyContact);
            }


            String contact = baseOrgService.queryContactInfo(ticket.getAllocDepartmentNum());
            ticket.setApplyContact(contact);
            // 根据联系人 查询手机号
            String contactPhone = sysUserDao.getPhone(contact);
            if(!StringUtils.isEmpty(contactPhone)){
                ticket.setApplyPhone(contactPhone);
            }

        } else {
            ticket.setTicketStatus(TICKET_STATUS_INIT);
        }
        ticket.setTicketType(TICKET_TYPE_ISSUE_STATUS);
        ticket.setIsCost(IS_COST_NO);
        ticket.setEditStatus(EDIT_STATUS_SUC);
        ticket.setOpPlatform("小程序端");
        ticket.setOccurredTime(DateHelper.getNow());
        ticketDao.appSave(ticket);
        if (userSession.getRoleId().equals(INSIDE_ROLE) || userSession.getRoleId().equals(OUTFIELD_ROLE)||userSession.getRoleId().equals(BUILD_ROLE)) {
            resetPredictArriveAndCompleteTime(ticket);
            ticketDao.update(ticket);
        }
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


    private void saveLog(TicketOpLogsReqVO ticketOpLogsReqVO) {
        TicketOpLogs ticketOpLogs = new TicketOpLogs();
        BeanUtils.copyProperties(ticketOpLogsReqVO, ticketOpLogs);
        ticketOpLogs.setTicketId(ticketOpLogsReqVO.getId());
        ticketOpLogs.setId(null);
        ticketOpLogsDao.insertSelective(ticketOpLogs);
    }


    private void updateStatus(Long id, Integer ticketStatus) {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setTicketStatus(ticketStatus);
        if (ticket != null) {
            ticketDao.update(ticket);
        }
    }

    private void checkSucAndUpdateStatus(Long id, Integer agree) {
        TicketSearchRespVO tickets = ticketDao.queryById(id);
        if (tickets.getDispatchStatus() == DISPATCH_STATUS_SUC && tickets.getAcceptStatus() == ACCEPT_STATUS_SUC) {
            updateStatus(id, TICKET_STATUS_VERIFY);
        }
        if (agree == TICKET_AGREE_RETURN) {
            updateStatus(id, TICKET_STATUS_CHECK);
        }
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


    @Transactional(rollbackFor = RuntimeException.class)
    public void saveLogsAndUpdateStatus(TicketOpLogsReqVO ticketOpLogsReqVO, Ticket ticket, Boolean flag) {
        if (flag) {
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
        List<CircleUnit> unitList = ticketDao.getAppTicketTotal(ticketSearchReqVO);
        if (isVerifyRole(userSession.getRoleId(), OWNER_ROLE) || isVerifyRole(userSession.getRoleId(), SUPERVISION_ROLE)) {
            //监理、业主（待审批）其他不变
            AppPendingApprovalReqVO appPendingApprovalReqVO = new AppPendingApprovalReqVO();
            appPendingApprovalReqVO.setAssign(userSession.getUserName());
            PageResult<AppPendingApprovalRespVO> pageResult = this.myPendingApprovalTicketList(appPendingApprovalReqVO);
            for (CircleUnit circleUnit : unitList) {
                if ("待办".equals(circleUnit.getChineName())) {
                    circleUnit.setName("pendingApproval");
                    circleUnit.setChineName("待审批");
                    circleUnit.setVal(pageResult.getTotalNum());
                    break;
                }
            }
        }
        return unitList;
    }

    @Override
    public List<CountUnit> getPcFaultType(UserSession userSession) {
        List<CountUnit> pcFaultType = ticketDao.getPcFaultType();
        List<String> list = null;
        if (!StringHelper.isEmpty(pcFaultType)) {
            list = pcFaultType.stream().map(CountUnit::getCountName).collect(Collectors.toList());
        } else {
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

        if(!StringHelper.isEmpty(baseMfRespVO)&&baseMfRespVO.size()>0&&2==ticketsDispatchViewRespVO.getDispatchStatus()){
            TicketDispatchEditVO ticketsDispatchEditVO = new TicketDispatchEditVO();
            ticketsDispatchEditVO.setId(ticketsDispatchViewRespVO.getId());
            ticketsDispatchEditVO.setBaseMfRespVOList(baseMfRespVO);
            ticketsDispatchEditVO.setCreator(ticketsDispatchViewRespVO.getCreator());
            this.editDispatch(ticketsDispatchEditVO);
            ticketsDispatchViewRespVO = ticketDao.queryTicketsDispatchViewById(id);
        }
        if (!StringHelper.isEmpty(baseMfRespVO)) {
            ticketsDispatchViewRespVO.setBaseMfRespVOList(baseMfRespVO);
        }
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        String taskId = workflowTaskInfoMapper.getByBusinessIdAndWorkflowId(id.toString(), DISPATCH_WORKFLOW_ID);
        String activityId = ticketWorkflowService.getActivityIdByTaskId(taskId);
        List<String> assigneeList = new ArrayList<>();
        if (!StringUtils.isEmpty(taskId)) {
            //通过任务id查询是否存在唯一待办人
            String assignee = ticketWorkflowService.getAssigneeByTaskId(taskId);
            //初始化待办人用户名列表
            if (!StringUtils.isEmpty(assignee)) {
                assigneeList.add(assignee);
            } else {
                assigneeList = workflowTaskInfoMapper.getUserIdListByTaskId(taskId);
            }
        }
        //2021/8/11修改 增加默认审批意见
        String defaultComment = workflowTaskInfoMapper.getDefaultComment(DISPATCH_WORKFLOW_ID, ticketsDispatchViewRespVO.getDispatchStatus());
        if ((5 == ticketsDispatchViewRespVO.getDispatchStatus() && ticketsDispatchViewRespVO.getPredictCost() < 3000) || (6 == ticketsDispatchViewRespVO.getDispatchStatus() && ticketsDispatchViewRespVO.getPredictCost() < 10000) || (7 == ticketsDispatchViewRespVO.getDispatchStatus() && ticketsDispatchViewRespVO.getPredictCost() < 30000)) {
            defaultComment = "同意";
        }
        if (4 == ticketsDispatchViewRespVO.getDispatchStatus()) {
            if ("10001".equals(ticketsDispatchViewRespVO.getAllocDepartmentNum())) {
                defaultComment = "情况属实，请罗科长批示";
            }
            if ("10002".equals(ticketsDispatchViewRespVO.getAllocDepartmentNum())) {
                defaultComment = "情况属实，请鞠科长批示";
            }
            if ("10003".equals(ticketsDispatchViewRespVO.getAllocDepartmentNum())) {
                defaultComment = "情况属实，请宋科长批示";
            }

        }
        if (null != ticketsDispatchViewRespVO.getPid()) {
            ticketsDispatchViewRespVO.setPCode(ticketDao.getCode(ticketsDispatchViewRespVO.getPid()));
        }
        if (null != ticketsDispatchViewRespVO.getIsMerge() && 1 == ticketsDispatchViewRespVO.getIsMerge()) {
            List<Ticket> ticketList = ticketDao.getByPid(ticketsDispatchViewRespVO.getId());
            if (null != ticketList && ticketList.size() > 0) {
                ticketsDispatchViewRespVO.setChildList(ticketList);
            }
        }
      /*  //通过工单id查询所有子工单
        if (null != ticketsDispatchViewRespVO.getIsMerge() && 1 == ticketsDispatchViewRespVO.getIsMerge()) {
            List<Ticket> ticketList = ticketDao.getByPid(ticketsDispatchViewRespVO.getId());
            //通过工单列表 查询所有的项目
            if (null != ticketList && ticketList.size() > 0) {
                ticketsDispatchViewRespVO.setChildList(ticketList);
                TicketPoint ticketPoint = ticketPointDao.findOne(ticketList.get(0).getId());
                if (ticketPoint != null) {
                    ticketsDispatchViewRespVO.setPointId(ticketPoint.getPointId());
                    ticketsDispatchViewRespVO.setPointName(ticketPoint.getPointName());
                }
                List<TicketBaseMfRespVO> baseMfRespVOList = new ArrayList<>();
                for (Ticket ticket : ticketList) {
                    List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(ticket.getId(), DISPATCH_OP_TYPE);
                    if (!StringHelper.isEmpty(baseMfRespVO)) {
                        baseMfRespVOList.addAll(baseMfRespVO);
                    }
                }
                List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(id, DISPATCH_OP_TYPE);
                if (!StringHelper.isEmpty(baseMfRespVO)) {
                    baseMfRespVOList.addAll(baseMfRespVO);
                }
                ticketsDispatchViewRespVO.setBaseMfRespVOList(baseMfRespVOList);
            }
        } else {
            List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(id, DISPATCH_OP_TYPE);
            if (!StringHelper.isEmpty(baseMfRespVO)) {
                ticketsDispatchViewRespVO.setBaseMfRespVOList(baseMfRespVO);
            }
            TicketPoint ticketPoint = ticketPointDao.findOne(id);
            if (ticketPoint != null) {
                ticketsDispatchViewRespVO.setPointId(ticketPoint.getPointId());
            }

        }*/

        TicketPoint ticketPoint = ticketPointDao.findOne(id);
        if (ticketPoint != null) {
            ticketsDispatchViewRespVO.setPointId(ticketPoint.getPointId());
        }

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(TICKETS_DISPATCH_INFO, ticketsDispatchViewRespVO)
                .put(TICKETS_DISPATCH_VIEW_INFO, getLogs(id, STATUS_DISABLE, DISPATCH_OP_TYPE, ticketsOpStatusList))
                .put(TICKETS_DISPATCH_LOGS_INFO, getLogs(id, STATUS_ENABLE, DISPATCH_OP_TYPE, ticketsOpStatusList))
                .put(TICKETS_DISPATCH_FLOW, getTicketFlow(id, DISPATCH_WORKFLOW_ID, DISPATCH_OP_TYPE, ticketsDispatchViewRespVO.getDispatchStatus()))
                .put("activityId", activityId)
                .put("assigneeList", assigneeList)
                .put("defaultComment", defaultComment)
                .build();
        return ApiResult.success(result);
    }


    @Override
    public ApiResult viewDispatchEdit(TicketDispatchViewEditVO ticketsDispatchViewEditVO) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketsDispatchViewEditVO, ticket);
        if(!StringUtils.isEmpty(ticket.getRemark())){
            ticket.setAcceptanceInstructions(ticket.getRemark().replaceAll("需",""));
        }
        ticketDao.update(ticket);
        return ApiResult.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult editDispatch(TicketDispatchEditVO ticketsDispatchEditVO) {
        //id查询是否是合并工单
        Ticket ticket = ticketDao.getTicket(ticketsDispatchEditVO.getId());
        List<TicketBaseMfRespVO> baseMfRespVOList = ticketsDispatchEditVO.getBaseMfRespVOList();
        if (StringHelper.isEmpty(baseMfRespVOList)) {
            ticketDetailDao.deleteByTicketId(ticketsDispatchEditVO.getId(), null);
            Ticket tickets = new Ticket();
            BeanUtils.copyProperties(ticketsDispatchEditVO, tickets);
            tickets.setPredictCost(BigDecimal.ZERO.doubleValue());
            TicketOpLogsReqVO ticketsOpReqVO = new TicketOpLogsReqVO();
            BeanUtils.copyProperties(ticketsDispatchEditVO, ticketsOpReqVO);
            ticketsOpReqVO.setOpType(null);
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
            ticketPredict.setIsAdj(ticketPredict.getIsAdj() == null ? 0 : ticketPredict.getIsAdj());
            ticketPredict.setAdjAmount(ticketPredict.getAdjAmount() == null ? new BigDecimal(0) : ticketPredict.getAdjAmount());
            BigDecimal bigDecimal = ticketPredict.getPredictPrice();
            predictPriceTotal = predictPriceTotal.add(bigDecimal);
            BeanUtils.copyProperties(ticketPredict, ticketAdjust);
            ticketAdjust.setType(ACCEPT_OP_TYPE);
            ticketPredictDetails.add(ticketPredict);
            ticketAdjustDetails.add(ticketAdjust);
        }

        TicketOpLogsReqVO ticketsOpReqVO = new TicketOpLogsReqVO();
        BeanUtils.copyProperties(ticketsDispatchEditVO, ticketsOpReqVO);
//        ticketsOpReqVO.setExMsg(String.format(FORMAT_MSG_PREDICT, predictPriceTotal.doubleValue()));
//        ApiResult apiResult = deal(ticketsOpReqVO);
//        if (apiResult.getCode() != RESP_CODE_SUCCESS) {
//            return apiResult;
//        }

        ticketDetailDao.deleteByTicketId(ticketsDispatchEditVO.getId(), null);
        ticketDetailDao.insertBatch(ticketPredictDetails);
        ticketDetailDao.insertBatch(ticketAdjustDetails);

        Ticket tickets = new Ticket();
        BeanUtils.copyProperties(ticketsDispatchEditVO, tickets);
        tickets.setPredictCost(predictPriceTotal.doubleValue());
        ticketsOpReqVO.setOpType(null);
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
        List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(id, ACCEPT_OP_TYPE);
        TicketAcceptViewRespVO ticketsAcceptViewRespVO = ticketDao.queryTicketsAcceptViewById(id);
        if(!StringHelper.isEmpty(baseMfRespVO)&&baseMfRespVO.size()>0&&2==ticketsAcceptViewRespVO.getAcceptStatus()){
            TicketAcceptEditVO ticketAcceptEditVO = new TicketAcceptEditVO();
            ticketAcceptEditVO.setId(ticketsAcceptViewRespVO.getId());
            ticketAcceptEditVO.setBaseMfRespVOList(baseMfRespVO);
            ticketAcceptEditVO.setCreator(ticketsAcceptViewRespVO.getCreator());
            this.editAccept(ticketAcceptEditVO);
            ticketsAcceptViewRespVO = ticketDao.queryTicketsAcceptViewById(id);
        }
        if (!StringHelper.isEmpty(baseMfRespVO)) {
            ticketsAcceptViewRespVO.setBaseMfRespVOList(baseMfRespVO);
        }
        if (ticketsAcceptViewRespVO == null) {
            return ApiResult.error("没有此工单号!id: " + id);
        }
        String taskId = workflowTaskInfoMapper.getByBusinessIdAndWorkflowId(id.toString(), ACCEPTANCE_WORKFLOW_ID);
        String activityId = ticketWorkflowService.getActivityIdByTaskId(taskId);
        List<String> assigneeList = new ArrayList<>();
        if (!StringUtils.isEmpty(taskId)) {
            //通过任务id查询是否存在唯一待办人
            String assignee = ticketWorkflowService.getAssigneeByTaskId(taskId);
            //初始化待办人用户名列表

            if (!StringUtils.isEmpty(assignee)) {
                assigneeList.add(assignee);
            } else {
                assigneeList = workflowTaskInfoMapper.getUserIdListByTaskId(taskId);
            }
        }
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        String defaultComment = workflowTaskInfoMapper.getDefaultComment(ACCEPTANCE_WORKFLOW_ID, ticketsAcceptViewRespVO.getAcceptStatus());
        if ((5 == ticketsAcceptViewRespVO.getAcceptStatus() && ticketsAcceptViewRespVO.getAcceptPredictCost() < 3000) || (6 == ticketsAcceptViewRespVO.getAcceptStatus() && ticketsAcceptViewRespVO.getAcceptPredictCost() < 10000) || (7 == ticketsAcceptViewRespVO.getAcceptStatus() && ticketsAcceptViewRespVO.getAcceptPredictCost() < 30000)) {
            defaultComment = "同意";
        }
        if (4 == ticketsAcceptViewRespVO.getAcceptStatus()) {
            if ("10001".equals(ticketsAcceptViewRespVO.getAllocDepartmentNum())) {
                defaultComment = "情况属实，请罗科长批示";
            }
            if ("10002".equals(ticketsAcceptViewRespVO.getAllocDepartmentNum())) {
                defaultComment = "情况属实，请鞠科长批示";
            }
            if ("10003".equals(ticketsAcceptViewRespVO.getAllocDepartmentNum())) {
                defaultComment = "情况属实，请宋科长批示";
            }

        }
       if (null != ticketsAcceptViewRespVO.getPid()) {
            ticketsAcceptViewRespVO.setPCode(ticketDao.getCode(ticketsAcceptViewRespVO.getPid()));
        }
        if (null != ticketsAcceptViewRespVO.getIsMerge() && 1 == ticketsAcceptViewRespVO.getIsMerge()) {
            List<Ticket> ticketList = ticketDao.getByPid(ticketsAcceptViewRespVO.getId());
            //通过工单列表 查询所有的项目
            if (null != ticketList && ticketList.size() > 0) {
                ticketsAcceptViewRespVO.setChildList(ticketList);
            }
        }
       /*

        if (null != ticketsAcceptViewRespVO.getIsMerge() && 1 == ticketsAcceptViewRespVO.getIsMerge()) {
            List<Ticket> ticketList = ticketDao.getByPid(ticketsAcceptViewRespVO.getId());
            //通过工单列表 查询所有的项目
            if (null != ticketList && ticketList.size() > 0) {
                ticketsAcceptViewRespVO.setChildList(ticketList);
                TicketPoint ticketPoint = ticketPointDao.findOne(ticketList.get(0).getId());
                if (ticketPoint != null) {
                    ticketsAcceptViewRespVO.setPointId(ticketPoint.getPointId());
                    ticketsAcceptViewRespVO.setPointName(ticketPoint.getPointName());
                }
                List<TicketBaseMfRespVO> baseMfRespVOList = new ArrayList<>();
                for (Ticket ticket : ticketList) {
                    List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(ticket.getId(), ACCEPT_OP_TYPE);
                    if (!StringHelper.isEmpty(baseMfRespVO)) {
                        baseMfRespVOList.addAll(baseMfRespVO);
                    }
                }
                List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(id, ACCEPT_OP_TYPE);
                if (!StringHelper.isEmpty(baseMfRespVO)) {
                    baseMfRespVOList.addAll(baseMfRespVO);
                }
                ticketsAcceptViewRespVO.setBaseMfRespVOList(baseMfRespVOList);
            }
        } else {
            List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(id, ACCEPT_OP_TYPE);
            if (!StringHelper.isEmpty(baseMfRespVO)) {
                ticketsAcceptViewRespVO.setBaseMfRespVOList(baseMfRespVO);
            }
            TicketPoint ticketPoint = ticketPointDao.findOne(id);
            if (ticketPoint != null) {
                ticketsAcceptViewRespVO.setPointId(ticketPoint.getPointId());
            }
        }*/


        TicketPoint ticketPoint = ticketPointDao.findOne(id);
        if (ticketPoint != null) {
            ticketsAcceptViewRespVO.setPointId(ticketPoint.getPointId());
        }


        Map<String, Object> result = MapBuilder.getBuilder()
                .put(TICKETS_ACCEPT_INFO, ticketsAcceptViewRespVO)
                .put(TICKETS_ACCEPT_VIEW_INFO, getLogs(id, STATUS_DISABLE, ACCEPT_OP_TYPE, ticketsOpStatusList))
                .put(TICKETS_ACCEPT_LOGS_INFO, getLogs(id, STATUS_ENABLE, ACCEPT_OP_TYPE, ticketsOpStatusList))
                .put(TICKETS_ACCEPT_FLOW, getTicketFlow(id, ACCEPTANCE_WORKFLOW_ID, ACCEPT_OP_TYPE, ticketsAcceptViewRespVO.getAcceptStatus()))
                .put("activityId", activityId)
                .put("assigneeList", assigneeList)
                .put("defaultComment", defaultComment)
                .build();
        return ApiResult.success(result);
    }


    private List<TicketFlowRespVO> getTicketFlow(Long id, String workflowId, Integer opType, Integer status) {
        List<TicketFlowRespVO> list = new ArrayList<>();
        Ticket ticket = ticketDao.getById(id.toString());
        //所有的流程
        List<TicketWorkFlowRespVO> flowAllRespVOS = workflowTaskInfoMapper.getByWorkflowId(null, workflowId, ticket.getPredictCost().intValue());
        //待处理的流程
        List<TicketWorkFlowRespVO> flowToDoRespVOS = workflowTaskInfoMapper.getByWorkflowId(status, workflowId, ticket.getPredictCost().intValue());

        List<TicketOpLogsRespVO> logsViewList = ticketOpLogsDao.queryTicketOpLogs(id, opType);

        if (flowAllRespVOS.size() != flowToDoRespVOS.size()) {
            flowAllRespVOS.removeAll(flowToDoRespVOS);

            setTicketFlow(list, flowAllRespVOS, logsViewList, STATUS_ENABLE);
            setTicketFlow(list, flowToDoRespVOS, logsViewList, STATUS_DISABLE);
        } else {
            setTicketFlow(list, flowAllRespVOS, logsViewList, STATUS_DISABLE);
        }

        return list;
    }

    private void setTicketFlow(List<TicketFlowRespVO> list, List<TicketWorkFlowRespVO> flowAllRespVOS, List<TicketOpLogsRespVO> logsViewList, int i) {
        for (TicketWorkFlowRespVO flowAllRespVO : flowAllRespVOS) {
            TicketFlowRespVO ticketFlowRespVO = new TicketFlowRespVO();
            ticketFlowRespVO.setDeal(i);
            ticketFlowRespVO.setOpName(flowAllRespVO.getTaskName());
            ticketFlowRespVO.setDescription(flowAllRespVO.getBusinessStatusName());
            setUserInfo(logsViewList, flowAllRespVO, ticketFlowRespVO);
            list.add(ticketFlowRespVO);
        }
    }

    private void setUserInfo(List<TicketOpLogsRespVO> logsViewList, TicketWorkFlowRespVO flowAllRespVO, TicketFlowRespVO ticketFlowRespVO) {
        String businessStatusCode = flowAllRespVO.getBusinessStatusCode();
        for (TicketOpLogsRespVO ticketOpLogsRespVO : logsViewList) {
            if (businessStatusCode.equals(String.valueOf(ticketOpLogsRespVO.getOpStatus()))) {
                ticketFlowRespVO.setRoleNum(ticketOpLogsRespVO.getRoleNum());
                ticketFlowRespVO.setMaintainNum(ticketOpLogsRespVO.getMaintainNum());
                ticketFlowRespVO.setCreator(ticketOpLogsRespVO.getCreator());
                ticketFlowRespVO.setCheckTime(ticketOpLogsRespVO.getUpdateTime());
                ticketFlowRespVO.setMsg(ticketOpLogsRespVO.getMsg());
                break;
            }
        }
        //如果循环完了creator还是空的 做一下处理 为了解决最后一个审批人显示为空的问题
        if (StringUtils.isEmpty(ticketFlowRespVO.getCreator())) {
            for (TicketOpLogsRespVO ticketOpLogsRespVO : logsViewList) {
                if (ticketOpLogsRespVO.getOpStatus() == 20) {
                    ticketFlowRespVO.setRoleNum(ticketOpLogsRespVO.getRoleNum());
                    ticketFlowRespVO.setMaintainNum(ticketOpLogsRespVO.getMaintainNum());
                    ticketFlowRespVO.setCreator(ticketOpLogsRespVO.getCreator());
                    ticketFlowRespVO.setCheckTime(ticketOpLogsRespVO.getUpdateTime());
                    ticketFlowRespVO.setMsg(ticketOpLogsRespVO.getMsg());
                    break;
                }
            }
        }
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
        Ticket ticket = ticketDao.getTicket(ticketAcceptEditVO.getId());
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
//        ticketsOpReqVO.setExMsg(String.format(FORMAT_MSG_PREDICT_COST, predictPriceTotal.doubleValue(), adjustPriceTotal.doubleValue()));
//        ApiResult apiResult = deal(ticketsOpReqVO);
//        if (apiResult.getCode() != RESP_CODE_SUCCESS) {
//            return apiResult;
//        }

        ticketDetailDao.deleteByTicketId(ticketAcceptEditVO.getId(), ACCEPT_OP_TYPE);
        ticketDetailDao.insertBatch(ticketAdjustDetails);

        Ticket tickets = new Ticket();
        BeanUtils.copyProperties(ticketAcceptEditVO, tickets);
        tickets.setAcceptPredictCost(predictPriceTotal.doubleValue());
        tickets.setAcceptAdjustCost(adjustPriceTotal.doubleValue());
        ticketsOpReqVO.setOpType(null);
        saveLogsAndUpdateStatus(ticketsOpReqVO, tickets);
        return ApiResult.success();
    }


    @Override
    public String exportDispatch(String ticketCode) {
        TicketsDispatchPdfVO pdfVO = ticketDao.queryTicketsDispatchPdfByCode(ticketCode);
       /* if (null != pdfVO.getIsMerge() && 1 == pdfVO.getIsMerge()) {
            List<Ticket> ticketList = ticketDao.getByPid(pdfVO.getId());
            //通过工单列表 查询所有的项目
            if (null != ticketList && ticketList.size() > 0) {
                List<TicketBaseMfRespVO> baseMfRespVOList = new ArrayList<>();
                for (Ticket ticket : ticketList) {
                    List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(ticket.getId(), DISPATCH_OP_TYPE);
                    if (!StringHelper.isEmpty(baseMfRespVO)) {
                        baseMfRespVOList.addAll(baseMfRespVO);
                    }
                }
                pdfVO.setBaseMfPdfRespVOList(baseMfRespVOList);
                //修改点位名称
                TicketPoint ticketPoint = ticketPointDao.findOne(ticketList.get(0).getId());
                if (ticketPoint != null) {
                    pdfVO.setPointName(ticketPoint.getPointName());
                }
            }

        } else {
            List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(pdfVO.getId(), DISPATCH_OP_TYPE);
            if (!StringHelper.isEmpty(baseMfRespVO)) {
                pdfVO.setBaseMfPdfRespVOList(baseMfRespVO);
            }
        }*/
        List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(pdfVO.getId(), DISPATCH_OP_TYPE);
        if (!StringHelper.isEmpty(baseMfRespVO)) {
            pdfVO.setBaseMfPdfRespVOList(baseMfRespVO);
        }
        List<TicketFlowRespVO> ticketFlowList = getTicketFlow(pdfVO.getId(), DISPATCH_WORKFLOW_ID, DISPATCH_OP_TYPE, pdfVO.getDispatchStatus());
        pdfVO.setTicketFlowList(ticketFlowList);
        return pdfService.generateDispatchPdf(pdfVO);
    }

    @Override
    public String exportAccept(String ticketCode) {
        TicketsDispatchPdfVO pdfVO = ticketDao.queryTicketsDispatchPdfByCode(ticketCode);
        /*if (null != pdfVO.getIsMerge() && 1 == pdfVO.getIsMerge()) {
            List<Ticket> ticketList = ticketDao.getByPid(pdfVO.getId());
            //通过工单列表 查询所有的项目
            if (null != ticketList && ticketList.size() > 0) {
                List<TicketBaseMfRespVO> baseMfRespVOList = new ArrayList<>();
                for (Ticket ticket : ticketList) {
                    List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(ticket.getId(), ACCEPT_OP_TYPE);
                    if (!StringHelper.isEmpty(baseMfRespVO)) {
                        baseMfRespVOList.addAll(baseMfRespVO);
                    }
                }
                pdfVO.setBaseMfPdfRespVOList(baseMfRespVOList);
                //修改点位名称
                TicketPoint ticketPoint = ticketPointDao.findOne(ticketList.get(0).getId());
                if (ticketPoint != null) {
                    pdfVO.setPointName(ticketPoint.getPointName());
                }
            }
        } else {
            List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(pdfVO.getId(), ACCEPT_OP_TYPE);
            if (!StringHelper.isEmpty(baseMfRespVO)) {
                pdfVO.setBaseMfPdfRespVOList(baseMfRespVO);
            }
        }*/
        List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(pdfVO.getId(), ACCEPT_OP_TYPE);
        if (!StringHelper.isEmpty(baseMfRespVO)) {
            pdfVO.setBaseMfPdfRespVOList(baseMfRespVO);
        }
        List<TicketFlowRespVO> ticketFlowList = getTicketFlow(pdfVO.getId(), ACCEPTANCE_WORKFLOW_ID, ACCEPT_OP_TYPE, pdfVO.getDispatchStatus());
        pdfVO.setTicketFlowList(ticketFlowList);
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
        ticket.setTicketCodeResult(ticket.getTicketCode());
        ticket.setOccurredTime(DateHelper.getNow());
        ticketDao.create(ticket);
        //如果责任人为空的话
        if (StringUtils.isEmpty(ticket.getDutyContact()) && !StringUtils.isEmpty(ticket.getAllocDepartmentNum())) {
            String dutyContact = baseOrgService.queryDutyInfo(ticket.getAllocDepartmentNum());
            ticket.setDutyContact(dutyContact);
        }
        String contact = baseOrgService.queryContactInfo(ticket.getAllocDepartmentNum());
        ticket.setApplyContact(contact);
        // 根据联系人 查询手机号
        String contactPhone = sysUserDao.getPhone(contact);
        if(!StringUtils.isEmpty(contactPhone)){
            ticket.setApplyPhone(contactPhone);
        }
        if(StringHelper.isEmpty(ticket.getRemark())){
            ticket.setRemark("设备掉线");
        }
        ticketDao.update(ticket);
        Long ticketId = ticket.getId();

        List<Asset> assetList = assetService.getListByCondition(new HashMap<String, Object>() {{
            put("assetIds", reqVO.getAssetIds());
        }});

        Set<TicketPoint> ticketPoints = new HashSet<>(assetList.size());
        List<TicketPointDevice> ticketPointDevices = new ArrayList<>(assetList.size());
        TicketPoint ticketPoint;
        TicketPointDevice ticketPointDevice;
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
     * 流程实例id关联
     *
     * @param ticket
     */
    @Override
    public void updateProcessInstanceIdById(Ticket ticket) {
        ticketDao.updateProcessInstanceIdById(ticket);
    }


    /**
     * @return
     * @Author beiming
     * @Description 设置预计抵达时间、预计完成时间
     * 严重 抵达30分钟 完成 3天
     * 紧急 抵达30分钟 完成3天
     * 一般 抵达60分钟 完成 3天
     * 轻微 抵达90分钟 完成3天严重
     * @Date 4/13/21
     * @Param
     **/
    private void resetPredictArriveAndCompleteTime(Ticket ticket) {
        Integer issueLevel = ticket.getIssueLevel();
        long currentTime = System.currentTimeMillis();
        Date now = new Date();
        ticket.setPredictCompleteTime(DateHelper.getDateTime(DateHelper.plusDays(now, 3)));
        if (TICKET_ISSUE_LEVEL_SERIOUS.equals(issueLevel)) {
            currentTime += 30 * 60 * 1000;
            Date date = new Date(currentTime);
            ticket.setPredictArriveTime(DateHelper.getDateTime(date));

        } else if (TICKET_ISSUE_LEVEL_URGENT.equals(issueLevel)) {
            currentTime += 30 * 60 * 1000;
            Date date = new Date(currentTime);
            ticket.setPredictArriveTime(DateHelper.getDateTime(date));

        } else if (TICKET_ISSUE_LEVEL_COMMONLY.equals(issueLevel)) {
            currentTime += 60 * 60 * 1000;
            Date date = new Date(currentTime);
            ticket.setPredictArriveTime(DateHelper.getDateTime(date));

        } else if (TICKET_ISSUE_LEVEL_SLIGHT.equals(issueLevel)) {
            currentTime += 90 * 60 * 1000;
            Date date = new Date(currentTime);
            ticket.setPredictArriveTime(DateHelper.getDateTime(date));
        }

    }

    /**
     * @return
     * @Author beiming
     * @Description 设置预计抵达时间、预计完成时间
     * 严重 抵达30分钟 完成 3天
     * 紧急 抵达30分钟 完成3天
     * 一般 抵达60分钟 完成 3天
     * 轻微 抵达90分钟 完成3天严重
     * @Date 4/13/21
     * @Param
     **/
    private void resetPredictArriveAndCompleteTime2(Ticket ticket) {
        Integer issueLevel = ticket.getIssueLevel();

        try {
            Date now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ticket.getOccurredTime());
            long currentTime = now.getTime();
            ticket.setPredictCompleteTime(DateHelper.getDateTime(DateHelper.plusDays(now, 3)));
            if (TICKET_ISSUE_LEVEL_SERIOUS.equals(issueLevel)) {
                currentTime += 30 * 60 * 1000;
                Date date = new Date(currentTime);
                ticket.setPredictArriveTime(DateHelper.getDateTime(date));

            } else if (TICKET_ISSUE_LEVEL_URGENT.equals(issueLevel)) {
                currentTime += 30 * 60 * 1000;
                Date date = new Date(currentTime);
                ticket.setPredictArriveTime(DateHelper.getDateTime(date));

            } else if (TICKET_ISSUE_LEVEL_COMMONLY.equals(issueLevel)) {
                currentTime += 60 * 60 * 1000;
                Date date = new Date(currentTime);
                ticket.setPredictArriveTime(DateHelper.getDateTime(date));

            } else if (TICKET_ISSUE_LEVEL_SLIGHT.equals(issueLevel)) {
                currentTime += 90 * 60 * 1000;
                Date date = new Date(currentTime);
                ticket.setPredictArriveTime(DateHelper.getDateTime(date));
            }
        }catch (Exception e){
            log.error("故障时间解析错误！调用默认方法！");
            this.resetPredictArriveAndCompleteTime(ticket);
        }

    }


    /***************工作流方法开始***********************/

    /**
     * 发起派工单方法
     *
     * @param startFlowDto
     * @param userName
     * @return
     */
    @Override
    public TaskRepDto startDispatch(StartFlowDto startFlowDto, String userName) {
        return startWorkflow(startFlowDto, userName, DISPATCH_WORKFLOW_ID);
    }

    @Override
    public ApiResult startDispatch(TicketOpLogsReqVO ticketOpLogsReqVO) {
        if (checkStatus(ticketOpLogsReqVO.getId(), DISPATCH_WORKFLOW_ID)) {
            return ApiResult.error("您已提交过审核或者无权限提交!");
        }
        Ticket ticket = this.getById(ticketOpLogsReqVO.getId().toString());
        //首先对工单的派工单驳回记录做一个清除
        this.clearBackRecord(ticketOpLogsReqVO.getId(), DISPATCH_WORKFLOW_ID);

        //用id 和 workflowId确定是否已经存在发起的流程 且
        String taskId = workflowTaskInfoMapper.getByBusinessIdAndWorkflowId(ticketOpLogsReqVO.getId().toString(), DISPATCH_WORKFLOW_ID);
        //如果不为空 说明此时只需要流转 不需要再次发起工单 开始塑造流转所需的参数
        if (!StringUtils.isEmpty(taskId)) {
            ticketOpLogsReqVO.setAgree(1);
            ticketOpLogsReqVO.setMsg("");
            String activityId = ticketWorkflowService.getActivityIdByTaskId(taskId);
            ticketOpLogsReqVO.setActivityId(activityId);
            //通过id和工单类型查询到一个流程实例id
            String processInstance = workflowTaskInfoMapper.getProcessInstanceByIdAndType(ticket.getId(), DISPATCH_WORKFLOW_ID);
            runtimeService.setVariable(processInstance, "money", ticket.getPredictCost());
            return this.circulationDispatch(ticketOpLogsReqVO);
        }
        // 1.创建派工单流程
        // 2.日志和状态记录
        StartFlowDto startFlowDto = new StartFlowDto();
        startFlowDto.setId(ticketOpLogsReqVO.getId().toString());
        List<ConditionDto> conditionDtoList = new ArrayList<>();

        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setKey("orgId");
        conditionDto.setValue(ticket.getAllocDepartmentNum());
        conditionDtoList.add(conditionDto);
        ConditionDto conditionDto2 = new ConditionDto();
        conditionDto2.setKey("money");
        conditionDto2.setDoubleValue(ticket.getPredictCost());
        conditionDtoList.add(conditionDto2);
        startFlowDto.setConditionDtoList(conditionDtoList);
        TaskRepDto taskRepDto = startDispatch(startFlowDto, ticketOpLogsReqVO.getCreator());
        ticketOpLogsReqVO.setMsg(getMsg(ticketOpLogsReqVO.getMsg(), taskRepDto.getBeforeTaskName(), ticketOpLogsReqVO.getAgree(), null));
        if (taskRepDto.getBusinessStatusCode() != null) {
            ticketOpLogsReqVO.setOpStatus(Integer.valueOf(taskRepDto.getBusinessStatusCode()));
        }
        saveLog(ticketOpLogsReqVO);
        updateStatus(ticketOpLogsReqVO.getId(), TICKET_STATUS_CHECK);
        ticketOpLogsReqVO.setTaskId(taskRepDto.getTaskId());
        workFlowPushWechatMsg(ticketOpLogsReqVO, DISPATCH_WORKFLOW_ID);
        return ApiResult.success();
    }


    private boolean checkStatus(Long id, String workflowId) {
        TicketSearchRespVO ticketSearchRespVO = ticketDao.queryById(id);
        if (DISPATCH_WORKFLOW_ID.equals(workflowId)) {
            if (ticketSearchRespVO.getDispatchStatus() == DISPATCH_STATUS_SUP_CHECK) {
                return true;
            }
            return false;
        }
        if (ACCEPTANCE_WORKFLOW_ID.equals(workflowId)) {
            if (ticketSearchRespVO.getAcceptStatus() == ACCEPT_STATUS_SUP_CHECK) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 派工单流转方法
     *
     * @param circulationTaskDto
     * @param userName
     * @return
     */
    @Override
    public TaskRepDto circulationDispatch(CirculationTaskDto circulationTaskDto, String userName) {
        circulationTaskDto.setUsername(userName);
        circulationTaskDto.setWorkflowId(DISPATCH_WORKFLOW_ID);
        return ticketWorkflowService.circulationTicketTask(circulationTaskDto);
    }

    @Override
    public ApiResult circulationDispatch(TicketOpLogsReqVO ticketOpLogsReqVO) {
        // todo 派工单批流程，需要taskId，isAgree,msg，使用CirculationTaskDto对象
        //【new】参数封装
        circulation(ticketOpLogsReqVO, DISPATCH_WORKFLOW_ID);
        ///todo 维护最近一次派工单审批时间
        this.maintainApprovedTime(ticketOpLogsReqVO,DISPATCH_WORKFLOW_ID);
        //流转完后 判断派工单状态和验收单状态是否都已经为审批完成 且工单状态不为已验收 修改工单状态为已验收
        this.autoInspected(ticketOpLogsReqVO.getId());
        return ApiResult.success();
    }

    private void circulation(TicketOpLogsReqVO ticketOpLogsReqVO, String workflowId) {
        CirculationTaskDto circulationTaskDto = new CirculationTaskDto();
        String taskId = workflowTaskInfoMapper.getByBusinessIdAndWorkflowId(ticketOpLogsReqVO.getId().toString(), workflowId);
        String activityId = ticketWorkflowService.getActivityIdByTaskId(taskId);
        if (StringUtils.isEmpty(activityId)) {
            throw new BusinessException("您已提交过审核或者无权限提交!");
        }
        if (!activityId.equals(ticketOpLogsReqVO.getActivityId())) {
            throw new BusinessException("您已提交过审核或者无权限提交！");
        }
        //【new】前端传过来的三个参数
        circulationTaskDto.setTaskId(taskId);
        circulationTaskDto.setIsAgree(ticketOpLogsReqVO.getAgree().toString());
        circulationTaskDto.setMsg(ticketOpLogsReqVO.getMsg());
        circulationTaskDto.setUsername(ticketOpLogsReqVO.getCreator());
        circulationTaskDto.setWorkflowId(workflowId);
        TicketWorkflowDto ticketWorkflowDto = ticketDao.getById(ticketOpLogsReqVO.getId().toString());
        //如果为0时维护派工单和验收单的状态名称
        if (circulationTaskDto.getIsAgree().equals("0")) {
            //修改验收单状态
            if (workflowId.equals(ACCEPTANCE_WORKFLOW_ID)) {
                //根据status查询对应的角色名称
                String roleName = workflowTaskInfoMapper.getRoleNameByStatusCode(ticketWorkflowDto.getAcceptStatus(), ACCEPTANCE_WORKFLOW_ID);
                if (!StringUtils.isEmpty(roleName)) {
                    ticketDao.updateAcceptanceStatusName(ticketOpLogsReqVO.getId(), "由" + roleName + "驳回过");
                }

            }
            if (workflowId.equals(DISPATCH_WORKFLOW_ID)) {
                String roleName = workflowTaskInfoMapper.getRoleNameByStatusCode(ticketWorkflowDto.getDispatchStatus(), DISPATCH_WORKFLOW_ID);
                if (!StringUtils.isEmpty(roleName)) {
                    ticketDao.updateDispatchStatusName(ticketOpLogsReqVO.getId(), "由" + roleName + "驳回过");
                }
            }
        }
        TaskRepDto taskRepDto = ticketWorkflowService.circulationTicketTask(circulationTaskDto);
        //流转完后、如果审批角色是监理 修改核算金额与预算金额一致
        if(workflowId.equals(ACCEPTANCE_WORKFLOW_ID)&&(null==ticketWorkflowDto.getAcceptAdjustCost()||0D==ticketWorkflowDto.getAcceptAdjustCost()||0.0D==ticketWorkflowDto.getAcceptAdjustCost())){
            //查询当前审批人的角色
            SysUser sysUser = sysUserDao.findUserByName(ticketOpLogsReqVO.getCreator());
            if(sysUser.getRoleNum().equals(SUPERVISION_ROLE)){
                ticketDetailDao.updateAcceptance(ticketOpLogsReqVO.getId());
                Ticket ticket = new Ticket();
                ticket.setId(ticketOpLogsReqVO.getId());
                ticket.setAcceptAdjustCost(ticketWorkflowDto.getAcceptPredictCost());
                ticketDao.update(ticket);


            }
        }
        ticketOpLogsReqVO.setMsg(getMsg(ticketOpLogsReqVO.getMsg(), taskRepDto.getBeforeTaskName(), ticketOpLogsReqVO.getAgree(), workflowId));
        if (taskRepDto.getBusinessStatusCode() != null) {
            ticketOpLogsReqVO.setOpStatus(Integer.valueOf(taskRepDto.getBusinessStatusCode()));
        }
        saveLog(ticketOpLogsReqVO);
        checkSucAndUpdateStatus(ticketOpLogsReqVO.getId(), ticketOpLogsReqVO.getAgree());
        ticketOpLogsReqVO.setTaskId(taskRepDto.getTaskId());
        workFlowPushWechatMsg(ticketOpLogsReqVO, workflowId);

    }


    /**
     * @return
     * @Author beiming
     * @Description 工作流推送工单微信消息
     * @Date 5/14/21
     * @Param
     **/
    private void workFlowPushWechatMsg(TicketOpLogsReqVO ticketOpLogsReqVO, String workflowId) {
        log.info("#开始进行微信推送{}", workflowId);
        String nextTaskId = workflowTaskInfoMapper.getByBusinessIdAndWorkflowId(ticketOpLogsReqVO.getId().toString(), workflowId);
        List<String> unionidList = new ArrayList<>();
        List<String> assigneeList = new ArrayList<>();
        if (!StringUtils.isEmpty(nextTaskId)) {
            //通过任务id查询是否存在唯一待办人
            String assignee = ticketWorkflowService.getAssigneeByTaskId(nextTaskId);
            //初始化待办人用户名列表
            if (!StringUtils.isEmpty(assignee)) {
                assigneeList.add(assignee);
            } else {
                assigneeList = workflowTaskInfoMapper.getUserIdListByTaskId(nextTaskId);
            }
        }
        if (null != assigneeList && assigneeList.size() > 0) {
            unionidList = workflowTaskInfoMapper.getUserByAssigneeList(assigneeList);
        }
        log.info("#微信推送unionId列表{}", unionidList);
        if (StringUtils.isEmpty(nextTaskId) || unionidList == null || unionidList.size() == 0) {
            return;
        }
        TicketSearchRespVO vo = ticketDao.queryById(ticketOpLogsReqVO.getId());
        if (DISPATCH_WORKFLOW_ID.equals(workflowId)) {
            log.info("派工单推送微信消息:taskId#{},ticketId#{},unionidList#{}", nextTaskId, ticketOpLogsReqVO.getId(), unionidList.toString());
            // 派工单推送微信消息
            packageDisPatchSubmitWeChatMsgWorkFlow(ticketOpLogsReqVO, unionidList, vo.getPredictCost());
        } else if (ACCEPTANCE_WORKFLOW_ID.equals(workflowId)) {
            //验收单推送微信消息
            log.info("验收单推送微信消息:taskId#{},ticketId#{},unionidList#{}", nextTaskId, ticketOpLogsReqVO.getId(), unionidList.toString());
            packageAcceptSubmitWeChatMsgWorkFlow(ticketOpLogsReqVO, unionidList, vo.getAcceptPredictCost(), vo.getAcceptAdjustCost());
        }
        log.info("#微信推送结束{}", workflowId);
    }

    /**
     * 我的派工单待办列表
     *
     * @param userName
     * @return
     */
    @Override
    public List<TicketWorkflowDto> myDispatchList(String userName) {
        List<TicketWorkflowDto> taskInfoDetailDtoList = ticketWorkflowService.myTicketFlowList(userName, DISPATCH_WORKFLOW_ID);
        return taskInfoDetailDtoList;
    }


    /**
     * 发起验收工单方法
     *
     * @param startFlowDto
     * @param userName
     * @return
     */
    @Override
    public TaskRepDto startAcceptance(StartFlowDto startFlowDto, String userName) {
        return startWorkflow(startFlowDto, userName, ACCEPTANCE_WORKFLOW_ID);
    }

    @Override
    public ApiResult startAccept(TicketOpLogsReqVO ticketOpLogsReqVO) {
        if (checkStatus(ticketOpLogsReqVO.getId(), ACCEPTANCE_WORKFLOW_ID)) {
            return ApiResult.error("您已提交过审核或者无权限提交!");
        }
        Ticket ticket = getById(ticketOpLogsReqVO.getId().toString());
        //对驳回记录进行清除
        this.clearBackRecord(ticketOpLogsReqVO.getId(), ACCEPTANCE_WORKFLOW_ID);
        //用id 和 workflowId确定是否已经存在发起的流程
        String taskId = workflowTaskInfoMapper.getByBusinessIdAndWorkflowId(ticketOpLogsReqVO.getId().toString(), ACCEPTANCE_WORKFLOW_ID);
        //如果不为空 说明此时只需要流转 不需要再次发起工单 开始塑造流转所需的参数
        if (!StringUtils.isEmpty(taskId)) {
            ticketOpLogsReqVO.setAgree(1);
            ticketOpLogsReqVO.setMsg("");
            String activityId = ticketWorkflowService.getActivityIdByTaskId(taskId);
            ticketOpLogsReqVO.setActivityId(activityId);
            //通过id和工单类型查询到一个流程实例id
            String processInstance = workflowTaskInfoMapper.getProcessInstanceByIdAndType(ticket.getId(), ACCEPTANCE_WORKFLOW_ID);
            runtimeService.setVariable(processInstance, "money", ticket.getAcceptPredictCost());
            return this.circulationAcceptance(ticketOpLogsReqVO);
        }

        // 1.创建派工单流程
        // 2.日志和状态记录
        StartFlowDto startFlowDto = new StartFlowDto();
        startFlowDto.setId(ticketOpLogsReqVO.getId().toString());
        List<ConditionDto> conditionDtoList = new ArrayList<>();
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setKey("orgId");
        conditionDto.setValue(ticket.getAllocDepartmentNum());
        conditionDtoList.add(conditionDto);
        ConditionDto conditionDto2 = new ConditionDto();
        conditionDto2.setKey("money");
        conditionDto2.setDoubleValue(ticket.getAcceptPredictCost());
        conditionDtoList.add(conditionDto2);
        startFlowDto.setConditionDtoList(conditionDtoList);
        TaskRepDto taskRepDto = startAcceptance(startFlowDto, ticketOpLogsReqVO.getCreator());
        ticketOpLogsReqVO.setMsg(getMsg(ticketOpLogsReqVO.getMsg(), taskRepDto.getBeforeTaskName(), ticketOpLogsReqVO.getAgree(), null));
        if (taskRepDto.getBusinessStatusCode() != null) {
            ticketOpLogsReqVO.setOpStatus(Integer.valueOf(taskRepDto.getBusinessStatusCode()));
        }
        saveLog(ticketOpLogsReqVO);
        updateStatus(ticketOpLogsReqVO.getId(), TICKET_STATUS_CHECK);
        ticketOpLogsReqVO.setTaskId(taskRepDto.getTaskId());
        workFlowPushWechatMsg(ticketOpLogsReqVO, ACCEPTANCE_WORKFLOW_ID);
        return ApiResult.success();
    }

    /**
     * 清除驳回记录
     *
     * @param id
     * @param type
     */
    private void clearBackRecord(Long id, String type) {
        ticketDao.clearBackRecord(id, type);
    }


    private String getMsg(String msg, String msg2, Integer agree, String workflowId) {
        if (agree != null) {
            String workflowName = DISPATCH_OP_TYPE_NAME;
            if (workflowId == ACCEPTANCE_WORKFLOW_ID) {
                workflowName = ACCEPT_OP_TYPE_NAME;
            }
            String agreeName = TICKET_AGREE_YES_NAME;
            if (agree == TICKET_AGREE_NO) {
                agreeName = TICKET_AGREE_NO_NAME;
            }
            if (agree == TICKET_AGREE_RETURN) {
                agreeName = TICKET_AGREE_RETURN_NAME;
            }
            if (StringUtils.isEmpty(msg2)) {
                msg2 = " ";
            }
            return workflowName.concat("#").concat(msg2).concat("#").concat(agreeName).concat("   ").concat(msg);
        } else {
            return msg2;
        }

    }


    /**
     * 验收单流转方法
     *
     * @param circulationTaskDto
     * @param userName
     * @return
     */
    @Override
    public TaskRepDto circulationAcceptance(CirculationTaskDto circulationTaskDto, String userName) {
        circulationTaskDto.setUsername(userName);
        circulationTaskDto.setWorkflowId(ACCEPTANCE_WORKFLOW_ID);
        return ticketWorkflowService.circulationTicketTask(circulationTaskDto);
    }

    @Override
    public ApiResult circulationAcceptance(TicketOpLogsReqVO ticketOpLogsReqVO) {
        circulation(ticketOpLogsReqVO, ACCEPTANCE_WORKFLOW_ID);
        ///todo 维护最近一次验收单审批时间
        this.maintainApprovedTime(ticketOpLogsReqVO,ACCEPTANCE_WORKFLOW_ID);
        this.autoInspected(ticketOpLogsReqVO.getId());
        return ApiResult.success();
    }

    private void maintainApprovedTime(TicketOpLogsReqVO ticketOpLogsReqVO, String workflowId) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketOpLogsReqVO.getId());
        if(DISPATCH_WORKFLOW_ID.equals(workflowId)){
            ticket.setDispatchApprovalTime(DateHelper.getNow());
        }else if(ACCEPTANCE_WORKFLOW_ID.equals(workflowId)){
            ticket.setAcceptApprovalTime(DateHelper.getNow());
        }
        if(!StringHelper.isEmpty(ticket.getDispatchApprovalTime())||!StringHelper.isEmpty(ticket.getAcceptApprovalTime())){
            ticketDao.maintainApprovedTime(ticket);
        }

    }

    /** ------------------------------自动验收开始----------------------------------------**/
    /**
     * 自动验收逻辑及方法
     * @param id
     */
    private void autoInspected(Long id) {
        TicketWorkflowDto ticketWorkflowDto = ticketDao.getById(id.toString());
        if(null!=ticketWorkflowDto&&ticketWorkflowDto.getDispatchStatus()==20
                &&ticketWorkflowDto.getAcceptStatus()==20&&ticketWorkflowDto.getTicketStatus()!=20){
            Ticket temp = new Ticket();
            temp.setId(id);
            temp.setTicketStatus(20);
            temp.setMaintenanceStatus(20);
            ticketDao.update(temp);
        }
    }


    /** ------------------------------自动验收结束----------------------------------------**/

    /**
     * 我的验收单流程列表
     *
     * @param userName
     * @return
     */
    @Override
    public List<TicketWorkflowDto> myAcceptanceList(String userName) {
        List<TicketWorkflowDto> taskInfoDetailDtoList = ticketWorkflowService.myTicketFlowList(userName, ACCEPTANCE_WORKFLOW_ID);
        return taskInfoDetailDtoList;
    }

    /**
     * 发起维修派工方法
     *
     * @param startFlowDto
     * @param userName
     * @return
     */
    @Override
    public TaskRepDto startMaintain(StartFlowDto startFlowDto, String userName) {
        return startWorkflow(startFlowDto, userName, MAINTAIN_WORKFLOW_ID);
    }

    /**
     * 维修派工流转方法
     *
     * @param circulationTaskDto
     * @param userName
     * @return
     */
    @Override
    public TaskRepDto circulationMaintain(CirculationTaskDto circulationTaskDto, String userName) {
        circulationTaskDto.setUsername(userName);
        circulationTaskDto.setWorkflowId(MAINTAIN_WORKFLOW_ID);
        return ticketWorkflowService.circulationTicketTask(circulationTaskDto);
    }

    /**
     * 我的维修待办列表
     *
     * @param userName
     * @return
     */
    @Override
    public List<TicketWorkflowDto> myMaintainList(String userName) {
        List<TicketWorkflowDto> taskInfoDetailDtoList = ticketWorkflowService.myTicketFlowList(userName, MAINTAIN_WORKFLOW_ID);
        return taskInfoDetailDtoList;
    }

    /**
     * 通过流程实例id拿到工单数据
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public TicketWorkflowDto getByProcessInstanceId(String processInstanceId) {
        return ticketDao.getByProcessInstanceId(processInstanceId);
    }


    /**
     * 通过id查询工单信息   需要修改
     *
     * @param id
     * @return
     */
    @Override
    public TicketWorkflowDto getById(String id) {
        return ticketDao.getById(id);
    }

    /**
     * 工单详情
     *
     * @param id
     * @param taskId
     * @return
     */
    @Override
    public TicketWorkflowDto ticketDetail(String id, String taskId) {
        return ticketWorkflowService.getByIdAndTaskId(id, taskId);
    }


    /**
     * 添加我的工单待办列表通用接口
     *
     * @param userName 用户名
     * @return
     */
    @Override
    public List<TicketWorkflowDto> myPendingApprovalTicketList(String userName) {
        List<String> workflowIdList = new ArrayList<>();
        workflowIdList.add(DISPATCH_WORKFLOW_ID);
        workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);

        List<TicketWorkflowDto> ticketWorkflowDtoList = ticketWorkflowService.myTicketFlowList(userName, workflowIdList);
        return ticketWorkflowDtoList;
    }




    /*   *//**
     * 添加我的工单待办列表通用接口 分页
     *
     * @param userName 用户名
     * @return
     *//*
    @Override
    public PageResult<TicketWorkflowDto> myPendingApprovalTicketList(String userName, Integer pageNum, Integer pageSize) {
        List<String> workflowIdList = new ArrayList<>();
        workflowIdList.add(DISPATCH_WORKFLOW_ID);
        workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        PageResult<TicketWorkflowDto> pageDto = ticketWorkflowService.myTicketFlowList(userName, workflowIdList, pageNum, pageSize);
        return pageDto;
    }*/

    /**
     * 添加我的工单待办列表通用接口 分页
     * 2021/10/14改造
     * @param reqVO 查询参数
     * @return
     */
    public PageResult<AppPendingApprovalRespVO> myPendingApprovalTicketList( AppPendingApprovalReqVO reqVO) {
        List<String> workflowIdList = new ArrayList<>();
        if(StringHelper.isEmpty(reqVO.getType())){
            workflowIdList.add(DISPATCH_WORKFLOW_ID);
            workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        }else if (DISPATCH_WORKFLOW_ID.equals(reqVO.getType())){
            workflowIdList.add(DISPATCH_WORKFLOW_ID);
        }else if (ACCEPTANCE_WORKFLOW_ID.equals(reqVO.getType())){
            workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        }
        reqVO.setWorkflowIdList(workflowIdList);
        //PageResult<TicketWorkflowDto> pageDto = ticketWorkflowService.myTicketFlowList(userName, workflowIdList, pageNum, pageSize);
        PageResult<AppPendingApprovalRespVO> pageDto = ticketWorkflowService.myTicketFlowList(reqVO);
        return pageDto;
    }

    private List<AppPendingApprovalRespVO>  getOnePendingTicket(Long id) {
        return ticketWorkflowService.getOnePendingTicket(id);
    }



    /**
     * 已审批工单列表 2021/10/14改造
     *
     * @param reqVO
     * @return
     */
    @Override
    public PageResult<AppPendingApprovalRespVO> myApprovedTicketList(AppPendingApprovalReqVO reqVO) {
        List<String> workflowIdList = new ArrayList<>();
        if(StringHelper.isEmpty(reqVO.getType())){
            workflowIdList.add(DISPATCH_WORKFLOW_ID);
            workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        }else if (DISPATCH_WORKFLOW_ID.equals(reqVO.getType())){
            workflowIdList.add(DISPATCH_WORKFLOW_ID);
        }else if (ACCEPTANCE_WORKFLOW_ID.equals(reqVO.getType())){
            workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        }
        reqVO.setWorkflowIdList(workflowIdList);
        PageResult<AppPendingApprovalRespVO> pageDto = ticketWorkflowService.myApprovedTicketList(reqVO);
        return pageDto;
    }

    /**
     * 我的所有工单列表查询 分页 2021/10/14 15：17改造
     *
     * @param reqVO 请求参数
     * @return
     */
    @Override
    public PageResult<AppPendingApprovalRespVO> myTicketList(AppPendingApprovalReqVO reqVO) {
        List<String> workflowIdList = new ArrayList<>();
        if(StringHelper.isEmpty(reqVO.getType())){
            workflowIdList.add(DISPATCH_WORKFLOW_ID);
            workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        }else if (DISPATCH_WORKFLOW_ID.equals(reqVO.getType())){
            workflowIdList.add(DISPATCH_WORKFLOW_ID);
        }else if (ACCEPTANCE_WORKFLOW_ID.equals(reqVO.getType())){
            workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        }
        reqVO.setWorkflowIdList(workflowIdList);
        PageResult<AppPendingApprovalRespVO> pageResult = ticketWorkflowService.myAllTicketList(reqVO);
        return pageResult;
    }


    /**
     * 我的所有工单列表查询
     *
     * @param userName 用户名
     * @return
     */
    @Override
    public List<TicketWorkflowDto> myTicketList(String userName) {
        List<String> workflowIdList = new ArrayList<>();
        workflowIdList.add(DISPATCH_WORKFLOW_ID);
        workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        workflowIdList.add(MAINTAIN_WORKFLOW_ID);
        List<TicketWorkflowDto> ticketWorkflowDtoList = ticketWorkflowService.myTicketList(userName, workflowIdList);
        return ticketWorkflowDtoList;
    }





    /*****************工作流结束**********************/


    /****************************通用方法开始**********************************/
    /******
     * 工单流程通用发起方法
     * @param startFlowDto
     * @param userName
     * @param workflowId
     * @return
     */
    public TaskRepDto startWorkflow(StartFlowDto startFlowDto, String userName, String workflowId) {
        Map<String, Object> map = new HashMap<>();
        if (!StringHelper.isEmpty(startFlowDto.getConditionDtoList())) {
            //此处map放流程判断条件 如金额  此处直接写死 集成时修改
            for (ConditionDto conditionDto : startFlowDto.getConditionDtoList()) {
                if (!StringUtils.isEmpty(conditionDto.getValue())) {
                    map.put(conditionDto.getKey(), conditionDto.getValue());
                } else {
                    map.put(conditionDto.getKey(), conditionDto.getDoubleValue());
                }
            }
        }
        TaskRepDto taskRepDto = ticketWorkflowService.startTicketFlow(userName, startFlowDto.getId(), map, workflowId);
        return taskRepDto;

    }


    /**************************通用方法结束*****************************************/


    // ------------------------------------------------监理、业主（待审批列表、已审批列表、全部列表）start------------------------
    @Override
    public ApiResult myPendingAndApprovalTicketList(AppPendingApprovalReqVO reqVO, UserSession userSession) {
        if (reqVO.getApprovalType() == null || reqVO.getApprovalType().equals(0)) {
            //默认 2.待审批
            reqVO.setApprovalType(TICKET_PENDING_APPROVAL);
        }
        reqVO.setAssign(userSession.getUserName());
        PageResult<AppPendingApprovalRespVO> pageResult = new PageResult<>(0, new ArrayList<>(), 1, 10);
        //待审批数据查询
        if (TICKET_PENDING_APPROVAL.equals(reqVO.getApprovalType())) {
            //pageResult = this.myPendingApprovalTicketList(userSession.getUserName(), reqVO.getPageNum(), reqVO.getPageSize());
            pageResult = this.myPendingApprovalTicketList(reqVO);
        }
        //已审批数据查询
        if (TICKET_APPROVAL.equals(reqVO.getApprovalType())) {
            pageResult = this.myApprovedTicketList(reqVO);
        }
        //全部工单数据查询
        if (TICKET_All_APPROVAL.equals(reqVO.getApprovalType())) {
            pageResult = this.myTicketList(reqVO);
        }

        List<AppPendingApprovalRespVO> list = pageResult.getList();
        if (list != null && list.size() > 0) {
            for (AppPendingApprovalRespVO appPendingApprovalRespVO : list) {
                if (TICKET_PENDING_APPROVAL.equals(reqVO.getApprovalType())) {
                    appPendingApprovalRespVO.setAcceptanceStatusName("待审批");
                    appPendingApprovalRespVO.setDispatchStatusName("待审批");
                } else if (TICKET_APPROVAL.equals(reqVO.getApprovalType())) {
                    appPendingApprovalRespVO.setAcceptanceStatusName("已审批");
                    appPendingApprovalRespVO.setDispatchStatusName("已审批");
                } else {

                    //查询待办所有数据 做遍历
                    List<AppPendingApprovalRespVO> tempAppPending = this.getOnePendingTicket(appPendingApprovalRespVO.getId());
                    if(!StringHelper.isEmpty(tempAppPending)){
                        for(AppPendingApprovalRespVO temp : tempAppPending){


                            if (DISPATCH_WORKFLOW_ID.equals(temp.getWorkflowStr()) && DISPATCH_WORKFLOW_ID.equals(appPendingApprovalRespVO.getWorkflowStr()) && temp.getId().equals(appPendingApprovalRespVO.getId())) {
                                appPendingApprovalRespVO.setDispatchStatusName("待审批");
                            }
                            if (ACCEPTANCE_WORKFLOW_ID.equals(temp.getWorkflowStr()) && ACCEPTANCE_WORKFLOW_ID.equals(appPendingApprovalRespVO.getWorkflowStr()) && temp.getId().equals(appPendingApprovalRespVO.getId())) {
                                appPendingApprovalRespVO.setAcceptanceStatusName("待审批");

                            }
                        }
                    }
                    if (!StringUtils.isEmpty(appPendingApprovalRespVO.getDispatchStatusName()) && !StringUtils.isEmpty(appPendingApprovalRespVO.getAcceptanceStatusName())) {
                        break;
                    }

                    if (StringUtils.isEmpty(appPendingApprovalRespVO.getDispatchStatusName())) {
                        appPendingApprovalRespVO.setDispatchStatusName("已审批");
                    }
                    if (StringUtils.isEmpty(appPendingApprovalRespVO.getAcceptanceStatusName())) {
                        appPendingApprovalRespVO.setAcceptanceStatusName("已审批");
                    }
                }
            }
        }
        return ApiResult.success(new PageResult(pageResult.getTotalNum(), list, pageResult.getPageSize(), pageResult.getPageNum()));
    }




    /* *//**
     * 内场运维 外场运维 已审批工单
     * @param orgId
     * @param pageNum
     * @param pageSize
     * @return
     *//*
    private PageResult<TicketWorkflowDto> maintainApprovedTicketList(String orgId, Integer pageNum, Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TicketWorkflowDto> result = ticketDao.maintainApprovedTicketList(orgId);
        return new PageResult<TicketWorkflowDto>(page.getTotal(), result, pageSize, pageNum);
    }

    *//**
     * 内场运维 外场运维所有工单
     * @param orgId
     * @param pageNum
     * @param pageSize
     * @return
     *//*
    private PageResult<TicketWorkflowDto> maintainTicketList(String orgId, Integer pageNum, Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TicketWorkflowDto> result = ticketDao.maintainTicketList(orgId);
        return new PageResult<TicketWorkflowDto>(page.getTotal(), result, pageSize, pageNum);
    }

    */

    /**
     * 内场运维 外场运维待审批工单
     *
     * @return
     *//*
    private PageResult<TicketWorkflowDto> maintainApprovalTicketList(String orgId, Integer pageNum, Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TicketWorkflowDto> result = ticketDao.maintainApprovalTicketList(orgId);
        return new PageResult<TicketWorkflowDto>(page.getTotal(), result, pageSize, pageNum);
    }*/
    @Override
    public ApiResult appViewDispatch(Long id) {
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


//        String taskId=workflowTaskInfoMapper.getByBusinessIdAndWorkflowId(id.toString(),DISPATCH_WORKFLOW_ID);
//        TaskRepDto taskInfo = new TaskRepDto();
//        if(!StringHelper.isEmpty(taskId)){
//            taskInfo = workFlowService.getTaskInfoByTaskId(taskId);
//        }

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(TICKETS_DISPATCH_INFO, ticketsDispatchViewRespVO)
                .put(TICKETS_DISPATCH_VIEW_INFO, getLogs(id, STATUS_DISABLE, DISPATCH_OP_TYPE, ticketsOpStatusList))
                .put(TICKETS_DISPATCH_LOGS_INFO, getLogs(id, STATUS_ENABLE, DISPATCH_OP_TYPE, ticketsOpStatusList))
                .put(TICKETS_ACCEPT_FLOW, getTicketFlow(id, DISPATCH_WORKFLOW_ID, DISPATCH_OP_TYPE, ticketsDispatchViewRespVO.getAcceptStatus()))
                //【new】添加任务信息返回值
//                .put("taskInfo",taskInfo)
                .build();
        return ApiResult.success(result);
    }

    @Override
    public ApiResult appViewAccept(Long id) {
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
                .put(TICKETS_ACCEPT_FLOW, getTicketFlow(id, ACCEPTANCE_WORKFLOW_ID, ACCEPT_OP_TYPE, ticketsAcceptViewRespVO.getAcceptStatus()))
                .build();
        return ApiResult.success(result);

    }


    @Override
    public ApiResult addDispatch(TicketOpLogsReqVO ticketOpLogsReqVO) {
        Ticket tickets = new Ticket();
        BeanUtils.copyProperties(ticketOpLogsReqVO, tickets);
        ticketOpLogsReqVO.setMsg("添加#派工单");
        saveLogsAndUpdateStatus(ticketOpLogsReqVO, tickets);
        return ApiResult.success();
    }

    @Override
    public ApiResult delDispatch(TicketOpLogsReqVO ticketOpLogsReqVO) {
        Ticket tickets = new Ticket();
        BeanUtils.copyProperties(ticketOpLogsReqVO, tickets);
        ticketOpLogsReqVO.setMsg("删除#派工单");
        saveLogsAndUpdateStatus(ticketOpLogsReqVO, tickets);
        //合并之前 删除派工单跟验收单的流程 防止退回的流程 发起合并 拥有两个流程的问题

        //清除父工单的is_merge并删除父工单及流程
        String dispatchPrecessInstanceId = workflowTaskInfoMapper.getProcessInstanceByIdAndType(ticketOpLogsReqVO.getId(), DISPATCH_WORKFLOW_ID);
        String acceptPrecessInstanceId = workflowTaskInfoMapper.getProcessInstanceByIdAndType(ticketOpLogsReqVO.getId(), ACCEPTANCE_WORKFLOW_ID);

        //通过流程实例id删除实例
        if (!StringUtils.isEmpty(dispatchPrecessInstanceId)) {
            Boolean flag = ticketWorkflowService.deleteProcessInstance(dispatchPrecessInstanceId);
            workflowTaskInfoMapper.deleteByBusinessIdAndType(ticketOpLogsReqVO.getId(), DISPATCH_WORKFLOW_ID);

        }
        if (!StringUtils.isEmpty(acceptPrecessInstanceId)) {
            Boolean flag = ticketWorkflowService.deleteProcessInstance(acceptPrecessInstanceId);
            workflowTaskInfoMapper.deleteByBusinessIdAndType(ticketOpLogsReqVO.getId(), ACCEPTANCE_WORKFLOW_ID);
        }


        return ApiResult.success();
    }

    /**
     * 工单删除方法
     *
     * @param id
     * @return
     */
    @Override
    public ApiResult delete(Long id) {
        TicketSearchRespVO vo = ticketDao.queryById(id);
        //根据工单id查询日志
        List<SelectVO> ticketsOpStatusList = constantItemService.findItemsByDict(MODULE_TICKET_OP);
        List<TicketOpLogsRespVO> logs = getLogs(id, STATUS_ENABLE, null, ticketsOpStatusList);
        if (vo.getAcceptStatus() <= 2 && vo.getDispatchStatus() <= 2 ) {
            //todo 工单删除
            //先删除流程相关
            //清除父工单的is_merge并删除父工单及流程
            String dispatchPrecessInstanceId = workflowTaskInfoMapper.getProcessInstanceByIdAndType(id, DISPATCH_WORKFLOW_ID);
            String acceptPrecessInstanceId = workflowTaskInfoMapper.getProcessInstanceByIdAndType(id, ACCEPTANCE_WORKFLOW_ID);

            //通过流程实例id删除实例
            if (!StringUtils.isEmpty(dispatchPrecessInstanceId)) {
                Boolean flag = ticketWorkflowService.deleteProcessInstance(dispatchPrecessInstanceId);
                workflowTaskInfoMapper.deleteByBusinessIdAndType(id, DISPATCH_WORKFLOW_ID);

            }
            if (!StringUtils.isEmpty(acceptPrecessInstanceId)) {
                Boolean flag = ticketWorkflowService.deleteProcessInstance(acceptPrecessInstanceId);
                workflowTaskInfoMapper.deleteByBusinessIdAndType(id, ACCEPTANCE_WORKFLOW_ID);
            }
            //先删除关联的数据 工单详情 点位 设备等
            ticketDetailDao.deleteByTicketId(id, null);
            ticketPointDao.deleteByTicketId(id, null);
            ticketPointDeviceDao.deleteByTicketId(id, null);
            ticketDao.deleteById(id);
            return ApiResult.success();
        } else {
            return ApiResult.error("工单数据已发生变化，无法删除！");
        }
    }

    // ------------------------------------------------监理、业主（待审批列表、已审批列表、全部列表）end  ------------------------

    /**
     * 巡检故障发起工单
     *
     * @param clockIn
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void createTicket(PatrolClockIn clockIn) {
        //根据点位id查询点位信息
        DevicePoint devicePoint = devicePointService.getById(Long.parseLong(clockIn.getPointId().toString()));
        SysUser sysUser = sysUserDao.findUserByName(clockIn.getCheckUser());
        Ticket ticket = new Ticket();
        //设置工单编码
        BeanUtils.copyProperties(devicePoint, ticket);
        ticket.setTicketCode(serialNumService.generateTicketSerialNum());
        ticket.setTicketCodeResult(ticket.getTicketCode());
        ticket.setIssueType(1);
        ticket.setIsCost(0);
        ticket.setRemark(clockIn.getCheckRemark());
        ticket.setAdjunctId(clockIn.getAdjunctUuid());
        ticket.setOccurredTime(clockIn.getCheckDate());
        ticket.setIssueSource(9);
        ticket.setDispatchStatus(DISPATCH_STATUS_START);
        ticket.setAcceptStatus(ACCEPT_STATUS_START);
        ticket.setTicketStatus(TICKET_STATUS_START);
        //生成故障工单
        //工单编辑已完成
        ticket.setEditStatus(EDIT_STATUS_SUC);
        //分配单位
        ticket.setAllocDepartmentNum(sysUser.getMaintainNum());
        //设置故障级别
        ticket.setIssueLevel(TICKET_ISSUE_LEVEL_COMMONLY);
        //生成故障工单
        ticket.setTicketType(TICKET_TYPE_ISSUE_STATUS);
        //设置创建平台
        ticket.setOpPlatform("小程序端");
        //如果责任人为空的话
        if (StringUtils.isEmpty(ticket.getDutyContact()) && !StringUtils.isEmpty(ticket.getAllocDepartmentNum())) {
            String dutyContact = baseOrgService.queryDutyInfo(ticket.getAllocDepartmentNum());
            ticket.setDutyContact(dutyContact);
        }
        String contact = baseOrgService.queryContactInfo(ticket.getAllocDepartmentNum());
        ticket.setApplyContact(contact);
        // 根据联系人 查询手机号
        String contactPhone = sysUserDao.getPhone(contact);
        if(!StringUtils.isEmpty(contactPhone)){
            ticket.setApplyPhone(contactPhone);
        }
        ticket.setCreator(clockIn.getCheckUser());
        //插入
        ticketDao.insert(ticket);
        //生成故障工单
        ticket.setTicketType(TICKET_TYPE_ISSUE_STATUS);
        //工单编辑已完成
        ticket.setEditStatus(EDIT_STATUS_SUC);
        resetPredictArriveAndCompleteTime(ticket);
        ticketDao.update(ticket);
        Long ticketId = ticket.getId();
        TicketPoint ticketPoint = new TicketPoint();
        ticketPoint.setCreator(clockIn.getCheckUser());
        ticketPoint.setTicketId(ticketId);
        ticketPoint.setCreateTime(new Date());
        ticketPoint.setPointId(Long.parseLong(clockIn.getPointId().toString()));
        ticketPointDao.insert(ticketPoint);
        //工单创建完后，巡检日志与工单关联
        Long resultId = patrolTaskCheckResultMapper.getCheckResultId(clockIn.getPointId());
        //关联工单
        patrolTaskCheckResultMapper.relationTicket(resultId,ticket.getId());
        //工单关联该巡检记录
        ticketDao.relationPatrolTaskCheckResult(ticket.getId(),resultId);
 /*       //要微信消息推送
        TicketSaveReqVO ticketSaveReqVO = new TicketSaveReqVO();
        ticketSaveReqVO.setCreator(ticket.getCreator());
        ticketCreateMsgWeChatPush(ticketSaveReqVO, ticket);*/
    }

    /**
     * 导出已验收工单的派工单和验收单数据
     *
     * @param ticketBatchExport
     * @return
     */
    @Override
    public String exportTicket(TicketBatchExportVO ticketBatchExport) {
        //先通过id查询出所有的工单数据
        List<Ticket> ticketList = ticketDao.getByIds(ticketBatchExport.getIds());
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String path = exportFolder + File.separator + date;
        File pathFile = new File(path);
        pathFile.mkdirs();
        if (StringUtils.isEmpty(ticketBatchExport.getType())) {
            for (Ticket ticket : ticketList) {
                if (ticket.getAcceptStatus() > 1) {
                    String exportAccept = this.exportAccept(ticket.getTicketCode());
            /*        ticketCodePath = path + File.separator + ticket.getTicketCodeResult();
                    File file = new File(ticketCodePath);
                    file.mkdirs();*/
                    FileHelper.fileCopy(exportFolder + File.separator + exportAccept, path + File.separator + exportAccept);
                }
                if (ticket.getDispatchStatus() > 1) {
                    String exportDispatch = this.exportDispatch(ticket.getTicketCode());
                    FileHelper.fileCopy(exportFolder + File.separator + exportDispatch, path + File.separator + exportDispatch);

                 /*   if (!StringUtils.isEmpty(ticketCodePath)) {
                        FileHelper.fileCopy(exportFolder + File.separator + exportDispatch, path + File.separator + exportDispatch);
                    } else {
                        ticketCodePath = path + File.separator + ticket.getTicketCodeResult();
                        File file = new File(ticketCodePath);
                        file.mkdirs();
                        FileHelper.fileCopy(exportFolder + File.separator + exportDispatch, ticketCodePath + File.separator + exportDispatch);

                    }*/
                }
            }
        } else {
            if (ticketBatchExport.getType().equals("dispatch")) {
                for (Ticket ticket : ticketList) {
                    if (ticket.getDispatchStatus() > 1) {
                        String exportDispatch = this.exportDispatch(ticket.getTicketCode());
                /*        ticketCodePath = path + File.separator + ticket.getTicketCodeResult();
                        File file = new File(ticketCodePath);
                        file.mkdirs();*/
                        FileHelper.fileCopy(exportFolder + File.separator + exportDispatch, path + File.separator + exportDispatch);
                    }
                }
            }
            if (ticketBatchExport.getType().equals("acceptance")) {
                for (Ticket ticket : ticketList) {
                    if (ticket.getAcceptStatus() > 1) {
                        String exportAccept = this.exportAccept(ticket.getTicketCode());
                     /*   ticketCodePath = path + File.separator + ticket.getTicketCodeResult();
                        File file = new File(ticketCodePath);
                        file.mkdirs();*/
                        FileHelper.fileCopy(exportFolder + File.separator + exportAccept, path + File.separator + exportAccept);
                    }
                }
            }
        }
        String desPathZip = path + ".zip";
        try {
            FileZip.ZipCompress(path, desPathZip);
        } catch (Exception e) {
            log.error("文件压缩失败!", e);
        }
        return desPathZip;
    }

    @Override
    public List<SelectVO> getDispatchInit() {
        return workflowTaskInfoMapper.getDispatchInit();
    }

    @Override
    public List<SelectVO> getAcceptanceInit() {
        return workflowTaskInfoMapper.getAcceptanceInit();
    }

    @Override
    public void update(Ticket ticket) {
        //进行工单基本信息维护
        try {
            ticketDao.updateBase(ticket);
        } catch (Exception e) {
            log.error("工单基本信息修改失败，{}", e);
            throw new BusinessException("工单基本信息修改失败！");
        }

    }

    @Override
    public ApiResult checkCode(TicketAcceptViewEditVO tickets) {
        if (!StringUtils.isEmpty(tickets.getTicketCodeResult())) {
            Ticket t = new Ticket();
            t.setId(tickets.getId());
            t.setTicketCodeResult(tickets.getTicketCodeResult());
            if (ticketDao.isExistTicketCodeRsult(t) > 0) {
                log.error("工单实际编号已存在，{}", tickets.getTicketCodeResult());
                return ApiResult.success(false);
            }
        }
        return ApiResult.success(true);
    }


    /*************************************查询语句实现工单待审批/已审批/全部查询**********************************************/


    /*************************************查询语句实现工单待审批/已审批/全部查询**********************************************/


    // --------------------------------------2021/07/22报表相关代码开始------------------------------------------------------

    /**
     * 故障工单统计
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public AppTicketChartVO breakdownTicket(AppTicketCharReqVO appTicketCharReqVO) {
        AppTicketChartVO appTicketChartVO = new AppTicketChartVO();
        //通过type查询统计图数据
        List<String> rowNameList = new ArrayList<>();
        Collections.addAll(rowNameList, "汉口片区", "武昌片区", "汉阳片区");
        appTicketChartVO.setCategories(rowNameList);
        List<Map<String, Object>> seriesList = new ArrayList<>();
        List<Integer> total = ticketDao.getBreakdownTicketTotal(appTicketCharReqVO);
        Map<String, Object> col1 = new HashMap<>();
        col1.put("name", "故障工单总数");
        col1.put("data", total);
        col1.put("color", "#6C75FF");
        seriesList.add(col1);
        List<Integer> complete = ticketDao.getBreakdownTicketComplete(appTicketCharReqVO);
        Map<String, Object> col2 = new HashMap<>();
        col2.put("name", "工单完工");
        col2.put("data", complete);
        col2.put("color", "#37CBCB");
        seriesList.add(col2);
        List<Integer> completeNum = ticketDao.getBreakdownTicketCompleteNum(appTicketCharReqVO);
       /* Map<String, Object> col3 = new HashMap<>();
        if(type==1){
            col3.put("name","今日完成");
        }else if(type==2){
            col3.put("name","近三天完成");
        }else if(type==3){
            col3.put("name","近一周完成");
        }

        col3.put("data",completeNum);
        seriesList.add(col3);*/
        appTicketChartVO.setSeries(seriesList);

        return appTicketChartVO;
    }

    /**
     * 故障table报表
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public List<AppFaultTicketChartVO> breakdownTicketTable(AppTicketCharReqVO appTicketCharReqVO) {
        //查询三个片区的故障工单总数 工单完工数量 一周内完成数量
        List<AppFaultTicketChartVO> breakdownTicketTable = new ArrayList<>();
        List<Integer> total = ticketDao.getBreakdownTicketTotal(appTicketCharReqVO);
        List<Integer> complete = ticketDao.getBreakdownTicketComplete(appTicketCharReqVO);
        List<Integer> completeNum = ticketDao.getBreakdownTicketCompleteNum(appTicketCharReqVO);
        AppFaultTicketChartVO first = new AppFaultTicketChartVO();
        first.setAreaName("汉口片区");
        first.setTotal(total.get(0).toString());
        first.setComplete(complete.get(0).toString());
        first.setCompleteInDay(completeNum.get(0).toString());
        AppFaultTicketChartVO second = new AppFaultTicketChartVO();
        second.setAreaName("武昌片区");
        second.setTotal(total.get(1).toString());
        second.setComplete(complete.get(1).toString());
        second.setCompleteInDay(completeNum.get(1).toString());
        AppFaultTicketChartVO third = new AppFaultTicketChartVO();
        third.setAreaName("汉阳片区");
        third.setTotal(total.get(2).toString());
        third.setComplete(complete.get(2).toString());
        third.setCompleteInDay(completeNum.get(2).toString());
        AppFaultTicketChartVO fourth = new AppFaultTicketChartVO();
        fourth.setAreaName("总计");
        Integer tot = total.get(0) + total.get(1) + total.get(2);
        fourth.setTotal(tot.toString());
        Integer com = complete.get(0) + complete.get(1) + complete.get(2);
        fourth.setComplete(com.toString());
        Integer num = completeNum.get(0) + completeNum.get(1) + completeNum.get(2);
        fourth.setCompleteInDay(num.toString());
        breakdownTicketTable.add(first);
        breakdownTicketTable.add(second);
        breakdownTicketTable.add(third);
        breakdownTicketTable.add(fourth);
        return breakdownTicketTable;
    }

    /**
     * 昨日比较
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public Map<String, Object> breakdownTicketCompare(AppTicketCharReqVO appTicketCharReqVO) {
        Map<String, Object> map = new HashMap<>();
        appTicketCharReqVO.setOrgId("10001");
        Map<String, Integer> firstBreakdownTicketCompare = ticketDao.breakdownTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c1 = new HashMap<>();
        c1.put("total", firstBreakdownTicketCompare.get("total"));
        c1.put("complete", firstBreakdownTicketCompare.get("complete"));
        c1.put("rate", firstBreakdownTicketCompare.get("rate"));
        appTicketCharReqVO.setOrgId("10002");
        Map<String, Integer> secondBreakdownTicketCompare = ticketDao.breakdownTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c2 = new HashMap<>();
        c2.put("total", secondBreakdownTicketCompare.get("total"));
        c2.put("complete", secondBreakdownTicketCompare.get("complete"));
        c2.put("rate", secondBreakdownTicketCompare.get("rate"));
        appTicketCharReqVO.setOrgId("10003");
        Map<String, Integer> thirdBreakdownTicketCompare = ticketDao.breakdownTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c3 = new HashMap<>();
        c3.put("total", thirdBreakdownTicketCompare.get("total"));
        c3.put("complete", thirdBreakdownTicketCompare.get("complete"));
        c3.put("rate", thirdBreakdownTicketCompare.get("rate"));
        map.put("hk", c1);
        map.put("wc", c2);
        map.put("hy", c3);
        return map;
    }

    /**
     * 派工单图表统计
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public AppTicketChartVO dispatchTicket(AppTicketCharReqVO appTicketCharReqVO) {
        AppTicketChartVO appTicketChartVO = new AppTicketChartVO();
        //通过type查询统计图数据
        List<String> rowNameList = new ArrayList<>();
        Collections.addAll(rowNameList, "汉口片区", "武昌片区", "汉阳片区");
        appTicketChartVO.setCategories(rowNameList);
        List<Map<String, Object>> seriesList = new ArrayList<>();
        List<Integer> dispatchTotal = ticketDao.getDispatchTotal(appTicketCharReqVO);
        Map<String, Object> col1 = new HashMap<>();
        col1.put("name", "工单申请数量");
        col1.put("data", dispatchTotal);
        col1.put("color", "#6E75FD");
        seriesList.add(col1);
        List<Integer> completeDispatchTotal = ticketDao.getCompleteDispatchTotal(appTicketCharReqVO);
        Map<String, Object> col2 = new HashMap<>();
        col2.put("name", "工单审批数量");
        col2.put("data", completeDispatchTotal);
        col2.put("color", "#37CBCB");
        seriesList.add(col2);
        List<Integer> moneyDispatchTotal = ticketDao.getMoneyDispatchTotal(appTicketCharReqVO);
        Map<String, Object> col3 = new HashMap<>();
        col3.put("name", "单笔>3000");
        col3.put("data", moneyDispatchTotal);
        col3.put("color", "#4ECA70");
        seriesList.add(col3);
        List<Integer> maxMoneyDispatchTotal = ticketDao.getMaxMoneyDispatchTotal(appTicketCharReqVO);
        Map<String, Object> col4 = new HashMap<>();
        col4.put("name", "单笔>10000");
        col4.put("data", maxMoneyDispatchTotal);
        col4.put("color", "#FBD336");
        seriesList.add(col4);
        appTicketChartVO.setSeries(seriesList);
        return appTicketChartVO;
    }

    /**
     * 派工单表格
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public List<AppDispatchTicketChartVO> dispatchTicketTable(AppTicketCharReqVO appTicketCharReqVO) {
        List<AppDispatchTicketChartVO> dispatchTicketTable = new ArrayList<>();
        List<Integer> dispatchTotal = ticketDao.getDispatchTotal(appTicketCharReqVO);
        List<Integer> completeDispatchTotal = ticketDao.getCompleteDispatchTotal(appTicketCharReqVO);
        List<Integer> moneyDispatchTotal = ticketDao.getMoneyDispatchTotal(appTicketCharReqVO);
        List<Integer> maxMoneyDispatchTotal = ticketDao.getMaxMoneyDispatchTotal(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10001");
        List<String> firstCost = ticketDao.getdispatchPredictCost(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10002");
        List<String> SecondCost = ticketDao.getdispatchPredictCost(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10003");
        List<String> thirdCost = ticketDao.getdispatchPredictCost(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId(null);
        List<String> sumCost = ticketDao.getdispatchPredictCost(appTicketCharReqVO);
        AppDispatchTicketChartVO first = new AppDispatchTicketChartVO();
        first.setAreaName("汉口片区");
        first.setTotal(dispatchTotal.get(0).toString());
        first.setTotalMoney(firstCost.get(0));
        first.setComplete(completeDispatchTotal.get(0).toString());
        first.setCompleteMoney(firstCost.get(3));
        first.setMin(moneyDispatchTotal.get(0).toString());
        first.setMinMoney(firstCost.get(1));
        first.setMax(maxMoneyDispatchTotal.get(0).toString());
        first.setMaxMoney(firstCost.get(2));
        AppDispatchTicketChartVO second = new AppDispatchTicketChartVO();
        second.setAreaName("武昌片区");
        second.setTotal(dispatchTotal.get(1).toString());
        second.setComplete(completeDispatchTotal.get(1).toString());
        second.setMin(moneyDispatchTotal.get(1).toString());
        second.setMax(maxMoneyDispatchTotal.get(1).toString());
        second.setTotalMoney(SecondCost.get(0));
        second.setMinMoney(SecondCost.get(1));
        second.setMaxMoney(SecondCost.get(2));
        second.setCompleteMoney(SecondCost.get(3));
        AppDispatchTicketChartVO third = new AppDispatchTicketChartVO();
        third.setAreaName("汉阳片区");
        third.setTotal(dispatchTotal.get(2).toString());
        third.setComplete(completeDispatchTotal.get(2).toString());
        third.setMin(moneyDispatchTotal.get(2).toString());
        third.setMax(maxMoneyDispatchTotal.get(2).toString());
        third.setTotalMoney(thirdCost.get(0));
        third.setMinMoney(thirdCost.get(1));
        third.setMaxMoney(thirdCost.get(2));
        third.setCompleteMoney(thirdCost.get(3));
        AppDispatchTicketChartVO fourth = new AppDispatchTicketChartVO();
        fourth.setAreaName("总计");
        Integer tot = dispatchTotal.get(0) + dispatchTotal.get(1) + dispatchTotal.get(2);
        fourth.setTotal(tot.toString());
        Integer com = completeDispatchTotal.get(0) + completeDispatchTotal.get(1) + completeDispatchTotal.get(2);
        fourth.setComplete(com.toString());
        Integer min = moneyDispatchTotal.get(0) + moneyDispatchTotal.get(1) + moneyDispatchTotal.get(2);
        fourth.setMin(min.toString());
        Integer max = maxMoneyDispatchTotal.get(0) + maxMoneyDispatchTotal.get(1) + maxMoneyDispatchTotal.get(2);
        fourth.setMax(max.toString());
        fourth.setTotalMoney(sumCost.get(0));
        fourth.setMinMoney(sumCost.get(1));
        fourth.setMaxMoney(sumCost.get(2));
        fourth.setCompleteMoney(sumCost.get(3));
        dispatchTicketTable.add(first);
        dispatchTicketTable.add(second);
        dispatchTicketTable.add(third);
        dispatchTicketTable.add(fourth);
        return dispatchTicketTable;
    }

    /**
     * 派工单比较信息
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public Map<String, Object> dispatchTicketCompare(AppTicketCharReqVO appTicketCharReqVO) {
        Map<String, Object> map = new HashMap<>();
        appTicketCharReqVO.setOrgId("10001");
        Integer firstApprovedList = ticketDao.getDispatchApprovedList(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10002");
        Integer SecondApprovedList = ticketDao.getDispatchApprovedList(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10003");
        Integer thirdApprovedList = ticketDao.getDispatchApprovedList(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10001");
        Map<String, Integer> dispatchTicketCompare = ticketDao.dispatchTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c1 = new HashMap<>();
        c1.put("num", dispatchTicketCompare.get("num"));
        c1.put("money", dispatchTicketCompare.get("money"));
        c1.put("min", dispatchTicketCompare.get("min"));
        c1.put("max", dispatchTicketCompare.get("max"));
        c1.put("Approved", firstApprovedList);
        appTicketCharReqVO.setOrgId("10002");
        Map<String, Integer> secondDispatchTicketCompare = ticketDao.dispatchTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c2 = new HashMap<>();
        c2.put("num", secondDispatchTicketCompare.get("num"));
        c2.put("money", secondDispatchTicketCompare.get("money"));
        c2.put("min", secondDispatchTicketCompare.get("min"));
        c2.put("max", secondDispatchTicketCompare.get("max"));
        c2.put("Approved", SecondApprovedList);
        appTicketCharReqVO.setOrgId("10003");
        Map<String, Integer> thirdDispatchTicketCompare = ticketDao.dispatchTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c3 = new HashMap<>();
        c3.put("num", thirdDispatchTicketCompare.get("num"));
        c3.put("money", thirdDispatchTicketCompare.get("money"));
        c3.put("min", thirdDispatchTicketCompare.get("min"));
        c3.put("max", thirdDispatchTicketCompare.get("max"));
        c3.put("Approved", thirdApprovedList);
        map.put("hk", c1);
        map.put("wc", c2);
        map.put("hy", c3);

        return map;
    }


    /**
     * 验收单工单统计图表
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public AppTicketChartVO acceptanceTicket(AppTicketCharReqVO appTicketCharReqVO) {
        AppTicketChartVO appTicketChartVO = new AppTicketChartVO();
        //通过type查询统计图数据
        List<String> rowNameList = new ArrayList<>();
        Collections.addAll(rowNameList, "汉口片区", "武昌片区", "汉阳片区");
        appTicketChartVO.setCategories(rowNameList);
        List<Map<String, Object>> seriesList = new ArrayList<>();
        List<Integer> dispatchTotal = ticketDao.getAcceptanceTotal(appTicketCharReqVO);
        Map<String, Object> col1 = new HashMap<>();
        col1.put("name", "工单申请数量");
        col1.put("data", dispatchTotal);
        col1.put("color", "#6E75FD");
        seriesList.add(col1);
        List<Integer> completeDispatchTotal = ticketDao.getCompleteAcceptanceTotal(appTicketCharReqVO);
        Map<String, Object> col2 = new HashMap<>();
        col2.put("name", "工单审批数量");
        col2.put("data", completeDispatchTotal);
        col2.put("color", "#37CBCB");
        seriesList.add(col2);
        List<Integer> moneyDispatchTotal = ticketDao.getMoneyAcceptanceTotal(appTicketCharReqVO);
        Map<String, Object> col3 = new HashMap<>();
        col3.put("name", "单笔>3000");
        col3.put("data", moneyDispatchTotal);
        col3.put("color", "#4ECA70");
        seriesList.add(col3);
        List<Integer> maxMoneyDispatchTotal = ticketDao.getMaxMoneyAcceptanceTotal(appTicketCharReqVO);
        Map<String, Object> col4 = new HashMap<>();
        col4.put("name", "单笔>10000");
        col4.put("data", maxMoneyDispatchTotal);
        col4.put("color", "#FBD336");
        seriesList.add(col4);
        appTicketChartVO.setSeries(seriesList);

        return appTicketChartVO;
    }


    @Override
    public List<AppDispatchTicketChartVO> acceptanceTicketTable(AppTicketCharReqVO appTicketCharReqVO) {
        List<AppDispatchTicketChartVO> dispatchTicketTable = new ArrayList<>();
        List<Integer> dispatchTotal = ticketDao.getAcceptanceTotal(appTicketCharReqVO);
        List<Integer> completeDispatchTotal = ticketDao.getCompleteAcceptanceTotal(appTicketCharReqVO);
        List<Integer> moneyDispatchTotal = ticketDao.getMoneyAcceptanceTotal(appTicketCharReqVO);
        List<Integer> maxMoneyDispatchTotal = ticketDao.getMaxMoneyAcceptanceTotal(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10001");
        List<String> firstCost = ticketDao.getAcceptancePredictCost(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10002");
        List<String> SecondCost = ticketDao.getAcceptancePredictCost(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10003");
        List<String> thirdCost = ticketDao.getAcceptancePredictCost(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId(null);
        List<String> sumCost = ticketDao.getAcceptancePredictCost(appTicketCharReqVO);
        AppDispatchTicketChartVO first = new AppDispatchTicketChartVO();
        first.setAreaName("汉口片区");
        first.setTotal(dispatchTotal.get(0).toString());
        first.setComplete(completeDispatchTotal.get(0).toString());
        first.setMin(moneyDispatchTotal.get(0).toString());
        first.setMax(maxMoneyDispatchTotal.get(0).toString());
        first.setTotalMoney(firstCost.get(0));
        first.setMinMoney(firstCost.get(1));
        first.setMaxMoney(firstCost.get(2));
        first.setCompleteMoney(firstCost.get(3));
        AppDispatchTicketChartVO second = new AppDispatchTicketChartVO();
        second.setAreaName("武昌片区");
        second.setTotal(dispatchTotal.get(1).toString());
        second.setComplete(completeDispatchTotal.get(1).toString());
        second.setMin(moneyDispatchTotal.get(1).toString());
        second.setMax(maxMoneyDispatchTotal.get(1).toString());
        second.setTotalMoney(SecondCost.get(0));
        second.setMinMoney(SecondCost.get(1));
        second.setMaxMoney(SecondCost.get(2));
        second.setCompleteMoney(SecondCost.get(3));
        AppDispatchTicketChartVO third = new AppDispatchTicketChartVO();
        third.setAreaName("汉阳片区");
        third.setTotal(dispatchTotal.get(2).toString());
        third.setComplete(completeDispatchTotal.get(2).toString());
        third.setMin(moneyDispatchTotal.get(2).toString());
        third.setMax(maxMoneyDispatchTotal.get(2).toString());
        third.setTotalMoney(thirdCost.get(0));
        third.setMinMoney(thirdCost.get(1));
        third.setMaxMoney(thirdCost.get(2));
        third.setCompleteMoney(thirdCost.get(3));
        AppDispatchTicketChartVO fourth = new AppDispatchTicketChartVO();
        fourth.setAreaName("总计");
        Integer tot = dispatchTotal.get(0) + dispatchTotal.get(1) + dispatchTotal.get(2);
        fourth.setTotal(tot.toString());
        Integer com = completeDispatchTotal.get(0) + completeDispatchTotal.get(1) + completeDispatchTotal.get(2);
        fourth.setComplete(com.toString());
        Integer min = moneyDispatchTotal.get(0) + moneyDispatchTotal.get(1) + moneyDispatchTotal.get(2);
        fourth.setMin(min.toString());
        Integer max = maxMoneyDispatchTotal.get(0) + maxMoneyDispatchTotal.get(1) + maxMoneyDispatchTotal.get(2);
        fourth.setTotalMoney(sumCost.get(0));
        fourth.setMinMoney(sumCost.get(1));
        fourth.setMaxMoney(sumCost.get(2));
        fourth.setCompleteMoney(sumCost.get(3));
        fourth.setMax(max.toString());
        dispatchTicketTable.add(first);
        dispatchTicketTable.add(second);
        dispatchTicketTable.add(third);
        dispatchTicketTable.add(fourth);
        return dispatchTicketTable;
    }

    /**
     * 验收单比较信息
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public Map<String, Object> acceptanceTicketCompare(AppTicketCharReqVO appTicketCharReqVO) {
        Map<String, Object> map = new HashMap<>();
        appTicketCharReqVO.setOrgId("10001");
        Integer firstApprovedList = ticketDao.getAcceptanceApprovedList(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10002");
        Integer SecondApprovedList = ticketDao.getAcceptanceApprovedList(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10003");
        Integer thirdApprovedList = ticketDao.getAcceptanceApprovedList(appTicketCharReqVO);
        appTicketCharReqVO.setOrgId("10001");
        Map<String, Integer> dispatchTicketCompare = ticketDao.acceptanceTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c1 = new HashMap<>();
        c1.put("num", dispatchTicketCompare.get("num"));
        c1.put("money", dispatchTicketCompare.get("money"));
        c1.put("min", dispatchTicketCompare.get("min"));
        c1.put("max", dispatchTicketCompare.get("max"));
        c1.put("Approved", firstApprovedList);
        appTicketCharReqVO.setOrgId("10002");
        Map<String, Integer> secondDispatchTicketCompare = ticketDao.acceptanceTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c2 = new HashMap<>();
        c2.put("num", secondDispatchTicketCompare.get("num"));
        c2.put("money", secondDispatchTicketCompare.get("money"));
        c2.put("min", secondDispatchTicketCompare.get("min"));
        c2.put("max", secondDispatchTicketCompare.get("max"));
        c2.put("Approved", SecondApprovedList);
        appTicketCharReqVO.setOrgId("10003");
        Map<String, Integer> thirdDispatchTicketCompare = ticketDao.acceptanceTicketCompare(appTicketCharReqVO);
        Map<String, Integer> c3 = new HashMap<>();
        c3.put("num", thirdDispatchTicketCompare.get("num"));
        c3.put("money", thirdDispatchTicketCompare.get("money"));
        c3.put("min", thirdDispatchTicketCompare.get("min"));
        c3.put("max", thirdDispatchTicketCompare.get("max"));
        c3.put("Approved", thirdApprovedList);
        map.put("hk", c1);
        map.put("wc", c2);
        map.put("hy", c3);

        return map;
    }

    /**
     * 小程序故障类型
     *
     * @param appTicketCharReqVO
     * @return
     */
    @Override
    public AppTicketChartVO getAppFaultType(AppTicketCharReqVO appTicketCharReqVO) {
        AppTicketChartVO appTicketChartVO = new AppTicketChartVO();
        List<Map<String, Object>> map = ticketDao.getAppFaultType(appTicketCharReqVO);
        String[] color = new String[]{"#6C74FD", "#35CBCC", "#4DCA72", "#FBD63A", "#F5647F", "#F4A244", "#33A0FF", "#CB80F0", "#5055D4",
                "#444F8B", "#6D75FE", "#36CBCB", "#4DCB73", "#FBD438"};
        for (int i = 0; i < map.size(); i++) {
            map.get(i).put("color", color[i]);
        }
        appTicketChartVO.setSeries(map);
        return appTicketChartVO;
    }

    @Override
    public AppTicketChartVO getAppTicketSource(AppTicketCharReqVO appTicketCharReqVO) {
        AppTicketChartVO appTicketChartVO = new AppTicketChartVO();
        List<Map<String, Object>> map = ticketDao.getAppTicketSource(appTicketCharReqVO);
        String[] color = new String[]{"#6E74FA", "#39CBCC", "#4ECC74", "#FBD439", "#F4667B", "#FDE5C4", "#3A9FFD", "#1353D7", "#1353D7"};
        for (int i = 0; i < map.size(); i++) {
            map.get(i).put("color", color[i]);
        }
        appTicketChartVO.setSeries(map);
        return appTicketChartVO;
    }

    @Override
    public AppTicketChartVO getDeviceType(AppTicketCharReqVO appTicketCharReqVO) {
        AppTicketChartVO appTicketChartVO = new AppTicketChartVO();
        List<Map<String, Object>> map = ticketDao.getDeviceType(appTicketCharReqVO);
        String[] color = new String[]{"#868AFF", "#55D5D5", "#6AD38B", "#FEDB57", "#F67C93", "#36CBCB", "#4DCB73", "#FBD438"};
        for (int i = 0; i < map.size(); i++) {
            map.get(i).put("color", color[i]);
        }
        appTicketChartVO.setSeries(map);
        return appTicketChartVO;
    }

    @Override
    public AppTicketChartVO getDealWay(AppTicketCharReqVO appTicketCharReqVO) {
        AppTicketChartVO appTicketChartVO = new AppTicketChartVO();
        List<Map<String, Object>> map = ticketDao.getDealWay(appTicketCharReqVO);
        String[] color = new String[]{"#868AFF", "#55D5D5", "#6AD38B", "#FEDB57", "#F67C93", "#36CBCB", "#4DCB73", "#FBD438"};
        for (int i = 0; i < map.size(); i++) {
            map.get(i).put("color", color[i]);
            if (map.get(i).get("name")==null){
                map.get(i).put("name","未标记");
            }
        }
        appTicketChartVO.setSeries(map);
        return appTicketChartVO;

    }

    // ------------------------------------2021/07/22报表相关代码结束--------------------------------------------------------

    /**
     * 获取工单图片
     *
     * @param id
     * @return
     */
    @Override
    public List<TicketImgVO> getTicketImg(Long id) {
        List<Long> ids = new ArrayList<>();
        Ticket ticket = ticketDao.getTicket(id);
        if (null != ticket.getIsMerge() && 1 == ticket.getIsMerge()) {
            //ids = ticketDao.getIdByPid(id);
            return this.getTicketAndDispatchImg(id);
        } else {
            ids.add(id);
        }
        List<TicketImgVO> ticketImgList = ticketDao.getTicketImg(ids);
        if (null != ticketImgList && ticketImgList.size() > 0) {
            for (TicketImgVO ticketImgVO : ticketImgList) {
                if ("0".equals(ticketImgVO.getOperation())) {
                    ticketImgVO.setOperation("备注");
                }
                if ("3".equals(ticketImgVO.getOperation())) {
                    ticketImgVO.setOperation("维修上报");
                }
                if ("31".equals(ticketImgVO.getOperation())) {
                    ticketImgVO.setOperation("打卡");
                }
            }
        }
        return ticketImgList;
    }


    /**
     * 验收单在派工单和日志中选图片接口
     *
     * @param id
     * @return
     */
    @Override
    public List<TicketImgVO> getTicketAndDispatchImg(Long id) {
        List<Long> ids = new ArrayList<>();
        Ticket ticket = ticketDao.getTicket(id);
        if (null != ticket.getIsMerge() && 1 == ticket.getIsMerge()) {
            ids = ticketDao.getIdByPid(id);
        } else {
            ids.add(id);
        }
        List<TicketImgVO> ticketImgList = ticketDao.getTicketAndDispatchImg(ids);
        if (null != ticketImgList && ticketImgList.size() > 0) {
            for (TicketImgVO ticketImgVO : ticketImgList) {
                if ("0".equals(ticketImgVO.getOperation())) {
                    ticketImgVO.setOperation("备注");
                }
                if ("3".equals(ticketImgVO.getOperation())) {
                    ticketImgVO.setOperation("维修上报");
                }
                if ("31".equals(ticketImgVO.getOperation())) {
                    ticketImgVO.setOperation("打卡");
                }
                if ("-1".equals(ticketImgVO.getOperation())) {
                    ticketImgVO.setOperation("派工单上传");
                }
            }
        }
        return ticketImgList;
    }

    /**
     * 通过图片id查询所有图片信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<BaseVfs> getImgByIds(List<Long> ids) {
        return ticketDao.getImgByIds(ids);
    }

    /**
     * 选择图片实现
     *
     * @param ticketSelectImg
     * @return
     */
    @Override
    public ApiResult selectTicketImg(TicketSelectImg ticketSelectImg) {
        //通过图片的id查到所有图片的信息
        List<BaseVfs> adjunctList = ticketDao.getImgByIds(ticketSelectImg.getIds());
        List<Integer> sorts = ticketSelectImg.getSorts();
        //对图片的adjustId和sort赋值
        for (int i = 0; i < adjunctList.size(); i++) {
            adjunctList.get(i).setAdjunctId(ticketSelectImg.getAdjunctId());
            //数据渲染完毕 做数据插入
            BaseVfs baseVfs = adjunctList.get(i);
            baseVfs.setSort(Integer.MAX_VALUE);
            baseVfsDao.insertSelective(baseVfs);
           /* adjunctList.get(i).setSort(baseVfs.getId().intValue());
            baseVfsDao.updateByPrimaryKeySelective(baseVfs);*/
        }

        return ApiResult.success();
    }

    /**
     * 整体图片排序方法
     *
     * @param ticketImgSortReqVOList
     * @return
     */
    @Override
    public ApiResult ticketImgSort(List<TicketImgSortReqVO> ticketImgSortReqVOList) {

        for (TicketImgSortReqVO ticketImgSortReqVO : ticketImgSortReqVOList) {
            BaseVfs baseVfs = new BaseVfs();
            BeanUtils.copyProperties(ticketImgSortReqVO, baseVfs);
            baseVfsDao.updateSort(baseVfs);
        }
        return ApiResult.success();
    }

    // ------------------------------------2021/07/27工单图片选择开始------------------------------------------------------//


    // ------------------------------------2021/07/27工单图片选择结束------------------------------------------------------//


    // -----------------------------------工单创建地图选择点位开始--------------------------------------------------------//
    @Override
    public ApiResult getMapList(TicketDevicePointMapQueryVO vo) {
        if(!StringUtils.isEmpty(vo.getKeyword())){
            String keyword1=vo.getKeyword().replaceAll("-","").replaceAll("_","").replaceAll(" ","");
            vo.setKeyword1(keyword1);
        }
        List<TicketForMapResVO> pointList = devicePointMapper.getListForTicketMap(vo);
        if (pointList != null && pointList.size() > 0) {
            int i = 0;
            for (TicketForMapResVO resVO : pointList) {
                //设置序号
                resVO.setId(i++);
            }
        }
        return ApiResult.success(pointList);
    }
    // ------------------------------------ 工单创建地图选择点位结束------------------------------------------------------//


    // ------------------------------------- 派工单 验收单 个人待审批总数查询开始--------------------------------------------//

    /**
     * 个人待审批派工单数量
     *
     * @param username
     * @return
     */
    @Override
    public Integer getApprovedDispatchCount(String username) {
        List<String> workflowIdList = new ArrayList<>();
        workflowIdList.add(DISPATCH_WORKFLOW_ID);
        return ticketWorkflowService.myTicketFlowCount(username, workflowIdList);
    }

    /**
     * 个任待审批验收单数量
     *
     * @param username
     * @return
     */
    @Override
    public Integer getApprovedAcceptanceCount(String username) {
        List<String> workflowIdList = new ArrayList<>();
        workflowIdList.add(ACCEPTANCE_WORKFLOW_ID);
        return ticketWorkflowService.myTicketFlowCount(username, workflowIdList);
    }


    // ------------------------------------- 派工单 验收单 个人待审批总数查询结束--------------------------------------------//


    @Override
    public Boolean pushTicketMessageTask() {
        //待推送unionId列表 由于需要根据每个人定制查询推送数据 查询所有的监理和业主账号
        List<String> roleList = new ArrayList<>();
        roleList.add(SUPERVISION_ROLE);
        roleList.add(OWNER_ROLE);
        List<String> usernameList = sysUserDao.findUsernameListByRoleList(roleList);
        //Collections.addAll(usernameList,"oyb","liuxiang","lgwy-test","zz002","cy","lht","lls","jyh","sk","fangy");
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        weChatPushReqVO.setTemplateType(TICKET_TEMPLATE_RECORD);
        weChatPushReqVO.setCreator("lgwy");
        List<String> keywords = new ArrayList<>();
        keywords.add(DateHelper.getNow());
        keywords.add("交管局科技处");
        keywords.add("工单数据统计");
        weChatPushReqVO.setKeywords(keywords);
        JSONObject jsonObject = new JSONObject();

        String breakdownCountMsg = "当日未发起故障工单";
        String breakdownCompleteCountMsg = "暂无工单完工";
        String dispatchMoneyMsg = "未发起派工单";
        //String dispatchTicketCountMsg="派工单全部审核完成";
        String acceptanceMoneyMsg = "未发起验收单";
        //String acceptanceTicketCountMsg="验收单全部审核完成";
        //查询当日统计信息
        //故障工单数量
        Integer breakDownCount = ticketDao.getBreakdownTicketWithoutOrgId(1);
        if (null != breakDownCount && 0 != breakDownCount) {
            breakdownCountMsg = "当日发起故障工单" + breakDownCount + "个";
        }
        //故障完工数量
        Integer breakDownCompleteCount = ticketDao.getBreakdownTicketCompleteWithoutOrgId(1);
        if (null != breakDownCompleteCount && 0 != breakDownCompleteCount) {
            breakdownCompleteCountMsg = "工单完工" + breakDownCompleteCount + "个";
        }
        //发起的派工单金额
        String dispatchMoney = ticketDao.getDispatchMoney(1);
        //发起的派工单数量
        Integer dispatchNum = ticketDao.getDispatchNum(1);

        if (null != dispatchNum && 0 != dispatchNum) {
            dispatchMoneyMsg = "据实结算派工单共" + dispatchMoney + "元(" + dispatchNum + "笔)";
        }
        //发起的验收单金额
        String acceptanceMoney = ticketDao.getAcceptanceMoney(1);
        //发起的验收单数量
        Integer acceptanceNum = ticketDao.getAcceptanceNum(1);
        if (null != acceptanceNum && 0 != acceptanceNum) {
            acceptanceMoneyMsg = "验收单共" + acceptanceMoney + "元(" + acceptanceNum + "笔)";
        }

        for (int i = 0; i < usernameList.size(); i++) {
            String dispatchTicketCountMsg = "派工单全部审核完成";
            String acceptanceTicketCountMsg = "验收单全部审核完成";
            List<String> currentUsername = new ArrayList<>();
            currentUsername.add(usernameList.get(i));
            List<String> unionidList = workflowTaskInfoMapper.getUserByApproved(currentUsername);
            //待审批派工单数量
            Integer approvedDispatchCount = this.getApprovedDispatchCount(usernameList.get(i));
            //待审批验收单数量
            Integer approvedAcceptanceCount = this.getApprovedAcceptanceCount(usernameList.get(i));
            if (null != approvedDispatchCount && 0 != approvedDispatchCount) {
                dispatchTicketCountMsg = "其中" + approvedDispatchCount + "笔待审批";
            }
            if (null != approvedAcceptanceCount && 0 != approvedAcceptanceCount) {
                acceptanceTicketCountMsg = "其中" + approvedAcceptanceCount + "笔待审批";
            }

            weChatPushReqVO.setUnionIdList(unionidList);
            jsonObject.put("breakdownCount", breakdownCountMsg);
            jsonObject.put("breakdownCompleteCount", breakdownCompleteCountMsg);
            jsonObject.put("dispatchMoney", dispatchMoneyMsg);
            jsonObject.put("dispatchTicketCount", dispatchTicketCountMsg);
            jsonObject.put("acceptanceMoney", acceptanceMoneyMsg);
            jsonObject.put("acceptanceTicketCount", acceptanceTicketCountMsg);
            weChatPushReqVO.setJsonObject(jsonObject);
            String pageTime = simpleDateFormat.format(new Date());
            //加上时间过滤
            weChatPushReqVO.setSuffix("pageTime=" + pageTime);
            try {
                weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
            } catch (Exception e) {
                log.error("工单数据推送失败!");
            }
        }
        return true;
    }


    /**
     * 撤回申请
     *
     * @param vo
     * @return
     */
    @Override
    public ApiResult ticketWithdraw(TicketSearchReqVO vo, TicketOpLogsReqVO ticketOpLogsReqVO) {
        if (StringUtils.isEmpty(vo.getType())) {
            return ApiResult.error("工单类型为空！");
        }
        String precessInstanceId = null;
        if (DISPATCH_WORKFLOW_ID.equals(vo.getType())) {
            precessInstanceId = workflowTaskInfoMapper.getProcessInstanceByIdAndType(vo.getId(), DISPATCH_WORKFLOW_ID);
        } else if (ACCEPTANCE_WORKFLOW_ID.equals(vo.getType())) {
            precessInstanceId = workflowTaskInfoMapper.getProcessInstanceByIdAndType(vo.getId(), ACCEPTANCE_WORKFLOW_ID);
        }
        //通过流程实例id删除实例
        Boolean flag = ticketWorkflowService.deleteProcessInstance(precessInstanceId);
        //如果删除成功 再把workflow_task_info中相关记录删除
        if (flag) {
            //日志添加
            TicketOpLogs ticketOpLogs = new TicketOpLogs();
            BeanUtils.copyProperties(ticketOpLogsReqVO, ticketOpLogs);
            ticketOpLogs.setTicketId(vo.getId());
            ticketOpLogs.setOpType(TICKET_OP_TYPE);
            ticketOpLogs.setCreator(vo.getCreator());
            Ticket ticket = new Ticket();
            ticket.setId(vo.getId());
            if (DISPATCH_WORKFLOW_ID.equals(vo.getType())) {
                workflowTaskInfoMapper.deleteByBusinessIdAndType(vo.getId(), DISPATCH_WORKFLOW_ID);
                ticketOpLogs.setMsg("撤回派工单申请");
                //删除完后修改为编辑态
                ticket.setDispatchStatus(2);
                ticketOpLogs.setOpType(1);
            } else if (ACCEPTANCE_WORKFLOW_ID.equals(vo.getType())) {
                workflowTaskInfoMapper.deleteByBusinessIdAndType(vo.getId(), ACCEPTANCE_WORKFLOW_ID);
                ticketOpLogs.setMsg("撤回验收单申请");
                //删除完后修改为编辑态
                ticket.setAcceptStatus(2);
                ticketOpLogs.setOpType(2);
            }
            ticketOpLogsDao.insertSelective(ticketOpLogs);
            ticketDao.update(ticket);
        } else {
            throw new BusinessException("该流程已被撤回，请勿多次重复点击!");
        }
        return ApiResult.success();
    }


    /**
     * 派工单合并
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult dispatchMerge(TicketDispatchMergeVO vo) {
        if (null == vo.getIds() || vo.getIds().size() < 1) {
            return ApiResult.error("传参为空！");
        }
        //合并之前 删除派工单跟验收单的流程 防止退回的流程 发起合并 拥有两个流程的问题
        for(Long id : vo.getIds()){
            //清除父工单的is_merge并删除父工单及流程
            String dispatchPrecessInstanceId =  workflowTaskInfoMapper.getProcessInstanceByIdAndType(id, DISPATCH_WORKFLOW_ID);
            String acceptPrecessInstanceId =    workflowTaskInfoMapper.getProcessInstanceByIdAndType(id, ACCEPTANCE_WORKFLOW_ID);

            //通过流程实例id删除实例
            if(!StringUtils.isEmpty(dispatchPrecessInstanceId)){
                Boolean flag = ticketWorkflowService.deleteProcessInstance(dispatchPrecessInstanceId);
                workflowTaskInfoMapper.deleteByBusinessIdAndType(id, DISPATCH_WORKFLOW_ID);

            }
            if(!StringUtils.isEmpty(acceptPrecessInstanceId)){
                Boolean flag = ticketWorkflowService.deleteProcessInstance(acceptPrecessInstanceId);
                workflowTaskInfoMapper.deleteByBusinessIdAndType(id, ACCEPTANCE_WORKFLOW_ID);

            }
        }
        //获取到需要合并的工单列表
        List<Ticket> ticketList = ticketDao.getByIds(vo.getIds());
        String remark = "";
        for (Ticket ticket : ticketList) {
            remark += ticket.getTicketCode() + ":" + ticket.getRemark() + "      ";
        }
        Long ticketId=vo.getIds().get(0);
        Ticket tempTicket = new Ticket();
        for(Ticket t : ticketList){
            if(t.getId().equals(ticketId)){
                tempTicket=t;
                break;
            }
        }
        //拿第一个作为主工单 生成合并后的工单
        Ticket mainTicket = new Ticket();
        BeanUtils.copyProperties(tempTicket, mainTicket);
        mainTicket.setId(null);
        String ticketCode = serialNumService.generateTicketSerialNum();
        mainTicket.setTicketCode(ticketCode);
        mainTicket.setTicketCodeResult(ticketCode);
        //合并描述

        mainTicket.setRemark(remark);
        mainTicket.setIsMerge(1);
        mainTicket.setAdjunctId(UUID.randomUUID().toString().replaceAll("-",""));
        mainTicket.setAcceptAdjunctId(UUID.randomUUID().toString().replaceAll("-",""));
        mainTicket.setOpPlatform("PC端");
        mainTicket.setDispatchStatus(2);
        mainTicket.setAcceptStatus(2);
        //插入这个合并工单
        ticketDao.insert(mainTicket);
        // 设置项目
        //通过工单列表 查询所有的项目
        if (null != ticketList && ticketList.size() > 0) {
            //获取所有图片
            List<BaseVfs> baseVfsList = new ArrayList<>();
            //获取所有项目信息
            List<TicketBaseMfRespVO> baseMfRespVOList = new ArrayList<>();
            for (Ticket ticket : ticketList) {
                List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(ticket.getId(), DISPATCH_OP_TYPE);
                if (!StringHelper.isEmpty(baseMfRespVO)) {
                    baseMfRespVOList.addAll(baseMfRespVO);
                }
                List<BaseVfs> baseVfsData = baseVfsDao.getDispatchAdjunctList(ticket.getId());
                if (!StringHelper.isEmpty(baseVfsData)) {
                    baseVfsList.addAll(baseVfsData);
                }
                //查询下面的点位和设备
                List <TicketPoint> ticketPointList = ticketPointDao.queryByIds(vo.getIds());
                if(!StringHelper.isEmpty(ticketPointList)){
                    for(TicketPoint ticketPoint : ticketPointList){
                        ticketPoint.setTicketId(mainTicket.getId());
                    }
                    ticketPointDao.insertBatch(ticketPointList);
                }
                List <TicketPointDevice> ticketPointDevicesList = ticketPointDeviceDao.queryByIds(vo.getIds());
                if(!StringHelper.isEmpty(ticketPointDevicesList)){
                    for(TicketPointDevice ticketPointDevice : ticketPointDevicesList){
                        ticketPointDevice.setTicketId(mainTicket.getId());
                    }
                    ticketPointDeviceDao.insertBatch(ticketPointDevicesList);
                }
            }
            if(!StringHelper.isEmpty(baseVfsList)){
                for(BaseVfs baseVfs : baseVfsList){
                    baseVfs.setAdjunctId(mainTicket.getAdjunctId());
                    baseVfs.setId(null);
                    baseVfsDao.insertSelective(baseVfs);
                }
            }
            TicketDispatchEditVO ticketDispatchEditVO = new TicketDispatchEditVO();
            ticketDispatchEditVO.setId(mainTicket.getId());
            ticketDispatchEditVO.setBaseMfRespVOList(baseMfRespVOList);
            ticketDispatchEditVO.setCreator(vo.getCreator());
            this.editDispatch(ticketDispatchEditVO);
            //子工单关联
            for (Ticket ticket : ticketList) {
                ticket.setPid(mainTicket.getId());
                ticketDao.updatePid(ticket);
            }
           /* ticketDao.update(mainTicket);*/
        }
        //返回合并工单的详情信息
        return ticketView(mainTicket.getId());
    }

    /**
     * 取消合并
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult unDispatchMerge(Long id) {
        //清除子工单的pid
        List<Ticket> ticketList = ticketDao.getByPid(id);
        if(!StringHelper.isEmpty(ticketList)){
            for(Ticket t : ticketList){
                ticketDao.clearPid(t.getId());
            }
        }
        //清除父工单的is_merge并删除父工单及流程
        String dispatchPrecessInstanceId =  workflowTaskInfoMapper.getProcessInstanceByIdAndType(id, DISPATCH_WORKFLOW_ID);
        String acceptPrecessInstanceId =    workflowTaskInfoMapper.getProcessInstanceByIdAndType(id, ACCEPTANCE_WORKFLOW_ID);

        //通过流程实例id删除实例
        if(!StringUtils.isEmpty(dispatchPrecessInstanceId)){
            Boolean flag = ticketWorkflowService.deleteProcessInstance(dispatchPrecessInstanceId);
        }
        if(!StringUtils.isEmpty(acceptPrecessInstanceId)){
            Boolean flag = ticketWorkflowService.deleteProcessInstance(acceptPrecessInstanceId);
        }
        //流程删除完毕 删除工单
        ticketPointDao.deleteByTicketId(id,null);
        ticketPointDeviceDao.deleteByTicketId(id,null);
        ticketOpLogsDao.deleteByTicketId(id);
        ticketDao.deleteById(id);
        return ApiResult.success();
    }

    /**
     * 子工单单个取消合并
     * @param id
     * @return
     */
    @Override
    public ApiResult unDispatchMergeOne(Long id) {

        return null;
    }


    /**
     * 批量添加工单
     * @param filePath
     * @param fileName
     * @param userSession
     * @return
     */
    @Override
    public ApiResult batchAddTicket(String filePath, String fileName, UserSession userSession) {
        File file = this.getRemoteFile(filePath);
        if (file == null) {
            return ApiResult.error("文件为空!");
        }
        String absoluteNewFilePath = this.uploadFolder + filePath;
        try {
            ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getTicketReader());
            if (linkResult == null) {
                return ApiResult.error("未填写任何工单数据!");
            }
            List<TicketImportVO> list = linkResult.getEntity().convertToRawTable(TicketImportVO.class);
            if (list == null || list.size() == 0) {
                return ApiResult.error("未填写任何工单信息!");
            }
            //取读取工单数据 放到表中，作为导入数据  进行数据处理 处理完后告知导入成功数、失败数、及导入数据的编辑
            Map<String, Object> result=dealDate(list,userSession,absoluteNewFilePath);
            if (Integer.parseInt(result.get("failCount").toString())>0) {
                log.error("文件校验失败#" + absoluteNewFilePath);
                result.put("errorPath", absoluteNewFilePath);
                result.put("msg", "部分数据存在格式错误");
            }
            return ApiResult.success(result);
        }catch (Exception e){
            log.error("用户上传模板错误或数据格式有误#" + absoluteNewFilePath, e);
            return ApiResult.error(5010, "用户上传模板错误，解析失败！");
        }
    }

    /**
     * 导入数据处理  文字转字典
     * @param list
     * @return
     */
    private Map<String, Object> dealDate(List<TicketImportVO> list, UserSession userSession,String absoluteNewFilePath) {
        Map<String, Object> map = new HashMap<>();
        int total = list.size();
        int success=0;
        int fail=0;
        String importNum=UUID.randomUUID().toString().replaceAll("-","");
        for(TicketImportVO ticketImportVO : list){
            //对有非空项不填的处理
            if (ticketImportVO.toString().contains("[必填]")) {
                fail++;
                writeErrorMsgUtil.writeErrorMsg(ticketImportVO.getRowIndex(), "请补充该行必填项！", absoluteNewFilePath);
                continue;
            }
            Ticket ticket = new Ticket();
            ticketImportVO.setCreator(userSession.getUserName());
            ticketImportVO.setAllocDepartmentNumBak(userSession.getOrgId());
            this.setTicketDate(ticketImportVO,ticket);
            try {
                ticket.setCreator(userSession.getUserName());
                ticket.setImportNum(importNum);
                ticketDao.insert(ticket);
                ticket.setUploadPointName(ticketImportVO.getPointName());
                ticketDao.update(ticket);
                if(null !=ticketImportVO.getPointId()){
                    TicketPoint ticketPoint = new TicketPoint();
                    ticketPoint.setTicketId(ticket.getId());
                    ticketPoint.setPointId(ticketImportVO.getPointId());
                    ticketPoint.setCreator(userSession.getUserName());
                    ticketPoint.setCreateTime(new Date());
                    ticketPointDao.insert(ticketPoint);
                }
               if(null!=ticketImportVO.getPointId()&&null!=ticketImportVO.getAssetId()){
                   TicketPointDevice ticketPointDevice = new TicketPointDevice();
                   ticketPointDevice.setPointId(ticketPointDevice.getPointId());
                   ticketPointDevice.setTicketId(ticket.getId());
                   ticketPointDevice.setAssetId(ticketImportVO.getAssetId());
                   ticketPointDevice.setCreator(userSession.getUserName());
                   ticketPointDevice.setCreateTime(new Date());
                   ticketPointDeviceDao.insert(ticketPointDevice);
               }

                success++;
            }catch (Exception e){
                log.error("导入数据失败，{}",e);
                fail++;
                writeErrorMsgUtil.writeErrorMsg(ticketImportVO.getRowIndex(), "请补充该行必填项或检查故障日志的数据格式！",absoluteNewFilePath);

            }

        }
        map.put("total",total);
        map.put("successCount",success);
        map.put("failCount",fail);
        map.put("importNum",importNum);
        return map;
    }

    /**
     * 为工单数据赋值
     * @param ticketImportVO
     * @param ticket
     */
    private void setTicketDate(TicketImportVO ticketImportVO, Ticket ticket) {
        ticket.setEditStatus(20);
        ticket.setTicketType(1);
        ticket.setTicketStatus(1);
        ticket.setDispatchStatus(1);
        ticket.setAcceptStatus(1);
        ticket.setMaintenanceStatus(0);
        ticket.setIsCost(0);
        ticket.setOpPlatform("PC端");
        ticket.setRemark(ticketImportVO.getRemark());
        String ticketCode = serialNumService.generateTicketSerialNum();
        ticket.setTicketCode(ticketCode);
        if(StringUtils.isEmpty(ticketImportVO.getTicketCodeResult())){
            ticket.setTicketCodeResult(ticketCode);
        }else {
            //唯一性校验 派单编号
            ticket.setTicketCodeResult(ticketImportVO.getTicketCodeResult());
        }
        if(!StringUtils.isEmpty(ticketImportVO.getOccurredTime())){
            ticket.setOccurredTime(ticketImportVO.getOccurredTime());
        }
        //辖区转字典
        if(!StringUtils.isEmpty(ticketImportVO.getAreaName())){
            String areaId = baseAreaMapper.getAreaIdByName(ticketImportVO.getAreaName());
            ticket.setAreaId(areaId);
        }
        //设备类型转换
        if(!StringUtils.isEmpty(ticketImportVO.getDeviceType())){
            String deviceType=baseDeviceTypeMapper.getTypeIdByName(ticketImportVO.getDeviceType());
            ticket.setDeviceType(deviceType);
        }
        //故障类型转换
        if(!StringUtils.isEmpty(ticket.getDeviceType())&&!StringUtils.isEmpty(ticketImportVO.getIssueType())){
            Integer issueType = baseFaultProfileMapper.selectFaultId(ticket.getDeviceType(),ticketImportVO.getIssueType());
            ticket.setIssueType(issueType);
        }
        //故障来源转字典
        if(!StringUtils.isEmpty(ticketImportVO.getIssueSource())){
            Integer issueSource = sysConstantItemMapper.getItemKey(ticketImportVO.getIssueSource(),"issue_source");
            ticket.setIssueSource(issueSource);
        }
        //分配单位转id
        if(StringUtils.isEmpty(ticketImportVO.getAllocDepartmentNum())){
            ticket.setAllocDepartmentNum(ticketImportVO.getAllocDepartmentNumBak());
        }else{
            String orgId = baseOrgService.getOrgIdByOrgName(ticketImportVO.getAllocDepartmentNum());
            ticket.setAllocDepartmentNum(orgId);
        }
        //联系人和联系人电话判空
        if(StringUtils.isEmpty(ticketImportVO.getApplyContact())){
            String contact = baseOrgService.queryContactInfo(ticket.getAllocDepartmentNum());
            ticket.setApplyContact(contact);
            // 根据联系人 查询手机号
            String contactPhone = sysUserDao.getPhone(contact);
            if(StringUtils.isEmpty(contactPhone)){
                ticket.setApplyPhone(contactPhone);
            }
        }else{
            ticket.setApplyContact(ticketImportVO.getApplyContact());
            ticket.setApplyPhone(ticketImportVO.getApplyPhone());
        }
        //如果责任人为空的话
        String dutyContact = baseOrgService.queryDutyInfo(ticket.getAllocDepartmentNum());
        ticket.setDutyContact(dutyContact);

        //故障级别转id
        if(StringUtils.isEmpty(ticketImportVO.getIssueLevel())){
            ticket.setIssueLevel(3);
        }else{
            Integer issueLevel = sysConstantItemMapper.getItemKey(ticketImportVO.getIssueLevel(),"issue_level");
            ticket.setIssueLevel(issueLevel);
        }
        resetPredictArriveAndCompleteTime2(ticket);
        //点位名称和设备编号转id
        if(!StringUtils.isEmpty(ticketImportVO.getPointName())){
            Long pointId=devicePointMapper.getidByNameAndDeviceType(ticketImportVO.getPointName(),null);
            ticketImportVO.setPointId(pointId);
        }
        if(!StringUtils.isEmpty(ticketImportVO.getAssetCode())&&null!=ticketImportVO.getPointId()){
            Long assetId = assetMapper.getIdByCodeAndPointId(ticketImportVO.getAssetCode(),ticketImportVO.getPointId());
            ticketImportVO.setAssetId(assetId);
        }




    }

    private ExcelSheetReader getTicketReader() {
        return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                .notNullFields(Lists.newArrayList(""))
                .headerClass(TicketImportVO.class).build();
    }


    private File getRemoteFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        File file = new File(this.uploadFolder + "/" + filePath);
        if (!file.exists()) {
            log.error("file error#" + this.uploadFolder + "/" + filePath);
            return null;
        }
        return file;
    }

    @Override
    public void batchComplete(List<Long> ids) {
        if(null !=ids &ids.size()>0){
            for(Long id :ids){
                ticketDao.complete(id);
            }
        }
    }

    @Override
    public void completeAll(String userName) {
        ticketDao.completeAll(userName);
    }

    @Override
    public ApiResult importTicketList(TicketSearchReqVO tickets) {
        int pageNum = tickets.getPageNum();
        int pageSize = tickets.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TicketSearchRespVO> list = ticketDao.queryImport(tickets);
        //如果pointId不为空
        if(null!=list && list.size()>0){
            for(TicketSearchRespVO ticketSearchRespVO : list){
                if(null!=ticketSearchRespVO.getPointId()){
                    List<Long> assetIds = assetMapper.getAssetIds(ticketSearchRespVO.getPointId(),ticketSearchRespVO.getId());
                    List<Map<Long,String>> assetList = assetMapper.getAsset(ticketSearchRespVO.getPointId());
                    ticketSearchRespVO.setAssetList(assetList);
                    ticketSearchRespVO.setAssetIds(assetIds);
                }else{
                    ticketSearchRespVO.setAssetList(new ArrayList<>());
                }
            }
        }

        return ApiResult.success(new PageResult(page.getTotal(), list, pageSize, pageNum));
    }

    @Override
    public ApiResult updateImport(TicketSearchRespVO tickets,UserSession userSession) {
        if(null==tickets.getId()){
            return ApiResult.error("未传主键！");
        }
        tickets.setIssueType(1);
        ticketDao.updateImport(tickets);
        if(null!=tickets.getPointId()){
            //先删除原来的点位 再添加这个新点位
            ticketPointDao.deleteByTicketId(tickets.getId(),null);
            ticketPointDeviceDao.deleteByTicketId(tickets.getId(),null);
            TicketPoint ticketPoint = new TicketPoint();
            ticketPoint.setCreator(userSession.getUserName());
            ticketPoint.setTicketId(tickets.getId());
            ticketPoint.setPointId(tickets.getPointId());
            ticketPoint.setCreateTime(new Date());

            ticketPointDao.insert(ticketPoint);
        }
        if(null!=tickets.getPointId()&&null!=tickets.getAssetIds()&&tickets.getAssetIds().size()>0){
            for(Long assetId : tickets.getAssetIds()){
                TicketPointDevice ticketPointDevice = new TicketPointDevice();
                ticketPointDevice.setTicketId(tickets.getId());
                ticketPointDevice.setPointId(tickets.getPointId());
                ticketPointDevice.setCreator(userSession.getUserName());
                ticketPointDevice.setAssetId(assetId);
                ticketPointDevice.setCreateTime(new Date());
                ticketPointDeviceDao.insert(ticketPointDevice);
            }
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult getAllPoint() {
        List<Map<Long, String>> pointList =devicePointMapper.getAllPoint();;
        return ApiResult.success(pointList);
    }

    @Override
    public ApiResult getAsset(Long pointId) {
        List<Map<Long, String>> assetList =assetMapper.getAsset(pointId);;
        return ApiResult.success(assetList);
    }

    @Override
    public ApiResult importCheck(TicketSearchReqVO tickets) {
        int pageNum = tickets.getPageNum();
        int pageSize = 10000;
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TicketSearchRespVO> list = ticketDao.queryImport(tickets);
        Integer count=0;
        //如果pointId不为空
        if(null!=list && list.size()>0){
            for(TicketSearchRespVO ticketSearchRespVO : list){
                if(null==ticketSearchRespVO.getPointId()){
                    count++;
                }
            }
        }

        return ApiResult.success(count);
    }

    @Override
    public ApiResult completeData(TicketSearchReqVO tickets) {
        int pageNum = tickets.getPageNum();
        int pageSize = 10000;
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TicketSearchRespVO> list = ticketDao.queryImport(tickets);
        for(TicketSearchRespVO ticketSearchRespVO :list){
            TicketSaveReqVO ticketSaveReqVO = new TicketSaveReqVO();
            ticketSaveReqVO.setCreator(ticketSearchRespVO.getCreator());
            ticketSaveReqVO.setAllocDepartmentNum(ticketSearchRespVO.getAllocDepartmentNum());
            Ticket ticket = new Ticket();
            ticket.setTicketType(ticketSearchRespVO.getTicketType());
            ticket.setTicketCode(ticketSearchRespVO.getTicketCode());
            ticketCreateMsgWeChatPush(ticketSaveReqVO, ticket);
        }

        return ApiResult.success();
    }


    @Override
    public ApiResult saveLogs1(TicketOpLogsReqVO tickets) {
        try {
            Ticket ticket = new Ticket();
            ticket.setId(tickets.getId());
            ticket.setTicketStatus(tickets.getTicketStatus());
            if(tickets.getTicketStatus()==4){
                ticket.setMaintenanceStatus(20);
            }
            //处理方式
            if (tickets.getDealWay() != null && tickets.getDealWay().intValue()!=0){
                ticket.setDealWay(tickets.getDealWay());
            }
            tickets.setMsg("#工单状态修改#"+tickets.getMsg());
            saveLogsAndUpdateStatus(tickets, ticket);
        } catch (Exception e) {
            log.error("新增评论日志失败！请求数据:{},原因是:", tickets, e);
            return ApiResult.error("新增失败!");
        }
        return ApiResult.success();
    }

    /**
     * 粘贴项目
     * @param data
     * @param userSession
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult pasteItem(String data,UserSession userSession) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject =  JSON.parseObject(data);
        }catch (Exception e){
            return ApiResult.error("json格式化不正确！");
        }
        String source=jsonObject.getString("source");
        if(StringUtils.isEmpty(source)||!"ticket-cost-items".equals(source)){
            return ApiResult.error("不是一段项目清单数据！");
        }
        JSONObject currentData=jsonObject.getJSONObject("data");
        Long ticketId = currentData.getLong("ticketId");
        if(null==ticketId){
            return ApiResult.error("工单id为空！");
        }
        //派工单/验收单
        String ticketType = currentData.getString("ticketType");
        if(StringUtils.isEmpty(ticketType)){
            return ApiResult.error("工单类型为空！");
        }
        //是否清空
        Integer isClean = currentData.getInteger("isClean");
        String pasteAllocDepartmentNum=currentData.getString("pasteAllocDepartmentNum");
        String currentAllocDepartmentNum=currentData.getString("currentAllocDepartmentNum");
        JSONArray items = currentData.getJSONArray("items");
        List<TicketBaseMfRespVO> ticketBaseMfRespVOList = new ArrayList<>();
        if(null==isClean||0==isClean){
            if (DISPATCH_WORKFLOW_ID.equals(ticketType)) {
                List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(ticketId, DISPATCH_OP_TYPE);
                if(!StringHelper.isEmpty(baseMfRespVO)){
                    ticketBaseMfRespVOList.addAll(baseMfRespVO);
                }
            } else if (ACCEPTANCE_WORKFLOW_ID.equals(ticketType)) {
                List<TicketBaseMfRespVO> baseMfRespVO = ticketDao.queryTicketById(ticketId, ACCEPT_OP_TYPE);
                if(!StringHelper.isEmpty(baseMfRespVO)){
                    ticketBaseMfRespVOList.addAll(baseMfRespVO);
                }
            }
        }
        List<TicketBaseMfRespVO> tempTicketBaseMfRespVOList = JSONArray.parseArray(items.toJSONString(), TicketBaseMfRespVO.class);
        if(!StringUtils.isEmpty(tempTicketBaseMfRespVOList)){
            ticketBaseMfRespVOList.addAll(tempTicketBaseMfRespVOList);
        }
        for(TicketBaseMfRespVO ticketBaseMfRespVO:ticketBaseMfRespVOList){
            ticketBaseMfRespVO.setAdjAmount(new BigDecimal(0));
            ticketBaseMfRespVO.setAdjustPrice(new BigDecimal(0));
        }
        //如果两个都不为空
        if (!StringUtils.isEmpty(pasteAllocDepartmentNum) && !StringUtils.isEmpty(currentAllocDepartmentNum)) {
            //如果两个被分配的项目相同 直接拿所有的code即可 如果分配单位不同 拿code做一次转换
            if (!pasteAllocDepartmentNum.equals(currentAllocDepartmentNum)) {
                for(TicketBaseMfRespVO ticketBaseMfRespVO:ticketBaseMfRespVOList){
                    BaseMaintaionFacility baseMaintaionFacility = ticketDetailDao.getIdByCodeAndDept(ticketBaseMfRespVO.getDeviceCode(),currentAllocDepartmentNum);
                    ticketBaseMfRespVO.setId(baseMaintaionFacility.getId());
                    ticketBaseMfRespVO.setAdjustPrice(baseMaintaionFacility.getDevicePrice());
                    ticketBaseMfRespVO.setDevicePrice(baseMaintaionFacility.getDevicePrice());
                    ticketBaseMfRespVO.setPredictPrice(ticketBaseMfRespVO.getAdjustPrice().multiply(ticketBaseMfRespVO.getAmount()));
                }
            }
            if (DISPATCH_WORKFLOW_ID.equals(ticketType)) {
                TicketDispatchEditVO ticketDispatchEditVO = new TicketDispatchEditVO();
                ticketDispatchEditVO.setCreator(userSession.getUserName());
                ticketDispatchEditVO.setBaseMfRespVOList(ticketBaseMfRespVOList);
                ticketDispatchEditVO.setId(ticketId);
                this.editDispatch(ticketDispatchEditVO);
            } else if (ACCEPTANCE_WORKFLOW_ID.equals(ticketType)) {
                TicketAcceptEditVO ticketAcceptEditVO = new TicketAcceptEditVO();
                ticketAcceptEditVO.setCreator(userSession.getUserName());
                ticketAcceptEditVO.setBaseMfRespVOList(ticketBaseMfRespVOList);
                ticketAcceptEditVO.setId(ticketId);
                this.editAccept(ticketAcceptEditVO);
            }
        } else {
            return ApiResult.error("分配单位传参数据为空！");
        }
        return ApiResult.success();
    }

    /**
     * 舆情转工单
     * @param vo
     */
    @Override
    public ApiResult po2Ticket(PoManagerRepeatMarkVO vo) {
        if(StringHelper.isEmpty(vo.getIds())){
            throw new BusinessException("传参id为空！");
        }
        int fail=0;
        List<String> failCodeList = new ArrayList<>();
        SysUser sysUser = sysUserDao.findUserByName(vo.getCreator());
        //不为空时  查询所有的舆情数据
        List<PoManagerRepVO> poManagerRepVOList = poManagerDao.getListByIds(vo.getIds());
        for(PoManagerRepVO poManagerRepVO : poManagerRepVOList){
            if(null==poManagerRepVO.getPointId()){
                fail++;
                failCodeList.add(poManagerRepVO.getPoCode());
                break;
            }
            //根据点位id查询点位信息
            DevicePoint devicePoint = devicePointService.getById(poManagerRepVO.getPointId());
            Ticket ticket = new Ticket();
            //设置工单编码
            BeanUtils.copyProperties(devicePoint, ticket);
            ticket.setTicketCode(serialNumService.generateTicketSerialNum());
            ticket.setTicketCodeResult(ticket.getTicketCode());
            ticket.setIssueType(1);
            ticket.setIsCost(0);
            ticket.setRemark(poManagerRepVO.getProblemDescription());
            ticket.setOccurredTime(poManagerRepVO.getBreakdownTime());
            //故障来源 网络舆情
            ticket.setIssueSource(5);
            ticket.setDispatchStatus(DISPATCH_STATUS_START);
            ticket.setAcceptStatus(ACCEPT_STATUS_START);
            ticket.setTicketStatus(TICKET_STATUS_START);
            //生成故障工单
            //工单编辑已完成
            ticket.setEditStatus(EDIT_STATUS_SUC);
            //分配单位
            ticket.setAllocDepartmentNum(sysUser.getMaintainNum());
            //设置故障级别
            ticket.setIssueLevel(TICKET_ISSUE_LEVEL_URGENT);
            if(1==poManagerRepVO.getIsImmidiately()){
                ticket.setIssueLevel(TICKET_ISSUE_LEVEL_SERIOUS);
            }
            //生成故障工单
            ticket.setTicketType(TICKET_TYPE_ISSUE_STATUS);
            //设置创建平台
            ticket.setOpPlatform("PC端");
            //如果责任人为空的话
            if (org.springframework.util.StringUtils.isEmpty(ticket.getDutyContact()) && !org.springframework.util.StringUtils.isEmpty(ticket.getAllocDepartmentNum())) {
                String dutyContact = baseOrgService.queryDutyInfo(ticket.getAllocDepartmentNum());
                ticket.setDutyContact(dutyContact);
            }
            String contact = baseOrgService.queryContactInfo(ticket.getAllocDepartmentNum());
            ticket.setApplyContact(contact);
            // 根据联系人 查询手机号
            String contactPhone = sysUserDao.getPhone(contact);
            if(!org.springframework.util.StringUtils.isEmpty(contactPhone)){
                ticket.setApplyPhone(contactPhone);
            }
            ticket.setCreator(vo.getCreator());
            //插入
            ticketDao.insert(ticket);
            //生成故障工单
            ticket.setTicketType(TICKET_TYPE_ISSUE_STATUS);
            //工单编辑已完成
            ticket.setEditStatus(EDIT_STATUS_SUC);
            resetPredictArriveAndCompleteTime(ticket);
            ticketDao.update(ticket);
            ///todo 把舆情的图片带到工单中
            Long ticketId = ticket.getId();
            TicketPoint ticketPoint = new TicketPoint();
            ticketPoint.setCreator(vo.getCreator());
            ticketPoint.setTicketId(ticketId);
            ticketPoint.setCreateTime(new Date());
            ticketPoint.setPointId(poManagerRepVO.getPointId());
            ticketPointDao.insert(ticketPoint);
            //工单创建完后，巡检日志与工单关联
            Long resultId = poManagerRepVO.getId();
            //关联工单
            poManagerDao.relationTicket(resultId,ticket.getId());
            //工单关联该巡检记录
            ticketDao.relationPoManager(ticket.getId(),resultId);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("file",fail);
        map.put("failCode",failCodeList);
        return ApiResult.success(map);

    }



    /**--------------------------------------------报表开始------------------------------------------**/
    /**
     * 故障工单统计
     * @param ticketChartReqVO
     * @return
     */
    @Override
    public ApiResult faultTicket(TicketChartReqVO ticketChartReqVO) {
        //日期校验
        if(StringUtils.isEmpty(ticketChartReqVO.getStartTime())||StringUtils.isEmpty(ticketChartReqVO.getEndTime())){
            return ApiResult.error("时间传值错误！");
        }
        String startTime = ticketChartReqVO.getStartTime()+" 00:00:00";
        String EndTime = ticketChartReqVO.getEndTime()+" 23:59:59";
        List<SelectVO> orgName= baseOrgService.queryMapOrg();
        TicketFaultTicketChartRepVO ticketFaultTicketChartRepVO = new TicketFaultTicketChartRepVO();
        List<Object> dimensions = new ArrayList<>();
        dimensions.add("product");
        dimensions.add("故障工单总数");
        dimensions.add("工单完工数");
        dimensions.add("一周内完成");

        /*ticketFaultTicketChartRepVO.setDimensions(dimensions);*/
        List<FaultTicketRespVO> faultTicketRespVOList = new ArrayList<>();
        List<List<Object>> sourceDate = new ArrayList<>();
        sourceDate.add(dimensions);
        for(SelectVO selectVO : orgName){
            /**
             * 查询数据
             */
            FaultTicketRespVO faultTicketRespVO = ticketDao.queryFaultTicket(startTime,EndTime,selectVO.getItemKey().toString());
            faultTicketRespVO.setProduct(selectVO.getItemValue());
            faultTicketRespVOList.add(faultTicketRespVO);
            List<Object> sourceDataVolume = new ArrayList<>();
            sourceDataVolume.add(selectVO.getItemValue());
            sourceDataVolume.add(faultTicketRespVO.getTotal());
            sourceDataVolume.add(faultTicketRespVO.getComplete());
            sourceDataVolume.add(faultTicketRespVO.getCompleteInWeek());
            if(faultTicketRespVO.getTotal()!=0||faultTicketRespVO.getComplete()!=0||faultTicketRespVO.getCompleteInWeek()!=0){
                sourceDate.add(sourceDataVolume);
            }

        }


        ticketFaultTicketChartRepVO.setSource(faultTicketRespVOList);
        ticketFaultTicketChartRepVO.setSourceData(sourceDate);
        return ApiResult.success(ticketFaultTicketChartRepVO);
    }

    @Override
    public ApiResult faultTicketTable(TicketChartReqVO ticketChartReqVO) {
        //日期校验
        if(StringUtils.isEmpty(ticketChartReqVO.getStartTime())||StringUtils.isEmpty(ticketChartReqVO.getEndTime())){
            return ApiResult.error("时间传值错误！");
        }
        String startTime = ticketChartReqVO.getStartTime()+" 00:00:00";
        String EndTime = ticketChartReqVO.getEndTime()+" 23:59:59";
        List<SelectVO> orgName= baseOrgService.queryMapOrg();
        List<FaultTicketTableRepVO> faultTicketTableRepVOList = new ArrayList<>();
        Integer totalTicket =0;
        Integer completeTicket=0;
        Integer completeInWeekTicket=0;
        for(SelectVO selectVO : orgName){
            /**
             * 查询数据
             */
            FaultTicketRespVO faultTicketRespVO = ticketDao.queryFaultTicket(startTime,EndTime,selectVO.getItemKey().toString());
            FaultTicketTableRepVO faultTicketTableRepVO = new FaultTicketTableRepVO();
            faultTicketTableRepVO.setOrgName(selectVO.getItemValue());
            faultTicketTableRepVO.setTotal(faultTicketRespVO.getTotal());
            faultTicketTableRepVO.setComplete(faultTicketRespVO.getComplete());
            faultTicketTableRepVO.setCompleteInWeek(faultTicketRespVO.getCompleteInWeek());
            totalTicket+=faultTicketRespVO.getTotal();
            completeTicket+=faultTicketRespVO.getComplete();
            completeInWeekTicket+=faultTicketRespVO.getCompleteInWeek();
            try {
                BigDecimal rate = new BigDecimal((float)faultTicketRespVO.getCompleteInWeek()/faultTicketRespVO.getTotal());
                faultTicketTableRepVO.setRate(rate.multiply(MMSConstants.ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");

            }catch (Exception e){
                faultTicketTableRepVO.setRate(BigDecimal.ZERO.multiply(MMSConstants.ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
            }
            if(faultTicketTableRepVO.getTotal()!=0||faultTicketTableRepVO.getComplete()!=0||faultTicketTableRepVO.getCompleteInWeek()!=0){
                faultTicketTableRepVOList.add(faultTicketTableRepVO);
            }
        }
        FaultTicketTableRepVO faultTicketTableRepVO = new FaultTicketTableRepVO();
        faultTicketTableRepVO.setOrgName("合计");
        faultTicketTableRepVO.setComplete(completeTicket);
        faultTicketTableRepVO.setTotal(totalTicket);
        faultTicketTableRepVO.setCompleteInWeek(completeInWeekTicket);
        try {
            BigDecimal rate = new BigDecimal((float)completeInWeekTicket/totalTicket);
            faultTicketTableRepVO.setRate(rate.multiply(MMSConstants.ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
        }catch (Exception e){
            faultTicketTableRepVO.setRate(BigDecimal.ZERO.multiply(MMSConstants.ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
        }

        faultTicketTableRepVOList.add(faultTicketTableRepVO);

        return ApiResult.success(faultTicketTableRepVOList);
    }

    @Override
    public ApiResult ticketPie(TicketChartReqVO ticketChartReqVO) {
        //日期校验
        if(StringUtils.isEmpty(ticketChartReqVO.getStartTime())||StringUtils.isEmpty(ticketChartReqVO.getEndTime())){
            return ApiResult.error("时间传值错误！");
        }
        String startTime = ticketChartReqVO.getStartTime()+" 00:00:00";
        String EndTime = ticketChartReqVO.getEndTime()+" 23:59:59";
        //工单来源饼图
        List<TicketPieCharRespVO> ticketSourceList = ticketDao.getTicketSource(startTime,EndTime);
        //工单设备类型饼图
        List<TicketPieCharRespVO> deviceTypeList = ticketDao.getTicketDeviceType(startTime,EndTime);
        //工单故障类型饼图
        List<TicketPieCharRespVO> ticketFaultTypeList = ticketDao.getTicketFaultType(startTime,EndTime);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("ticketSource",ticketSourceList)
                .put("deviceType",deviceTypeList)
                .put("faultType",ticketFaultTypeList)
                .build();
        return ApiResult.success(result);
    }

    /**
     * 导出工单报表
     * @param ticketChartReqVO
     * @param fileName
     * @return
     */
    @Override
    public String exportTicketReport(TicketChartReqVO ticketChartReqVO, String fileName) {
        //日期校验
        if(StringUtils.isEmpty(ticketChartReqVO.getStartTime())||StringUtils.isEmpty(ticketChartReqVO.getEndTime())){
            throw new BusinessException("时间传值错误！");
        }
        String startTime = ticketChartReqVO.getStartTime()+" 00:00:00";
        String EndTime = ticketChartReqVO.getEndTime()+" 23:59:59";
        List<List<String>> rowNameList = new ArrayList<>();
        //写四个sheet的表头和数据
        List<String> ticketRowList = new ArrayList<>();
        Collections.addAll(ticketRowList,"   ","故障工单总数","工单完工","周期内完成","工单完成率");
        List<String> dispatchRowList  = new ArrayList<>();
        Collections.addAll(dispatchRowList,"    ","申请数量","申请金额","单笔＞3000数量","单笔＞3000金额","单笔>10000数量","单笔>10000金额","审批完成数量","审批完成金额");
        List<String> acceptanceRowList  = new ArrayList<>();
        Collections.addAll(acceptanceRowList,"    ","申请数量","申请金额","单笔＞3000数量","单笔＞3000金额","单笔>10000数量","单笔>10000金额","审批完成数量","审批完成金额");
        List<String> ticketSourceRowList = new ArrayList<>();
        Collections.addAll(ticketSourceRowList,"故障来源","数量");
        List<String> deviceTypeRowList = new ArrayList<>();
        Collections.addAll(deviceTypeRowList,"设备类型","数量");
        List<String> faultTypeRowList = new ArrayList<>();
        Collections.addAll(faultTypeRowList,"故障类型","数量");
        rowNameList.add(ticketRowList);
        rowNameList.add(dispatchRowList);
        rowNameList.add(acceptanceRowList);
        rowNameList.add(ticketSourceRowList);
        rowNameList.add(deviceTypeRowList);
        rowNameList.add(faultTypeRowList);
        List<String> sheetNameList = new ArrayList<>();
        Collections.addAll(sheetNameList,"故障工单统计","派工单审批统计","验收单审批统计","故障来源","设备类型","故障类型");
        //此时写完文件头
        String absoluteNewFilePath= null;
        try {
            absoluteNewFilePath=excelExportUtil.writeMultipleHeader(sheetNameList,rowNameList,fileName);
        }catch (Exception e){
            log.error("工单报表导出时，写文件头错误{}",e);
        }
        ticketChartReqVO.setStartTime(startTime);
        ticketChartReqVO.setEndTime(EndTime);
        List<FaultTicketTableRepVO> faultTicketTableRepVOList=(List<FaultTicketTableRepVO>) this.faultTicketTable(ticketChartReqVO).getData();
        //文件头写完，写数据
        this.writeTicketReportData(absoluteNewFilePath,faultTicketTableRepVOList);
        //写其他三个统计table
        AppTicketCharReqVO appTicketCharReqVO = new AppTicketCharReqVO();
        appTicketCharReqVO.setType(null);
        appTicketCharReqVO.setStartTime(startTime);
        appTicketCharReqVO.setEndTime(EndTime);
        List<AppDispatchTicketChartVO> dispatchData = this.dispatchTicketTable(appTicketCharReqVO);
        this.writeApprovedTicketData(absoluteNewFilePath,dispatchData,1);
        List<AppDispatchTicketChartVO> acceptanceData = this.acceptanceTicketTable(appTicketCharReqVO);
        this.writeApprovedTicketData(absoluteNewFilePath,acceptanceData,2);
        HashMap<String, Object> map= (HashMap)this.ticketPie(ticketChartReqVO).getData();
        this.writePieData(absoluteNewFilePath,map.get("ticketSource"),3);
        this.writePieData(absoluteNewFilePath,map.get("deviceType"),4);
        this.writePieData(absoluteNewFilePath,map.get("faultType"),5);
        return absoluteNewFilePath;
    }

    /**
     * 写派工单跟验收单审批的数据
     * @param dispatchData
     * @param j
     */
    private void writeApprovedTicketData(String absoluteNewFilePath,List<AppDispatchTicketChartVO> dispatchData, int j) {
        FileInputStream fs=null;
        FileOutputStream out =null;
        try{
            //获取absoluteNewFilePath
            fs=new FileInputStream(absoluteNewFilePath);
            //做2003和2007兼容
            Workbook wb = WorkbookFactory.create(fs);

            //默认取第一个sheet页
            Sheet sheetAt = wb.getSheetAt(j);

            //做数据遍历 进行数据写入、
            //逻辑处理 需修改
            for(int i =0;i<dispatchData.size();i++){
                Row row = sheetAt.createRow(i+1);
                int currentCell=0;
                //"申请数量","申请金额","单笔＞3000数量，单笔＞3000金额","单笔>10000数量","单笔>10000金额","审批完成数量","审批完成金额"
                //row.createCell(currentCell++).setCellValue(devicePointList.get(i).getId());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getAreaName());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getTotal());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getTotalMoney());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getMin());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getMinMoney());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getMax());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getMaxMoney());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getComplete());
                row.createCell(currentCell++).setCellValue(dispatchData.get(i).getCompleteMoney());
            }
            out = new FileOutputStream(absoluteNewFilePath);
            wb.write(out);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出数据写入出现错误！");
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("输出流关闭出现异常{}",e);
                }
            }
            if(fs!=null){
                try {
                    fs.close();
                } catch (IOException e) {
                    log.error("文件输入流出现异常{}",e);
                }
            }
        }
    }

    private void writePieData(String absoluteNewFilePath, Object data, int j) {
        List<TicketPieCharRespVO> ticketPieCharRespVOS=(List<TicketPieCharRespVO>) data;
        FileInputStream fs=null;
        FileOutputStream out =null;
        try{
            //获取absoluteNewFilePath
            fs=new FileInputStream(absoluteNewFilePath);
            //做2003和2007兼容
            Workbook wb = WorkbookFactory.create(fs);

            //默认取第一个sheet页
            Sheet sheetAt = wb.getSheetAt(j);

            //做数据遍历 进行数据写入、
            //逻辑处理 需修改
            for(int i =0;i<ticketPieCharRespVOS.size();i++){
                Row row = sheetAt.createRow(i+1);
                int currentCell=0;
                //"故障工单总数","工单完工","一周内完成","工单完成率
                //row.createCell(currentCell++).setCellValue(devicePointList.get(i).getId());
                row.createCell(currentCell++).setCellValue(ticketPieCharRespVOS.get(i).getName());
                row.createCell(currentCell++).setCellValue(ticketPieCharRespVOS.get(i).getValue());
            }
            out = new FileOutputStream(absoluteNewFilePath);
            wb.write(out);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出数据写入出现错误！");
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("输出流关闭出现异常{}",e);
                }
            }
            if(fs!=null){
                try {
                    fs.close();
                } catch (IOException e) {
                    log.error("文件输入流出现异常{}",e);
                }
            }
        }
    }

    private void writeTicketReportData(String absoluteNewFilePath,  List<FaultTicketTableRepVO> faultTicketTableRepVOList) {
        FileInputStream fs=null;
        FileOutputStream out =null;
        try{
            //获取absoluteNewFilePath
            fs=new FileInputStream(absoluteNewFilePath);
            //做2003和2007兼容
            Workbook wb = WorkbookFactory.create(fs);

            //默认取第一个sheet页
            Sheet sheetAt = wb.getSheetAt(0);

            //做数据遍历 进行数据写入、
            //逻辑处理 需修改
            for(int i =0;i<faultTicketTableRepVOList.size();i++){
                Row row = sheetAt.createRow(i+1);
                int currentCell=0;
                //"故障工单总数","工单完工","一周内完成","工单完成率
                //row.createCell(currentCell++).setCellValue(devicePointList.get(i).getId());
                row.createCell(currentCell++).setCellValue(faultTicketTableRepVOList.get(i).getOrgName());
                row.createCell(currentCell++).setCellValue(faultTicketTableRepVOList.get(i).getTotal());
                row.createCell(currentCell++).setCellValue(faultTicketTableRepVOList.get(i).getComplete());
                row.createCell(currentCell++).setCellValue(faultTicketTableRepVOList.get(i).getCompleteInWeek());
                row.createCell(currentCell++).setCellValue(faultTicketTableRepVOList.get(i).getRate());
            }
            out = new FileOutputStream(absoluteNewFilePath);
            wb.write(out);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出数据写入出现错误！");
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("输出流关闭出现异常{}",e);
                }
            }
            if(fs!=null){
                try {
                    fs.close();
                } catch (IOException e) {
                    log.error("文件输入流出现异常{}",e);
                }
            }
        }
    }



    /**-----------------------------------------报表结束--------------------------------------------**/


    @Override
    public String exportTicketData(TicketSearchReqVO tickets, UserSession userSession,String fileName) {

        //写文件逻辑 制定导出模板
        List<String> rowNameList = new ArrayList<>();
        if(StringHelper.isEmpty(tickets.getType())){
            Collections.addAll(rowNameList,"派单编号","辖区","点位名称","设备类型","故障类型","分配单位","上报时间","维修完成时间","描                 述","状态","派工单提交时间","派工单状态","预算金额","验收单提交时间","验收单状态","核算金额");
        }
        if(!StringHelper.isEmpty(tickets.getType())&&"dispatch".equals(tickets.getType())){
            Collections.addAll(rowNameList,"派单编号","辖区","点位名称","设备类型","故障类型","分配单位","预算金额","上报时间","维修完成时间","描                 述","状态");
        }
        if(!StringHelper.isEmpty(tickets.getType())&&"acceptance".equals(tickets.getType())){
            Collections.addAll(rowNameList,"派单编号","辖区","点位名称","设备类型","故障类型","分配单位","预算金额","结算金额","上报时间","维修完成时间","描                 述","状态");
        }
        List<String> summaryRowNameList = new ArrayList<>();
        Collections.addAll(summaryRowNameList,"导出时间","导出总数","工单号");
        List<String> sheetNameList = new ArrayList<>();
        Collections.addAll(sheetNameList,"工单列表","统计");
        List<List<String>> rowName = new ArrayList<>();
        rowName.add(rowNameList);
        rowName.add(summaryRowNameList);
        //此时写完文件头
        String absoluteNewFilePath= null;
        try {
            absoluteNewFilePath=excelExportUtil.writeMultipleHeaderWithWidth(sheetNameList,rowName,fileName,500);
        }catch (Exception e){
            log.error("工单数据导出时，写文件头错误{}",e);
        }
        //数据查询完成 写文件头
        List<TicketSearchRespVO> ticketDataList = this.getTicketDataList(tickets, userSession);
        this.writeTicketData(absoluteNewFilePath,ticketDataList,tickets.getType());
        this.writeSummaryData(absoluteNewFilePath,ticketDataList);
        return absoluteNewFilePath;
    }

    private void writeSummaryData(String absoluteNewFilePath, List<TicketSearchRespVO> ticketDataList) {
        FileInputStream fs=null;
        FileOutputStream out =null;
        try{
            //获取absoluteNewFilePath
            fs=new FileInputStream(absoluteNewFilePath);
            //做2003和2007兼容
            Workbook wb = WorkbookFactory.create(fs);

            //默认取第一个sheet页
            Sheet sheetAt = wb.getSheetAt(1);

            for(int i =0;i<ticketDataList.size();i++){
                Row row = sheetAt.createRow(i+1);
                int currentCell = 0;
                //"工单编号","辖区","点位名称","设备类型","故障类型","分配单位","上报时间","描述","状态"
                //row.createCell(currentCell++).setCellValue(devicePointList.get(i).getId());
                if(i==0){
                    row.createCell(currentCell++).setCellValue(DateHelper.getNow());
                    row.createCell(currentCell++).setCellValue(ticketDataList.size());
                }else{
                    row.createCell(currentCell++).setCellValue("");
                    row.createCell(currentCell++).setCellValue("");
                }
            }


            out = new FileOutputStream(absoluteNewFilePath);
            wb.write(out);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出数据写入出现错误！");
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("输出流关闭出现异常{}",e);
                }
            }
            if(fs!=null){
                try {
                    fs.close();
                } catch (IOException e) {
                    log.error("文件输入流出现异常{}",e);
                }
            }
        }
    }

    private void writeTicketData(String absoluteNewFilePath, List<TicketSearchRespVO> ticketDataList,String type) {
        FileInputStream fs=null;
        FileOutputStream out =null;
        try{
            //获取absoluteNewFilePath
            fs=new FileInputStream(absoluteNewFilePath);
            //做2003和2007兼容
            Workbook wb = WorkbookFactory.create(fs);

            //默认取第一个sheet页
            Sheet sheetAt = wb.getSheetAt(0);

            //做数据遍历 进行数据写入、
            //逻辑处理 需修改
            List<SelectVO> areaToSelect = baseAreaMapper.findAreaToSelect();
            Map<Object, String> areaMap= areaToSelect.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
            List<SelectVO> deviceTypeToSelect = baseDeviceTypeMapper.findDeviceTypeToSelect();
            Map<Object,String> deviceTypeMap = deviceTypeToSelect.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
            List<SelectVO> faultToSelect = baseFaultTypeMapper.faultList();
            Map<Object, String> issueTypeMap =  faultToSelect.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
            List<SelectVO> orgIdToSelect = baseOrgService.queryMapOrg();
            Map<Object, String> orgIdMap =orgIdToSelect.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
            List<SelectVO> ticketStatusToSelect = sysConstantItemMapper.findItemsByDict("ticket_status");
            Map<Object, String> ticketStatusMap =ticketStatusToSelect.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
            List<SelectVO> dispatchStatusToSelect = sysConstantItemMapper.findItemsByDict("dispatch_status");
            Map<Object, String> dispatchStatusMap =dispatchStatusToSelect.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
            List<SelectVO> acceptStatusToSelect = sysConstantItemMapper.findItemsByDict("accept_status");
            Map<Object, String> acceptStatusMap =acceptStatusToSelect.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
            for(int i =0;i<ticketDataList.size();i++){
                Row row = sheetAt.createRow(i+1);
                int currentCell=0;

                //"派单编号","辖区","点位名称","设备类型","故障类型","分配单位","上报时间","维修完成时间","描                 述","状态","派工单提交时间","派工单状态","预算金额","验收单提交时间","验收单状态","核算金额"
                //row.createCell(currentCell++).setCellValue(devicePointList.get(i).getId());
                row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getTicketCodeResult());
                row.createCell(currentCell++).setCellValue(areaMap.get(ticketDataList.get(i).getAreaId()));
                row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getPointName());
                row.createCell(currentCell++).setCellValue(deviceTypeMap.get(ticketDataList.get(i).getDeviceType()));
                try {
                    row.createCell(currentCell++).setCellValue(issueTypeMap.get(ticketDataList.get(i).getIssueType().toString()));
                }catch (Exception e){
                    row.createCell(currentCell).setCellValue(issueTypeMap.get(ticketDataList.get(i).getIssueType()));
                }
                row.createCell(currentCell++).setCellValue(orgIdMap.get(ticketDataList.get(i).getAllocDepartmentNum()));
                if(!StringHelper.isEmpty(type)){
                    if("dispatch".equals(type)){
                        row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getPredictCost());
                    }
                    if("acceptance".equals(type)){
                        row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getAcceptPredictCost());
                        row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getAcceptAdjustCost());

                    }
                }
                row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getCreateTime());
                row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getPracticalCompleteTime());
                row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getRemark());
                row.createCell(currentCell++).setCellValue(ticketStatusMap.get(ticketDataList.get(i).getTicketStatus().toString()));
                //11/15添加 "派工单提交时间","派工单状态","预算金额","验收单提交时间","验收单状态","核算金额"
                // 根据id查询派工单提交时间和验收单提交时间
                if(StringHelper.isEmpty(type)) {
                    String dispatchRecentTime = ticketDao.getDispatchRecentTime(ticketDataList.get(i).getId());
                    row.createCell(currentCell++).setCellValue(dispatchRecentTime);
                    row.createCell(currentCell++).setCellValue(dispatchStatusMap.get(ticketDataList.get(i).getDispatchStatus().toString()));
                    row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getPredictCost());
                    // 根据id查询验收单提交时间
                    String acceptRecentTime = ticketDao.getAcceptRecentTime(ticketDataList.get(i).getId());
                    row.createCell(currentCell++).setCellValue(acceptRecentTime);
                    row.createCell(currentCell++).setCellValue(acceptStatusMap.get(ticketDataList.get(i).getAcceptStatus().toString()));
                    row.createCell(currentCell++).setCellValue(ticketDataList.get(i).getAcceptAdjustCost());
                }
            }
            out = new FileOutputStream(absoluteNewFilePath);
            wb.write(out);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出数据写入出现错误！");
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("输出流关闭出现异常{}",e);
                }
            }
            if(fs!=null){
                try {
                    fs.close();
                } catch (IOException e) {
                    log.error("文件输入流出现异常{}",e);
                }
            }
        }
    }


    public List<TicketSearchRespVO> getTicketDataList(TicketSearchReqVO tickets, UserSession userSession){
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
        int pageNum = 1;
        int pageSize = Integer.MAX_VALUE;
        if (StringUtils.isEmpty(tickets.getSortOrder())) {
            tickets.setSortOrder("desc");
        }
        tickets.setEditStatus(EDIT_STATUS_SUC);
        if (tickets.getTicketStatus() != null && tickets.getTicketStatus() == TICKET_STATUS_DRAFT) {
            tickets.setEditStatus(EDIT_STATUS_START);
        }
        Page page = PageHelper.startPage(pageNum, pageSize);
        tickets.setIsNeedRemark(1);
        List<TicketSearchRespVO> list = ticketDao.queryAll(tickets);
        for (TicketSearchRespVO vo : list) {
            vo.setDeleteAble(0);
            if (vo.getAcceptStatus() <= 2 && vo.getDispatchStatus() <= 2 && INSIDE_ROLE.equals(userSession.getRoleId()) && 2 != vo.getTicketStatus()) {
                vo.setDeleteAble(1);
            }
            vo.setPointName(devicePointMapper.selectPointName(vo.getPointId()));
        }
        return list;
    }
}
