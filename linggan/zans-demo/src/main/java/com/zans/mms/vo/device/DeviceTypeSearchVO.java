package com.zans.mms.vo.device;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class DeviceTypeSearchVO extends BasePage {

    @ApiModelProperty(name = "type_name", value = "类型名称")
    @JSONField(name = "type_name")
    private String typeName;

}
