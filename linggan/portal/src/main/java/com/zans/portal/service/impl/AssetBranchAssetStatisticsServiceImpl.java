package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.AssetBranchAssetStatisticsMapper;
import com.zans.portal.model.AssetBranchAssetStatistics;
import com.zans.portal.service.IAssetBranchAssetStatisticsService;
import com.zans.portal.vo.asset.branch.req.AssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.ExcelAssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.RunningDetailListReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AssetBranchAssetStatisticsServiceImpl extends BaseServiceImpl<AssetBranchAssetStatistics> implements IAssetBranchAssetStatisticsService {
    @Autowired
    AssetBranchAssetStatisticsMapper assetBranchAssetStatisticsMapper;
    @Resource
    public void setAssetBranchAssetStatisticsMapper(AssetBranchAssetStatisticsMapper assetBranchAssetStatisticsMapper) {
        super.setBaseMapper(assetBranchAssetStatisticsMapper);
        this.assetBranchAssetStatisticsMapper = assetBranchAssetStatisticsMapper;
    }

    @Override
    public List<AssetBranchStatisticsVO> runningDetailList(RunningDetailListReqVO reqVO) {
        return assetBranchAssetStatisticsMapper.runningDetailList(reqVO);
    }

    @Override
    public List<ExcelAssetBranchStatisticsVO> runningDetailListForExcel(RunningDetailListReqVO reqVO) {
        return assetBranchAssetStatisticsMapper.runningDetailListForExcel(reqVO);
    }
}
