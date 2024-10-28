package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.AlertRuleMapper;
import com.zans.portal.dao.RadiusPolicyConfigDao;
import com.zans.portal.dao.SysCustomFieldDao;
import com.zans.portal.model.DeviceType;
import com.zans.portal.model.RadiusPolicyConfig;
import com.zans.portal.model.SysCustomField;
import com.zans.portal.model.Universality;
import com.zans.portal.service.*;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.vo.alert.AlertRecordRespVO;
import com.zans.portal.vo.alert.AlertRecordSearchVO;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.radius.*;
import com.zans.portal.vo.switcher.SwitcherVlanConfigRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;


@Api(value = "/radius/qz", tags = {"/radius/qz ~ radius 隔离区"})
@RestController
@RequestMapping("/radius/qz")
@Slf4j
public class RadiusQzController extends BasePortalController {

    @Autowired
    IRadiusEndPointService radiusEndPointService;
    @Autowired
    IDeviceTypeService deviceTypeService;
    @Autowired
    IIpService ipService;
    @Autowired
    IArpService arpService;
    @Autowired
    IAreaService areaService;
    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    ISysSwitcherVlanConfigService sysSwitcherVlanConfigService;

    @Autowired
    IAssetService iAssetService;

    @Autowired
    private AlertRuleMapper mapper;

    @Autowired
    private ISysConstantService sysConstantService;

    @Autowired
    private IConstantItemService constantItemService;

    @Resource
    private RadiusPolicyConfigDao radiusPolicyConfigDao;

    @Autowired
    RestTemplateHelper restTemplateHelper;

    @Resource
    private SysCustomFieldDao sysCustomFieldDao;

    @Autowired
    IFileService fileService;

    @Value("${api.export.folder}")
    String exportFolder;
    @Value("${api.upload.folder}")
    String uploadFolder;


    @ApiOperation(value = "/list", notes = "隔离区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "QzReqVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult getList(@RequestBody QzReqVO req) {
        SelectVO selectVO = sysConstantService.findSelectVOByKey(ACCESS_QZ_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());

        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> detectType = constantItemService.findItemsByDict(MODULE_DETECT_TYPE);
        List<SelectVO> assetSource = constantItemService.findItemsByDict(MODULE_ASSET_SOURCE);
        List<SelectVO> assetManage = constantItemService.findItemsByDict(MODULE_ASSET_MANAGE);
        if (req.getMac() != null) {
            req.setMac(req.getMac().replaceAll(" ", ""));
        }
        PageResult<EndPointViewVO> pageResult = radiusEndPointService.getQzEndPointPage(req);
        Map<Object, String> stringMap = constantItemService.findItemsMapByDict("hint_status");
        List<RadiusPolicyConfig> radiusPolicyConfigList = radiusPolicyConfigDao.queryAll(new RadiusPolicyConfig());
        List<String> list = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        //漏扫开关
        SelectVO selectVO2 = sysConstantService.findSelectVOByKey(OTHER_MISSION);
        Integer enable = Integer.valueOf(selectVO2.getItemValue());
        for (EndPointViewVO endPointViewVO : pageResult.getList()) {
            list.add(endPointViewVO.getIpAddr());
            if(enable!=null && enable ==1){
                checkReach(stringMap, radiusPolicyConfigList, endPointViewVO);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("检疫区的放行建议检查时间花费:{} ms",endTime-startTime);
        SysCustomField sysCustomField = new SysCustomField();
        sysCustomField.setFiledEnable(1);
        sysCustomField.setModuleName(MODULE_NAME_ACCESS_QZ);
        sysCustomField.setFieldType(1);
        Map<String, String> data = new LinkedHashMap<>();
        data = getFiledMap(sysCustomField, data,enable);
        Map<String, String> queryData = new LinkedHashMap<>();
        sysCustomField.setFieldType(2);
        queryData = getFiledMap(sysCustomField, queryData,enable);

        List<SelectVO> selectVOList =  constantItemService.findItemsByDict("ret_level");
        if(!StringHelper.isEmpty(selectVOList)) {
            selectVOList.forEach(selectVO1 -> {
                selectVO1.setItemKey(selectVO1.getItemValue());
            });
        }
        List<TreeSelect>  deviceTypeParentList = deviceTypeService.deviceTypeTreeList();


        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .put(MODULE_DEVICE, deviceList)
                .put(MODULE_DEVICE_DETECT, deviceList)
                .put("device_parent_tree",deviceTypeParentList)
                .put(CUSTOM_COLUMN, data)
                .put(CUSTOM_QUERY, queryData)
                .put(MODULE_DETECT_TYPE, detectType)
                .put(MODULE_ASSET_SOURCE, assetSource)
                .put(MODULE_ASSET_MANAGE, assetManage)
                .put("retLevel", selectVOList)
                .build();
        return ApiResult.success(result);
    }

    /**
     * 获取字段列表数据
     * @param sysCustomField
     * @param data
     * @param enable
     * @return
     */
    private Map<String, String> getFiledMap(SysCustomField sysCustomField, Map<String, String> data, Integer enable) {
        List<SysCustomField> sysCustomFields = sysCustomFieldDao.queryAll(sysCustomField);
        if(!StringUtils.isEmpty(sysCustomFields)){
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

    /**
    * @Author beixing
    * @Description  判断放行状态
    * @Date  2021/9/23
    * @Param
    * @return
    **/
    private void checkReach(Map<Object, String> stringMap, List<RadiusPolicyConfig> radiusPolicyConfigList, EndPointViewVO endPointViewVO) {
        /**
         * 对设备的数据提供建议
         * 1.首先查询准入规则表，得到前置条件，不符合前置条件则进行下一轮的或判断；
         * 2.在后续的判断中，只要有一个符合就提示 可放行，否则提示基线不一致
         */
        for (RadiusPolicyConfig radiusPolicyConfig : radiusPolicyConfigList) {
            int level = radiusPolicyConfig.getLevel();
            int hintStatus = radiusPolicyConfig.getHintStatus();
            int result = radiusPolicyConfig.getResult();
            String name = radiusPolicyConfig.getRuleName();
            String sql = radiusPolicyConfig.getRule();
            String ip = endPointViewVO.getIpAddr();
            int otherScanStatus = endPointViewVO.getOtherScanStatus();
            if (StringHelper.isEmpty(ip)) {
                continue;
            }
            sql = String.format(sql, ip);
            List<Map<String, String>> mapList = radiusPolicyConfigDao.executeSql(sql);
            final boolean isPre = (level == 0 && otherScanStatus == 0 );
            //第三方的前置条件判断
            if (isPre) {
                hintStatus = 3;
                endPointViewVO.setPolicyRuleName(name);
                endPointViewVO.setHitStatus(hintStatus);
                endPointViewVO.setSuggest(stringMap.get(hintStatus));
                break;
            }
            //判断是否是有第三方的漏洞，otherScanStatus=2表示有风险
            final boolean isBug = (level == 0 && otherScanStatus == 2 );
            if (isBug) {
//                hintStatus = 2;
                endPointViewVO.setPolicyRuleName(name);
                endPointViewVO.setHitStatus(hintStatus);
                endPointViewVO.setSuggest(stringMap.get(hintStatus));
                break;
            }
            //判断是否符合自身的问题
            final boolean isReach = ((result == 1 && !StringHelper.isEmpty(mapList)) || result == 0 && StringHelper.isEmpty(mapList));
            if (isReach) {
                endPointViewVO.setPolicyRuleName(name);
                endPointViewVO.setHitStatus(hintStatus);
                endPointViewVO.setSuggest(stringMap.get(hintStatus));
                break;
            }

        }
    }

    @ApiOperation(value = "/delete", notes = "删除隔离区数据")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_QZ, operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {

        return radiusEndPointService.delete(id);
    }

    @ApiOperation(value = "/dpApi", notes = "迪普的API")
    @RequestMapping(value = "/dpApi", method = {RequestMethod.GET})
    public ApiResult dpApi(String mac, String ipAddr,HttpServletRequest request) {
        //调用scan的dp服务接口
        SelectVO selectVO = sysConstantService.findSelectVOByKey(SCAN_DP_API);
        String value = selectVO.getItemValue();
        String url = value+"?"+"mac="+mac+"&ipAddr="+ipAddr;
        try {
            log.info("调用scan的迪普接口请求参数:{}",url);
            restTemplateHelper.getForObject(url,30);
        }catch (Exception e){
            log.error("调用scan的迪普接口失败!请求参数:{},原因:",url,e);
            return ApiResult.error("调用scan的迪普接口失败!");
        }
        return ApiResult.success();
    }


    @ApiOperation(value = "/view", notes = "隔离区详情")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<QzRespVO> view(@NotNull(message = "id必填") Integer id) {

        QzRespVO respVO = radiusEndPointService.findQzById(id);
        if (respVO == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("隔离区不存在#" + id);
        }
        String nasPortId = respVO.getNasPortId();
        if (!StringUtils.isEmpty(nasPortId)) {
            nasPortId = nasPortId.replace("3D", "");
            nasPortId = nasPortId.replace("=3B", ";");
            nasPortId = getPortId(nasPortId);
            respVO.setNasPortId(nasPortId);
        }

        Map<String, Object> resultMap = getIpAndAssetsCp(respVO);
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("radiusQz", respVO)
                .build();
        map.putAll(resultMap);
        return ApiResult.success(map);
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

    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author pancm
     * @Description 获取IP和资产对比信息数据
     * @Date 2020/9/28
     * @Param [respVO]
     **/
    public Map<String, Object> getIpAndAssetsCp(QzRespVO respVO) {
        List<QzViewRespVO> ipView = new ArrayList<>();
        List<QzViewRespVO> arpView = new ArrayList<>();
        //校验对比的数据
        List<Universality> verifyView = new ArrayList<>();

        //2020-10-29 和北辰确认ip信息对比查询由t_ip_all改为查询asset表
        //2020-11-6 如果 t_ip_all表的数据为空，那么就查询asset
        //Ip对比
        QzViewRespVO ipRes = new QzViewRespVO();
        ipRes = ipService.findByIp(respVO.getIpAddr());
        if (ipRes == null) {
            ipRes = iAssetService.findByIp(respVO.getIpAddr());
        }
        QzViewRespVO qzRes = new QzViewRespVO();
        if (ipRes == null) {
            ipRes = new QzViewRespVO();
            qzRes.setIpStatus(0);
            ipRes.setIpStatus(0);
            respVO.setIsIpNotAllocErr(1);
            Universality universality = new Universality();
            universality.setName("ip未分配");
            universality.setValue(5);
            verifyView.add(universality);
        }

        //2020-12-16 列表页增加vlan配置错误的提示
        String vlan = ipRes.getVlan();
        String curVlan = respVO.getVlan();
        if (!StringUtils.isEmpty(vlan) && !StringUtils.isEmpty(curVlan)) {
            if (!vlan.equals(curVlan)) {
                respVO.setIsVlanErr(1);
                Universality universality = new Universality();
                universality.setName("Vlan错误");
                universality.setValue(1);
                verifyView.add(universality);
            }
        }
        if (StringUtils.isEmpty(ipRes.getVlan())) {
            ipRes.setVlan("-");
        }

        String osName = ipRes.getServerOs();
        String curOsName = respVO.getAreaName();
        if (!StringUtils.isEmpty(osName) && !StringUtils.isEmpty(curOsName)) {
            if (!osName.equals(curOsName)) {
                respVO.setIsOsErr(1);
                Universality universality = new Universality();
                universality.setName("设备操作类型");
                universality.setValue(3);
                verifyView.add(universality);
            }
        }

        String modelDes = ipRes.getDeviceModelDes();
        String curModelDes = respVO.getModelDes();
        String brandName = ipRes.getDeviceModelBrand();
        String curBrandName = respVO.getBrandName();
        final boolean isModelDesOrBrandNameErr = (!StringUtils.isEmpty(modelDes) && !StringUtils.isEmpty(curModelDes))
                || (!StringUtils.isEmpty(brandName) && !StringUtils.isEmpty(curBrandName));
        if (isModelDesOrBrandNameErr) {
            if ((!StringUtils.isEmpty(modelDes) && !modelDes.equals(curModelDes)) || (!StringUtils.isEmpty(brandName) && !brandName.equals(curBrandName))) {
                respVO.setIsModelDesErr(1);
                Universality universality = new Universality();
                universality.setName("设备型号");
                universality.setValue(4);
                verifyView.add(universality);
            }
        }


        BeanUtils.copyProperties(respVO, qzRes);
        qzRes.setModuleName("扫描设备信息");
        qzRes.setMac(respVO.getMac());
        qzRes.setDeviceModelDes(respVO.getModelDes());
        qzRes.setCurOpenPort(respVO.getOpenPortAll());
        qzRes.setDeviceModelBrand(respVO.getBrandName());
        qzRes.setPointName(ipRes.getPointName());
        qzRes.setProjectName(ipRes.getProjectName());
        qzRes.setMaintainCompany(ipRes.getMaintainCompany());
        qzRes.setContractorPerson(ipRes.getContractorPerson());
        qzRes.setContractorPhone(ipRes.getContractorPhone());
        qzRes.setVlan(ipRes.getVlan());
        Integer deviceType = respVO.getDeviceType();
        if (deviceType != null && deviceType != 0) {
            DeviceType device = deviceTypeService.getById(deviceType);
            if (device != null) {
                qzRes.setTypeName(device.getTypeName());
            }
        }

        //2020-9-11 增加逻辑，deviceType如果在上述条件查不到，将ipRes的数据放到
        if (deviceType == null && qzRes.getTypeName() == null) {
            qzRes.setTypeName(ipRes.getTypeName());
            qzRes.setDeviceModelDes(ipRes.getDeviceModelDes());
        }
        //资产对比
        // 2020-9-27 资产对比由t_arp表改为radius_endpoint_profile
        QzViewRespVO curRqs = radiusEndPointService.findCurByCurMac(respVO.getMac());
        QzViewRespVO baseRqs = radiusEndPointService.findBaseByCurMac(respVO.getMac());

        // 2020-10-26 修改这个ip资产对比的顺序
        ipView.add(ipRes);
        ipView.add(qzRes);

        if (curRqs == null) {
            curRqs = new QzViewRespVO();
            curRqs.setArpStatus(0);
        } else {
            curRqs.setNasIp(respVO.getNasIp());
            curRqs.setNasName(respVO.getNasName());
            curRqs.setNasPort(respVO.getNasPort());
            curRqs.setNasPortType(respVO.getNasPortType());
            curRqs.setNasPortId(respVO.getNasPortId());
            curRqs.setCalledStationId(respVO.getCalledStationId());
        }

        if (baseRqs == null) {
            baseRqs = new QzViewRespVO();
            baseRqs.setArpStatus(0);
        }
        arpView.add(baseRqs);
        arpView.add(curRqs);
        if (!StringUtils.isEmpty(respVO.getIpAddr())) {
            AlertRecordSearchVO reqVO = new AlertRecordSearchVO();
            reqVO.setIpAddr(respVO.getIpAddr());
            reqVO.setIndexId(12);
            List<AlertRecordRespVO> list = mapper.getAlertRecord(reqVO);
            reqVO.setIndexId(17);
            List<AlertRecordRespVO> list2 = mapper.getAlertRecord(reqVO);
            if ((list != null && list.size() > 0) || (list2 != null && list2.size() > 0)) {
                Universality universality = new Universality();
                universality.setName("ip冲突");
                universality.setValue(6);
                verifyView.add(universality);
            }
        }

        //交换机配置的 VLAN 地址范围(当前ip应该在哪个交换机哪个VLAN上)
//        List<SwitcherVlanConfigRespVO> vlanView = sysSwitcherVlanConfigService.getIpMatchVlanList(respVO.getIpAddr());
        List<SwitcherVlanConfigRespVO> vlanView = new ArrayList<>(0);
        if (vlanView == null) {
            vlanView = new ArrayList<>(0);
        }
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("ipView", ipView)
                .put("arpView", arpView)
                .put("vlanView", vlanView)
                .put("verifyView", verifyView)
                .build();
        return map;
    }

    @ApiOperation(value = "/check/alive", notes = "检疫区资产ping-是否在线")
    @RequestMapping(value = "/check/alive", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult checkAlive(@RequestBody QzReqVO req) {
        SelectVO selectVO = sysConstantService.findSelectVOByKey(ACCESS_QZ_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());

        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        if (req.getMac() != null) {
            req.setMac(req.getMac().replaceAll(" ", ""));
        }
        PageResult<EndPointViewVO> pageResult = radiusEndPointService.getQzEndPointPage(req);
        Map<Object, String> stringMap = constantItemService.findItemsMapByDict("hint_status");
        List<RadiusPolicyConfig> radiusPolicyConfigList = radiusPolicyConfigDao.queryAll(new RadiusPolicyConfig());
        List<String> list = new ArrayList<>();
        //漏扫开关
        SelectVO selectVO2 = sysConstantService.findSelectVOByKey(OTHER_MISSION);
        Integer enable = Integer.valueOf(selectVO2.getItemValue());
        for (EndPointViewVO endPointViewVO : pageResult.getList()) {
            list.add(endPointViewVO.getIpAddr());
            if(enable!=null && enable ==1){
                checkReach(stringMap, radiusPolicyConfigList, endPointViewVO);
            }

        }
        try {
//            radiusEndPointService.checkAlive(list,pageResult);
        } catch (Exception e) {
            log.warn("存活校验失败！error:{}", list,e);
        }
        Map<String, String> data = new HashMap<>(CUSTOM_COLUMN_MAP);
        data.put("authMark", "检疫原因");
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .put(MODULE_DEVICE, deviceList)
                .put(CUSTOM_COLUMN, data)
                .build();
        return ApiResult.success(result);

    }


    @ApiOperation(value = "/export", notes = "导出")
    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_RADIUS,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void export( @RequestBody EndPointReqVO req , HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("access downloadFile #===========");
        String filePath = this.exportFolder + "准入导出_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "准入";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";
        SelectVO selectVO = sysConstantService.findSelectVOByKey(ACCESS_QZ_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());
        req.setPageSize(100000);
        PageResult<EndPointViewVO> pageResult = radiusEndPointService.getEndPointPage(req);
        if (pageResult.getList().size()==0){
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }
        List<EndPointViewExportVO> list = new ArrayList<>();
        for (EndPointViewVO respVO : pageResult.getList()) {
            EndPointViewExportVO exportVO = new EndPointViewExportVO();
            BeanUtils.copyProperties(respVO,exportVO);
            list.add(exportVO);
        }
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }

}
