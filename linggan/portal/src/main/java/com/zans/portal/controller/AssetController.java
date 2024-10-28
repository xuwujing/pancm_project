package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.SysCustomFieldDao;
import com.zans.portal.model.AssetProfile;
import com.zans.portal.model.SysCustomField;
import com.zans.portal.service.*;
import com.zans.portal.vo.AlertRuleStrategyReqVO;
import com.zans.portal.vo.arp.*;
import com.zans.portal.vo.asset.req.AssetAddReqVO;
import com.zans.portal.vo.asset.req.AssetEditCoordinatesReqVO;
import com.zans.portal.vo.asset.req.AssetEditReqVO;
import com.zans.portal.vo.asset.resp.AssetDetailRespVO;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.deny.DenyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.BaseConstants.DATE_RANGE_ARRAY_SIZE;
import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.base.office.excel.ExportConfig.TOP;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "  /asset", tags = {"/asset ~ 资产管理"})
@RestController
@Validated
@RequestMapping("/asset")
@Slf4j
public class AssetController extends BasePortalController {


    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;


    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    IFileService fileService;

    @Autowired
    IRadiusEndPointService radiusEndPointService;


    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;


    @Autowired
    IIpService ipService;

    /**
     * 资产管理
     */
    @Autowired
    IAssetService assetService;

    @Autowired
    IAlertRuleService ruleService;

    @Autowired
    private ISysConstantService sysConstantService;

    @Resource
    private SysCustomFieldDao sysCustomFieldDao;

    @ApiOperation(value = "/list", notes = "资产查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AssetSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetRespVO>> list(@RequestBody AssetSearchVO req) {

        SelectVO selectVO = sysConstantService.findSelectVOByKey(ASSET_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());
        List<String> dateRange = req.getAliveDateRange();
        if (dateRange != null && dateRange.size() == DATE_RANGE_ARRAY_SIZE) {
            req.setAliveStartDate(dateRange.get(0));
            req.setAliveEndDate(dateRange.get(1));
        }
        PageResult<AssetRespVO> pageResult = assetService.getAssetPage(req);
        return ApiResult.success(pageResult);
    }

    @ApiOperation(value = "/list_init", notes = "设备综合查询初始化")
    @RequestMapping(value = "/list_init", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult init() {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<TreeSelect>  deviceTypeParentList = deviceTypeService.deviceTypeTreeList();

        List<SelectVO> arpAlive = constantItemService.findItemsByDict(MODULE_ARP_ALIVE);
        List<SelectVO> arpDisStatus = constantItemService.findItemsByDict(MODULE_ARP_DIS_STATUS);
        List<SelectVO> arpMute = constantItemService.findItemsByDict(MODULE_ARP_MUTE);
        List<SelectVO> arpIpStatus = constantItemService.findItemsByDict(MODULE_ARP_IP_STATUS);
        List<SelectVO> projectStatusList = constantItemService.findItemsByDict(MODULE_IP_ALL_PROJECT_STATUS);
        List<SelectVO> arpConfirm = constantItemService.findItemsByDict(MODULE_ARP_CONFIRM_STATUS);
        List<SelectVO> detectType = constantItemService.findItemsByDict(MODULE_DETECT_TYPE);

        List<SelectVO> regionFirstList = areaService.findRegionToSelect(REGION_LEVEL_ONE);
        List<SelectVO> regionSecondList = areaService.findRegionToSelect(REGION_LEVEL_TWO);
        //是否可用
        List<SelectVO> enableStatus = constantItemService.findItemsByDict(GlobalConstants.SYS_DICT_KEY_ENABLE_STATUS);
        //资产录入来源
        List<SelectVO> sourceList = constantItemService.findItemsByDict(SYS_DICT_KEY_SOURCE);
        //维护状态
        List<SelectVO> maintainStatusList = constantItemService.findItemsByDict(GlobalConstants.SYS_DICT_KEY_MAINTAIN_STATUS);
        List<SelectVO> assetSource = constantItemService.findItemsByDict(GlobalConstants.MODULE_ASSET_SOURCE);
        List<SelectVO> assetManage = constantItemService.findItemsByDict(MODULE_ASSET_MANAGE);
        List<SelectVO> policy = constantItemService.findItemsByDict(MODULE_POLICY);


        SysCustomField sysCustomField = new SysCustomField();
        sysCustomField.setFiledEnable(1);
        sysCustomField.setModuleName(MODULE_NAME_ASSET_LIST);
        sysCustomField.setFieldType(1);
        Map<String, String> data = new LinkedHashMap<>();
        data = getFiledMap(sysCustomField, data);
        Map<String, String> queryData = new LinkedHashMap<>();
        sysCustomField.setFieldType(2);
        queryData = getFiledMap(sysCustomField, queryData);


        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ARP_ALIVE, arpAlive)
                .put(MODULE_ARP_DIS_STATUS, arpDisStatus)
                .put(MODULE_ARP_MUTE, arpMute)
                .put(MODULE_DEVICE, deviceTypeList)
                .put(MODULE_DEVICE_DETECT, deviceTypeList)
                .put("device_parent_tree",deviceTypeParentList)
                .put(SYS_DICT_KEY_ENABLE, enableStatus)
                .put(SYS_DICT_KEY_SOURCE, sourceList)
                .put(MODULE_REGION_FIRST, regionFirstList)
                .put(MODULE_REGION_SECOND, regionSecondList)
                .put(MODULE_ARP_IP_STATUS, arpIpStatus)
                .put(MODULE_ARP_CONFIRM_STATUS, arpConfirm)
                .put(MODULE_IP_ALL_PROJECT_STATUS, projectStatusList)
                .put("maintainStatus", maintainStatusList)
                .put(CUSTOM_COLUMN, data)
                .put(CUSTOM_QUERY, queryData)
                .put(MODULE_DETECT_TYPE, detectType)
                .put(MODULE_ASSET_SOURCE, assetSource)
                .put(MODULE_ASSET_MANAGE, assetManage)
                .put(MODULE_POLICY, policy)
                // .put(INIT_DATA_TABLE, assetPage)
                .build();
        return ApiResult.success(result);
    }

    private Map<String, String> getFiledMap(SysCustomField sysCustomField, Map<String, String> data) {
        List<SysCustomField> sysCustomFields = sysCustomFieldDao.queryAll(sysCustomField);
        if(!org.springframework.util.StringUtils.isEmpty(sysCustomFields)){
            //这个方法的排序
//            data = sysCustomFields.stream().collect(Collectors.toMap(SysCustomField::getFieldKey, SysCustomField::getFieldName));
            for (SysCustomField customField : sysCustomFields) {
                data.put(customField.getFieldKey(),customField.getFieldName());
            }

        }
        return data;
    }

    @ApiOperation(value = "/view", notes = "资产详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资产id", required = true,
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "endpointId", value = "设备id", required = false,
                    dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<AssetRespVO> getAssetById(Integer id,String ipAddr) {
        AssetRespVO asset = assetService.getAssetDynamicDetail(id,ipAddr);
        if (asset == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("ip.id isn't existed#" + id);
        }
//        asset.setMaintainStatusName(asset.getMaintainStatus() == null ? "" : (asset.getMaintainStatus() == 1 ? "正常" : (asset.getMaintainStatus() == 2 ? "迁改停用" : "审核停用")));
        asset.setMaintainStatusName(asset.getMaintainStatus() == null ? "" : asset.getMaintainStatus());

        return ApiResult.success(asset);
    }

    @ApiOperation(value = "/detail", notes = "资产基础详细信息")
    @RequestMapping(value = "/detail", method = {RequestMethod.GET})
    public ApiResult<AssetDetailRespVO> getAssetDetailById(Integer id,String ipAddr) {
        AssetDetailRespVO asset = assetService.getAssetDetail(id,ipAddr);
        if (asset == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("资产ID 不存在#" + id);
        }
        return ApiResult.success(asset);
    }

    @ApiOperation(value = "/getDeny", notes = "设备详细信息")
    @RequestMapping(value = "/getDeny", method = {RequestMethod.POST})
    public ApiResult getDeny(@RequestBody ArpViewVO res) {
        return assetService.getDeny(res);
    }


    @ApiOperation(value = "/delete", notes = "删除资产")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ASSET_MANAGE, operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(Integer id,String ipAddr, HttpServletRequest request) {
        //V2.2.2  改为物理删除，删除成功后，需要解绑基线表asset_baseline，修改bind_status为0；
        AssetProfile ipAll = assetService.deleteAsset(id,ipAddr, getUserSession(request));
        if (ipAll == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("资产ID 不存在#" + id);
        }
        // 记录日志
        return ApiResult.success();
    }

    @ApiOperation(value = "/export", notes = "导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AssetSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ASSET_MANAGE, operation = PORTAL_LOG_OPERATION_EXPORT)
    public void downloadFile(@RequestBody AssetSearchVO req,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        SelectVO selectVO = sysConstantService.findSelectVOByKey(ASSET_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());
        log.info("downloadFile#{}", req);
        String filePath = this.exportFolder +
                "asset_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "资产管理";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";

        List<String> dateRange = req.getAliveDateRange();
        if (dateRange != null && dateRange.size() == DATE_RANGE_ARRAY_SIZE) {
            req.setAliveStartDate(dateRange.get(0));
            req.setAliveEndDate(dateRange.get(1));
        }
        req.setPageNum(null);
        req.setPageSize(null);
        List<ExcelAssetVO> list = assetService.getExportAssets(req);
        int onlineCount = 0;
        for (ExcelAssetVO vo : list) {
            if (vo != null && vo.getAlive() != null && ARP_ALIVE_ONLINE == vo.getAlive()) {
                onlineCount++;
            }
        }
        int onlinePercent = 0;
        if (list.size() > 0) {
            onlinePercent = onlineCount * 100 / list.size();
        }

        log.info("data#{}, onlinePercent#{}", list.size(), onlinePercent);
        Map<String, Object> contentMap = MapBuilder.getBuilder()
                .put("在线设备数", onlineCount)
                .put("设备总数", list.size())
                .put("设备在线率", onlinePercent + "%").build();
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .extraContentPosition(TOP).extraContent(contentMap).extraContentBlankRow(1)
                .freeze(true).freezeCol(10).freezeRow(1 + contentMap.size() + 1).build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }


    @ApiOperation(value = "/getSwitcherByMac", notes = "网络管理  - 入网路径")
    @RequestMapping(value = "/getSwitcherByMac", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult getSwitcherByMac(String mac) {
        ArpSwitchVO arpSwitchVO = assetService.getByMacAdress(mac);
        //20210105 入网路径改为先读拓扑图 然后读network_switcher_mac
//        List<NetworkSwitchVO> netWork = arpService.getNetWorkSwitchByMac(macAddr);
        List<NetworkSwitchVO> netWork = assetService.getNetWorkSwitchByMacTopo(mac);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put("arp", arpSwitchVO)
                .put(INIT_DATA_TABLE, netWork)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/summary", notes = "资产概述报表")
    @RequestMapping(value = "/summary", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult summary(HttpServletRequest request) {
        List<CountUnit> summary = radiusEndPointService.summary();
        return ApiResult.success(summary);
    }


    @ApiOperation(value = "/device", notes = "资产类型报表")
    @RequestMapping(value = "/device", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult device(HttpServletRequest request) {
        return ApiResult.success(radiusEndPointService.device());
    }


    @ApiOperation(value = "/download/template", notes = "文件下载")
    @GetMapping("/download/template")
    @Record(module = PORTAL_MODULE_ASSET_MANAGE, operation = PORTAL_LOG_TEMPLATE_DOWNLOAD)
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder + "template/资产导入模板.xlsx";
        if (PROJECT_ENV_XYGA.equals(PROJECT_ENV)) {
            //如果是襄阳公安
            filePath = this.uploadFolder + "/template/资产上传模板(襄阳).xlsx";
        }

        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "资产导入模板" + date + ".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    @ApiOperation(value = "/upload", notes = "上传文件，post参数id")
    @PostMapping("/upload")
    @ResponseBody
    @Record(module = PORTAL_MODULE_ASSET_MANAGE, operation = PORTAL_LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,
                                @RequestParam(name = "assetBranchId", required = false) Integer assetBranchId,
                                @RequestParam(name = "assetGuardLineId", required = false) Integer assetGuardLineId,
                                HttpServletRequest request) throws Exception {
        if (!ExcelHelper.checkExtension(file)) {
            return ApiResult.error(SERVER_UPLOAD_MIME_ERROR).appendMessage("不是Excel类型");
        }
        long size = file.getSize();
        if (size > UPLOAD_FILE_MAX_SIZE) {
            return ApiResult.error(SERVER_UPLOAD_MAX_SIZE_ERROR)
                    .appendMessage("最大" + FileHelper.getSizeInMb(UPLOAD_FILE_MAX_SIZE) + "MB");
        }

        // 上传文件，持久化到本地，写数据库
        String originName = file.getOriginalFilename();
        String newFileName = fileService.getNewFileName(originName);
        boolean saved = FileHelper.saveFile(file, uploadFolder, newFileName);
        if (saved) {
            UserSession userSession = getUserSession(request);
            if (PROJECT_ENV_XYGA.equals(PROJECT_ENV)) {
                //如果是襄阳公安
                return ipService.readIpAllFile(newFileName, originName, userSession, assetBranchId, assetGuardLineId);
            }

            return assetService.batchAddAsset(newFileName, originName, assetBranchId, assetGuardLineId, userSession);
        }
        return ApiResult.success();
    }

    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile", method = {RequestMethod.GET})
    @Record(module = PORTAL_MODULE_ASSET_MANAGE, operation = LOG_OPERATION_DOWNLOAD_ERROR_FILE)
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "资产上传失败" + date + ".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }


    @ApiOperation(value = "/add", notes = "新增资产")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reqVO", value = "查询条件", required = true,
                    dataType = "AssetAddReqVO", paramType = "body")
    })
    @Record(module = PORTAL_MODULE_ASSET_MANAGE, operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult<DenyVO> addAsset(@Valid @RequestBody AssetAddReqVO reqVO,
                                      HttpServletRequest request) {
        if (0 == assetService.addAsset(reqVO, getUserSession(request))) {
            return ApiResult.error("ip已存在");
        }
        return ApiResult.success();
    }

    @ApiOperation(value = "/edit", notes = "编辑资产")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @ApiImplicitParam(name = "reqVO", value = "编辑信息", required = true, dataType = "AssetEditReqVO", paramType = "body")
    @Record(module = PORTAL_MODULE_ASSET_MANAGE, operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult editAsset(@Valid @RequestBody AssetEditReqVO reqVO,
                               HttpServletRequest request) {
        int count = assetService.editAsset(reqVO, getUserSession(request));
        if (0 == count) {
            return ApiResult.error("编辑资产失败");
        }
        return ApiResult.success();
    }


    @ApiOperation(value = "/editCoordinates", notes = "编辑资产经纬度")
    @RequestMapping(value = "/editCoordinates", method = {RequestMethod.POST})
    @ApiImplicitParam(name = "reqVO", value = "编辑资产经纬度信息", required = true, dataType = "AssetEditReqVO", paramType = "body")
    @Record(module = PORTAL_MODULE_ASSET_MANAGE, operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult editCoordinates(@Valid @RequestBody AssetEditCoordinatesReqVO reqVO,
                                     HttpServletRequest request) {
        int count = assetService.editAssetCoordinates(reqVO);
        if (0 == count) {
            return ApiResult.error("编辑资产经纬度失败");
        }
        return ApiResult.success();
    }

    //assetGuardLineId

    /**
     * 重点线路策略列表
     *
     * @param assetGuardLineId 重点线路id
     * @return
     */
    @RequestMapping(value = "/guardline/strategy", method = {RequestMethod.GET})
    public ApiResult guardlineStrategy(@RequestParam("assetGuardLineId") Long assetGuardLineId) {
        //以规则为主表 关联策略表 再关联重点线路 做数据展示
        if (null == assetGuardLineId) {
            return ApiResult.error("重点线路id为空！");
        }
        return ruleService.guardline(assetGuardLineId);
    }


    /**
     * 重点线路策略新增和修改
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/guardline/addStrategy", method = {RequestMethod.POST})
    public ApiResult addStrategy(@RequestBody AlertRuleStrategyReqVO alertRuleStrategyReqVO) {
        if (null == alertRuleStrategyReqVO.getId() && !ruleService.checkGroupName(null, alertRuleStrategyReqVO.getGroup_name())) {
            return ApiResult.error("该策略组名称已被使用！");
        } else if (null != alertRuleStrategyReqVO.getId() && !ruleService.checkGroupName(alertRuleStrategyReqVO.getId(), alertRuleStrategyReqVO.getGroup_name())) {
            return ApiResult.error("该策略组名称已被使用！");
        }
        return ruleService.addStrategy(alertRuleStrategyReqVO);
    }


    /**
     * 重点线路策略启用停用
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/guardline/updateStatus", method = {RequestMethod.POST})
    public ApiResult updateStatus(@RequestBody AlertRuleStrategyReqVO alertRuleStrategyReqVO) {
        return ruleService.updateStatus(alertRuleStrategyReqVO);
    }


    @ApiOperation(value = "/guardline_init", notes = "设备综合查询初始化")
    @RequestMapping(value = "/guardlineInit", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult guardlineInit() {
        List<SelectVO> alertLevel = constantItemService.findItemsByDict(MODULE_ALERT_LEVEL);
        List<SelectVO> intervalTime = constantItemService.findItemsByDict(MODULE_ALERT_INTERVAL_TIME);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("alertLevel", alertLevel)
                .put("interval", intervalTime)
                .build();
        return ApiResult.success(result);
    }

}
