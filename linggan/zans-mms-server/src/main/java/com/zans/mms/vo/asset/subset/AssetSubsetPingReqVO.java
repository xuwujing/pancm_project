package com.zans.mms.vo.asset.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* @Title: AssetSubsetPingReqVO
* @Description: 资产子集一键ping ReqVO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/19/21
*/
@ApiModel(value = "AssetSubsetPingReqVO", description = "")
@Data
public class AssetSubsetPingReqVO {
    @NotNull
    private Long subsetId;

    private String pointName;

    private String pointCode;
    private String areaId;
    private String assetCode;
    private String maintainStatus;
    private String networkIp;
    private String projectName;
    private String deviceType;
    private String onlineStatus;
}
