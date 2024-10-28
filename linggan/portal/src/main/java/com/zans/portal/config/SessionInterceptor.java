package com.zans.portal.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.zans.portal.util.JwtHelper;
import com.zans.portal.util.LoginCacheHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

import static com.zans.base.config.EnumErrorCode.NEED_LOGIN_AGAIN;

@Component
@Slf4j
public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Value("${security.user-verify}")
    String userVerify;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    LoginCacheHelper loginCacheHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (verify(request)) {
            return true;
        }

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
                JSONObject.toJSONString(ApiResult.error(NEED_LOGIN_AGAIN)));
        response.setContentType(FastJsonJsonView.DEFAULT_CONTENT_TYPE);
        return false;
    }

    private boolean verify(HttpServletRequest request) {

        if (GlobalConstants.USER_VERIFY_JWT.equalsIgnoreCase(userVerify)) {
            String token = request.getHeader("token");
//            log.info("verify request#{}， {}", request.getRequestURI(), token);
            if (token == null || "undefined".equals(token)) {
                return false;
            }
            Pair<Boolean, String> logoutCacheResult = loginCacheHelper.isLogout(token);
            if (logoutCacheResult.getValue0()) {
                log.info("token[{}] has logout before#{}", token, logoutCacheResult.getValue1());
                return false;
            }

            UserSession userSession = jwtHelper.getUserSession(token);
            boolean verity = (userSession != null);
//            log.info("jwt verify#{} -> {}, {}", token, verity, userSession);
            return verity;
        } else {
            HttpSession session = request.getSession();
            if (session != null && session.getAttribute("userId") != null) {
                return true;
            }
        }
        return false;
    }

}
