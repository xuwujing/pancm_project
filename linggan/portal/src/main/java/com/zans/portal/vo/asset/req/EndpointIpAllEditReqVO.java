package com.zans.portal.vo.asset.req;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description="资产编辑请求体")
@Data
public class EndpointIpAllEditReqVO {
    // 点位名称 所属辖区 设备型号 是否哑终端 承建单位  承建电话   维护联系人
    // 设备类型 设备品牌 ip地址  项目名称   承建联系人 维护单位  维护电话

    @ApiModelProperty(name = "point_name", value = "点位名称",required = true)
    @NotNull(message = "点位名称不能为空")
    @JSONField(name = "point_name")
    private String pointName;

    @ApiModelProperty(name = "device_type", value = "设备类型",required = true)
    @NotNull(message = "设备类型不能为空")
    @JSONField(name = "device_type")
    private Integer deviceTypeId;

    @ApiModelProperty(name = "brand_name", value = "设备品牌")
    @JSONField(name = "brand")
    private String brandName;

    @ApiModelProperty(name = "model_des", value = "设备型号")
    @JSONField(name = "model_des")
    private String modelDes;

    @ApiModelProperty(name = "mute", value = "是否哑终端 0：活跃终端；1：哑终端")
    @JSONField(name = "mute")
    private Integer mute;

    @ApiModelProperty(name = "project_name", value = "项目名称")
    @JSONField(name = "project_name")
    private String projectName;

    @ApiModelProperty(name = "contractor", value = "承建单位")
    @JSONField(name = "contractor")
    private String contractor;

    @ApiModelProperty(name = "contractor_person", value = "承建联系人")
    @JSONField(name = "contractor_person")
    private String contractorPerson;

    @ApiModelProperty(name = "contractor_phone", value = "承建电话")
    @JSONField(name = "contractor_phone")
    private String contractorPhone;

    @ApiModelProperty(name = "maintain_company", value = "维护单位")
    @JSONField(name = "maintain_company")
    private String maintainCompany;

    @ApiModelProperty(name = "maintain_person", value = "维护联系人")
    @JSONField(name = "maintain_person")
    private String maintainPerson;
    
    @ApiModelProperty(name = "maintain_phone", value = "维护电话")
    @JSONField(name = "maintain_phone")
    private String maintainPhone;

    @ApiModelProperty(name = "ip_addr", value = "维护电话")
    @JSONField(name = "ip_addr")
    private String ipAddr;
}
