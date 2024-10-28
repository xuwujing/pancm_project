package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.AssetProfile;

public interface IAssetProfileService extends BaseService<AssetProfile> {

    AssetProfile findByIdAddr(String ipAddr);

}
