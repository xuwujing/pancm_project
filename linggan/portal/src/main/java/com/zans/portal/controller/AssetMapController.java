package com.zans.portal.controller;


import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.service.*;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.asset.map.AssetMapConvergeRespVO;
import com.zans.portal.vo.asset.map.AssetMapInitReqVO;
import com.zans.portal.vo.asset.req.AssetMapUpdateReqVO;
import com.zans.portal.vo.asset.req.AssetReqVO;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.common.TreeSelect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.BUILD_UNIT;
import static com.zans.portal.config.GlobalConstants.DEPARTMENT_TREE;
import static com.zans.portal.constants.PortalConstants.PORTAL_LOG_OPERATION_EDIT;
import static com.zans.portal.constants.PortalConstants.PORTAL_MODULE_ASSET_MAP;

@Api(tags = {"/asset/map ~ 资产地图"})
@RestController
@Validated
@RequestMapping("/asset/map")
@Slf4j
public class AssetMapController extends BasePortalController {

    @Autowired
    ILogOperationService logOperationService;
    @Autowired
    IDeviceTypeService deviceTypeService;
    @Autowired
    IAreaService areaService;
    @Autowired
    IRadiusEndPointService radiusEndPointService;
    @Autowired
    IConstantItemService constantItemService;
    @Autowired
    IAssetMapService assetMapService;


    @ApiOperation(value = "/assetMapInit", notes = "初始化资产地图")
    @RequestMapping(value = "/assetMapInit", method = {RequestMethod.GET})
    public ApiResult<Map<String, Object>> assetMapInit(Integer parentId) {
        List<TreeSelect> menuTree = assetMapService.getMapTreeAndData(parentId);
        List<SelectVO> buildType = constantItemService.findItemsByDict(BUILD_UNIT);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(DEPARTMENT_TREE, menuTree)
                .put("buildType", buildType)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/getMapTree", notes = "初始化资产地图结构")
    @RequestMapping(value = "/getMapTree", method = {RequestMethod.GET})
    public ApiResult<Map<String, Object>> getMapTree(Integer parentId) {
        List<TreeSelect> menuTree = assetMapService.getMapTree(parentId);
        List<SelectVO> buildType = constantItemService.findItemsByDict(BUILD_UNIT);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(DEPARTMENT_TREE, menuTree)
                .put("buildType", buildType)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/getMapTreeData", notes = "初始化资产地图")
    @RequestMapping(value = "/getMapTreeData", method = {RequestMethod.GET})
    public ApiResult<Map<String, Object>> getMapTreeData(Integer assetBranchId) {
        List<AssetBaselineVO> menuTree = assetMapService.assetMapForTreeList(assetBranchId);
        return ApiResult.success(menuTree);
    }



    @ApiOperation(value = "/assetMap", notes = "获得资产地图")
    @RequestMapping(value = "/assetMap", method = {RequestMethod.POST})
    public ApiResult<AssetMapConvergeRespVO> assetMap(@Valid @RequestBody AssetMapInitReqVO reqVO) {

        AssetMapConvergeRespVO respVO = assetMapService.assetMap(reqVO);
        respVO.setReqVO(reqVO);
        return ApiResult.success(respVO);
    }


    @ApiOperation(value = "/updateAssetMap", notes = "更新资产地图经纬度")
    @RequestMapping(value = "/updateAssetMap", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ASSET_MAP, operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult updateAssetMap(@Valid @RequestBody AssetMapUpdateReqVO updateReqVO) {
        return assetMapService.updateAssetMap(updateReqVO);
    }

    @ApiOperation(value = "/assetMapList", notes = "获得资产地图列表分页")
    @RequestMapping(value = "/assetMapList", method = {RequestMethod.POST})
    public ApiResult<PageResult<AssetMapRespVO>> assetMapList(@Valid @RequestBody AssetReqVO reqVO) {
        super.checkPageParams(reqVO, "id desc");
        PageResult<AssetMapRespVO> respVOList = assetMapService.assetMapListPage(reqVO);
        return ApiResult.success(respVOList);
    }
}
