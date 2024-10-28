package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@ApiModel
@Data
public class SwitcherScanReqVO extends BasePage {

    @ApiModelProperty(value = "sw_id")
    @JSONField(name = "sw_id")
    @NotNull
    private Integer swId;

    @ApiModelProperty(value = "day")
    @JSONField(name = "day")
    private Integer day;

    @ApiModelProperty(name = "date", value = "自定义时间（时间范围）")
    @JSONField(name = "date")
    private String date[];

}
