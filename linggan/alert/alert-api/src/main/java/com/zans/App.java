package com.zans;

import com.zans.commons.contants.Constants;
import com.zans.commons.utils.GetSpringBeanUtil;
import com.zans.contants.AlertConstants;
import com.zans.task.ManageTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
* @Title: App
* @Description:
* 主程序入口
* @Version:1.0.0
* @author pancm
* @date 2018年1月9日
 */
@SpringBootApplication
@Slf4j
public class App
{
    public static void main( String[] args ) {
    	// 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        GetSpringBeanUtil springBean = new GetSpringBeanUtil();
        springBean.setApplicationContext(context);
        AlertConstants.IS_INIT=true;
        new ManageTask().start();
        log.info("程序启动成功!{}", Constants.VERSION);
    }

}
