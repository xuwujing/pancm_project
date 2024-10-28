package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.guard.RadiusEndpointProfileMapper;
import com.zans.mms.model.RadiusEndpointProfile;
import com.zans.mms.service.IRadiusEndPointProfileService;
import com.zans.mms.vo.arp.AssetRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ns_wang
 */
@Service
@Slf4j
public class RadiusEndPointProfileServiceImpl extends BaseServiceImpl<RadiusEndpointProfile>
        implements IRadiusEndPointProfileService {

    RadiusEndpointProfileMapper endpointProfileMapper;

    @Resource
    public void setEndpointProfileMapper(RadiusEndpointProfileMapper endpointProfileMapper) {
        super.setBaseMapper(endpointProfileMapper);
        this.endpointProfileMapper = endpointProfileMapper;
    }



    @Override
    public List<AssetRespVO> findIpEndpointAliveNum(List<String> ipList) {
        if (CollectionUtils.isNotEmpty(ipList)){
            return endpointProfileMapper.findIpEndpointAliveNum(ipList);
        }
        return new ArrayList<>(0);
    }

    
}
