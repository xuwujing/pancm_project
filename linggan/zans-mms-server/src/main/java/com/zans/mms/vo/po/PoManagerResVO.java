package com.zans.mms.vo.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情投诉返回值实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/27
 */
@Data
public class PoManagerResVO implements Serializable {
	/**
	 * 主键 自增
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	/**
	 * 辖区
	 */
	@Column(name = "area_id")
	private Integer areaId;

	/**
	 * 路口
	 */
	@Column(name = "intersection")
	private String intersection;

	/**
	 * 故障时间
	 */
	@Column(name="breakdown_time")
	private String breakdownTime;

	/**
	 * 舆情事件
	 */
	@Column(name="po_event")
	private Integer poEvent;

	/**
	 * 事件来源
	 */
	@Column(name="event_source")
	private Integer eventSource;

	/**
	 * 舆情事件类型
	 */
	@Column(name="po_type")
	private Integer poType;

	/**
	 * 原因
	 */
	@Column(name="reason")
	private Integer reason;

	/**
	 * 点位id
	 */
	@Column(name="point_id")
	private Long pointId;

	private String  pointName;

	/**
	 * 解决方案
	 */
	@Column(name="solution")
	private String solution;

	/**
	 * 解决时间
	 */
	@Column(name="solution_time")
	private String solutionTime;

	/**
	 * 问题描述
	 */
	@Column(name="problem_description")
	private String problemDescription;

	/**
	 * 创建人
	 */
	@Column(name="creator")
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
	 * 设备类型
	 */
	private String deviceType;

	private List<String> pointNameList;
}
