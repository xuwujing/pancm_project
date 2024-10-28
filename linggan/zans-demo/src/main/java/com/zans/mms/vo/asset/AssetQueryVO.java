package com.zans.mms.vo.asset;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "AssetQueryVo", description = "")
@Data
public class AssetQueryVO extends BasePage implements Serializable {
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
    private String ipAddr;

    /**
     * 所属单位id
     */
    private String orgId;

    private Integer diagnosisResult;
}
