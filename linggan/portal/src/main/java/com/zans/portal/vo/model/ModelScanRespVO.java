package com.zans.portal.vo.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ModelScanRespVO {

    private Integer id;

    @ApiModelProperty(value = "设备厂商编码")
    private String company;

    @ApiModelProperty(name = "device_type", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "device_type_name", value = "设备类型名称")
    @JSONField(name = "device_type_name")
    private String deviceTypeName;

    @ApiModelProperty(name = "device_des", value = "设备型号")
    @JSONField(name = "device_des")
    private String deviceDes;

    @ApiModelProperty(name = "open_port" ,value = "开放端口")
    @JSONField(name = "open_port")
    private String openPort;

    @ApiModelProperty(value = "终端情况")
    private Integer mute;

    @ApiModelProperty(name = "mute_name", value = "终端情况")
    @JSONField(name = "mute_name")
    private String muteName;

    @ApiModelProperty(name = "insert_source", value = "模型来源")
    @JSONField(name = "insert_source")
    private Integer insertSource;

    @ApiModelProperty(name = "insert_source", value = "模型来源名称")
    @JSONField(name = "insert_source_name")
    private String insertSourceName;

    @ApiModelProperty(name = "insert_ip", value = "模型来源IP")
    @JSONField(name = "insert_ip")
    private String insertIp;

    @ApiModelProperty(name = "insert_time", value = "模型学习时间")
    @JSONField(name = "insert_time", format = "yyyy-MM-dd HH:mm:ss")
    private Date insertTime;

    @ApiModelProperty(value = "模型状态")
    private Integer confirm;

    @ApiModelProperty(name = "confirm_name", value = "模型状态名称")
    @JSONField(name = "confirm_name")
    private String confirmName;


    public void setMuteByMap(Map<Object, String> map) {
        Integer status = this.getMute();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setMuteName(name);
    }

    public void setConfirmByMap(Map<Object, String> map) {
        Integer status = this.getConfirm();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setConfirmName(name);
    }

    public void setSourceByMap(Map<Object, String> map) {
        Integer status = this.getInsertSource();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setInsertSourceName(name);
    }

}
