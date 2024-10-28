package com.zans.portal.util;

import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.zans.base.config.BaseConstants.VALID_IP_MAX_LENGTH;
import static com.zans.portal.config.GlobalConstants.*;

@Slf4j
@Component
public class HttpHelper {

    @Autowired
    JwtHelper jwtHelper;

    @Value("${security.user-verify}")
    String userVerify;

    @Value("${spring.profiles.active}")
    String active;

    public UserSession getUser(HttpServletRequest request) {
        UserSession session = null;
        if (GlobalConstants.USER_VERIFY_JWT.equalsIgnoreCase(userVerify)) {
            session = getUserOfJwt(request);
        } else {
            session = getUserOfSession(request);
        }
        String ip = getIpAddr(request);
        String traceId =getTraceId(request);
        if (session != null ) {
            session.setIp(ip);
            session.setTraceId(traceId);
        }

        return session;
    }

    private UserSession getUserOfSession(HttpServletRequest request) {
        HttpSession s = request.getSession();
        if (s == null) {
            return null;
        }

        UserSession userSession = new UserSession();
        try {
            userSession.setUserId(getIntSessionAttribute(s, SESSION_KEY_USER_ID));
            userSession.setNickName(getStringSessionAttribute(s, SESSION_KEY_NICK_NAME));
            userSession.setUserName(getStringSessionAttribute(s, SESSION_KEY_PASSPORT));
        } catch (Exception ex) {

        }
        return userSession;
    }

    private UserSession getUserOfJwt(HttpServletRequest request) {
        String token = request.getHeader(SESSION_KEY_TOKEN);
        if (token == null) {
            if (APP_ENV_LOCAL.equals(active) || APP_ENV_DEV.equals(active)) {
                return new UserSession(1, "dev_user", "内测用户","000001");
            } else {
                return null;
            }
        } else {
            return jwtHelper.getUserSession(token);
        }
    }

    private static String getStringSessionAttribute(HttpSession session, String key) {
        if (session == null || key == null) {
            return null;
        }
        Object val = session.getAttribute(key);
        return (val == null) ? null : val.toString();
    }

    private static Integer getIntSessionAttribute(HttpSession session, String key) {
        String val = getStringSessionAttribute(session, key);
        if (val == null) {
            return null;
        }
        Integer intValue = null;
        try {
            intValue = Integer.parseInt(val);
        } catch (Exception ex) {

        }
        return intValue;
    }

    public boolean doVerifyLogin(HttpServletRequest request) {
        if (GlobalConstants.USER_VERIFY_JWT.equalsIgnoreCase(userVerify)) {
            String token = request.getHeader(SESSION_KEY_TOKEN);
            if (token == null) {
                return false;
            }
            boolean verity = jwtHelper.verity(token);
//            log.info("jwt verify#{} -> {}", token, verity);
            return verity;
        } else {
            HttpSession session = request.getSession();
            if (session != null && session.getAttribute(SESSION_KEY_USER_ID) != null) {
                return true;
            }
        }
        return false;
    }

    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                log.info("request#{}", ipAddress);

                if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    try {
                        InetAddress inet = Inet4Address.getLocalHost();
                        ipAddress = inet.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > VALID_IP_MAX_LENGTH) {
                // "***.***.***.***".length()= 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
//        ipAddress = request.getRemoteAddr();
        log.info("ip-header#{}, {}", ipAddress, request.getRemoteAddr());

        return ipAddress;
    }


    private String getTraceId(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("reqId");
    }


}
