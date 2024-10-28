package com.zans.config;

import com.zans.task.AnalyzeTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author pancm
 * @Title:
 * @Description: 启动之后初始化
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/1
 */
@Component
@Slf4j
public class Runner implements CommandLineRunner {


    @Override
    public void run(String... args){
        initServer();
    }

    /**
     * @return void
     * @Author pancm
     * @Description 服务初始化
     * @Date 2020/9/1
     * @Param []
     **/
    private void initServer() {
        try {
            new AnalyzeTask().start();
        } catch (Exception e) {
            log.error("初始化服务配置异常:", e);
        }
    }



}
