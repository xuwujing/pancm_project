package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单请求实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/24
 */
@Data
public class TicketChartReqVO implements Serializable {

	/**
	 * 设备类型列表
	 */
	private List<String> deviceTypeList;

	/**
	 * 想看的起始日期 如果不传 默认设为当前时间
	 */
	private String startDate;




}
