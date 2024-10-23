package com.zans.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
* @Author beixing
* @Description  数据库的信息
* @Date  2021/12/17
* @Param
* @return
**/
@Configuration
@ConfigurationProperties(prefix = "db")
@Data
public class MapDbProperties {
    private Map<String, String> info;

}
