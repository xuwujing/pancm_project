package com.zans.mms.controller.pc;

import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.service.*;
import com.zans.mms.vo.asset.subset.AssetSubsetLineChartReqVO;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.chart.CountUnit;
import com.zans.mms.vo.common.TreeSelect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @ApiOperation(value = "/init", notes = "")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    public ApiResult init(){
        List<SelectVO> patrolAssetStatusList = constantItemService.findItemsByDict(MODULE_PATROL_ASSET_STATUS);
        List<SelectVO> patrolStatusList = constantItemService.findItemsByDict(MODULE_PATROL_STATUS);
        List<SelectVO> maintainStatusList = constantItemService.findItemsByDict(SYS_DICT_KEY_MAINTAIN_STATUS);
        List<SelectVO> deviceCategoryList = constantItemService.findItemsByDict(MODULE_DEVICE_CATEGORY);
        List<SelectVO> deviceTypeList = baseDeviceTypeService.findDeviceTypeToSelect();
        List<TreeSelect> areaTree = baseAreaService.areaTreeList();
        List<TreeSelect> allAreaTree = baseAreaService.allAreaTreeList();
        List<SelectVO> areaList = baseAreaService.areaList();

        List<SelectVO> allOrgList = baseOrgService.orgList();
        List<SelectVO> orgTypeList = baseOrgService.orgTypeList();
        List<SelectVO> orgList = baseOrgService.queryBaseOrg();
        List<SelectVO>  orgRoleList = baseOrgRoleService.orgRoleList();

        Map<String, List<SelectVO>> baseDeviceTypeFaultSelect = baseFaultTypeService.listFaultTypeView();
        List<SelectVO> faultList = baseFaultTypeService.faultList();
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_DEVICE,deviceTypeList)
                .put(MODULE_PATROL_ASSET_STATUS,patrolAssetStatusList)
                .put(MODULE_PATROL_STATUS,patrolStatusList)
                .put("area_tree",areaTree)
                .put("all_area_tree",allAreaTree)
                .put("areaList",areaList)
                .put("allOrgList",allOrgList)
                .put("orgList",orgList)
                .put("orgTypeList",orgTypeList)
                .put(SYS_DICT_KEY_MAINTAIN_STATUS,maintainStatusList)
                .put(MODULE_DEVICE_CATEGORY,deviceCategoryList)
                .put("orgRoleList",orgRoleList)
                .put("type_fault_list",baseDeviceTypeFaultSelect)
                .put("type_fault",faultList)
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


}
