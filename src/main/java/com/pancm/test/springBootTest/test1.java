package com.pancm.test.springBootTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
* Title: test1
* Description: springBoot的第一个测试
* Version:1.0.0  
* @author pancm
* @date 2018年1月3日
 */

//开启组件扫描和自动配置
@SpringBootApplication 
public class test1 {
	public static void main(String[] args) {
		//负责启动引导应用程序
		SpringApplication.run(test1.class, args);
	}
}
