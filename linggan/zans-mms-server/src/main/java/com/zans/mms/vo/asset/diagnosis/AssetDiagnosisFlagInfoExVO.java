package com.zans.mms.vo.asset.diagnosis;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.zans.base.vo.BasePage;
import lombok.NoArgsConstructor;

/**
 * @author beixing
 * @Title: (AssetDiagnosisFlagInfoEx)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-24 16:01:59
 */
@ApiModel(value = "AssetDiagnosisFlagInfoEx", description = "")
@Data
@NoArgsConstructor
public class AssetDiagnosisFlagInfoExVO extends BasePage implements Serializable {
    private static final long serialVersionUID = 205846346484740799L;
    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id")
    private Long id;
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
     * 图片故障类型
     */
    @ApiModelProperty(value = "图片故障类型")
    private Integer faultType;
    /**
     * 诊断类型结果 0 未检测 1 正常 2 异常
     */
    @ApiModelProperty(value = "诊断类型结果 0 未检测 1 正常 2 异常")
    private Integer faultTypeResult;
    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String imgUrl;
    /**
     * 诊断时间
     */
    @ApiModelProperty(value = "诊断时间")
    private String diagnosisTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    private String timeBegin;

    private String timeEnd;

    public AssetDiagnosisFlagInfoExVO(String timeBegin, String timeEnd) {
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
    }
}