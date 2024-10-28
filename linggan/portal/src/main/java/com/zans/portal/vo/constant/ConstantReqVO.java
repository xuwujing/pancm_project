package com.zans.portal.vo.constant;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ConstantReqVO {

    private Integer id;

    @ApiModelProperty(name = "constant_key", value = "常量key")
    @JSONField(name = "constant_key")
    @NotNull
    private String constantKey;

    @ApiModelProperty(name = "status", value = "是否可用")
    @JSONField(name = "status")
    private Integer status;

    @ApiModelProperty(name = "comment", value = "备注")
    @JSONField(name = "comment")
    private String comment;

    @ApiModelProperty(name = "constant_value", value = "常量值")
    @JSONField(name = "constant_value")
    @NotNull
    private String constantValue;

    @ApiModelProperty(name = "constant_type", value = "常量类型")
    @JSONField(name = "constant_type")
    @NotNull
    private String constantType;

}
