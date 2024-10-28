package com.zans.mms.vo.asset.diagnosis;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author beixing
 * @Title: (AssetDiagnosticThreshold)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-26 16:52:28
 */
@ApiModel(value = "AssetDiagnosticThreshold", description = "")
@Data
public class AssetDiagnosticThresholdReqVO  implements Serializable {
    private static final long serialVersionUID = 424386630758945015L;


    private String deviceId;
    @ApiModelProperty(value = "${column.comment}")
    private String ipAddress;


    private List<AssetThresholdVO> assetThresholdVOS;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
