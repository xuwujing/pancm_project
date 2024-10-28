package com.zans.portal.vo.alert;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AlertRuleSearchVO extends BasePage {



    @ApiModelProperty(name = "strategyName",value = "告警策略名称")
    private String strategyName;

    @ApiModelProperty(name = "typeName",value = "告警类型名称")
    private String typeName;

    @ApiModelProperty(name = "typeId",value = "告警类型ID")
    private String typeId;

    @ApiModelProperty(name = "indexName",value = "指标名称")
    private String indexName;

    @ApiModelProperty(name = "ruleStatus",value = "状态")
    private Integer ruleStatus;

    @ApiModelProperty(name = "startTime",value = "查询开始时间")
    private String startTime;

    @ApiModelProperty(name = "endTime",value = "查询结束时间")
    private String endTime;


}
