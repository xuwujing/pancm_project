package com.zans.portal.controller;

import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.service.IPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author xv
 * @since 2020/3/6 17:32
 */
@Api(value = "/rbac", tags = {"/rbac ~ 用户权限"})
@RestController
@RequestMapping("/rbac")
@Validated
@Slf4j
public class RbacController extends BasePortalController {

    @Autowired
    IPermissionService permissionService;

    @ApiOperation(value = "/menu", notes = "获得用户菜单")
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public ApiResult getMenu(HttpServletRequest request) {
        UserSession session = super.getUserSession(request);
        Integer userId = session.getUserId();
        return ApiResult.success(permissionService.getMenuTreeByUserId(userId,0));
    }

    @ApiOperation(value = "/validate", notes = "验证用户权限")
    @ApiImplicitParam(name = "route", value = "前端路由", required = true,
            dataType = "string", paramType = "query")
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public ApiResult isValid(@NotEmpty(message="前端路由必填") String route,
                             HttpServletRequest request) {
        UserSession session = super.getUserSession(request);
        Integer userId = session.getUserId();
        boolean valid = permissionService.isFrontRouteValid(route, userId);
        log.info("validate front route#{} {}, {}", route, userId, valid);
        return ApiResult.success(MapBuilder.getSimpleMap("validate", valid));
    }



}
