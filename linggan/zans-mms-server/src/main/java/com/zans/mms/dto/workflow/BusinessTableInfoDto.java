package com.zans.mms.dto.workflow;

import lombok.Data;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description: 业务表信息实例
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/6
 */
@Data
public class BusinessTableInfoDto {

	/**
	 * 业务表名
	 */
	private String businessTableName;

	/**
	 * 业务表字段
	 */
	private String businessTableStatusField;
}
