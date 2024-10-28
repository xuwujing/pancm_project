package com.zans.portal.vo.radius;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class RadiusNasSearchVO extends BasePage {


    @ApiModelProperty(name = "nas_ip", value = "nas_ip")
    @JSONField(name = "nas_ip")
    private String nasIp;

    @ApiModelProperty(name = "nas_sw_ip", value = "交换机ip")
    @JSONField(name = "nas_sw_ip")
    private String nasSwIp;

    private Integer areaId;

}
