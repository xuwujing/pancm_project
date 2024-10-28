package com.zans.portal.dao;

import java.util.List;

import com.zans.portal.model.SysSwitcherVlanConfig;
import com.zans.portal.vo.switcher.SwitcherVlanConfigRespVO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SysSwitcherVlanConfigMapper extends Mapper<SysSwitcherVlanConfig> {

    void deleteAll();

    Integer findCountBySwitcherVlan(@Param("swId") int swId,@Param("vlan") int vlan,@Param("ip") String ip,@Param("mask") String mask);

    List<SwitcherVlanConfigRespVO> findIpMatchVlanList(@Param("ip") String ip);
}