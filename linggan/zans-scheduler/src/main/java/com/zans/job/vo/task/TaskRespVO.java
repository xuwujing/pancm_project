package com.zans.job.vo.task;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.job.model.OpsJob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@ApiModel
@Data
public class TaskRespVO {

    @ApiModelProperty(name = "task_id", value = "task ID")
    @JSONField(name = "task_id")
    private Integer taskId;

    @ApiModelProperty(name = "exec_id", value = "exec_id")
    @JSONField(name = "exec_id")
    private Integer execId;

    @ApiModelProperty(name = "job_id", value = "opt_job.id")
    @JSONField(name = "job_id")
    private Integer jobId;

    @ApiModelProperty(name = "task_name", value = "任务名称")
    @JSONField(name = "task_name")
    private String taskName;

    @ApiModelProperty(name = "params", value = "任务参数")
    @JSONField(name = "params")
    private String params;

    @ApiModelProperty(name = "node_id", value = "ops_node.id")
    @JSONField(name = "node_id")
    private String nodeId;

    @ApiModelProperty(name = "alloc_status", value = "分配状态")
    @JSONField(name = "alloc_status")
    private Integer allocStatus;

    @ApiModelProperty(name = "task_status", value = "执行状态")
    @JSONField(name = "task_status")
    private Integer taskStatus;

    @ApiModelProperty(name = "alloc_time", value = "分配时间")
    @JSONField(name = "alloc_time")
    private String allocTime;

    @ApiModelProperty(name = "start_time", value = "任务开始时间")
    @JSONField(name = "start_time")
    private String startTime;

    @ApiModelProperty(name = "finish_time", value = "任务结束时间")
    @JSONField(name = "finish_time")
    private String finishTime;

    @ApiModelProperty(name = "todo_count", value = "待处理数据量")
    @JSONField(name = "todo_count")
    private Integer todoCount;

    @ApiModelProperty(name = "done_count", value = "已处理数据量")
    @JSONField(name = "done_count")
    private String doneCount;

    @ApiModelProperty(name = "progress", value = "进度百分比")
    @JSONField(name = "progress")
    private Integer progress;

    @ApiModelProperty(name = "error", value = "error")
    @JSONField(name = "error")
    private String error;

    @ApiModelProperty(name = "content", value = "摘要")
    @JSONField(name = "content")
    private String content;

    @ApiModelProperty(name = "enable", value = "0, 正常； 1，删除")
    @JSONField(name = "enable")
    private Integer enable;

    @ApiModelProperty(name = "execute_time", value = "执行时间")
    @JSONField(name = "execute_time")
    private String executeTime;

}
