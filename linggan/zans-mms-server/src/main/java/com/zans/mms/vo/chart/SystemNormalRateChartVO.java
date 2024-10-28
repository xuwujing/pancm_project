package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:系统正常运行率
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
@Data
public class SystemNormalRateChartVO implements Serializable {

	/**
	 * 正常率
	 */
	private String normalRate;

	/**
	 * 正常总数
	 */
	private Integer normalTotal;

	/**
	 * 在线数
	 */
	private Integer onlineNum;

	/**
	 * 故障数
	 */
	private Integer faultNum;

	/**
	 * 在线率数值
	 */
	private BigDecimal normalRateNum;
}
