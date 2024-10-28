package com.zans.mms.vo.ticket.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:故障工单表格返回值
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/14
 */
@Data
public class FaultTicketTableRepVO implements Serializable {
	private String orgName;

	private Integer total;

	private Integer complete;

	private Integer completeInWeek;

	/**
	 * 比率
	 */
	private String rate;




}
