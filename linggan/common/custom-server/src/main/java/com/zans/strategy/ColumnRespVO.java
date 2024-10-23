package com.zans.strategy;

import lombok.Data;

/**
 * @author beixing
 * @Title: custom-server
 * @Description: 通用查询对象
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/4/18
 */
@Data
public class ColumnRespVO {
    /** 字段key值*/
    private String itemKey;
    /** 字段名称*/
    private String itemValue;



}
