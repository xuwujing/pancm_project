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

    private String start;//展示开始时间

    private String end;//展示结束时间

    private Integer status;

    private String proposer;

    private String startTime;//真实开始时间

    private String endTime;//真实结束时间

}
