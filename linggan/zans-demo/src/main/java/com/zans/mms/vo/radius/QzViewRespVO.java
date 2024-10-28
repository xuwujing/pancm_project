package com.zans.mms.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class QzViewRespVO {

    @ApiModelProperty(name = "module_name", value = "模块名")
    @JSONField(name = "module_name")
    private String moduleName;

    @ApiModelProperty(name = "mac", value = "mac")
    @JSONField(name = "mac")
    private String mac;

    @ApiModelProperty(name = "type_name", value = "设备类型", required = true)
    @JSONField(name = "type_name")
    private String typeName;

    @ApiModelProperty(name = "device_model_brand", value = "设备品牌", required = true)
    @JSONField(name = "device_model_brand")
    private String deviceModelBrand;

    @ApiModelProperty(name = "company", value = "厂商", required = true)
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "device_model_des", value = "设备型号", required = true)
    @JSONField(name = "device_model_des")
    private String deviceModelDes;

    @ApiModelProperty(name = "server_os", value = "操作系统", required = true)
    @JSONField(name = "server_os")
    private String serverOs;

    @ApiModelProperty(name = "cur_open_port", value = "开放端口", required = true)
    @JSONField(name = "cur_open_port")
    private String curOpenPort;

    @ApiModelProperty(name = "point_name", value = "点位名称", required = true)
    @JSONField(name = "point_name")
    private String pointName;

    @ApiModelProperty(name = "project_name", value = "项目名称", required = true)
    @JSONField(name = "project_name")
    private String projectName;

    @ApiModelProperty(name = "area_name", value = "区域名称", required = true)
    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(name = "maintain_company", value = "承建单位", required = true)
    @JSONField(name = "maintain_company")
    private String maintainCompany;

    @ApiModelProperty(name = "contractor_person", value = "联系人", required = true)
    @JSONField(name = "contractor_person")
    private String contractorPerson;

    @ApiModelProperty(name = "contractor_phone", value = "联系电话", required = true)
    @JSONField(name = "contractor_phone")
    private String contractorPhone;

    @ApiModelProperty(name = "ip_status", value = "0：未找到对应的Ip_All;1：正常", required = true)
    @JSONField(name = "ip_status")
    private Integer ipStatus = 1;

    @ApiModelProperty(name = "arp_status", value = "0：未找到对应的ARP;1：正常")
    @JSONField(name = "arp_status")
    private Integer arpStatus = 1;

    @ApiModelProperty(name = "ip_addr", value = "IP地址")
    @JSONField(name = "ip_addr")
    private String ipAddr;

    //2020-9-27 和北傲确认，资产对比新增如下字段显示
    @ApiModelProperty(name = "called_station_id", value = "nas接口的mac地址", required = true)
    @JSONField(name = "called_station_id")
    private String calledStationId;

    @ApiModelProperty(name = "calling_station_id", value = "终端mac地址", required = true)
    @JSONField(name = "calling_station_id")
    private String callingStationId;

    @ApiModelProperty(name = "nas_name", value = "nas名称", required = true)
    @JSONField(name = "nas_name")
    private String nasName;

    @ApiModelProperty(name = "nas_ip", value = "nas_ip", required = true)
    @JSONField(name = "nas_ip")
    private String nasIp;

    @ApiModelProperty(name = "nas_port", value = "nas端口", required = true)
    @JSONField(name = "nas_port")
    private Integer nasPort;

    @ApiModelProperty(name = "nas_port_type", value = "nas类型", required = true)
    @JSONField(name = "nas_port_type")
    private String nasPortType;

    @ApiModelProperty(name = "nas_port_id", value = "nas接口名称", required = true)
    @JSONField(name = "nas_port_id")
    private String nasPortId;

    private String vlan;

}
