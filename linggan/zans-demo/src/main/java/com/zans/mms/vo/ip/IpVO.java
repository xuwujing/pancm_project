package com.zans.mms.vo.ip;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.mms.vo.chart.OnlineCircleUnit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * IpAll 返回信息
 */
@Data
@ApiModel
public class IpVO {

    private Integer id;

    @JSONField(name = "point_name")
    @ApiModelProperty(name = "point_name", value = "点位名称")
    private String pointName;

    @JSONField(name = "area")
    @ApiModelProperty(name = "area", value = "所属辖区编号")
    private Integer areaId;

    @JSONField(name = "area_name")
    @ApiModelProperty(name = "area_name", value = "所属辖区名称")
    private String areaName;

    @JSONField(name = "device_type")
    @ApiModelProperty(name = "device_type", value = "分配类型编号")
    private Integer deviceType;

    @JSONField(name = "device_type_name")
    @ApiModelProperty(name = "device_type_name", value = "分配类别名称")
    private String deviceTypeName;

    @JSONField(name = "device_model_type")
    @ApiModelProperty(name = "device_model_type", value = "设备类别")
    private String deviceModelType;

    @JSONField(name = "device_model_brand")
    @ApiModelProperty(name = "device_model_brand", value = "设备品牌")
    private String deviceModelBrand;

    @JSONField(name = "device_model_des")
    @ApiModelProperty(name = "device_model_des", value = "设备型号")
    private String deviceModelDes;

    @JSONField(name = "ip_addr")
    @ApiModelProperty(name = "ip_addr", value = "IP地址")
    private String ipAddr;

    @JSONField(name = "mask")
    @ApiModelProperty(name = "mask", value = "掩码")
    private String mask;

    @ApiModelProperty(name = "gateway", value = "网关")
    private String gateway;

    @ApiModelProperty(name = "vlan", value = "vlan")
    private String vlan;

    @JSONField(name = "the_date", format = "yyyy-MM-dd")
    @ApiModelProperty(name = "the_date", value = "分配日期")
    private Date theDate;

    @JSONField(name = "project_name")
    @ApiModelProperty(name = "project_name", value = "项目名称")
    private String projectName;

    @ApiModelProperty(name = "contractor", value = "承建单位")
    private String contractor;

    @JSONField(name = "contractor_person")
    @ApiModelProperty(name = "contractor_person", value = "承建联系人")
    private String contractorPerson;

    @JSONField(name = "contractor_phone")
    @ApiModelProperty(name = "contractor_phone", value = "承建电话")
    private String contractorPhone;

    @JSONField(name = "project_status")
    @ApiModelProperty(name = "project_status", value = "项目维护状态")
    private Integer projectStatus;

    @JSONField(name = "project_status_name")
    @ApiModelProperty(name = "project_status_name", value = "项目维护状态名称")
    private String projectStatusName;

    @JSONField(name = "maintain_company")
    @ApiModelProperty(name = "maintain_company", value = "维护公司")
    private String maintainCompany;

    @JSONField(name = "maintain_person")
    @ApiModelProperty(name = "maintain_person", value = "维护联系人")
    private String maintainPerson;

    @JSONField(name = "maintain_phone")
    @ApiModelProperty(name = "maintain_phone", value = "维护电话")
    private String maintainPhone;


    @JSONField(name = "auth_status")
    @ApiModelProperty(name = "auth_status", value = "入网认证状态")
    private Integer authStatus;

    @JSONField(name = "auth_status_name")
    @ApiModelProperty(name = "auth_status_name", value = "入网认证状态名称")
    private String authStatusName;

    @JSONField(name = "auth_mac")
    @ApiModelProperty(name = "auth_mac", value = "认证mac地址")
    private String authMac;

    @JSONField(name = "auth_person")
    @ApiModelProperty(name = "auth_person", value = "入网认证申请人")
    private String authPerson;

    @JSONField(name = "auth_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "auth_time", value = "入网认证申请时间")
    private Date authTime;

    @JSONField(name = "auth_permit_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "auth_permit_time", value = "入网认证审批时间")
    private Date authPermitTime;

    @JSONField(name = "auth_permit_person")
    @ApiModelProperty(name = "auth_permit_person", value = "入网认证审批人")
    private String authPermitPerson;

    @JSONField(name = "auth_mark")
    @ApiModelProperty(name = "auth_mark", value = "入网认证审批意见")
    private String authMark;

    @JSONField(name = "arp_mac")
    @ApiModelProperty(name = "arp_mac", value = "入网mac地址")
    private String arpMac;

    @ApiModelProperty(name = "department_name", value = "单位名称")
    @JSONField(name = "department_name")
    private String departmentName;

    @ApiModelProperty(name = "department_id", value = "单位id")
    @JSONField(name = "department_id")
    private Integer departmentId;

    @ApiModelProperty(value = "接入点的经度")
    private String longitude;

    @ApiModelProperty(value = "接入点的纬度")
    private String latitude;

    @ApiModelProperty(value = "创建时间")
    @JSONField(name = "create_time")
    private String createTime;

    @ApiModelProperty(value = "是否哑终端")
    @JSONField(name = "mute")
    private Integer mute;

    @ApiModelProperty(value = "是否哑终端名称")
    @JSONField(name = "mute_name")
    private String muteName;

    public void setIpAuthStatusNameByMap(Map<Object, String> map) {
        Integer status = this.authStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setAuthStatusName(name);
    }

    public void setProjectStatusNameByMap(Map<Object, String> map) {
        Integer status = this.projectStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setProjectStatusName(name);
    }

    public void setMuteNameByMap(Map<Object, String> map) {
        Integer status = this.mute;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setMuteName(name);
    }

    public void setDepartmentNameByList(List<OnlineCircleUnit> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (departmentId == null){
            return;
        }
        for (OnlineCircleUnit unit : list) {
            if (departmentId.equals(unit.getId())) {
                this.departmentName = unit.getName();
                break;
            }
        }
    }
}
