package com.zans.job.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.zans.base.config.EnumErrorCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zans.job.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalResponseMessage(RequestMethod.GET, EnumErrorCode.getGlobalResponseMessage())
                .globalResponseMessage(RequestMethod.POST, EnumErrorCode.getGlobalResponseMessage());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("zans-swagger2-job")
                .description("Restful-API-Doc")
                .contact(new Contact("job", "https://www.lingganwy.com", "alexv1@163.com"))
                .version("0.6")
                .build();
    }
}
