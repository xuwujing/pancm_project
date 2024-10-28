package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:通讯网络实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
@Data
public class CommunicationNetworkChartVO implements Serializable {

	/**
	 * 片区
	 */
	private String areaName;

	/**
	 * 总数
	 */
	private Integer total;

	/**
	 * 在线数
	 */
	private Integer onlineNum;

	/**
	 * 故障数
	 */
	private Integer fault;

	/**
	 * 在线率
	 */
	private String onlineRate;
}
