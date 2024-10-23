package com.zans;

import com.zans.config.SpringBeanFactory;
import com.zans.netty.NettyServer;
import com.zans.util.FileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
* @Title: Application
* @Description: main
* @Version:1.0.0
* @Since:jdk1.8
* @author pancm
* @Date  2021/10/15
**/
@ServletComponentScan
@SpringBootApplication
@Slf4j
public class Application {
    private final static String  VERSION_NAME = "version.txt";

    private static NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("程序启动成功!{}", FileHelper.readResourcesFile(VERSION_NAME));
        nettyServer = (NettyServer) SpringBeanFactory.getBean("nettyServer");
        nettyServer.nettyServerRun();
    }

}
