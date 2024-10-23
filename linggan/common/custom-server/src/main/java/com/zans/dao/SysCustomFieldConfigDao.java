package com.zans.dao;

import com.alibaba.fastjson.JSONObject;
import com.zans.model.SysCustomFieldConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 自定义列表(SysCustomFieldConfig)表数据库访问层
 *
 * @author beixing
 * @since 2022-05-07 15:47:31
 */
@Mapper
public interface SysCustomFieldConfigDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysCustomFieldConfig queryById(Integer id);

    SysCustomFieldConfig queryByModule(String module);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysCustomFieldConfig 实例对象
     * @return 对象列表
     */
    List<SysCustomFieldConfig> queryAll(SysCustomFieldConfig sysCustomFieldConfig);

    /**
     * 新增数据
     *
     * @param sysCustomFieldConfig 实例对象
     * @return 影响行数
     */
    int insert(SysCustomFieldConfig sysCustomFieldConfig);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysCustomFieldConfig> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysCustomFieldConfig> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysCustomFieldConfig> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysCustomFieldConfig> entities);

    /**
     * 修改数据
     *
     * @param sysCustomFieldConfig 实例对象
     * @return 影响行数
     */
    int update(SysCustomFieldConfig sysCustomFieldConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    @Select("${sqlStr}")
    List<JSONObject> executeSql(@Param(value = "sqlStr") String sqlStr);


}

