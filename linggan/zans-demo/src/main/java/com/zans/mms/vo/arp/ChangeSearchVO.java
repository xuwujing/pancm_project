package com.zans.mms.vo.arp;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class ChangeSearchVO extends MacPage {

    private String ip;

    private Integer area;

    @JSONField(name = "device_type")
    private Integer deviceType;

    private Integer alive;

    private Integer mute;

    private String company;

    @JSONField(name = "change_type")
    private Integer changeType;

    @JSONField(name = "risk_type")
    private Integer riskType;

}
