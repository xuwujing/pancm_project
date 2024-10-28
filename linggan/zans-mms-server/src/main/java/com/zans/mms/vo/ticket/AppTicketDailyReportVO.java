package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单日报信息实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/29
 */
@Data
public class AppTicketDailyReportVO implements Serializable {

	/**
	 * 故障工单个数
	 */
	private Integer breakdownCount;

	/**
	 * 故障工单完工数量
	 */
	private Integer breakdownCompleteCount;

	/**
	 * 派工单金额
	 */
	private String dispatchMoney;

	/**
	 * 派工单个数
	 */
	private Integer dispatchCount;

	/**
	 * 待审批派工单工单数
	 */
	private Integer dispatchTicketCount;

	/**
	 * 验收单金额
	 */
	private String acceptanceMoney;

	/**
	 * 验收单个数
	 */
	private Integer acceptanceCount;

	/**
	 * 待审批验收单工单数
	 */
	private Integer acceptanceTicketCount;
}
