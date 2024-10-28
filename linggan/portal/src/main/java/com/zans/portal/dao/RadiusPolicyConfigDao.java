package com.zans.portal.dao;

import com.zans.portal.model.RadiusPolicyConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * (RadiusPolicyConfig)表数据库访问层
 *
 * @author beixing
 * @since 2021-09-23 17:34:17
 */
@Mapper
public interface RadiusPolicyConfigDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RadiusPolicyConfig queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param radiusPolicyConfig 实例对象
     * @return 对象列表
     */
    List<RadiusPolicyConfig> queryAll(RadiusPolicyConfig radiusPolicyConfig);

    /**
     * 新增数据
     *
     * @param radiusPolicyConfig 实例对象
     * @return 影响行数
     */
    int insert(RadiusPolicyConfig radiusPolicyConfig);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RadiusPolicyConfig> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RadiusPolicyConfig> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RadiusPolicyConfig> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<RadiusPolicyConfig> entities);

    /**
     * 修改数据
     *
     * @param radiusPolicyConfig 实例对象
     * @return 影响行数
     */
    int update(RadiusPolicyConfig radiusPolicyConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    @Select("${sqlStr}")
    List<Map<String, String>> executeSql(@Param(value = "sqlStr") String sqlStr);
}

