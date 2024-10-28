package com.zans.mms.vo.alert;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AlertRuleRespVO {


    @ApiModelProperty(name = "strategyName",value = "告警策略名称")
    private String strategyName;

    @ApiModelProperty(name = "strategyDesc",value = "告警策略描述")
    private String strategyDesc;

    @ApiModelProperty(name = "typeName",value = "告警类型名称")
    private String typeName;

    @ApiModelProperty(name = "typeId",value = "告警类型ID")
    private String typeId;

    @ApiModelProperty(name = "indexName",value = "指标名称")
    private String indexName;

    @ApiModelProperty(name = "ruleStatus",value = "状态")
    private Integer ruleStatus;

    @ApiModelProperty(name = "createUser",value = "创建人")
    private String createUser;

    @ApiModelProperty(name = "createTime",value = "创建时间")
    private String createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
