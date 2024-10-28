package com.zans.mms.vo.device;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class DeviceTypeResVO extends BasePage {

    @ApiModelProperty(name = "type_id", value = "设备类别编号")
    @JSONField(name = "type_id")
    private Integer typeId;

    @ApiModelProperty(name = "type_name", value = "类型名称")
    @JSONField(name = "type_name")
    @NotNull
    private String typeName;

    @ApiModelProperty(name = "mute", value = "是否哑终端")
    @JSONField(name = "mute")
    @NotNull
    private Integer mute;

    @ApiModelProperty(name = "template", value = "模板地址")
    @JSONField(name = "template")
    private String template;

}
