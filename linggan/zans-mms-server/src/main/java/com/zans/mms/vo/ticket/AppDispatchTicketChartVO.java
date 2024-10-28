package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:小程序故障工单统计
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/22
 */
@Data
public class AppDispatchTicketChartVO implements Serializable {

	private String areaName;

	private String total;

	private String totalMoney;

	private String complete;

	private String completeMoney;

	private String min;

	private String minMoney;

	private String max;

	private String maxMoney;


}
