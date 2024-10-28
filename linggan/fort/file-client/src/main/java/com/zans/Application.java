package com.zans;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zans.utils.FileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
public class Application {

    private final static String  VERSION_NAME = "version.txt";

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        log.info("Current ActiveProfiles:{}", ctx.getEnvironment().getActiveProfiles()[0]);
        log.info("The program started successfully! Current Version:{}", FileHelper.readResourcesFile(VERSION_NAME));
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1、先定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2、添加fastjson的配置信息，比如是否要格式化返回的json数据；
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 渲染 null，避免前端bug
        fastJsonConfig.setSerializerFeatures();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);
        //附加：处理中文乱码
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }

}
