package com.zans;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zans.base.util.FileHelper;
import com.zans.mms.syslog.UdpDecoderHandler;
import com.zans.mms.syslog.UdpEncoderHandler;
import com.zans.mms.syslog.UdpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.netty.udp.UdpServer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.zans.base.config.GlobalConstants.VERSION_NAME;

@SpringBootApplication
public class ZansDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(ZansDemoApplication.class);

    /**
     * 监听IP和端口
     */
    @Value("${syslog.listen.port:514}")
    private Integer port;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ZansDemoApplication.class, args);
        log.info("Current ActiveProfiles:{}", ctx.getEnvironment().getActiveProfiles()[0]);
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

    @Bean
    CommandLineRunner serverRunner(UdpDecoderHandler udpDecoderHanlder, UdpEncoderHandler udpEncoderHandler, UdpHandler udpHandler) {
        return strings -> {
            createUdpServer(udpDecoderHanlder, udpEncoderHandler, udpHandler);
        };
    }

    /**
     *
     * 创建UDP Server
     * @param udpDecoderHandler： 用于解析UDP Client上报数据的handler
     * @param udpEncoderHandler： 用于向UDP Client发送数据进行编码的handler
     * @param udpHandler: 用户维护UDP链接的handler
     */
    private void createUdpServer(UdpDecoderHandler udpDecoderHandler, UdpEncoderHandler udpEncoderHandler, UdpHandler udpHandler) {
        UdpServer.create()
                .handle((in,out) -> {
                    in.receive()
                            .asByteArray()
                            .subscribe();
                    return Flux.never();
                })
                // host ip
                .host("0.0.0.0")
                //UDP Server端口
                .port(port)
                .doOnBound(conn -> conn
                        .addHandler("decoder",udpDecoderHandler)
                        .addHandler("encoder", udpEncoderHandler)
                        .addHandler("handler", udpHandler)
                ) //可以添加多个handler
                .bindNow(Duration.ofSeconds(10));
        log.info("udp-server init.......");
    }



}
