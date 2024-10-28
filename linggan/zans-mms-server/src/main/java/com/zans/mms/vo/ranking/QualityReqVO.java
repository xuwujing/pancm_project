package com.zans.mms.vo.ranking;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:质保单位新增编辑接受实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/8
 */
@Data
public class QualityReqVO implements Serializable {
	/**
	 * 服务商名称
	 */
	private String orgName;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 排名
	 */
	private String rank;


	/**
	 * 在线率
	 */
	private String onlineRate;

	private String currentDate;

	private String creator;

	private Integer type;

	private Long id;
}
