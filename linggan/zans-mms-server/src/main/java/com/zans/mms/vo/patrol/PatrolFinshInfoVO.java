package com.zans.mms.vo.patrol;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/26
 */
@Data
public class PatrolFinshInfoVO {

	/**
	 * 巡检任务id
	 */
	private Long patrolTaskId;


	/**
	 * 总数
	 */
	private Integer pointCount;

	/**
	 * 完成数量
	 */
	private Integer finishedCount;

	/**
	 * 完成率
	 */
	private BigDecimal finishedRate;

}
