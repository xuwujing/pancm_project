package com.zans.portal.vo.chart;


import lombok.Data;

@Data
public class OnlineCircleUnit {
    private Integer id;
    private Object value;
    private String name;
    private Integer failCount;
    private Integer successCount;
    private Integer waitCount;
    private Object rate;
}
