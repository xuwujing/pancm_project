package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:服务质量排名
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
@Data
public class QualityRankingChartVO implements Serializable {

	/**
	 * 服务商名称
	 */
	private String orgName;


	/**
	 * 片区名称
	 */
	private String areaName;


	/**
	 * 当月得分
	 */
	private String score;

	/**
	 * 当月扣分
	 */
	private String deductPoints;

	private Long id;

}
