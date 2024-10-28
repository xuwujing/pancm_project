package com.zans.mms.vo.devicepoint.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "DevicePointSubsetAddReqVO", description = "")
@Data
public class DevicePointSubsetAddReqVO {
    private String subsetName;
    private Integer sort;
    private String remark;
}
