package com.zans.portal.dao;

import com.alibaba.fastjson.JSONObject;
import com.zans.portal.model.AssetLocalTrack;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 资产轨迹表(AssetLocalTrack)表数据库访问层
 *
 * @author beixing
 * @since 2022-06-10 17:59:56
 */
@Mapper
public interface AssetLocalTrackDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetLocalTrack queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetLocalTrack 实例对象
     * @return 对象列表
     */
    List<AssetLocalTrack> queryAll(AssetLocalTrack assetLocalTrack);

    /**
     * 新增数据
     *
     * @param assetLocalTrack 实例对象
     * @return 影响行数
     */
    int insert(AssetLocalTrack assetLocalTrack);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetLocalTrack> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetLocalTrack> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetLocalTrack> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetLocalTrack> entities);

    /**
     * 修改数据
     *
     * @param assetLocalTrack 实例对象
     * @return 影响行数
     */
    int update(AssetLocalTrack assetLocalTrack);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    JSONObject getGroupId(String id);

}

