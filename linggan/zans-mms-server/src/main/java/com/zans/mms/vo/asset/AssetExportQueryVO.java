package com.zans.mms.vo.asset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "AssetExportQueryVO", description = "")
@Data
public class AssetExportQueryVO implements Serializable {
    private String pointName;
    private String pointCode;
    private List<String> areaId;
    private String assetCode;
    private List<String> maintainStatus;
    private String networkIp;
    private String projectName;
    private String deviceType;
    private Long   pointId;
    private String deviceSubType;
    private String deviceModelBrand;

    /**
     * 所属单位id
     */
    private List<String> orgId;

    private List<Integer> diagnosisResult;
}
