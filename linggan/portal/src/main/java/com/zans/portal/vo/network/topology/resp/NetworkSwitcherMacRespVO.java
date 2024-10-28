package com.zans.portal.vo.network.topology.resp;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("下行设备实体")
@Data
public class NetworkSwitcherMacRespVO {
    @ApiModelProperty(name = "id", value = "id")
    private Integer id;
    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    private String ipAddr;
    @ApiModelProperty(name = "interfaceDetail", value = "接口描述")
    private String interfaceDetail;
    @ApiModelProperty(name = "mac", value = "mac地址")
    private String mac;
    @ApiModelProperty(name = "macAddr", value = "mac地址")
    @JSONField(name = "macAddr")
    private String macAddr;
    @ApiModelProperty(name = "mac_addr", value = "mac地址")
    @JSONField(name = "mac_addr")
    private String mac_addr;
    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;
    @ApiModelProperty(name = "deviceTypeName", value = "类型")
    private String deviceTypeName;

}
