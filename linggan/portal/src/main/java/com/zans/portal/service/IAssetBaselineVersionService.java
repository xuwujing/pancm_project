package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.model.AssetBaselineVersion;
import com.zans.portal.vo.AssetBaselineVersionVO;
import com.zans.base.vo.ApiResult;

import java.util.List;


/**
 * @author beixing
 * @Title: 基线变更记录表(AssetBaselineVersion)表服务接口
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:10:23
 */
public interface IAssetBaselineVersionService extends BaseService<AssetBaselineVersion> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetBaselineVersionVO queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetBaselineVersionVO 实例对象
     * @return 对象列表
     */
    ApiResult list(AssetBaselineVersionVO assetBaselineVersionVO);


    /**
     * 新增数据
     *
     * @param assetBaselineVersionVO 实例对象
     * @return 实例对象
     */
    int insert(AssetBaselineVersionVO assetBaselineVersionVO);

    /**
     * 修改数据
     *
     * @param assetBaselineVersionVO 实例对象
     * @return 实例对象
     */
    int update(AssetBaselineVersionVO assetBaselineVersionVO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);


}
