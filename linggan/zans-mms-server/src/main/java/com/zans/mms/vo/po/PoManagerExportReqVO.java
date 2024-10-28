package com.zans.mms.vo.po;

import com.zans.base.vo.BasePage;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情投诉请求实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Data
public class PoManagerExportReqVO  implements Serializable {

	/**
	 * 主键 自增
	 */
	private Long id;

	/**
	 * 舆情编号
	 */
	private String poCode;


	private Integer areaId;


	/**
	 * 辖区
	 */
	private List<Integer> areaIdList;

	/**
	 * 路口
	 */
	private String intersection;

	/**
	 * 故障开始时间
	 */
	private Date startBreakdownTime;


	/**
	 * 故障开始时间
	 */
	private Date endBreakdownTime;


	/**
	 * 舆情事件
	 */
	private Integer poEvent;

	/**
	 * 事件来源
	 */
	private Integer eventSource;

	/**
	 * 舆情事件类型
	 */
	private Integer poType;

	/**
	 * 原因
	 */
	private Integer reason;


	/**
	 * 故障原因多选
	 */
	private List<Integer> reasonList;

	/**
	 * 解决方案
	 */
	private String solution;

	/**
	 * 解决时间开始
	 */
	private Date startSolutionTime;

	/**
	 * 解决时间结束
	 */
	private Date endSolutionTime;

	/**
	 * 问题描述
	 */
	private String problemDescription;

	/**
	 * 创建人
	 */
	private String creator;


	/**
	 * 关键字
	 */
	private String keyword;


	private List<String> keywordList;

	/**
	 * 维修状态
	 */
	private List<Integer> repairStatusList;

	private String repairStatus;



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
	 * 问题编号
	 */
	private String originalProblemId;


	/**
	 * 负责人
	 */
	private List<String> dutyContactList;

	/**
	 * 是否紧急
	 */
	private Integer isImmidiately;

	/**
	 * 数据权限
	 */
	private Integer dataPermissions;

	/**
	 * 故障现象
	 */
	private Integer faultPhenomenon;

	private List<Integer> faultPhenomenonList;

	/**
	 * 是否显示重复舆情
	 */
	private Integer showRepeat;


}
