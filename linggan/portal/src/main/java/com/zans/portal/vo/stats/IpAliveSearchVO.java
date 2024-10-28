package com.zans.portal.vo.stats;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * 网络质量查询vo
 */
@Data
public class IpAliveSearchVO extends BasePage {

    @ApiModelProperty(name = "time", value = "查询时间，单位为小时，一天等于24小时")
    @JSONField(name = "time")
    private Integer time;

    @ApiModelProperty(name = "date", value = "自定义时间（时间范围）")
    @JSONField(name = "date")
    private String date[];

    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    private String ipAddr;

}
