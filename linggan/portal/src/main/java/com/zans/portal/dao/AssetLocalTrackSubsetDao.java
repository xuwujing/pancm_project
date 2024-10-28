package com.zans.portal.dao;

import com.zans.portal.model.AssetLocalTrackSubset;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 资产轨迹子集(AssetLocalTrackSubset)表数据库访问层
 *
 * @author beixing
 * @since 2022-06-10 18:00:14
 */
@Mapper
public interface AssetLocalTrackSubsetDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetLocalTrackSubset queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetLocalTrackSubset 实例对象
     * @return 对象列表
     */
    List<AssetLocalTrackSubset> queryAll(AssetLocalTrackSubset assetLocalTrackSubset);

    /**
     * 新增数据
     *
     * @param assetLocalTrackSubset 实例对象
     * @return 影响行数
     */
    int insert(AssetLocalTrackSubset assetLocalTrackSubset);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetLocalTrackSubset> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetLocalTrackSubset> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetLocalTrackSubset> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetLocalTrackSubset> entities);

    /**
     * 修改数据
     *
     * @param assetLocalTrackSubset 实例对象
     * @return 影响行数
     */
    int update(AssetLocalTrackSubset assetLocalTrackSubset);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

