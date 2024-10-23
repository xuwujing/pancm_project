package com.zans.dao;

import com.zans.model.AsynQueue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (AsynQueue)表数据库访问层
 *
 * @author beixing
 * @since 2021-11-30 17:36:02
 */
@Mapper
public interface AsynQueueDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AsynQueue queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param asynQueue 实例对象
     * @return 对象列表
     */
    List<AsynQueue> queryAll(AsynQueue asynQueue);

    /**
     * 新增数据
     *
     * @param asynQueue 实例对象
     * @return 影响行数
     */
    int insert(AsynQueue asynQueue);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AsynQueue> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AsynQueue> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AsynQueue> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AsynQueue> entities);

    /**
     * 修改数据
     *
     * @param asynQueue 实例对象
     * @return 影响行数
     */
    int update(AsynQueue asynQueue);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

