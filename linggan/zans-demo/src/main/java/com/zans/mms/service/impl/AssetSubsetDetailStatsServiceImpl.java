package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.mms.AssetSubsetDetailStatsMapper;
import com.zans.mms.model.AssetSubsetDetailStats;
import com.zans.mms.service.IAssetSubsetDetailStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 *  AssetSubsetDetailStatsServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("assetSubsetDetailStatsService")
public class AssetSubsetDetailStatsServiceImpl extends BaseServiceImpl<AssetSubsetDetailStats> implements IAssetSubsetDetailStatsService {


	@Autowired
	private AssetSubsetDetailStatsMapper assetSubsetDetailStatsMapper;

    @Resource
    public void setAssetSubsetDetailStatsMapper(AssetSubsetDetailStatsMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.assetSubsetDetailStatsMapper = baseMapper;
    }


}
