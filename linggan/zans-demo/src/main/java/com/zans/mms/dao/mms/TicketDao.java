package com.zans.mms.dao.mms;

import com.zans.mms.model.Ticket;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.ticket.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}

