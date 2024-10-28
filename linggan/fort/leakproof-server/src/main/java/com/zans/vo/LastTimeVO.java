package com.zans.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastTimeVO {

    private Integer day;

    private Integer hour;

    private Integer minutes;

    private Integer seconds;

    private Integer milliseconds;

    public LastTimeVO(Integer day, Integer hour, Integer minutes, Integer seconds) {
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }
}
