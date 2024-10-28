package com.zans.portal.vo.custom;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class CustomOptionRespVO {

    @ApiModelProperty(name = "id", value = "主键")
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(name = "option_key", value = "选项键")
    @JSONField(name = "option_key")
    private String optionKey;

    @ApiModelProperty(name = "option_name", value = "显示名称")
    @JSONField(name = "option_name")
    private String optionName;

    @ApiModelProperty(name = "checked", value = "是否默认选中 1:选中；0:不选中")
    @JSONField(name = "checked")
    private Integer checked;

    @ApiModelProperty(name = "custom_field_id", value = "列类型id")
    @JSONField(name = "custom_field_id")
    private String customFieldId;


}
