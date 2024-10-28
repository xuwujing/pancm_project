package com.zans.mms.dto.workflow;

import lombok.Data;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:任务信息返回实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/22
 */
@Data
public class TaskRepDto {

	/**
	 * 任务id
	 */
	private String taskId;


	/**
	 * 任务描述
	 */
	private String description;

	/**
	 * 流程实例id
	 */
	private String processInstanceId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 上一步任务名称
	 */
	private String beforeTaskName;


	/**
	 * 审批人
	 */
	private String assignee;


	/**
	 * 业务状态码
	 */
	private String businessStatusCode;

	/**
	 * 流程文件id 派工单 dispatch /验收单 acceptance
	 */
	private String workflowId;


}
