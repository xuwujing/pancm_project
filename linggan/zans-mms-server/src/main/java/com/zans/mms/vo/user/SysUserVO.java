package com.zans.mms.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:用户信息
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/6
 */
@Data
public class SysUserVO implements Serializable {

	private String userName;


	private String nickName;

	private String orgName;
}
