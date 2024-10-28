package com.zans.mms.vo.ticket.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:单个单位故障工单
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/14
 */
@Data
public class FaultTicketRespVO implements Serializable {

	/**
	 * 运维单位名称
	 */
	private String product;

	/**
	 * 第一个指标 工单总数
	 */
	private Integer total;

	/**
	 * 第二个指标 工单完工数
	 */
	private Integer complete;

	/**
	 * 第三个指标 一周内完成
	 */
	private Integer completeInWeek;
}
