package com.zans.portal.vo.custom;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class CustomRespVO {

    @ApiModelProperty(name = "id", value = "主键")
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(name = "dataId", value = "主键")
    @JSONField(name = "dataId")
    private Integer dataId;

    @ApiModelProperty(name = "module_name", value = "模块名称")
    @JSONField(name = "module_name")
    @NotNull
    private String moduleName;

    @ApiModelProperty(name = "field_key", value = "列键")
    @JSONField(name = "field_key")
    private String fieldKey;

    @ApiModelProperty(name = "field_name", value = "列名称")
    @JSONField(name = "field_name")
    @NotNull
    private String fieldName;

    @ApiModelProperty(name = "field_value", value = "列值")
    @JSONField(name = "field_value")
    private String fieldValue;

    @ApiModelProperty(name = "field_type", value = "列类型")
    @JSONField(name = "field_type")
    @NotNull
    private Integer fieldType;

    @ApiModelProperty(name = "column_name", value = "关联的列名")
    @JSONField(name = "column_name")
    private String columnName;

    @ApiModelProperty(name = "isrequire", value = "校验规则")
    @JSONField(name = "isrequire")
    private String isrequire;

    @ApiModelProperty(name = "default_value", value = "默认值")
    @JSONField(name = "default_value")
    private String defaultValue;

    @ApiModelProperty(name = "field_desc", value = "备注")
    @JSONField(name = "field_desc")
    private String fieldDesc;

    @ApiModelProperty(name = "sort", value = "排序")
    @JSONField(name = "sort")
    private Integer sort;

    @ApiModelProperty(name = "options", value = "多选值")
    @JSONField(name = "options")
    private List<CustomOptionRespVO> options;

}
