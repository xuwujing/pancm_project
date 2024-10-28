package com.zans.portal.vo.log;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class EsPortalLogSearchVO extends BasePage {

    @ApiModelProperty(name = "traceId",value = "追踪ID")
    private Long traceId;

    @ApiModelProperty(name = "startTime",value = "查询开始时间")
    private String startTime;

    @ApiModelProperty(name = "endTime",value = "查询结束时间")
    private String endTime;

    @ApiModelProperty(name = "sort",value = "排序方式 1:正序,0:倒序")
    private int sort;

}
