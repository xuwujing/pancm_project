package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.guard.AreaMapper;
import com.zans.mms.dao.guard.ArpMapper;
import com.zans.mms.model.Arp;
import com.zans.mms.service.*;
import com.zans.mms.vo.arp.AssetRespVO;
import com.zans.mms.vo.arp.AssetSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ArpServiceImpl extends BaseServiceImpl<Arp> implements IArpService {
    /**
     * t_arp.dis_status 状态
     */
    public static String MODULE_ARP_DIS_STATUS = "dis_status";
    public static String MODULE_ARP_ALIVE = "alive";
    public static String MODULE_ARP_MUTE = "mute";
    public static String MODULE_ARP_CHANGE_TYPE = "change_type";
    public static String MODULE_ARP_IP_STATUS = "ip_status";
    public static String MODULE_ARP_MODEL_LEVEL = "model_level";
    public static String MODULE_ARP_RISK_TYPE = "risk_type";
    public static String MODULE_ARP_CONFIRM_STATUS = "arp_confirm_status";
    public static String MODULE_IP_ALL_PROJECT_STATUS = "project_status";

    /**
     * 数据字典 是否可用
     */
    public static String SYS_DICT_KEY_ENABLE = "enable";
    public static String SYS_DICT_KEY_ENABLE_STATUS = "enable_status";

    /**
     * 数据字典  资产录入来源
     */
    public static String SYS_DICT_KEY_SOURCE = "source";

    public static String SYS_DICT_KEY_MAINTAIN_STATUS = "maintain_status";

    @Autowired
    private IRadiusEndPointService endPointService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;

    @Autowired
    private IRadiusEndPointProfileService endPointProfileService;

    ArpMapper arpMapper;

    @Resource
    public void setArpMapper(ArpMapper arpMapper) {
        super.setBaseMapper(arpMapper);
        this.arpMapper = arpMapper;
    }

    @Override
    public PageResult<AssetRespVO> getAssetPage(AssetSearchVO asset) {
        int pageNum = asset.getPageNum();
        int pageSize = asset.getPageSize();

        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(MODULE_ARP_MUTE);
        Map<Object, String> ipStatusMap = constantItemService.findItemsMapByDict(MODULE_ARP_IP_STATUS);
        Map<Object, String> disStatusMap = constantItemService.findItemsMapByDict(MODULE_ARP_DIS_STATUS);
        Map<Object, String> riskTypeMap = constantItemService.findItemsMapByDict(MODULE_ARP_RISK_TYPE);
        Map<Object, String> confirmMap = constantItemService.findItemsMapByDict(MODULE_ARP_CONFIRM_STATUS);
        Map<Object, String> projectStatusMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_PROJECT_STATUS);

        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_ENABLE_STATUS);
        Map<Object, String> sourceMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_SOURCE);
        Map<Object, String> maintainStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_MAINTAIN_STATUS);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();
        Map<Object, String> areaNameMap = areaService.findAreaToMap();

        Page page = PageHelper.startPage(pageNum, pageSize);
        /**
         * @description: 资产列表改为查endpoint相关 和首页公用一个查询主表条件
         * edit by ns_wang 2020/10/13 14:19
         */
        List<AssetRespVO> list = endPointService.findAssetsForList(asset);
        List<String> ipList = new ArrayList<>(list.size());
        for (AssetRespVO row : list) {
            if(row == null){
                continue;
            }
            ipList.add(row.getIpAddr());
            row.setAliveByMap(aliveMap);
            row.setMuteByMap(muteMap);
            row.setIpStatusByMap(ipStatusMap);
            row.setDisStatusByMap(disStatusMap);
            row.setRiskTypeNameByMap(riskTypeMap);
            row.setConfirmStatusNameByMap(confirmMap);
            row.setProjectStatusNameByMap(projectStatusMap);
            row.setEnableStatusNameByMap(enableStatusMap);
            row.setSourceNameByMap(sourceMap);
            if (row.getDeviceType() != null){
                row.setDeviceTypeName(deviceTypeMap.get(row.getDeviceType()));
            }
            if (row.getAreaId() != null){
                row.setAreaName(areaNameMap.get(row.getAreaId()));
            }
            row.setMaintainStatusNameByMap(maintainStatusMap);
        }
        //获取每个ip当前接入设备数量
        List<AssetRespVO> ipCurEndpointNum = endPointProfileService.findIpEndpointAliveNum(ipList);
        if (CollectionUtils.isNotEmpty(ipCurEndpointNum)){
            Map<String,Integer> ipEndpointNumMap = ipCurEndpointNum.stream().collect(Collectors.toMap(r->r.getIpAddr(), r->r.getCurAliveDeviceNum()));
            for (AssetRespVO row : list) {
                if(ipEndpointNumMap.get(row.getIpAddr()) != null){
                    row.setCurAliveDeviceNum(ipEndpointNumMap.get(row.getIpAddr()));
                }
            }
        }
        return new PageResult<AssetRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

}
