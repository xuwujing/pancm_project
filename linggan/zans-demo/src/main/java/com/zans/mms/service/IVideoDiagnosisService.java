package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosticThresholdReqVO;
import com.zans.mms.vo.asset.diagnosis.AssetThresholdVO;

import java.util.List;

/**
 * @author beixing
 * @Title: zans-demo
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/27
 */
public interface IVideoDiagnosisService {


    ApiResult diagnosisConfig(AssetDiagnosticThresholdReqVO assetDiagnosticThresholdReqVOS);

    ApiResult diagnosisOverallConfig(List<AssetThresholdVO> assetThresholdVOS);

    ApiResult diagnosisOverallView();

    ApiResult diagnosisConfigView(String deviceId,String ipAddress);
}
