package com.zans.mms.vo.asset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "AssetResVo", description = "")
@Data
public class AssetMonitorResVO {


    private String ipAddr;
    private String deviceTypeName;
    private String deviceType;


    private String faultTypesName;
    private String faultTypes;
    private Integer diagnosisResult;
    private String diagnosisTime;
    private String assetCode;
    private String traceId;

}
