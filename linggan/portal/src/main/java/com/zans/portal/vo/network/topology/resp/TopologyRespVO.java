package com.zans.portal.vo.network.topology.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("网络拓扑图详情VO")
@Data
public class TopologyRespVO {
    @ApiModelProperty(name = "id", value = "id")
    private Integer id;

    @ApiModelProperty(name = "sw_id", value = "交换机id")
    private Integer swId;

    @ApiModelProperty(name = "sourceIp", value = "上层ip")
    private String sourceIp;

    @ApiModelProperty(name = "sourceInterface", value = "上层接口")
    private String sourceInterface;

    @ApiModelProperty(name = "destIp", value = "本机Ip")
    private String destIp;

    @ApiModelProperty(name = "destInterface", value = "本机接口")
    private String destInterface;

    @ApiModelProperty(name = "name", value = "交换机系统名,用户定义")
    private String name;

    @ApiModelProperty(name = "sysName", value = "交换机系统名")
    private String sysName;

    @ApiModelProperty(name = "sysDesc", value = "交换机描述")
    private String sysDesc;

    @ApiModelProperty(name = "swHost", value = "本机ip")
    private String swHost;

    @ApiModelProperty(name = "brand", value = "品牌")
    private String brandName;

    @ApiModelProperty(name = "alive", value = "在线状态 1在线 0离线")
    private Integer alive;

    @ApiModelProperty(name = "aliveName", value = "在线状态 1在线 0离线")
    private String aliveName;

    @ApiModelProperty(name = "acceptance", value = "已验收 1 未验收0")
    private Integer acceptance;

    @ApiModelProperty(name = "acceptanceName", value = "已验收 1 未验收0")
    private String acceptanceName;

    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "upTime", value = "上线时间")
    private String upTime;

    @ApiModelProperty(name = "version", value = "版本")
    private String version;


    @ApiModelProperty(name = "scanInterfaceCount", value = "物理接口数量")
    private Integer scanInterfaceCount;

    @ApiModelProperty(name = "useInterface", value = "已用接口")
    private String useInterface;

    @ApiModelProperty(name = "scanMacAll", value = "使用中物理接口数量")
    private Integer scanMacAll;

    @ApiModelProperty(name = "topoType", value = "CORE;DISTRIBUTE;ACCESS;核心，汇聚，接入")
    private String topoType;
}
