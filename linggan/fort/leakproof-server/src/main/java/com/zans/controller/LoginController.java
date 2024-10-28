package com.zans.controller;

import com.zans.constant.EnumErrorCode;
import com.zans.config.GlobalConstants;
import com.zans.model.SysUser;
import com.zans.service.ISysUserService;
import com.zans.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.zans.config.GlobalConstants.PROJECT_ENV;

@Api(value = "/login", tags = {"/login ~ 登录/退出"})
@RestController
@Validated
@Slf4j
public class LoginController extends BaseController {

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    LoginCacheHelper loginCacheHelper;

    @Autowired
    MmsLoginHelper mmsSessionHelper;

//    @Autowired
//    IPermissionService permissionService;


    @ApiOperation(value = "登录", notes = "返回值：token: 登录成功后的token值、用户id、昵称。之后的请求请将token放入header中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passport", value = "登陆账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码，用AES加密后传输", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    @Record
    public ApiResult login(@NotEmpty(message = "用户名必填") String passport, @NotEmpty(message = "密码必填") String password, HttpServletRequest request) {
        SysUser user = sysUserService.queryByIdOrUsername(null, passport);
        if (user == null) {
            return ApiResult.error(EnumErrorCode.LOGIN_USER_NULL.getMessage());
        }

        if (user.getEnable() == GlobalConstants.STATUS_DISABLE) {
            return ApiResult.error(EnumErrorCode.LOGIN_USER_DISABLE.getMessage());
        }

        if (user.getPcEnable() == GlobalConstants.STATUS_DISABLE) {
            return ApiResult.error(EnumErrorCode.LOGIN_USER_UNOPENED_PC.getMessage());
        }
        // TODO IP锁定、mac地址锁定
        // 错误密码，封锁 n 小时
        if (loginCacheHelper.isLoginLocked(passport)) {
            return ApiResult.error(EnumErrorCode.LOGIN_PASSWORD_ERROR_MAX.getMessage());
        }
        String desPassword;
        try {
            desPassword = AESUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            log.error("desEncrypt error", e);
            desPassword = password;
        }

        String passwordEncry = SecurityHelper.getMd5WithSalt(desPassword, user.getSalt());
        if (!passwordEncry.equals(user.getPassword())) {
            // 错误次数过多
            loginCacheHelper.increaseLoginAttemptError(passport);
            return ApiResult.error(EnumErrorCode.LOGIN_PASSWORD_ERROR.getMessage());
        }

        // TODO 异地登陆
        // 正常
        Map<String, Object> data = mmsSessionHelper.doSign(passport, user, request);

        //设置 角色权限缓存、菜单、路由
     //   setRouteMenu(user, data);
//        log.info("user:{} login success!", user.getUserName());


        //2020-11-23 新增用户登录获取环境配置
        setConfig(data);
        SysUser update = new SysUser();
        update.setId(user.getId());
        update.setLastLoginTime(new Date());
        sysUserService.update(update);
        log.info("user:{} login success!", user.getUserName());
        data.put("isAdmin",user.getIsAdmin());
        return ApiResult.success(data);
    }

//    /**
//     *  角色权限缓存、菜单、路由
//     * @param user
//     * @param data
//     */
//    private void setRouteMenu(SysUser user, Map<String, Object> data) {
//        List<String> frontList = permissionService.getAllFrontRouteByUser(user);
//        Set<String> frontSet = new HashSet<>(frontList);
//        if (MMSConstants.CHECK_GIS_PERM_FLAG.equals(user.getCheckGis()) ){
//            frontSet.add(MMSConstants.CHECK_GIS_PERM_STR);
//        }
//        data.put("route", frontSet);
//
//        data.put("route", frontList);
//
//        List<MenuItem> menuItems = permissionService.getMenuTreeByUser(user, PERM_CACHE_YES,MODULE_TYPE_PC);
//        data.put("menuItems", menuItems);
//    }

    private void setConfig(Map<String, Object> data) {
//        List<Map<String,Object>> configMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("env", PROJECT_ENV);
//        configMap.add(map);
        data.put("config", map);
    }

    @ApiOperation(value = "退出登录", notes = "jwt方式，token放入黑名单，自动过期")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
//    @Record
    public ApiResult logout(HttpServletRequest request) {
        this.mmsSessionHelper.doLoginOut(request);
        return ApiResult.success();
    }

    @ApiOperation(value = "/verify_login", notes = "验证用户是否登陆")
    @RequestMapping(value = "/verify_login", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult<Boolean> doVerifyLogin(HttpServletRequest request) {
        boolean isLogin = httpHelper.doVerifyLogin(request);
        return ApiResult.success(isLogin);
    }

    @ApiOperation(value = "/constant/reset_login_count", notes = "清空登录错误次数")
    @RequestMapping(value = "/constant/reset_login_count", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passport", value = "登陆账号", required = true, dataType = "String", paramType = "query"),
    })
    public ApiResult<Boolean> resetLoginCount(@NotEmpty(message = "用户名必填") String passport, HttpServletRequest request) {
        // 错误次数过多
        loginCacheHelper.removeLoginAttemptError(passport);
        return ApiResult.success();
    }

}