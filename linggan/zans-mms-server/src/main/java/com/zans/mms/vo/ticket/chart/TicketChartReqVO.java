package com.zans.mms.vo.ticket.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单报表参数接受实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/14
 */
@Data
public class TicketChartReqVO implements Serializable {

	/**
	 * 开始时间
	 */
	private String startTime;

	/**
	 * 结束时间
	 */
	private String endTime;
}
