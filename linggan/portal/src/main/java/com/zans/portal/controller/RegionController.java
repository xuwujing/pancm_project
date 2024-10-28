package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysRegion;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.IRegionService;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.area.RegionRespVO;
import com.zans.portal.vo.common.TreeSelect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;
import static com.zans.portal.constants.PortalConstants.PORTAL_LOG_OPERATION_ONE_DELETE;

@Api(value = "/region", tags = {"/region ~ 行政区划管理"})
@RestController
@RequestMapping("/region")
@Validated
@Slf4j
public class RegionController extends BasePortalController {

    @Value("${api.area.city}")
    Integer city;

    @Autowired
    IRegionService regionService;
    @Autowired
    ILogOperationService logOperationService;

    @ApiOperation(value = "/tree", notes = "获得行政区划树")
    @RequestMapping(value = "/tree", method = {RequestMethod.GET})
    public ApiResult tree(String regionName) {
        List<TreeSelect> menuTree = regionService.getRegionTree(regionName);
        return ApiResult.success(menuTree);
    }

    @ApiOperation(value = "/insertOrUpdate", notes = "新增或修改行政区划")
    @ApiImplicitParam(name = "mergeVO", value = "新增或修改行政区划", required = true,
            dataType = "RegionRespVO", paramType = "body")
    @RequestMapping(value = "/insertOrUpdate", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_REGION,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insertOrUpdate(@Valid @RequestBody RegionRespVO mergeVO, HttpServletRequest request) {
        int count = regionService.getByRegionName(mergeVO.getRegionName(), mergeVO.getRegionId());
        if (count > 0) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("区域名称已存在#" + mergeVO.getRegionName());
        }
        SysRegion region = new SysRegion();
        BeanUtils.copyProperties(mergeVO, region);
        region.setCityId(city);
        Integer parentId = region.getParentId();
        if (mergeVO.getRegionId() == null) {
            regionService.insert(region);
            //生成region_path
            String regionPath = "";
            if (parentId == 0) {
                regionPath = "-0-" + region.getRegionId() + "-";
            } else {
                SysRegion parentRegion = regionService.getById(parentId);
                regionPath = parentRegion.getRegionPath() + region.getRegionId() + "-";
            }
            region.setRegionPath(regionPath);
        } else {
            SysRegion regionByid = regionService.getById(mergeVO.getRegionId());
            region.setRegionPath(regionByid.getRegionPath());
        }
        regionService.update(region);

        return ApiResult.success(MapBuilder.getSimpleMap("id", region.getRegionId())).appendMessage("请求成功");
    }


    @ApiOperation(value = "/delete", notes = "删除模块")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_REGION,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        SysRegion region = regionService.getById(id);
        if (region == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前行政区划不存在#" + id);
        }
        if (region.getParentId() == 0) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前行政区划非叶子节点，不允许删除#" + id);
        }
        regionService.delete(region);

        // 记录日志

        return ApiResult.success().appendMessage("删除成功");
    }

}
