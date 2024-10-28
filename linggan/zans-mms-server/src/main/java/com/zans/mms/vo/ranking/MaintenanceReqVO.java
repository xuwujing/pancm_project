package com.zans.mms.vo.ranking;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:维保单位请求实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/8
 */
@Data
public class MaintenanceReqVO implements Serializable {

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

	/**
	 * 数据日期所属月份
	 */
	private String currentDate;

	private String creator;

	private Long id;
}
