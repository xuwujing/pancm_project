package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.portal.vo.asset.req.AssetBranchSnapShootReqVO;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/20
 */
public interface IAssetBranchSnapShootService {

    ApiResult getAssetBranchSnapShoot(AssetBranchSnapShootReqVO req);

    ApiResult save(AssetBranchSnapShootReqVO req);



}
