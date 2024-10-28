package com.zans.portal.dao;

import com.zans.portal.model.NetworkTopology;
import com.zans.portal.model.SysSwitcher;
import com.zans.portal.vo.network.TopoSeqReqVO;
import com.zans.portal.vo.network.topology.resp.NetworkSwitcherMacRespVO;
import com.zans.portal.vo.network.topology.resp.TopologyExceptRespVO;
import com.zans.portal.vo.network.topology.resp.TopologyRespVO;
import com.zans.portal.vo.switcher.SwitchBaseInfoVO;
import com.zans.portal.vo.switcher.SwitchTopologyVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface NetworkTopologyMapper extends Mapper<NetworkTopology> {
    List<NetworkTopology> getListCondition(Map<String,Object> map);

    TopologyRespVO findViewByIdAndIp(@Param(value = "id")Integer id,@Param(value = "ipAddr") String ipAddr);

    TopologyRespVO findViewByIp(@Param(value = "ipAddr") String ipAddr);


    List<NetworkSwitcherMacRespVO> listNetworkSwitcherMac(@Param(value = "ipAddr") String ipAddr);

    List<TopologyExceptRespVO> listTopologyExcept(@Param(value = "ipAddr") String ipAddr);

	List<SwitchBaseInfoVO> switchList();

	List<String> getNextLevelIp(String ip);

	List<SwitchTopologyVO> switchRelation();

    void updateSeq(TopoSeqReqVO old);
}