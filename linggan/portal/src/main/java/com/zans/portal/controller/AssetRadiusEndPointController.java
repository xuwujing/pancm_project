package com.zans.portal.controller;

import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.SysBrandMapper;
import com.zans.portal.dao.SysCustomFieldDao;
import com.zans.portal.model.SysCustomField;
import com.zans.portal.service.*;
import com.zans.portal.vo.arp.AssetRespVO;
import com.zans.portal.vo.arp.AssetSearchVO;
import com.zans.portal.vo.common.TreeSelect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.BaseConstants.DATE_RANGE_ARRAY_SIZE;
import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;

@Api(value = "/asset/radius/endpoint", tags = {"/asset/radius/endpoint ~ 资产管控详情数据"})
@RestController
@RequestMapping("/asset/radius/endpoint")
@Validated
@Slf4j
public class AssetRadiusEndPointController extends BasePortalController {

    @Autowired
    IRadiusEndPointService radiusEndPointService;
    @Autowired
    IDeviceTypeService deviceTypeService;
    @Autowired
    IAreaService areaService;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    RadiusQzController radiusQzController;
    @Autowired
    private IConstantItemService constantItemService;

    @Autowired
    private ISysConstantService sysConstantService;

    @Resource
    private SysCustomFieldDao sysCustomFieldDao;

    @Autowired
    IAssetService assetService;

    @Resource
    private SysBrandMapper sysBrandMapper;


    @ApiOperation(value = "/deviceDiscovery/list", notes = "设备发现列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "EndPointReqVO", paramType = "body")
    })
    @RequestMapping(value = "/deviceDiscovery/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetRespVO>> deviceDiscoveryList(@RequestBody AssetSearchVO req) {
        SelectVO selectVO = sysConstantService.findSelectVOByKey(ASSET_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());
        List<String> dateRange = req.getAliveDateRange();
        if (dateRange != null && dateRange.size() == DATE_RANGE_ARRAY_SIZE) {
            req.setAliveStartDate(dateRange.get(0));
            req.setAliveEndDate(dateRange.get(1));
        }
        req.setAssetSource(2);
        PageResult<AssetRespVO> pageResult = assetService.getAssetNewListPage(req);
        return ApiResult.success(pageResult);
    }


    @ApiOperation(value = "/nanotubes/list", notes = "纳管设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "EndPointReqVO", paramType = "body")
    })
    @RequestMapping(value = "/nanotubes/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetRespVO>> nanotubesList(@RequestBody AssetSearchVO req) {

        SelectVO selectVO = sysConstantService.findSelectVOByKey(ASSET_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());
        List<String> dateRange = req.getAliveDateRange();
        if (dateRange != null && dateRange.size() == DATE_RANGE_ARRAY_SIZE) {
            req.setAliveStartDate(dateRange.get(0));
            req.setAliveEndDate(dateRange.get(1));
        }
        req.setAssetManage(1);
        PageResult<AssetRespVO> pageResult = assetService.getAssetNewListPage(req);
        return ApiResult.success(pageResult);
    }


    @ApiOperation(value = "/noNanotubes/list", notes = "非纳管设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "EndPointReqVO", paramType = "body")
    })
    @RequestMapping(value = "/noNanotubes/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetRespVO>> noNanotubesList(@RequestBody AssetSearchVO req) {

        SelectVO selectVO = sysConstantService.findSelectVOByKey(ASSET_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());
        List<String> dateRange = req.getAliveDateRange();
        if (dateRange != null && dateRange.size() == DATE_RANGE_ARRAY_SIZE) {
            req.setAliveStartDate(dateRange.get(0));
            req.setAliveEndDate(dateRange.get(1));
        }
        req.setAssetManage(2);
        PageResult<AssetRespVO> pageResult = assetService.getAssetNewListPage(req);
        return ApiResult.success(pageResult);
    }




    private Map<String, String> getFiledMap(SysCustomField sysCustomField, Map<String, String> data, Integer enable) {
        List<SysCustomField> sysCustomFields = sysCustomFieldDao.queryAll(sysCustomField);
        if(!org.springframework.util.StringUtils.isEmpty(sysCustomFields)){
            //这个方法的排序
//            data = sysCustomFields.stream().collect(Collectors.toMap(SysCustomField::getFieldKey, SysCustomField::getFieldName));
            for (SysCustomField customField : sysCustomFields) {
                data.put(customField.getFieldKey(),customField.getFieldName());
            }
            //如果未开启漏扫，则移除漏扫的字段
            if(enable == null || enable ==0){
                data.remove("retLevel");
                data.remove("suggest");
            }

        }
        return data;
    }

    @ApiOperation(value = "/list_init", notes = "设备综合查询初始化")
    @RequestMapping(value = "/list_init", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult init() {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();

        List<TreeSelect>  deviceTypeParentList = deviceTypeService.deviceTypeTreeList();


        List<SelectVO> deviceBrandList = sysBrandMapper.findBrand();
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
                .put(MODULE_DEVICE_BRAND, deviceBrandList)
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
        AssetRespVO asset = assetService.getAssetNewDynamicDetail(id,ipAddr);
        if (asset == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("ip.id isn't existed#" + id);
        }
//        asset.setMaintainStatusName(asset.getMaintainStatus() == null ? "" : (asset.getMaintainStatus() == 1 ? "正常" : (asset.getMaintainStatus() == 2 ? "迁改停用" : "审核停用")));
        asset.setMaintainStatusName(asset.getMaintainStatus() == null ? "" : asset.getMaintainStatus());
        return ApiResult.success(asset);
    }


    private String getPortId(String nasPortId) {
        String[] portIds = nasPortId.split(";");
        if (portIds.length < 3) {
            return nasPortId;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            String sp = portIds[i];
            if (i > 0) {
                sb.append("/");
            }
            sb.append(substringAfter(sp, "="));
        }
        return sb.toString();
    }

    private String substringAfter(final String str, final String separator) {
        if (org.springframework.util.StringUtils.isEmpty(str)) {
            return "";
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }




}
