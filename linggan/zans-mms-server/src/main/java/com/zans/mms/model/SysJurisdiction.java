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
 * @Description:权限管理
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Data
@Table(name = "`sys_jurisdiction`")
@ToString
public class SysJurisdiction implements Serializable {

	/**
	 * 主键 自增
	 */
	@Id
	private String id;



	/**
	 * 权限组名称
	 */
	@Column(name = "`name`")
	private String name;


	/**
	 * 权限组描述
	 */
	@Column(name = "remark")
	private String  remark;

	@Column(name="creator")
	private String creator;


}
