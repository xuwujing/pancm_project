package com.zans.portal.vo.device;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel
@Data
public class DeviceSearchVO extends BasePage {

    @ApiModelProperty(name = "device_type_id", value = "设备类型")
    private Integer deviceTypeId;

    @ApiModelProperty(name = "model_code", value = "设备型号")
    private String modelCode;

    @ApiModelProperty(name = "model_des", value = "设备名称")
    private String modelDes;

    @ApiModelProperty(name = "mute", value = "哑终端")
    @JSONField(name = "mute")
    private Integer mute;

    @ApiModelProperty(name = "company", value = "公司")
    private String company;

    @ApiModelProperty(name = "brand_id", value = "公司id")
    private String brandId;

}
