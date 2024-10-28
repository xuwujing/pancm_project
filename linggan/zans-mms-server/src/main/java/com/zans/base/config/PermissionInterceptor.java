package com.zans.base.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.service.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

import static com.zans.base.config.EnumErrorCode.PERMISSION_NONE;
import static com.zans.base.config.GlobalConstants.DEFAULT_SERVER_CONTEXT;

@Slf4j
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    IPermissionService permissionService;

    @Autowired
    HttpHelper httpHelper;

    @Value("${server.servlet.context-path:/}")
    String contextPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (verify(request)) {
            return true;
        }

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
                JSONObject.toJSONString(ApiResult.error(PERMISSION_NONE)));
        response.setContentType(FastJsonJsonView.DEFAULT_CONTENT_TYPE);
        return false;
    }

    private boolean verify(HttpServletRequest request) {
        String path = request.getRequestURI();
        UserSession session = httpHelper.getUser(request);
        if (contextPath != null && !contextPath.equals(DEFAULT_SERVER_CONTEXT) && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        boolean valid = permissionService.isApiValid(path, session.getUserId());
        log.info("api_path#{}, {}", path, valid);
        return valid;
    }

}
