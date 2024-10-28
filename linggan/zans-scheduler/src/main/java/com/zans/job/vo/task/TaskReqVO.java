package com.zans.job.vo.task;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class TaskReqVO extends BasePage {

    @ApiModelProperty(name = "exec_id", value = "exec_id")
    @JSONField(name = "exec_id")
    private Integer execId;

    @ApiModelProperty(name = "node_id", value = "node_id")
    @JSONField(name = "node_id")
    private String nodeId;

    @ApiModelProperty(name = "task_id", value = "子任务id")
    @JSONField(name = "task_id")
    private Integer taskId;

    @ApiModelProperty(name = "start_time", value = "任务开始时间")
    @JSONField(name = "start_time")
    private String startTime;

    @ApiModelProperty(name = "alloc_status", value = "分配状态")
    @JSONField(name = "alloc_status")
    private Integer allocStatus;

    @ApiModelProperty(name = "task_status", value = "执行状态")
    @JSONField(name = "task_status")
    private Integer taskStatus;

    @ApiModelProperty(name = "type", value = "1:当前执行；2：历史记录  不传查所有")
    @JSONField(name = "type")
    private Integer type;

}
