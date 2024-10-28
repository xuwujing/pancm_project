package com.zans.portal.vo.log;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class TaskLogSearchVO extends BasePage {

    @ApiModelProperty(name = "task_id", value = "任务ID")
    @JSONField(name = "task_id")
    private Integer taskId;

    @ApiModelProperty(name = "task_status", value = "任务状态")
    @JSONField(name = "task_status")
    private Integer taskStatus;

    @ApiModelProperty(value = "日志内容")
    private String content;
}
