package com.zans.portal.vo.network;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xv
 * @since 2020/6/5 0:28
 */
@ApiModel
@Data
public class ProjectEditVO {

    @NotNull(message = "项目ID必填")
    @ApiModelProperty(value = "项目ID，主键")
    private Integer id;

    @NotEmpty(message = "项目名称必填")
    @ApiModelProperty(value = "项目名称")
    @JSONField(name = "project_name")
    private String projectName;

    /**
     * 项目类型，局自建项目、新改扩建项目
     */
    @NotNull(message = "项目类型必填")
    @ApiModelProperty(value = "项目类型")
    @JSONField(name = "project_type")
    private Integer projectType;

    /**
     * 接入片区
     */
    @NotNull(message = "接入片区必填")
    @ApiModelProperty(value = "接入片区，汉口/汉阳/武昌")
    private Integer region;

    /**
     * 承建单位
     */
    @NotEmpty(message = "承建单位必填")
    @ApiModelProperty(value = "承建单位")
    private String contractor;

    /**
     * 承建联系人
     */
    @ApiModelProperty(value = "承建联系人")
    @JSONField(name = "contractor_person")
    private String contractorPerson;

    /**
     * 承建联系电话
     */
    @ApiModelProperty(value = "承建联系电话")
    @JSONField(name = "contractor_phone")
    private String contractorPhone;

    /**
     * 派单日期
     */
//    @ApiModelProperty(value = "派单日期")
//    @JSONField(name = "dispatch_date", format = "yyyy-MM-dd HH:mm:ss")
//    private Date dispatchDate;

    /**
     * 建设时间，单位天
     */
    @ApiModelProperty(value = "建设时间，单位天")
    @JSONField(name = "build_day")
    private Integer buildDay;

    /**
     * 链路数量，填写
     */
    @NotNull(message = "链路数量必填")
    @ApiModelProperty(value = "链路数量")
    @JSONField(name = "link_count")
    private Integer linkCount;
}
