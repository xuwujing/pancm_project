package com.zans.portal.dao;

import com.zans.portal.model.IpAlloc;
import com.zans.portal.vo.ip.IpAllocRespVO;
import com.zans.portal.vo.ip.IpAllocSearchVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IpAllocMapper extends Mapper<IpAlloc> {

    List<IpAllocRespVO> findAllIpAlloc(@Param("ip") IpAllocSearchVO ip);

    IpAllocRespVO findIpAlloc(@Param("id") Integer id);

    Long isIpAlloc(@Param("id") Integer id);
}