package com.zans.portal.dao;

import com.zans.portal.model.TraceData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (TraceData)表数据库访问层
 *
 * @author beixing
 * @since 2022-01-11 11:40:36
 */
@Mapper
public interface TraceDataDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TraceData queryById(Long id);

    List<TraceData> queryByTraceId(String traceId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param traceData 实例对象
     * @return 对象列表
     */
    List<TraceData> queryAll(TraceData traceData);

    /**
     * 新增数据
     *
     * @param traceData 实例对象
     * @return 影响行数
     */
    int insert(TraceData traceData);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TraceData> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TraceData> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TraceData> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TraceData> entities);

    /**
     * 修改数据
     *
     * @param traceData 实例对象
     * @return 影响行数
     */
    int update(TraceData traceData);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

