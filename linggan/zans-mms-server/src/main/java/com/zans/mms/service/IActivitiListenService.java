package com.zans.mms.service;

import org.activiti.engine.delegate.DelegateExecution;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:监听接口
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/26
 */
public interface IActivitiListenService {

	String receiver(DelegateExecution execution);


	String supervisor(DelegateExecution delegateExecution);


	String clerk(DelegateExecution execution);


	/**
	 * 固定角色选择器
	 * @param roleName
	 * @return
	 */
	String roleSelector(String roleName);

	/**
	 * 固定部门选择器
	 * @param departmentName
	 * @return
	 */
	String departmentSelector(String departmentName);



}
