package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.portal.model.AssetLocalTrack;

/**
 * 资产轨迹表(AssetLocalTrack)表服务接口
 *
 * @author beixing
 * @since 2022-06-10 17:59:56
 */
public interface IAssetLocalTrackService {

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
    ApiResult list(AssetLocalTrack assetLocalTrack);


    /**
     * 新增数据
     *
     * @param assetLocalTrack 实例对象
     * @return 实例对象
     */
    int insert(AssetLocalTrack assetLocalTrack);

    /**
     * 修改数据
     *
     * @param assetLocalTrack 实例对象
     * @return 实例对象
     */
    int update(AssetLocalTrack assetLocalTrack);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    ApiResult view(AssetLocalTrack assetLocalTrack);
}
