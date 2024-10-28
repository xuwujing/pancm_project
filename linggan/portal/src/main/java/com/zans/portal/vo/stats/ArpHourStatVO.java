package com.zans.portal.vo.stats;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author xv
 * @since 2020/3/4 17:49
 */
@Data
public class ArpHourStatVO {

    @JSONField(name = "hour")
    private String dimHour;

    @JSONField(name = "total")
    private Integer totalCount;

    @JSONField(name = "alive")
    private Integer aliveCount;

    @JSONField(name = "auth")
    private Integer authCount;

    @JSONField(name = "change")
    private Integer changeCount;
}
