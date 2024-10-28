package com.zans.portal.dao;

import java.util.List;

import com.zans.portal.model.RadiusEndpointProfile;
import com.zans.portal.vo.arp.AssetRespVO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;
@Repository
public interface RadiusEndpointProfileMapper extends Mapper<RadiusEndpointProfile> {

    List<AssetRespVO> findIpEndpointAliveNum(@Param("ipList") List<String> ipList);

	RadiusEndpointProfile getOne(Integer id);
}