package com.zans.mms.vo.arp;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class NetworkSwitchVO {

    @ApiModelProperty(name = "sw_ip", value = "交换机IP")
    @JSONField(name = "sw_ip")
    private String swIp;

    @ApiModelProperty(name = "sys_name", value = "交换机名")
    @JSONField(name = "sys_name")
    private String sysName;

    @ApiModelProperty(name = "sys_desc", value = "型号")
    @JSONField(name = "sys_desc")
    private String sysDesc;

    @ApiModelProperty(name = "sw_type", value = "交换机类型")
    @JSONField(name = "sw_type")
    private String swType;

    @ApiModelProperty(name = "area_name", value = "所属区域")
    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(name = "interface_index", value = "接口序号")
    @JSONField(name = "interface_index")
    private String interfaceIndex;

    @ApiModelProperty(name = "interface_detail", value = "接口描述")
    @JSONField(name = "interface_detail")
    private String interfaceDetail;

    @ApiModelProperty(name = "create_time", value = "学习时间")
    @JSONField(name = "create_time")
    private String createTime;

    @ApiModelProperty(name = "sourceIp", value = "源交换机ip")
    @JSONField(name = "source_ip")
    private String sourceIp;


}
