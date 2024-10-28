package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.exception.RollbackException;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.SysSwitcherBranchMapper;
import com.zans.portal.model.Asset;
import com.zans.portal.model.SysBrand;
import com.zans.portal.model.SysSwitcher;
import com.zans.portal.model.SysSwitcherBranch;
import com.zans.portal.service.*;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.vo.asset.branch.req.AssetBranchDisposeReqVO;
import com.zans.portal.vo.asset.req.AssetAddReqVO;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.switcher.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_ERROR;
import static com.zans.portal.config.GlobalConstants.REGION_LEVEL_TWO;

@Service
@Slf4j
public class SwitcherBrunchServiceImpl extends BaseServiceImpl<SysSwitcherBranch> implements ISwitcherBrunchService {

    @Value("${api.upload.folder}")
    String uploadFolder;
    @Value("${api.export.folder}")
    String exportFolder;
    SysSwitcherBranchMapper switcherBranchMapper;
    @Autowired
    IAreaService areaService;
    @Autowired
    IRegionService regionService;
    @Autowired
    IFileService fileService;
    @Autowired
    ISwitcherService switcherService;

    @Autowired
    IAssetService assetService;
    @Autowired
    ISysBrandService sysBrandService;
    @Autowired
    RestTemplateHelper restTemplateHelper;


    //    {"status":0,"result":[{"x":113.82272467469467,"y":23.041698608356179}]}
    private String[] getLl(String lon, String lat) {
        String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + lon + "," + lat + "&from=1&to=5&ak=jk3iC1WjTfqmTOfk0BjQRMT4T3WLaf1Q";
        String apiResult = restTemplateHelper.getForStr(url);
        System.out.println("===========================================" + apiResult);
        JSONObject o = (JSONObject) JSON.parse(apiResult);
        if (o.getInteger("status") == 0) {
            JSONObject xy = (JSONObject) o.getJSONArray("result").get(0);
            return new String[]{xy.getString("y"), xy.getString("x")};
        }
        return new String[2];
    }

    @Resource
    public void setSysSwitcherBranchMapper(SysSwitcherBranchMapper switcherBranchMapper) {
        super.setBaseMapper(switcherBranchMapper);
        this.switcherBranchMapper = switcherBranchMapper;
    }

    @Override
    public PageResult<SwitchBranchResVO> getSwitchPage(SwitchBranchSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<SwitchBranchResVO> list = switcherBranchMapper.findSwitchList(reqVO);
        return new PageResult<SwitchBranchResVO>(page.getTotal(), list, reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public SysSwitcherBranch findBySwHost(String ipAddr, Integer id) {
        return switcherBranchMapper.findBySwHost(ipAddr, id);
    }


    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public ApiResult batchAddSwitcher(String filePath, String fileName, UserSession userSession) {
        File file = this.getRemoteFile(filePath);
        if (file == null) {
            return null;
        }
        String absoluteNewFilePath = this.uploadFolder + filePath;
        log.info("file#{}", file.getAbsolutePath());
        ExcelEntity linkResult = null;
        try {
            linkResult = ExcelHelper.checkFile(fileName, file, absoluteNewFilePath, getAssetReader());
        } catch (Exception ex) {
            log.error("读取交换机上传文件失败#" + absoluteNewFilePath, ex);
            return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
        }
        if (!linkResult.getValid()) {
            log.error("文件校验失败#" + absoluteNewFilePath);
            return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
        }
        if (linkResult == null) {
            return ApiResult.error("未填写任何交换机!");
        }
        List<ExcelSwitchBranchVO> list = linkResult.getEntity().convertToRawTable(ExcelSwitchBranchVO.class);
        if (list == null || list.size() == 0) {
            return ApiResult.error("未填写任何交换机!!");
        }
        log.info("link.size#{}", list.size());
        SysSwitcherBranch switcherBranch = null;
        List<SelectVO> regionList = areaService.findRegionToSelect(REGION_LEVEL_TWO);
        List<SysBrand> brandList = sysBrandService.getAll();
        int successCount = 0;
        int errorCount = 0;

        AssetBranchDisposeReqVO enableStatus = new AssetBranchDisposeReqVO();
        AssetBranchDisposeReqVO disableStatus = new AssetBranchDisposeReqVO();
        StringBuffer enableSb = new StringBuffer();
        StringBuffer disableSb = new StringBuffer();
        int disCount = 0;
        int enCount = 0;
        disableStatus.setEnableStatus(0);
        disableStatus.setUpdateId(userSession.getUserId());
        disableStatus.setRemark("汇聚点列表导入更新");
        enableStatus.setEnableStatus(1);
        enableStatus.setUpdateId(userSession.getUserId());
        enableStatus.setRemark("汇聚点列表导入更新");
        List<ExcelSwitchBranchVO> errorList = new ArrayList<>();

        for (ExcelSwitchBranchVO dv : list) {
            if ("高德".equals(dv.getMapType())) {
//                String[] xy = getLl(dv.getLon().toString(),dv.getLat().toString());
                errorCount++;
                dv.setErrMsg("请使用百度经纬度");
//                dv.setLat(xy[0]);
//                dv.setLon(xy[1]);
                errorList.add(dv);
                continue;
            }

            List<SelectVO> curRegion = regionList.stream().filter(it -> it.getItemValue().equals(dv.getRegionName())).collect(Collectors.toList());
            if (curRegion == null || curRegion.size() == 0) {
                log.info("片区不存在=={}", dv.getRegionName());
                errorCount++;
                dv.setErrMsg("大区[" + dv.getRegionName() + "]不存在");
                errorList.add(dv);
                continue;
            }
            if (!StringHelper.isValidIp(dv.getIpAddr())) {
                log.info("ip地址错误=={}", dv.getIpAddr());
                errorCount++;
                dv.setErrMsg("ip地址[" + dv.getIpAddr() + "]错误");
                errorList.add(dv);
                continue;
            }
            List<SysBrand> curBrand = brandList.stream().filter(it -> it.getBrandName().equals(dv.getBrand())).collect(Collectors.toList());
            if (curBrand == null || curBrand.size() == 0) {
                log.info("品牌错误=={}", dv.getBrand());
                errorCount++;
                dv.setErrMsg("品牌[" + dv.getBrand() + "]错误");
                errorList.add(dv);
                continue;
            }
            if (!StringHelper.isNumber(dv.getLat())) {//纬度
                log.info("纬度错误=={}", dv.getLat());
                errorCount++;
                dv.setErrMsg("纬度[" + dv.getLat() + "]必须是数字");
                errorList.add(dv);
                continue;
            }
            if (!StringHelper.isNumber(dv.getLon())) {//经度
                log.info("经度错误=={}", dv.getLon());
                errorCount++;
                dv.setErrMsg("经度[" + dv.getLon() + "]必须是数字");
                errorList.add(dv);
                continue;
            }

            switcherBranch = switcherBranchMapper.findBySwHost(dv.getIpAddr(), null);
            boolean op_updated = switcherBranch != null;
            if (!op_updated) {
                switcherBranch = new SysSwitcherBranch();
            }
            switcherBranch.setRegion(Integer.parseInt(curRegion.get(0).getItemKey().toString()));
            switcherBranch.setPointName(dv.getPointName());
            switcherBranch.setIpAddr(dv.getIpAddr());
            //0core 核心交换机; 1convergence 汇聚交换机; 2access',接入交换机
            if (dv.getSwTypeName().contains("核心")) {
                switcherBranch.setSwType(0);
            } else if (dv.getSwTypeName().contains("汇聚")) {
                switcherBranch.setSwType(1);
            } else {
                switcherBranch.setSwType(2);
            }


            if ("正常使用".equals(dv.getStatus().trim()) || "启用".equals(dv.getStatus().trim())) {
                enCount++;
                enableSb.append(dv.getIpAddr()).append(",");
            }
            if ("维护中".equals(dv.getStatus().trim()) || "停用".equals(dv.getStatus().trim())) {
                disCount++;
                disableSb.append(dv.getIpAddr()).append(",");
            }

            switcherBranch.setStatus(("停用".equals(dv.getStatus()) || "维护中".equals(dv.getStatus())) ? 1 : 0);//'0启用 1 停用,
            switcherBranch.setBrand(curBrand.get(0).getBrandId());
            switcherBranch.setModel(dv.getModel());
            switcherBranch.setAcceptance("已验收".equals(dv.getAcceptStatus()) ? 1 : 0);


            if (op_updated) {
                // modify by beiso[2020/12/31]: 增加一项判断，对于交换机账号、密码、团体字，如果数据库里面值为空，才从excel数据中更新；
                String swAccount = StringUtils.defaultIfEmpty(switcherBranch.getSwAccount(), dv.getSwAccount());
                switcherBranch.setSwAccount(swAccount);
                String swPassword = StringUtils.defaultIfEmpty(switcherBranch.getSwPassword(), dv.getSwPassword());
                switcherBranch.setSwPassword(swPassword);
                String community = StringUtils.defaultIfEmpty(switcherBranch.getCommunity(), dv.getCommunity());
                switcherBranch.setCommunity(community);
            } else {
                switcherBranch.setSwAccount(dv.getSwAccount());
                switcherBranch.setSwPassword(dv.getSwPassword());
                switcherBranch.setCommunity(dv.getCommunity());
            }
            switcherBranch.setCreateTime(new Date());
            switcherBranch.setConsBatch("一期".equals(dv.getConsBatch()) ? 1 : 2);
            try {
                switcherBranch.setLat(new BigDecimal(dv.getLat()));
                switcherBranch.setLon(new BigDecimal(dv.getLon()));
                if (!StringHelper.isBlank(dv.getAcceptDate())) {
                    if (dv.getAcceptDate().length() > 10) {
                        switcherBranch.setAcceptDate(DateHelper.parseDatetime(dv.getAcceptDate()));
                    } else {
                        switcherBranch.setAcceptDate(DateHelper.parseDay(dv.getAcceptDate()));
                    }
                }


                if (op_updated) {
                    this.updateSelective(switcherBranch);
                } else {
                    switcherBranchMapper.insert(switcherBranch);
                }
            } catch (Exception e) {
                log.error("导入交换机失败，err={}", e.getMessage(), e);
                errorCount++;
                dv.setErrMsg(e.getMessage());
                errorList.add(dv);
                continue;
            }
            successCount++;
            insertOrUpdateSwitcher(switcherBranch, dv.getProjectName());
            insertOrUpdateAsset(switcherBranch, dv.getProjectName(), userSession);
        }
        //2020-12-17 增加逻辑，导入修改资产表的状态
        if (disableSb.length() > 0) {
            disableSb.delete(disableSb.length() - 1, disableSb.length());
            disableStatus.setIpAddr(disableSb.toString());
            assetService.disposeAsset(disableStatus, userSession);
        }
        if (enableSb.length() > 0) {
            enableSb.delete(enableSb.length() - 1, enableSb.length());
            enableStatus.setIpAddr(enableSb.toString());
            assetService.disposeAsset(enableStatus, userSession);
        }
        log.info("用户:{},总共导入:{}条数据！启用数据:{}条,停用数据:{}条.", userSession.getUserName(), list.size(), enCount, disCount);

        Map<String, Object> map = MapBuilder.getBuilder()
                .append("totalCount", list.size())
                .append("successCount", successCount)
                .append("errorCount", errorCount)
                .build();
        if (errorCount > 0) {
            saveErrFile(errorList, map, brandList, regionList, null);
        }
        return ApiResult.success(map);

    }

    @Override
    public List<TreeSelect> getPointListByArea(Integer areaId, List<Integer> projectIds, String pointName) {
        Map<Integer, TreeSelect> resultMap = new HashMap<>();
        Map<Integer, List<TreeSelect>> childList = new HashMap<>();
        List<SwitchBranchResVO> list = switcherBranchMapper.getPointListByArea(areaId, projectIds, pointName);
        for (int i = 0; i < list.size(); i++) {
            SwitchBranchResVO resVO = list.get(i);
            TreeSelect leafSelect = TreeSelect.builder()
                    .id(resVO.getId())
                    .label(resVO.getPointName())
                    .children(new ArrayList<>(0))
                    .data(resVO)
                    .seq(2)
                    .build();
            if (childList.containsKey(resVO.getAreaId())) {
                childList.get(resVO.getAreaId()).add(leafSelect);
            } else {
                List<TreeSelect> listRes = new ArrayList<>();
                listRes.add(leafSelect);
                childList.put(resVO.getAreaId(), listRes);
            }
            if (resultMap.containsKey(resVO.getAreaId())) {
                resultMap.get(resVO.getAreaId()).setChildren(childList.get(resVO.getAreaId()));
            } else {
                TreeSelect areaTree = TreeSelect.builder()
                        .id(resVO.getAreaId())
                        .label(resVO.getAreaName() + "大队")
                        .children(childList.get(resVO.getAreaId()))
                        .seq(1)
                        .build();
                resultMap.put(resVO.getAreaId(), areaTree);
            }
        }
        return resultMap.values().stream().collect(Collectors.toList());
    }


    private void saveErrFile(List<ExcelSwitchBranchVO> list, Map<String, Object> map, List<SysBrand> brandList, List<SelectVO> regionSecondList, List<SelectVO> area) {
        String errFilePath = this.uploadFolder +
                "交换机导入错误数据-" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        map.put("errorFilePath", errFilePath);
        Map<Integer, String[]> selectContent = new HashMap<>();
        // 下拉

//        List<SelectVO> area = areaService.findAreaToSelect();
//        List<SelectVO> regionSecondList = areaService.findRegionToSelect(REGION_LEVEL_TWO);
        List<String> regionNames = regionSecondList.stream().map(SelectVO::getItemValue).collect(Collectors.toList());
        List<String> areaNames = area.stream().map(it -> it.getItemValue()).collect(Collectors.toList());
        selectContent.put(0, regionNames.toArray(new String[regionNames.size()]));
        selectContent.put(1, areaNames.toArray(new String[areaNames.size()]));
        selectContent.put(8, new String[]{"接入交换机", "汇聚交换机", "核心交换机"});
        selectContent.put(9, brandList.stream().map(SysBrand::getBrandName).toArray(String[]::new));
        selectContent.put(11, new String[]{"停用", "启用"});
        selectContent.put(12, new String[]{"已验收", "未验收"});
        Map<String, Object> extMap = new HashMap<>();
        extMap.put("提示", "*为必填字段");
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(false)
                .wrap(true)
                .selectContent(selectContent)
                .extraContentPosition(ExportConfig.TOP).extraContent(extMap).extraContentBlankRow(0)
                .build();
        fileService.exportExcelFile(list, "交换机", errFilePath, exportConfig);
    }

    public void insertOrUpdateSwitcher(SysSwitcherBranch switcherBranch, String projectName) {
        try {
            SysSwitcher sysSwitcher = switcherService.findBySwHost(switcherBranch.getIpAddr(), null);
            if (sysSwitcher == null) {
                sysSwitcher = new SysSwitcher();
                BeanUtils.copyProperties(switcherBranch, sysSwitcher);
                sysSwitcher.setId(null);
                sysSwitcher.setSwHost(switcherBranch.getIpAddr());
                sysSwitcher.setSwCommunity(switcherBranch.getCommunity());
                sysSwitcher.setProjectName(projectName);
                switcherService.insert(sysSwitcher);
            } else {
                sysSwitcher.setLon(switcherBranch.getLon());
                sysSwitcher.setLat(switcherBranch.getLat());
                sysSwitcher.setSwCommunity(switcherBranch.getCommunity());
                sysSwitcher.setProjectName(projectName);
                if (switcherBranch.getSysName() != null)
                    sysSwitcher.setSysName(switcherBranch.getSysName());
                switcherService.updateSelective(sysSwitcher);
            }

        } catch (Exception e) {
            log.info("导入SysSwitcher-ip已经存在不导入#ip={},err={}", switcherBranch.getIpAddr(), e.getMessage());
        }
    }

    public void insertOrUpdateAsset(SysSwitcherBranch switcherBranch, String projectName, UserSession userSession) {
        AssetAddReqVO assetAddReqVO = new AssetAddReqVO();
        assetAddReqVO.setIpAddr(switcherBranch.getIpAddr());
        assetAddReqVO.setPointName(switcherBranch.getPointName());
        assetAddReqVO.setProjectName(projectName);
        assetAddReqVO.setDeviceType(1);
        assetAddReqVO.setMute(1);
        if (switcherBranch.getLat() != null) {
            assetAddReqVO.setLatitude(switcherBranch.getLat().toString());
        }

        if (switcherBranch.getLon() != null) {
            assetAddReqVO.setLongitude(switcherBranch.getLon().toString());

        }
        if (switcherBranch.getBrand() != null) {
            SysBrand sysBrand = sysBrandService.getCacheById(switcherBranch.getBrand());
            if (sysBrand != null) {
                assetAddReqVO.setDeviceModelBrand(sysBrand.getBrandName());
            }
        }
        assetService.addOrUpdate(assetAddReqVO, userSession);
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public int insertSwitcherBranch(SwitchBranchMergeVO mergeVO, UserSession userSession) {
        SysSwitcherBranch model = new SysSwitcherBranch();
        BeanUtils.copyProperties(mergeVO, model);

        model.setOnline(mergeVO.getStatus() == 1 ? 0 : 1);
        this.insert(model);
        return model.getId();
    }

    @Override
    public void editSwitcherBranch(SysSwitcherBranch sysSwitcher, SwitchBranchMergeVO mergeVO, UserSession userSession) {
        sysSwitcher.setPointName(mergeVO.getPointName());
        sysSwitcher.setSwPassword(mergeVO.getSwPassword());
        sysSwitcher.setSwAccount(mergeVO.getSwAccount());
        sysSwitcher.setSwType(mergeVO.getSwType());
        if (mergeVO.getStatus() != null) {
            sysSwitcher.setStatus(mergeVO.getStatus());
            sysSwitcher.setOnline(mergeVO.getStatus() == 1 ? 0 : 1);
        }
        sysSwitcher.setLon(mergeVO.getLon());
        sysSwitcher.setLat(mergeVO.getLat());
        sysSwitcher.setRegion(mergeVO.getRegion());
        sysSwitcher.setBrand(mergeVO.getBrand());
        sysSwitcher.setSysName(mergeVO.getSysName());
        sysSwitcher.setCommunity(mergeVO.getCommunity());
        sysSwitcher.setAcceptance(mergeVO.getAcceptance());
        sysSwitcher.setAcceptDate(mergeVO.getAcceptDate());
        sysSwitcher.setAcceptIdea(mergeVO.getAcceptIdea());
        sysSwitcher.setConsBatch(mergeVO.getConsBatch());

        this.updateSelective(sysSwitcher);

    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public ApiResult dispose(SwitchBranchDisposeVO disposeVO, UserSession userSession) {
        List<Integer> list = disposeVO.getIds();
        for (Integer id : list) {
            SysSwitcherBranch sysSwitcher = this.getById(id);
            if (sysSwitcher == null) {
                continue;
            }
            sysSwitcher.setStatus(disposeVO.getStatus());
            sysSwitcher.setOnline(disposeVO.getStatus() == 1 ? 0 : 1);
            sysSwitcher.setReason(disposeVO.getReason());
            this.updateSelective(sysSwitcher);
            AssetBranchDisposeReqVO reqVO = new AssetBranchDisposeReqVO();
            reqVO.setIpAddr(sysSwitcher.getIpAddr());
            reqVO.setEnableStatus(sysSwitcher.getStatus() == 0 ? 1 : 0);
            reqVO.setRemark(disposeVO.getReason());
            assetService.disposeAsset(reqVO, userSession);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult batchAcceptance(SwitchBranchAcceptVO acceptVO) {
        switcherBranchMapper.batchAcceptance(acceptVO);
        return ApiResult.success();
    }

    @Override
    public List<TreeSelect> getMapTreeAndData(Integer parentId) {
        TreeSelect areaRoot = TreeSelect.builder()
                .id(1)
                .label("开发区交管大队")
                .isShow(false)
                .seq(1)
                .children(new ArrayList<>())
                .build();
        if(parentId == 0){
            return Arrays.asList(areaRoot);
        }
        TreeSelect productRoot = TreeSelect.builder()
                .id(2)
                .label("交换机")
                .isShow(false)
                .children(new ArrayList<>())
                .seq(2)
                .build();
        if(parentId == 1){
            return Arrays.asList(productRoot);
        }
        List<AssetMapRespVO> assetMapRespVOList = switcherBranchMapper.assetSwitchMapListPage(new SwitchBranchSearchVO());

        List<TreeSelect> treeSelectList = new ArrayList<>();
        int i = 1;
        for (AssetMapRespVO assetMapRespVO : assetMapRespVOList) {
            TreeSelect node2 = TreeSelect.builder().id(assetMapRespVO.getId())
                    .label(assetMapRespVO.getPointName()).seq(i).isShow(false)
                    .children(new LinkedList<>())
                    .data(assetMapRespVO)
                    .build();
            treeSelectList.add(node2);
            i++;
        }
        productRoot.setChildren(treeSelectList);
        areaRoot.setChildren(Arrays.asList(productRoot));
        return treeSelectList;
    }





    @Override
    public ApiResult deleteSwitcherBranch(Integer id) {
        SysSwitcherBranch switcher = this.getById(id);
        if (switcher == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("交换机不存在#" + id);
        }
//        switcher.setDeleteStatus(1);
        //物理删除
        this.delete(switcher);
        Asset asset = assetService.findByIpAddr(switcher.getIpAddr());
        if (asset != null) {
            assetService.delete(asset);
        }
        return ApiResult.success().appendMessage("删除成功");
    }

    private File getRemoteFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        File file = new File(this.uploadFolder + "/" + filePath);
        if (!file.exists()) {
            log.error("file error#" + this.uploadFolder + "/" + filePath);
            return null;
        }
        return file;
    }

    private ExcelSheetReader getAssetReader() {
        return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                .notNullFields(Lists.newArrayList("regionName,areaName,productName,lon,lat"))
                .headerClass(ExcelSwitchBranchVO.class).build();
    }


}
