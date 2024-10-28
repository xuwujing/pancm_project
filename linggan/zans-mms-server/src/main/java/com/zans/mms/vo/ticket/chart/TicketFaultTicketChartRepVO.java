package com.zans.mms.vo.ticket.chart;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:故障工单统计实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/14
 */
@Data
public class TicketFaultTicketChartRepVO implements Serializable {

	/**
	 * 故障工单数  工单完工数 一周内完成
	 */
	private List<String> dimensions;

	/**
	 * 数据
	 */
	private List<FaultTicketRespVO> source;

	/**
	 * 数据格式2
	 */
	private List<List<Object>> sourceData;


}
