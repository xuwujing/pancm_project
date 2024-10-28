package com.zans.mms.vo.asset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "AssetResVo", description = "")
@Data
public class AssetViewResVO {
    private String areaId;
    private String pointName;
    private String pointCode;
    private Long pointId;
    private String powerWay;
    private String powerPlace ;
    private String longitude;
    private String latitude;
    private String projectName;
    private String buildYear;
    private String buildCompany;
    private String buildContact;
    private String buildPhone;
    private String maintainCompany;
    private String maintainContact;
    private String maintainPhone;
    private String deviceDirection;
    private String deviceModelBrand;
    private String deviceModelDes;
    private String networkIp;
    private String networkMask;
    private String networkGeteway;
    private String installTime;
    private String onlineStatus;
    private String maintainStatus;
    private String remark;
    private Long assetId;

}
