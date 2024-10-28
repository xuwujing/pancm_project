package com.zans.portal.vo.device;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class DeviceMergeVO {

    private Integer id;

    @ApiModelProperty(name = "brand_id", value = "公司id", required = true)
    private Integer brandId;

    @ApiModelProperty(name = "company", value = "公司名称")
    @NotNull(message = "公司名称")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "device_type_id", value = "设备类型", required = true)
    @NotNull(message = "设备类型")
    @JSONField(name = "device_type_id")
    private Integer deviceTypeId;

    @ApiModelProperty(name = "model_code", value = "设备型号", required = true)
    @NotNull(message = "设备型号")
    @JSONField(name = "model_code")
    private String modelCode;

    @ApiModelProperty(name = "model_des", value = "设备名称", required = true)
    @NotNull(message = "设备名称")
    @JSONField(name = "model_des")
    private String modelDes;

    @ApiModelProperty(name = "remark", value = "备注信息")
    @JSONField(name = "remark")
    private String remark;

    @ApiModelProperty(name = "mute", value = "哑终端", required = true)
    @NotNull(message = "哑终端")
    @JSONField(name = "mute")
    private Integer mute;

}
