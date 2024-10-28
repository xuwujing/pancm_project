package com.zans.mms.dao.guard;

import com.zans.mms.model.RadiusEndpointProfile;
import com.zans.mms.vo.arp.AssetRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RadiusEndpointProfileMapper extends Mapper<RadiusEndpointProfile> {

    List<AssetRespVO> findIpEndpointAliveNum(@Param("ipList") List<String> ipList);

}