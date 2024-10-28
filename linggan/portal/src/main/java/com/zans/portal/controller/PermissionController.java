package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.SysPermission;
import com.zans.portal.service.IPermissionService;
import com.zans.portal.util.Router;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.perm.PermissionAddVO;
import com.zans.portal.vo.perm.PermissionEditVO;
import com.zans.portal.vo.perm.PermissionRespVO;
import com.zans.portal.vo.role.RoleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.constants.PortalConstants.*;

/**
 * @author xv
 * @since 2020/3/7 16:16
 */
@Api(value = "/perm", tags = {"/perm ~ 权限管理"})
@RestController
@RequestMapping("/perm")
@Validated
@Slf4j
public class PermissionController extends BasePortalController {

    @Autowired
    IPermissionService permissionService;

    @ApiOperation(value = "/list", notes = "权限树")
    @ApiImplicitParams({@ApiImplicitParam(name = "permName",value = "权限名称")})
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ApiResult gePermissionPageList(String permName) {
        return ApiResult.success(permissionService.findAllPermissionInTree(permName));
    }

    @ApiOperation(value = "/tree", notes = "获得权限树")
    @RequestMapping(value = "/tree", method = {RequestMethod.GET})
    public ApiResult<RoleRespVO> getRoleById() {
        return ApiResult.success(permissionService.findAllPermissionInTree());
    }

    @ApiOperation(value = "/select", notes = "获得模块树")
    @RequestMapping(value = "/select", method = {RequestMethod.GET})
    public ApiResult<TreeSelect> getModuleTree() {
        List<TreeSelect> moduleList = permissionService.findAllModuleInTree();
        TreeSelect root = TreeSelect.builder().id(0).label("根节点").children(moduleList).build();
        return ApiResult.success(root);
    }

    @ApiOperation(value = "/add", notes = "新增权限")
    @ApiImplicitParam(name = "req", value = "新增权限", required = true, dataType = "PermissionAddVO", paramType = "body")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_PERMISSION,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult addPermission(@Valid @RequestBody PermissionAddVO req,
                                   HttpServletRequest request) {
        PermissionRespVO sameNamePermission = permissionService.findPermissionByName(req.getPermName());
        if (sameNamePermission != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("权限名称已存在#" + req.getPermName());
        }
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(req, permission);
        this.permissionService.save(permission);
        return ApiResult.success();
    }


    @ApiOperation(value = "/edit", notes = "修改权限")
    @ApiImplicitParam(name = "req", value = "修改权限请求", required = true, dataType = "PermissionEditVO", paramType = "body")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_PERMISSION,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult editPermission(@Valid @RequestBody PermissionEditVO req,
                                    HttpServletRequest request) {
        PermissionRespVO sameNamePermission = permissionService.findPermissionByNameExceptId(req.getPermName(), req.getPermId());
        if (sameNamePermission != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("权限名称已存在#" + req.getPermName());
        }
        SysPermission permission = permissionService.getById(req.getPermId());
        BeanUtils.copyProperties(req, permission);
        permissionService.update(permission);
        return ApiResult.success();
    }

    @ApiOperation(value = "/change_enable", notes = "启用/禁用权限")
    @RequestMapping(value = "/change_enable", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_PERMISSION,operation = PORTAL_MODULE_EN_ABLE)
    public ApiResult changePermissionEnable(@NotNull(message = "权限ID必填") Integer id,
                                            @NotNull(message = "状态必填") @Min(value = 0) @Max(value = 1) Integer status,
                                            HttpServletRequest request) {
        SysPermission permission = permissionService.getById(id);
        if (permission == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("权限ID不存在#" + id);
        }
        permission.setEnable(status);
        permissionService.update(permission);
        return ApiResult.success();
    }

    @ApiOperation(value = "/getPasswdPermission", notes = "获取当前模块是否具有查看密码权限")
    @RequestMapping(value = "/getPasswdPermission", method = {RequestMethod.POST})
    public ApiResult getPasswdPermission(@NotNull(message = "路由必填") String route, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        String current_route = permissionService.getpasswdPermission(route);
        if (StringUtils.isEmpty(current_route)) {
            return ApiResult.error("无权限");
        }
        Router rout = permissionService.getUserFrontRoute(userSession.getUserId());
        List<String> routers = rout.toList();
        for (String router : routers) {
            if (router.equals(current_route)) {
                return ApiResult.success();
            }
        }
        return ApiResult.error("无权限");
    }

}
