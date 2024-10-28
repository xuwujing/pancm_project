package com.zans.mms.vo.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:民意平台接受数据实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/9
 */
@Data
public class PoRemoteDataVO implements Serializable {

	/**
	 * 民意平台自增主键
	 */
	private Long enid;

	/**
	 * 案件编号
	 */
	private String ajbh;

	/**
	 * 申请时间
	 */
	private String sqsj;


	/**
	 * 一级来文分类
	 */
	private String lwfl;

	/**
	 * 二级来文分类
	 */
	private String lwfl2;

	/**
	 * 当事人姓名
	 */
	private String dsrxm;

	/**
	 * 当事人电话
	 */
	private String dsrdh;

	/**
	 * 行政区划
	 */
	private String xzqh;

	/**
	 * 一级问题分类
	 */
	private String wtfl;

	/**
	 * 二级问题分类
	 */
	private String wtfl1;

	/**
	 * 问题分类描述
	 */
	private String wtfl2;

	/**
	 * 归档状态
	 */
	private Integer gdzt;

	/**
	 * 责任单位
	 */
	private String dutyDept;

	/**
	 * 来文分类名称
	 */
	private String lwflName;


	/**
	 * 来文分类名称
	 */
	private String tName;


	/**
	 * 问题内容 故障描述
	 */
	private String wtnr;

	/**
	 * 办理组织名称
	 */
	private String orgName;

}
