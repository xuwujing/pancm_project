package com.zans.dao;

import com.zans.model.AsynConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (AsynConfig)表数据库访问层
 *
 * @author beixing
 * @since 2021-11-22 11:50:18
 */
@Mapper
public interface AsynConfigDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AsynConfig queryById(Integer id);

    /**
     * 项目ID
     * @param projectId
     * @return
     */
    AsynConfig queryByProjectId(String projectId);



    /**
     * 通过实体作为筛选条件查询
     *
     * @param asynConfig 实例对象
     * @return 对象列表
     */
    List<AsynConfig> queryAll(AsynConfig asynConfig);

    /**
     * 新增数据
     *
     * @param asynConfig 实例对象
     * @return 影响行数
     */
    int insert(AsynConfig asynConfig);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AsynConfig> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AsynConfig> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AsynConfig> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AsynConfig> entities);

    /**
     * 修改数据
     *
     * @param asynConfig 实例对象
     * @return 影响行数
     */
    int update(AsynConfig asynConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

