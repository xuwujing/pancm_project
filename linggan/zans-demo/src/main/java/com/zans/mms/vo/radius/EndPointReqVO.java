package com.zans.mms.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@ApiModel
@Data
public class EndPointReqVO extends BasePage {

    @ApiModelProperty(name = "ip_addr", value = "ip地址")
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(name = "username", value = "mac地址")
    @JSONField(name = "username")
    private String username;

    @ApiModelProperty(name = "pass", value = "mac地址")
    @JSONField(name = "pass")
    private String pass;

    @ApiModelProperty(name = "device_type", value = "设备类型")
    @JSONField(name = "device_type")
    private Integer deviceType;

    @ApiModelProperty(name = "area_id", value = "区域")
    @JSONField(name = "area_id")
    private Integer areaId;

    @ApiModelProperty(name = "access_policy", value = "状态：0, 阻断；1，检疫区，2，放行")
    @JSONField(name = "access_policy")
    @NotNull
    private Integer accessPolicy;

    @ApiModelProperty(name = "company", value = "厂商")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "nas_ip_address", value = "NAS地址")
    @JSONField(name = "nas_ip_address")
    private String nasIpAddress;

    @ApiModelProperty(name = "brand_name", value = "品牌名称")
    @JSONField(name = "brand_name")
    private String brandName;

    @ApiModelProperty(name = "alive", value = "在线状态")
    @JSONField(name = "alive")
    private Integer alive;


}
