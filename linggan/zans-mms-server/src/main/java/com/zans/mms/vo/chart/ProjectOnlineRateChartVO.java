package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:项目在线率
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/27
 */
@Data
public class ProjectOnlineRateChartVO implements Serializable {

	/**
	 * 服务商名称
	 */
	private String orgName;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 排名
	 */
	private String rank;


	/**
	 * 在线率
	 */
	private String onlineRate;

	private Long id;


}
