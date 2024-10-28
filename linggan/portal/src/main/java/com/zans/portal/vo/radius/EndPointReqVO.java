package com.zans.portal.vo.radius;


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
//    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(name = "mac", value = "mac地址")
    @JSONField(name = "mac")
    private String mac;



    @ApiModelProperty(name = "device_type", value = "设备类型")
//    @JSONField(name = "device_type")
    private Integer deviceType;


    @ApiModelProperty(name = "access_policy", value = "状态：0, 阻断；1，检疫区，2，放行")
//    @JSONField(name = "access_policy")
    @NotNull
    private Integer accessPolicy;

    @ApiModelProperty(name = "company", value = "厂商")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "nas_ip_address", value = "NAS地址")
//    @JSONField(name = "nas_ip_address")
    private String nasIpAddress;

    @ApiModelProperty(name = "brand_name", value = "品牌名称")
//    @JSONField(name = "brand_name")
    private String brandName;

    // 检疫设备状态 0,新设备;1,基线设备
    private Integer qzDeviceStatus;

    private Integer detectType;
    private Integer aliveQz;
    //漏洞风险
    private String retLevel;

    @ApiModelProperty(name = "deviceTypeDetect", value = "识别设备类型编号")
    private Integer deviceTypeDetect;


    @ApiModelProperty(name = "assetManage", value = "纳管设备")
    private Integer assetManage;

    @ApiModelProperty(name = "assetSource", value = "建档状态")
    private Integer assetSource;


}
