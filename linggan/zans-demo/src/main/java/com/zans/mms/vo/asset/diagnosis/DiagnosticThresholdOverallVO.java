package com.zans.mms.vo.asset.diagnosis;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (DiagnosticThresholdOverall)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-26 16:51:52
 */
@ApiModel(value = "DiagnosticThresholdOverall", description = "")
@Data
public class DiagnosticThresholdOverallVO  implements Serializable {
    private static final long serialVersionUID = 873534296713354304L;
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    @ApiModelProperty(value = "${column.comment}")
    private Integer faultType;
    @ApiModelProperty(value = "${column.comment}")
    private String faultTypeName;
    @ApiModelProperty(value = "${column.comment}")
    private Integer faultTypeThreshold;
    @ApiModelProperty(value = "${column.comment}")
    private String createTime;
    @ApiModelProperty(value = "${column.comment}")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
