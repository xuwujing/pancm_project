package com.zans.mms.vo.asset.diagnosis;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author beixing
 * @Title: (AssetDiagnosisInfo)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 14:55:50
 */
@ApiModel(value = "AssetDiagnosisInfoRespVO", description = "")
@Data
public class AssetDiagnosisInfoRespVO  implements Serializable {
    private static final long serialVersionUID = 430133460045331065L;


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
    /**
     * 诊断结果 1 正常 2 异常
     */
    @ApiModelProperty(value = "诊断结果 1 正常 2 异常")
    private Integer diagnosisResult;
    /**
     * 图片故障类型集合，用逗号隔开
     */
    @ApiModelProperty(value = "图片故障类型集合，用逗号隔开")
    private String faultTypes;
    /**
     * 图片故障类型名称集合，用逗号隔开
     */
    @ApiModelProperty(value = "图片故障类型名称集合，用逗号隔开")
    private String faultTypesName;
    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String imgUrl;

    @ApiModelProperty(value = "二维码图片路径")
    private String qrCodeImgUrl;
    /**
     * 诊断时间
     */
    @ApiModelProperty(value = "诊断时间")
    private String diagnosisTime;

    private String pointName;

    private String pointCode;

    private String ipAddr;

    private String deviceTypeName;

    private List<AssetDiagnosisInfoExRespVO> assetDiagnosisInfoExVOS;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}