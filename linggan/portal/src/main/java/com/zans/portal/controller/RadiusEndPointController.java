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
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.SysCustomFieldDao;
import com.zans.portal.model.RadiusEndpoint;
import com.zans.portal.model.SysCustomField;
import com.zans.portal.service.*;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.radius.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/radius/endpoint", tags = {"/radius/endpoint ~ radius核心（在线、离线、检疫）"})
@RestController
@RequestMapping("/radius/endpoint")
@Validated
@Slf4j
public class RadiusEndPointController extends BasePortalController {

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
    IFileService fileService;

    @Value("${api.export.folder}")
    String exportFolder;
    @Value("${api.upload.folder}")
    String uploadFolder;


    @ApiOperation(value = "/list", notes = "radius 在线、离线、检疫列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "EndPointReqVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody EndPointReqVO req) {

        SelectVO selectVO = sysConstantService.findSelectVOByKey(ACCESS_PASS_SORT);
        super.checkPageParams(req, "");
        req.setOrderString(selectVO.getItemValue());

        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> detectType = constantItemService.findItemsByDict(MODULE_DETECT_TYPE);
        List<SelectVO> assetSource = constantItemService.findItemsByDict(MODULE_ASSET_SOURCE);
        List<SelectVO> assetManage = constantItemService.findItemsByDict(MODULE_ASSET_MANAGE);
        if (req.getMac() != null) {
            req.setMac(req.getMac().replaceAll(" ", ""));
        }
        PageResult<EndPointViewVO> pageResult = radiusEndPointService.getEndPointPage(req);

        List<EndPointViewVO> list = pageResult.getList();
        list.forEach(e -> {
            if (StringUtils.isNotEmpty(e.getAcctSessionTime())) {
                String second = StringHelper.secondToTime(String.valueOf(e.getAcctSessionTime()));
                e.setAcctSessionTime(second);
            }
        });
        Integer policy = req.getAccessPolicy();
        SysCustomField sysCustomField = new SysCustomField();
        sysCustomField.setFiledEnable(1);
        if(policy != null && policy == GlobalConstants.RADIUS_ACCESS_POLICY_BLOCK){
            sysCustomField.setModuleName(MODULE_NAME_ACCESS_BLOCK);
        }else{
            sysCustomField.setModuleName(MODULE_NAME_ACCESS_PASS);
        }
        SelectVO selectVO2 = sysConstantService.findSelectVOByKey(OTHER_MISSION);
        Integer enable = Integer.valueOf(selectVO2.getItemValue());
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

    @ApiOperation(value = "/view", notes = "查看设备详情")
    @ApiImplicitParam(name = "id", value = "地址id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult getEndPointById(@RequestParam("id") Integer id,
                                     @RequestParam(value = "arpId", required = false) Integer arpId) {
        //2020-9-28 在线设备和阻断设备详情显示改为和检疫区一致
        QzRespVO respVO = radiusEndPointService.findQzById(id);
        if (respVO == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("检疫区不存在#" + id);
        }
        String nasPortId = respVO.getNasPortId();
        if (!org.springframework.util.StringUtils.isEmpty(nasPortId)) {
            nasPortId = nasPortId.replace("3D", "");
            nasPortId = nasPortId.replace("=3B", ";");
            nasPortId = getPortId(nasPortId);
            respVO.setNasPortId(nasPortId);
        }

        Map<String, Object> resultMap = radiusQzController.getIpAndAssetsCp(respVO);
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


    @ApiOperation(value = "/delete", notes = "删除")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_RADIUS, operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult deleteAcctList(@RequestParam("id") Integer id, HttpServletRequest request) {
        return radiusEndPointService.delete(id);

    }


    @ApiOperation(value = "/batchDelete", notes = "批量删除")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")
    @RequestMapping(value = "/batchDelete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_RADIUS, operation = PORTAL_LOG_OPERATION_BATCH_DELETE)
    public ApiResult batchDeleteAcctList(@RequestBody EndPointBatchReqVO reqVO, HttpServletRequest request) {
        String[] ids = reqVO.getIds().split(",");
        for (String id : ids) {
            RadiusEndpoint endpoint = radiusEndPointService.getById(Integer.parseInt(id));
            endpoint.setDeleteStatus(1);
            radiusEndPointService.update(endpoint);
            // 记录日志
        }

        return ApiResult.success().appendMessage("清除成功");
    }


    @ApiOperation(value = "/dm", notes = "强制下线")
    @ApiImplicitParam(name = "mac", value = "mac", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/dm", method = {RequestMethod.POST, RequestMethod.GET})
    @Transactional(rollbackFor = Exception.class)
    @Record(module = PORTAL_MODULE_RADIUS, operation = LOG_OPERATION_FORCE_OFFLINE)
    public ApiResult dm(@RequestParam("mac") String mac, HttpServletRequest request) {
        ApiResult rad = radiusEndPointService.doDm(mac, 1);
//        if (rad == null) {
//            return ApiResult.error("执行dm异常");
//        }
//        if (rad.getCode() != CODE_SUCCESS) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return rad;
//        }

        // 记录日志

        return ApiResult.success().appendMessage("清除成功");
    }


    @ApiOperation(value = "/batchDm", notes = "强制下线")
    @ApiImplicitParam(name = "mac", value = "mac", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/batchDm", method = {RequestMethod.POST, RequestMethod.GET})
    @Transactional(rollbackFor = Exception.class)
    @Record(module = PORTAL_MODULE_RADIUS, operation = LOG_OPERATION_FORCE_OFFLINE)
    public ApiResult batchDm(@RequestParam("policy") Integer policy, @RequestBody String macs, HttpServletRequest request) {

        String[] macS = macs.split(",");
        for (String mac : macS) {
            ApiResult rad = radiusEndPointService.doDm(mac, policy);
//            if (rad == null) {
//                return ApiResult.error("执行dm异常");
//            }
//            if (rad.getCode() != CODE_SUCCESS) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return rad;
//            }
            // 记录日志

        }

        return ApiResult.success().appendMessage("清除成功");
    }


    /***
     * 审核处置
     * @param id
     * @param policy  0:阻断; 1:检疫区; 2:放行
     * @param request
     * @return
     */
    @ApiOperation(value = "/judge", notes = "审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "policy", value = "policy", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "authMark", value = "authMark", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/judge", method = {RequestMethod.POST, RequestMethod.GET})
    @Record(module = PORTAL_MODULE_RADIUS, operation = PORTAL_LOG_OPERATION_APPROVE, type = PORTAL_MODULE_TYPE_JUDGE)
    public ApiResult judge(@RequestParam("id") Integer id,
                           @RequestParam("policy") Integer policy,
                           @RequestParam("authMark") String authMark,
                           @RequestParam("mac") String mac,
                           HttpServletRequest request) {
        ApiResult result = radiusEndPointService.syncJudge(id, policy, authMark, getUserSession(request));
        return result;
    }


    /***
     * 批量审核处置
     * @param request
     * @return
     */
    @ApiOperation(value = "/batchJudge", notes = "批量审核")
    @RequestMapping(value = "/batchJudge", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_RADIUS, operation = PORTAL_LOG_OPERATION_BATCH_JUDGE, type = PORTAL_MODULE_TYPE_JUDGE_BATCH)
    public ApiResult batchJudge(@RequestBody EndPointBatchReqVO reqVO,
                                HttpServletRequest request) {
        reqVO.setUsername(getUserSession(request).getUserName());
        return radiusEndPointService.batchJudge(reqVO);
    }

    @ApiOperation(value = "/verify ", notes = "进行校验")
    @ApiImplicitParam(name = "id", value = "地址id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/verify", method = {RequestMethod.GET})
    public ApiResult verify(@RequestParam("id") Integer id) {
        QzRespVO respVO = radiusEndPointService.findQzById(id);
        if (respVO == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("检疫区不存在#" + id);
        }
        ApiResult apiResult = radiusEndPointService.verify(respVO);
        return apiResult;
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
