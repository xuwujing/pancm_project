package com.zans.portal.vo.switcher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel
@Data
public class SwitchBranchMergeVO {

    @ApiModelProperty(name = "id", value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "ipAddr", value = "ip", required = true)
    @NotNull(message = "ip")
    @Pattern(regexp = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}",message = "ip地址格式错误")
    private String ipAddr;

    @ApiModelProperty(name = "name", value = "交换机名称", required = true)
    private String sysName;


    @ApiModelProperty(name = "region", value = "区域", required = true)
    private Integer  region;

    @ApiModelProperty(name = "lon", value = "经度", required = true)
    @NotNull(message = "经度")
    @Max(value = 180,message = "经度")
    private BigDecimal lon;
    @ApiModelProperty(name = "lat", value = "纬度", required = true)
    @NotNull(message = "纬度")
    @Max(value = 90,message = "纬度")
    private BigDecimal lat;

//    private Integer online;
    @ApiModelProperty(name = "status", value = "状态 0启用 1 停用", required = true)
    private Integer status=0;//  '状态'0启用 1 停用,

    @ApiModelProperty(name = "swType", value = "交换机类型，0  核心交换机; 1   汇聚交换机; 2  ,接入交换机", required = true)
    @NotNull(message = "交换机类型")
    private Integer swType;

    @ApiModelProperty(name = "pointName", value = "点位名称", required = true)
    @NotNull(message = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "projectName", value = "项目名称", required = true)
    @NotNull(message = "项目名称")
    private String projectName;

    @ApiModelProperty(name = "brand", value = "品牌")
    private Integer brand;

    @ApiModelProperty(name = "swAccount", value = "登录账号")
    private String swAccount;

    @ApiModelProperty(name = "swPassword", value = "登录密码")
    private String swPassword;

    @ApiModelProperty(name = "community", value = "snmp名称")
    private String community;

    @ApiModelProperty(name = "acceptance", value = "状态 0未验收 1 已验收")
    private Integer acceptance;

    @ApiModelProperty(name = "acceptDate", value = "验收时间")
    private Date acceptDate;

    @ApiModelProperty(name = "acceptIdea", value = "验收意见")
    private String acceptIdea;
    @ApiModelProperty(name = "consBatch", value = "建设批次1 一期 2二期")
    private Integer consBatch;//

}
