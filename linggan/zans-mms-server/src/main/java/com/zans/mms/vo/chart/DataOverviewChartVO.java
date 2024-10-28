package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:运维概览数据
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
@Data
public class DataOverviewChartVO implements Serializable {

	/**
	 * 环节名称
	 */
	private String linkName;

	/**
	 * 当天
	 */
	private Integer day;

	/**
	 * 本周
	 */
	private Integer week;

	/**
	 * 本月
	 */
	private Integer month;
}
