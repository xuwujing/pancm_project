package com.zans.portal.vo.device;

import com.zans.base.vo.BasePage;
import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel
@Data
public class DeviceResponseVO extends BasePage {

    private Integer id;

    @ApiModelProperty(name = "brand_id", value = "厂商id")
    private Integer brandId;

    @ApiModelProperty(name = "model_code", value = "设备厂商")
    private String company;

    @ApiModelProperty(name = "model_code", value = "设备厂商")
    private String modelCode;

    @ApiModelProperty(name = "model_des", value = "设备具体型号名称")
    private String modelDes;

    @ApiModelProperty(name = "remark", value = "备注信息")
    private String remark;

    @ApiModelProperty(name = "device_type_id", value = "设备型号id")
    private Integer deviceTypeId;

    @ApiModelProperty(name = "device_type_name", value = "设备型号名称")
    private String deviceTypeName;

    @ApiModelProperty(name = "mute", value = "哑终端")
    private Integer mute;


}
