package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.NetworkTopologyMapper;
import com.zans.portal.model.NetworkTopology;
import com.zans.portal.model.SysConstant;
import com.zans.portal.model.SysSwitcher;
import com.zans.portal.service.*;
import com.zans.portal.vo.common.TreeSelectTopo;
import com.zans.portal.vo.network.TopoSeqReqVO;
import com.zans.portal.vo.network.topology.resp.NetworkSwitcherMacRespVO;
import com.zans.portal.vo.network.topology.resp.TopologyExceptRespVO;
import com.zans.portal.vo.network.topology.resp.TopologyRespVO;
import com.zans.portal.vo.switcher.SwitchBaseInfoVO;
import com.zans.portal.vo.switcher.SwitchTopologyRepVO;
import com.zans.portal.vo.switcher.SwitchTopologyVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.portal.config.GlobalConstants.REGION_LEVEL_TWO;
import static com.zans.portal.constants.PortalConstants.*;


@Service
public class NetworkTopologyServiceImpl extends BaseServiceImpl<NetworkTopology> implements INetworkTopologyService {
    NetworkTopologyMapper networkTopologyMapper;
    @Autowired
    ISwitcherService switcherService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IRegionService regionService;
    @Autowired
    ISysConstantService constantService;

    @Value("${topo.mock:0}")
    private String mockTopoDataFlag;

    @Resource
    public void setNetworkTopologyMapper(NetworkTopologyMapper networkTopologyMapper) {
        super.setBaseMapper(networkTopologyMapper);
        this.networkTopologyMapper = networkTopologyMapper;
    }

    @Override
    public List<TreeSelectTopo> treeList(String ipAddr) {
        List<SysSwitcher> rootSwitcherList = switcherService.getListCondition(new HashMap<String, Object>(4) {{
            put("arpEnable", 1);
        }});
        List<String> rootIps = rootSwitcherList.stream().map(sysSwitcher -> sysSwitcher.getSwHost()).collect(Collectors.toList());


        List<NetworkTopology> secondList = networkTopologyMapper.getListCondition(new HashMap<String, Object>(4) {{
            put("ipAddr", ipAddr);
            put("rootIps", rootIps);
        }});

        //节点第一级 根节点id为0
        List<TreeSelectTopo> treeList = new ArrayList<>();
        TreeSelectTopo rootNode = TreeSelectTopo.builder().name(ipAddr).children(new LinkedList<>()).id(0).build();
        treeList.add(rootNode);

        List<String> sourceIps = secondList.stream()
                //  .filter(t ->!rootIps.contains(t.getDestIp()))
                .map(topology -> topology.getDestIp()).collect(Collectors.toList());

        List<NetworkTopology> thirdAllList = networkTopologyMapper.getListCondition(new HashMap<String, Object>(4) {{
            put("sourceIps", sourceIps);
            put("rootIps", rootIps);
        }});
        Map<String, List<NetworkTopology>> thirdAllListMap = thirdAllList.stream().collect(Collectors.groupingBy(topology -> topology.getSourceIp()));
        for (NetworkTopology secondTopology : secondList) {

            //节点第二级
            TreeSelectTopo secondNode = TreeSelectTopo.builder().name(secondTopology.getDestIp()).children(new LinkedList<>()).id(secondTopology.getId()).build();
            rootNode.getChildren().add(secondNode);

            List<NetworkTopology> networkThird = thirdAllListMap.get(secondTopology.getDestIp());
            if (CollectionUtils.isNotEmpty(networkThird)) {
                for (NetworkTopology topology : networkThird) {
                    //节点第三级
                    TreeSelectTopo thirdNode = TreeSelectTopo.builder().name(topology.getDestIp()).children(new LinkedList<>()).id(topology.getId()).build();
                    secondNode.getChildren().add(thirdNode);
                }
            }

        }

        return treeList;
    }

    @Override
    public TopologyRespVO view(Integer id, String ipAddr) {
        if (id.intValue() != 0) {
            //不为0查拓扑图表 查出连接线
            TopologyRespVO respVO = networkTopologyMapper.findViewByIdAndIp(id, ipAddr);
            return respVO;
        }
        TopologyRespVO respVO2 = networkTopologyMapper.findViewByIp(ipAddr);

        respVO2.setUpTime(formatUpTime(respVO2.getUpTime()));
        respVO2.setUseInterface(formatUseInterface(respVO2.getScanMacAll(), respVO2.getScanInterfaceCount()));

        return respVO2;
    }

    private String resetInterfaceName(String interfaceName) {
        if (StringUtils.isBlank(interfaceName)) {
            return "";
        }
        if (interfaceName.startsWith("XGigabitEthernet")) {
            return interfaceName.replace("XGigabitEthernet", "XGe");
        }
        if (interfaceName.startsWith("Ten-GigabitEthernet")) {
            return interfaceName.replace("Ten-GigabitEthernet", "XGe");
        }
        if (interfaceName.startsWith("10GE")) {
            return interfaceName.replace("10GE", "XGe");
        }

        if (interfaceName.startsWith("gige")) {
            return interfaceName.replace("gige", "Ge");
        }
        if (interfaceName.startsWith("GigabitEthernet")) {
            return interfaceName.replace("GigabitEthernet", "Ge");
        }

        return interfaceName;
    }

    private String formatUpTime(String upTime) {
        if (StringUtils.isNotBlank(upTime)) {
            return upTime.substring(upTime.lastIndexOf(")") + 1).replace("days", "天");
        }
        return "";
    }

    private String formatUseInterface(Integer scanMacAll, Integer scanInterfaceCount) {
        if (scanMacAll != null && scanInterfaceCount != null) {
            return scanMacAll + "/" + scanInterfaceCount;
        } else if (scanMacAll != null) {
            return scanMacAll + "/0";
        } else if (scanInterfaceCount != null) {
            return "0/" + scanInterfaceCount;
        }
        return "";
    }

    @Override
    public List<TreeSelectTopo> init() {

        //节点第一级 根节点id为0
        List<TreeSelectTopo> areaTreeList = new ArrayList<>();
        TreeSelectTopo firstRoot = TreeSelectTopo.builder()
                .id(0)
                .type(0)
                .name("武汉市交管局网络")
                .is_outline(true)
                .children(new LinkedList<>())
                .build();
        areaTreeList.add(firstRoot);


        List<SelectVO> regionList = areaService.findRegionToSelect(REGION_LEVEL_TWO);

        for (SelectVO vo : regionList) {
            TreeSelectTopo secondRoot = TreeSelectTopo.builder()
                    .id(vo.getItemKey())
                    .type(1)
                    .name(vo.getItemValue() + "片区")
                    .is_outline(true)
                    .children(new LinkedList<>())
                    .build();
            firstRoot.getChildren().add(secondRoot);



        }

        return areaTreeList;
    }



    @Override
    public List<TreeSelectTopo> rootTreeList(Integer topoDataType,String ipAddr) {
        SysConstant constant = constantService.findConstantByKey(TOPO_DATA_TPYE);
        if (constant == null){
            throw new RuntimeException("系统变量topy:data_type未配置");
        }
        if (topoDataType==null || topoDataType==0){
            topoDataType = Integer.valueOf(constant.getConstantValue()) ;
        }
        String coreIp="";
        if ("1".equals(mockTopoDataFlag)){
            coreIp = this.mockData(topoDataType);
        }

        HashMap<String,Object> map =  new HashMap<>(4);
        map.put("topoType", TOPO_TPYE_CORE);
        map.put("swHost", coreIp);
        List<SysSwitcher> rootSwitcherList = switcherService.getListCondition(map);
        if (rootSwitcherList == null || rootSwitcherList.size()==0 || rootSwitcherList.size() >2){
            throw new RuntimeException("核心交换机不存在或数量大于2!");
        }
        List<String> rootIps = rootSwitcherList.stream().map(sysSwitcher -> sysSwitcher.getSwHost()).collect(Collectors.toList());

        //节点第一级 根节点id为0
        List<TreeSelectTopo> treeList = new ArrayList<>();
        String rootIp1 = rootIps.get(0);
        if (StringHelper.isNotBlank(ipAddr)){
            rootIp1 = ipAddr;
        }

        String rootIp2 = "";
        if(rootIps.size()==2){
            rootIp2 = rootIps.get(1);
        }

        TopologyRespVO rootView = this.view(0, rootIp1);

        TreeSelectTopo rootNode = TreeSelectTopo.builder().name(rootIp1).name2(rootIp2).children(new LinkedList<>()).data(rootView).build();
        treeList.add(rootNode);
        HashMap<String, Object> map2 = new HashMap<>(8);
        if (StringHelper.isNotBlank(ipAddr)){
            //用前台传入的 ipAddr
            map2.put("ipAddr", ipAddr);
        } else {
            map2.put("sourceIps", rootIps);
        }

        map2.put("rootIps", rootIps);
//        map2.put("visible", 1);
        List<NetworkTopology> secondList = networkTopologyMapper.getListCondition(map2);
        int secondListSize = secondList.size();

        //移除不显示的记录
        Iterator<NetworkTopology> iterator = secondList.iterator();
        while (iterator.hasNext()){
            NetworkTopology next = iterator.next();
            if (next.getVisible()==2){
                iterator.remove();
            }
        }


        Integer treeTopoDataType =2;
        Boolean secondTopoFlag = Boolean.TRUE;

        for (NetworkTopology secondTopology : secondList) {
            if (StringUtils.isBlank(secondTopology.getDestIp())) {
                continue;
            }
            //节点第二级
            TopologyRespVO secondView = this.view(0, secondTopology.getDestIp());
            secondView.setDestInterface(resetInterfaceName(secondTopology.getDestInterface()));
            secondView.setSourceInterface(resetInterfaceName(secondTopology.getSourceInterface()));
            secondView.setDestIp(secondTopology.getDestIp());
            secondView.setSourceIp(secondTopology.getSourceIp());

            TreeSelectTopo secondNode = TreeSelectTopo.builder().name(secondTopology.getDestIp()).children(new LinkedList<>())
                    .data(secondView).id(secondTopology.getId()).build();
            rootNode.getChildren().add(secondNode);

            HashMap<String, Object> map3 = new HashMap<>(8);
            map3.put("ipAddr", secondTopology.getDestIp());
            map3.put("rootIps", rootIps);
            map3.put("visible", 1);
            List<NetworkTopology> thirdList = networkTopologyMapper.getListCondition(map3);

            for (NetworkTopology thirdTopology : thirdList) {
                secondTopoFlag = Boolean.FALSE;
                //节点第三级
                TopologyRespVO thirdView = this.view(0, thirdTopology.getDestIp());
                thirdView.setDestInterface(resetInterfaceName(thirdTopology.getDestInterface()));
                thirdView.setSourceInterface(resetInterfaceName(thirdTopology.getSourceInterface()));
                thirdView.setDestIp(thirdTopology.getDestIp());
                thirdView.setSourceIp(thirdTopology.getSourceIp());

                TreeSelectTopo thirdNode = TreeSelectTopo.builder().name(thirdTopology.getDestIp()).children(new LinkedList<>())
                        .data(thirdView).id(thirdTopology.getId()).build();
                secondNode.getChildren().add(thirdNode);
            }
        }
        treeTopoDataType = getTreeTopoDataType(secondListSize, treeTopoDataType, secondTopoFlag);

        //设置前端展示模板
        rootNode.setTreeTopoDataType(treeTopoDataType);
        return treeList;
    }

    private Integer getTreeTopoDataType(int secondSize, Integer treeTopoDataType, Boolean secondTopoFlag) {

        if (secondTopoFlag){
            if (secondSize==1){
                return TOPO_DATA_TPYE_1;
            }
            if (secondSize==2){
                return TOPO_DATA_TPYE_2;
            }
            if (secondSize<=12){
                return TOPO_DATA_TPYE_3;
            }
            if (secondSize>12){
                return TOPO_DATA_TPYE_4;
            }
        }else {
            if (secondSize<=3){
                return TOPO_DATA_TPYE_5;
            }
            if (secondSize>=4 && secondSize<=8){
                return TOPO_DATA_TPYE_6;
            }
            if (secondSize>8){
                return TOPO_DATA_TPYE_7;
            }
        }
        return treeTopoDataType;
    }

    private String mockData(Integer topoDataType) {
        if (topoDataType==1){
            return "192.173";
        }
        if (topoDataType==2){
            return "192.174";
        }
        if (topoDataType==3){
            return "192.168";
        }
        if (topoDataType==4){
            return "192.169";
        }
        if (topoDataType==5){
            return "192.170";
        }
        if (topoDataType==6){
            return "192.171";
        }
        if (topoDataType==7){
            return "192.172";
        }

        return "192.168";
    }
/*    @Override
    public List<TreeSelectTopo> rootTreeList() {
        List<SysSwitcher> rootSwitcherList = switcherService.getListCondition(new HashMap<String, Object>(4) {{
            put("arpEnable", 1);
        }});
        List<String> rootIps = rootSwitcherList.stream().map(sysSwitcher -> sysSwitcher.getSwHost()).collect(Collectors.toList());

        List<NetworkTopology> sourceTopologyList = new ArrayList<>();
        if (StringUtils.isNotBlank(sourceIp)) {
            rootIps.add(sourceIp);
            sourceTopologyList = networkTopologyMapper.getListCondition(new HashMap<String, Object>(4) {{
                put("ipAddr", sourceIp);
                put("destIp", ipAddr);
            }});

        }
        String[] split = ipAddr.split("\\.");

        HashMap<String, Object> map = new HashMap<>(8);
        map.put("ipAddr", ipAddr);
        map.put("rootIps", rootIps);
        //# 过滤同一台交换机多个Ip的情况，用接口过滤
        map.put("sourceTopologyList", sourceTopologyList);
        map.put("ipAddrForAB", split[0] + "." + split[1]);

        List<NetworkTopology> secondList = networkTopologyMapper.getListCondition(map);

        //节点第一级 根节点id为0
        List<TreeSelectTopo> treeList = new ArrayList<>();
        TopologyRespVO rootView = this.view(0, ipAddr);
        rootView.setSourceIp(sourceIp);

        TreeSelectTopo rootNode = TreeSelectTopo.builder().name(ipAddr).children(new LinkedList<>()).id(0).data(rootView).build();
        treeList.add(rootNode);

        for (NetworkTopology secondTopology : secondList) {
            if (StringUtils.isBlank(secondTopology.getDestIp())) {
                continue;
            }
            //节点第二级
            TopologyRespVO secondView = this.view(0, secondTopology.getDestIp());
            secondView.setDestInterface(resetInterfaceName(secondTopology.getDestInterface()));
            secondView.setSourceInterface(resetInterfaceName(secondTopology.getSourceInterface()));
            secondView.setDestIp(secondTopology.getDestIp());
            secondView.setSourceIp(secondTopology.getSourceIp());

            TreeSelectTopo secondNode = TreeSelectTopo.builder().name(secondTopology.getDestIp()).children(new LinkedList<>())
                    .data(secondView).id(secondTopology.getId()).build();
            rootNode.getChildren().add(secondNode);
        }

        return treeList;
    }*/

    @Override
    public PageResult<NetworkSwitcherMacRespVO> listNetworkSwitcherMac(String ipAddr, Integer pageNum, Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<NetworkSwitcherMacRespVO> list = networkTopologyMapper.listNetworkSwitcherMac(ipAddr);
        if (CollectionUtils.isNotEmpty(list)) {
            for (NetworkSwitcherMacRespVO vo : list) {
                vo.setInterfaceDetail(resetInterfaceName(vo.getInterfaceDetail()));
                vo.setMac_addr(vo.getMac());
                vo.setMacAddr(vo.getMac());
            }
        }

        return new PageResult<NetworkSwitcherMacRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public List<TopologyExceptRespVO> listTopologyExcept(String ipAddr) {
        List<TopologyExceptRespVO> list = networkTopologyMapper.listTopologyExcept(ipAddr);
        for (TopologyExceptRespVO vo : list) {
            vo.setDestInterface(this.resetInterfaceName(vo.getDestInterface()));
            vo.setSourceInterface(this.resetInterfaceName(vo.getSourceInterface()));
        }

        return list;
    }

    @Override
    public ApiResult switchList() {
        List<SwitchBaseInfoVO> sysSwitcherList = networkTopologyMapper.switchList();
        return ApiResult.success(sysSwitcherList);
    }


    @Override
    public ApiResult switchView(String ip) {
        SwitchTopologyRepVO firstSwitchTopologyRepVO = new SwitchTopologyRepVO();
        List<SwitchTopologyRepVO> firstSwitchTopologyRepVOList = new ArrayList<>();
        firstSwitchTopologyRepVO.setIp(ip);
        //查询当前核心交换机下的子节点
        List<String> firstIpList = networkTopologyMapper.getNextLevelIp(ip);
        for (String secondIp : firstIpList) {
            List<SwitchTopologyRepVO> secondSwitchTopologyRepVOList = new ArrayList<>();
            SwitchTopologyRepVO secondSwitchTopologyRepVO = new SwitchTopologyRepVO();
            secondSwitchTopologyRepVO.setIp(secondIp);
            //根据第二层 找第三层的ip
            List<String> secondIpList = networkTopologyMapper.getNextLevelIp(secondIp);
            for (String thirdIp : secondIpList) {
                List<SwitchTopologyRepVO> thirdSwitchTopologyRepVOList = new ArrayList<>();
                SwitchTopologyRepVO thirdSwitchTopologyRepVO = new SwitchTopologyRepVO();
                thirdSwitchTopologyRepVO.setIp(thirdIp);
                secondSwitchTopologyRepVOList.add(thirdSwitchTopologyRepVO);
                List<String> thirdIpList = networkTopologyMapper.getNextLevelIp(thirdIp);
                for (String fourthIp : thirdIpList) {
                    SwitchTopologyRepVO fourthSwitchTopologyRepVO = new SwitchTopologyRepVO();
                    fourthSwitchTopologyRepVO.setIp(fourthIp);
                    thirdSwitchTopologyRepVOList.add(fourthSwitchTopologyRepVO);
                }
                thirdSwitchTopologyRepVO.setChildren(thirdSwitchTopologyRepVOList);
            }
            secondSwitchTopologyRepVO.setChildren(secondSwitchTopologyRepVOList);
            firstSwitchTopologyRepVOList.add(secondSwitchTopologyRepVO);
        }
        firstSwitchTopologyRepVO.setChildren(firstSwitchTopologyRepVOList);
        return ApiResult.success(firstSwitchTopologyRepVO);
    }

    @Override
    public ApiResult switchView1(String ip) {
        SwitchTopologyRepVO firstSwitchTopologyRepVO = new SwitchTopologyRepVO();
        firstSwitchTopologyRepVO.setIp(ip);
        setNext(firstSwitchTopologyRepVO);
        return ApiResult.success(firstSwitchTopologyRepVO);
    }

    /**
     * 狠心交换机互联拓扑图
     * @return
     */
    @Override
    public ApiResult switchTopo() {
        List<SwitchBaseInfoVO> sysSwitcherList = networkTopologyMapper.switchList();
        //查询所有的互联的核心交换机
        List<SwitchTopologyVO> oldSwitchTopologyVOList = networkTopologyMapper.switchRelation();
        //对互连关系进行去重
        List<SwitchTopologyVO> newSwitchTopologyVOList = new ArrayList<>();
        for(SwitchTopologyVO oldSwitchTopology: oldSwitchTopologyVOList){
            Boolean flag=false;
            if(null!=newSwitchTopologyVOList&&newSwitchTopologyVOList.size()>0){
                for(SwitchTopologyVO newSwitchTopology:newSwitchTopologyVOList){
                    if((newSwitchTopology.getFrom().equals(oldSwitchTopology.getFrom())&&newSwitchTopology.getTo().equals(oldSwitchTopology.getTo()))||
                            (newSwitchTopology.getFrom().equals(oldSwitchTopology.getTo())&&newSwitchTopology.getTo().equals(oldSwitchTopology.getFrom()))
                    ){
                        //说明此数据已存在 不做新增
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    newSwitchTopologyVOList.add(oldSwitchTopology);
                }
            }else{
                newSwitchTopologyVOList.add(oldSwitchTopology);
            }
        }
        Map<String, Object> map =new HashMap<>();
        map.put("sysSwitcherList",sysSwitcherList);
        map.put("newSwitchTopologyVOList",newSwitchTopologyVOList);
        return ApiResult.success(map);
    }


    public void setNext(SwitchTopologyRepVO firstSwitchTopologyRepVO) {
        List<String> firstIpList = networkTopologyMapper.getNextLevelIp(firstSwitchTopologyRepVO.getIp());
        List<SwitchTopologyRepVO> switchTopologyRepVOList = new ArrayList<>();
        SwitchTopologyRepVO switchTopologyRepVO = new SwitchTopologyRepVO();
        if(null!=firstIpList&&firstIpList.size()>0){
            for (String ip : firstIpList) {
                switchTopologyRepVO.setIp(ip);
                switchTopologyRepVOList.add(switchTopologyRepVO);
                List<String> nextIpList = networkTopologyMapper.getNextLevelIp(ip);
                if (null != nextIpList && nextIpList.size() > 0) {
                   setNext(switchTopologyRepVO);
                }
            }
            switchTopologyRepVO.setChildren(switchTopologyRepVOList);
        }


    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public ApiResult updateSeq(TopoSeqReqVO vo) {
        String sourceIp = vo.getSourceIp();
        SysSwitcher switcher = switcherService.findBySwHost(sourceIp, null);
        String topoType = switcher.getTopoType();
        //https://fgr44sks34.feishu.cn/docs/doccn5vtpIqBMGFmQivui19RVxb#
        if (vo.getTopoDataType().intValue()!=4 || vo.getTopoDataType().intValue()!=7){
            ApiResult.error("非法类型!");
        }
        if (vo.getTopoDataType().intValue()==4 && vo.getDestIps().size()!=12){
            ApiResult.error("必须选中12台接入交换机!");
        }

        if (vo.getTopoDataType().intValue()==7 ){
//            显示核心+8台汇聚+每台汇聚下3台接入
            if (TOPO_TPYE_CORE.equals(topoType) && vo.getDestIps().size()!=8){
                ApiResult.error("必须选中8台汇聚交换机!");
            }
            if (TOPO_TPYE_DIST.equals(topoType) && vo.getDestIps().size()!=3){
                ApiResult.error("必须选中3台接入交换机!");
            }
        }

        TopoSeqReqVO old = new TopoSeqReqVO();
        old.setSourceIp(sourceIp);
        // 1,显示；2，隐藏    设置全部隐藏
        old.setVisible(2);
        networkTopologyMapper.updateSeq(old);
        // 设置显示当前传入都
        vo.setVisible(1);
        networkTopologyMapper.updateSeq(vo);
        return ApiResult.success();
    }

    @Override
    public List<NetworkTopology> findListBySourceIp(String sourceIp,Integer topoDataType) {

        SysConstant constant = constantService.findConstantByKey(TOPO_DATA_TPYE);
        if (constant == null){
            throw new RuntimeException("系统变量topy:data_type未配置");
        }
        String coreIp="";
        if ("1".equals(mockTopoDataFlag)){
            coreIp = this.mockData(topoDataType);
        }


        HashMap<String,Object> map =  new HashMap<>(4);
        map.put("topoType", TOPO_TPYE_CORE);
        map.put("swHost", coreIp);
        List<SysSwitcher> rootSwitcherList = switcherService.getListCondition(map);
        if (rootSwitcherList == null || rootSwitcherList.size()==0 || rootSwitcherList.size() >2){
            throw new RuntimeException("核心交换机不存在或数量大于2!");
        }
        List<String> rootIps = rootSwitcherList.stream().map(sysSwitcher -> sysSwitcher.getSwHost()).collect(Collectors.toList());
        HashMap<String, Object> map2 = new HashMap<>(8);
        map2.put("ipAddr", sourceIp);
        map2.put("rootIps", rootIps);
        List<NetworkTopology> list = networkTopologyMapper.getListCondition(map2);

        return list;

    }
}
