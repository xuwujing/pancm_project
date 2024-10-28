package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.AssetBranchAsset;
import com.zans.portal.vo.asset.branch.req.AssetBranchAssetAddReqVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchReqVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.ExcelAssetBranchStatisticsVO;
import com.zans.portal.vo.asset.resp.AssetStatisRespVO;

import java.util.List;

public interface IAssetBranchAssetService extends BaseService<AssetBranchAsset> {
    int groupsAddAsset(AssetBranchAssetAddReqVO req, UserSession userSession);

    List<AssetBranchStatisticsVO> assetBranchList(AssetBranchReqVO reqVO);

    List<ExcelAssetBranchStatisticsVO> assetBranchListForExcel(AssetBranchReqVO reqVO);


    /**
     * 获取设备子集在线率、资产总数等统计数据
     * @return
     */
    List<AssetStatisRespVO> getAssetBranchStatis();

    int deleteByIpAddr(String ipAddr);
}
