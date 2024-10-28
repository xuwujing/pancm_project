package com.zans.mms.vo.asset.diagnosis;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (AssetDiagnosisFlagInfoHis)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-24 16:02:16
 */
@ApiModel(value = "AssetDiagnosisFlagInfoHis", description = "")
@Data
public class AssetDiagnosisFlagInfoHisVO extends BasePage implements Serializable {
    private static final long serialVersionUID = 641634296455246185L;
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
     * ip
     */
    @ApiModelProperty(value = "ip")
    private String ipaddress;
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
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
