package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.AssetBranchAssetStatistics;
import com.zans.portal.vo.asset.branch.req.AssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.ExcelAssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.RunningDetailListReqVO;

import java.util.List;

public interface IAssetBranchAssetStatisticsService extends BaseService<AssetBranchAssetStatistics> {
    List<AssetBranchStatisticsVO> runningDetailList(RunningDetailListReqVO reqVO);

    List<ExcelAssetBranchStatisticsVO> runningDetailListForExcel(RunningDetailListReqVO reqVO);
}
