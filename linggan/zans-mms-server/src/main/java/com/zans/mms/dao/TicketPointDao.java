package com.zans.mms.dao;

import com.zans.mms.model.TicketPoint;
import com.zans.mms.vo.ticket.TicketPointResVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TicketPoint)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-02 17:24:46
 */
@Mapper
public interface TicketPointDao extends tk.mybatis.mapper.common.Mapper<TicketPoint> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TicketPoint queryById(Long id);


    TicketPoint findOne(Long ticketId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ticketPoint 实例对象
     * @return 对象列表
     */
    List<TicketPoint> queryAll(TicketPoint ticketPoint);


    List<TicketPointResVO> queryByTicketId(Long id);




    /**
     * 新增数据
     *
     * @param ticketPoint 实例对象
     * @return 影响行数
     */
    @Override
    int insert(TicketPoint ticketPoint);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TicketPoint> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TicketPoint> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TicketPoint> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TicketPoint> entities);

    /**
     * 修改数据
     *
     * @param ticketPoint 实例对象
     * @return 影响行数
     */
    int update(TicketPoint ticketPoint);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);


    int deleteByTicketId(Long ticketId,Long pointId);

	List<TicketPoint> queryByIds(@Param("ids") List<Long> ids);
}

