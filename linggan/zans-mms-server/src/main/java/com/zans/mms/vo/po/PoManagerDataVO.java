package com.zans.mms.vo.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单统计
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/26
 */
@Data
public class PoManagerDataVO implements Serializable {

	/**
	 * 辖区名称
	 */
	private String areaName;

	/**
	 * 统计总数
	 */
	private Integer totalCount;

	/**
	 * 结案数/完工数
	 */
	private Integer completeCount;

	/**
	 * 超时数
	 */
	private Integer timeOutCount;


}
