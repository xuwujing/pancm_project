package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情图表返回值
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/12
 */
@Data
public class PoManagerChartVO implements Serializable {

	/**
	 * 今日新增舆情数量
	 */
	private Integer todayAdd;

	/**
	 * 已结案总数
	 */
	private Integer dealCount;

	/**
	 * 最近30条数据
	 */
	private List<PoManagerRepVO> recentData;


	/**
	 * 当前最新数据
	 */
	private List<PoManagerRepVO> currentData;
}
