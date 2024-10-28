package com.zans.mms.vo.po;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情统计表格返回值
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/26
 */
@Data
public class PoManagerCharRepVO implements Serializable {

	List<PoManagerDataVO> poManagerDataVOList;

	/**
	 * 故障总数有
	 */
	private Integer total;

	/**
	 * 主城区投诉
	 */
	private Integer mainCityCount;

	/**
	 * 新城区投诉
	 */
	private Integer remoteCityCount;

	/**
	 * 日期
	 */
	private String currentDate;

	/**
	 * 星期几
	 */
	private String currentWeekDay;

	/**
	 * 中心城区数据
	 */
	private List<PoManagerDataVO> centerCityData;

	/**
	 * 远城区数据
	 */
	private List<PoManagerDataVO> remoteCityData;
}
