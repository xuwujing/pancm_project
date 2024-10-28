package com.zans.portal.vo.arp;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ArpEditVO {

    @ApiModelProperty(name = "id",value = "设备ID", required = true)
    @NotNull(message = "设备ID必填")
    private Integer id;



    @ApiModelProperty(name = "region_first", value = "大区ID")
    @JSONField(name = "region_first")
    private Integer regionFirst;

    @ApiModelProperty(name = "region_second", value = "小区ID")
    @JSONField(name = "region_second")
    private Integer regionSecond;

    @JSONField(name = "project_name")
    @ApiModelProperty(name = "project_name", value = "项目名称")
    private String projectName;

    @JSONField(name = "point_name")
    @ApiModelProperty(name = "point_name", value = "点位名称")
    private String pointName;

    @JSONField(name = "project_status")
    @ApiModelProperty(name = "project_status", value = "项目维护状态")
    private Integer projectStatus;

    @JSONField(name = "confirm_status")
    @ApiModelProperty(name = "confirm_status", value = "档案确认状态")
    private Integer confirmStatus;

    @JSONField(name = "contractor")
    @ApiModelProperty(name = "contractor", value = "承建单位")
    private String contractor;

    @JSONField(name = "contractor_person")
    @ApiModelProperty(name = "contractor_person", value = "承建联系人")
    private String contractorPerson;

    @JSONField(name = "contractor_phone")
    @ApiModelProperty(name = "contractor_phone", value = "承建电话")
    private String contractorPhone;

    @JSONField(name = "maintain_company")
    @ApiModelProperty(name = "maintain_company", value = "维护公司")
    private String maintainCompany;

    @JSONField(name = "maintain_person")
    @ApiModelProperty(name = "maintain_person", value = "维护联系人")
    private String maintainPerson;

    @JSONField(name = "maintain_phone")
    @ApiModelProperty(name = "maintain_phone", value = "维护电话")
    private String maintainPhone;

}
