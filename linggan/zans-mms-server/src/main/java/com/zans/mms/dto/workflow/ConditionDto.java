package com.zans.mms.dto.workflow;

import lombok.Data;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:条件实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/8
 */
@Data
public class ConditionDto {

	/**
	 * 键值
	 */
	private String key;


	/**
	 * String值
	 */
	private String value;

	/**
	 * double值
	 */
	private Double doubleValue;
}
