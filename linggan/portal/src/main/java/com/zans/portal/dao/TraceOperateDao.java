package com.zans.portal.dao;

import com.zans.portal.model.TraceOperate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (TraceOperate)表数据库访问层
 *
 * @author beixing
 * @since 2022-02-28 17:47:55
 */
@Mapper
public interface TraceOperateDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TraceOperate queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param traceOperate 实例对象
     * @return 对象列表
     */
    List<TraceOperate> queryAll(TraceOperate traceOperate);

    /**
     * 新增数据
     *
     * @param traceOperate 实例对象
     * @return 影响行数
     */
    int insert(TraceOperate traceOperate);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TraceOperate> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TraceOperate> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TraceOperate> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TraceOperate> entities);

    /**
     * 修改数据
     *
     * @param traceOperate 实例对象
     * @return 影响行数
     */
    int update(TraceOperate traceOperate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

