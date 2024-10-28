package com.zans.mms.vo.asset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "AssetResVo", description = "")
@Data
public class AssetResVO {
    private String areaId;
    private String pointName;
    private String deviceDirection;
    private String pointCode;
    private Long pointId;
    private String assetCode;
    private String networkIp;
    private String maintainCompany;
    private String buildCompany;
    private String onlineStatus;
    private String maintainStatus;
    private Long assetId;
    private String deviceSubType;


    private String deviceModelBrand;
    private String installTime;
    private String deviceModelDes;
    private String laneNumber;

    private String checkStatus;

    private String deviceTypeName;
    private String deviceType;
}
