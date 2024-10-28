package com.zans.mms.vo.arp;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ArpSwitchVO {

    @ApiModelProperty(name = "mac_addr",value = "mac地址", required = true)
    private String macAddr;

    @ApiModelProperty(name = "ip_addr", value = "ip地址")
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(name = "company", value = "厂商")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "device_type_name", value = "设备类型名称")
    @JSONField(name = "device_type_name")
    private String deviceTypeName;

    @ApiModelProperty(name = "model_des", value = "设备型号")
    @JSONField(name = "model_des")
    private String modelDes;

}
