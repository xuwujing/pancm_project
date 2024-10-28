package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.RadiusNas;
import com.zans.portal.service.IAreaService;
import com.zans.portal.service.IDeviceTypeService;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.IRadiusNasService;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.vo.radius.RadiusNasRespVO;
import com.zans.portal.vo.radius.RadiusNasSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/radius/nas", tags = {"/radius/nas ~ 区域接入点"})
@RestController
@RequestMapping("/radius/nas")
@Validated
@Slf4j
public class RadiusNasController extends BasePortalController {

    @Autowired
    IRadiusNasService radiusNasService;
    @Autowired
    IAreaService areaService;
    @Autowired
    IDeviceTypeService deviceTypeService;
    @Autowired
    ILogOperationService logOperationService;
    @Autowired
    RestTemplateHelper restTemplateHelper;


    @ApiOperation(value = "/list", notes = "区域接入点查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "RadiusNasSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody RadiusNasSearchVO req) {
        super.checkPageParams(req, "id desc");

        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> area = areaService.findAreaToSelect();
        PageResult<RadiusNasRespVO> pageResult = radiusNasService.getRadiusNasPage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_DEVICE, deviceList)
                .put(INIT_DATA_TABLE, pageResult)
                .put(MODULE_AREA, area)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/insertOrUpdate", notes = "新增或修改区域接入点")
    @ApiImplicitParam(name = "mergeVO", value = "新增或修改区域接入点", required = true,
            dataType = "RadiusNasRespVO", paramType = "body")
    @RequestMapping(value = "/insertOrUpdate", method = {RequestMethod.POST})
    @Transactional(rollbackFor = Exception.class)
    @Record(module = PORTAL_MODULE_RADIUS_NAS,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insertOrUpdate(@Valid @RequestBody RadiusNasRespVO mergeVO, HttpServletRequest request) {
        RadiusNas nas = new RadiusNas();
        BeanUtils.copyProperties(mergeVO, nas);
        nas.setDeleteStatus(mergeVO.getDeleteStatus() == 0 ? false : true);

        RadiusNas nasByName = radiusNasService.getByNameOrNasIp(nas.getName(), "", mergeVO.getId());
        RadiusNas nasByIp = radiusNasService.getByNameOrNasIp("", nas.getNasIp(), mergeVO.getId());
        if (nasByName != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("#区域接入点名称已存在" + nas.getName());
        }
        if (nasByIp != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("#区域接入点NasIp已存在" + nas.getNasIp());
        }

        if (nas.getId() == null) {
            radiusNasService.insert(nas);
        } else {
            radiusNasService.update(nas);
        }

        //调用rad_api接口 2020-10-21 暂时注掉走单表
//        ApiResult result = restTemplateHelper.getForObject(nodeurl, 30 * 1000);
//        //2020-9-7 修复JSONObject强转LinkedHashMap的bug
//        JSONObject jsonObject = (JSONObject) result.getData();
//        LinkedHashMap data = JSON.parseObject(JSONObject.toJSONString(jsonObject), (Type) LinkedHashMap.class);
//
//        if (result.getCode() != 0) {
//            return ApiResult.error("获取rad_api接口异常");
//        }

        ///2020-9-15 和北辰确认，襄阳项目远程调用都禁用
//        JSONObject jsonObject2 = (JSONObject) data.get("rad_api");
//        LinkedHashMap rad_api= JSON.parseObject(JSONObject.toJSONString(jsonObject2), (Type) LinkedHashMap.class);
//        String ip = (String) rad_api.get("ip");
//        Integer port = (Integer) rad_api.get("port");
//        String serverUrl = "http://" + ip + ":" + port;
//        serverUrl = serverUrl + "/sync/nas?ip=" + nas.getNasIp() + "&delete_status=" + nas.getDeleteStatus();
//        result = restTemplateHelper.getForObject(serverUrl, 2 * 60 * 1000);


        return ApiResult.success(MapBuilder.getSimpleMap("id", nas.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/delete", notes = "禁用")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Transactional(rollbackFor = Exception.class)
    @Record(module = PORTAL_MODULE_RADIUS_NAS,operation = PORTAL_MODULE_EN_ABLE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, @NotNull(message = "禁用启用") Integer type, HttpServletRequest request) {
        RadiusNas nas = radiusNasService.getById(id);
        if (nas == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("交换机不存在#" + id);
        }
        nas.setDeleteStatus(type == 0 ? false : true);
        radiusNasService.update(nas);

        //调用rad_api接口 2020-10-21 暂时注掉走单表
//        ApiResult result = restTemplateHelper.getForObject(nodeurl, 30 * 1000);
//        //2020-9-7 修复JSONObject强转LinkedHashMap的bug
//        JSONObject jsonObject = (JSONObject) result.getData();
//        LinkedHashMap data = JSON.parseObject(JSONObject.toJSONString(jsonObject), (Type) LinkedHashMap.class);
//        if (result.getCode() != 0) {
//            return ApiResult.error("获取rad_api接口异常");
//        }


        ///2020-9-15 和北辰确认，襄阳项目远程调用都禁用
//        JSONObject jsonObject2 = (JSONObject) data.get("rad_api");
//        LinkedHashMap rad_api= JSON.parseObject(JSONObject.toJSONString(jsonObject2), (Type) LinkedHashMap.class);
//        String ip = (String) rad_api.get("ip");
//        Integer port = (Integer) rad_api.get("port");
//        String serverUrl = "http://" + ip + ":" + port;
//        serverUrl = serverUrl + "/sync/nas?ip=" + nas.getNasIp() + "&delete_status=" + nas.getDeleteStatus();
//        result = restTemplateHelper.getForObject(serverUrl, 2 * 60 * 1000);

        // 记录日志

        return ApiResult.success().appendMessage("禁用成功");
    }



}
