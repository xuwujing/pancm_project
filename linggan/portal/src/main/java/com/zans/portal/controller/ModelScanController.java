package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.zans.base.annotion.Record;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.TDeviceModelScan;
import com.zans.portal.service.IConstantItemService;
import com.zans.portal.service.IDeviceTypeService;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.IModelScanService;
import com.zans.portal.vo.model.ModelScanAddVO;
import com.zans.portal.vo.model.ModelScanEditVO;
import com.zans.portal.vo.model.ModelScanRespVO;
import com.zans.portal.vo.model.ModelScanSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@Api(value = "/model/scan", tags = {"/model/scan ~ 设备端口模型管理"})
@RestController
@Validated
@RequestMapping("/model/scan")
@Slf4j
public class ModelScanController extends BasePortalController {

    @Autowired
    IModelScanService modelScanService;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IConstantItemService constantItemService;



    @ApiOperation(value = "/list", notes = "设备端口模型查询")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "req", value = "查询条件", required = true,
                    dataType = "ModelScanSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<ModelScanRespVO>> getPageList(@RequestBody ModelScanSearchVO req) {
        log.info("getModelList#{}", req);
        PageResult<ModelScanRespVO> pageResult = modelScanService.getModelPage(req);
        return ApiResult.success(pageResult);
    }

    @ApiOperation(value = "/init", notes = "设备端口模型查询初始化")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult initPage() {
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> muteList = constantItemService.findItemsByDict(MODULE_ARP_MUTE);
        List<SelectVO> confirmList = constantItemService.findItemsByDict(MODULE_MODEL_SCAN_CONFIRM);
        List<SelectVO> sourceList = constantItemService.findItemsByDict(MODULE_MODEL_SCAN_SOURCE);

        ModelScanSearchVO req = new ModelScanSearchVO();


        PageResult<ModelScanRespVO> pageResult = modelScanService.getModelPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_DEVICE, deviceList)
                .put(MODULE_MODEL_SCAN_CONFIRM, confirmList)
                .put(MODULE_MODEL_SCAN_SOURCE, sourceList)
                .put(MODULE_ARP_MUTE, muteList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/view", notes = "设备端口模型详细信息")
    @ApiImplicitParam(name = "id", value = "id", required = true,
            dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<ModelScanRespVO> getModelById(@NotNull(message="id必填")  Integer id) {
        ModelScanRespVO model = modelScanService.getModelById(id);
        if (model == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("端口模型不存在#" + id);
        }

        return ApiResult.success(model);
    }

    @ApiOperation(value = "/delete", notes = "设备端口模型删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_MODEL_SCAN,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message="id必填")  Integer id, HttpServletRequest request) {
        TDeviceModelScan model = modelScanService.getById(id);
        if (model == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("端口模型不存在#" + id);
        }
        String content = JSON.toJSONString(model);
        modelScanService.delete(model);

        // 记录日志


        return ApiResult.success();
    }

    @ApiOperation(value = "/add", notes = "设备端口模型新增")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_MODEL_SCAN,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult add(@Valid @RequestBody ModelScanAddVO vo,
                          HttpServletRequest request) {

        TDeviceModelScan model = new TDeviceModelScan();
        BeanUtils.copyProperties(vo, model);
        model.setConfirm(GlobalConstants.MODEL_SCAN_CONFIRM_NO);
        model.setInsertSource(GlobalConstants.MODEL_SCAN_SOURCE_MANUAL);
        model.setInsertTime(DateHelper.parseDatetime(vo.getInsertTime()));
        modelScanService.save(model);


        return ApiResult.success(MapBuilder.getSimpleMap("id", model.getId()));
    }

    @ApiOperation(value = "/edit", notes = "设备端口模型修改")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_MODEL_SCAN,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult edit(@Valid @RequestBody ModelScanEditVO editVO,
                          HttpServletRequest request) {
        int id = editVO.getId();
        TDeviceModelScan model = modelScanService.getById(id);
        if (model == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("端口模型不存在#" + id);
        }
        BeanUtils.copyProperties(editVO, model);
        model.setInsertTime(DateHelper.parseDatetime(editVO.getInsertTime()));
        modelScanService.update(model);


        return ApiResult.success(MapBuilder.getSimpleMap("id", id));
    }

}
