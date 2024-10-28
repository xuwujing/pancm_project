package com.zans.mms.vo.radius;

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
public class RadiusVerifyRespVO implements Serializable {

    private String key;

    private String title;

    private Object value;

    /** 耗时*/
    private Long costTime;




}
