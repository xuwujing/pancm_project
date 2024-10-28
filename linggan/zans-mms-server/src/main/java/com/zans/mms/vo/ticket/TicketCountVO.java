package com.zans.mms.vo.ticket;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "ticketCountVO", description = "工单统计实体")
@Data
public class TicketCountVO {

	/**
	 * 组织名称
	 */
	private String orgName;

	/**
	 * 工单数量总计
	 */
	private Integer totalCount;

	/**
	 * 近一个月工单数量统计
	 */
	private Integer monthCount;

	/**
	 * 工单完成数量统计
	 */
	private Integer completeCount;


	/**
	 * 抵达超时统计
	 */
	private Integer arriveTimeCount;

	/**
	 * 完成超时统计
	 */
	private Integer completeTimeCount;


	/**
	 * 未完成统计
	 */
	private Integer notCompleteCount;

	/**
	 * 三天未完成统计
	 */

	private Integer overTimeComplete;
}
