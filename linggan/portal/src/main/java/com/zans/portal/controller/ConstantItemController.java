package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysConstantItem;
import com.zans.portal.service.*;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.constant.ConstantItemReqVO;
import com.zans.portal.vo.constant.ConstantItemSearchVO;
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

@Api(value = "/constant", tags = {"/constant ~ 系统字典表详情管理"})
@RestController
@RequestMapping("/constant")
@Validated
@Slf4j
public class ConstantItemController extends BasePortalController {

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IDeviceTypeService deviceTypeService;



    @Autowired
    IModuleService moduleService;

    @Autowired
    ILogOperationService logOperationService;

    @ApiOperation(value = "/item/list", notes = "系统字典表详情列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantItemSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/item/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list_constant_item(@RequestBody ConstantItemSearchVO req) {
        super.checkPageParams(req, "ordinal ASC,id DESC");

        List<SelectVO> moduleu = moduleService.findModuleToSelect();

        PageResult<ConstantItemReqVO> pageResult = constantItemService.getConstantItemPage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .put(MODULE_MODULE, moduleu)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/item/insert", notes = "新增字典详情表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantItemReqVO", paramType = "body")
    })
    @RequestMapping(value = "/item/insert", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT_DICT,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insert_constant_item(@Valid @RequestBody ConstantItemReqVO req, HttpServletRequest request) {
        SysConstantItem constant = new SysConstantItem();
        BeanUtils.copyProperties(req, constant);
        SysConstantItem item = constantItemService.findByDictKeyAndItemKey(req.getDictKey(), req.getItemKey(), req.getId());
        if (item != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前键已存在#" + req.getItemKey());
        }
        Integer ordinal = constantItemService.getOrdinalByDictKeyAndItemKey(req.getDictKey(), "") + 1;
        constant.setOrdinal((byte) ordinal.intValue());
        constantItemService.insert(constant);


        return ApiResult.success(MapBuilder.getSimpleMap("id", constant.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/item/update", notes = "修改字典详情表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantItemReqVO", paramType = "body")
    })
    @RequestMapping(value = "/item/update", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT_DICT,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult update_constant_item(@RequestBody ConstantItemReqVO req, HttpServletRequest request) {
        SysConstantItem constant = constantItemService.getById(req.getId());
        if (constant == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("系统字典表详情不存在#" + req.getId());
        }
        SysConstantItem item = constantItemService.findByDictKeyAndItemKey(req.getDictKey(), req.getItemKey(), req.getId());
        if (item != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前键已存在#" + req.getItemKey());
        }
        BeanUtils.copyProperties(req, constant);

        if (constant.getOrdinal() == null){
            Integer ordinal = constantItemService.getOrdinalByDictKeyAndItemKey(req.getDictKey(), "") + 1;
            constant.setOrdinal((byte) ordinal.intValue());
        }
        constantItemService.update(constant);

        return ApiResult.success(MapBuilder.getSimpleMap("id", constant.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/item/delete", notes = "删除系统字典表详情数据")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/item/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT_DICT,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete_constant_item(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        SysConstantItem constant = constantItemService.getById(id);
        if (constant == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("系统字典表不存在#" + id);
        }
        constantItemService.delete(constant);

        // 记录日志

        return ApiResult.success().appendMessage("删除成功");
    }

    @ApiOperation(value = "/item/move", notes = "排序上下移动数据")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/item/move", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT_DICT,operation = LOG_OPERATION_ITEM_MOVE)
    public ApiResult move(@NotNull(message = "id必填") Integer id, @NotNull(message = "id必填") Integer type, HttpServletRequest request) {
        SysConstantItem constant = constantItemService.getById(id);
        if (constant == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("系统字典表不存在#" + id);
        }
        //type = 1 上移，type = 2 下移
        String dictKey = constant.getDictKey();
        if (constant.getOrdinal() == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前字典序号为空，请重新保存生成序号后使用#" + constant.getOrdinal());
        }
        int ordinal = (int) constant.getOrdinal();
        SysConstantItem item = new SysConstantItem();
        if (type == 1) {
            item = constantItemService.findItemsByDictAndOridinal(dictKey, ordinal - 1);
            if (item == null) {
                return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前元素已是最上级#" + id);
            }
            constant.setOrdinal((byte) (ordinal - 1));
            item.setOrdinal((byte) (item.getOrdinal() + 1));
        } else {
            item = constantItemService.findItemsByDictAndOridinal(dictKey, ordinal + 1);
            if (item == null) {
                return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前元素已是最下级#" + id);
            }
            constant.setOrdinal((byte) (ordinal + 1));
            item.setOrdinal((byte) (item.getOrdinal() - 1));
        }
        constantItemService.update(constant);
        constantItemService.update(item);
        return ApiResult.success().appendMessage("删除成功");
    }

    @ApiOperation(value = "/item/find_select_items", notes = "系统字典表详情查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "常量主键", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/item/find_select_items", method = {RequestMethod.GET})
    public ApiResult findItemsByModule(String key) {
        List<SelectVO> list = constantItemService.findItemsByDict(key);
        return ApiResult.success(list);
    }

    @ApiOperation(value = "/item/export", notes = "返回所有字典表详情信息")
    @RequestMapping(value = "/item/export", method = {RequestMethod.GET})
    @Record
    public ApiResult export(HttpServletRequest request) {
        List<String> modules = constantItemService.getDictKeys();
        MapBuilder builder = MapBuilder.getBuilder();
        for (String moduleKey : modules) {
            List<SelectVO> list = constantItemService.findItemsByDict(moduleKey);
            builder.put(moduleKey, list);
        }


        builder.put(MODULE_DEVICE, deviceTypeService.findDeviceTypeToSelect());
        builder.put(MODULE_DEVICE_TEMPLATE, deviceTypeService.findDeviceTypeHasTemplateToSelect());
        builder.put(MODULE_REGION_FIRST, areaService.findRegionToSelect(REGION_LEVEL_ONE));
        builder.put(MODULE_REGION_SECOND, areaService.findRegionToSelect(REGION_LEVEL_TWO));
        builder.put(MODULE_REGION_THIRD, areaService.findRegionToSelect(REGION_LEVEL_THREE));
        builder.put(MODULE_ROLE, roleService.findRoleToSelect());
        builder.put(MODULE_MODULE, moduleService.findModuleToSelect());

        return ApiResult.success(builder.build());
    }


}
