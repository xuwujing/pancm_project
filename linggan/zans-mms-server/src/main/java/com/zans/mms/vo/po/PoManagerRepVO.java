package com.zans.mms.vo.po;

import com.zans.mms.model.BaseVfs;
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
 * @Description:舆情返回值实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/4
 */
@Data
public class PoManagerRepVO implements Serializable {


	/**
	 * 主键 自增
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	/**
	 * 舆情编号
	 */
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
	 * 负责人昵称
	 */
	private String dutyContactName;

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


	@Column(name="是否紧急")
	private Integer isImmidiately;


	private String deviceType;
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 重复标记id
	 */
	private String repeatMark;

	/**
	 * 重复标记code
	 */
	private String repeatCode;

	/**
	 * 维修前附件图片列表
	 */
	private List<BaseVfs> beforeBaseVfsList;

	/**
	 * 维修后附件图片列表
	 */
	private List<BaseVfs> afterBaseVfsList;

	/**
	 * 是否派单超时 同步时间与实际时间相差大于30分钟
	 */
	private Integer isDispatchTimeOut;


	/**
	 * 是否维修超时
	 */
	private Integer isMaintainTimeOut;

	private Integer isPid;

	/**
	 * 故障现象
	 */
	private Integer faultPhenomenon;


}
