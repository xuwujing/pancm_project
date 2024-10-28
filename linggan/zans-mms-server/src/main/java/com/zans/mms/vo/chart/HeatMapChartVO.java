package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:热力图返回值实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/24
 */
@Data
public class HeatMapChartVO implements Serializable {

	/**
	 * 纬度
	 */
	private String lat;

	/**
	 * 经度
	 */
	private String lng;

	/**
	 * 半径
	 */
	private Integer count;

	/**
	 * 点位名称
	 */
	private String pointName;

	/**
	 * 负责单位名称
	 */
	private String orgName;

}
