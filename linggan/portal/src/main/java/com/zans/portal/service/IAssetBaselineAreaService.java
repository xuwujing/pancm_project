package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.portal.model.AssetBaselineArea;
import com.zans.portal.vo.AssetBaselineAreaPageVO;

public interface IAssetBaselineAreaService extends BaseService<AssetBaselineArea> {

    AssetBaselineArea getByName(String areaName);

    int deleteById(Integer id);

    ApiResult list(AssetBaselineAreaPageVO vo);

    ApiResult listAll();

    AssetBaselineArea getOnlyLevel();


    int updateById(AssetBaselineArea assetBaselineArea);

}
