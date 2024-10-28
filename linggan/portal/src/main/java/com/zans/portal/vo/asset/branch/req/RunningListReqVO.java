package com.zans.portal.vo.asset.branch.req;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("运行统计请求实体")
@Data
public class RunningListReqVO extends BasePage {

    @ApiModelProperty(name = "statisticsTime", value = "统计时间")
    private String statisticsTime;
}