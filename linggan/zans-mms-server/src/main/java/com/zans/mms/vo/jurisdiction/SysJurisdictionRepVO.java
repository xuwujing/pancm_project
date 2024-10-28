package com.zans.mms.vo.jurisdiction;

import com.zans.base.vo.BasePage;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:权限返回实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/18
 */
@Data
public class SysJurisdictionRepVO  implements Serializable {

	/**
	 * 权限组id
	 */
	private String id;


	/**
	 * 权限组名称
	 */
	private String name;

	/**
	 * 权限组描述
	 */
	private String remark;
}
