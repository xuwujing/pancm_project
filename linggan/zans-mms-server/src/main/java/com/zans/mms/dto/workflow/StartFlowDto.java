package com.zans.mms.dto.workflow;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:发起流程实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/8
 */
@Data
public class StartFlowDto implements Serializable {

	/**
	 * 业务主键id
	 */
	private String id;


	/**
	 * 条件列表
	 */
	private List<ConditionDto> conditionDtoList;
}
