package com.zans.portal.vo.radius;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/12/28
 */
@Data
public class RadiusRespVO implements Serializable {


    private String username;

    private Integer accessPolicy;

    private String updateTime;

    private String dbName;



}