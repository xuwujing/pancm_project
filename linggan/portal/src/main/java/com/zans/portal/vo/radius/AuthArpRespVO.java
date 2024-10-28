package com.zans.portal.vo.radius;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel
@Data
public class AuthArpRespVO {

    @ApiModelProperty(name = "arp_id", value = "设备ID")
    @JSONField(name = "arp_id")
    private Integer arpId;

    @ApiModelProperty(name = "auth_id", value = "认证ID")
    @JSONField(name = "auth_id")
    private Integer authId;

    @ApiModelProperty(name = "ip_addr", value = "IP地址")
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(name = "mac_addr", value = "MAC地址")
    @JSONField(name = "mac_addr")
    private String macAddr;


    @JSONField(name = "area_name")
    @ApiModelProperty(name = "area_name", value = "所属辖区")
    private String areaName;

    @JSONField(name = "device_type")
    @ApiModelProperty(name = "device_type", value = "设备类型编号")
    private Integer deviceType;

    @JSONField(name = "device_type_name")
    @ApiModelProperty(name = "device_type_name", value = "设备类型名称")
    private String deviceTypeName;

    @JSONField(name = "model_des")
    @ApiModelProperty(name = "model_des", value = "设备型号")
    private String modelDes;

    @JSONField(name = "mute")
    @ApiModelProperty(name = "mute", value = "是否哑终端")
    private Integer mute;

    @JSONField(name = "mute_name")
    @ApiModelProperty(name = "mute_name", value = "哑终端/活跃终端")
    private String muteName;

    @ApiModelProperty(name = "alive", value = "在线情况")
    private Integer alive;

    @JSONField(name = "alive_name")
    @ApiModelProperty(name = "alive_name", value = "在线情况名称")
    private String aliveName;

    @JSONField(name = "dis_status")
    @ApiModelProperty(name = "dis_status", value = "地址分配")
    private Integer disStatus;

    @JSONField(name = "dis_status_name")
    @ApiModelProperty(name = "dis_status_name", value = "地址分配名称")
    private String disStatusName;

    @ApiModelProperty(name = "company",value = "厂商")
    private String company;

    @ApiModelProperty(name = "reply_status", value = "认证结果")
    @JSONField(name = "reply_status")
    private Integer replyStatus;

    @ApiModelProperty(name = "reply_status_name", value = "认证结果名称")
    @JSONField(name = "reply_status_name")
    private String replyStatusName;

    @ApiModelProperty(name = "auth_date", value = "认证日期")
    @JSONField(name = "auth_date", format = "yyyy-MM-dd HH:mm:ss")
    private Date authDate;

    @ApiModelProperty(name = "nas_type", value = "接入类型")
    @JSONField(name = "nas_type")
    private String nasType;

    @ApiModelProperty(name = "nas_ip", value = "接入IP")
    @JSONField(name = "nas_ip")
    private String nasIp;

    @ApiModelProperty(name = "nas_port", value = "接入端口")
    @JSONField(name = "nas_port")
    private String nasPort;

    @ApiModelProperty(name = "nas_area", value = "接入区域")
    @JSONField(name = "nas_area")
    private Integer nasArea;

    @ApiModelProperty(name = "nas_area_name", value = "接入区域名称")
    @JSONField(name = "nas_area_name")
    private String nasAreaName;

    @ApiModelProperty(name = "aaa_mac", value = "3A认证的mac")
    @JSONField(name = "aaa_mac")
    private String aaaMac;

    public void setAliveByMap(Map<Object, String> map) {
        Integer status = this.getAlive();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setAliveName(name);
    }

    public void setMuteByMap(Map<Object, String> map) {
        Integer status = this.getMute();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setMuteName(name);
    }

    public void setDisStatusByMap(Map<Object, String> map) {
        Integer status = this.getDisStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setDisStatusName(name);
    }

    public void setReplyStatusByMap(Map<Object, String> map) {
        Integer status = this.getReplyStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setReplyStatusName(name);
    }

}
