package com.zans.portal.dao;

import com.zans.portal.model.AssetBranchStatistics;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface AssetBranchStatisticsMapper extends Mapper<AssetBranchStatistics> {
    Integer getNextId();
}