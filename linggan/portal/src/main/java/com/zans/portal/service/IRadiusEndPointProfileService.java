package com.zans.portal.service;

import java.util.List;

import com.zans.base.service.BaseService;
import com.zans.portal.model.RadiusEndpointProfile;
import com.zans.portal.vo.arp.AssetRespVO;

public interface IRadiusEndPointProfileService extends BaseService<RadiusEndpointProfile> {

    /**
     * 根据Profile表中的cur_ip_addr匹配数据
     * @param ipAddr
     * @return
     */
    List<RadiusEndpointProfile> findByCurIpAddr(String ipAddr);

    List<AssetRespVO> findIpEndpointAliveNum(List<String> ipList);

	/**
	 * 获取一条
	 * @param id
	 * @return
	 */
	RadiusEndpointProfile getOne(Integer id);
}
