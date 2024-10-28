package com.zans.mms.config;

import com.zans.mms.service.IWeChatReqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author pancm
 * @Title: portal
 * @Description: 任务执行
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/20
 */
@Component
@Slf4j
public class JobRunner implements CommandLineRunner {


    @Autowired
    private IWeChatReqService weChatReqService;


    @Override
    public void run(String... args) {
//        register();
    }


    private void register() {
        weChatReqService.setConfig();
    }

}
