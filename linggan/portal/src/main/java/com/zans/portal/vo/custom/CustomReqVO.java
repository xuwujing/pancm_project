package com.zans.portal.vo.custom;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CustomReqVO extends BasePage {

    @ApiModelProperty(name = "module_name", value = "模块名称")
    @JSONField(name = "module_name")
    private String moduleName;

    @ApiModelProperty(name = "field_key", value = "键")
    @JSONField(name = "field_key")
    private String fieldKey;

    @ApiModelProperty(name = "field_name", value = "名称")
    @JSONField(name = "field_name")
    private String fieldName;

}
