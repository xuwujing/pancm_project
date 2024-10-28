package com.zans.portal.controller;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.NetworkTopology;
import com.zans.portal.service.IAreaService;
import com.zans.portal.service.INetworkTopologyService;
import com.zans.portal.service.ISwitcherService;
import com.zans.portal.vo.common.TreeSelectTopo;
import com.zans.portal.vo.network.TopoSeqReqVO;
import com.zans.portal.vo.network.topology.resp.NetworkSwitcherMacRespVO;
import com.zans.portal.vo.network.topology.resp.TopologyExceptRespVO;
import com.zans.portal.vo.network.topology.resp.TopologyRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = {"/topology ~ 交换机拓扑图"})
@RestController
@Validated
@RequestMapping("/network/networkTopology")
@Slf4j
public class NetworkTopologyController extends BasePortalController {

    @Autowired
    INetworkTopologyService topologyService;

    @Autowired
    ISwitcherService switcherService;

    @Autowired
    IAreaService areaService;

    @ApiOperation(value = "/list", notes = "拓扑图树形图")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ApiResult<List<TreeSelectTopo>> list(@RequestParam(value = "topoDataType", required = true) Integer topoDataType,@RequestParam(value = "ipAddr",required = false) String ipAddr) {
        return ApiResult.success(topologyService.rootTreeList(topoDataType,ipAddr));
    }

    @ApiOperation(value = "/findListBySourceIp", notes = "根据源ip找目标ip")
    @RequestMapping(value = "/findListBySourceIp", method = {RequestMethod.GET})
    public ApiResult<List<NetworkTopology>> findListBySourceIp( @RequestParam(value = "sourceIp") String sourceIp,@RequestParam(value = "topoDataType") Integer topoDataType) {
        List<NetworkTopology> list = topologyService.findListBySourceIp(sourceIp,topoDataType);
        return ApiResult.success(list);
    }


    @ApiOperation(value = "/listTopologyExcept", notes = "异常拓扑图数据")
    @RequestMapping(value = "/listTopologyExcept", method = {RequestMethod.GET})
    public ApiResult<List<TopologyExceptRespVO>> listTopologyExcept(@RequestParam(value = "ipAddr") String ipAddr) {
        return ApiResult.success(topologyService.listTopologyExcept(ipAddr));
    }

    @ApiOperation(value = "/view", notes = "交换机详情")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<TopologyRespVO> view(@RequestParam(value = "id") Integer id, @RequestParam(value = "ipAddr") String ipAddr) {
        TopologyRespVO respVO = topologyService.view(id, ipAddr);
        return ApiResult.success(respVO);
    }

    @ApiOperation(value = "/init", notes = "拓扑图树形图初始化")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    public ApiResult<List<TreeSelectTopo>> init() {

        return ApiResult.success(topologyService.init());
    }


    @ApiOperation(value = "/listNetworkSwitcherMac", notes = "拓扑图树形图")
    @RequestMapping(value = "/listNetworkSwitcherMac", method = {RequestMethod.GET})
    public ApiResult<PageResult<NetworkSwitcherMacRespVO>> listNetworkSwitcherMac(@RequestParam(value = "ipAddr") String ipAddr,
                                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageResult<NetworkSwitcherMacRespVO> result = topologyService.listNetworkSwitcherMac(ipAddr, pageNum, pageSize);
        return ApiResult.success(result);
    }

    /**
     * 查询交换机列表
     *
     * @return
     */
    @ApiOperation(value = "/switchList", notes = "核心交换机列表")
    @RequestMapping(value = "/switchList", method = {RequestMethod.GET})
    public ApiResult switchList() {
        return topologyService.switchList();
    }

    /**
     * 核心交换机拓扑图
     *
     * @param ip
     * @return
     */
    @ApiOperation(value = "/switchView", notes = "核心交换机拓扑图")
    @RequestMapping(value = "/switchView", method = {RequestMethod.GET})
    public ApiResult switchView(@RequestParam(value = "ip") String ip) {
        return topologyService.switchView(ip);
    }


    /**
     * 核心交换机拓扑图
     *
     * @return
     */
    @ApiOperation(value = "/switchTopo", notes = "核心交换机拓扑图")
    @RequestMapping(value = "/switchTopo", method = {RequestMethod.GET})
    public ApiResult switchTopo() {
        return topologyService.switchTopo();
    }

    /**
     * 更新排序 显示前几个
     * @return
     */
    @ApiOperation(value = "/updateSeq", notes = "更新排序")
    @RequestMapping(value = "/updateSeq", method = {RequestMethod.POST})
    public ApiResult updateSeq(@RequestBody TopoSeqReqVO vo) {
        topologyService.updateSeq(vo);
        return ApiResult.success();
    }

}
