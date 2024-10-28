package com.zans.mms.controller.applet;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.annotion.Record;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.config.GlobalConstants;
import com.zans.base.controller.BaseController;
import com.zans.base.util.*;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.model.SysUser;
import com.zans.mms.model.WechatUserWxbind;
import com.zans.mms.service.IPermissionService;
import com.zans.mms.service.ISysUserService;
import com.zans.mms.service.IWeChatService;
import com.zans.mms.service.IWechatUserWxbindService;
import com.zans.mms.util.MmsLoginHelper;
import com.zans.mms.vo.user.MenuItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.zans.base.config.EnumErrorCode.LOGIN_USER_DISABLE;
import static com.zans.base.config.EnumErrorCode.LOGIN_USER_UNOPENED_APP;
import static com.zans.mms.config.MMSConstants.MODULE_TYPE_APP;
import static com.zans.mms.config.PermissionConstans.PERM_CACHE_YES;

@Api(value = "/小程序login", tags = {"/小程序login ~ 登录/退出"})
@RestController
@Validated
@Slf4j
@RequestMapping("app")
public class AppLoginController extends BaseController {

    @Autowired
    ISysUserService sysUserService;


    @Autowired
    IWechatUserWxbindService wechatUserWxbindService;

    @Autowired
    IWeChatService weChatService;

    @Autowired
    MmsLoginHelper mmsLoginHelper;

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    IPermissionService permissionService;

    @Autowired
    LoginCacheHelper loginCacheHelper;


    @ApiOperation(value = "退出登录", notes = "jwt方式，token放入黑名单，自动过期")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @Record(opPlatform = MMSConstants.LOG_APP,module = MMSConstants.MODULE_SYSTEM_USER,operation = MMSConstants.LOG_OPERATION_LOG_OUT)
    public ApiResult logout(HttpServletRequest request) {
        mmsLoginHelper.doLoginOut(request);
        UserSession userSession = httpHelper.getUser(request);
        if(userSession!=null && !StringHelper.isEmpty(userSession.getUserName())){
            wechatUserWxbindService.deleteByUserName(userSession.getUserName());
        }
        return ApiResult.success("ok");
    }

    @ApiOperation(value = "/verify_login", notes = "验证用户是否登陆")
    @RequestMapping(value = "/verify_login", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult<Boolean> doVerifyLogin(HttpServletRequest request) {
        boolean isLogin = mmsLoginHelper.doVerifyLogin(request);
        return ApiResult.success(isLogin);
    }


    @RequestMapping(value = "/loginByWxCode", method = {RequestMethod.POST})
    @Record(log = false)
    public ApiResult<Map> loginByWxCode(@RequestParam("loginCode") String loginCode, HttpServletRequest request) {
        log.debug("loginCode:{}", loginCode);
        Map wxInfoMap = weChatService.getWxMiniAuthInfo(loginCode);
        log.debug("wxInfoMap - {}", wxInfoMap);
        if (wxInfoMap == null || wxInfoMap.isEmpty()) {
            return ApiResult.error("服务器异常，wx_http_api无法打开");
        }
        String sessionKey = (String) wxInfoMap.get("session_key");
        if (StringUtils.isEmpty(sessionKey)) {
            return appLoginTimeout("#http_skey");
        }
        String encryptedSessionKey = this.encryptSessionKey(sessionKey);
        if (StringUtils.isEmpty(encryptedSessionKey)) {
            return appLoginTimeout("#encrypt_skey");
        }
        String wxMiniAppId = weChatService.getWxMiniAppId();
        String openid = (String) wxInfoMap.get("openid");
        WechatUserWxbind bind = this.wechatUserWxbindService.queryByOpenId(wxMiniAppId, openid);
        wxInfoMap.put("isHave",1);
        if (bind != null) {
            SysUser sysUser = sysUserService.queryByIdOrUsername(null, bind.getUserName());
            if (sysUser != null) {
                if (sysUser.getEnable() == GlobalConstants.STATUS_DISABLE) {
                    return ApiResult.error(LOGIN_USER_DISABLE);
                }
                if (sysUser.getWechatEnable() == GlobalConstants.STATUS_DISABLE) {
                    return ApiResult.error(LOGIN_USER_UNOPENED_APP);
                }
                wxInfoMap = this.mapperSysUser(sysUser, wxInfoMap, request);
                updateLoginTime(sysUser);
                setRouteMenu(sysUser, wxInfoMap);
                wxInfoMap.put("isHave",sysUser.getCheckGis());
            }
        }
        wxInfoMap.put("encrypted_session_key", encryptedSessionKey);


        return ApiResult.success(wxInfoMap);
    }

    private void updateLoginTime(SysUser sysUser) {
        SysUser update = new SysUser();
        update.setId(sysUser.getId());
        update.setWechatLastLoginTime(new Date());
        sysUserService.update(update);
        WechatUserWxbind wechatUserWxbind = new WechatUserWxbind();
        wechatUserWxbind.setUserName(sysUser.getUserName());
        wechatUserWxbind.setUpdateTime(new Date());
        wechatUserWxbindService.update(wechatUserWxbind);
    }


    @ApiOperation(value = "/loginByWxPhone", notes = "根据wx.login的code登录")
    @RequestMapping(value = "/loginByWxPhone", method = {RequestMethod.POST})
    @Record(log = false)
    public ApiResult loginByWxPhone(@RequestParam("encryptedSessionKey") String encryptedSessionKey,
                                    @RequestParam("iv") String iv,
                                    @RequestParam("encryptedData") String encryptedData,
                                    @RequestParam("openId") String openId,
                                    @RequestParam("unionId") String unionId,
                                    HttpServletRequest request) {
        log.debug("user:{},encryptedSessionKey:{}, encryptedKey:{}, iv:{} ", encryptedSessionKey, encryptedData, iv);
        String sessionKey = desEncryptSessionKey(encryptedSessionKey);
        if (StringUtils.isEmpty(sessionKey)) {
            return appLoginTimeout("#key");
        }
        String data = WxUtil.decryptData(encryptedData, sessionKey, iv);
        if (StringUtils.isEmpty(data)) {
            return appLoginTimeout("#decrypt");
        }
        JSONObject jsonObject = JSONObject.parseObject(data);
        SysUser user = sysUserService.queryByMobile(null, jsonObject.getString("phoneNumber"));
        if (user == null) {
            return ApiResult.error(EnumErrorCode.LOGIN_USER_NULL, "用户不存在,请联系管理员");
        }

        if (user.getEnable() == GlobalConstants.STATUS_DISABLE) {
            return ApiResult.error(LOGIN_USER_DISABLE);
        }

        if (user.getWechatEnable() == GlobalConstants.STATUS_DISABLE) {
            return ApiResult.error(LOGIN_USER_UNOPENED_APP);
        }
        String wxMiniAppId = weChatService.getWxMiniAppId();
        WechatUserWxbind bind = this.wechatUserWxbindService.queryByOpenId(wxMiniAppId, openId);
        if (bind == null) {
            WechatUserWxbind wxBind = new WechatUserWxbind();
            wxBind.setWxAppid(wxMiniAppId);
            wxBind.setWxUnionid(unionId);
            wxBind.setWxOpenid(openId);
            wxBind.setUserName(user.getUserName());
            wxBind.setWxNicename(user.getNickName());
            wechatUserWxbindService.insert(wxBind);
        }else {
            updateLoginTime(user);
        }
        Map map = new HashMap();
        map.put("openid", openId);
        map.put("unionid", unionId);
        map.put("encrypted_session_key", encryptedSessionKey);
        map.put("isHave",user.getCheckGis());


        setRouteMenu(user, map);

        return apiResultSysUser(user, map, request);
    }

    /**
     *  角色权限缓存、菜单、路由
     * @param user
     * @param data
     */
    private void setRouteMenu(SysUser user, Map data) {
        List<String> frontList = permissionService.getAllFrontRouteByUser(user);
        Set<String> frontSet = new HashSet<>(frontList);
        if (MMSConstants.CHECK_GIS_PERM_FLAG.equals(user.getCheckGis()) ){
            frontSet.add(MMSConstants.CHECK_GIS_PERM_STR);
        }
        data.put("route", frontSet);

        List<MenuItem> menuItems = permissionService.getMenuTreeByUser(user, PERM_CACHE_YES,MODULE_TYPE_APP);
        data.put("menuItems", menuItems);
    }

    private String desEncryptSessionKey(String encryptedSessionKey) {
        try {
            String sessionKey = AESUtil.desEncrypt(encryptedSessionKey).trim();
            log.debug("session-key:{}", sessionKey);
            return sessionKey;
        } catch (Exception e) {
            log.error("sessionKey解密失败", e);
            return null;
        }
    }


    private String encryptSessionKey(String sessionKey) {
        try {
            log.debug("session-key:{}", sessionKey);
            String encrypted_session_key = AESUtil.encrypt(sessionKey);
            log.debug("encrypted_session_key: {}", encrypted_session_key);
            return encrypted_session_key;
        } catch (Exception e) {
            log.error("sessionKey加密失败", e);
            return null;
        }
    }

    private ApiResult appLoginTimeout(String code) {
        return ApiResult.error(String.format("登录超时，请重新打开微信小程序%s", code));
    }

    private ApiResult apiResultSysUser(SysUser user, Map map, HttpServletRequest request) {
        if (map == null) {
            map = new HashMap<>();
        }
        map = mapperSysUser(user, map, request);
        return ApiResult.success(map);
    }

    private Map mapperSysUser(SysUser user, Map map, HttpServletRequest request) {
        Map<String, Object> signMap = mmsLoginHelper.doSign(user.getUserName(), user, request);
        if (signMap != map) {
            map.putAll(signMap);
        }
        return map;
    }

}
