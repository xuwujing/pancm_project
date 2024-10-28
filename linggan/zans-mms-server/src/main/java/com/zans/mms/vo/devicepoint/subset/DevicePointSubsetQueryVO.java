package com.zans.mms.vo.devicepoint.subset;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
@ApiModel(value = "DevicePointSubsetQueryVO", description = "")
@Data
public class DevicePointSubsetQueryVO extends BasePage implements Serializable {
    private String subsetName;
}
