package com.zans.portal.dao;

import com.zans.portal.vo.asset.map.AssetMapInitReqVO;
import com.zans.portal.vo.asset.req.AssetReqVO;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.common.MapAssetVO;
import com.zans.portal.vo.common.MapCatalogueVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetMapMapper {
    List<AssetMapRespVO> assetMapList(AssetMapInitReqVO reqVO);


    List<MapCatalogueVO> assetMapForTreeList(MapAssetVO reqVO);

    List<AssetMapRespVO> assetMapList2(AssetReqVO reqVO);

    List<MapCatalogueVO> assetMapForXGJGTreeList(MapAssetVO reqVO);

    List<MapCatalogueVO> findMapTreeByName();
}
