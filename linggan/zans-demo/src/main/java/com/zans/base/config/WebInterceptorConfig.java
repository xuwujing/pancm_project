package com.zans.base.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedList;
import java.util.List;

@Configuration
@Slf4j
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    PermissionInterceptor permissionInterceptor;

    @Autowired
    SessionInterceptor sessionInterceptor;

    /**
     * 不需要拦截的
     */
    @Value("${security.exclude-path.base}")
    String[] basePathPatterns;

    @Value("${security.exclude-path.api:[]}")
    String[] apiPathPatterns;

    @Value("${security.exclude-path.swagger}")
    String[] swaggerPathPatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> sessionExcludePathList = new LinkedList<>();
        sessionExcludePathList.addAll(Lists.newArrayList(swaggerPathPatterns));
        sessionExcludePathList.addAll(Lists.newArrayList(basePathPatterns));

        List<String> permissionExcludePathList = new LinkedList<>();
        permissionExcludePathList.addAll(Lists.newArrayList(swaggerPathPatterns));
        permissionExcludePathList.addAll(Lists.newArrayList(basePathPatterns));
        permissionExcludePathList.addAll(Lists.newArrayList(apiPathPatterns));

        log.info("excludePath, length#{}, {}", sessionExcludePathList.size(), apiPathPatterns);
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns(GlobalConstants.INTERCEPTOR_ALL)
                .excludePathPatterns(sessionExcludePathList);

        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns(GlobalConstants.INTERCEPTOR_ALL)
                .excludePathPatterns(permissionExcludePathList);
    }
}
