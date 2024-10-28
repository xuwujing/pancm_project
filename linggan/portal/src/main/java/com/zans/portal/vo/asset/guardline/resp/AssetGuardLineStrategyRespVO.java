package com.zans.portal.vo.asset.guardline.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/14
 */
@Data
public class AssetGuardLineStrategyRespVO implements Serializable {

	/**
	 * 策略id
	 */
	private Long id;


	/**
	 * 规则id
	 */
	private Long ruleId;

	/**
	 * 规则名称
	 */
	private String ruleName;

	/**
	 * 策略组名称
	 */
	private String groupName;

	/**
	 * 告警等级
	 */
	private Integer alertLevel;


	/**
	 * 扫描间隔
	 */
	private Integer alertInterval;

	/**
	 * 告警阈值
	 */
	private String alertThreshold;

	/**
	 * 是否启用
	 */
	private Boolean strategyStatus;

	/**
	 * 重点资产分组id
	 */
	private Long guardId;


	/**
	 * 告警阈值状态
	 */
	private Integer alertThresholdStatus;

	/**
	 * 告警阈值描述
	 */
	private String alertThresholdDesc;
}
