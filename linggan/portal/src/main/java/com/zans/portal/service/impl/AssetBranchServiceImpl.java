package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.PagePlusResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.AssetBranchMapper;
import com.zans.portal.model.AssetBranch;
import com.zans.portal.model.AssetBranchAssetStatistics;
import com.zans.portal.model.AssetBranchStatistics;
import com.zans.portal.service.*;
import com.zans.portal.vo.asset.branch.req.*;
import com.zans.portal.vo.asset.branch.resp.AssetBranchJoinRespVO;
import com.zans.portal.vo.asset.branch.resp.ChoiceDeviceRespVO;
import com.zans.portal.vo.asset.branch.resp.ExcelAssetRunStatisticsVO;
import com.zans.portal.vo.asset.branch.resp.RunningListRespVO;
import com.zans.portal.vo.common.TreeSelect;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.zans.portal.config.GlobalConstants.*;

@Service
public class AssetBranchServiceImpl extends BaseServiceImpl<AssetBranch>  implements IAssetBranchService {
    @Autowired
    AssetBranchMapper assetBranchMapper;
    @Autowired
    IAssetBranchStatisticsService branchStatisticsService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IAssetBranchAssetService assetBranchAssetService;

    @Autowired
    IAssetBranchAssetStatisticsService assetBranchAssetStatisticsService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IDeviceTypeService deviceTypeService;


    @Resource
    public void setAssetBranchMapper(AssetBranchMapper assetBranchMapper) {
        super.setBaseMapper(assetBranchMapper);
        this.assetBranchMapper = assetBranchMapper;
    }

    @Override
    public Integer findByName(String name, Integer id) {
        return assetBranchMapper.findByName(name,id);
    }


    @Override
    public List<AssetBranchStatisticsVO> statisticsAssetBranchById(Integer assetBranchId,Date statisticsTime,Boolean saveStatisticsDetailFlag) {
        List<AssetBranchStatisticsVO> list = assetBranchMapper.statisticsAssetBranch(assetBranchId);
        if (null == list || list.size()==0){
            return null;
        }
       Integer branchStatisticsId = branchStatisticsService.getNextId();

        //总数
        int branchTotal = list.size();
        //停用设备数
        int disableNumber = 0;
        //在线设备数
        int onlineNumber = 0;
        //离线设备数
        int offlineNumber = 0;
        AssetBranchAssetStatistics assetBranchAssetStatistics = null;
        for (AssetBranchStatisticsVO vo : list) {
            if (saveStatisticsDetailFlag) {
                assetBranchAssetStatistics = new AssetBranchAssetStatistics();
                BeanUtils.copyProperties(vo,assetBranchAssetStatistics);
                assetBranchAssetStatistics.setAssetBranchStatisticsId(branchStatisticsId);
                assetBranchAssetStatistics.setStatisticsTime(statisticsTime);
                assetBranchAssetStatisticsService.saveSelective(assetBranchAssetStatistics);
            }

            if (0 == vo.getEnableStatus().intValue()){
                disableNumber++;
                continue;
            }
            if (1 == vo.getAlive().intValue()){
                onlineNumber++;
                continue;
            }
        }
        offlineNumber = branchTotal - disableNumber - onlineNumber;

        BigDecimal onlineBigDecimal = new BigDecimal(onlineNumber);
        BigDecimal offlineBigDecimal = new BigDecimal(offlineNumber);

        //启动状态下的设备总数 = 在线设备+离线设备
        BigDecimal totalActivateBigDecimal  = onlineBigDecimal.add(offlineBigDecimal);
        //在线率=在线设备/启动状态下的设备总数
        String onlineRate = 0.00 +"%";
        if(totalActivateBigDecimal.intValue() != 0){
            onlineRate = onlineBigDecimal.multiply(new BigDecimal("100")).divide(totalActivateBigDecimal,2, RoundingMode.HALF_UP) +"%";
        }

        AssetBranchStatistics assetBranchStatistics = new AssetBranchStatistics();
        assetBranchStatistics.setAssetBranchId(assetBranchId);
        assetBranchStatistics.setBranchTotal(branchTotal);
        assetBranchStatistics.setDisableNumber(disableNumber);
        assetBranchStatistics.setOnlineNumber(onlineNumber);
        assetBranchStatistics.setOfflineNumber(offlineNumber);
        if (saveStatisticsDetailFlag) {
            //如果写入运行统计详情 设置id  否则用自增的id 避免实时统计时候并发主键冲突
            assetBranchStatistics.setId(branchStatisticsId);
        }
        assetBranchStatistics.setStatisticsTime(statisticsTime);
        assetBranchStatistics.setOnlineRate(onlineRate);
        branchStatisticsService.saveSelective(assetBranchStatistics);

        return null;
    }

    @Override
    public PageResult<RunningListRespVO> syncStatistics(RunningListReqVO  reqVO) {
        List<Integer> ids = assetBranchMapper.getIds();
        Date statisticsTime = new Date();
        String statisticsTimeStr = DateHelper.formatDate(statisticsTime,"yyyy-MM-dd HH:mm:ss");
        reqVO.setStatisticsTime(statisticsTimeStr);

        //重新转换一次date类型 保证一致性，不然会出现有毫秒误差
        statisticsTime = DateHelper.parseDatetime(statisticsTimeStr);
        for (Integer id : ids) {
            this.statisticsAssetBranchById(id,statisticsTime,false);
        }

        Integer count = assetBranchMapper.countEndpointStatistics();
        if (count.intValue()>0){
            //  清理之前的实时查询数据AssetBranchStatistics
            assetBranchMapper.deleteHistoryStatistics(statisticsTimeStr);
        }


        return this.runningList(reqVO);
    }



    private String getYYMMDD() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean statisticsAssetBranchAll(String time) {
        List<Integer> ids = assetBranchMapper.getIds();
        if (null == ids || ids.size()==0) {
            return true;
        }
        Date statisticsTime = DateHelper.parseDatetime(getYYMMDD()+" "+time);
        for (Integer id : ids) {
            this.statisticsAssetBranchById(id,statisticsTime,true);
        }
        return true;
    }

    @Override
    public PageResult<ChoiceDeviceRespVO> choiceDevicePage(ChoiceDeviceReqVO reqVO) {
   //     List<String> passList = assetBranchMapper.getExistPassList(reqVO.getAssetBranchId());
   //     reqVO.setPassList(passList);
        List<String> existAssetIds = assetBranchMapper.getExistAssetIps(reqVO.getAssetBranchId());
        reqVO.setExistAssetIds(existAssetIds);

        int pageNum = reqVO.getPageNum();
        int pageSize = reqVO.getPageSize();
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();
        Page page = PageHelper.startPage(pageNum, pageSize);

//        List<ChoiceDeviceRespVO> list = assetBranchMapper.choiceDeviceList(reqVO);
        List<ChoiceDeviceRespVO> list = assetBranchMapper.choiceDeviceAssetList(reqVO);
        for (ChoiceDeviceRespVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setAliveByMap(aliveMap);
            if (vo.getDeviceType() != null){
                vo.setDeviceTypeName(deviceTypeMap.get(vo.getDeviceType()));
            }

        }

        return new PageResult<ChoiceDeviceRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public PageResult<RunningListRespVO> runningList(RunningListReqVO reqVO) {
        int pageNum = reqVO.getPageNum();
        int pageSize = reqVO.getPageSize();
        //不分页的数量 用来展示统计是条形图
        List<RunningListRespVO> listAll = assetBranchMapper.runningList(reqVO);

        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RunningListRespVO> list = assetBranchMapper.runningList(reqVO);
        Map<Object, String> deleteStatusMap = constantItemService.findItemsMapByDict(MODULE_DELETE);
        for (RunningListRespVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setDeleteByMap(deleteStatusMap);
        }

        return new PagePlusResult<RunningListRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum,listAll);
    }

    @Override
    public List<ExcelAssetRunStatisticsVO> runningListExport(RunningListReqVO reqVO) {
        return assetBranchMapper.runningListExport(reqVO);
    }
    @Override
    public List<String> timeList() {
        return assetBranchMapper.timeList();
    }


    @Override
    public PageResult<AssetBranchStatisticsVO> runningDetailListPage(RunningDetailListReqVO reqVO) {
        int pageNum = reqVO.getPageNum();
        int pageSize = reqVO.getPageSize();

        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_ENABLE_STATUS);
        Page page = PageHelper.startPage(pageNum, pageSize);
//        List<AssetBranchStatisticsVO> list = assetBranchMapper.runningDetailList(reqVO);
        List<AssetBranchStatisticsVO> list = assetBranchAssetStatisticsService.runningDetailList(reqVO);
        for (AssetBranchStatisticsVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setAliveByMap(aliveMap);
            vo.setEnableStatusNameByMap(enableStatusMap);
        }
        return new PageResult<AssetBranchStatisticsVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public List<ExcelAssetBranchStatisticsVO> runningDetailList(RunningDetailListReqVO reqVO) {
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_ENABLE_STATUS);
//        List<ExcelAssetBranchStatisticsVO> list = assetBranchMapper.runningDetailListForExcel(reqVO);
        List<ExcelAssetBranchStatisticsVO> list = assetBranchAssetStatisticsService.runningDetailListForExcel(reqVO);

        for (ExcelAssetBranchStatisticsVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setAliveByMap(aliveMap);
            vo.setEnableStatusNameByMap(enableStatusMap);
        }
        return list;
    }

    @Override
    public PageResult<AssetBranchStatisticsVO> assetBranchEndpointPage(AssetBranchReqVO reqVO) {
        int pageNum = reqVO.getPageNum();
        int pageSize = reqVO.getPageSize();

        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_ENABLE_STATUS);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();
        Integer assetBranchId = reqVO.getAssetBranchId();
        if(assetBranchId!=null){
            AssetBranch assetBranch = assetBranchMapper.findById(assetBranchId);
            //如果是第一级，直接置位null，查询所有
            if(assetBranch!=null && assetBranch.getParentId() == 0){
                reqVO.setAssetBranchId(null);
            }
        }

        Page page = PageHelper.startPage(pageNum, pageSize);
     //   List<AssetBranchStatisticsVO> list = assetBranchMapper.assetBranchEndpointList(reqVO);
        List<AssetBranchStatisticsVO> list = assetBranchAssetService.assetBranchList(reqVO);
        for (AssetBranchStatisticsVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setAliveByMap(aliveMap);
            vo.setEnableStatusNameByMap(enableStatusMap);
//            if (vo.getDeviceType() != null){
//                vo.setDeviceTypeName(deviceTypeMap.get(vo.getDeviceType()));
//            }

        }
        return new PageResult<AssetBranchStatisticsVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public List<ExcelAssetBranchStatisticsVO> assetBranchEndpoint(AssetBranchReqVO reqVO) {
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_ENABLE_STATUS);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();
        List<ExcelAssetBranchStatisticsVO> list = assetBranchAssetService.assetBranchListForExcel(reqVO);
        for (ExcelAssetBranchStatisticsVO vo : list) {
            if (null == vo) {
                continue;
            }
            vo.setAliveByMap(aliveMap);
            vo.setEnableStatusNameByMap(enableStatusMap);
            if (vo.getDeviceType() != null){
                vo.setDeviceTypeName(deviceTypeMap.get(vo.getDeviceType()));
            }
        }
        return list;
    }

    @Override
    public List<SelectVO> findToSelect(String name) {
        return assetBranchMapper.findToSelect(name);
    }

    @Override
    public List<TreeSelect> findToTreeSelect() {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        List<AssetBranch> branchList = assetBranchMapper.findToTreeSelect(null);
        Map<Integer, TreeSelect> treeMap = new HashMap<>(branchList.size());

        for (AssetBranch assetBranch : branchList) {
            TreeSelect node = TreeSelect.builder()
                    .id(assetBranch.getId()).label(assetBranch.getName()).seq(assetBranch.getSeq())
                    .level(assetBranch.getLevel()).isShow(false).build();
            if (node != null && node.getChildren() == null) {
                node.setChildren(new LinkedList<>());
            }
            treeMap.put(assetBranch.getId(), node);
            Integer parentId = assetBranch.getParentId();
            if (parentId == 0) {
                treeSelectList.add(node);
            }else {
                TreeSelect parent = treeMap.get(parentId);
                if (parent == null) {
                    treeSelectList.add(node);
                }  else {
                    parent.addChild(node);
                }
            }
        }

        return treeSelectList;
    }

    @Override
    public AssetBranchJoinRespVO getDetailById(Integer id) {
        AssetBranchJoinRespVO joinVO = new AssetBranchJoinRespVO();
        AssetBranch self = this.getById(id);
        joinVO.setSlef(self);
        Integer seq = self.getSeq();
        Integer parentId = self.getParentId();
        List<AssetBranch> list = assetBranchMapper.getAll(parentId);
        if (null == list || list.size() == 0) {
            joinVO.setNext(null);
            joinVO.setPrev(null);
            return joinVO;
        }

        AssetBranch next = null;
        AssetBranch prev = null;
        for (int i = 0; i < list.size(); i++) {
            AssetBranch assetBranch = list.get(i);
            //将id的判断改成排序值判断
//            if (id.intValue() == assetBranch.getId().intValue()){
            if (seq.intValue() == assetBranch.getSeq().intValue() && parentId.equals(assetBranch.getParentId())){
                if (i-1 >= 0){
                    prev = list.get(i-1);
                }
                if (list.size() > i+1){
                    next = list.get(i+1);
                }
                break;
            }
        }
        joinVO.setNext(next);
        joinVO.setPrev(prev);

        return joinVO;
    }

    @Override
    public Integer getNextSeq(Integer parentId) {
        return assetBranchMapper.getNextSeq(parentId);
    }

    @Override
    public List<AssetBranch> getAllList() {
        return assetBranchMapper.getAll(null);
    }

    @Override
    public List<AssetBranch> getAssetBranchByAreaId(Integer baselineAreaId) {
        return assetBranchMapper.getAssetBranchByAreaId(baselineAreaId);
    }
}
