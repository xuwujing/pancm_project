package com.zans.portal.vo.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModelScanSearchVO extends BasePage {

    @ApiModelProperty(value = "设备厂商")
    private String company;

    @ApiModelProperty(name = "device_type", value = "设备类型")
    @JSONField(name = "device_type")
    private Integer deviceType;

    @ApiModelProperty(name = "device_des", value = "设备型号")
    @JSONField(name = "device_des")
    private String deviceDes;

    @ApiModelProperty(name = "insert_source", value = "模型来源")
    @JSONField(name = "insert_source")
    private Integer insertSource;

    @ApiModelProperty(value = "模型状态")
    private Integer confirm;

    @ApiModelProperty(value = "终端情况")
    private Integer mute;

    @ApiModelProperty(name = "open_port" ,value = "开放端口")
    @JSONField(name = "open_port")
    private String openPort;
}
