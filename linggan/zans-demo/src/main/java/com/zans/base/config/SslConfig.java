package com.zans.base.config;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动将 http 端口请求，转发到 https
 * @author xv
 * @since 2020/3/30 14:17
 */
@Configuration
public class SslConfig {

    /**
     * http服务端口
     */
    @Value("${custom.server.http.port:21000}")
    private Integer httpPort;

    /**
     * https服务端口
     */
    @Value("${server.port}")
    private Integer serverPort;

    @Value("${server.ssl.enabled:false}")
    private Boolean sslEnable;

    @Bean
    public ServletWebServerFactory undertowFactory() {
        UndertowServletWebServerFactory undertowFactory = new UndertowServletWebServerFactory();

        if (this.sslEnable) {
            undertowFactory.addBuilderCustomizers((Undertow.Builder builder) -> {
                builder.addHttpListener(httpPort, "0.0.0.0");
                // 开启HTTP2
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
            });

            undertowFactory.addDeploymentInfoCustomizers(deploymentInfo -> {
                // 开启HTTP自动跳转至HTTPS
                deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                        .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                        .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                        .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                        .setConfidentialPortManager(exchange -> serverPort);
            });
        }
        return undertowFactory;
    }
}
