package com.zans.dao;

import com.zans.model.RadiusEndpoint;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (RadiusEndpoint)表数据库访问层
 *
 * @author beixing
 * @since 2022-03-16 10:52:38
 */
@Mapper
public interface RadiusEndpointDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RadiusEndpoint queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param radiusEndpoint 实例对象
     * @return 对象列表
     */
    List<RadiusEndpoint> queryAll(RadiusEndpoint radiusEndpoint);


    List<RadiusEndpoint> findAllEndpointOfSwitcher(String swIp);

    /**
     * 新增数据
     *
     * @param radiusEndpoint 实例对象
     * @return 影响行数
     */
    int insert(RadiusEndpoint radiusEndpoint);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RadiusEndpoint> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RadiusEndpoint> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RadiusEndpoint> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<RadiusEndpoint> entities);

    /**
     * 修改数据
     *
     * @param radiusEndpoint 实例对象
     * @return 影响行数
     */
    int update(RadiusEndpoint radiusEndpoint);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

