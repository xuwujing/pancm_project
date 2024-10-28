package com.zans.mms.dao.mms;

import com.zans.mms.model.TicketOpLogs;
import com.zans.mms.vo.ticket.TicketOpLogsRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工单日志表(TicketOpLogs)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-03 15:38:11
 */
@Mapper
public interface TicketOpLogsDao extends tk.mybatis.mapper.common.Mapper<TicketOpLogs> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TicketOpLogs queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ticketOpLogs 实例对象
     * @return 对象列表
     */
    List<TicketOpLogs> queryAll(TicketOpLogs ticketOpLogs);

    /**
     * 新增数据
     *
     * @param ticketOpLogs 实例对象
     * @return 影响行数
     */
    @Override
    int insert(TicketOpLogs ticketOpLogs);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TicketOpLogs> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TicketOpLogs> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TicketOpLogs> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TicketOpLogs> entities);

    /**
     * 修改数据
     *
     * @param ticketOpLogs 实例对象
     * @return 影响行数
     */
    int update(TicketOpLogs ticketOpLogs);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);


    List<TicketOpLogsRespVO> queryTicketOpLogs(@Param("ticketId") Long ticketId, @Param("type") Integer type);



}

