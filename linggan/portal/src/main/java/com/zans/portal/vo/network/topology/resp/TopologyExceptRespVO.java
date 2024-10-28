package com.zans.portal.vo.network.topology.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("网络拓扑图详情VO")
@Data
public class TopologyExceptRespVO {
    @ApiModelProperty(name = "id", value = "id")
    private Integer id;

    @ApiModelProperty(name = "sw_id", value = "交换机id")
    private Integer swId;

    @ApiModelProperty(name = "sourceIp", value = "本机ip")
    private String sourceIp;

    @ApiModelProperty(name = "sourceInterface", value = "本机接口")
    private String sourceInterface;

    @ApiModelProperty(name = "sourceTypeDesc", value = "源交换类型描述")
    private String sourceTypeDesc;


    @ApiModelProperty(name = "destInterface", value = "下级接口")
    private String destInterface;

    @ApiModelProperty(name = "destMac", value = "下级mac")
    private String destMac;

}
