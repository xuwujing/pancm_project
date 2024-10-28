package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.BasePage;
import com.zans.base.vo.PagePlusResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.AssetGuardLineAssetMapper;
import com.zans.portal.model.AssetGuardLineAsset;
import com.zans.portal.service.IAssetGuardLineAssetService;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineAssetAddReqVO;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineReqVO;
import com.zans.portal.vo.asset.guardline.resp.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssetGuardLineAssetServiceImpl extends BaseServiceImpl<AssetGuardLineAsset>
        implements IAssetGuardLineAssetService {

    AssetGuardLineAssetMapper assetGuardLineAssetMapper;

    @Resource
    public void setAssetGuardLineAssetMapper(AssetGuardLineAssetMapper assetGuardLineAssetMapper) {
        super.setBaseMapper(assetGuardLineAssetMapper);
        this.assetGuardLineAssetMapper = assetGuardLineAssetMapper;
    }

    @Override
    public int addAssetToLine(AssetGuardLineAssetAddReqVO req, UserSession userSession) {
        AssetGuardLineAsset asset = new AssetGuardLineAsset();
        BeanUtils.copyProperties(req, asset);
        AssetGuardLineAsset unique = assetGuardLineAssetMapper.getByAssetIdAndLineId(req.getAssetId(),
                req.getGuardLineId());
        if (unique != null) {
            // 存在 激活即可
            asset.setDeleteStatus(GlobalConstants.DELETE_NOT);
            asset.setUpdateId(userSession.getUserId());
            asset.setId(unique.getId());
            assetGuardLineAssetMapper.updateByPrimaryKeySelective(asset);
        } else {
            asset.setDeleteStatus(GlobalConstants.DELETE_NOT);
            asset.setUpdateId(userSession.getUserId());
            asset.setCreatorId(userSession.getUserId());
            assetGuardLineAssetMapper.insertSelective(asset);
        }
        return 1;
    }

    @Override
    public List<AssetGuardLineListVO> getGuardLineAssetList(AssetGuardLineReqVO reqVO) {
        return assetGuardLineAssetMapper.findGuardLineAssetList(reqVO);
    }

    @Override
    public List<Integer> getExistAssetIds(Integer guardLineId) {
        if (guardLineId != null) {
            return assetGuardLineAssetMapper.findExistAssetIds(guardLineId);
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<String> getExistAssetIps(Integer guardLineId) {
        if (guardLineId != null) {
            return assetGuardLineAssetMapper.findExistAssetIps(guardLineId);
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<ExcelAssetGuardLineStatisticsVO> getGuardListAssetExcelList(AssetGuardLineReqVO reqVO) {
        return assetGuardLineAssetMapper.findGuardLineAssetExcelList(reqVO);
    }

    @Override
    public PageResult<GuardLineRunningListRespVO> runningList(BasePage reqVO) {
        List<GuardLineRunningListRespVO> listAll = assetGuardLineAssetMapper.findRunningStatis();
        if (CollectionUtils.isNotEmpty(listAll)) {
            listAll.forEach(re -> re.resetOnlineRate());
        }
        int pageNum = reqVO.getPageNum();
        int pageSize = reqVO.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<GuardLineRunningListRespVO> list = assetGuardLineAssetMapper.findRunningStatis();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(re -> re.resetOnlineRate());
        }
        return new PagePlusResult<GuardLineRunningListRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum,
                listAll);
    }

    @Override
    public List<ExcelGuardLineRunningListRespVO> runningListExport() {
        List<ExcelGuardLineRunningListRespVO> list = assetGuardLineAssetMapper.findRunningStatisExport();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(re -> re.resetOnlineRate());
        }
        return list;
    }

    @Override
    public List<GuardLineForceCommandObjectVO> findForceEndpointIds(List<Integer> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            return assetGuardLineAssetMapper.findForceEndpointIds(idList);
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<GuardLineForceCommandObjectVO> findForceEndpointIps(List<String> ipList) {
        return assetGuardLineAssetMapper.findForceEndpointIps(ipList);
    }

    @Override
    public List<AssetGuardLineAsset> getAllByLineId(int lineId) {
        return assetGuardLineAssetMapper.getByLineId(lineId);
    }


    @Override
    public List<GuardLineForceCommandObjectVO> getCommandExecResult(List<Integer> gulineLineAssetIdList) {
        if (CollectionUtils.isNotEmpty(gulineLineAssetIdList)){
            List<GuardLineForceCommandObjectVO> list = assetGuardLineAssetMapper.findCommandExecResult(gulineLineAssetIdList);
            if (CollectionUtils.isNotEmpty(list)){
                list.forEach(vo -> {
                    if (vo.getCommandExecuteStatus().intValue() == 1 && vo.getAlive() == GlobalConstants.ARP_ALIVE_OFFLINE) {// 命令执行中，设备下线
                        vo.setCommandExecuteStatus(2);
                        AssetGuardLineAsset asset = new AssetGuardLineAsset();
                        asset.setId(vo.getId());
                        asset.setCommandExecuteStatus(2);
                        this.updateSelective(asset);
                    }
                });
                return list;
            }
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<GuardLineForceCommandObjectVO> findForceOnlineEndpointIds(List<Integer> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            return assetGuardLineAssetMapper.findForceOnlineEndpointIds(idList);
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<GuardLineForceCommandObjectVO> findForceEndpointIps2(List<String> ipList) {
        return assetGuardLineAssetMapper.findForceEndpointIps2(ipList);
    }


}
