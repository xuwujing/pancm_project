package com.zans.mms.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @Title: SwitcherMacInterface
* @Description:
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 6/16/21
*/
@Data
@ApiModel(value = "SwitcherMacInterfaceResVO", description = "")
public class SwitcherMacInterfaceResVO {
    @ApiModelProperty(name = "interfaceDetail", value = "接口详情")
    private String interfaceDetail;

    @ApiModelProperty(name = "nasPortId", value = "接口详情")
    private String nasPortId;

    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "mac", value = "mac地址")
    private String mac;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private String deviceType;

    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty(name = "modelDes", value = "设备型号")
    private String modelDes;

    @ApiModelProperty(name = "accessPolicy", value = "0, 阻断；1，检疫区，2，放行")
    private Integer accessPolicy;

    @ApiModelProperty(name = "accessPolicy", value = "0, 阻断；1，检疫区，2，放行")
    private Integer alive;


    private Long id;

    @ApiModelProperty(name = "accessPolicy", value = "0, 阻断；1，检疫区，2，放行")
    private Integer aliveQz;

    private String pId;

}
