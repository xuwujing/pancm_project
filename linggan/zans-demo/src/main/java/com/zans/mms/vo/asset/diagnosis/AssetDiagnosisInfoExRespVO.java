package com.zans.mms.vo.asset.diagnosis;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (AssetDiagnosisInfoEx)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 14:43:12
 */
@ApiModel(value = "AssetDiagnosisInfoEx", description = "")
@Data
public class AssetDiagnosisInfoExRespVO implements Serializable {
    private static final long serialVersionUID = -99470260273675263L;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String assetCode;

    /**
     * 图片故障类型
     */
    @ApiModelProperty(value = "图片故障类型")
    private Integer faultType;
    /**
     * 诊断类型结果 0 未检测 1 正常 2 异常
     */
    @ApiModelProperty(value = "诊断类型结果 0 未检测 1 正常 2 异常")
    private Integer faultTypeResult;

    @ApiModelProperty(value = "诊断类型结果 0 未检测 1 正常 2 异常")
    private String faultTypeResultName;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
