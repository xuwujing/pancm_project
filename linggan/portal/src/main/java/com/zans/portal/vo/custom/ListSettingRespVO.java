package com.zans.portal.vo.custom;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class ListSettingRespVO {

    @ApiModelProperty(name = "id", value = "主键")
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(name = "module_name", value = "模块名")
    @JSONField(name = "module_name")
    @NotNull
    private String moduleName;

    @ApiModelProperty(name = "field_key", value = "键")
    @JSONField(name = "field_key")
    private String fieldKey;

    @ApiModelProperty(name = "field_name", value = "列名称")
    @JSONField(name = "field_name")
    @NotNull
    private String fieldName;

    @ApiModelProperty(name = "sort", value = "排序")
    @JSONField(name = "sort")
    private Integer sort;

    @ApiModelProperty(name = "userid", value = "用户id")
    @JSONField(name = "userid")
    private Integer userid;

}
