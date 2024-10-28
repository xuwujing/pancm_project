package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.ArithmeticUtil;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.AssetBranchAssetMapper;
import com.zans.portal.model.AssetBranchAsset;
import com.zans.portal.service.IAssetBranchAssetService;
import com.zans.portal.vo.asset.branch.req.AssetBranchAssetAddReqVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchReqVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchStatisticsVO;
import com.zans.portal.vo.asset.branch.req.ExcelAssetBranchStatisticsVO;
import com.zans.portal.vo.asset.resp.AssetStatisRespVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.zans.portal.config.GlobalConstants.DELETE_NOT;

@Service
public class AssetBranchIAssetServiceImpl extends BaseServiceImpl<AssetBranchAsset>
        implements IAssetBranchAssetService {

    @Resource
    AssetBranchAssetMapper assetBranchAssetMapper;

    @Resource
    public void setAssetBranchAssetMapper(AssetBranchAssetMapper assetBranchAssetMapper) {
        super.setBaseMapper(assetBranchAssetMapper);
        this.assetBranchAssetMapper = assetBranchAssetMapper;
    }

    @Override
    public int groupsAddAsset(AssetBranchAssetAddReqVO req, UserSession userSession) {
        AssetBranchAsset assetBranchAsset = new AssetBranchAsset();
        BeanUtils.copyProperties(req, assetBranchAsset);
        AssetBranchAsset unique = assetBranchAssetMapper.getByAssetIdAndBranchId(req.getIpAddr(),
                req.getAssetBranchId());
        if (unique != null) {
            // 存在 激活即可
            assetBranchAsset.setDeleteStatus(DELETE_NOT);
            assetBranchAsset.setUpdateId(userSession.getUserId());
            assetBranchAsset.setId(unique.getId());
            assetBranchAssetMapper.updateByPrimaryKeySelective(assetBranchAsset);
        } else {
            assetBranchAsset.setDeleteStatus(DELETE_NOT);
            assetBranchAsset.setUpdateId(userSession.getUserId());
            assetBranchAsset.setCreatorId(userSession.getUserId());
            assetBranchAssetMapper.insertSelective(assetBranchAsset);
        }
        return 1;
    }

    @Override
    public List<AssetBranchStatisticsVO> assetBranchList(AssetBranchReqVO reqVO) {
        return assetBranchAssetMapper.assetBranchList(reqVO);
    }

    @Override
    public List<ExcelAssetBranchStatisticsVO> assetBranchListForExcel(AssetBranchReqVO reqVO) {
        return assetBranchAssetMapper.assetBranchListForExcel(reqVO);
    }

    @Override
    public List<AssetStatisRespVO> getAssetBranchStatis() {
        List<AssetStatisRespVO> resultList = assetBranchAssetMapper.findAssetBranchStatis();
        if (CollectionUtils.isEmpty(resultList)){
            resultList = new ArrayList<>();
        }
        resultList.forEach(vo -> vo.setOnlineRate(ArithmeticUtil.percent(vo.getAliveNum(), vo.getTotalNum(), 1)));
        return resultList;
    }

    @Override
    public int deleteByIpAddr(String ipAddr) {
        return assetBranchAssetMapper.deleteByIpAddr(ipAddr);
    }
}
