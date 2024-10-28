package com.zans.portal.vo.device;

import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ExcelUnknownDeviceVO   {

    @ExcelProperty(ignore = true)
    private Integer id;

    @ExcelProperty(value = "公司", index = 1, validate = {"not_empty"},isSelect = true)
    @ApiModelProperty(name = "company", value = "厂商")
    private String company;

    @ExcelProperty(value = "设备型号", index = 2 , validate = {"not_empty"})
    @ApiModelProperty(name = "model_code", value = "设备型号")
    private String modelCode;

    @ExcelProperty(value = "设备名称", index = 3, validate = {"not_empty"})
    @ApiModelProperty(name = "model_des", value = "设备名称")
    private String modelDes;

    @ExcelProperty(value = "设备类型", index = 4, validate = {"not_empty"},isSelect = true)
    @ApiModelProperty(name = "device_type_name", value = "设备类型")
    private String deviceTypeName;

    @ExcelProperty(value = "备注", index = 5, validate = {"not_empty"},colors = "red")
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ExcelProperty(value = "是否哑终端", index = 6,isSelect = true)
    @ApiModelProperty(name = "mute", value = "哑终端")
    private String mute;

    @ExcelProperty(value = "更新使用ID", index = 7)
    private String endpointId;
    @ExcelProperty(value = "更新使用IP", index = 8)
    private String ipAddr;

}
