package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.NetworkSwitcherInterface;
import com.zans.portal.vo.switcher.SwitcherInterfaceReqVO;
import com.zans.portal.vo.switcher.SwitcherInterfaceRespVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NetworkSwitcherInterfaceMapper extends Mapper<NetworkSwitcherInterface> {

    List<SelectVO> getInterfaceBySwId(@Param("swId") String swId);

    List<SwitcherInterfaceRespVO> getSwitcherInterfacePage(@Param("reqVo") SwitcherInterfaceReqVO reqVO);


}