package com.zans.mms.vo.po;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情报表
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/26
 */
@Data
public class PoManagerCharVO implements Serializable {

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
	 * 辖区投诉量列表
	 */
	private List<Map<String,Integer>> mapList;
}
