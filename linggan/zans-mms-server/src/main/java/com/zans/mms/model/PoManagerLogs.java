package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情操作日志
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/4
 */
@Data
@Table(name="po_manager_logs")
public class PoManagerLogs implements Serializable {


	/**
	 * 操作人
	 */
	private String creator;

	/**
	 * 操作时间
	 */
	private String createTime;

	/**
	 * 修改时间
	 */
	private String updateTime;

	/**
	 * 状态
	 */
	private Integer poStatus;

	/**
	 * 舆情id
	 */
	private Long poId;

	/**
	 * 操作类型 状态变更：1 责任人变更：2
	 */
	private Integer opType;

	/**
	 * 操作平台  1 - PC
	 * 2 - 微信app
	 */
	private Integer opPlatform;

	/**
	 * 信息
	 */
	private String msg;
}
