package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:延迟数据返回
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/11
 */
@Data
public class DelayRepVO implements Serializable {

	/**
	 * 辖区名称
	 */
	private String areaName;

	/**
	 * 丢包率
	 */
	private BigDecimal rate;


	/**
	 * 延时
	 */
	private String delayed;
}
