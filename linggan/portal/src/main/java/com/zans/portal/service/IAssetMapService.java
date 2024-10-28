package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.asset.map.AssetMapConvergeRespVO;
import com.zans.portal.vo.asset.map.AssetMapInitReqVO;
import com.zans.portal.vo.asset.req.AssetMapUpdateReqVO;
import com.zans.portal.vo.asset.req.AssetReqVO;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.common.MapAssetVO;
import com.zans.portal.vo.common.MapCatalogueVO;
import com.zans.portal.vo.common.TreeSelect;

import java.util.List;


/**
 * @description: 资产地图相关
 * @author: ns_wang
 * @time: 2020/11/4 11:14
 */
public interface IAssetMapService {
    AssetMapConvergeRespVO assetMap(AssetMapInitReqVO reqVO);

    List<MapCatalogueVO> assetMapForTreeList(MapAssetVO reqVO);

    PageResult<AssetMapRespVO> assetMapListPage(AssetReqVO reqVO);

    List<MapCatalogueVO> assetMapForXGJGTreeList(MapAssetVO reqVO);

    ApiResult updateAssetMap(AssetMapUpdateReqVO searchVO);


    /**
    * @Author beixing
    * @Description  获取地图的结构和数据
    * @Date  2021/9/23
    * @Param
    * @return
    **/
    List<TreeSelect> getMapTreeAndData(Integer parentId);



    /**
     * @Author beixing
     * @Description  获取地图的结构
     * @Date  2021/9/23
     * @Param
     * @return
     **/
    List<TreeSelect> getMapTree(Integer assetBranchId);

    /**
    * @Author beixing
    * @Description  根据分组Id获取地图的数据
    * @Date  2021/9/23
    * @Param
    * @return
    **/
    List<AssetBaselineVO> assetMapForTreeList(Integer assetBranchId);


}
