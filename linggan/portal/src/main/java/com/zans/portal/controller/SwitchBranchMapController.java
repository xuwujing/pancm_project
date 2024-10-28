package com.zans.portal.controller;

import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.SysSwitcherBranch;
import com.zans.portal.service.*;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.switcher.SwitchBranchConvergeMapResVO;
import com.zans.portal.vo.switcher.SwitchBranchMapInitVO;
import com.zans.portal.vo.switcher.SwitchBranchMapUpdateVO;
import com.zans.portal.vo.switcher.SwitchBranchSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.BUILD_UNIT;
import static com.zans.portal.config.GlobalConstants.DEPARTMENT_TREE;

@Api(value = "/switchBrunchMap", tags = {"/switchBrunchMap ~ 汇聚点地图"})
@RestController
@RequestMapping("/switchBrunchMap")
@Slf4j
@Validated
public class SwitchBranchMapController extends BasePortalController {

    @Autowired
    ISwitcherBrunchService switcherBrunchService;
    @Autowired
    ISwitcherBrunchMapService switcherBrunchMapService;
    @Autowired
    ILogOperationService logOperationService;
    @Autowired
    IAreaService areaService;
    @Autowired
    IConstantItemService constantItemService;

//    @ApiOperation(value = "/init", notes = "地图列表初始化")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
//                    dataType = "SwitchBranchMapInitVO", paramType = "body")
//    })
//    @RequestMapping(value = "/init", method = {RequestMethod.POST})
//    @ResponseBody
//    public ApiResult init(@RequestBody SwitchBranchMapInitVO initVO) {
//        TreeSelect areaRoot = TreeSelect.builder()
//                .id(0)
//                .label("开发区交管大队")
//                .seq(1)
//                .build();
//        TreeSelect productRoot = TreeSelect.builder()
//                .id(0)
//                .label("交换机")
//                .seq(1)
//                .build();
//        List<SelectVO> areaList = areaService.findRegionToSelect(REGION_LEVEL_TWO);
//
////        List<SysProject> list = sysProjectService.findLikeByProjectName(initVO.getProjectName());
////        List<TreeSelect> areaList = switcherBrunchService.getAreaList(initVO.getAreaName(),initVO.getProjectIds());
//        List<TreeSelect> areaPointList = switcherBrunchService.getPointListByArea(null,initVO.getProjectIds(),initVO.getPointName());
//        areaRoot.setChildren(areaPointList);
//        areaList.forEach(it->{
//            productRoot.addChild(TreeSelect.builder()
//                    .id(it.getItemKey())
//                    .label(it.getItemValue())
//                    .children(new ArrayList<>(0))
//                    .build());
//        });
//
//
//        Map<String, Object> result = MapBuilder.getBuilder()
//                .put(MODULE_AREA, Arrays.asList(areaRoot))
//                .put("project", Arrays.asList(productRoot))
//                .build();
//        return ApiResult.success(result);
//    }

    @ApiOperation(value = "/init", notes = "初始化汇聚点地图")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    public ApiResult<Map<String, Object>> assetMapInit(Integer parentId) {
        List<TreeSelect> menuTree = switcherBrunchService.getMapTreeAndData(parentId);
        List<SelectVO> buildType = constantItemService.findItemsByDict(BUILD_UNIT);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(DEPARTMENT_TREE, menuTree)
                .put("buildType", buildType)
                .build();
        return ApiResult.success(result);
    }

//    @ApiOperation(value = "/getPointList", notes = "获取点位列表")
//    @RequestMapping(value = "/getPointList", method = {RequestMethod.POST})
//    @ResponseBody
//    public ApiResult getPointListByArea(@RequestBody SwitchBranchMapInitVO initVO){
//        List<TreeSelect> pointList = switcherBrunchService.getPointListByArea(initVO.getAreaId(),initVO.getProjectIds(),"");
//        return ApiResult.success(pointList);
//    }


    @ApiOperation(value = "/map", notes = "获得交换机地图")
    @RequestMapping(value = "/map", method = {RequestMethod.POST})
    public ApiResult<List<SwitchBranchConvergeMapResVO>> map(@Valid @RequestBody SwitchBranchMapInitVO reqVO) {
        SwitchBranchConvergeMapResVO respVOList = switcherBrunchMapService.mapList(reqVO);
        return ApiResult.success(respVOList);
    }

    @ApiOperation(value = "/mapList", notes = "获得交换机地图列表分页")
    @RequestMapping(value = "/mapList", method = {RequestMethod.POST})
    public ApiResult<PageResult<AssetMapRespVO>> mapList(@Valid @RequestBody SwitchBranchSearchVO reqVO) {
        super.checkPageParams(reqVO, "id desc");
        PageResult<AssetMapRespVO> respVOList = switcherBrunchMapService.assetSwitchMapListPage(reqVO);
        return ApiResult.success();
    }

//    @ApiOperation(value = "/mapDetail", notes = "交换机地图查看信息")
//    @RequestMapping(value = "/mapDetail", method = {RequestMethod.GET})
//    public ApiResult<AssetMapRespVO> mapDetail(@RequestParam(value = "id") Integer id) {
////        AssetMapRespVO respVO = radiusEndPointService.assetMapDetail(id);
//        return ApiResult.success();
//    }

    @ApiOperation(value = "/updateMap", notes = "更新资产地图经纬度")
    @RequestMapping(value = "/updateMap", method = {RequestMethod.POST})
    public ApiResult updateMap(@Valid @RequestBody SwitchBranchMapUpdateVO updateReqVO, HttpServletRequest request) {
        SysSwitcherBranch sysSwitcher = switcherBrunchService.getById(updateReqVO.getId());
        if (sysSwitcher == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前交换机不存在#" + updateReqVO.getId());
        }
        sysSwitcher.setLat(updateReqVO.getLatitude());
        sysSwitcher.setLon(updateReqVO.getLongitude());
        switcherBrunchService.updateSelective(sysSwitcher);

//        LogOperation logOperation = new LogBuilder().session(getUserSession(request))
//                .module(LOG_MODULE_SWITCH_BRANCH)
//                .operation(GlobalConstants.LOG_OPERATION_ENABLE)
//                .content(JSON.toJSONString(updateReqVO))
//                .result(GlobalConstants.LOG_RESULT_SUCCESS).build();
//        logOperationService.save(logOperation);
        return ApiResult.success(MapBuilder.getSimpleMap("id", sysSwitcher.getId())).appendMessage("请求成功");

    }




}
