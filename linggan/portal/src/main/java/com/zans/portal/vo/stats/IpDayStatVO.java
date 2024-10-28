package com.zans.portal.vo.stats;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xv
 * @since 2020/3/4 17:49
 */
@Data
public class IpDayStatVO {

    @JSONField(name = "date", format = "yyyy-MM-dd")
    private Date dimDay;

    @JSONField(name = "total")
    private Integer totalCount;

    @JSONField(name = "auth_count")
    private Integer authCount;

    @JSONField(name = "auth_radio")
    private Integer authRadio;

    private String name = "认证设备比例";
}
