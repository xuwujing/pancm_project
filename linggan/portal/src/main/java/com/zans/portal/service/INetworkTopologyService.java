package com.zans.portal.service;

import com.zans.base.service.BaseService;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.NetworkTopology;
import com.zans.portal.vo.common.TreeSelectTopo;
import com.zans.portal.vo.network.TopoSeqReqVO;
import com.zans.portal.vo.network.topology.resp.NetworkSwitcherMacRespVO;
import com.zans.portal.vo.network.topology.resp.TopologyExceptRespVO;
import com.zans.portal.vo.network.topology.resp.TopologyRespVO;

import java.util.List;

public interface INetworkTopologyService extends BaseService<NetworkTopology> {
    List<TreeSelectTopo> treeList(String ipAddr);

    TopologyRespVO view(Integer id,String ipAddr);

    List<TreeSelectTopo> init();


    /**
     * 核心交换机树形
     * @param ipAddr
     * @return
     */
    List<TreeSelectTopo> rootTreeList(Integer topoDataType,String ipAddr);

    PageResult<NetworkSwitcherMacRespVO> listNetworkSwitcherMac(String ipAddr, Integer pageNum, Integer pageSize) ;

    List<TopologyExceptRespVO> listTopologyExcept(String ipAddr);

	ApiResult switchList();

    ApiResult switchView(String ip);

	ApiResult switchView1(String ip);

    ApiResult switchTopo();

    ApiResult updateSeq(TopoSeqReqVO vo);

    List<NetworkTopology> findListBySourceIp(String sourceIp,Integer topoDataType);
}
