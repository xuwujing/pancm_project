package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysModule;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.IModuleService;
import com.zans.portal.service.IPermissionService;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.perm.ModuleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import static com.zans.portal.config.GlobalConstants.LOG_MODULE_MODULE;
import static com.zans.portal.config.GlobalConstants.LOG_OPERATION_EDIT;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/module", tags = {"/module ~ 模块管理"})
@RestController
@RequestMapping("/module")
@Validated
@Slf4j
public class ModuleController extends BasePortalController {

    @Autowired
    IModuleService moduleService;
    @Autowired
    IPermissionService permissionService;
    @Autowired
    ILogOperationService logOperationService;

    @ApiOperation(value = "/tree", notes = "获得模块树")
    @RequestMapping(value = "/tree", method = {RequestMethod.GET})
    public ApiResult tree(String moduleName) {
        List<TreeSelect> menuTree = moduleService.getMenuTree(moduleName);
        return ApiResult.success(menuTree);
    }

    @ApiOperation(value = "/insertOrUpdate", notes = "新增或修改模块")
    @ApiImplicitParam(name = "mergeVO", value = "新增或修改模块", required = true,
            dataType = "ModuleRespVO", paramType = "body")
    @RequestMapping(value = "/insertOrUpdate", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_MODULE,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insertOrUpdate(@Valid @RequestBody ModuleRespVO mergeVO, HttpServletRequest request) {
        SysModule module = new SysModule();
        BeanUtils.copyProperties(mergeVO, module);
//        int count = moduleService.findByName(mergeVO.getName(), mergeVO.getId());
//        if (count > 0) {
//            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("模块名称已存在#" + mergeVO.getName());
//        }
//        int routecount = moduleService.findByRoute(mergeVO.getRoute(), mergeVO.getId());
//        if (routecount > 0) {
//            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("模块路由已存在#" + mergeVO.getName());
//        }
        if (mergeVO.getId() == null) {
            int seq = moduleService.findSeq(mergeVO.getParentId());
            module.setSeq(seq);
            moduleService.insert(module);
            module.setSort(module.getParentId() + "-" + module.getId());
        }
        moduleService.update(module);

        return ApiResult.success(MapBuilder.getSimpleMap("id", module.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/delete", notes = "删除模块")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_MODULE,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        SysModule module = moduleService.getById(id);
        if (module == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("模块不存在#" + id);
        }
        //非叶子节点，不允许删除；
        //sys_permission 中 module 不为空的，不允许删除
        if (module.getParentId() == 0) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("非叶子节点，不允许删除#" + module.getName());
        }
        int count = permissionService.findByModuleId(id);
        if (count > 0) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("权限表中已使用，不允许删除#" + module.getName());
        }
        moduleService.delete(module);

        // 记录日志

        return ApiResult.success().appendMessage("删除成功");
    }

    // 1 -启用，0 - 禁用
    @ApiOperation(value = "/enable", notes = "启用/禁用")
    @RequestMapping(value = "/enable", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_MODULE,operation = PORTAL_MODULE_EN_ABLE)
    public ApiResult enable(Integer id, Integer enable,
                            HttpServletRequest request) {
        SysModule module = moduleService.getById(id);
        if (module == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("模块不存在#" + id);
        }
        module.setEnable(enable);
        moduleService.update(module);
        return ApiResult.success().appendMessage("操作成功");
    }

}
