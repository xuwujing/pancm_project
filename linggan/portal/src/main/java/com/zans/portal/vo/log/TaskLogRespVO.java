package com.zans.portal.vo.log;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class TaskLogRespVO {

    private Integer id;

    @ApiModelProperty(name = "task",value = "任务ID")
    private Integer task;

    @ApiModelProperty(name = "task_name",value = "任务名称")
    @JSONField(name = "task_name")
    private String taskName;

    @ApiModelProperty(name = "task_status",value = "任务状态")
    @JSONField(name = "task_status")
    private Integer taskStatus;

    @ApiModelProperty(name = "task_status_name",value = "任务状态名称，开始/结束")
    @JSONField(name = "task_status_name")
    private String taskStatusName;

    @ApiModelProperty(name = "start_time",value = "任务开始时间")
    @JSONField(name = "start_time", format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(name = "finish_time",value = "任务结束时间")
    @JSONField(name = "finish_time", format = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    @ApiModelProperty(name = "content",value = "执行日志")
    private String content;

    @ApiModelProperty(name = "error",value = "错误日志")
    private String error;

    @ApiModelProperty(name = "content_map",value = "日志内容反序列化")
    @JSONField(name = "content_map")
    private Map<String, Object> contentMap;

    public void setStatusByMap(Map<Object, String> map) {
        Integer status = this.getTaskStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setTaskStatusName(name);
    }
}
