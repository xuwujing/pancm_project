package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.guard.SysSwitcherVlanConfigMapper;
import com.zans.mms.model.SysSwitcherVlanConfig;
import com.zans.mms.service.ISysSwitcherVlanConfigService;
import com.zans.mms.vo.switcher.SwitcherVlanConfigRespVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    public List<SwitcherVlanConfigRespVO> getIpMatchVlanList(String ip) {
        if (StringUtils.isNotBlank(ip)){
            return switcherVlanConfigMapper.findIpMatchVlanList(ip);
        }
        return new ArrayList<>(0);
    }

        
        
}
