package com.zans.controller;

import com.alibaba.druid.util.StringUtils;
import com.zans.config.GlobalConstants;
import com.zans.constant.EnumErrorCode;
import com.zans.model.SysUser;
import com.zans.model.UserSession;
import com.zans.service.ISysUserService;
import com.zans.utils.*;
import com.zans.vo.ApiResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Map;

import static com.zans.constant.SystemConstant.REGEX_MOBILE;
import static com.zans.constant.SystemConstant.SALT;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/18
 */
@RestController
@RequestMapping("user")
@Slf4j
public class SysUserController extends BaseController {

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    LoginCacheHelper loginCacheHelper;

    @Autowired
    MmsLoginHelper mmsSessionHelper;

    @Autowired
    protected HttpHelper httpHelper;

    @ApiOperation(value = "登录", notes = "返回值：token: 登录成功后的token值、用户id、昵称。之后的请求请将token放入header中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "登陆账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码，用AES加密后传输", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult login(@NotEmpty(message = "用户名必填")String userName, @NotEmpty(message = "密码必填") String password, HttpServletRequest request) {
        SysUser user = new SysUser();
        boolean matches = userName.matches(REGEX_MOBILE);
        if (matches) {
            user = sysUserService.queryByIdOrNameOrPhone(null,null,userName);
        }else {
            user = sysUserService.queryByIdOrNameOrPhone(null,userName,null);
        }
        if (user == null) {
            return ApiResult.error(EnumErrorCode.LOGIN_USER_NULL.getMessage());
        }

        if (user.getEnable() == GlobalConstants.STATUS_DISABLE) {
            return ApiResult.error(EnumErrorCode.LOGIN_USER_DISABLE.getMessage());
        }

        // TODO IP锁定、mac地址锁定
        // 错误密码，封锁 n 小时
        if (loginCacheHelper.isLoginLocked(userName)) {
            return ApiResult.error(EnumErrorCode.LOGIN_PASSWORD_ERROR_MAX.getMessage());
        }
        String desPassword;
        try {
            desPassword = AESUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            log.error("desEncrypt error", e);
            desPassword = password;
        }

        String passwordEncry = SecurityHelper.getMd5WithSalt(desPassword, SALT);
        if (!passwordEncry.equals(user.getPassword())) {
            // 错误次数过多
            loginCacheHelper.increaseLoginAttemptError(userName);
            return ApiResult.error(EnumErrorCode.LOGIN_PASSWORD_ERROR.getMessage());
        }

        // TODO 异地登陆
        // 正常
        Map<String, Object> data = mmsSessionHelper.doSign(userName, user, request);
        log.info("user:{} login success!", user.getUserName());
        data.put("isAdmin",user.getIsAdmin());
        return ApiResult.success(data);
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }

}
