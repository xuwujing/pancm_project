package com.zans.dao;

import com.zans.model.RadiusEndpointProfile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (RadiusEndpointProfile)表数据库访问层
 *
 * @author beixing
 * @since 2022-03-23 16:28:52
 */
@Mapper
public interface RadiusEndpointProfileDao {

    /**
     * 通过ID查询单条数据
     *
     * @param endpointId 主键
     * @return 实例对象
     */
    RadiusEndpointProfile queryById(Integer endpointId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param radiusEndpointProfile 实例对象
     * @return 对象列表
     */
    List<RadiusEndpointProfile> queryAll(RadiusEndpointProfile radiusEndpointProfile);

    /**
     * 新增数据
     *
     * @param radiusEndpointProfile 实例对象
     * @return 影响行数
     */
    int insert(RadiusEndpointProfile radiusEndpointProfile);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RadiusEndpointProfile> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RadiusEndpointProfile> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RadiusEndpointProfile> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<RadiusEndpointProfile> entities);

    /**
     * 修改数据
     *
     * @param radiusEndpointProfile 实例对象
     * @return 影响行数
     */
    int update(RadiusEndpointProfile radiusEndpointProfile);

    /**
     * 通过主键删除数据
     *
     * @param endpointId 主键
     * @return 影响行数
     */
    int deleteById(Integer endpointId);

}

