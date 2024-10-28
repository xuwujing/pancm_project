package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:资产在线返回值
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
@Data
public class AssetOnlineChartVO implements Serializable {

	/**
	 * 设备类型名称
	 */
	private String deviceTypeName;

	/**
	 * 设备总数
	 */
	private Integer total;

	/**
	 * 故障数量
	 */
	private Integer fault;

	/**
	 * 在线率
	 */
	private String onlineRate;

	/**
	 * 在线率数字
	 */
	private BigDecimal onlineRateNum;

	/**
	 * 设备类型
	 */
	private String deviceType;
}
