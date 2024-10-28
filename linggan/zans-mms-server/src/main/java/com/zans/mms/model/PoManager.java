package com.zans.mms.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情投诉管理
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Data
@Table(name = "po_manager")
@ToString
public class PoManager implements Serializable {

	/**
	 * 主键 自增
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;



	/**
	 * 舆情编号
	 */
	@Column(name = "po_code")
	private String poCode;

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
	 * 修复状态
	 */
	@Column(name="repair_status")
	private Integer repairStatus;

	/**
	 * 数据来源平台
	 */
	@Column(name="source_platform")
	private Integer sourcePlatform;

	/**
	 * 负责人用户名
	 */
	@Column(name="duty_contact")
	private String dutyContact;

	/**
	 * 联系人
	 */
	@Column(name="contact")
	private String contact;

	/**
	 * 联系人电话
	 */
	@Column(name="contact_phone")
	private String contactPhone;


	/**
	 * 修复前附件id
	 */
	@Column(name="before_adjunct_id")
	private String beforeAdjunctId;

	/**
	 * 修复后附件id
	 */
	@Column(name="after_adjunct_id")
	private String afterAdjunctId;


	/**
	 * 确认人描述
	 */
	@Column(name="confirmer_description")
	private String confirmerDescription;


	/**
	 * 结案人描述
	 */
	@Column(name="closing_person_description")
	private String closingPersonDescription;


	@Column(name="original_problem_id")
	private String originalProblemId;


	@Column(name="is_immidiately")
	private Integer isImmidiately;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;


	/**
	 * 维修人员备注
	 */
	@Column(name="remark")
	private String remark;


	/**
	 * 本舆情的维修人员账号 如有多个 用，分割
	 */
	@Column(name="repair_person")
	private String repairPerson;

	@Column(name="repeat_mark")
	private Long repeatMark;

	@Column(name="device_type")
	private String deviceType;

	//推送人列表
	@Transient
	private List<String> repairPersonList;

	/**
	 * 是否派单超时 同步时间与实际时间相差大于30分钟
	 */
	@Transient
	private Integer isDispatchTimeOut;


	/**
	 * 是否维修超时
	 */
	@Transient
	private Integer isMaintainTimeOut;


	/**
	 * 派工时间
	 */
	private String dispatchTime;

	/**
	 * 维修时间
	 */
	@Column(name="maintain_time")
	private String maintainTime;


	/**
	 * 同步时间
	 */
	private String synchronizationTime;

	@Column(name="fault_phenomenon")
	private Integer faultPhenomenon;
}
