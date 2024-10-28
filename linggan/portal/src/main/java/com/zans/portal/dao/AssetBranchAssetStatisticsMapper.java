package com.zans.portal.dao;

import com.zans.portal.model.AssetBranchAssetStatistics;
import com.zans.portal.vo.asset.branch.req.AssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.ExcelAssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.RunningDetailListReqVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AssetBranchAssetStatisticsMapper extends Mapper<AssetBranchAssetStatistics> {
    List<AssetBranchStatisticsVO> runningDetailList(@Param("reqVO") RunningDetailListReqVO reqVO);

    List<ExcelAssetBranchStatisticsVO> runningDetailListForExcel(@Param("reqVO")RunningDetailListReqVO reqVO);
}