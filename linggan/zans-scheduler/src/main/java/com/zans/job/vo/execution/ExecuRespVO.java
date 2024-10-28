package com.zans.job.vo.execution;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class ExecuRespVO {

    @ApiModelProperty(name = "exec_id", value = "id")
    @JSONField(name = "exec_id")
    private Integer execId;

    @ApiModelProperty(name = "job_id", value = "job id")
    @JSONField(name = "job_id")
    private Integer jobId;

    @ApiModelProperty(name = "record_count", value = "待处理记录数")
    @JSONField(name = "record_count")
    private Integer recordCount;

    @ApiModelProperty(name = "sharding_num", value = "任务分片数")
    @JSONField(name = "sharding_num")
    private Integer shardingNum;

    @ApiModelProperty(name = "finished_sharding", value = "完成的分片数")
    @JSONField(name = "finished_sharding")
    private String finishedSharding;

    @ApiModelProperty(name = "start_time", value = "任务开始时间")
    @JSONField(name = "start_time", format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(name = "finish_time", value = "任务结束时间")
    @JSONField(name = "finish_time", format = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    @ApiModelProperty(name = "job_status", value = "执行状态")
    @JSONField(name = "job_status")
    private String jobStatus;

    @ApiModelProperty(name = "job_status_name", value = "执行状态显示名称")
    @JSONField(name = "job_status_name")
    private String jobStatusName;

    @ApiModelProperty(name = "execute_time", value = "执行时间")
    @JSONField(name = "execute_time")
    private String executeTime;

}
