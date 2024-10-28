package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.SysSwitcher;
import com.zans.portal.vo.switcher.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ISwitcherService extends BaseService<SysSwitcher> {

    PageResult<SwitchResVO> getSwitchPage(SwitchSearchVO reqVO);

    SysSwitcher findBySwHost(String swHost, Integer id);




    SwitcherScanRespVO getScanBySwId(SwitcherScanReqVO reqVO);

    SwitcherScanRespVO getLastScanBySwId(String ipAddr);

    PageResult<SwitcherScanListRespVO> getSwitchScanPage(SwitcherScanReqVO reqVO);


    List<SwitcherScanListRespVO> findSwitchScanList(SwitcherScanReqVO reqVO);

    List<SwitcherScanListRespVO> findSwitchScanDateHourList(SwitcherScanReqVO reqVO);

    /**
     * 从交换机配置信息中提取VLAN配置，并计算每个vlan的可用IP范围
     */
    void splitVlanInfoFromConfig();

    List<SysSwitcher> getListCondition(HashMap<String, Object> map);



}
