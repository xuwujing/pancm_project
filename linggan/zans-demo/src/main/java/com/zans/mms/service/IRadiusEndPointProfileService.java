package com.zans.mms.service;

import java.util.List;

import com.zans.base.service.BaseService;
import com.zans.mms.model.RadiusEndpointProfile;
import com.zans.mms.vo.arp.AssetRespVO;


public interface IRadiusEndPointProfileService extends BaseService<RadiusEndpointProfile> {



    List<AssetRespVO> findIpEndpointAliveNum(List<String> ipList);
}
