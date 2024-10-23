package com.zans.dao;

import com.zans.model.SysConstant;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 常量表(SysConstant)表数据库访问层
 *
 * @author beixing
 * @since 2022-03-15 16:37:51
 */
@Mapper
public interface SysConstantDao {

    /**
     * 通过ID查询单条数据
     *
     * @param key 主键
     * @return 实例对象
     */
    SysConstant queryByKey(String key);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysConstant 实例对象
     * @return 对象列表
     */
    List<SysConstant> queryAll(SysConstant sysConstant);

    /**
     * 新增数据
     *
     * @param sysConstant 实例对象
     * @return 影响行数
     */
    int insert(SysConstant sysConstant);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysConstant> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysConstant> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysConstant> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysConstant> entities);

    /**
     * 修改数据
     *
     * @param sysConstant 实例对象
     * @return 影响行数
     */
    int update(SysConstant sysConstant);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

