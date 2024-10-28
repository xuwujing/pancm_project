package com.zans.mms.vo.ticket.chart;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单审批返回值
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/25
 */
@Data
public class TicketApprovedChartRepVO implements Serializable {

	/**
	 * 数据格式1
	 */
	private List<ApprovedTicketRepVO> source;

	/**
	 * 数据格式2
	 */
	private List<List<Object>> sourceData;
}
