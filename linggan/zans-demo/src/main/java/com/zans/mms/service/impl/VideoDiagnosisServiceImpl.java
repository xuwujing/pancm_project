package com.zans.mms.service.impl;

import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.guard.AssetDiagnosticThresholdDao;
import com.zans.mms.dao.guard.DiagnosticThresholdOverallDao;
import com.zans.mms.model.AssetDiagnosticThreshold;
import com.zans.mms.model.DiagnosticThresholdOverall;
import com.zans.mms.service.IVideoDiagnosisService;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosticThresholdReqVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosticThresholdVO;
import com.zans.mms.vo.asset.diagnosis.AssetThresholdVO;
import com.zans.mms.vo.asset.diagnosis.DiagnosticThresholdOverallVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author beixing
 * @Title: zans-demo
 * @Description: 视频诊断实现类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/27
 */
@Service
public class VideoDiagnosisServiceImpl implements IVideoDiagnosisService {

    @Resource
    private DiagnosticThresholdOverallDao diagnosticThresholdOverallDao;

    @Resource
    private AssetDiagnosticThresholdDao assetDiagnosticThresholdDao;

    @Override
    public ApiResult diagnosisConfig(AssetDiagnosticThresholdReqVO assetDiagnosticThresholdReqVOS) {
        List<AssetThresholdVO> assetThresholdVOS = assetDiagnosticThresholdReqVOS.getAssetThresholdVOS();
        String deviceId = assetDiagnosticThresholdReqVOS.getDeviceId();
        String ipAddress = assetDiagnosticThresholdReqVOS.getIpAddress();
        List<AssetDiagnosticThreshold> list = new ArrayList<>();
        for (AssetThresholdVO assetThresholdVO : assetThresholdVOS) {
            AssetDiagnosticThreshold assetDiagnosticThreshold = new AssetDiagnosticThreshold();
            assetDiagnosticThreshold.setFaultType(assetThresholdVO.getFaultType());
            assetDiagnosticThreshold.setFaultTypeName(assetThresholdVO.getFaultTypeName());
            assetDiagnosticThreshold.setFaultTypeThreshold(assetThresholdVO.getFaultTypeThreshold());
            assetDiagnosticThreshold.setIpaddress(ipAddress);
            assetDiagnosticThreshold.setDeviceId(deviceId);
            list.add(assetDiagnosticThreshold);
        }
        if(!StringHelper.isEmpty(list)){
            assetDiagnosticThresholdDao.deleteByIp(ipAddress,deviceId);
            assetDiagnosticThresholdDao.insertBatch(list);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult diagnosisOverallConfig(List<AssetThresholdVO> assetThresholdVOS) {
        for (AssetThresholdVO assetThresholdVO : assetThresholdVOS) {
            DiagnosticThresholdOverall diagnosticThresholdOverall = new DiagnosticThresholdOverall();
            diagnosticThresholdOverall.setFaultType(assetThresholdVO.getFaultType());
            diagnosticThresholdOverall.setFaultTypeThreshold(assetThresholdVO.getFaultTypeThreshold());
            diagnosticThresholdOverallDao.update(diagnosticThresholdOverall);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult diagnosisOverallView() {
        List<DiagnosticThresholdOverallVO> diagnosticThresholdOverallVOS = diagnosticThresholdOverallDao.queryAll(new DiagnosticThresholdOverallVO());
        return ApiResult.success(diagnosticThresholdOverallVOS);
    }

    @Override
    public ApiResult diagnosisConfigView(String deviceId,String ipAddress) {
        AssetDiagnosticThresholdVO assetDiagnosticThresholdVO = new AssetDiagnosticThresholdVO();
        assetDiagnosticThresholdVO.setDeviceId(deviceId);
        assetDiagnosticThresholdVO.setIpaddress(ipAddress);
        // 查询该设备的阈值
        List<AssetDiagnosticThresholdVO> assetDiagnosticThresholdVOS = assetDiagnosticThresholdDao.queryAll(assetDiagnosticThresholdVO);
        return ApiResult.success(assetDiagnosticThresholdVOS);
    }
}
