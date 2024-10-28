package com.zans.mms.dto.workflow;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:完成任务参数接收实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/20
 */
@ApiModel(value = "completeTaskDto", description = "完成任务参数接收实体")
@Data
public class CirculationTaskDto implements Serializable {

	/**
	 * 评论
	 */
	private String msg;


	/**
	 * 待办任务id
	 */
	private String taskId;


	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 是否同意 1为同意 0为驳回
	 */
	private String isAgree;

	/**
	 * 流程文件id
	 */
	private String workflowId;
}
