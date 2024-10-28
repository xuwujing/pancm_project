package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.service.IAlertOfflineDeviceService;
import com.zans.portal.service.IConstantItemService;
import com.zans.portal.service.IDeviceTypeService;
import com.zans.portal.vo.alert.offlineDevice.CountGroupVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceDisposeVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceResVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.PORTAL_LOG_OPERATION_BATCH;
import static com.zans.portal.constants.PortalConstants.PORTAL_MODULE_ALERT_OFFLINE;

@Api(value = "/alert/lost", tags = {"/alert/lost ~ 设备掉线列表"})
@RestController
@RequestMapping("/alert/lost")
@Validated
@Slf4j
public class AlertOfflineDeviceController extends BasePortalController {

    @Autowired
    private IAlertOfflineDeviceService alertOfflineDeviceService;

    @Autowired
    private IConstantItemService constantItemService;


    @Autowired
    IDeviceTypeService deviceTypeService;




    @ApiOperation(value = "/init", notes = "列表初始化")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult init() {
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        //是否可用
        List<SelectVO> enableStatus = constantItemService.findItemsByDict(GlobalConstants.SYS_DICT_KEY_ENABLE_STATUS);
        List<CountGroupVO> countList = new ArrayList<>();
        Date currDate = new Date();
        countList.add(CountGroupVO.builder().group(CountGroupVO.GROUP0).groupName(CountGroupVO.GROUP_NAME).count(alertOfflineDeviceService.countOfflineDeviceByTime(null,false)).build());
        countList.add(CountGroupVO.builder().group(CountGroupVO.GROUP3).groupName(CountGroupVO.GROUP_NAME3).count(alertOfflineDeviceService.countOfflineDeviceByTime(DateHelper.plusDays(currDate,-3),false)).build());
        countList.add(CountGroupVO.builder().group(CountGroupVO.GROUP7).groupName(CountGroupVO.GROUP_NAME7).count(alertOfflineDeviceService.countOfflineDeviceByTime(DateHelper.plusDays(currDate,-7),false)).build());
        countList.add(CountGroupVO.builder().group(CountGroupVO.GROUP14).groupName(CountGroupVO.GROUP_NAME14).count(alertOfflineDeviceService.countOfflineDeviceByTime(DateHelper.plusDays(currDate,-14),false)).build());
        countList.add(CountGroupVO.builder().group(CountGroupVO.GROUP30).groupName(CountGroupVO.GROUP_NAME30).count(alertOfflineDeviceService.countOfflineDeviceByTime(DateHelper.plusMonth(currDate,-1),false)).build());
        countList.add(CountGroupVO.builder().group(CountGroupVO.GROUP31).groupName(CountGroupVO.GROUP_NAME31).count(alertOfflineDeviceService.countOfflineDeviceByTime(DateHelper.plusDays(currDate,-30),true)).build());
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_DEVICE, deviceList)
                .put(SYS_DICT_KEY_ENABLE,enableStatus)
                .put("countList",countList)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/list", notes = "掉线列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "OfflineDeviceSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult getAlertRecordList(@RequestBody OfflineDeviceSearchVO req) {
        PageResult<OfflineDeviceResVO> pageResult = alertOfflineDeviceService.getDevicePage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }


//    @ApiOperation(value = "/record/view", notes = "告警详情")
//    @RequestMapping(value = "/record/view", method = {RequestMethod.GET})
//    public ApiResult getAlertRecordView(@RequestParam("id") Long id, @RequestParam("status") int status) {
//        return alertOfflineDeviceService.getAlertRecordView(id, status);
//    }

    @Deprecated//调用资产处置
    @ApiOperation(value = "/batchDeal", notes = "批量处置")
    @ApiImplicitParam(name = "disposeVO", value = "批量处置", required = true,  dataType = "OfflineDeviceDisposeVO", paramType = "body")
    @RequestMapping(value = "/batchDeal", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT_OFFLINE,operation = PORTAL_LOG_OPERATION_BATCH)
    public ApiResult  dispose(@Valid @RequestBody OfflineDeviceDisposeVO disposeVO, HttpServletRequest request){
        alertOfflineDeviceService.dispose(disposeVO,getUserSession(request));
        return ApiResult.success(MapBuilder.getSimpleMap("id", disposeVO.getAlertIds())).appendMessage("请求成功");
    }




}
