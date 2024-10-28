package com.zans.mms.dao.mms;

import com.zans.mms.model.TicketPointDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TicketPointDevice)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-03 10:52:22
 */
@Mapper
public interface TicketPointDeviceDao extends tk.mybatis.mapper.common.Mapper<TicketPointDevice>{

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TicketPointDevice queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ticketPointDevice 实例对象
     * @return 对象列表
     */
    List<TicketPointDevice> queryAll(TicketPointDevice ticketPointDevice);

    /**
     * 新增数据
     *
     * @param ticketPointDevice 实例对象
     * @return 影响行数
     */
    @Override
    int insert(TicketPointDevice ticketPointDevice);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TicketPointDevice> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TicketPointDevice> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TicketPointDevice> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TicketPointDevice> entities);

    /**
     * 修改数据
     *
     * @param ticketPointDevice 实例对象
     * @return 影响行数
     */
    int update(TicketPointDevice ticketPointDevice);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    int deleteByTicketId(Long ticketId,Long pointId);

}

