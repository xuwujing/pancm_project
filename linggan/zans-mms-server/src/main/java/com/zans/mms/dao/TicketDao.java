package com.zans.mms.dao;

import com.zans.base.vo.ApiResult;
import com.zans.mms.dto.workflow.TicketWorkflowDto;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.model.Screen;
import com.zans.mms.model.Ticket;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.ticket.*;
import com.zans.mms.vo.ticket.chart.FaultTicketRespVO;
import com.zans.mms.vo.ticket.chart.TicketPieCharRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工单表(Ticket)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-02 16:58:57
 */
@Mapper
public interface TicketDao {


	int queryDraftByUser(String user);
	/**
	 * 通过ID查询单条数据
	 *
	 * @param id 主键
	 * @return 实例对象
	 */
	TicketSearchRespVO queryById(Long id);


	TicketDispatchViewRespVO queryTicketsDispatchViewById(Long ticketId);


	TicketAcceptViewRespVO queryTicketsAcceptViewById(Long ticketId);

	List<TicketBaseMfRespVO> queryTicketById(Long ticketId, Integer type);



	/**
	 * 通过实体作为筛选条件查询
	 *
	 * @param tickets 实例对象
	 * @return 对象列表
	 */
	List<TicketSearchRespVO> queryAll(TicketSearchReqVO tickets);



	List<TicketSearchRespVO> queryAppAll(TicketSearchReqVO tickets);




	/**
	 * 新增数据
	 *
	 * @param tickets 实例对象
	 * @return 影响行数
	 */
	int insert(Ticket tickets);

	/**
	 *  app新增
	 **/
	int appSave(Ticket ticket);

	/**
	 * @Author pancm
	 * @Description 工单创建
	 * @Date  2021/3/10
	 * @Param [tickets]
	 * @return int
	 **/
	int create(Ticket tickets);

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<Tickets> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<Ticket> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<Tickets> 实例对象列表
	 * @return 影响行数
	 */
	int insertOrUpdateBatch(@Param("entities") List<Ticket> entities);

	/**
	 * 修改数据
	 *
	 * @param tickets 实例对象
	 * @return 影响行数
	 */
	int update(Ticket tickets);

	/**
	 * 通过主键删除数据
	 *
	 * @param id 主键
	 * @return 影响行数
	 */
	int deleteById(Long id);


	List<CircleUnit> getPcTicketTotal(TicketSearchReqVO ticket);

	List<CircleUnit> getAppTicketTotal(TicketSearchReqVO ticket);


	List<CountUnit> getPcFaultType();


	List<CountUnit> getPcTicketSource();


	List<CircleUnit> getPcMaintainFacility();




	TicketsDispatchPdfVO queryTicketsDispatchPdfByCode(@Param("ticketCode") String ticketCode);

	List<TicketByFaultTypeVO> statisticsByFaultType();

	List<TicketCountVO> ticketStatistics();

	void updateProcessInstanceIdById(Ticket ticket);


	/**
	 * 根据流程实例id获取工单信息
	 * @param processInstanceId
	 * @return
	 */
	TicketWorkflowDto getByProcessInstanceId(String processInstanceId);

	/**
	 * 通过id查询工单数据方法
	 * @param id
	 * @return
	 */
	TicketWorkflowDto getById(String id);

	List<Ticket> getByIds(@Param("ids") List<Long> ids);

	void updateAcceptanceStatusName(@Param("id") Long id, @Param("name") String name);

	void updateDispatchStatusName(@Param("id") Long id,@Param("name") String name);

	void updateBase(Ticket ticket);

	Screen getScreen(String userName);

	void updateScreen(Screen screen);

	Integer isExist(Screen screen);

	void insertScreen(Screen screen);

	List<TicketWorkflowDto> maintainApprovedTicketList(String orgId);

	List<TicketWorkflowDto> maintainTicketList(String orgId);

	List<TicketWorkflowDto> maintainApprovalTicketList(String orgId);

	int isExistTicketCodeRsult(Ticket ticket);

	void clearBackRecord(@Param("id") Long id,@Param("type") String type);

	List<Map<String, Object>> breakdownTicketBarChart();

	List<Integer> getBreakdownTicketTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getBreakdownTicketComplete(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getBreakdownTicketCompleteNum(AppTicketCharReqVO appTicketCharReqVO);

	Map<String, Integer> breakdownTicketCompare(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getDispatchTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getCompleteDispatchTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getMoneyDispatchTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getMaxMoneyDispatchTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getAcceptanceTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getCompleteAcceptanceTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getMoneyAcceptanceTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Integer> getMaxMoneyAcceptanceTotal(AppTicketCharReqVO appTicketCharReqVO);

	List<Map<String, Object>> getAppFaultType(AppTicketCharReqVO appTicketCharReqVO);

	List<Map<String, Object>> getAppTicketSource(AppTicketCharReqVO appTicketCharReqVO);

	List<Map<String, Object>> getDeviceType(AppTicketCharReqVO appTicketCharReqVO);

	List<String> getdispatchPredictCost(AppTicketCharReqVO appTicketCharReqVO);

	List<String> getAcceptancePredictCost(AppTicketCharReqVO appTicketCharReqVO);

	Map<String, Integer> dispatchTicketCompare(AppTicketCharReqVO appTicketCharReqVO);

	Map<String, Integer> acceptanceTicketCompare(AppTicketCharReqVO appTicketCharReqVO);

	Integer getAcceptanceApprovedList(AppTicketCharReqVO appTicketCharReqVO);

	Integer getDispatchApprovedList(AppTicketCharReqVO appTicketCharReqVO);

	List<TicketImgVO> getTicketImg(@Param("ids") List<Long> id);

	List<BaseVfs> getImgByIds(@Param("ids") List<Long> ids);


	/** --------------------日报相关sql----------------------------------**/
	Integer getBreakdownTicketWithoutOrgId(@Param("type") int type);

	Integer getBreakdownTicketCompleteWithoutOrgId(@Param("type") int type);

	String getDispatchMoney(@Param("type") int type);

	Integer getDispatchNum(@Param("type") int type);

	String getAcceptanceMoney(@Param("type") int type);

	Integer getAcceptanceNum(@Param("type") int type);

	//todo
	Integer getApprovedDispatchCount(@Param("type") int type,@Param("username") String username);

	Integer getApprovedAcceptanceCount(@Param("type") int type,@Param("username") String username);

	List<TicketImgVO> getTicketAndDispatchImg(@Param("ids") List<Long> ids);

	void updatePid(Ticket ticket);

	List<Ticket> getByPid(@Param("pid") Long id);

	Ticket getTicket(@Param("id") Long id);

	List<Long> getIdByPid(@Param("id") Long id);

	String getCode(@Param("id")Long id);

	void clearPid(@Param("id") Long id);

	void clearIsMerge(@Param("id") Long id);

	void complete(@Param("id") Long id);


	void completeAll(@Param("username") String userName);

	List<TicketSearchRespVO> queryImport(TicketSearchReqVO tickets);

	void updateImport(TicketSearchRespVO tickets);

	void relationPatrolTaskCheckResult(@Param("id") Long id,@Param("resultId") Long resultId);

	void relationPoManager(@Param("id") Long id,@Param("poId") Long resultId);

	FaultTicketRespVO queryFaultTicket(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("orgId")String orgId);

	List<TicketPieCharRespVO> getTicketSource(@Param("startTime") String startTime, @Param("endTime") String endTime);

	List<TicketPieCharRespVO> getTicketDeviceType(@Param("startTime") String startTime,@Param("endTime") String endTime);

	List<TicketPieCharRespVO> getTicketFaultType(@Param("startTime") String startTime,@Param("endTime") String endTime);

	void maintainApprovedTime(Ticket ticket);

	String getDispatchRecentTime(@Param("id") Long id);

	String getAcceptRecentTime(@Param("id")Long id);

    List<Map<String, Object>> getDealWay(AppTicketCharReqVO appTicketCharReqVO);
}

