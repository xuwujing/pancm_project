package com.zans.portal.vo.ip;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class IpEditVO {

    private Integer id;

    @JSONField(name = "point_name")
    @ApiModelProperty(name = "point_name", value = "点位名称")
    private String pointName;

    @JSONField(name = "device_type")
    private Integer deviceTypeId;

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

    @JSONField(name = "the_date")
    @ApiModelProperty(name = "the_date", value = "分配日期")
    private String theDate;

    @JSONField(name = "project_name")
    @ApiModelProperty(name = "project_name", value = "项目名称")
    private String projectName;

    @JSONField(name = "contractor_phone")
    @ApiModelProperty(name = "", value = "")
    private String contractorPhone;

    @JSONField(name = "project_status")
    @ApiModelProperty(name = "project_status", value = "项目维护状态")
    private Integer projectStatus;

    @JSONField(name = "maintain_company")
    @ApiModelProperty(name = "maintain_company", value = "维护公司")
    private String maintainCompany;

    @JSONField(name = "maintain_person")
    @ApiModelProperty(name = "maintain_person", value = "维护联系人")
    private String maintainPerson;

    @JSONField(name = "maintain_phone")
    @ApiModelProperty(name = "maintain_phone", value = "维护电话")
    private String maintainPhone;

    @ApiModelProperty(name = "contractor", value = "承建单位")
    private String contractor;

    @JSONField(name = "contractor_person")
    @ApiModelProperty(name = "contractor_person", value = "联系人")
    private String contractorPerson;

    @JSONField(name = "auth_status")
    @ApiModelProperty(name = "auth_status", value = "入网认证状态")
    private Integer authStatus;

    
    @JSONField(name = "mute")
    private Integer mute;
}
