package com.zans.mms.dto.workflow;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:封装待办任务实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/20
 */
@ApiModel(value = "TaskInfoDetailDto", description = "待办任务实体")
@Data
public class TaskInfoDetailDto implements Serializable {

	/**
	 * 任务id
	 */
	private String taskId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务创建时间
	 */
	private Date createTime;

	/**
	 * 办理人
	 */
	private String assignee;

	/**
	 * 流程实例id
	 */
	private String processInstanceId;


	/**
	 * 描述
	 */
	private String description;


	/**
	 * 流程类型 派工单 dispatch  / 验收单 acceptance
	 */
	private String workflowId;


	/**
	 * 业务主键
	 */
	private String businessKey;



}
