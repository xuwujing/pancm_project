package com.zans.mms.dto.workflow;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:业务状态信息实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/12
 */
@Data
public class BusinessStatusInfoDto implements Serializable {

	/**
	 * 流程文件id
	 */
	private String workflowId;

	/**
	 * 流程状态名称
	 */
	private String businessStatusName;

	/**
	 * 流程状态编码
	 */
	private String businessStatusCode;

	/**
	 * 角色名称
	 */
	private String roleName;
}
