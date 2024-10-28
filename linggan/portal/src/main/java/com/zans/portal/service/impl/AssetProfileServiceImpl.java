package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.AssetProfileMapper;
import com.zans.portal.model.AssetProfile;
import com.zans.portal.service.IAssetProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AssetProfileServiceImpl extends BaseServiceImpl<AssetProfile> implements IAssetProfileService {
    @Autowired
    AssetProfileMapper assetProfileMapper;


    @Resource
    public void setAssetProfileMapper(AssetProfileMapper assetProfileMapper) {
        super.setBaseMapper(assetProfileMapper);
        this.assetProfileMapper = assetProfileMapper;
    }

    @Override
    public AssetProfile findByIdAddr(String ipAddr) {
        return assetProfileMapper.findByIdAddr(ipAddr);
    }
}
