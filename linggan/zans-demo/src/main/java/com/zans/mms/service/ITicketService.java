package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.Ticket;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.ticket.*;

import java.util.List;

/**
 * 工单表(Tickets)表服务接口
 *
 * @author beixing
 * @since 2021-01-13 18:14:23
 */
public interface ITicketService {





    /**
    *  app列表查询
     *  注：只查询当前用户所属机构的数据
    **/
    ApiResult appList(TicketSearchReqVO tickets, UserSession userSession);

    /**
     *  工单查看
     **/
    ApiResult appView(Long id);

    /**
     * @Author pancm
     * @Description 工单打卡
     * @Date  2021/3/9
     * @Param [tickets]
     * @return com.zans.base.vo.ApiResult
     **/
    ApiResult appClockIn(TicketReportReqVO tickets, UserSession userSession);


    /**
     * @Author pancm
     * @Description 工单维修上报
     * @Date  2021/3/9
     * @Param [tickets]
     * @return com.zans.base.vo.ApiResult
     **/
    ApiResult appMaintenanceStatusReport(TicketReportReqVO tickets, UserSession userSession);

    /**
    *  小程序工单新增
     *  必定为故障工单，且是未分配状态
    **/
    ApiResult appSaveTicket(AppTicketSaveReqVO appTicketSaveReqVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tickets 实例对象
     * @param userSession
     * @return 对象列表
     */
    ApiResult ticketList(TicketSearchReqVO tickets, UserSession userSession);




    ApiResult ticketSubmit(TicketSaveReqVO tickets);


    ApiResult ticketAddPoint(TicketSaveReqVO tickets);


    ApiResult ticketDelPoint(TicketSaveReqVO tickets);



    /**
     * @Author pancm
     * @Description 创建工单，返回主键ID
     * @Date  2021/3/10
     * @Param []
     * @return com.zans.base.vo.ApiResult
     **/
    ApiResult ticketCreate(TicketSaveReqVO ticketSaveReqVO);



    /**
     *  工单查看
     **/
    ApiResult ticketView(Long id);


   /**
   * @Author beixing
   * @Description
   * @Date  2021/4/6
   * @Param
   * @return
   **/
    ApiResult viewEdit(TicketViewEditVO vo);


    /**
     * @Author pancm
     * @Description 派工单详情
     * @Date  2021/1/16
     * @Param [id]
     * @return com.zans.base.vo.ApiResult
     **/
    ApiResult viewDispatch(Long id);


    ApiResult viewDispatchEdit(TicketDispatchViewEditVO ticketsDispatchViewEditVO);


    ApiResult editDispatch(TicketDispatchEditVO ticketsDispatchEditVO) ;


    /**
     * @Author pancm
     * @Description 验收单详情
     * @Date  2021/1/16
     * @Param [id]
     * @return com.zans.base.vo.ApiResult
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
     * @Author pancm
     * @Description 转工单接口
     * @Date  2021/1/14
     * @Param [issueNum, user]
     * @return boolean
     **/
    boolean saveAndTransformTickets(String issueNum,String user);


    /**
     * @Author pancm
     * @Description 工单分配
     * @Date  2021/3/3
     * @Param [ticketOpLogsReqVO]
     * @return com.zans.base.vo.ApiResult
     **/
    ApiResult assign(TicketOpLogsReqVO ticketOpLogsReqVO);

    /**
     * @Author pancm
     * @Description 状态处置
     * @Date  2021/3/3
     * @Param [ticketOpLogsReqVO]
     * @return com.zans.base.vo.ApiResult
     **/
    ApiResult dispose(TicketOpLogsReqVO ticketOpLogsReqVO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    ApiResult deleteById(Long id);



    /**
     * 派工单导出pdf
     * @param
     * @return
     */
    String exportDispatch(String ticketCode);

    /**
     * 验收单导出pdf
     * @param
     * @return
     */
    String exportAccept(String ticketCode);



    /**
     *  获取Pc首页的总数
     *
     * */
    List<CircleUnit> getPcTicketTotal(UserSession userSession);

    /**
     * 获取App首页的总数
     *
     * */
    List<CircleUnit> getAppTicketTotal(UserSession userSession);

    /**
     * 获取故障类型占比
     *
     * */
    List<CountUnit> getPcFaultType(UserSession userSession);


    /**
     * 获取派工来源
     *
     * */
    List<CountUnit> getPcTicketSource(UserSession userSession);


    /**
     * 获取运维单位占比
     *
     * */
    List<CircleUnit> getPcMaintainFacility(UserSession userSession);


    /**
     * 工单按故障类型统计,用于在图表中显示
     * @return
     */
	ApiResult statisticsByFaultType();

    /**
     * 工单数量统计
     * @return
     */
	ApiResult ticketStatistics();

    /**
     * 工单按设备类型统计
     * @return
     */
	ApiResult statisticsTicketByDeviceType();

	/**
	* @Author beiming
	* @Description  一键工单
	* @Date  4/13/21
	* @Param TicketCreateByAssetReqVO
	* @return
	**/
    ApiResult<Ticket> createByAsset(TicketCreateByAssetReqVO reqVO);
}
