package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.dto.workflow.*;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.model.PatrolClockIn;
import com.zans.mms.model.Screen;
import com.zans.mms.model.Ticket;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.devicepoint.map.DevicePointMapQueryVO;
import com.zans.mms.vo.po.PoManagerRepeatMarkVO;
import com.zans.mms.vo.ticket.*;
import com.zans.mms.vo.ticket.chart.TicketChartReqVO;

import java.util.List;
import java.util.Map;

/**
 * 工单表(Tickets)表服务接口
 *
 * @author beixing
 * @since 2021-01-13 18:14:23
 */
public interface ITicketService {


    /**
     * app列表查询
     * 注：只查询当前用户所属机构的数据
     **/
    ApiResult appList(TicketSearchReqVO tickets, UserSession userSession);

    /**
     * 工单查看
     **/
    ApiResult appView(Long id);

    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 工单打卡
     * @Date 2021/3/9
     * @Param [tickets]
     **/
    ApiResult appClockIn(TicketReportReqVO tickets, UserSession userSession);


    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 工单维修上报
     * @Date 2021/3/9
     * @Param [tickets]
     **/
    ApiResult appMaintenanceStatusReport(TicketReportReqVO tickets, UserSession userSession);

    /**
     * 小程序工单新增
     * 必定为故障工单，且是未分配状态
     **/
    ApiResult appSaveTicket(AppTicketSaveReqVO appTicketSaveReqVO,UserSession userSession);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tickets     实例对象
     * @param userSession
     * @return 对象列表
     */
    ApiResult ticketList(TicketSearchReqVO tickets, UserSession userSession);


    ApiResult ticketSubmit(TicketSaveReqVO tickets);


    ApiResult ticketAddPoint(TicketSaveReqVO tickets);


    ApiResult ticketDelPoint(TicketSaveReqVO tickets);


    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 创建工单，返回主键ID
     * @Date 2021/3/10
     * @Param []
     **/
    ApiResult ticketCreate(TicketSaveReqVO ticketSaveReqVO);


    /**
     * 工单查看
     **/
    ApiResult ticketView(Long id);


    /**
     * @return
     * @Author beixing
     * @Description
     * @Date 2021/4/6
     * @Param
     **/
    ApiResult viewEdit(TicketViewEditVO vo);


    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 派工单详情
     * @Date 2021/1/16
     * @Param [id]
     **/
    ApiResult viewDispatch(Long id);


    ApiResult viewDispatchEdit(TicketDispatchViewEditVO ticketsDispatchViewEditVO);


    ApiResult editDispatch(TicketDispatchEditVO ticketsDispatchEditVO);


    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 验收单详情
     * @Date 2021/1/16
     * @Param [id]
     **/
    ApiResult viewAccept(Long id);


    ApiResult viewEditAccept(TicketAcceptViewEditVO ticketAcceptEditVO);


    ApiResult editAccept(TicketAcceptEditVO ticketAcceptEditVO);


    /**
     * 新增数据
     *
     * @param tickets 实例对象
     * @return 实例对象
     */
    int insert(Ticket tickets);

    /**
     * 根据用户查询当前草稿数
     **/
    int queryDraftByUser(String user);


    ApiResult saveLogs(TicketOpLogsReqVO tickets);


    /**
     * @return boolean
     * @Author pancm
     * @Description 转工单接口
     * @Date 2021/1/14
     * @Param [issueNum, user]
     **/
    boolean saveAndTransformTickets(String issueNum, String user);


    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 工单分配
     * @Date 2021/3/3
     * @Param [ticketOpLogsReqVO]
     **/
    ApiResult assign(TicketOpLogsReqVO ticketOpLogsReqVO);

    /**
     * @return com.zans.base.vo.ApiResult
     * @Author pancm
     * @Description 状态处置
     * @Date 2021/3/3
     * @Param [ticketOpLogsReqVO]
     **/
    ApiResult dispose(TicketOpLogsReqVO ticketOpLogsReqVO);

    /**
    * @Author beixing
    * @Description  工单验收
    * @Date  2021/5/12
    * @Param
    * @return
    **/
    ApiResult succeed(TicketOpLogsReqVO ticketOpLogsReqVO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    ApiResult deleteById(Long id);


    /**
     * 派工单导出pdf
     *
     * @param
     * @return
     */
    String exportDispatch(String ticketCode);

    /**
     * 验收单导出pdf
     *
     * @param
     * @return
     */
    String exportAccept(String ticketCode);


    /**
     * 获取Pc首页的总数
     */
    List<CircleUnit> getPcTicketTotal(UserSession userSession);

    /**
     * 获取App首页的总数
     */
    List<CircleUnit> getAppTicketTotal(UserSession userSession);

    /**
     * 获取故障类型占比
     */
    List<CountUnit> getPcFaultType(UserSession userSession);


    /**
     * 获取派工来源
     */
    List<CountUnit> getPcTicketSource(UserSession userSession);


    /**
     * 获取运维单位占比
     */
    List<CircleUnit> getPcMaintainFacility(UserSession userSession);


    /**
     * 工单按故障类型统计,用于在图表中显示
     *
     * @return
     */
    ApiResult statisticsByFaultType();

    /**
     * 工单数量统计
     *
     * @return
     */
    ApiResult ticketStatistics();

    /**
     * 工单按设备类型统计
     *
     * @return
     */
    ApiResult statisticsTicketByDeviceType();

    /**
     * @return
     * @Author beiming
     * @Description 一键工单
     * @Date 4/13/21
     * @Param TicketCreateByAssetReqVO
     **/
    ApiResult<Ticket> createByAsset(TicketCreateByAssetReqVO reqVO);

    /**
     * ticket关联流程实例id
     * @param ticket
     */
    void updateProcessInstanceIdById(Ticket ticket);

    /**
     * 发起派工单流程 返回下一步的任务信息
     * @param startFlowDto
     * @param userName
     * @return
     */
    TaskRepDto startDispatch(StartFlowDto startFlowDto, String userName);

    /**
     *  发起派工单流程
     * @param tickets
     * @return
     */
	ApiResult startDispatch(TicketOpLogsReqVO tickets);


    /**
     * 发起验收工单流程 返回下一步的任务信息
     * @param startFlowDto
     * @param userName
     * @return
     */
    TaskRepDto startAcceptance(StartFlowDto startFlowDto, String userName);


    /**
     *  发起验收工单流程
     * @param tickets
     * @return
     */
    ApiResult startAccept(TicketOpLogsReqVO tickets);

    /**
     * 发起维修工单流程 返回下一步的任务信息
     * @param startFlowDto
     * @param userName
     * @return
     */
    TaskRepDto startMaintain(StartFlowDto startFlowDto, String userName);


    /**
     * 派工单流转方法
     * @param completeTaskVO
     * @param userName
     * @return
     */
	TaskRepDto circulationDispatch(CirculationTaskDto completeTaskVO, String userName);

	ApiResult circulationDispatch(TicketOpLogsReqVO tickets);

    /**
     * 验收单流转方法
     * @param completeTaskVO
     * @param userName
     * @return
     */
    TaskRepDto circulationAcceptance(CirculationTaskDto completeTaskVO, String userName);

    ApiResult circulationAcceptance(TicketOpLogsReqVO ticketOpLogsReqVO);
    /**
     * 维修派工流转方法
     * @param completeTaskVO
     * @param userName
     * @return
     */
    TaskRepDto circulationMaintain(CirculationTaskDto completeTaskVO, String userName);

    /**
     * 我的派工单待办列表
     * @param userName
     * @return
     */
    List<TicketWorkflowDto> myDispatchList(String userName);

    /**
     * 我的验收单待办列表
     * @param userName
     * @return
     */
    List<TicketWorkflowDto> myAcceptanceList(String userName);

    /**
     * 我的维修工单待办列表
     * @param userName
     * @return
     */
    List<TicketWorkflowDto> myMaintainList(String userName);

    /**
     * 通过流程实例id获取工单数据
     * @param processInstanceId
     * @return
     */
    TicketWorkflowDto getByProcessInstanceId(String processInstanceId);

    /**
     * 通过id查看工单数据
     * @param id
     * @return
     */
    TicketWorkflowDto getById(String id);

    /**
     * 查询工单详情方法  通过id查询
     * @param id
     * @return
     */
    TicketWorkflowDto ticketDetail(String id, String taskId);


    /**
     * 我的工单待办列表
     * @param userName
     * @return
     */
    List<TicketWorkflowDto> myPendingApprovalTicketList(String userName);

  /*  *//**
     * 我的工单待办列表 分页
     * @param userName
     * @return
     *//*
    PageResult<TicketWorkflowDto> myPendingApprovalTicketList(String userName, Integer pageNum, Integer pageSize);*/


    /**
     * 我的工单已办列表
     * @param reqVO
     * @return
     */
    PageResult<AppPendingApprovalRespVO> myApprovedTicketList(AppPendingApprovalReqVO reqVO);




    /**
    * @Author beiming
    * @Description  app待审批\审批列表
    * @Date  5/11/21
    * @Param
    * @return
    **/
    ApiResult myPendingAndApprovalTicketList(AppPendingApprovalReqVO reqVO, UserSession userSession);


    /**
     * 我的所有工单流程
     * @param userName  用户名
     * @return
     */
    List<TicketWorkflowDto> myTicketList(String userName);



    /**
     * 我的所有工单流程 分页 2021/10/14改造
     * @param reqVO  用户名
     * @return
     */
    PageResult<AppPendingApprovalRespVO> myTicketList(AppPendingApprovalReqVO reqVO);


    /**
    * @Author beiming
    * @Description  app派工单详情
    * @Date  5/11/21
    * @Param
    * @return
    **/
    ApiResult appViewDispatch(Long id);

    /**
    * @Author beiming
    * @Description  验收单详情
    * @Date  5/11/21
    * @Param
    * @return
    **/
    ApiResult appViewAccept(Long id);

    /**
     * 发起工单
     * @param clockIn
     */
    void createTicket(PatrolClockIn clockIn);


    ApiResult addDispatch(TicketOpLogsReqVO tickets);

    ApiResult delDispatch(TicketOpLogsReqVO ticketOpLogsReqVO);

    /**
     * 工单删除方法
     * @param id
     * @return
     */
	ApiResult delete(Long id);

	String exportTicket(TicketBatchExportVO ticketBatchExport);


	List<SelectVO> getDispatchInit();

    List<SelectVO> getAcceptanceInit();

    void update(Ticket ticket);

	ApiResult appListInit(String userName);

	ApiResult checkCode(TicketAcceptViewEditVO tickets);

    AppTicketChartVO breakdownTicket(AppTicketCharReqVO appTicketCharReqVO);

    AppTicketChartVO dispatchTicket(AppTicketCharReqVO appTicketCharReqVO);

    AppTicketChartVO acceptanceTicket(AppTicketCharReqVO appTicketCharReqVO);

    Map<String, Object> breakdownTicketCompare(AppTicketCharReqVO appTicketCharReqVO);

    Map<String, Object> dispatchTicketCompare(AppTicketCharReqVO appTicketCharReqVO);

    Map<String, Object> acceptanceTicketCompare(AppTicketCharReqVO appTicketCharReqVO);

	AppTicketChartVO getAppFaultType(AppTicketCharReqVO appTicketCharReqVO);

    AppTicketChartVO getAppTicketSource(AppTicketCharReqVO appTicketCharReqVO);

    AppTicketChartVO getDeviceType(AppTicketCharReqVO appTicketCharReqVO);

    List<AppFaultTicketChartVO> breakdownTicketTable(AppTicketCharReqVO appTicketCharReqVO);

	List<AppDispatchTicketChartVO> dispatchTicketTable(AppTicketCharReqVO appTicketCharReqVO);

    List<AppDispatchTicketChartVO> acceptanceTicketTable(AppTicketCharReqVO appTicketCharReqVO);

	List<TicketImgVO> getTicketImg(Long id);

    List<BaseVfs> getImgByIds(List<Long> ids);

    ApiResult selectTicketImg(TicketSelectImg ticketSelectImg);

	ApiResult ticketImgSort(List<TicketImgSortReqVO> ticketImgSortReqVOList);

	ApiResult getMapList(TicketDevicePointMapQueryVO vo);

	Integer getApprovedDispatchCount(String s);

    Integer getApprovedAcceptanceCount(String s);

    Boolean pushTicketMessageTask();

	List<TicketImgVO> getTicketAndDispatchImg(Long id);

    /**
     * 撤回申请
     * @param vo
     * @return
     */
	ApiResult ticketWithdraw(TicketSearchReqVO vo,TicketOpLogsReqVO ticketOpLogsReqVO );

    /**
     * 派工单合并
     * @param vo
     * @return
     */
	ApiResult dispatchMerge(TicketDispatchMergeVO vo);

    /**
     * 派工单取消合并
     * @param id
     * @return
     */
	ApiResult unDispatchMerge(Long id);

    /**
     * 子工单单个取消合并
     * @param id
     * @return
     */
	ApiResult unDispatchMergeOne(Long id);

    /**
     * 批量添加工单
     * @param newFileName
     * @param originName
     * @param userSession
     * @return
     */
	ApiResult batchAddTicket(String newFileName, String originName, UserSession userSession);

	void batchComplete(List<Long> ids);

    void completeAll(String userName);

	ApiResult importTicketList(TicketSearchReqVO tickets);

    ApiResult updateImport(TicketSearchRespVO tickets,UserSession userSession);

    ApiResult getAllPoint();

    ApiResult getAsset(Long pointId);

	ApiResult importCheck(TicketSearchReqVO tickets);

    ApiResult completeData(TicketSearchReqVO tickets);

	ApiResult saveLogs1(TicketOpLogsReqVO tickets);

    ApiResult pasteItem(String data,UserSession userSession);


    /**
     * 舆情转工单
     * @param vo
     */
    ApiResult po2Ticket(PoManagerRepeatMarkVO vo);

    /**
     * 故障工单统计
     * @param ticketChartReqVO
     * @return
     */
    ApiResult faultTicket(TicketChartReqVO ticketChartReqVO);

    /**
     * 故障工单统计表格
     * @param ticketChartReqVO
     * @return
     */
    ApiResult faultTicketTable(TicketChartReqVO ticketChartReqVO);

    /**
     * 故障来源统计
     * @param ticketChartReqVO
     * @return
     */
    ApiResult ticketPie(TicketChartReqVO ticketChartReqVO);


    /**
     * 导出日报
     * @param ticketChartReqVO
     * @param fileName
     * @return
     */
    String exportTicketReport(TicketChartReqVO ticketChartReqVO, String fileName);

	String exportTicketData(TicketSearchReqVO ticketSearchReqVO, UserSession userSession, String fileName);

    AppTicketChartVO getDealWay(AppTicketCharReqVO appTicketCharReqVO);
}
