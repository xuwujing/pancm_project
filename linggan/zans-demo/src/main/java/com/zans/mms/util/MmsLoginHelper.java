package com.zans.mms.util;

import com.zans.base.config.GlobalConstants;
import com.zans.base.util.JwtHelper;
import com.zans.base.util.LoginCacheHelper;
import com.zans.mms.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.zans.base.config.GlobalConstants.*;

@Component("mmsLoginHelper")
@Slf4j
public class MmsLoginHelper {

    @Autowired()
    private JwtHelper jwtHelper;

    @Autowired
    private LoginCacheHelper loginCacheHelper;

    @Autowired
    private MmsLoginHelper httpHelper;

    private boolean isJwtVerify;

    public Map<String, Object> doSessionSign(String passport, SysUser user, HttpServletRequest request) {
        HttpSession s = request.getSession();
        s.setAttribute(SESSION_KEY_USER_ID, user.getId());
        s.setAttribute(SESSION_KEY_NICK_NAME, user.getNickName());
        s.setAttribute(SESSION_KEY_USER_NAME, user.getUserName());

        s.setAttribute(SESSION_KEY_ORG_ID, user.getMaintainNum());
        s.setAttribute(SESSION_KEY_AREA_ID_STR, user.getAreaIdStr());
        s.setAttribute(SESSION_KEY_ROLE_ID, user.getRoleNum());
        Map<String, Object> data = new HashMap<>();
        data.put(SESSION_KEY_USER_ID, user.getId());
        data.put(SESSION_KEY_USER_NAME, user.getUserName());
        data.put(SESSION_KEY_NICK_NAME, user.getNickName());
        data.put(SESSION_KEY_ORG_ID, user.getMaintainNum());
        data.put(SESSION_KEY_AREA_ID_STR, user.getAreaIdStr());
        data.put(SESSION_KEY_ROLE_ID, user.getRoleNum());
        return data;
    }


    @Value("${security.user-verify}")
    public void setUserVerify(String userVerify) {
        this.isJwtVerify = GlobalConstants.USER_VERIFY_JWT.equalsIgnoreCase(userVerify);
        log.info("isJwtVerify:{}", this.isJwtVerify);
    }


    public Map<String, Object> doJwtSign(String passport, SysUser user) {
//        String token = jwtHelper.sign(passport, user.getId().toString(), user.getNickName());
        String token = jwtHelper.signUser(user);
        Map<String, Object> data = new HashMap<>();
        data.put(SESSION_KEY_UID, user.getId());
        data.put(SESSION_KEY_USER_ID, user.getUserName());
        data.put(SESSION_KEY_USER_NAME, user.getUserName());
        data.put(SESSION_KEY_NICK_NAME, user.getNickName());
        data.put(SESSION_KEY_USER_PHONE, user.getMobile());
        data.put(SESSION_KEY_ROLE_ID, user.getRoleNum());
        data.put(SESSION_KEY_ORG_ID, user.getMaintainNum());
        data.put(SESSION_KEY_AREA_ID_STR, user.getAreaIdStr());
        data.put(SESSION_KEY_TOKEN, token);
        return data;
    }

    public Map<String, Object> doSign(String passport, SysUser user, HttpServletRequest request) {
        return isJwtVerify() ? doJwtSign(passport, user) : doSessionSign(passport, user, request);
    }


    public void doLoginOut(HttpServletRequest request) {
        if (isJwtVerify()) {
            String token = request.getHeader(SESSION_KEY_TOKEN);
            if (token != null) {
                loginCacheHelper.logout(token);
            }
        } else {
            HttpSession s = request.getSession();
            if (s != null) {
                s.removeAttribute(SESSION_KEY_UID);
                s.removeAttribute(SESSION_KEY_USER_ID);
            }
        }
    }

    public boolean isJwtVerify() {
        return isJwtVerify;
    }

    public Boolean doVerifyLogin(HttpServletRequest request) {
        return httpHelper.doVerifyLogin(request);
    }
}
