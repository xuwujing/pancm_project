package com.zans.portal.vo.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ModelScanEditVO {

    @ApiModelProperty(name = "id",value = "模型ID", required = true)
    @NotNull(message = "模型ID必填")
    private Integer id;

    @ApiModelProperty(name = "device_type",value = "设备类型", required = true)
    @NotNull(message = "设备类型必填")
    @JSONField(name = "device_type")
    private Integer deviceType;

    @ApiModelProperty(name = "company",value = "设备厂商", required = true)
    @NotEmpty(message = "设备厂商必填")
    private String company;

    @ApiModelProperty(name = "device_des",value = "设备型号", required = true)
    @NotEmpty(message = "设备型号必填")
    @JSONField(name = "device_des")
    private String deviceDes;

    @ApiModelProperty(name = "open_port",value = "开放端口", required = true)
    @NotEmpty(message = "开放端口必填")
    @JSONField(name = "open_port")
    private String openPort;

    @ApiModelProperty(name = "mute",value = "终端情况", required = true)
    @NotNull(message = "终端情况必填")
    @Min(value = 0)
    @Max(value = 1)
    private Integer mute;

    @ApiModelProperty(name = "confirm",value = "模型状态", required = true)
    @NotNull(message = "模型状态必填")
    @Min(value = 0)
    @Max(value = 1)
    private Integer confirm;

    @ApiModelProperty(name = "insert_time",value = "添加时间")
    @JSONField(name = "insert_time")
    private String insertTime;

    @ApiModelProperty(name = "insert_ip",value = "学习来源")
    @JSONField(name = "insert_ip")
    private String insertIp;

    @ApiModelProperty(name = "insert_source",value = "模型来源")
    @JSONField(name = "insert_source")
    private Integer insertSource;


}
