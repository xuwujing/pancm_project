package com.zans.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:通用标签实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/31
 */
@Data
@Table(name = "common_label")
public class CommonLabel implements Serializable {

	/**
	 * 标签id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 模块
	 */
	@Column(name="module")
	private String module;

	/**
	 * 值
	 */
	@Column(name="value")
	private String value;

	/**
	 * 创建人
	 */
	@Column(name="creator")
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(name="create_time")
	private String createTime;

	/**
	 * 修改时间
	 */
	@Column(name="update_time")
	private String updateTime;
}

