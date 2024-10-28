package com.zans.mms.vo.qrcode;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
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
@ApiModel(value = "QrCodeReqVO", description = "")
@Data
public class QrCodeReqVO extends BasePage implements Serializable {
    private static final long serialVersionUID = -99470260273675263L;


    /**
     * 追踪标记
     */
    @ApiModelProperty(value = "项目ID")
    private String projectId;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "手机号")
    private String phone;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
