package com.zans.mms.vo.ticket.chart;

import lombok.Data;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/25
 */
@Data
public class ApprovedTicketRepVO {

	/**
	 * 运维单位名称
	 */
	private String product;

	/**
	 * 第一个指标 工单总数
	 */
	private Integer total;

	/**
	 * 第二个指标 工单审批数量
	 */
	private Integer complete;

	/**
	 * 第三个指标 单笔大于3000
	 */
	private Integer min;

	/**
	 * 第四个指标 单笔大于10000
	 */
	private Integer max;
}
