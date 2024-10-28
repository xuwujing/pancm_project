package com.zans.mms.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工作流任务信息实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/8
 */
@Data
@Table(name = "workflow_task_info")
public class WorkflowTaskInfo implements Serializable {

	/**
	 * 数据库主键，自增长
	 */
	@Column(name = "id")
	@ApiModelProperty(value = "数据库主键，自增长")
	private Long id;


	/**
	 * 流程实例id
	 */
	@Column(name = "process_instance_id")
	@ApiModelProperty(value = "流程实例id")
	private String processInstanceId;

	/**
	 * 业务id
	 */
	@Column(name = "business_id")
	@ApiModelProperty(value = "业务id")
	private String businessId;


	/**
	 * 流程类型
	 */
	@Column(name = "type")
	@ApiModelProperty(value = "流程类型")
	private String type;

	/**
	 * 任务id
	 */
	@Column(name = "task_id")
	@ApiModelProperty(value = "任务id")
	private String taskId;

	@Column(name = "create_time")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
}
