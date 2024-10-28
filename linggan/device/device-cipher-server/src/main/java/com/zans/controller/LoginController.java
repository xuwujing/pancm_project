package com.zans.controller;

import com.zans.service.ISysUserService;
import com.zans.utils.HttpHelper;
import com.zans.utils.JwtHelper;
import com.zans.utils.LoginCacheHelper;
import com.zans.utils.MmsLoginHelper;
import com.zans.vo.ApiResult;
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
