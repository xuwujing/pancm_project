package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.StatsIpAlive;
import com.zans.portal.vo.stats.IpAliveResponseVO;
import com.zans.portal.vo.stats.IpAliveSearchVO;

import java.util.Date;

public interface IStatsIpAliveService extends BaseService<StatsIpAlive> {

    PageResult<IpAliveResponseVO> getIpAlivePage(IpAliveSearchVO reqVO, Date[] timeInterval);

    Double getPacket(String ip, Date start, Date end, Integer type);

}
