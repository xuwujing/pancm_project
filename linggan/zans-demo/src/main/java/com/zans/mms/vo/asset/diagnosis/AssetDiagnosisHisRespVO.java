package com.zans.mms.vo.asset.diagnosis;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beiming
 * @Title: (AssetDiagnosisHisRespVO)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 14:43:12
 */
@ApiModel(value = "AssetDiagnosisHisRespVO", description = "")
@Data

public class AssetDiagnosisHisRespVO implements Serializable {
    private String  assetCode;
    private Integer diagnosisResult;
    private String diagnosisTime;
    private String traceId;
}
