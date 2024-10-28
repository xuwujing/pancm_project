package com.zans.mms.vo.area;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class AreaInitResVO {

    private Integer id;

    @ApiModelProperty(name = "area_id", value = "区域id")
    @JSONField(name = "area_id")
    @NotNull
    private Integer areaId;

    @ApiModelProperty(name = "area_name", value = "区域名称")
    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(name = "device_type_id", value = "设备类型")
    @JSONField(name = "device_type_id")
    @NotNull
    private Integer deviceTypeId;

    @ApiModelProperty(name = "device_type_name", value = "设备类型名称")
    @JSONField(name = "device_type_name")
    private String deviceTypeName;

    @ApiModelProperty(name = "ip_addr", value = "ip地址")
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(name = "mask", value = "掩码")
    @JSONField(name = "mask")
    private String mask;

    @ApiModelProperty(name = "gateway", value = "网关")
    @JSONField(name = "gateway")
    private String gateway;

    @ApiModelProperty(name = "vlan", value = "vlan")
    @JSONField(name = "vlan")
    private String vlan;

}
