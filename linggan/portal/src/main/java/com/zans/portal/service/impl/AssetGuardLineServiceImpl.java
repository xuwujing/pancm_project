package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.config.BaseConstants;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.AssetBranchMapper;
import com.zans.portal.dao.AssetGuardLineMapper;
import com.zans.portal.model.AssetGuardLine;
import com.zans.portal.model.AssetGuardLineAsset;
import com.zans.portal.service.*;
import com.zans.portal.vo.asset.branch.req.ChoiceDeviceReqVO;
import com.zans.portal.vo.asset.branch.resp.ChoiceDeviceRespVO;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineReqVO;
import com.zans.portal.vo.asset.guardline.req.GuardLineChoiceDeviceReqVO;
import com.zans.portal.vo.asset.guardline.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.MODULE_ARP_ALIVE;
import static com.zans.portal.config.GlobalConstants.SYS_DICT_KEY_ENABLE_STATUS;

@Service
@Slf4j
public class AssetGuardLineServiceImpl extends BaseServiceImpl<AssetGuardLine> implements IAssetGuardLineService {

    AssetGuardLineMapper assetGuardLineMapper;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAssetGuardLineAssetService assetGuardLineAssetService;

    @Autowired
    AssetBranchMapper assetBranchMapper;

    @Autowired
    IRadiusEndPointService radiusEndPointService;

    @Autowired
    IAlertRuleService alertRuleService;

    @Autowired
    ISysConstantService constantService;

    @Resource
    public void setAssetGuardLineMapper(AssetGuardLineMapper assetGuardLineMapper) {
        super.setBaseMapper(assetGuardLineMapper);
        this.assetGuardLineMapper = assetGuardLineMapper;
    }

    @Override
    public Integer getNextSeq() {
        return assetGuardLineMapper.getNextSeq();
    }

    @Override
    public List<AssetGuardLineRespVO> getListByName(String name) {
        List<AssetGuardLineRespVO> list = assetGuardLineMapper.findListByNameFuzzy(name);
        if (list == null) {
            list = new ArrayList<>(0);
        }
        return list;
    }

    @Override
    public boolean isNameExist(String name, Integer id) {
        if (StringUtils.isNotBlank(name)) {
            Integer count = assetGuardLineMapper.findCountByName(name, id);
            return count != null && count > 0;
        }
        return false;
    }

    @Override
    public List<AssetGuardLineListVO> getGuardLineAsset(AssetGuardLineReqVO reqVO) {
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_ENABLE_STATUS);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();
        List<AssetGuardLineListVO> list = assetGuardLineAssetService.getGuardLineAssetList(reqVO);
        for (AssetGuardLineListVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setAliveByMap(aliveMap);
            vo.setEnableStatusNameByMap(enableStatusMap);
            if (vo.getDeviceType() != null) {
                vo.setDeviceTypeName(deviceTypeMap.get(vo.getDeviceType()));
            }
            if (vo.getCommandExecuteStatus().intValue() == 1 && vo.getAlive() == GlobalConstants.ARP_ALIVE_OFFLINE) {// 命令执行中，设备下线
                vo.setCommandExecuteStatus(2);
                AssetGuardLineAsset asset = new AssetGuardLineAsset();
                asset.setId(vo.getAssetGuardLineAssetId());
                asset.setCommandExecuteStatus(2);
                assetGuardLineAssetService.updateSelective(asset);
            }
            if (vo.getHasAlert() != null && vo.getHasAlert() > 0) {
                vo.setHasAlert(1);
            } else {
                vo.setHasAlert(0);
            }
        }
        return list;
    }

    @Override
    public PageResult<ChoiceDeviceRespVO> choiceDevice(GuardLineChoiceDeviceReqVO reqVO) {
//        List<Integer> existAssetIds = assetGuardLineAssetService.getExistAssetIds(reqVO.getAssetGuardLineId());
        List<String> existAssetIds = assetGuardLineAssetService.getExistAssetIps(reqVO.getAssetGuardLineId());
        int pageNum = reqVO.getPageNum();
        int pageSize = reqVO.getPageSize();
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();
        ChoiceDeviceReqVO cVo = new ChoiceDeviceReqVO();
        BeanUtils.copyProperties(reqVO, cVo);
        cVo.setExistAssetIds(existAssetIds);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<ChoiceDeviceRespVO> list = assetBranchMapper.choiceDeviceAssetList(cVo);
        for (ChoiceDeviceRespVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setAliveByMap(aliveMap);
            if (vo.getDeviceType() != null) {
                vo.setDeviceTypeName(deviceTypeMap.get(vo.getDeviceType()));
            }

        }
        return new PageResult<ChoiceDeviceRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public List<ExcelAssetGuardLineStatisticsVO> getGuardListExcelList(AssetGuardLineReqVO reqVO) {
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_ENABLE_STATUS);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();
        List<ExcelAssetGuardLineStatisticsVO> list = assetGuardLineAssetService.getGuardListAssetExcelList(reqVO);
        for (ExcelAssetGuardLineStatisticsVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setAliveByMap(aliveMap);
            vo.setEnableStatusNameByMap(enableStatusMap);
            if (vo.getDeviceType() != null) {
                vo.setDeviceTypeName(deviceTypeMap.get(vo.getDeviceType()));
            }
        }
        return list;
    }

    @Override
    public AssetGuardLineJoinRespVO getDetailById(Integer id) {
        AssetGuardLineJoinRespVO joinVO = new AssetGuardLineJoinRespVO();
        AssetGuardLine self = this.getById(id);
        joinVO.setSlef(self);
        List<AssetGuardLine> list = assetGuardLineMapper.getAll();
        if (null == list || list.size() == 0) {
            joinVO.setNext(null);
            joinVO.setPrev(null);
            return joinVO;
        }
        AssetGuardLine next = null;
        AssetGuardLine prev = null;
        for (int i = 0; i < list.size(); i++) {
            AssetGuardLine assetBranch = list.get(i);
            if (id.intValue() == assetBranch.getId().intValue()) {
                if (i - 1 >= 0) {
                    prev = list.get(i - 1);
                }
                if (list.size() > i + 1) {
                    next = list.get(i + 1);
                }
                break;
            }
        }
        joinVO.setNext(next);
        joinVO.setPrev(prev);
        return joinVO;
    }

    @Override
    public void execForceOfflineCommand(List<Integer> gulineLineAssetIdList, List<String> ipAddr, Integer policy, UserSession userSession) {
        if (CollectionUtils.isNotEmpty(ipAddr)) {
//            List<GuardLineForceCommandObjectVO> list = assetGuardLineAssetService
//                    .findForceEndpointIds(gulineLineAssetIdList);
            List<GuardLineForceCommandObjectVO> list = assetGuardLineAssetService
                    .findForceEndpointIps(ipAddr);

            if (CollectionUtils.isNotEmpty(list)) {
                AssetGuardLineAsset asset = null;
                for (GuardLineForceCommandObjectVO vo : list) {
                    asset = new AssetGuardLineAsset();
                    asset.setId(vo.getId());
                    asset.setCommandExecuteStatus(1);
                    asset.setUpdateId(userSession.getUserId());
//                    if (vo.getEndpointId() != null) {
                    if (policy == null) {
                        return;
                    }
                    asset.setOfflineEndpointId(vo.getEndpointId());

                    ApiResult apiresult = radiusEndPointService.syncJudge(vo.getEndpointId(), policy, "警卫路线强制下线",
                            userSession);
                    if (apiresult.getCode() != BaseConstants.CODE_SUCCESS) {
                        asset.setCommandExecuteStatus(2);
                    }
//                        String radApiHost = constantService.getRadApi();
//                        String mac = vo.getPass();
//                        String ip = vo.getIpAddr();
//                        radiusEndPointService.radiusJudge(policy,radApiHost,mac,0,ip);
//                        asset.setCommandExecuteStatus(0);

//                    }
                    assetGuardLineAssetService.updateSelective(asset);
                }
            } else {
                log.error("未查询到该ip在radius_endpoint_profile表中存在！ip:{}", ipAddr);
            }
        }
    }


    @Override
    public void execForceOnlineCommand(List<Integer> gulineLineAssetIdList, List<String> ipAddr, Integer policy, UserSession userSession) {
        if (CollectionUtils.isNotEmpty(ipAddr)) {
//            List<GuardLineForceCommandObjectVO> list = assetGuardLineAssetService
//                    .findForceOnlineEndpointIds(gulineLineAssetIdList);

            List<GuardLineForceCommandObjectVO> list = assetGuardLineAssetService
                    .findForceEndpointIps2(ipAddr);


            if (CollectionUtils.isNotEmpty(list)) {
                AssetGuardLineAsset asset = null;
                for (GuardLineForceCommandObjectVO vo : list) {
                    asset = new AssetGuardLineAsset();
                    asset.setId(vo.getId());
                    asset.setCommandExecuteStatus(0);
                    asset.setUpdateId(userSession.getUserId());
                    if (vo.getEndpointId() != null) {
                        if (policy == null) {
                            return;
                        }
                        ApiResult apiresult = radiusEndPointService.syncJudge(vo.getEndpointId(), policy, "警卫路线恢复入网",
                                userSession);
                        if (apiresult.getCode() != BaseConstants.CODE_SUCCESS) {
                            asset.setCommandExecuteStatus(0);
                        }
//                        String radApiHost = constantService.getRadApi();
//                        String mac = vo.getPass();
//                        String ip = vo.getIpAddr();
//                        radiusEndPointService.radiusJudge(policy,radApiHost,mac,0,ip);
//                        asset.setCommandExecuteStatus(0);
                    }
                    assetGuardLineAssetService.updateSelective(asset);
                }
            } else {
                log.error("未查询到该ip在radius_endpoint表中存在！ip:{}", ipAddr);
            }
        }
    }

    @Override
    public boolean canAddNewLine() {
        Integer count = assetGuardLineMapper.findTotalCount();
        return count == null || count < 30;
    }

    @Override
    public int deleteByIpAddr(String ipAddr) {
        return assetGuardLineMapper.deleteByIpAddr(ipAddr);
    }

    @Override
    public ApiResult getPointList(AssetGuardLineReqVO reqVO) {
        List<AssetGuardLineListVO> assetPage = getGuardLineAsset(reqVO);
        JSONObject jsonObject = assetGuardLineMapper.getGroupStatistics(reqVO);
        JSONObject jsonObjectTotal = assetGuardLineMapper.getGroupStatisticsTotal(reqVO);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("data", assetPage)
                .put("data_statistics", jsonObject)
                .put("data_statistics_total", jsonObjectTotal)
                .build();
        return ApiResult.success(result);
    }
}
