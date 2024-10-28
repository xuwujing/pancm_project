package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.AssetBranchStatistics;

public interface IAssetBranchStatisticsService extends BaseService<AssetBranchStatistics> {
    /**
     * 获取下一个自增id
     * @return
     */
    Integer getNextId();
}
