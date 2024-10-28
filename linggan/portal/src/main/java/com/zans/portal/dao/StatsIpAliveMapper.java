package com.zans.portal.dao;

import com.zans.portal.model.StatsIpAlive;
import com.zans.portal.vo.stats.IpAliveResponseVO;
import com.zans.portal.vo.stats.IpAliveSearchVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface StatsIpAliveMapper extends Mapper<StatsIpAlive> {

    List<IpAliveResponseVO> findIpAliveList(@Param("reqVo") IpAliveSearchVO reqVo, @Param("start") Date start, @Param("end") Date end);

    Double findPacketLoss(@Param("ip") String ip, @Param("start") Date start, @Param("end") Date end);

    Double findPacketTime(@Param("ip") String ip, @Param("start") Date start, @Param("end") Date end);

}