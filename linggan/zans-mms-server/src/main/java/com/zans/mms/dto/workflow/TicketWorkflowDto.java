package com.zans.mms.dto.workflow;

import com.zans.mms.model.Ticket;
import lombok.Data;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/26
 */
@Data
public class TicketWorkflowDto extends Ticket {

	/**
	 * 流程实例id
	 */
	private String processInstanceId;


	/**
	 * 任务id
	 */
	private String taskId;


	/**
	 * 任务名称
	 */
	private String taskName;


	/**
	 * 任务描述
	 */
	private String description;

	/**
	 * 流程文件id  派工单 dispatch / 派工单 acceptance
	 */
	private String workflowId;

	/**
	 * 是否合并工单
	 */
	private Integer isMerge;



}
