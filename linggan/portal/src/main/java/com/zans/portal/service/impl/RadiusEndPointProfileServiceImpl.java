package com.zans.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.RadiusEndpointProfileMapper;
import com.zans.portal.model.IpAll;
import com.zans.portal.model.RadiusEndpointProfile;
import com.zans.portal.service.IRadiusEndPointProfileService;
import com.zans.portal.vo.arp.AssetRespVO;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

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
    public List<RadiusEndpointProfile> findByCurIpAddr(String ipAddr) {
        if (StringUtils.isEmpty(ipAddr)) {
            return new ArrayList<>(0);
        }
        Example example = new Example(RadiusEndpointProfile.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("curIpAddr", ipAddr);
        List<RadiusEndpointProfile> list = endpointProfileMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<AssetRespVO> findIpEndpointAliveNum(List<String> ipList) {
        if (CollectionUtils.isNotEmpty(ipList)){
            return endpointProfileMapper.findIpEndpointAliveNum(ipList);
        }
        return new ArrayList<>(0);
    }

    @Override
    public RadiusEndpointProfile getOne(Integer id) {
        return endpointProfileMapper.getOne(id);
    }
}
