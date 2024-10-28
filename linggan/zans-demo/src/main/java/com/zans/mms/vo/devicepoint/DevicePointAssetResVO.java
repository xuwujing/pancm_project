package com.zans.mms.vo.devicepoint;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "DevicePointAssetResVO", description = "")
@Data
public class DevicePointAssetResVO {
    private String deviceDirection;
    private String assetCode;
    private String networkIp;
    private String deviceModelBrand;
    private String installTime;
    private String deviceModelDes;
    private String laneNumber;
}
