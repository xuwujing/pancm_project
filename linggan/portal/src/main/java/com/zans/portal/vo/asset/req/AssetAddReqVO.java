package com.zans.portal.vo.asset.req;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel
@Data
public class AssetAddReqVO {
    
    @ApiModelProperty(name = "pointName",value = "点位名称")
    @NotNull(message = "点位名称不能为空")
    private String pointName;

    @ApiModelProperty(name = "deviceType",value = "设备类型ID")
    @NotNull(message = "设备类型不能为空")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceModelBrand",value = "设备品牌")
    private String deviceModelBrand;

    @ApiModelProperty(name = "deviceModelDes",value = "设备型号")
    private String deviceModelDes;

    @ApiModelProperty(name = "ipAddr",value = "IP地址")
    @NotNull(message = "IP地址不能为空")
    private String ipAddr;

    @ApiModelProperty(name = "mute",value = "是否哑终端")
    private Integer mute;

    @ApiModelProperty(name = "projectName",value = "项目名称")
    private String projectName;

    @ApiModelProperty(name = "contractor",value = "承建单位")
    private String contractor;

    @ApiModelProperty(name = "contractorPerson",value = "承建联系人")
    private String contractorPerson;

    @ApiModelProperty(name = "contractorPhone",value = "承建联系人")
    private String contractorPhone;

    @ApiModelProperty(name = "maintainCompany",value = "维护单位")
    private String maintainCompany;

    @ApiModelProperty(name = "maintainPerson",value = "维护联系人")
    private String maintainPerson;

    @ApiModelProperty(name = "maintainPhone",value = "维护联系电话")
    private String maintainPhone;

    @ApiModelProperty(name = "maintainStatus",value = "维护状态")
    private Integer maintainStatus;

    @ApiModelProperty(name = "longitude",value = "经度")
    private String longitude;

    @ApiModelProperty(name = "latitude",value = "纬度")
    private String latitude;

    @ApiModelProperty(name = "departmentId",value = "资产部门id")
    private Integer departmentId;

}
