package com.zans.portal.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel
@Data
public class QzRespVO {

    @ApiModelProperty(name = "id", value = "id", required = true)
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(name = "mac", value = "用户名", required = true)
    @JSONField(name = "mac")
    private String mac;


    private String macDetect;





    @ApiModelProperty(name = "service_type", value = "service_type", required = true)
//    @JSONField(name = "service_type")
    private String serviceType;

    @ApiModelProperty(name = "framed_protocol", value = "framed_protocol", required = true)
//    @JSONField(name = "framed_protocol")
    private String framedProtocol;

    @ApiModelProperty(name = "ip_addr", value = "ip地址", required = true)
//    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(name = "called_station_id", value = "nas接口的mac地址", required = true)
//    @JSONField(name = "called_station_id")
    private String calledStationId;

    @ApiModelProperty(name = "calling_station_id", value = "终端mac地址", required = true)
//    @JSONField(name = "calling_station_id")
    private String callingStationId;

    @ApiModelProperty(name = "nas_name", value = "nas名称", required = true)
//    @JSONField(name = "nas_name")
    private String nasName;

    @ApiModelProperty(name = "nas_ip", value = "nas_ip", required = true)
//    @JSONField(name = "nas_ip")
    private String nasIp;

    @ApiModelProperty(name = "nas_port", value = "nas端口", required = true)
//    @JSONField(name = "nas_port")
    private Integer nasPort;

    @ApiModelProperty(name = "nas_port_type", value = "nas类型", required = true)
//    @JSONField(name = "nas_port_type")
    private String nasPortType;

    @ApiModelProperty(name = "nas_port_id", value = "nas接口名称", required = true)
//    @JSONField(name = "nas_port_id")
    private String nasPortId;

    @ApiModelProperty(name = "access_time", value = "认证时间", required = true)
    @JSONField(name = "accessTime", format = "yyyy-MM-dd hh:mm:ss")
    private Date accessTime;

    @ApiModelProperty(name = "open_port", value = "开放端口列表", required = true)
//    @JSONField(name = "open_port")
    private String openPort;

    @ApiModelProperty(name = "open_port_all", value = "开放端口列表", required = true)
//    @JSONField(name = "open_port_all")
    private String openPortAll;


    @ApiModelProperty(name = "area_name", value = "区域名称", required = true)
//    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(name = "company", value = "厂商", required = true)
    @JSONField(name = "company")
    private String company;



    @ApiModelProperty(name = "alive", value = "在线状态", required = true)
    @JSONField(name = "alive")
    private Integer alive;

    @ApiModelProperty(name = "alive_name", value = "在线状态名称")
//    @JSONField(name = "alive_name")
    private String aliveName;

    @ApiModelProperty(name = "alive_qz", value = "检疫区在线状态")
//    @JSONField(name = "alive_qz")
    private Integer aliveQz;

    @ApiModelProperty(name = "alive_qz_name", value = "检疫区在线状态名称")
//    @JSONField(name = "alive_qz_name")
    private String aliveQzName;

    @ApiModelProperty(name = "mute", value = "是否哑终端", required = true)
    @JSONField(name = "mute")
    private Integer mute;

    @ApiModelProperty(name = "acct_session_time", value = "在线时长")
//    @JSONField(name = "acct_session_time")
    private String acctSessionTime;

    @ApiModelProperty(name = "acct_start_time", value = "认证时间")
//    @JSONField(name = "acct_start_time")
    private String acctStartTime;

    @ApiModelProperty(name = "server_os", value = "服务器操作系统版本", required = true)
//    @JSONField(name = "server_os")
    private String serverOs;

    @ApiModelProperty(name = "device_type", value = "设备类型", required = true)
//    @JSONField(name = "device_type")
    private Integer deviceType;


    @ApiModelProperty(name = "device_type_name", value = "设备类型名称", required = true)
    private String deviceTypeName;

    @ApiModelProperty(name = "model_des", value = "设备型号", required = true)
    private String modelDes;

    @ApiModelProperty(name = "brand_name", value = "设备品牌", required = true)
    private String brandName;

    @ApiModelProperty(name = "deviceTypeNameDetect", value = "识别设备类型")
    private String deviceTypeNameDetect;
    @ApiModelProperty(name = "modelDesDetect", value = "识别设备型号")
    private String modelDesDetect;
    @ApiModelProperty(name = "brandDetect",value = "识别品牌")
    private String brandNameDetect;



    @ApiModelProperty(name = "model_level", value = "设备识别等级  0:未识别；1:协议扫描识别; 2:端口扫描识别;", required = true)
//    @JSONField(name = "model_level")
    private Integer modelLevel;


    @ApiModelProperty(name = "serial_no", value = "设备序列号", required = true)
//    @JSONField(name = "serial_no")
    private String serialNo;

    @ApiModelProperty(name = "version", value = "软件版本", required = true)
    @JSONField(name = "version")
    private String version;

    @ApiModelProperty(name = "mac_scan", value = "协议扫描到的MAC地址", required = true)
//    @JSONField(name = "mac_scan")
    private String macScan;

    @ApiModelProperty(name = "deal_status", value = "0新数据，1放行，2阻断", required = true)
//    @JSONField(name = "deal_status")
    private Integer dealStatus;

    @ApiModelProperty(name = "auth_mark", value = "审核意见", required = true)
//    @JSONField(name = "auth_mark")
    private String authMark;

    @ApiModelProperty(name = "auth_person", value = "审核人", required = true)
//    @JSONField(name = "auth_person")
    private String authPerson;

    @ApiModelProperty(name = "auth_time", value = "审核时间", required = true)
//    @JSONField(name = "auth_time")
    private String authTime;


    @ApiModelProperty(name = "point_name", value = "点位名称", required = true)
//    @JSONField(name = "point_name")
    private String pointName;

    @ApiModelProperty(name = "create_time", value = "入网时间")
    @JSONField(name = "createTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    private String vlan;

    private String interfaceDetail;

    private int isVlanErr;

    private int isAreaErr;

    private int isOsErr;

    private int isModelDesErr;

    private int isIpClashErr;

    private int isIpNotAllocErr;

    private Integer detectType;



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
