package com.zans.portal.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class RadiusNasRespVO {

    private Integer id;

    @ApiModelProperty(name = "name", value = "接入点描述")
    @JSONField(name = "name")
    @NotNull
    private String name;

    @ApiModelProperty(name = "area", value = "区域id")
    @JSONField(name = "area")
    private Integer area;

    @ApiModelProperty(name = "area_name", value = "区域")
    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(name = "nas_ip", value = "server描述")
    @JSONField(name = "nas_ip")
    @NotNull
    private String nasIp;

    @ApiModelProperty(name = "nas_sw_ip", value = "交换机地址")
    @JSONField(name = "nas_sw_ip")
    @NotNull
    private String nasSwIp;

    @ApiModelProperty(name = "delete_status", value = "状态")
    @JSONField(name = "delete_status")
    private Integer deleteStatus;

    @ApiModelProperty(name = "secret", value = "密码")
    @JSONField(name = "secret")
    @NotNull
    private String secret;

    @ApiModelProperty(name = "short_name", value = "接入点shortname")
    @JSONField(name = "short_name")
    private String shortName;

    @ApiModelProperty(name = "nas_type", value = "nas_type")
    @JSONField(name = "nas_type")
    private String nasType;

    @ApiModelProperty(name = "coa_port", value = "coa_port")
    @JSONField(name = "coa_port")
    private Integer coaPort;

    @ApiModelProperty(name = "coa_enable", value = "coa_enable")
    @JSONField(name = "coa_enable")
    private Integer coaEnable;

    @ApiModelProperty(name = "coa_secret", value = "coa_secret")
    @JSONField(name = "coa_secret")
    private String coaSecret;

}
