package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:在线率返回值实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/24
 */
@Data
public class OnlineResultVO implements Serializable {

	/**
	 * 设备类型
	 */
	private String deviceType;

	/**
	 * 在线率
	 */
	private BigDecimal onlineRate;
}
