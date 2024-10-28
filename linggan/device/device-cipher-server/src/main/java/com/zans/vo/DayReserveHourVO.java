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
 * @date 2021/6/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayReserveHourVO {

    private Integer value;

    private boolean isDisabled;

    private String name;

    private String showName;

}
