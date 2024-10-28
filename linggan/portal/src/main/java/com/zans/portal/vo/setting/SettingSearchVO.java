package com.zans.portal.vo.setting;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class SettingSearchVO extends BasePage {

    @ApiModelProperty(name = "setting_name", value = "常量名称", required = true)
    @JSONField(name = "setting_name")
    private String settingName;

}
