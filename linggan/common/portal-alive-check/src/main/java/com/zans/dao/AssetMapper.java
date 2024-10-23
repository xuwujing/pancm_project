package com.zans.dao;


import com.zans.model.Asset;
import com.zans.vo.AliveHeartbeatVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetMapper  {



	AliveHeartbeatVO findAliveHeartBeatByIp(String ip);

    int updateAliveHeartBeat(String ipAddr, int alive);

    int saveAliveHeartBeat(String ipAddr, int alive);

    List<String> getIpAll();

    Asset getEndpointByIp(String ipAddr);

    List<String> getIpAllByRadiusPoint();

}
