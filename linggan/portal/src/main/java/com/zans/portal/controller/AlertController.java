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
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.AlertRuleMapper;
import com.zans.portal.dao.RadiusEndpointMapper;
import com.zans.portal.model.VulHostVuln;
import com.zans.portal.service.*;
import com.zans.portal.vo.alert.*;
import com.zans.portal.vo.radius.EndPointViewVO;
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

import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/alert", tags = {"/alert ~ 告警中心"})
@RestController
@RequestMapping("/alert")
@Validated
@Slf4j
public class AlertController extends BasePortalController {

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


    @ApiOperation(value = "/record/list", notes = "风险预警列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRecordSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/record/list", method = {RequestMethod.POST})
    public ApiResult getAlertRecordList(@RequestBody AlertRecordSearchVO req) {
        super.checkPageParams(req, "");
        List<SelectVO> levelList = constantItemService.findItemsByDict(MODULE_ALERT_LEVEL);
        List<SelectVO> statusList = constantItemService.findItemsByDict(MODULE_DEAL_STATUS);
        List<SelectVO> typeList = ruleService.getAlertType();
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> ruleNameList = ruleService.getAlertRuleName(req.getTypeId());

        PageResult<AlertRecordRespVO> pageResult = ruleService.getAlertRecordPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("alertLevel", levelList)
                .put("dealStatus", statusList)
                .put("alertType", typeList)
                .put("alertRuleName", ruleNameList)
                .put(MODULE_DEVICE, deviceList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/record/hisList", notes = "风险预警历史列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRecordSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/record/hisList", method = {RequestMethod.POST})
    public ApiResult getAlertRecordHisList(@RequestBody AlertRecordSearchVO req) {
        List<SelectVO> levelList = constantItemService.findItemsByDict(MODULE_ALERT_LEVEL);
        List<SelectVO> statusList = constantItemService.findItemsByDict(MODULE_DEAL_STATUS);
        List<SelectVO> typeList = ruleService.getAlertType();
        List<SelectVO> ruleNameList = ruleService.getAlertRuleName(req.getTypeId());
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();

        PageResult<AlertRecordRespVO> pageResult = ruleService.getHisAlertRecordPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("alertLevel", levelList)
                .put("dealStatus", statusList)
                .put("alertType", typeList)
                .put("alertRuleName", ruleNameList)
                .put(MODULE_DEVICE, deviceList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }



    @ApiOperation(value = "/record/init", notes = "风险预警界面初始化")
    @RequestMapping(value = "/record/init", method = {RequestMethod.GET})
    public ApiResult getAlertRecordInit() {
        return ruleService.getAlertRecordInit(0);
    }




    @ApiOperation(value = "/record/list/his", notes = "告警记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRecordSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/record/list/his", method = {RequestMethod.POST})
    public ApiResult getAlertRecordListHis(@RequestBody AlertRecordSearchVO req) {
        List<SelectVO> levelList = constantItemService.findItemsByDict(MODULE_ALERT_LEVEL);
        List<SelectVO> statusList = constantItemService.findItemsByDict(MODULE_DEAL_STATUS);
        PageResult<AlertRecordRespVO> pageResult = ruleService.getAlertRecordHisPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("alertLevel", levelList)
                .put("dealStatus", statusList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/record/his/export", notes = "风险预警历史记录导出")
    @RequestMapping(value = "/record/his/export", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void hisExport(@RequestBody AlertRecordSearchVO req , HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("alert downloadFile #===========");
        String filePath = this.exportFolder + "风险预警历史数据导出_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "风险预警历史";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";
        req.setPageSize(100000);

        PageResult<AlertRecordRespVO> pageResult = ruleService.getAlertRecordPage(req);
        if (pageResult.getList().size()==0){
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }
        Map<Object, String> alertLevelMap = constantItemService.findItemsMapByDict(MODULE_ALERT_LEVEL);
        Map<Object, String> statusMap = constantItemService.findItemsMapByDict(MODULE_DEAL_STATUS);
        List<AlertRecordExportRespVO> list = new ArrayList<>();
        for (AlertRecordRespVO respVO : pageResult.getList()) {
            AlertRecordExportRespVO exportVO = new AlertRecordExportRespVO();
            BeanUtils.copyProperties(respVO,exportVO);
            if(respVO.getAlertLevel()!=null){
                exportVO.setAlertLevelName(alertLevelMap.get(respVO.getAlertLevel()));
            }
//            if(respVO.getDealStatus()!=null){
//                exportVO.setNoticeStatusName(statusMap.get(respVO.getDealStatus()));
//            }
            list.add(exportVO);
        }
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }


    @ApiOperation(value = "/record/view", notes = "告警详情")
    @RequestMapping(value = "/record/view", method = {RequestMethod.GET})
    public ApiResult getAlertRecordView(@RequestParam("id") Long id,@RequestParam(value = "type",defaultValue = "1") Integer  typeId, HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        String nickName = userSession.getNickName();
        return ruleService.getAlertRecordView(id, typeId);
    }

    @ApiOperation(value = "/record/view/otherDetail", notes = "告警第三方详情")
    @RequestMapping(value = "/record/view/otherDetail", method = {RequestMethod.POST})
    public ApiResult getAlertRecordViewOtherDetail(@RequestBody VulHostVuln req, HttpServletRequest request) {
        super.checkPageParams(req, "");
        UserSession userSession = httpHelper.getUser(request);
        String nickName = userSession.getNickName();
        return ruleService.getAlertRecordViewOtherDetail(req);
    }



    @ApiOperation(value = "/record/update", notes = "告警列表处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRecordSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/record/update", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult updateAlertRecord(@RequestBody AlertRecordSearchVO req, HttpServletRequest request) {
        String userName = super.getUserSession(request).getUserName();
        UserSession userSession = getUserSession(request);
        ApiResult apiResult = alertRecordDispose(req, userName, userSession);
        if (apiResult != null) {
            return apiResult;
        }
        return ApiResult.success();
    }


    @ApiOperation(value = "/record/batchUpdate", notes = "告警列表批量处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRecordSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/record/batchUpdate", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_BATCH)
    public ApiResult updateBatchAlertRecord(@RequestBody List<AlertRecordSearchVO> reqs, HttpServletRequest request) {
        String userName = super.getUserSession(request).getUserName();
        UserSession userSession = getUserSession(request);
        //进行校验，如果批量处置存在不同类型的数据，那么不允许执行
        boolean flag = templateIdCheck(reqs);
        if (!flag) {
            return ApiResult.error("请选择同一类指标进行批量操作！");
        }
        for (int i = 0; i < reqs.size(); i++) {
            AlertRecordSearchVO req = reqs.get(i);
            alertRecordDispose(req, userName, userSession);
        }
        return ApiResult.success();
    }


    private boolean templateIdCheck(List<AlertRecordSearchVO> reqs) {
        Set<Integer> set = new HashSet<>();
        reqs.forEach(req -> {
            set.add(req.getIndexId());
        });
        return set.size() > 1 ? false : true;
    }


    private ApiResult alertRecordDispose(AlertRecordSearchVO req, String userName, UserSession userSession) {
        req.setUser(userName);
        Integer disposeStatus = req.getDisposeStatus();
        //人工处理 dispose_status  0 线上派单; 1  线下排查
        ruleService.updateRuleRecord(req);
        //阻断mac地址处理
        ApiResult blockMac = dealBlockMac(req, userName, userSession, disposeStatus);
        if (blockMac != null) {
            req.setNoticeRemark("用户"+userSession.getNickName()+"阻断了MAC:"+req.getMac()+"  备注:"+req.getNoticeRemark());
            ruleService.updateRuleRecord(req);
            return blockMac;
        }

        //交换机端口关闭(目前是逻辑关闭)
        ApiResult x1 = closePort(req, disposeStatus);
        if (x1 != null){
            return x1;
        }

        return ApiResult.success();
    }

    private ApiResult closePort(AlertRecordSearchVO req, Integer disposeStatus) {
        if (disposeStatus!=null && disposeStatus == 5) {
             String swIp = req.getKeywordValue();
            String sql = req.getAction();
            if(!StringUtils.isEmpty(sql)){
                List<AlertReportRespVO> macList = req.getMacList();
                for (AlertReportRespVO alertReportRespVO : macList) {
                    String mac = alertReportRespVO.getName();
                    Long status = alertReportRespVO.getValue();
                     String  exSql = String.format(sql, status,swIp,mac);
                    alertRuleMapper.executeSql(exSql);
                }
                ruleService.updateRuleRecord(req);
                return ApiResult.success();
            }else {
                return ApiResult.error("该规则未配置执行语句！请联系管理员！");
            }
        }
        return null;
    }



    private ApiResult dealBlockMac(AlertRecordSearchVO req, String userName, UserSession userSession, Integer disposeStatus) {
        if (disposeStatus!=null && disposeStatus == 4) {
            //2020-10-9 和北傲确认,如果是忽略操作的就直接标记为已处理
            if (req.getMacs() != null && req.getMacs().length == 0) {
                req.setDisposeStatus(null);
                ruleService.updateRuleRecord(req);
                return ApiResult.success();
            }
            //todo 这个注释就是添加用户，以及mac
            String authMark = req.getAuthMark();
            //这里需要通过选择的mac地址去得到endPointId
            EndPointViewVO viewVO = radiusEndpointMapper.findEndPointIdByPass(req.getMac());
            if (viewVO == null) {
                return ApiResult.error("该 " + req.getMac() + " mac地址未找到！请联系管理员查看！");
            }
            Integer endPointId = viewVO.getId();
            //因为是对mac直接阻断，所以直接给0
            ApiResult apiResult = service.judge(endPointId, 0, authMark, userSession);
            if (apiResult.getCode() == 0) {
                String format = String.format("处置人:s%,对mac:s%已完成了阻断操作! 备注：", userName, req.getMac());
                authMark=format+authMark;
                req.setAuthMark(authMark);
                ruleService.updateRuleRecord(req);
            }
            log.info("处置人:{},对mac:{}已完成了阻断操作!", userName, req.getMac());
            return ApiResult.success();
        }
        return null;
    }



    @ApiOperation(value = "/record/del", notes = "告警列表记录删除")
    @RequestMapping(value = "/record/del", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult del(@RequestParam("id") Long id, HttpServletRequest request) {
        ruleService.delRecordById(id);
        return ApiResult.success();
    }


    @ApiOperation(value = "/record/batchDel", notes = "告警列表记录批量删除")
    @RequestMapping(value = "/record/batchDel", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_BATCH_DELETE)
    public ApiResult batchDel(@RequestParam("ids") String ids, HttpServletRequest request) {
        ruleService.batchDelRecordByIds(ids);
        return ApiResult.success();
    }

    @ApiOperation(value = "/record/clean", notes = "告警列表记录清空")
    @RequestMapping(value = "/record/clean", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_LIST_CLEAR)
    public ApiResult clean(@RequestParam("keywordValue") String keywordValue, HttpServletRequest request) {
        ruleService.cleanRecordByKeyword(keywordValue);
        return ApiResult.success();
    }


    @ApiOperation(value = "/rule/list", notes = "告警策略列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRuleSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/rule/list", method = {RequestMethod.POST})
    public ApiResult getAlertRuleList(@RequestBody AlertRuleSearchVO req) {
        List<SelectVO> typeList = ruleService.getAlertType();
        PageResult<AlertRuleRespVO> pageResult = ruleService.getAlertRulePage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ALERT_TYPE, typeList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/type/list", notes = "告警类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRuleSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/type/list", method = {RequestMethod.POST})
    public ApiResult getAlertTypeList(@RequestBody AlertTypeSearchVO req) {
        return ApiResult.success(ruleService.getAlertTypePage(req));
    }


    @ApiOperation(value = "/index/list", notes = "指标列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AlertRuleSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/index/list", method = {RequestMethod.POST})
    public ApiResult getAlertIndexList(@RequestBody AlertIndexSearchVO req) {
        List<SelectVO> typeList = ruleService.getAlertType();
        PageResult<AlertIndexRespVO> pageResult = ruleService.getAlertIndexPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ALERT_TYPE, typeList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/record/dispose/init", notes = "处置管理界面初始化")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "处置管理查询", required = true,
                    dataType = "AlertRecordSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/record/dispose/init", method = {RequestMethod.GET})
    public ApiResult getAlertRecordDisposeInit() {
        return ruleService.getAlertRecordDisposeInit();
    }


    @ApiOperation(value = "/record/dispose/list", notes = "处置管理查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "处置管理查询", required = true,
                    dataType = "AlertRecordSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/record/dispose/list", method = {RequestMethod.POST})
    public ApiResult getAlertRecordDisposeList(@RequestBody AlertRecordSearchVO req) {
        List<SelectVO> disList = constantItemService.findItemsByDict(MODULE_DISPOSE_TYPE);
        List<SelectVO> levelList = constantItemService.findItemsByDict(MODULE_ALERT_LEVEL);
        List<SelectVO> typeList = ruleService.getAlertType();
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        PageResult<AlertRecordRespVO> pageResult = ruleService.getAlertRecordDisposePage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("alertLevel", levelList)
                .put("dealStatus", disList)
                .put("alertType", typeList)
                .put("disposeStatus", disList)
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/record/export", notes = "风险预警导出")
    @RequestMapping(value = "/record/export", method = {RequestMethod.POST}, headers = "Accept=application/octet-stream")
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void export(@RequestBody AlertRecordSearchVO req , HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("alert downloadFile #===========");
        String filePath = this.exportFolder + "风险预警导出_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "风险预警";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";
        req.setPageSize(10000);
        PageResult<AlertRecordRespVO> pageResult = ruleService.getAlertRecordPage(req);
        if (pageResult.getList().size()==0){
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }
        Map<Object, String> alertLevelMap = constantItemService.findItemsMapByDict(MODULE_ALERT_LEVEL);
        Map<Object, String> statusMap = constantItemService.findItemsMapByDict(MODULE_DEAL_STATUS);
        List<AlertRecordExportRespVO> list = new ArrayList<>();
        for (AlertRecordRespVO respVO : pageResult.getList()) {
            AlertRecordExportRespVO exportVO = new AlertRecordExportRespVO();
            BeanUtils.copyProperties(respVO,exportVO);
            if(respVO.getAlertLevel()!=null){
                exportVO.setAlertLevelName(alertLevelMap.get(respVO.getAlertLevel()));
            }
//            if(respVO.getDealStatus()!=null){
//                exportVO.setNoticeStatusName(statusMap.get(respVO.getDealStatus()));
//            }
            list.add(exportVO);
        }
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }


    @ApiOperation(value = "/record/read", notes = "告警列表设为已读")
    @RequestMapping(value = "/record/read", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult read(@RequestParam("id") Long id, HttpServletRequest request) {
        ruleService.readRecordById(id);
        return ApiResult.success();
    }


    @ApiOperation(value = "/record/batchRead", notes = "告警列表记录批量删除")
    @RequestMapping(value = "/record/batchRead", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ALERT,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult batchRead(@RequestParam("ids") String ids, HttpServletRequest request) {
        ruleService.batchReadRecordByIds(ids);
        return ApiResult.success();
    }



}
