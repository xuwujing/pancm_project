package com.zans.mms.vo.jurisdiction;

import com.zans.base.vo.BasePage;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:权限请求实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/18
 */
@Data
public class SysJurisdictionReqVO extends BasePage implements Serializable {


	/**
	 * 关键字
	 */
	private String keyword;


	/**
	 * 被克隆的权限组id
	 */
	private String cloneId;

	/**
	 * 新建的分组名称
	 */
	private String name;


	/**
	 * 新建的分组描述
	 */
	private String remark;

	/**
	 * 权限组创建人
	 */
	private String creator;


}
