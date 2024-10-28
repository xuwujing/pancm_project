package com.zans.mms.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@ApiModel
@Data
public class QzReqVO extends BasePage {

    @ApiModelProperty(name = "area_id", value = "区域id")
    @JSONField(name = "area_id")
    private Integer areaId;

    @ApiModelProperty(name = "device_type", value = "设备类型")
    @JSONField(name = "device_type")
    private Integer deviceType;

    @ApiModelProperty(name = "company", value = "厂商")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "access_time", value = "认证时间")
    @JSONField(name = "access_time", format = "yyyy-MM-dd hh:mm:ss")
    private Date accessTime;

    @ApiModelProperty(name = "nas_ip_address", value = "NAS地址")
    @JSONField(name = "nas_ip_address")
    private String nasIpAddress;

    @ApiModelProperty(name = "ip", value = "ip")
    @JSONField(name = "ip")
    private String ip;

    @ApiModelProperty(name = "username", value = "username")
    @JSONField(name = "username")
    private String username;

    @ApiModelProperty(name = "all", value = "1：仅显示有ip地址的项；0（或空字符）：显示所有")
    @JSONField(name = "all")
    private Integer all;

    @ApiModelProperty(name = "date", value = "")
    @JSONField(name = "date")
    private Date[] date;

    @ApiModelProperty(name = "revealStatus", value = "异常数据显示:0:显示,1:不显示")
    private Integer revealStatus;

    @ApiModelProperty(name = "alive_qz", value = "在线状态 1:在线,2:离线")
    @JSONField(name = "alive_qz")
    private Integer aliveQz;

}
