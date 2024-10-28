package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@ApiModel
@Data
public class SwitcherInterfaceReqVO extends BasePage {

    @ApiModelProperty(value = "sw_id")
    @JSONField(name = "sw_id")
    @NotNull
    private Integer swId;

}
