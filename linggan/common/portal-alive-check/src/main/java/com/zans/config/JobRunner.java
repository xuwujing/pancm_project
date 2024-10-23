package com.zans.config;


import com.zans.service.IAssetService;
import com.zans.util.FileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author pancm
 * @Title: portal
 * @Description: 定时任务执行
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/20
 */
@Component
@Slf4j
public class JobRunner implements CommandLineRunner {



    @Value("${spring.profiles.active}")
    String active;



    @Value("${checkAliveTime:60}")
    int checkAliveTime;

    @Value("${checkAliveEnable:1}")
    int checkAliveEnable;

    private final static String VERSION_NAME = "version.txt";
    private long count;



    @Autowired
    IAssetService assetService;


    ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    @Override
    public void run(String... args) throws Exception {
        log.info("The following profiles are active:{}", active);
        if (checkAliveEnable == 1) {
            log.info("开启资产扫描！");
            checkAlive();
        }
        log.info("The program started successfully! Current Version:{}", FileHelper.readResourcesFile(VERSION_NAME));
    }


    private void checkAlive() {
        Runnable runnable = () -> {
            assetService.checkAlive();
        };
        service.scheduleAtFixedRate(runnable, 0, checkAliveTime, TimeUnit.SECONDS);
    }


}
