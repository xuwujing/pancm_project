package com.zans.job.vo.job;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.job.model.OpsJob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class JobRespVO {

    @ApiModelProperty(name = "job_id", value = "任务ID")
    @JSONField(name = "job_id")
    private Integer jobId;

    @ApiModelProperty(name = "job_name", value = "任务名称")
    @JSONField(name = "job_name")
    private String jobName;

    @ApiModelProperty(name = "job_type", value = "任务类型")
    @JSONField(name = "job_type")
    private String jobType;

    @ApiModelProperty(name = "priority", value = "优先级")
    @JSONField(name = "priority")
    private Integer priority;

    @ApiModelProperty(name = "sharding", value = "是否分片")
    @JSONField(name = "sharding")
    @NotNull
    private Integer sharding;

    @ApiModelProperty(name = "sharding_num", value = "分片数量")
    @JSONField(name = "sharding_num")
    private Integer shardingNum;

    @ApiModelProperty(name = "cron_expression", value = "调度规则")
    @JSONField(name = "cron_expression")
    private String cronExpression;

    @ApiModelProperty(name = "timeout", value = "超时时间")
    @JSONField(name = "timeout")
    private Integer timeout;

    @ApiModelProperty(name = "timeoutstr", value = "超时时间")
    @JSONField(name = "timeoutstr")
    private String timeoutstr;

    @ApiModelProperty(name = "run_count", value = "运行次数")
    @JSONField(name = "run_count")
    private Integer runCount;

    @ApiModelProperty(name = "new_start_time", value = "最近运行时间")
    @JSONField(name = "new_start_time")
    private String newStartTime;

    @ApiModelProperty(name = "job_status", value = "最近执行状态")
    @JSONField(name = "job_status")
    private String jobStatus;

    @ApiModelProperty(name = "enable", value = "1,启用；0，禁用；2,停止")
    @JSONField(name = "enable")
    private Integer enable;

    @ApiModelProperty(name = "remark", value = "备注")
    @JSONField(name = "remark")
    private String remark;

    public static OpsJob init(OpsJob job, JobReqVO respVO) {
        job.setJobName(respVO.getJobName());
        job.setPriority(respVO.getPriority());
        job.setSharding(respVO.getSharding());
        job.setShardingNum(respVO.getShardingNum());
        job.setCronExpression(respVO.getCronExpression());
        job.setTimeout(respVO.getTimeout());
        job.setRemark(respVO.getRemark());
        return job;
    }
}
