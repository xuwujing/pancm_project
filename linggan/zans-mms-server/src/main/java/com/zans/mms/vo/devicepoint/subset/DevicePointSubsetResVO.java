package com.zans.mms.vo.devicepoint.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "DevicePointSubsetResVO", description = "")
@Data
public class DevicePointSubsetResVO {
    private String  subsetName;

    private String  remark;

    private Integer normalCount;

    private Integer stopCount;

    private Integer faultCount;

    private Integer demolishCount;

    private Long    subsetId;
}
