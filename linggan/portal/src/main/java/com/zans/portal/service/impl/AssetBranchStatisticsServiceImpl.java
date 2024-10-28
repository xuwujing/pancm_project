package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.AssetBranchStatisticsMapper;
import com.zans.portal.model.AssetBranchStatistics;
import com.zans.portal.service.IAssetBranchStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AssetBranchStatisticsServiceImpl extends BaseServiceImpl<AssetBranchStatistics>  implements IAssetBranchStatisticsService {
    @Autowired
    AssetBranchStatisticsMapper AssetBranchStatisticsMapper;
    @Resource
    public void setAssetBranchStatisticsMapper(AssetBranchStatisticsMapper AssetBranchStatisticsMapper) {
        super.setBaseMapper(AssetBranchStatisticsMapper);
        this.AssetBranchStatisticsMapper = AssetBranchStatisticsMapper;
    }

    @Override
    public Integer getNextId() {
        return AssetBranchStatisticsMapper.getNextId();
    }
}
