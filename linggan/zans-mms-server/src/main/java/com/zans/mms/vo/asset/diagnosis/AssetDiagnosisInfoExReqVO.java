package com.zans.mms.vo.asset.diagnosis;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author beixing
 * @Title: (AssetDiagnosisInfoEx)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 14:43:12
 */
@ApiModel(value = "AssetDiagnosisInfoExReqVO", description = "")
@Data
public class AssetDiagnosisInfoExReqVO implements Serializable {
    private static final long serialVersionUID = -99470260273675263L;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String assetCode;
    /**
     * 追踪标记
     */
    @ApiModelProperty(value = "追踪标记")
    private String traceId;

    private List<AssetDiagnosisInfoExVO> assetDiagnosisInfoExVOS;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
