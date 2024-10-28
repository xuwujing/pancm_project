package com.zans.mms.controller.demokit;

import com.zans.base.annotion.Record;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.controller.BaseController;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.guard.AlertRuleMapper;
import com.zans.mms.dao.guard.RadiusEndpointMapper;
import com.zans.mms.service.*;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import com.zans.mms.vo.alert.AlertRecordSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import static com.zans.mms.config.PortalGlobalConstants.*;

@Api(value = "demoKit/alert", tags = {"/alert ~ 告警中心"})
@RestController
@RequestMapping("demoKit/alert")
@Validated
@Slf4j
public class AlertController extends BaseController {

    @Autowired
    private IAlertRuleService ruleService;

    @Autowired
    private IConstantItemService constantItemService;

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private IRadiusEndPointService service;

    @Autowired
    private RadiusEndpointMapper radiusEndpointMapper;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IFileService fileService;

    @Value("${api.export.folder}")
    String exportFolder;
    @Value("${api.upload.folder}")
    String uploadFolder;


    /**
     * typeId为1 终端 typeId为2 网络
     * @param req
     * @return
     */
    @ApiOperation(value = "/record/list", notes = "风险预警列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRecordSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/record/list", method = {RequestMethod.POST})
    public ApiResult getAlertRecordList(@RequestBody AlertRecordSearchVO req) {
        List<SelectVO> levelList = constantItemService.findItemsByDict(MODULE_ALERT_LEVEL);
        List<SelectVO> statusList = constantItemService.findItemsByDict(MODULE_DEAL_STATUS);
        List<SelectVO> typeList = ruleService.getAlertType();
        List<SelectVO> indexList = ruleService.getAlertIndex(req.getTypeId());
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> area = areaService.findAreaToSelect();
        List<SelectVO> noticeTimeList = new ArrayList<>();
        SelectVO selectVO = new SelectVO();
        selectVO.setItemValue("一天内");
        selectVO.setItemKey("1");
        noticeTimeList.add(selectVO);
        SelectVO selectVO2 = new SelectVO();
        selectVO2.setItemValue("一周内");
        selectVO2.setItemKey("2");
        noticeTimeList.add(selectVO2);
        SelectVO selectVO3 = new SelectVO();
        selectVO3.setItemValue("两周内");
        selectVO3.setItemKey("3");
        noticeTimeList.add(selectVO3);
        SelectVO selectVO4 = new SelectVO();
        selectVO4.setItemValue("一月内");
        selectVO4.setItemKey("4");
        noticeTimeList.add(selectVO4);
        SelectVO selectVO5 = new SelectVO();
        selectVO5.setItemValue("一个月前");
        selectVO5.setItemKey("5");
        noticeTimeList.add(selectVO5);

        PageResult<AlertRecordRespVO> pageResult = ruleService.getAlertRecordPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ALERT_LEVEL, levelList)
                .put(MODULE_DEAL_STATUS, statusList)
                .put(MODULE_ALERT_TYPE, typeList)
                .put(MODULE_ALERT_INDEX, indexList)
                .put(MODULE_AREA, area)
                .put(MODULE_DEVICE, deviceList)
                .put(INIT_DATA_TABLE, pageResult)
                .put("noticeTimeList",noticeTimeList)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/init", notes = "字典数据")
    @RequestMapping(value = "/init", method = {RequestMethod.POST})
    public ApiResult init(@RequestBody AlertRecordSearchVO req) {
        List<SelectVO> levelList = constantItemService.findItemsByDict(MODULE_ALERT_LEVEL);
        List<SelectVO> statusList = constantItemService.findItemsByDict(MODULE_DEAL_STATUS);
        List<SelectVO> typeList = ruleService.getAlertType();
        List<SelectVO> indexList = ruleService.getAlertIndex(req.getTypeId());
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> area = areaService.findAreaToSelect();
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ALERT_LEVEL, levelList)
                .put(MODULE_DEAL_STATUS, statusList)
                .put(MODULE_ALERT_TYPE, typeList)
                .put(MODULE_ALERT_INDEX, indexList)
                .put(MODULE_AREA, area)
                .put(MODULE_DEVICE, deviceList)
                .build();
        return ApiResult.success(result);
    }



    @ApiOperation(value = "/record/init", notes = "风险预警界面初始化")
    @RequestMapping(value = "/record/init", method = {RequestMethod.GET})
    public ApiResult getAlertRecordInit() {
        return ruleService.getAlertRecordInit(0);
    }


    @ApiOperation(value = "/record/view", notes = "告警详情")
    @RequestMapping(value = "/record/view", method = {RequestMethod.GET})
    public ApiResult getAlertRecordView(@RequestParam("id") Long id, @RequestParam("status") int status,@RequestParam(value = "type",defaultValue = "1") Integer  typeId) {
        return ruleService.getAlertRecordView(id, status,typeId);
    }



}
