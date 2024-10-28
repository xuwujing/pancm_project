package com.zans.mms.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel
@Data
public class EndPointViewVO {

    @ApiModelProperty(name = "id", value = "id")
    @JSONField(name = "id")
    private Integer id;


    @ApiModelProperty(name = "arp_id", value = "arp_id")
    @JSONField(name = "arp_id")
    private Integer arpId;

    @ApiModelProperty(name = "alive_arp", value = "arp在线状态")
    @JSONField(name = "alive_arp")
    private Integer aliveArp;

    @ApiModelProperty(name = "alive", value = "在线状态")
    @JSONField(name = "alive")
    private Integer alive;

    @ApiModelProperty(name = "alive_name", value = "在线状态名称")
    @JSONField(name = "alive_name")
    private String aliveName;

    @ApiModelProperty(name = "alive_qz", value = "检疫区在线状态")
    @JSONField(name = "alive_qz")
    private Integer aliveQz;

    @ApiModelProperty(name = "alive_qz_name", value = "检疫区在线状态名称")
    @JSONField(name = "alive_qz_name")
    private String aliveQzName;

    @ApiModelProperty(name = "acct_session_time", value = "在线时长")
    @JSONField(name = "acct_session_time")
    private String acctSessionTime;

    @ApiModelProperty(name = "point_name", value = "点位名称")
    @JSONField(name = "point_name")
    private String pointName;

    @ApiModelProperty(name = "ip_addr", value = "ip地址")
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(name = "username", value = "mac地址")
    @JSONField(name = "username")
    private String username;


    @ApiModelProperty(name = "pass", value = "mac地址")
    @JSONField(name = "pass")
    private String pass;

    @ApiModelProperty(name = "area_name", value = "所属区域")
    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(name = "device_type_name", value = "设备类型")
    @JSONField(name = "device_type_name")
    private String deviceTypeName;

    @ApiModelProperty(name = "device_type_id", value = "设备类型Id")
    @JSONField(name = "device_type_id")
    private String deviceTypeId;

    @ApiModelProperty(name = "brand_name", value = "品牌")
    @JSONField(name = "brand_name")
    private String brandName;

    @ApiModelProperty(name = "company", value = "厂商")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "acct_start_time", value = "认证时间")
    @JSONField(name = "acct_start_time")
    private String acctStartTime;

    @ApiModelProperty(name = "auth_mark", value = "审批原因")
    @JSONField(name = "auth_mark")
    private String authMark;

    @ApiModelProperty(name = "auth_time", value = "审批时间")
    @JSONField(name = "auth_time")
    private String authTime;

    @ApiModelProperty(name = "nas_ip_address", value = "nas地址")
    @JSONField(name = "nas_ip_address")
    private String nasIpAddress;

    @ApiModelProperty(name = "nas_name", value = "nas名称")
    @JSONField(name = "nas_name")
    private String nasName;

    @ApiModelProperty(name = "access_policy", value = "状态：0, 阻断；1，检疫区，2，放行")
    @JSONField(name = "access_policy")
    private String accessPolicy;

    @ApiModelProperty(name = "nas_port_type", value = "Nas 类型")
    @JSONField(name = "nas_port_type")
    private String nasPorType;

    @ApiModelProperty(name = "nas_port_id", value = "Nas 接口")
    @JSONField(name = "nas_port_id")
    private String nasPortId;

    @ApiModelProperty(name = "nas_mac", value = "Nas Mac地址")
    @JSONField(name = "nas_mac")
    private String nasMac;


    @ApiModelProperty(name = "called_station_id", value = "nas接口的mac地址", required = true)
    @JSONField(name = "called_station_id")
    private String calledStationId;

    @ApiModelProperty(name = "contractor_phone", value = "联系电话")
    @JSONField(name = "contractor_phone")
    private String contractorPhone;

    @ApiModelProperty(name = "contractor_person", value = "联系人")
    @JSONField(name = "contractor_person")
    private String contractorPerson;

    @ApiModelProperty(name = "contractor", value = "承建单位")
    @JSONField(name = "contractor")
    private String contractor;

    @ApiModelProperty(name = "project_name", value = "项目名称")
    @JSONField(name = "project_name")
    private String projectName;

    @ApiModelProperty(name = "cur_model_des", value = "设备型号")
    @JSONField(name = "cur_model_des")
    private String curModelDes;

    @JSONField(name = "create_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "create_time", value = "入网时间")
    private Date createTime;

    @ApiModelProperty(name = "server_os", value = "系统版本")
    @JSONField(name = "server_os")
    private String serverOs;

    @ApiModelProperty(name = "reveal_status", value = "reveal_status")
    @JSONField(name = "reveal_status")
    private Integer revealStatus;


    @JSONField(name = "alive_last_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "alive_last_time", value = "在线时间")
    private Date aliveLastTime;

    @ApiModelProperty(name = "open_port", value = "端口列表")
    @JSONField(name = "open_port")
    private String openPort;

    @ApiModelProperty(name = "mute", value = "是否哑终端(0:否，1:是)")
    @JSONField(name = "mute")
    private Integer mute;

    @ApiModelProperty(name = "alertRecord", value = "是否显示告警记录(0:否，1:是) 默认0")
    private Integer alertRecord;

    private String vlan;

    private String curVlan;

    private int isVlanErr;


    public void setAliveNameByMap(Map<Object, String> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        if (this.alive != null) {
            this.setAliveName(map.get(this.alive));
        }
        if (this.aliveQz != null) {
            this.setAliveQzName(map.get(this.aliveQz));
        }

    }

}
