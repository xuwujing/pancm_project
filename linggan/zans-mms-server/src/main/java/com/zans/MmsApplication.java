package com.zans;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zans.base.util.FileHelper;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

import static com.zans.base.config.GlobalConstants.VERSION_NAME;

@EnableAsync
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MmsApplication {

    private static final Logger log = LoggerFactory.getLogger(MmsApplication.class);
    public static long PROJECT_START_TIME;



    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        PROJECT_START_TIME = currentTimeMillis;
        log.info("----------------currentTimeMillis:{}", currentTimeMillis);

        ConfigurableApplicationContext ctx = SpringApplication.run(MmsApplication.class, args);
        log.info("Current ActiveProfiles:{},currentTimeMillis:{}", ctx.getEnvironment().getActiveProfiles()[0],currentTimeMillis);
        log.info("The program started successfully! Current Version:{}", FileHelper.readResourcesFile(VERSION_NAME));

        /*exit(ctx);*/
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
