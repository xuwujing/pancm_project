package com.zans.mms.vo.alert;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AlertTypeSearchVO extends BasePage {

    @ApiModelProperty(name = "typeName",value = "告警类型名称")
    private String typeName;

}
