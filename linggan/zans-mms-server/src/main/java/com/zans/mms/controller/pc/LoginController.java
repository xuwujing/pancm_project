package com.zans.mms.controller.pc;

import com.zans.base.annotion.Record;
import com.zans.base.config.GlobalConstants;
import com.zans.base.controller.BaseController;
import com.zans.base.util.*;
import com.zans.base.vo.ApiResult;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.SysUser;
import com.zans.mms.service.IModuleService;
import com.zans.mms.service.IPermissionService;
import com.zans.mms.service.ISysUserService;
import com.zans.mms.util.MmsLoginHelper;
import com.zans.mms.vo.user.MenuItem;
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
import java.util.*;

import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.base.config.GlobalConstants.PROJECT_ENV;
import static com.zans.mms.config.MMSConstants.*;
import static com.zans.mms.config.PermissionConstans.PERM_CACHE_YES;

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

    @Autowired
    IPermissionService permissionService;

    @Autowired
    IModuleService moduleService;


    @ApiOperation(value = "登录", notes = "返回值：token: 登录成功后的token值、用户id、昵称。之后的请求请将token放入header中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passport", value = "登陆账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码，用AES加密后传输", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Record(module = MODULE_SYSTEM_USER,operation = LOG_OPERATION_LOGIN)
    public ApiResult login(@NotEmpty(message = "用户名必填") String passport, @NotEmpty(message = "密码必填") String password, HttpServletRequest request) {
        SysUser user = sysUserService.queryByIdOrUsername(null, passport);
        if (user == null) {
            return ApiResult.error(LOGIN_USER_NULL);
        }

        if (user.getEnable() == GlobalConstants.STATUS_DISABLE) {
            return ApiResult.error(LOGIN_USER_DISABLE);
        }

        if (user.getPcEnable() == GlobalConstants.STATUS_DISABLE) {
            return ApiResult.error(LOGIN_USER_UNOPENED_PC);
        }
        // TODO IP锁定、mac地址锁定
        // 错误密码，封锁 n 小时
        if (loginCacheHelper.isLoginLocked(passport)) {
            return ApiResult.error(LOGIN_PASSWORD_ERROR_MAX);
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
            return ApiResult.error(LOGIN_PASSWORD_ERROR);
        }

        // TODO 异地登陆
        // 正常
        Map<String, Object> data = mmsSessionHelper.doSign(passport, user, request);

        //设置 角色权限缓存、菜单、路由
        setRouteMenu(user, data);
        setDefaultIssueSource(user, data);
        log.info("user:{} login success!", user.getUserName());


        //2020-11-23 新增用户登录获取环境配置
        setConfig(data);
        SysUser update = new SysUser();
        update.setId(user.getId());
        update.setLastLoginTime(new Date());
        sysUserService.update(update);
        log.info("user:{} login success!", user.getUserName());
        return ApiResult.success(data);
    }

    private void setDefaultIssueSource(SysUser user, Map<String, Object> data) {
        String roleNum = user.getRoleNum();
        if (roleNum.equals(INSIDE_ROLE)){
            //前提条件 (role == 运维单位内场)
            data.put("defaultIssueSource",11);
        }
    }

    /**
     *  角色权限缓存、菜单、路由
     * @param user
     * @param data
     */
    private void setRouteMenu(SysUser user, Map<String, Object> data) {
        List<String> frontList = permissionService.getAllFrontRouteByUser(user);
        Set<String> frontSet = new HashSet<>(frontList);
        if (MMSConstants.CHECK_GIS_PERM_FLAG.equals(user.getCheckGis()) ){
            frontSet.add(MMSConstants.CHECK_GIS_PERM_STR);
        }
        data.put("route", frontSet);

        data.put("route", frontList);

        List<MenuItem> menuItems = permissionService.getMenuTreeByUser(user, PERM_CACHE_YES,MODULE_TYPE_PC);
        data.put("menuItems", menuItems);
    }

    private void setConfig(Map<String, Object> data) {
//        List<Map<String,Object>> configMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("env", PROJECT_ENV);
//        configMap.add(map);
        data.put("config", map);
    }

    @ApiOperation(value = "退出登录", notes = "jwt方式，token放入黑名单，自动过期")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @Record
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
