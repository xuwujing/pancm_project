package com.zans.portal.vo.white;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.valid.IPAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class WhiteIpAddVO {

    @NotEmpty(message = "设备名称必填")
    @JSONField(name = "device_name")
    @ApiModelProperty(name = "device_name", value = "设备名称")
    private String deviceName;

    @JSONField(name = "device_des")
    @ApiModelProperty(name = "device_des", value = "设备型号")
    private String deviceDes;

    @NotNull(message = "设备类型必填")
    @JSONField(name = "device_type")
    @ApiModelProperty(name = "device_type", value = "设备类型")
    private Integer deviceType;

    @NotNull(message = "是否哑终端必填")
    @ApiModelProperty(value = "是否哑终端")
    private Integer mute;

    @NotEmpty(message = "IP地址必填")
    @IPAddress
    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "联系人")
    private String person;

    @ApiModelProperty(value = "联系电话")
    private String phone;
}
