package com.zans.mms.vo.alert;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AlertIndexSearchVO extends BasePage {

    @ApiModelProperty(name = "typeName",value = "告警类型名称")
    private String typeName;

    @ApiModelProperty(name = "typeId",value = "告警类型ID")
    private String typeId;

    @ApiModelProperty(name = "indexName",value = "指标名称")
    private String indexName;



}
