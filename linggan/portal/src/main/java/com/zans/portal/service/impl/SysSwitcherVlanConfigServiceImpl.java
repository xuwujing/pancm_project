package com.zans.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.SysSwitcherVlanConfigMapper;
import com.zans.portal.model.SysSwitcherVlanConfig;
import com.zans.portal.service.ISysSwitcherVlanConfigService;
import com.zans.portal.vo.switcher.SwitcherVlanConfigRespVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysSwitcherVlanConfigServiceImpl extends BaseServiceImpl<SysSwitcherVlanConfig>
        implements ISysSwitcherVlanConfigService {

    SysSwitcherVlanConfigMapper switcherVlanConfigMapper;

    @Resource
    public void setSwitcherVlanConfigMapper(SysSwitcherVlanConfigMapper switcherVlanConfigMapper) {
        this.setBaseMapper(switcherVlanConfigMapper);
        this.switcherVlanConfigMapper = switcherVlanConfigMapper;
    }

    @Override
    public void deleteAll() {
        switcherVlanConfigMapper.deleteAll();
    }

    @Override
    public boolean existRecord(int swId, int vlan, String ip, String mask) {
        return switcherVlanConfigMapper.findCountBySwitcherVlan(swId, vlan, ip, mask) > 0;
    }

    @Override
    public List<SwitcherVlanConfigRespVO> getIpMatchVlanList(String ip) {
        if (StringUtils.isNotBlank(ip)){
            return switcherVlanConfigMapper.findIpMatchVlanList(ip);
        }
        return new ArrayList<>(0);
    }
        
        
}
