package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.SysRole;
import com.zans.portal.service.IModuleService;
import com.zans.portal.service.IPermissionService;
import com.zans.portal.service.IRoleService;
import com.zans.portal.vo.role.RoleAddVO;
import com.zans.portal.vo.role.RoleAndUserRespVO;
import com.zans.portal.vo.role.RoleEditVO;
import com.zans.portal.vo.role.RoleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
 * @since 2020/3/6 23:20
 */
@Api(value = "/role", tags = {"/role ~ 角色管理"})
@RestController
@RequestMapping("/role")
@Validated
@Slf4j
public class RoleController extends BasePortalController {

    @Autowired
    IRoleService roleService;

    @Autowired
    IPermissionService permissionService;

    @Autowired
    IModuleService moduleService;

    @ApiOperation(value = "/list", notes = "获取角色列表")
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult<List<RoleRespVO>> findAllRoles(@RequestParam("roleName") String roleName) {
        List<RoleRespVO> roleList = roleService.findAllRoles(roleName,null);
        Object obj = MapBuilder.getBuilder()
                .put("role", roleList).build();
        return ApiResult.success(obj);
    }

    @ApiOperation(value = "/view", notes = "获取角色详细信息")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<RoleRespVO> getRoleById(@NotNull(message = "角色id必填") Integer id) {
        RoleRespVO role = roleService.findRoleById(id);
        if (role == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("角色ID不存在#" + id);
        }
        Object obj = MapBuilder.getBuilder()
                .put("role", role)
                .put("tree", permissionService.findAllPermissionInTree())
                .build();
        return ApiResult.success(obj);
    }

    @ApiOperation(value = "/add", notes = "新增角色")
    @ApiImplicitParam(name = "req", value = "新增角色请求", required = true, dataType = "RoleAddVO", paramType = "body")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ROLE,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult addRole(@Valid @RequestBody RoleAddVO req,
                             HttpServletRequest request) {
        SysRole sameNameRole = roleService.findRoleByName(req.getRoleName());
        if (sameNameRole != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("角色名称已存在#" + req.getRoleName());
        }
        this.roleService.saveRole(req);
        return ApiResult.success();
    }


    @ApiOperation(value = "/edit", notes = "修改角色")
    @ApiImplicitParam(name = "req", value = "修改角色请求", required = true, dataType = "RoleEditVO", paramType = "body")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ROLE,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult editRole(@Valid @RequestBody RoleEditVO req,
                              HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        SysRole sameNameRole = roleService.findRoleByNameExceptId(req.getRoleName(), req.getRoleId());
        if (sameNameRole != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("角色名称已存在#" + req.getRoleName());
        }

        int id = roleService.editRole(req);
        if (id == -1) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("角色ID不存在#" + req.getRoleId());
        }

        //重载缓存数据
        permissionService.getNoCacheMenuTreeByUserId(userSession.getUserId());

        return ApiResult.success();
    }

    @ApiOperation(value = "/change_enable", notes = "启用/禁用角色")
    @RequestMapping(value = "/change_enable", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ROLE,operation = PORTAL_MODULE_EN_ABLE)
    public ApiResult changeRoleEnable(@NotNull(message = "角色id必填") Integer id,
                                      @NotNull(message = "状态必填") @Min(value = 0) @Max(value = 1) Integer status,
                                      HttpServletRequest request) {
        SysRole role = roleService.getById(id);
        if (role == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("角色ID不存在#" + id);
        }
        role.setEnable(status);
        roleService.update(role);
        return ApiResult.success();
    }


    @ApiOperation(value = "/delete", notes = "删除角色")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ROLE,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "角色id必填") Integer id,
                                      HttpServletRequest request) {
        SysRole role = roleService.getById(id);
        if (role == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("角色ID不存在#" + id);
        }
        List<RoleAndUserRespVO> roleAndUserRespVOList = roleService.findRoleAndUserById(id);
        if(roleAndUserRespVOList!=null && roleAndUserRespVOList.size()>0){
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("该角色下有账号,无法进行删除！");
        }
        //需要判断改角色状态是否被禁用了
        if(role.getEnable() == 1){
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("该角色未禁用！请禁用后再删除！");
        }
        roleService.delete(role);
        return ApiResult.success();
    }


}
