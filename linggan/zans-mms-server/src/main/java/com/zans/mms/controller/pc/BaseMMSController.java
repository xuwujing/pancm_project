package com.zans.mms.controller.pc;

import com.zans.base.controller.BaseController;
import com.zans.base.exception.BusinessException;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.CommonLabel;
import com.zans.mms.service.*;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import com.zans.mms.vo.asset.subset.AssetSubsetLineChartReqVO;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.chart.TicketChartReqVO;
import com.zans.mms.vo.common.TreeSelect;
import com.zans.mms.vo.push.DropLine;
import com.zans.mms.vo.ticket.AppFaultTicketChartVO;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import com.zans.mms.vo.ticket.AppTicketChartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.GlobalConstants.*;
/**
 * (BaseArea)表控制层
 *
 * @author makejava
 * @since 2021-01-12 15:20:08
 */
@Api(value = "/base", tags = {"/基础数据"})
@RestController
@RequestMapping("/base")
@Slf4j
public class BaseMMSController extends BaseController {
    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    IBaseAreaService baseAreaService;

    @Autowired
    IBaseDeviceTypeService baseDeviceTypeService;

    @Autowired
    IBaseOrgService baseOrgService;

    @Autowired
    IBaseOrgRoleService baseOrgRoleService;

    @Autowired
    IBaseFaultTypeService baseFaultTypeService;

    @Autowired
    ITicketService ticketService;

    @Autowired
    IAssetSubsetService assetSubsetService;

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    private IPatrolTaskService patrolTaskService;

    @Autowired
    private  ICommonLabelService commonLabelService;

    @Autowired
    private IChartService chartService;

    @Autowired
    private ISysJurisdictionService sysJurisdictionService;

    @Autowired
    private IAssetService assetService;

    @Autowired
    private IDevicePointService devicePointService;

    @Autowired
    private ISpeedService speedService;

    @Autowired
    private ISendMsgService sendMsgService;


    @ApiOperation(value = "/init", notes = "")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    public ApiResult init(HttpServletRequest request){
        List<SelectVO> patrolAssetStatusList = constantItemService.findItemsByDict(MODULE_PATROL_ASSET_STATUS);
        List<SelectVO> patrolStatusList = constantItemService.findItemsByDict(MODULE_PATROL_STATUS);
        List<SelectVO> maintainStatusList = constantItemService.findItemsByDict(SYS_DICT_KEY_MAINTAIN_STATUS);
        List<SelectVO> deviceCategoryList = constantItemService.findItemsByDict(MODULE_DEVICE_CATEGORY);
        List<SelectVO> videoFaultTypeList = constantItemService.findItemsByDict(VIDEO_FAULT_TYPE);
        List<SelectVO> videoFaultTypeResultLList = constantItemService.findItemsByDict(VIDEO_FAULT_TYPE_RESULT);
        List<SelectVO> videoDiagnosisResultLList = constantItemService.findItemsByDict(VIDEO_DIAGNOSIS_RESULT);
        List<SelectVO> checkResultList = constantItemService.findItemsByDict(SYS_DICT_KEY_PATROL_CHECK_RESULT);
        List<SelectVO> projectNameList = constantItemService.findItemsByDict(SYS_DICT_KEY_PROJECT_NAME);
        List<SelectVO> deviceTypeList = baseDeviceTypeService.findDeviceTypeToSelect();
        List<TreeSelect> areaTree = baseAreaService.areaTreeList();
        List<TreeSelect> allAreaTree = baseAreaService.allAreaTreeList();
        List<SelectVO> areaList = baseAreaService.areaList();

        List<SelectVO> allOrgList = baseOrgService.orgList();
        List<SelectVO> orgTypeList = baseOrgService.orgTypeList();
        List<SelectVO> orgList = baseOrgService.queryBaseOrg();
        List<SelectVO> mapOrgList = baseOrgService.queryMapOrg();
        List<SelectVO>  orgRoleList = baseOrgRoleService.orgRoleList();

        Map<String, List<SelectVO>> baseDeviceTypeFaultSelect = baseFaultTypeService.listFaultTypeView();
        List<SelectVO> faultList = baseFaultTypeService.faultList();
        List<SelectVO> jurisdictionList = sysJurisdictionService.selectList();
        List<String> pointBuildCompanyList = devicePointService.getBuildCompany();
        List<SelectVO> itemClassificationList = constantItemService.findItemsByDict(SYS_DICT_KEY_ITEM_CLASSIFICATION);
        UserSession user = httpHelper.getUser(request);
        //查询单位类型
        String orgType = baseOrgService.getOrgType(user.getOrgId());
        if(!StringHelper.isEmpty(orgType)&&"03".equals(orgType)){
            itemClassificationList = constantItemService.findItemsByDict("item_classification1");
        }
        if(!StringHelper.isEmpty(orgType)&&"02".equals(orgType)){
            if("30001".equals(user.getOrgId())){
                itemClassificationList = constantItemService.findItemsByDict("item_classification5");

            }
            if("30002".equals(user.getOrgId())){
                itemClassificationList = constantItemService.findItemsByDict("item_classification6");

            }
            if("30003".equals(user.getOrgId())){
                itemClassificationList = constantItemService.findItemsByDict("item_classification7");

            }
        }
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_DEVICE,deviceTypeList)
                .put(MODULE_PATROL_ASSET_STATUS,patrolAssetStatusList)
                .put(MODULE_PATROL_STATUS,patrolStatusList)
                .put("area_tree",areaTree)
                .put("all_area_tree",allAreaTree)
                .put("areaList",areaList)
                .put("allOrgList",allOrgList)
                .put("orgList",orgList)
                .put("mapOrgList",mapOrgList)
                .put("orgTypeList",orgTypeList)
                .put(SYS_DICT_KEY_MAINTAIN_STATUS,maintainStatusList)
                .put(MODULE_DEVICE_CATEGORY,deviceCategoryList)
                .put("orgRoleList",orgRoleList)
                .put("type_fault_list",baseDeviceTypeFaultSelect)
                .put("type_fault",faultList)
                .put(SYS_DICT_KEY_PATROL_CHECK_RESULT,checkResultList)
                .put("video_fault_type",videoFaultTypeList)
                .put("video_fault_type_result",videoFaultTypeResultLList)
                .put("video_diagnosis_result",videoDiagnosisResultLList)
                .put("jurisdiction",jurisdictionList)
                .put("projectName",projectNameList)
                .put("pointBuildCompany",pointBuildCompanyList)
                .put("itemClassificationList",itemClassificationList)
                .build();
        return ApiResult.success(result);
    }



    @ApiOperation(value = "/依据单位获取角色", notes = "依据单位获取角色")
    @RequestMapping(value = "/getRoleBMaintain", method = {RequestMethod.GET})
    public ApiResult<List<SelectVO>> getRoleBMaintain(@RequestParam("maintainNum") String maintainNum){
        List<SelectVO> result = baseOrgRoleService.getRoleBMaintain(maintainNum);
        return ApiResult.success(result);
    }




    @ApiOperation(value = "PC总数查询", notes = "PC总数查询")
    @RequestMapping(value = "/getTotal", method = {RequestMethod.GET})
    public ApiResult getTotal(HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        List<CircleUnit> ticketsTotal = ticketService.getPcTicketTotal(userSession);
       List<CircleUnit> patrolSchemeTotal = patrolTaskService.getPcPatrolTotal(userSession);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("tickets_total", ticketsTotal)
                .put("patrol_total", patrolSchemeTotal)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "故障类型占比", notes = "故障类型占比")
    @RequestMapping(value = "/getTicketFaultType", method = {RequestMethod.GET})
    public ApiResult getTicketFaultType(HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        List<CountUnit> pcFaultType = ticketService.getPcFaultType(userSession);
        return ApiResult.success(pcFaultType);
    }


    @ApiOperation(value = "派工来源占比", notes = "派工来源占比")
    @RequestMapping(value = "/getPcTicketSource", method = {RequestMethod.GET})
    public ApiResult getPcTicketSource(HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        List<CountUnit> pcTicketSource = ticketService.getPcTicketSource(userSession);
        return ApiResult.success(pcTicketSource);
    }


    @ApiOperation(value = "工单分配单位数量", notes = "工单分配单位数量")
    @RequestMapping(value = "/getPcMaintainFacility", method = {RequestMethod.GET})
    public ApiResult getPcMaintainFacility(HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        List<CircleUnit> pcMaintainFacility = ticketService.getPcMaintainFacility(userSession);
        //配合前端柱状图实现，将返回的值改成x轴和y轴
        List<String> x = new ArrayList<>();
        List<Object> y = new ArrayList<>();
        for (CircleUnit circleUnit : pcMaintainFacility) {
            x.add(circleUnit.getChineName());
            y.add(circleUnit.getVal());
        }
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("x", x)
                .put("y", y)
                .build();
        return ApiResult.success(result);
    }







    /**
     * 工单按故障类型统计方法
     * @return
     */
    @ApiOperation(value = "工单按故障类型统计,用于在图表中显示", notes = "工单按故障类型统计，用于在图表中显示")
    @RequestMapping(value = "/statisticsByFaultType", method = RequestMethod.GET)
    public ApiResult statisticsByFaultType(){
        return ticketService.statisticsByFaultType();
    }

    /**
     * 工单数量统计方法
     * @return
     */
    @ApiOperation(value = "工单数量统计方法", notes = "工单数量统计方法")
    @RequestMapping(value = "/ticketStatistics", method = RequestMethod.GET)
    public ApiResult ticketStatistics(){
        return ticketService.ticketStatistics();
    }


    /**
     * 工单按设备类型统计方法 预留 尚未完成
     * @return
     */
    @ApiOperation(value = "工单按设备类型统计方法", notes = "工单按设备类型统计方法")
    @RequestMapping(value = "/ticketStatisticsByDeviceType", method = RequestMethod.GET)
    public ApiResult statisticsTicketByDeviceType(){
        return ticketService.statisticsTicketByDeviceType();
    }


    /**
     * 按单位统计巡检设备数量
     */
    @ApiOperation(value = "按单位统计巡检设备数量", notes = "按单位统计巡检设备数量")
    @RequestMapping(value = "/patrolInspectionByUnit", method = RequestMethod.GET)
    public ApiResult patrolInspectionByUnit(){
        return ApiResult.success();
    }

    /**
     * 按辖区统计巡检设备数量
     */
    @ApiOperation(value = "按辖区统计巡检设备数量", notes = "按辖区统计巡检设备数量")
    @RequestMapping(value = "/patrolInspectionByAreaName", method = RequestMethod.GET)
    public ApiResult patrolInspectionByAreaName(){
        return ApiResult.success();
    }


    /**
     * 按时间周期统计辖区上报数据
     */
    @ApiOperation(value = "按时间周期统计辖区上报数据", notes = "按时间周期统计辖区上报数据")
    @RequestMapping(value = "/reportingDataByTime", method = RequestMethod.GET)
    public ApiResult reportingDataByTime(){
        return ApiResult.success();
    }

    /**
     * 按状态统计辖区上报数据
     */
    @ApiOperation(value = "按状态统计辖区上报数据", notes = "按状态统计辖区上报数据")
    @RequestMapping(value = "/reportingDataByType", method = RequestMethod.GET)
    public ApiResult reportingDataByType(){
        return ApiResult.success();
    }

    /**
     * 资产子集在线率折线图数据获取并处理 需要与在线率联动 主要传值为设备子集id
     * @return
     */
    @ApiOperation(value = "/getAssetSubsetChartData",notes = "资产子集在线率折线图数据获取并处理")
    @PostMapping("/getAssetSubsetChartData")
    public ApiResult getAssetSubsetChartData(@RequestBody AssetSubsetLineChartReqVO assetSubsetLineChartReqVO) throws Exception {
        return assetSubsetService.getAssetSubsetChartData(assetSubsetLineChartReqVO);
    }


    /**
     * 资产子集实时在线率展示接口 显示最后一条在线率数据
     * @return
     */
    @ApiOperation(value = "/getAssetSubsetOnlineData",notes = "资产子集实时在线率展示接口 显示最后一条在线率数据")
    @GetMapping("/getAssetSubsetOnlineData")
    public ApiResult getAssetSubsetOnlineData() throws Exception {
        return assetSubsetService.getAssetSubsetOnlineData();
    }


    /**----------------------------------通用标签增删改查开始-----------------------------------**/
    @ApiOperation(value = "/commonLabel/create",notes = "通用标签新增")
    @PostMapping("/commonLabel/create")
    public ApiResult commonLabelCreate(@RequestBody CommonLabel commonLabel, HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        commonLabel.setCreator(userSession.getUserName());
        return commonLabelService.create(commonLabel);
    }

    @ApiOperation(value = "/commonLabel/update",notes = "通用标签修改")
    @PostMapping("/commonLabel/update")
    public ApiResult commonLabelUpdate(@RequestBody CommonLabel commonLabel,HttpServletRequest request) {
        return commonLabelService.update(commonLabel);
    }


    @ApiOperation(value = "/commonLabel/delete",notes = "通用标签删除")
    @PostMapping("/commonLabel/delete")
    public ApiResult commonLabelDelete(@RequestBody CommonLabel commonLabel,HttpServletRequest request) {
        return commonLabelService.delete(commonLabel);
    }

    @ApiOperation(value = "/commonLabel/list",notes = "通用标签列表")
    @PostMapping("/commonLabel/list")
    public ApiResult commonLabelList(@RequestBody CommonLabel commonLabel,HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        commonLabel.setCreator(userSession.getUserName());
        return commonLabelService.list(commonLabel);
    }



    /**----------------------------------通用标签增删改查结束-----------------------------------**/



    /**----------------------------------大屏报表开始-------------------------------------------**/
    /**
     * type为空或为0展示静态数据 type为1展示真实数据
     * @param type
     * @return
     */
    @ApiOperation(value = "/indexData",notes = "大屏首页接口")
    @GetMapping("/indexData")
    public ApiResult indexData(@RequestParam("type") Integer type,@RequestParam("deviceType")String deviceType) {
        return chartService.indexData(type,deviceType);
    }


    /**
     * 运维工单监控
     * @return
     */
    @ApiOperation(value = "/ticketMonitoring",notes = "运维工单监控")
    @PostMapping("/ticketMonitoring")
    public ApiResult ticketMonitoring(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return chartService.ticketMonitoring(ticketChartReqVO);
    }




    /**
     * 运维巡检监控
     * @return
     */
    @ApiOperation(value = "/patrolMonitoring",notes = "运维巡检监控")
    @PostMapping("/patrolMonitoring")
    public ApiResult patrolMonitoring(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return chartService.patrolMonitoring(ticketChartReqVO);
    }


    /**
     * 绩效考核 维保单位评分
     * @return
     */
    @ApiOperation(value = "/maintainOrgRank",notes = "维保单位评分")
    @PostMapping("/maintainOrgRank")
    public ApiResult maintainOrgRank(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return chartService.maintainOrgRank(ticketChartReqVO);
    }

    /**
     * 绩效考核 质保单位在线率
     * @return
     */
    @ApiOperation(value = "/qualityAssuranceUnit",notes = "质保单位在线率")
    @PostMapping("/qualityAssuranceUnit")
    public ApiResult qualityAssuranceUnit(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return chartService.qualityAssuranceUnit(ticketChartReqVO);
    }

    /**
     * 绩效考核 拓建项目在线率
     * @return
     */
    @ApiOperation(value = "/extensionProject",notes = "拓建项目在线率")
    @PostMapping("/extensionProject")
    public ApiResult extensionProject(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return chartService.extensionProject(ticketChartReqVO);
    }


    /**
     * 查询告警数据
     * @param
     * @return
     */
    @RequestMapping(value = "/record/list", method = {RequestMethod.POST})
    public ApiResult getAlertRecordList(@RequestBody TicketChartReqVO ticketChartReqVO) {
       return chartService.getAlertRecordPageTop(ticketChartReqVO);

    }



    /**
     * 延迟数据
     * @param
     * @return
     */
    @RequestMapping(value = "/delay/list", method = {RequestMethod.POST})
    public ApiResult delayList() {
        return chartService.delayList();

    }


    @ApiOperation(value = "故障工单统计Table", notes = "故障工单统计Table")
    @RequestMapping(value = "/breakdownTicketTable", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult breakdownTicketTable (@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        //type 1:日 2：3天  3：近一周
        //设置一个默认的统计时间段
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        List<AppFaultTicketChartVO> map = ticketService.breakdownTicketTable(appTicketCharReqVO);
        return ApiResult.success(map);
    }

    @ApiOperation(value = "故障类型统计", notes = "故障类型统计")
    @RequestMapping(value = "/getChartTicketFaultType", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult getTicketFaultType(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO appTicketChartVO= ticketService.getAppFaultType(appTicketCharReqVO);
        return ApiResult.success(appTicketChartVO);
    }


    @ApiOperation(value = "故障来源统计", notes = "派工来源统计")
    @RequestMapping(value = "/getTicketSource", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult getAppTicketSource(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO appTicketChartVO = ticketService.getAppTicketSource(appTicketCharReqVO);
        return ApiResult.success(appTicketChartVO);
    }

    @ApiOperation(value = "故障设备类型统计", notes = "故障设备类型统计")
    @RequestMapping(value = "/getDeviceType", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult getDeviceType(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        AppTicketChartVO appTicketChartVO = ticketService.getDeviceType(appTicketCharReqVO);
        return ApiResult.success(appTicketChartVO);
    }


    @ApiOperation(value = "故障处理方式统计", notes = "故障处理方式统计")
    @RequestMapping(value = "/getDealWay", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResult getDealWay(@RequestBody AppTicketCharReqVO appTicketCharReqVO) {
        if(null==appTicketCharReqVO.getType()&& StringUtils.isEmpty(appTicketCharReqVO.getStartTime())
                &&StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
            appTicketCharReqVO.setType(1);
        }else{
            if(StringUtils.isEmpty(appTicketCharReqVO.getStartTime()) ||StringUtils.isEmpty(appTicketCharReqVO.getEndTime())){
                throw new BusinessException("开始时间或结束时间未传值！");
            }
            appTicketCharReqVO.setType(null);
        }
        String startTime = appTicketCharReqVO.getStartTime()+" 00:00:00";
        String EndTime = appTicketCharReqVO.getEndTime()+" 23:59:59";
        appTicketCharReqVO.setStartTime(startTime);
        appTicketCharReqVO.setEndTime(EndTime);
        appTicketCharReqVO.setType(null);
        AppTicketChartVO appTicketChartVO = ticketService.getDealWay(appTicketCharReqVO);
        return ApiResult.success(appTicketChartVO);
    }



    /**
     * 舆情图表数据
     * @param
     * @return
     */
    @RequestMapping(value = "/poManager", method = {RequestMethod.POST})
    public ApiResult poManager(@RequestBody TicketChartReqVO ticketChartReqVO) {
        return chartService.poManager(ticketChartReqVO);

    }





    /**-----------------------------------大屏报表结束------------------------------------------------**/




    /** --------------------------------导入进度实时查看方法------------------------------------------**/


    /**
     * 上传进度实时查看
     * @param
     * @return
     */
    @GetMapping(value = "/speedOfProgress")
    public ApiResult speedOfProgress(@RequestParam("id") Long id) {
        return speedService.getById(id);
    }












    /** ----------------------------------导入进入实时查看方法结束---------------------------------------**/




    /** ----------------------------------安全系统微信通知开始--------------------------------------------**/

    /**
     * 推送给通信运营商人员
     * @param dropLine 掉线数据
     * @return
     */
    @PostMapping(value = "/send")
    public ApiResult sendMsg(@RequestBody DropLine dropLine) {
        log.info("离线(断光)接口请求入参:{}",dropLine);
        return sendMsgService.sendCommonMsg(dropLine);
    }













    /** ----------------------------------安全系统微信通知结束--------------------------------------------**/




}
