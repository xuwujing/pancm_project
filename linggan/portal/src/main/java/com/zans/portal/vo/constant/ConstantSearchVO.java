package com.zans.portal.vo.constant;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ConstantSearchVO extends BasePage {

    @ApiModelProperty(name = "comment", value = "显示名称", required = true)
    @JSONField(name = "comment")
    private String comment;

    @ApiModelProperty(name = "status", value = "是否可用")
    @JSONField(name = "status")
    private Integer status;

}
