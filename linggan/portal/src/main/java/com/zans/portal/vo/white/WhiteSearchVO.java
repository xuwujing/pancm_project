package com.zans.portal.vo.white;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WhiteSearchVO extends MacPage {

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @JSONField(name = "device_type")
    @ApiModelProperty(name = "device_type", value = "设备名称")
    private Integer deviceType;

    @ApiModelProperty(name = "mute", value = "是否哑终端")
    private Integer mute;

}
