package com.zans.portal.vo.stats;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * 网络质量查询vo
 */
@Data
public class IpAliveResponseVO extends BasePage {

    @ApiModelProperty(name = "data", value = "时间")
    @JSONField(name = "data")
    private String data;

    @ApiModelProperty(name = "packet_loss_rate", value = "丢包率")
//    @JSONField(name = "packet_loss_rate")
    private String packetLossRate;

    @ApiModelProperty(name = "rtt_min", value = "最短响应时间")
//    @JSONField(name = "rtt_min")
    private String rttMin;

    @ApiModelProperty(name = "rtt_avg", value = "平均响应时间")
//    @JSONField(name = "rtt_avg")
    private String rttAvg;

    @ApiModelProperty(name = "rtt_max", value = "最长响应时间")
//    @JSONField(name = "rtt_max")
    private String rttMax;

    @ApiModelProperty(name = "rtt_mdev", value = "方差")
//    @JSONField(name = "rtt_mdev")
    private String rttMdev;


}
