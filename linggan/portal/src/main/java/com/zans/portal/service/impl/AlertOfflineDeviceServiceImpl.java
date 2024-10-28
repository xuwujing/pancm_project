package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.AlertOfflineDeviceMapper;
import com.zans.portal.dao.AlertRuleMapper;
import com.zans.portal.service.IAlertOfflineDeviceService;
import com.zans.portal.service.IAssetService;
import com.zans.portal.service.ISwitcherService;
import com.zans.portal.vo.alert.AlertRecordRespVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceDisposeVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceResVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceSearchVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchDisposeReqVO;
import com.zans.portal.vo.switcher.SwitcherScanRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlertOfflineDeviceServiceImpl implements IAlertOfflineDeviceService {

    @Autowired
    AlertOfflineDeviceMapper alertOfflineDeviceMapper;
    @Autowired
    IAssetService assetService;
    @Autowired
    ISwitcherService switcherService;
    @Autowired
    private AlertRuleMapper mapper;

    @Override
    public PageResult<OfflineDeviceResVO> getDevicePage(OfflineDeviceSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        List<OfflineDeviceResVO> resVOList = alertOfflineDeviceMapper.getAlertOfflineDevicePage(reqVO);

        resVOList.forEach(it->{
            if (it.getDeviceType()!=null && it.getDeviceType() == 1) {
               SwitcherScanRespVO respVO = switcherService.getLastScanBySwId(it.getIpAddr());
               if (respVO != null) {
                   it.setSwAlive(respVO.getAlive());
                   it.setOfflineType(respVO.getOfflineType());
               }
            }
        });

        Map<String, Object> map = new HashMap<>();
        Set<String> ids = new HashSet<>();
        for (OfflineDeviceResVO alertRecordRespVO : resVOList) {
            ids.add(alertRecordRespVO.getKeywordValue());
        }
        if(ids.size()>0){
            map.put("ids",ids);
            mapper.batchUpdateByIds(map);
        }

        return new PageResult<OfflineDeviceResVO>(page.getTotal(), resVOList, reqVO.getPageSize(), reqVO.getPageNum());
    }

    private void updateReadStatus(List<AlertRecordRespVO> alertRecordRespVOList) {
        Map<String, Object> map = new HashMap<>();
        Set<String> ids = new HashSet<>();
        for (AlertRecordRespVO alertRecordRespVO : alertRecordRespVOList) {
            ids.add(alertRecordRespVO.getKeywordValue());
        }
        map.put("ids",ids);
        mapper.batchUpdateByIds(map);
    }

    @Override
    public Integer countOfflineDeviceByTime(Date aliveLastTime,boolean isLessThan) {
        return alertOfflineDeviceMapper.countOfflineDeviceByTime(aliveLastTime,isLessThan);
    }

    @Override
    public int dispose(OfflineDeviceDisposeVO disposeVO, UserSession userSession) {

        List<OfflineDeviceResVO> resVOList =  alertOfflineDeviceMapper.getByIds(disposeVO.getAlertIds());
        for (OfflineDeviceResVO vo:resVOList) {

            AssetBranchDisposeReqVO reqVO = new AssetBranchDisposeReqVO();
            reqVO.setIpAddr(vo.getIpAddr());
//          0，停用;1,启用
            reqVO.setEnableStatus(disposeVO.getEnableStatus());
            reqVO.setRemark(disposeVO.getReason());
            assetService.disposeAsset(reqVO,userSession);
        }
        return 1;
    }
}
