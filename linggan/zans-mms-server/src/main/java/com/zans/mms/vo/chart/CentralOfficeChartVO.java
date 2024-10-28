package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:中心机房返回值
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
@Data
public class CentralOfficeChartVO implements Serializable {

	/**
	 * 机房名称
	 */
	private String machineRoomName;

	/**
	 * 湿度
	 */
	private String humidity;

	/**
	 * 温度
	 */
	private String temperature;

	/**
	 * UPS状态
	 */
	private String upsStatus;


	/**
	 * 是否水浸
	 */
	private String waterImmersion;


}

