package com.zans.portal.vo.white;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@ApiModel
@Data
public class WhiteRespVO {

    private Integer id;

    @JSONField(name = "device_name")
    @ApiModelProperty(name = "device_name", value = "设备名称")
    private String deviceName;

    @JSONField(name = "device_des")
    @ApiModelProperty(name = "device_des", value = "设备型号")
    private String deviceDes;

    @JSONField(name = "device_type")
    @ApiModelProperty(name = "device_type", value = "设备类型")
    private Integer deviceType;

    @JSONField(name = "device_type_name")
    @ApiModelProperty(name = "device_type_name", value = "设备类型名")
    private String deviceTypeName;

    @ApiModelProperty(value = "是否哑终端")
    private Integer mute;

    @JSONField(name = "mute_name")
    @ApiModelProperty(name = "mute_name", value = "是否哑终端名称")
    private String muteName;

    @ApiModelProperty(value = "MAC地址")
    private String mac;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "联系人")
    private String person;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    public void setMuteByMap(Map<Object, String> map) {
        Integer status = this.getMute();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setMuteName(name);
    }
}
