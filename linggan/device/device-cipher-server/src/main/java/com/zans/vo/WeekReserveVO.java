package com.zans.vo;

import lombok.Data;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/18
 */
@Data
public class WeekReserveVO {

    private Integer id;

    private String title;

    private String start;

    private String end;

    private Integer status;

}
