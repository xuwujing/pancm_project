package com.zans.portal.dao;

import com.zans.portal.model.AssetBranchAsset;
import com.zans.portal.vo.asset.branch.req.AssetBranchReqVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.ExcelAssetBranchStatisticsVO;
import com.zans.portal.vo.asset.resp.AssetStatisRespVO;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AssetBranchAssetMapper extends Mapper<AssetBranchAsset> {
    AssetBranchAsset getByAssetIdAndBranchId(@Param("ipAddr") String ipAddr, @Param("assetBranchId")Integer assetBranchId);

    List<AssetBranchStatisticsVO> assetBranchList(@Param("reqVO") AssetBranchReqVO reqVO);

    List<ExcelAssetBranchStatisticsVO> assetBranchListForExcel(@Param("reqVO") AssetBranchReqVO reqVO);

    /**
     * 获取资产子集在线率、资产总数等统计数据
     * @return
     */
    List<AssetStatisRespVO> findAssetBranchStatis();



    int deleteByIpAddr(String ipAddr);
}
