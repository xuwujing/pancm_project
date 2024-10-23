package com.zans;

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
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
