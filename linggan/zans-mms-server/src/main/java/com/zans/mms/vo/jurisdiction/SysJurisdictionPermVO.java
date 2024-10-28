package com.zans.mms.vo.jurisdiction;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:权限列表返回实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/19
 */
@Data
public class SysJurisdictionPermVO implements Serializable {

	private String id;


	private String jurisdictionId;

	private Integer permId;


	private String dataPerm;


	private String dataPermDesc;
}
