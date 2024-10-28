package com.zans.portal.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.zans.base.config.EnumErrorCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        //添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        //添加head参数end


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zans.portal.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .globalResponseMessage(RequestMethod.GET, EnumErrorCode.getGlobalResponseMessage())
                .globalResponseMessage(RequestMethod.POST, EnumErrorCode.getGlobalResponseMessage());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("zans-swagger2")
                .description("Restful-API-Doc")
                .termsOfServiceUrl("https://www.lingganwy.com")
                .contact(new Contact("零感网御", "https://www.lingganwy.com", "alexv1@163.com"))
                .version("0.6")
                .build();
    }
}
