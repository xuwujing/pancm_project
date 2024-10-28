package com.zans.portal.vo.setting;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class SettingReqVO {

    private Integer id;

    @ApiModelProperty(name = "setting_key", value = "系统设置的键")
    @JSONField(name = "setting_key")
    @NotNull
    private String settingKey;

    @ApiModelProperty(name = "setting_value", value = "系统设置的值")
    @JSONField(name = "setting_value")
    @NotNull
    private String settingValue;

    @ApiModelProperty(name = "setting_name", value = "显示的name")
    @JSONField(name = "setting_name")
    @NotNull
    private String settingName;

    @ApiModelProperty(name = "setting_type", value = "系统设置的值类型")
    @JSONField(name = "setting_type")
    @NotNull
    private Integer settingType;

    @ApiModelProperty(name = "setting_around", value = "系统设置的取值范围")
    @JSONField(name = "setting_around")
    private String settingAround;

}
