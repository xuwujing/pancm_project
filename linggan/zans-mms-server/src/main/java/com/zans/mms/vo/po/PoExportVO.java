package com.zans.mms.vo.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Data
public class PoExportVO implements Serializable {


	/**
	 * 主键 自增
	 */
	private Long id;

	/**
	 * 舆情编号
	 */
	private String poCode;


	/**
	 * 辖区
	 */

	private String areaId;

	/**
	 * 路口
	 */
	private String intersection;

	/**
	 * 故障时间
	 */
	private String breakdownTime;

	/**
	 * 舆情事件
	 */
	@Column(name="po_event")
	private String poEvent;

	/**
	 * 事件来源
	 */
	private String eventSource;

	/**
	 * 舆情事件类型
	 */
	private String poType;

	/**
	 * 原因
	 */
	private String reason;

	/**
	 * 解决方案
	 */
	private String solution;

	/**
	 * 解决时间
	 */
	private String solutionTime;

	/**
	 * 问题描述
	 */
	private String problemDescription;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 重复标记
	 */
	private Long repeatMark;


	/**
	 * 故障现象
	 */
	private String faultPhenomenon;



}
