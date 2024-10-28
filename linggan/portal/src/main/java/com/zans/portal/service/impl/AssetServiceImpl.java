package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.exception.RollbackException;
import com.zans.base.office.excel.*;
import com.zans.base.office.validator.ValidateHelper;
import com.zans.base.office.validator.ValidateResult;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.ArithmeticUtil;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.*;
import com.zans.portal.model.*;
import com.zans.portal.service.*;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.arp.*;
import com.zans.portal.vo.asset.AssetVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchAssetAddReqVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchDisposeReqVO;
import com.zans.portal.vo.asset.req.AssetAddReqVO;
import com.zans.portal.vo.asset.req.AssetEditCoordinatesReqVO;
import com.zans.portal.vo.asset.req.AssetEditReqVO;
import com.zans.portal.vo.asset.req.ExcelEndpointIpAllVO;
import com.zans.portal.vo.asset.resp.AssetDetailRespVO;
import com.zans.portal.vo.asset.resp.AssetStatisRespVO;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.network.NetworkArpChangeRespVO;
import com.zans.portal.vo.radius.QzViewRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_ERROR;
import static com.zans.portal.config.GlobalConstants.*;

@Service("assetService")
@Slf4j
public class AssetServiceImpl extends BaseServiceImpl<Asset> implements IAssetService {
    @Autowired
    AssetMapper assetMapper;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IpAllMapper ipAllMapper;


    @Autowired
    private NetworkArpChangeMapper networkArpChangeMapper;

    @Autowired
    IAssetBranchAssetService assetBranchAssetService;

    @Autowired
    private IAssetProfileService assetProfileService;

    @Autowired
    private AssetBaselineDao assetBaselineDao;

    @Resource
    private AssetProfileMapper assetProfileMapper;

    @Autowired
    IAssetGuardLineService assetGuardLineService;

    @Autowired
    IRadiusEndPointService radiusEndPointService;

    @Value("${api.upload.folder}")
    String uploadFolder;


    @Autowired
    private IRadiusEndPointService endPointService;

    @Autowired
    private IRadiusEndPointProfileService endPointProfileService;

    @Autowired
    private ISwitcherService switcherService;

    @Resource
    public void setAssetMapper(AssetMapper assetMapper) {
        super.setBaseMapper(assetMapper);
        this.assetMapper = assetMapper;
    }

    @Override
    public int disposeAsset(AssetBranchDisposeReqVO reqVO, UserSession userSession) {
        reqVO.setRemark(userSession.getUserId() + " " + userSession.getUserName() + " 原因:" + reqVO.getRemark());
        reqVO.setUpdateId(userSession.getUserId());
        return assetMapper.disposeAsset(reqVO);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AssetProfile deleteAsset(Integer id, String ipAddr, UserSession userSession) {
//        if (id == null) {
//            return null;
//        }
//        AssetProfile asset = assetProfileService.getById(id);
////        String ipAddr = asset.getIpAddr();
        AssetProfile asset = assetProfileService.findByIdAddr(ipAddr);
        if (asset == null) {
            return null;
        }

        AssetBaselineVO baselineVO = assetBaselineDao.getByIp(ipAddr);
        Assert.isTrue(baselineVO!=null,"该IP在基线不存在!");
        RadiusEndpoint endpoint = radiusEndPointService.findByMacMin(baselineVO.getMac());
        Assert.isTrue(endpoint!=null,"该mac在准入不存在!");

//        asset.setDeleteStatus(GlobalConstants.DELETE_DONE);
//        assetProfileService.updateSelective(asset);
        //资产做物理删除，涉及到准入（进检疫）、基线、分组、重点资产  https://fgr44sks34.feishu.cn/docs/doccnsyvFzjYOkfKAlz7NqhnIxb#
        assetProfileMapper.deleteByIp(ipAddr);
//        assetBaselineDao.unbindByIpAddr(ipAddr);
        assetBranchAssetService.deleteByIpAddr(ipAddr);
        assetGuardLineService.deleteByIpAddr(ipAddr);
        //2022-3-3增加逻辑
        //删除准入数据;
        //如果是radius模式，进行dm操作，
        //如果是acl模式，清空交换机指令
        radiusEndPointService.delete(endpoint.getId());
//        radiusEndPointService.syncJudge(endpoint.getId(), 1, "删除资产", userSession);
        return asset;
    }

    @Override
    public AssetDetailRespVO getAssetDetail(Integer id, String ipAddr) {
//        if (id == null) {
//            return null;
//        }
//        AssetProfile asset = assetProfileService.getById(id);
//        assetMapper.findByIpAddr()
//        if (asset == null) {
//            return null;
//        }
        AssetBaselineVO byIp = assetBaselineDao.getByIp(ipAddr);
        AssetDetailRespVO resp = new AssetDetailRespVO();
        if(byIp!=null){
            BeanUtils.copyProperties(byIp, resp);
            resp.setDeviceModelBrand(byIp.getBrandName());
            resp.setDeviceModelDes(byIp.getModelDes());
            if(!StringHelper.isEmpty(byIp.getMute())){
                resp.setMuteName(byIp.getMute() == 0 ? "活跃终端" : "哑终端");
            }
            resp.setId(id);
        }
        // 组装其他数据
        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_ARP_MUTE);
        resp.setMuteByMap(muteMap);
        if (resp.getDeviceType() != null) {
            resp.setDeviceTypeName(deviceTypeService.findDeviceTypeToMap().get(resp.getDeviceType().toString()));
        }
        return resp;
    }

    @Override
    public int editAsset(AssetEditReqVO reqVO, UserSession userSession) {
        AssetProfile assetProfile = new AssetProfile();
        assetProfile.setAssetId(reqVO.getId());
        assetProfile.setCurDeviceType(reqVO.getDeviceType());
        assetProfile.setCurModelDes(reqVO.getDeviceModelDes());
        assetProfile.setCurMute(reqVO.getMute());
        AssetBaseline assetBaseline = new AssetBaseline();
        BeanUtils.copyProperties(reqVO,assetBaseline);
        assetBaseline.setModelDes(reqVO.getDeviceModelDes());
        assetBaseline.setBrandName(reqVO.getDeviceModelBrand());
        if (!StringUtils.isBlank(assetBaseline.getIpAddr())) {
            assetBaselineDao.updateAssetBaselineByAddr(assetBaseline);
        }
        assetProfileService.updateSelective(assetProfile);
        return 1;
    }

    @Override
    public int addAsset(AssetAddReqVO reqVO, UserSession userSession) {
        String ipAddr = reqVO.getIpAddr();
        if (StringUtils.isBlank(ipAddr)) {
            return 0;
        }
        if (StringUtils.isEmpty(reqVO.getLongitude())) {
            reqVO.setLongitude(null);
        }
        if (StringUtils.isEmpty(reqVO.getLatitude())) {
            reqVO.setLatitude(null);
        }
        Asset asset = assetMapper.findByIpAddr(ipAddr);
        if (asset != null) {
            if (asset.getDeleteStatus().intValue() == GlobalConstants.DELETE_DONE) {// 已经删除
                BeanUtils.copyProperties(reqVO, asset);
                AssetBaseline assetBaseline = new AssetBaseline();
                BeanUtils.copyProperties(asset, assetBaseline);
                assetBaselineDao.updateAssetBaselineByAddr(assetBaseline);//同步更新基线对应ip数据
                ///asset.setAlive(GlobalConstants.ARP_ALIVE_OFFLINE);
                asset.setSource(GlobalConstants.ASSET_SOURCE_MANUAL);
                asset.setCreatorId(userSession.getUserId());
                asset.setCreatePerson(userSession.getNickName());
                asset.setUpdateId(userSession.getUserId());
                asset.setCreateTime(new Date());
                asset.setDeleteStatus(GlobalConstants.DELETE_NOT);
                updateSelective(asset);
            } else {
                return 0;
            }
        } else {
            asset = new Asset();
            BeanUtils.copyProperties(reqVO, asset);
            ///asset.setAlive(GlobalConstants.ARP_ALIVE_OFFLINE);
            asset.setSource(GlobalConstants.ASSET_SOURCE_MANUAL);
            asset.setCreatorId(userSession.getUserId());
            asset.setCreatePerson(userSession.getNickName());
            asset.setUpdateId(userSession.getUserId());
            saveSelective(asset);
        }
        return 1;
    }

    @Override
    public int addOrUpdate(AssetAddReqVO reqVO, UserSession userSession) {
        Asset asset = assetMapper.findByIpAddr(reqVO.getIpAddr());
        if (asset != null) {
            BeanUtils.copyProperties(reqVO, asset);
            asset.setCreatorId(userSession.getUserId());
            asset.setCreatePerson(userSession.getNickName());
            asset.setUpdateId(userSession.getUserId());
            updateSelective(asset);
        } else {
            asset = new Asset();
            BeanUtils.copyProperties(reqVO, asset);
            ///asset.setAlive(GlobalConstants.ARP_ALIVE_OFFLINE);
            asset.setSource(GlobalConstants.ASSET_SOURCE_MANUAL);
            asset.setCreatorId(userSession.getUserId());
            asset.setCreatePerson(userSession.getNickName());
            asset.setUpdateId(userSession.getUserId());
            saveSelective(asset);
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public ApiResult batchAddAsset(String filePath, String fileName, Integer assetBranchId, Integer assetGuardLineId,
                                   UserSession userSession) {
        File file = getRemoteFile(filePath);
        if (file == null) {
            return null;
        }
        String absoluteNewFilePath = this.uploadFolder + filePath;
        log.info("file#{}", file.getAbsolutePath());
        try {
            ExcelEntity linkResult = checkFile(fileName, file, absoluteNewFilePath, getAssetReader());
            if (!linkResult.getValid()) {
                log.error("文件校验失败#" + absoluteNewFilePath);
                return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
            }

            if (linkResult == null) {
                return ApiResult.error("未填写任何资产!");
            }
            List<ExcelEndpointIpAllVO> list = linkResult.getEntity().convertToRawTable(ExcelEndpointIpAllVO.class);
            if (list == null || list.size() == 0) {
                return ApiResult.error("未填写任何资产!!");
            }
            log.info("link.size#{}", list.size());
            dealExcelEndpointIpAll(list, assetBranchId, assetGuardLineId, userSession);

            return ApiResult.success();
        } catch (Exception ex) {
            log.error("读取资产上传文件失败#" + absoluteNewFilePath, ex);
            return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
        }

    }

    void dealExcelEndpointIpAll(List<ExcelEndpointIpAllVO> list, Integer assetBranchId, Integer assetGuardLineId,
                                UserSession userSession) {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> maintainStatusList = constantItemService
                .findItemsByDict(GlobalConstants.SYS_DICT_KEY_MAINTAIN_STATUS);
        Asset asset = null;
        for (ExcelEndpointIpAllVO vo : list) {
            vo.resetDeviceType(deviceTypeList);
            vo.resetMaintainStatus(maintainStatusList);
            //
            if (vo.getMaintainStatus() != null
                    && vo.getMaintainStatus().intValue() == GlobalConstants.ASSET_MAINTAINSTATUS_NORMAL) {
                vo.setEnableStatus(GlobalConstants.STATUS_ENABLE);
            } else {
                vo.setEnableStatus(GlobalConstants.STATUS_DISABLE);
            }
            String ipAddr = vo.getIpAddr();
            if (StringUtils.isBlank(ipAddr)) {
                continue;
            }
            asset = assetMapper.findByIpAddr(ipAddr);
            if (asset != null) {
                BeanUtils.copyProperties(vo, asset);
                asset.setUpdateId(userSession.getUserId());
                asset.setDeleteStatus(GlobalConstants.DELETE_NOT);//导入数据默认状态未删除 edit 2021/06/29
                asset.setEnableStatus(GlobalConstants.STATUS_ENABLE);//导入数据默认状态启用 edit 2021/06/29
                updateSelective(asset);
            } else {
                asset = new Asset();
                BeanUtils.copyProperties(vo, asset);
                ///asset.setAlive(GlobalConstants.ARP_ALIVE_OFFLINE); 去下资产表在线状态 来源转至alive_heartbeat表
                asset.setSource(GlobalConstants.ASSET_SOURCE_IMPORT);
                asset.setCreatorId(userSession.getUserId());
                asset.setCreatePerson(userSession.getNickName());
                asset.setUpdateId(userSession.getUserId());
                saveSelective(asset);
            }
            if (assetBranchId != null) {
                AssetBranchAssetAddReqVO branchAddVO = new AssetBranchAssetAddReqVO();
                branchAddVO.setAssetBranchId(assetBranchId);
                branchAddVO.setAssetId(asset.getId());
                branchAddVO.setIpAddr(ipAddr);
                assetBranchAssetService.groupsAddAsset(branchAddVO, userSession);
            }

        }
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
                .notNullFields(Lists.newArrayList("ipAddr,pointName,areaName,deviceTypeName"))
                .headerClass(ExcelEndpointIpAllVO.class).build();
    }

    private ExcelEntity checkFile(String fileName, File file, String outFilePath, ExcelSheetReader reader)
            throws Exception {
        ExcelEntity result = new ExcelEntity();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook, reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;
            sheetEntity.resetRuleMap();

            checkInput(sheet, sheetEntity, true, dataStyle);
            result.setValid(sheetEntity.getValid());
            result.setEntity(sheetEntity);

            // 校验不通过，修改文件
            if (!result.getValid()) {
                os = new FileOutputStream(new File(outFilePath));
                workbook.write(os);
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception ex1) {
                log.error("readFile error, close error", ex1);
            }
        }
        return result;
    }

    /**
     * 校验 excel输入，直接保存校验结果到 poi 对象
     *
     * @param poiSheet    poi 的sheet
     * @param sheetEntity 数据对象
     * @param writeExcel  true，校验结果写入excel； false，不写文件
     */
    private void checkInput(Sheet poiSheet, SheetEntity sheetEntity, boolean writeExcel, CellStyle cellStyle) {
        boolean validResult = true;
        for (ExcelRow excelRow : sheetEntity.getData()) {
            int rowIndex = excelRow.getRow();

            Row poiRow = poiSheet.getRow(rowIndex);
            log.info("row[{}], {}", rowIndex, (poiRow != null));
            for (String name : excelRow.getData().keySet()) {
                ExcelColumn column = excelRow.getColumn(name);
                int colIndex = column.getCol();
                Object val = column.getValue();
                String[] rules = sheetEntity.getValidateRule(name);
                // 数据校验
                ValidateResult result = ValidateHelper.doValidate(val, rules);
                column.setValid(result.getPassed());
                if (result.getPassed()) {
                    continue;
                }
                validResult = false;
                String message = result.getMessages();

                // 更新内存对象的值
                String strVal = (val == null) ? "" : val.toString();
                if (!strVal.contains(message)) {
                    strVal = strVal + message;
                    column.setValue(strVal);
                }
                log.error("validate#{}-{}, {}, {}, {}", rowIndex, colIndex, name, message, strVal);

                log.info("col[{}]#{}, error#{}", colIndex, name, strVal);
                if (!writeExcel) {
                    continue;
                }
                // 更新Excel的值
                Cell poiCell = poiRow.getCell(colIndex);

                if (poiCell == null) {
                    poiCell = poiRow.createCell(colIndex);
                    poiCell.setCellStyle(cellStyle);
                }
                poiCell.setCellValue(strVal);
            }
        }
        sheetEntity.setValid(validResult);
    }

    @Override
    public AssetRespVO getAssetDynamicDetail(Integer id, String ipAddr) {
//        AssetRespVO respVO = assetMapper.findAssetDynamicDetail(id);
        AssetRespVO respVO = assetMapper.findAssetDynamicDetailByIp(ipAddr);

        if (respVO == null) {
            return null;
        }
        if (respVO.getDeviceType() != null) {
//            respVO.setDeviceTypeName(deviceTypeService.findDeviceTypeToMap().get(respVO.getDeviceType().toString()));
            respVO.setDeviceTypeNameDetect(deviceTypeService.findDeviceTypeToMap().get(respVO.getDeviceTypeDetect().toString()));
        }

        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(MODULE_ARP_MUTE);
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> assetSourceMap = constantItemService.findItemsMapByDict(MODULE_ASSET_SOURCE);
        Map<Object, String> assetManageMap = constantItemService.findItemsMapByDict(MODULE_ASSET_MANAGE);
        respVO.setAliveByMap(aliveMap);
        respVO.setMuteByMap(muteMap);
        respVO.setAssetSourceNameByMap(assetSourceMap);
        respVO.setAssetManageNameByMap(assetManageMap);
        return respVO;
    }

    @Override
    public ApiResult getDeny(ArpViewVO res) {
        Asset asset = getById(res.getId());
        if (asset == null) {
            return ApiResult.success(new ArrayList<>());
        }
        int pageNum = res.getPageNum();
        int pageSize = res.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<NetworkArpChangeRespVO> list = networkArpChangeMapper.findNetworkArpChangeByIp(asset.getIpAddr());
        if (list == null || list.size() < 2) {
            return ApiResult.success(new ArrayList<>());
        }

        // NetworkArpChangeRespVO arpChangeByOldIp =
        // networkArpChangeMapper.findNetworkArpChangeOldByIp(asset.getIpAddr());
        // NetworkArpChangeRespVO arpChangeByCurIp =
        // networkArpChangeMapper.findNetworkArpChangeCurByIp(asset.getIpAddr());
        // if(arpChangeByCurIp!=null && arpChangeByOldIp!=null){
        // arpChangeByOldIp.setCompany(arpChangeByCurIp.getCompany());
        // arpChangeByOldIp.setMacAddr(arpChangeByCurIp.getMacAddr());
        // arpChangeByOldIp.setModelDes(arpChangeByCurIp.getModelDes());
        // arpChangeByOldIp.setOpenPort(arpChangeByCurIp.getOpenPort());
        // arpChangeByOldIp.setDeviceType(arpChangeByCurIp.getDeviceType());
        // }

        return ApiResult.success(
                new PageResult<NetworkArpChangeRespVO>(page.getTotal(), list, res.getPageSize(), res.getPageNum()));

    }

    @Override
    public AssetVO findByIpAddr(String ip) {
        return assetMapper.findByIpAddr(ip);
    }

    /**
     * @param ip
     * @return com.zans.portal.vo.radius.QzViewRespVO
     * @Author pancm
     * @Description 根据IP查询
     * @Date 2020/10/29
     * @Param [ip]
     */
    @Override
    public QzViewRespVO findByIp(String ip) {
        Asset asset = assetMapper.findByIpAddr(ip);
        if (asset == null) {
            return null;
        }
        QzViewRespVO qzViewRespVO = new QzViewRespVO();
        if (asset.getDeviceType() != null) {
            qzViewRespVO.setTypeName(deviceTypeService.getNameByType(asset.getDeviceType()));
        }
        qzViewRespVO.setPointName(asset.getPointName());
        qzViewRespVO.setProjectName(asset.getProjectName());
        qzViewRespVO.setContractorPerson(asset.getContractorPerson());
        qzViewRespVO.setContractorPhone(asset.getContractorPhone());
        qzViewRespVO.setDeviceModelDes(asset.getDeviceModelDes());
        qzViewRespVO.setDeviceModelBrand(asset.getDeviceModelBrand());
        qzViewRespVO.setMaintainCompany(asset.getMaintainCompany());
        qzViewRespVO.setIpAddr(asset.getIpAddr());
        return qzViewRespVO;
    }

    @Override
    public Asset uniqueByCondition(HashMap<String, Object> map) {
        return assetMapper.uniqueByCondition(map);
    }

    @Override
    public List<AssetStatisRespVO> getAssetDeviceStatis() {
        List<AssetStatisRespVO> resultList = assetMapper.findAssetDeviceStatis();
        if (CollectionUtils.isEmpty(resultList)) {
            resultList = new ArrayList<>();
        }
        resultList.forEach(vo -> vo.setOnlineRate(ArithmeticUtil.percent(vo.getAliveNum(), vo.getTotalNum(), 1)));
        return resultList;
    }


    @Override
    public List<String[]> findRadiusMacByDate() {
        List<String> days = getDaysBetwwen(9);
        String[] dates = days.toArray(new String[10]);
        List<String[]> units = new ArrayList<>();
        for (String date : dates) {
            String[] unit = new String[3];
            CountUnit auth = assetMapper.findAuthByDate(date);
            CountUnit refuse = assetMapper.findRefuseByDate(date);
            unit[0] = date.substring(5, date.length());
            unit[1] = String.valueOf(auth.getVal());
            unit[2] = String.valueOf(refuse.getVal());
            units.add(unit);
        }
        return units;
    }


    @Override
    public PageResult<AssetRespVO> getAssetPage(AssetSearchVO asset) {
        int pageNum = asset.getPageNum();
        int pageSize = asset.getPageSize();

        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(MODULE_ARP_MUTE);
        Map<Object, String> ipStatusMap = constantItemService.findItemsMapByDict(MODULE_ARP_IP_STATUS);
        Map<Object, String> disStatusMap = constantItemService.findItemsMapByDict(MODULE_ARP_DIS_STATUS);
        Map<Object, String> riskTypeMap = constantItemService.findItemsMapByDict(MODULE_ARP_RISK_TYPE);
        Map<Object, String> confirmMap = constantItemService.findItemsMapByDict(MODULE_ARP_CONFIRM_STATUS);
        Map<Object, String> projectStatusMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_PROJECT_STATUS);

        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(GlobalConstants.SYS_DICT_KEY_ENABLE_STATUS);
        Map<Object, String> sourceMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_SOURCE);
        Map<Object, String> maintainStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_MAINTAIN_STATUS);
        Map<Object, String> assetSourceMap = constantItemService.findItemsMapByDict(MODULE_ASSET_SOURCE);
        Map<Object, String> assetManageMap = constantItemService.findItemsMapByDict(MODULE_ASSET_MANAGE);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();

        Page page = PageHelper.startPage(pageNum, pageSize);
        /**
         * @description: 资产列表改为查endpoint相关 和首页公用一个查询主表条件
         * edit by ns_wang 2020/10/13 14:19
         *  2022-5-30 武汉沌口的更改资产数据以asset_baseline为准
         *
         */
        List<AssetRespVO> list = endPointService.findAssetsForList(asset);
        List<String> ipList = new ArrayList<>(list.size());
        for (AssetRespVO row : list) {
            if (row == null) {
                continue;
            }
            ipList.add(row.getIpAddr());
            row.setAliveByMap(aliveMap);
            row.setMuteByMap(muteMap);
            row.setIpStatusByMap(ipStatusMap);
            row.setDisStatusByMap(disStatusMap);
            row.setRiskTypeNameByMap(riskTypeMap);
            row.setConfirmStatusNameByMap(confirmMap);
            row.setProjectStatusNameByMap(projectStatusMap);
            row.setEnableStatusNameByMap(enableStatusMap);
            row.setSourceNameByMap(sourceMap);
//            if (row.getDeviceType() != null) {
//                row.setDeviceTypeName(deviceTypeMap.get(row.getDeviceType().toString()));
//            }
            if (row.getDeviceTypeDetect()!= null) {
                row.setDeviceTypeNameDetect(deviceTypeMap.get(row.getDeviceTypeDetect().toString()));
            }
            row.setMaintainStatusNameByMap(maintainStatusMap);
            row.setAssetSourceNameByMap(assetSourceMap);
            row.setAssetManageNameByMap(assetManageMap);
        }
        //获取每个ip当前接入设备数量
        List<AssetRespVO> ipCurEndpointNum = endPointProfileService.findIpEndpointAliveNum(ipList);
        if (CollectionUtils.isNotEmpty(ipCurEndpointNum)) {
            Map<String, Integer> ipEndpointNumMap = ipCurEndpointNum.stream().collect(Collectors.toMap(r -> r.getIpAddr(), r -> r.getCurAliveDeviceNum()));
            for (AssetRespVO row : list) {
                if (ipEndpointNumMap.get(row.getIpAddr()) != null) {
                    row.setCurAliveDeviceNum(ipEndpointNumMap.get(row.getIpAddr()));
                }
            }
        }
        return new PageResult<AssetRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public PageResult<AssetRespVO> getAssetNewListPage(AssetSearchVO asset) {
        int pageNum = asset.getPageNum();
        int pageSize = asset.getPageSize();

        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(MODULE_ARP_MUTE);
        Map<Object, String> ipStatusMap = constantItemService.findItemsMapByDict(MODULE_ARP_IP_STATUS);
        Map<Object, String> disStatusMap = constantItemService.findItemsMapByDict(MODULE_ARP_DIS_STATUS);
        Map<Object, String> riskTypeMap = constantItemService.findItemsMapByDict(MODULE_ARP_RISK_TYPE);
        Map<Object, String> confirmMap = constantItemService.findItemsMapByDict(MODULE_ARP_CONFIRM_STATUS);
        Map<Object, String> projectStatusMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_PROJECT_STATUS);

        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(GlobalConstants.SYS_DICT_KEY_ENABLE_STATUS);
        Map<Object, String> sourceMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_SOURCE);
        Map<Object, String> maintainStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_MAINTAIN_STATUS);
        Map<Object, String> assetSourceMap = constantItemService.findItemsMapByDict(MODULE_ASSET_SOURCE);
        Map<Object, String> assetManageMap = constantItemService.findItemsMapByDict(MODULE_ASSET_MANAGE);
        Map<Object, String> deviceTypeMap = deviceTypeService.findDeviceTypeToMap();

        Page page = PageHelper.startPage(pageNum, pageSize);
        /**
         * @description: 资产列表改为查endpoint相关 和首页公用一个查询主表条件
         * edit by ns_wang 2020/10/13 14:19
         *  2022-5-30 武汉沌口的更改资产数据以asset_baseline为准
         *
         */
        List<AssetRespVO> list = endPointService.getAssetNewListPage(asset);
        List<String> ipList = new ArrayList<>(list.size());
        for (AssetRespVO row : list) {
            if (row == null) {
                continue;
            }
            ipList.add(row.getIpAddr());
            row.setAliveByMap(aliveMap);
            row.setMuteByMap(muteMap);
            row.setIpStatusByMap(ipStatusMap);
            row.setDisStatusByMap(disStatusMap);
            row.setRiskTypeNameByMap(riskTypeMap);
            row.setConfirmStatusNameByMap(confirmMap);
            row.setProjectStatusNameByMap(projectStatusMap);
            row.setEnableStatusNameByMap(enableStatusMap);
            row.setSourceNameByMap(sourceMap);
//            if (row.getDeviceType() != null) {
//                row.setDeviceTypeName(deviceTypeMap.get(row.getDeviceType().toString()));
//            }
            if (row.getDeviceTypeDetect()!= null) {
                row.setDeviceTypeNameDetect(deviceTypeMap.get(row.getDeviceTypeDetect().toString()));
            }
            row.setMaintainStatusNameByMap(maintainStatusMap);
            row.setAssetSourceNameByMap(assetSourceMap);
            row.setAssetManageNameByMap(assetManageMap);
        }
        //获取每个ip当前接入设备数量
        List<AssetRespVO> ipCurEndpointNum = endPointProfileService.findIpEndpointAliveNum(ipList);
        if (CollectionUtils.isNotEmpty(ipCurEndpointNum)) {
            Map<String, Integer> ipEndpointNumMap = ipCurEndpointNum.stream().collect(Collectors.toMap(r -> r.getIpAddr(), r -> r.getCurAliveDeviceNum()));
            for (AssetRespVO row : list) {
                if (ipEndpointNumMap.get(row.getIpAddr()) != null) {
                    row.setCurAliveDeviceNum(ipEndpointNumMap.get(row.getIpAddr()));
                }
            }
        }
        return new PageResult<AssetRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }


    @Override
    public List<ExcelAssetVO> getExportAssets(AssetSearchVO reqVO) {
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(MODULE_ARP_MUTE);
        Map<Object, String> ipStatusMap = constantItemService.findItemsMapByDict(MODULE_ARP_IP_STATUS);
        Map<Object, String> disStatusMap = constantItemService.findItemsMapByDict(MODULE_ARP_DIS_STATUS);
        Map<Object, String> projectStatusMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_PROJECT_STATUS);

        Map<Object, String> enableStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_ENABLE);
        Map<Object, String> sourceMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_SOURCE);
        Map<Object, String> maintainStatusMap = constantItemService.findItemsMapByDict(SYS_DICT_KEY_MAINTAIN_STATUS);
        Map<Object, String> deviceTypeNameMap = deviceTypeService.findDeviceTypeToMap();
        List<ExcelAssetVO> list = assetMapper.findAssetsForExport2(reqVO);

        for (ExcelAssetVO row : list) {
            if (row == null) {
                continue;
            }
            if (row.getDeviceType() != null) {
                row.setDeviceTypeName(deviceTypeNameMap.get(row.getDeviceType()));
            }
            row.setAliveByMap(aliveMap);
            row.setMuteByMap(muteMap);
            row.setIpStatusByMap(ipStatusMap);
            row.setDisStatusByMap(disStatusMap);
            row.setProjectStatusByMap(projectStatusMap);
            row.setEnableStatusNameByMap(enableStatusMap);
            row.setSourceNameByMap(sourceMap);
            row.setMaintainStatusNameByMap(maintainStatusMap);
        }
        return list;
    }


    @Override
    public ArpSwitchVO getByMacAdress(String macAdress) {
        return ipAllMapper.getByMacAdress(macAdress);
        //2020-9-18 襄阳项目查询更改表
//        return arpMapper.getByMacAdress(macAdress);
    }


    @Override
    public List<NetworkSwitchVO> getNetWorkSwitchByMacTopo(String mac) {
        List<NetworkSwitchVO> netWork = new ArrayList<>();
        NetworkSwitchVO vo = assetMapper.findNetWorkSwitchByMacTopo(mac);
        AtomicInteger depth = new AtomicInteger(1);
        if (null != vo) {
            List<SysSwitcher> rootSwitcherList = switcherService.getListCondition(new HashMap<String, Object>(4) {{
                put("arpEnable", 1);
            }});
            List<String> rootIps = rootSwitcherList.stream().map(sysSwitcher -> sysSwitcher.getSwHost()).collect(Collectors.toList());
            netWork.add(vo);
            dealSwitcherTopo(netWork, vo.getSwIp(), rootIps, depth);
            depth.set(1);
        } else {
            netWork = assetMapper.findNetWorkSwitchByMac(mac);
        }
        return netWork;
    }

    private void dealSwitcherTopo(List<NetworkSwitchVO> netWork, String swIp, List<String> rootIps, AtomicInteger depth) {
        int i = depth.incrementAndGet();
        if (rootIps.contains(swIp) || i > 9) {
            return;
        }
        NetworkSwitchVO vo = assetMapper.findSwitcherTopo(swIp);
        netWork.add(vo);
        dealSwitcherTopo(netWork, vo.getSwIp(), rootIps, depth);
    }


    public static Date getDateAdd(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    private static List<String> getDaysBetwwen(int days) {
        List<String> dayss = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(getDateAdd(days));
        Long startTIme = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();
        Long oneDay = 1000 * 60 * 60 * 24L;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dayss.add(df.format(d));
            time += oneDay;
        }
        return dayss;
    }


    @Override
    public int editAssetCoordinates(AssetEditCoordinatesReqVO reqVO) {
        AssetBaseline assetBaseline = new AssetBaseline();
        // 2021-9-24 更逻辑
        if (reqVO.getLongitude() != null && reqVO.getLatitude() != null) {
            assetBaseline.setIpAddr(reqVO.getIpAddr());
            assetBaseline.setLongitude(reqVO.getLongitude());
            assetBaseline.setLatitude(reqVO.getLatitude());
            return assetBaselineDao.updateAssetBaselineByAddr(assetBaseline);
        }
        return 0;

    }

    @Override
    public List<CircleUnit> getBrand() {
        return assetMapper.getBrand();
    }

    @Override
    public AssetRespVO getAssetNewDynamicDetail(Integer id, String ipAddr) {
//        AssetRespVO respVO = assetMapper.findAssetDynamicDetail(id);
        AssetRespVO respVO = assetMapper.findAssetNewDynamicDetailByIp(ipAddr);

        if (respVO == null) {
            return null;
        }
        if (respVO.getDeviceType() != null) {
            respVO.setDeviceTypeName(deviceTypeService.findDeviceTypeToMap().get(respVO.getDeviceType()));
            respVO.setDeviceTypeNameDetect(deviceTypeService.findDeviceTypeToMap().get(respVO.getDeviceTypeDetect()));
        }

        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(MODULE_ARP_MUTE);
        Map<Object, String> aliveMap = constantItemService.findItemsMapByDict(MODULE_ARP_ALIVE);
        Map<Object, String> assetSourceMap = constantItemService.findItemsMapByDict(MODULE_ASSET_SOURCE);
        Map<Object, String> assetManageMap = constantItemService.findItemsMapByDict(MODULE_ASSET_MANAGE);
        respVO.setAliveByMap(aliveMap);
        respVO.setMuteByMap(muteMap);
        respVO.setAssetSourceNameByMap(assetSourceMap);
        respVO.setAssetManageNameByMap(assetManageMap);
        return respVO;
    }


}
