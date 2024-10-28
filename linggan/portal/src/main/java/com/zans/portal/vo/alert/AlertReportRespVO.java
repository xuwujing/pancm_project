package com.zans.portal.vo.alert;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AlertReportRespVO {



    @ApiModelProperty(name = "name",value = "名称")
    private String name;

    @ApiModelProperty(name = "value",value = "总数")
    private Long value;

    private Object val;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
