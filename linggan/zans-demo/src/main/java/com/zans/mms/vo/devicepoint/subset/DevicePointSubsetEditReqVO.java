package com.zans.mms.vo.devicepoint.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DevicePointSubsetEditReqVO", description = "")
@Data
public class DevicePointSubsetEditReqVO {
    private String subsetName;
    private Integer sort;
    @NotBlank
    private Long subsetId;
    private String remark;

}
