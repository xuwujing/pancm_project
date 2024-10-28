package com.zans.portal.vo.constant;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ConstantItemReqVO {

    private Integer id;

    @ApiModelProperty(name = "dict_key", value = "常量的类型")
    @JSONField(name = "dict_key")
    @NotNull
    private String dictKey;

    @ApiModelProperty(name = "item_key", value = "常量的取值，表中字段的取值")
    @JSONField(name = "item_key")
    @NotNull
    private String itemKey;

    @ApiModelProperty(name = "item_value", value = "常量value，显示用")
    @JSONField(name = "item_value")
    @NotNull
    private String itemValue;

    @ApiModelProperty(name = "ordinal", value = "排序")
    @JSONField(name = "ordinal")
    private Integer ordinal;

    @ApiModelProperty(name = "class_type", value = "数值类型")
    @JSONField(name = "class_type")
    private String classType;

}
