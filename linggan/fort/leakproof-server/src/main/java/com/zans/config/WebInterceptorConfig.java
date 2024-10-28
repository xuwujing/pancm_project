package com.zans.config;

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
    SessionInterceptor sessionInterceptor;

    /**
     * 不需要拦截的
     */
    @Value("${security.exclude-path.base}")
    List<String> basePathPatterns;

    @Value("${security.exclude-path.swagger}")
    List<String> swaggerPathPatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> sessionExcludePathList = new LinkedList<>();
        sessionExcludePathList.addAll(swaggerPathPatterns);
        sessionExcludePathList.addAll(basePathPatterns);
        log.info("NoAuth:{}",basePathPatterns);

        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns(GlobalConstants.INTERCEPTOR_ALL)
                .excludePathPatterns(sessionExcludePathList);

    }
}
