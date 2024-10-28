package com.zans.portal.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zans.base.annotion.Record;
import com.zans.base.util.SecurityHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.license.util.MachineIdentityUtil;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.TUserMapper;
import com.zans.portal.model.SysConstant;
import com.zans.portal.model.SysModule;
import com.zans.portal.model.TUser;
import com.zans.portal.service.*;
import com.zans.portal.util.*;
import com.zans.portal.vo.license.LicenseInfo;
import com.zans.portal.vo.user.MenuItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/login", tags = {"/login ~ 登录/退出"})
@RestController
@Validated
@Slf4j
public class LoginController extends BasePortalController {

    @Value("${security.user-verify}")
    String userVerify;

    @Autowired
    IUserService userService;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    LoginCacheHelper loginCacheHelper;

    @Autowired
    IPermissionService permissionService;

    @Autowired
    IModuleService moduleService;

    @Autowired
    ILicenseService licenseService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    ISysConstantService constantService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @ApiOperation(value = "机器码", notes = "机器码")
    @RequestMapping(value = "/machineCode", method = RequestMethod.GET)
    public ApiResult machineCode(){
        return ApiResult.success(MachineIdentityUtil.getMachineIdentity());
    }


    @ApiOperation(value = "授权码", notes = "返回授权码信息")
    @RequestMapping(value = "/license", method = RequestMethod.GET)
    public ApiResult licenseInfo(){
        LicenseInfo info = licenseService.getLicenseInfo();
        if (info == null) {
            return ApiResult.error(LICENSE_ERROR);
        }
        //测试时可以注释
        if (!info.getSerialNumber().equals(MachineIdentityUtil.getMachineIdentity())){
            return ApiResult.error(LICENSE_ERROR);
        }
        return ApiResult.success(info);
    }

    @ApiOperation(value = "/changeLicense", notes = "切换授权码")
    @RequestMapping(value = "/changeLicense", method = {RequestMethod.POST})
    public ApiResult changeLicense(@RequestParam("license") String license){
        return licenseService.changeLicense(license);
    }

    @ApiOperation(value = "登录", notes = "返回值：token: 登录成功后的token值、用户id、昵称。之后的请求请将token放入header中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passport", value = "登录账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码，用AES加密后传输", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Record(module = PORTAL_MODULE_LOGIN,operation = LOG_OPERATION_USER_LOGIN)
    public ApiResult login(@NotEmpty(message = "用户名必填") String passport, @NotEmpty(message = "密码必填") String password, HttpServletRequest request) throws LoginException {
        TUser user = userService.findUserByName(passport);
        if (user == null) {
            return ApiResult.error(LOGIN_USER_NULL);
        }

        if (user.getEnable() == GlobalConstants.STATUS_DISABLE) {
            return ApiResult.error(LOGIN_USER_DISABLE);
        }

        if (user.getLockStatus() != GlobalConstants.USER_LOCK_NONE) {
            return ApiResult.error(LOGIN_USER_LOCKED);
        }

        // TODO IP锁定、mac地址锁定

        // 错误密码，封锁 n 小时
        if (loginCacheHelper.isLoginLocked(passport)) {
//            throw new LoginException("密码错误");
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

        // TODO 异地登录

        // 正常
        Map<String, Object> data = this.doSign(passport, user, request);

        List<String> frontList = permissionService.getAllFrontRouteByUserId(user.getId());
        SysConstant constant = constantService.findConstantByKey(TOPO_DATA_TPYE);
        data.put("route", frontList);
        data.put("isAdmin",user.getIsAdmin());
        data.put("topoDataType",Integer.valueOf(constant.getConstantValue()) );
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();
        for (SelectVO selectVO : deviceList) {
            Object key = selectVO.getItemKey();
            String value = selectVO.getItemValue();
            selectVO.setItemValue((String) key);
            selectVO.setItemKey(value);
        }
        data.put("device_name",deviceList);
        //2020-11-23 新增用户登录获取环境配置
        setConfig(data);

        permissionService.getMenuTreeByUserId(user.getId(), 1);
        log.info("user:{} login success!", user.getUserName());
        return ApiResult.success(data);
    }

    private void setConfig(Map<String, Object> data) {
//        List<Map<String,Object>> configMap = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("env", PROJECT_ENV);
//        configMap.add(map);
        data.put("config", map);
    }

    private Map<String, Object> doSign(String passport, TUser user, HttpServletRequest request) {
        if (GlobalConstants.USER_VERIFY_JWT.equalsIgnoreCase(userVerify)) {
            log.info("jwt sign");
            return this.doJwtSign(passport, user);
        }
        return doSessionSign(passport, user, request);
    }

    private Map<String, Object> doSessionSign(String passport, TUser user, HttpServletRequest request) {
        HttpSession s = request.getSession();
        s.setAttribute(SESSION_KEY_USER_ID, user.getId());
        s.setAttribute(SESSION_KEY_PASSPORT, passport);
        s.setAttribute(SESSION_KEY_NICK_NAME, user.getNickName());
        Map<String, Object> data = new HashMap<>(3);
        data.put(SESSION_KEY_PASSPORT, passport);
        data.put(SESSION_KEY_USER_ID, user.getId());
        data.put(SESSION_KEY_NICK_NAME, user.getNickName());
        return data;
    }

    private Map<String, Object> doJwtSign(String passport, TUser user) {
        String token = jwtHelper.sign(passport, user.getId().toString(), user.getNickName());
        Map<String, Object> data = new HashMap<>(4);
        data.put(SESSION_KEY_PASSPORT, passport);
        data.put(SESSION_KEY_USER_ID, user.getId());
        data.put(SESSION_KEY_NICK_NAME, user.getNickName());
        data.put(SESSION_KEY_TOKEN, token);
        return data;
    }

    @ApiOperation(value = "退出登录", notes = "jwt方式，token放入黑名单，自动过期")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @Record(module = PORTAL_MODULE_LOGIN,operation = LOG_OPERATION_USER_LOGOUT)
    public ApiResult logout(HttpServletRequest request) {

        String username = getUser();
        TUser tUser = new TUser();
        tUser.setUserName(username);
        List<TUser> user = tUserMapper.select(tUser);
        Integer userId = 0;
        if (!CollectionUtils.isEmpty(user)){
            userId = user.get(0).getId();
        }
        Object onLineUser = redisUtil.get("onLineUser");
        List<Integer> list = JSONObject.parseObject(onLineUser.toString(), List.class);
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()){
            if (iterator.next().equals(userId)){
                iterator.remove();
            }
        }
        redisUtil.set("onLineUser", JSONArray.toJSON(list).toString(), SCHDULED_TASK_TIME);

        if (GlobalConstants.USER_VERIFY_JWT.equalsIgnoreCase(userVerify)) {
            String token = request.getHeader(SESSION_KEY_TOKEN);
            if (token != null) {

                loginCacheHelper.logout(token);
            }
        } else {
            HttpSession s = request.getSession();
            if (s != null) {
                s.removeAttribute(SESSION_KEY_USER_ID);
                s.removeAttribute(SESSION_KEY_PASSPORT);
                s.removeAttribute(SESSION_KEY_NICK_NAME);
            }
        }

        return ApiResult.success();
    }
    public String getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) {
            return "admin9";
        }
        String account = request.getParameter("passport");
        if (StringUtils.isEmpty(account)) {
            UserSession userSession = getUserSession(request);
            if (userSession != null) {
                return userSession.getUserName();
            }
            return "";
        }

        return request.getParameter("passport");
    }

    @ApiOperation(value = "/verify_login", notes = "验证用户是否登录")
    @RequestMapping(value = "/verify_login", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult<Boolean> doVerifyLogin(HttpServletRequest request) {

        boolean isLogin = httpHelper.doVerifyLogin(request);
        return ApiResult.success(isLogin);
    }

    @ApiOperation(value = "/constant/reset_login_count", notes = "清空登录错误次数")
    @RequestMapping(value = "/constant/reset_login_count", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passport", value = "登录账号", required = true, dataType = "String", paramType = "query"),
    })
    public ApiResult<Boolean> resetLoginCount(@NotEmpty(message = "用户名必填") String passport, HttpServletRequest request) {

        // 错误次数过多
        loginCacheHelper.removeLoginAttemptError(passport);
        return ApiResult.success();
    }

    @ApiOperation(value = "/constant/getCurRoute", notes = "获取当前界面用户route")
    @RequestMapping(value = "/constant/getCurRoute", method = {RequestMethod.POST})
    public ApiResult getCurRoute( @RequestParam(value = "route") String route,
        @RequestParam(value = "parentId")  Integer parentId, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        Integer userId = userSession.getUserId();
//        Integer userId = 74;
        List<MenuItem> menuItemList = permissionService.getMenuTreeByUserId(userId, 0);
        List<MenuItem> result = Lists.newArrayList();
        getMenuItem(menuItemList, result, route,parentId);
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/constant/findMenuList", notes = "")
    @RequestMapping(value = "/constant/findMenuList", method = {RequestMethod.POST})
    public ApiResult findMenuList() {
        List<SysModule> menuList = moduleService.findMenuList();
        return ApiResult.success(menuList);
    }


    /**
     * @Author pancm
     * @Description 迭代目录，获取当前的菜单(不包括子菜单)
     * @Date  2020/10/29
     * @Param [subItem, result, route]
     * @return void
     **/
    private void getMenuItem(List<MenuItem> subItem, List<MenuItem> result, String route,Integer parentId) {
        if (subItem != null && subItem.size() > 0) {
            for (MenuItem item : subItem) {
                if (item.getPath().startsWith(route) && item.getParentId().equals(parentId)) {
                    if (item.getPath().equals("/asset/branch")){
                        item.setPath("/asset/branch/statistics");
                    }
                    if (item.getPath().equals("/asset/guardline")){
                        item.setPath("/asset/guardline/statistics");
                    }
                    if (item.getPath().equals("/security/alert/setting")){
                        item.setPath("/security/alert/setting/index");
                    }
                    result.add(item);
                }
                if(result.size() == 0){
                    getMenuItem(item.getSubItem(), result, route,parentId);
                }

//                if (item.getSubItem() == null || item.getSubItem().size() == 0) {
//                    if (item.getPath().startsWith(route)) {
//                        result.add(item);
//                    }
//                    continue;
//                }
//                if(result.size() == 0){
//                    getMenuItem(item.getSubItem(), result, route);
//                }
            }
        }
    }
}
