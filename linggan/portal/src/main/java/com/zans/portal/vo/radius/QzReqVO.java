package com.zans.portal.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@ApiModel
@Data
public class QzReqVO extends BasePage {



    @ApiModelProperty(name = "device_type", value = "档案类型")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeDetect", value = "识别类型")
    private Integer deviceTypeDetect;

    @ApiModelProperty(name = "asset_source", value = "资产建档")
    private Integer assetSource;

    private Integer assetManage;

    @ApiModelProperty(name = "company", value = "厂商")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "access_time", value = "认证时间")
    @JSONField(name = "accessTime", format = "yyyy-MM-dd hh:mm:ss")
    private Date accessTime;

    @ApiModelProperty(name = "nas_ip_address", value = "NAS地址")
    private String nasIpAddress;

    @ApiModelProperty(name = "ipAddr", value = "ipAddr")
    @JSONField(name = "ipAddr")
    private String ipAddr;

    @ApiModelProperty(name = "mac", value = "mac")
    @JSONField(name = "mac")
    private String mac;

    @ApiModelProperty(name = "all", value = "1：仅显示有ip地址的项；0（或空字符）：显示所有")
    @JSONField(name = "all")
    private Integer all;

    @ApiModelProperty(name = "date", value = "")
    @JSONField(name = "date")
    private Date[] date;

    @ApiModelProperty(name = "revealStatus", value = "异常数据显示:0:显示,1:不显示")
    private Integer revealStatus;

    @ApiModelProperty(name = "alive_qz", value = "在线状态 1:在线,2:离线")
    private Integer aliveQz;

    private Integer detectType;

    // 检疫设备状态 0,新设备;1,基线设备
    private Integer qzDeviceStatus;

    //漏洞风险
    private String retLevel;



}
