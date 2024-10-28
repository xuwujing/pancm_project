package com.zans.portal.dao;

import com.zans.portal.model.NetworkSwitcherMac;
import com.zans.portal.vo.switcher.SwitcherMacReqVO;
import com.zans.portal.vo.switcher.SwitcherMacRespVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NetworkSwitcherMacMapper extends Mapper<NetworkSwitcherMac> {

    List<SwitcherMacRespVO> findSwitcherMacList(@Param("reqVo") SwitcherMacReqVO reqVO);

}