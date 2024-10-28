package com.zans.portal.vo.asset.req;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description="资产新增请求体")
@Data
public class EndpointIpAllAddReqVO {
    // 点位名称 所属辖区 设备型号 是否哑终端 承建单位  承建电话   维护联系人
    // 设备类型 设备品牌 ip地址  项目名称   承建联系人 维护单位  维护电话
    @ApiModelProperty(name = "point_name", value = "点位名称",required = true)
    @NotNull(message = "点位名称不能为空")
    private String pointName;

    @ApiModelProperty(name = "device_type", value = "设备类型",required = true)
    @NotNull(message = "设备类型不能为空")
    private Integer deviceTypeId;


    @ApiModelProperty(name = "device_model_brand", value = "设备品牌")
    private String deviceModelBrand;

    @ApiModelProperty(name = "device_model_des", value = "设备型号")
    private String deviceModelDes;

    @ApiModelProperty(name = "ip_addr", value = "IP地址",required = true)
    @NotNull(message = "IP地址不能为空")
    private String ipAddr;

    @ApiModelProperty(name = "mute", value = "是否哑终端 0：活跃终端；1：哑终端")
    private Integer mute;

    @ApiModelProperty(name = "project_name", value = "项目名称")
    private String projectName;

    @ApiModelProperty(name = "contractor", value = "承建单位")
    private String contractor;

    @ApiModelProperty(name = "contractor_person", value = "承建联系人")
    private String contractorPerson;

    @ApiModelProperty(name = "contractor_phone", value = "承建电话")
    private String contractorPhone;

    @ApiModelProperty(name = "maintain_company", value = "维护单位")
    private String maintainCompany;

    @ApiModelProperty(name = "maintain_person", value = "维护联系人")
    private String maintainPerson;
    
    @ApiModelProperty(name = "maintain_phone", value = "维护电话")
    private String maintainPhone;

    
}
