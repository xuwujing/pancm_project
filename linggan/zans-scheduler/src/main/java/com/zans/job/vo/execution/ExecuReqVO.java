package com.zans.job.vo.execution;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ExecuReqVO extends BasePage {

    @ApiModelProperty(name = "job_id", value = "job id")
    @JSONField(name = "job_id")
    private Integer jobId;

    @ApiModelProperty(name = "data", value = "关键字查询")
    @JSONField(name = "data")
    private String data;

    @ApiModelProperty(name = "job_status", value = "执行结果")
    @JSONField(name = "job_status")
    private Integer jobStatus;

    @ApiModelProperty(name = "date", value = "调度时间")
    @JSONField(name = "date")
    private String date;

}
