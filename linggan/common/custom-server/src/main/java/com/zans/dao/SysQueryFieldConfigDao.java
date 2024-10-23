package com.zans.dao;

import com.zans.model.SysQueryFieldConfig;
import com.zans.strategy.QueryRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义表查询项(SysQueryFieldConfig)表数据库访问层
 *
 * @author beixing
 * @since 2022-05-20 11:28:40
 */
@Mapper
public interface SysQueryFieldConfigDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysQueryFieldConfig queryById(Integer id);


    List<QueryRespVO> queryByModule(String module);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysQueryFieldConfig 实例对象
     * @return 对象列表
     */
    List<SysQueryFieldConfig> queryAll(SysQueryFieldConfig sysQueryFieldConfig);

    /**
     * 新增数据
     *
     * @param sysQueryFieldConfig 实例对象
     * @return 影响行数
     */
    int insert(SysQueryFieldConfig sysQueryFieldConfig);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysQueryFieldConfig> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysQueryFieldConfig> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysQueryFieldConfig> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysQueryFieldConfig> entities);

    /**
     * 修改数据
     *
     * @param sysQueryFieldConfig 实例对象
     * @return 影响行数
     */
    int update(SysQueryFieldConfig sysQueryFieldConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

