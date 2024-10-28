package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单小程序图表统计接受实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/19
 */
@Data
public class AppTicketCharReqVO implements Serializable {

	/**
	 * 类型
	 */
	private Integer type;

	/**
	 * 起始时间
	 */
	private String startTime;


	/**
	 * 结束时间
	 */
	private String endTime;


	/**
	 * 组织id
	 */
	private String orgId;


	/**
	 * 相比上次的开始时间
	 */
	private String beforeStartTime;

	/**
	 * 相比上次的结束时间
	 */
	private String beforeEndTime;
}
