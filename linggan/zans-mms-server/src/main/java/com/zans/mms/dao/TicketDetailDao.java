package com.zans.mms.dao;

import com.zans.mms.model.BaseMaintaionFacility;
import com.zans.mms.model.TicketDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TicketDetail)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-05 18:13:51
 */
@Mapper
public interface TicketDetailDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TicketDetail queryById(Long id);


    TicketDetail queryByTicketId(Long ticketId,Integer type);
    /**
     * 通过实体作为筛选条件查询
     *
     * @param ticketDetail 实例对象
     * @return 对象列表
     */
    List<TicketDetail> queryAll(TicketDetail ticketDetail);

    /**
     * 新增数据
     *
     * @param ticketDetail 实例对象
     * @return 影响行数
     */
    int insert(TicketDetail ticketDetail);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TicketDetail> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TicketDetail> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TicketDetail> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TicketDetail> entities);

    /**
     * 修改数据
     *
     * @param ticketDetail 实例对象
     * @return 影响行数
     */
    int update(TicketDetail ticketDetail);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);


    int deleteByTicketId(Long ticketId,Integer type);


    void updateAcceptance(Long id);

    BaseMaintaionFacility getIdByCodeAndDept(@Param("deviceCode") String deviceCode, @Param("orgId") String currentAllocDepartmentNum);
}

