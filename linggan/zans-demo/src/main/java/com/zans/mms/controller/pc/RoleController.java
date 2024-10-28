package com.zans.mms.controller.pc;

import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.mms.service.IBaseOrgRoleService;
import com.zans.mms.service.IModuleService;
import com.zans.mms.service.IPermissionService;
import com.zans.mms.vo.role.RoleEditVO;
import com.zans.mms.vo.role.RoleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.mms.config.MMSConstants.LOG_OPERATION_EDIT;
import static com.zans.mms.config.MMSConstants.MODULE_ROLE_SCHEME;

/**
 * @author xv
 * @since 2020/3/6 23:20
 */
@Api(value = "/role", tags = {"/role ~ 角色管理"})
@RestController
@RequestMapping("/role")
@Validated
@Slf4j
public class RoleController extends BaseController {


    @Autowired
    IPermissionService permissionService;

    @Autowired
    IModuleService moduleService;

    @Autowired
    IBaseOrgRoleService baseOrgRoleService;


    @ApiOperation(value = "/view", notes = "获取角色详细信息")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<RoleRespVO> getRoleById(@RequestParam(value = "id") @NotNull(message = "角色id必填") String id,@NotNull(message = "moduleType必填")Integer moduleType) {
        RoleRespVO role = baseOrgRoleService.findRoleById(id,moduleType);
        if (role == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("角色ID不存在#" + id);
        }
        Object obj = MapBuilder.getBuilder()
                .put("role", role)
                .put("tree", permissionService.findRolePermissionInTree(id,moduleType))
                .build();
        return ApiResult.success(obj);
    }
    @ApiOperation(value = "/list", notes = "获取角色列表")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ApiResult<List<RoleRespVO>> findAllRoles(@RequestParam(value = "roleName",defaultValue = "") String roleName) {
        List<RoleRespVO> roleList = baseOrgRoleService.findAllRoles(roleName);
        return ApiResult.success(roleList);
    }

    @ApiOperation(value = "/edit", notes = "修改角色")
    @ApiImplicitParam(name = "req", value = "修改角色请求", required = true, dataType = "RoleEditVO", paramType = "body")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @Record(module = MODULE_ROLE_SCHEME,operation = LOG_OPERATION_EDIT)
    public ApiResult editRole(@Valid @RequestBody RoleEditVO req,
                              HttpServletRequest request) {

        baseOrgRoleService.editRole(req);

        return ApiResult.success();
    }

}
