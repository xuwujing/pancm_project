package com.zans.portal.vo.asset.resp;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel
@Data
public class AssetMapRespVO {
    @JSONField(name = "id")
    @ApiModelProperty(name = "id", value = "id")
    private Integer id;

//    @JSONField(name = "ip_addr")
    @ApiModelProperty(name = "ip_addr", value = "ip地址")
    private String ipAddr;

    @JSONField(name = "longitude")
    @ApiModelProperty(name = "longitude", value = "经度")
    private String longitude;

    @JSONField(name = "latitude")
    @ApiModelProperty(name = "latitude", value = "纬度")
    private String latitude;

//    @JSONField(name = "contractor_person")
    @ApiModelProperty(name = "contractor_person", value = "联系人")
    private String contractorPerson;

//    @JSONField(name = "project_name")
    @ApiModelProperty(name = "project_name", value = "项目名称")
    private String projectName;

//    @JSONField(name = "maintain_company")
    @ApiModelProperty(name = "maintain_company", value = "维护单位")
    private String maintainCompany;

//    @JSONField(name = "point_name")
    @ApiModelProperty(name = "point_name", value = "点位名称")
    private String pointName;

//    @JSONField(name = "brand_name")
    @ApiModelProperty(name = "brand_name", value = "名牌")
    private String brandName;

    @JSONField(name = "alive")
    @ApiModelProperty(name = "alive", value = "1.在线 2离线")
    private Integer alive;

    @JSONField(name = "mac")
    @ApiModelProperty(name = "mac", value = "mac地址")
    private String mac;

//    @JSONField(name = "access_policy")
    @ApiModelProperty(name = "access_policy", value = "0,阻断;1,检疫区;2,放行")
    private Integer accessPolicy;

//    @JSONField(name = "delete_status")
    @ApiModelProperty(name = "delete_status", value = "0,未删除")
    private Integer deleteStatus;

//    @JSONField(name = "department_id")
    @ApiModelProperty(name = "department_id", value = "部门id")
    private Integer departmentId;

//    @JSONField(name = "department_name")
    @ApiModelProperty(name = "department_name", value = "部门name")
    private String departmentName;




//    @JSONField(name = "device_type")
    @ApiModelProperty(name = "device_type", value = "设备类型id")
    private Integer deviceType;

//    @JSONField(name = "type_name")
    @ApiModelProperty(name = "type_name", value = "设备类型名称")
    private String typeName;

    @ApiModelProperty(name = "deviceStatus", value = "设备状态;-1 未知;0.阻断;1.在线;2.离线;3.告警;")
    private String deviceStatus;

    @JSONField(name = "name")
    @ApiModelProperty(name = "name", value = "点位名称")
    private String name;

//    @JSONField(name = "nas_ip_address")
    @ApiModelProperty(name = "nas_ip_address", value = "点位名称")
    private String nasIpAddress;
}
