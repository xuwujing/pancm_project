package com.zans.mms.service.impl;

import com.zans.base.exception.BusinessException;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.Ticket;
import com.zans.mms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工作流选人监听service
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/26
 */
@Service("activitiListenService")
@Slf4j
public class ActivitiListenServiceImpl implements IActivitiListenService {

	@Autowired
	ITicketService ticketService;

	@Autowired
	ISysUserService sysUserService;

	@Autowired
	IBaseOrgRoleService baseOrgRoleService;

	@Autowired
	IBaseOrgService baseOrgService;

	@Autowired
	HttpHelper httpHelper;

	@Resource
	HttpServletRequest request;


	/**
	 * 工单接收审核
	 * @param
	 * @return
	 */
	@Override
	public String receiver(DelegateExecution execution) {
		log.info("审批人名称{}");
		return "qitian";
	}

	/**
	 * 监理审核
	 * @param execution 执行器
	 * @return
	 */
	@Override
	public String supervisor(DelegateExecution execution) {
		//查询系统中角色为监理的用户名
		String roleNum=baseOrgRoleService.findRoleByName("监理单位");
		List<String> usernameList = sysUserService.findUsernameListByRoleId(roleNum);
		return StringUtils.collectionToDelimitedString(usernameList ,",");
	}



	/**
	 * 文员审批
	 * @param execution 执行器
	 * @return
	 */
	@Override
	public String clerk(DelegateExecution execution) {
		//通过执行器拿到流程实例id
		String processInstanceId = execution.getProcessInstanceId();
		//通过流程实例id 拿到业务数据
		Ticket ticket = ticketService.getByProcessInstanceId(processInstanceId);
		//拿到工单的分配单位
		String allocDepartmentNum = ticket.getAllocDepartmentNum();
		//查询单位为该分配单位且角色为内场运维的用户的用户名列表
		String roleNum=baseOrgRoleService.findRoleByName("内场运维");
		List<String> usernameList = sysUserService.findUsernameListByRoleAndMaintainNum(roleNum,allocDepartmentNum);
		return StringUtils.collectionToDelimitedString(usernameList ,",");
	}

	/**
	 * 固定角色选择器
	 * @param roleName
	 * @return
	 */
	@Override
	public String roleSelector(String roleName) {
		//查询单位为该分配单位且角色为内场运维的用户的用户名列表
		String roleNum=baseOrgRoleService.findRoleByName(roleName);
		List<String> usernameList = sysUserService.findUsernameListByRoleId(roleNum);
		return StringUtils.collectionToDelimitedString(usernameList ,",");
	}

	/**
	 * 固定部门选择器
	 * @param departmentName
	 * @return
	 */
	@Override
	public String departmentSelector(String departmentName) {
		//通过部门名称查询部门id
		String departmentNum = baseOrgService.getOrgIdByOrgName(departmentName);
		//通过部门id  查询该部门所有用户
		List<String> usernameList = sysUserService.findUsernameListByDepartmentNum(departmentNum);
		return null;
	}


	/**
	 * 通用方法  规则定义
	 * 规则： 传参 冒号前为role/dept
	 * 			 冒号后为参数列表，用逗号分隔
	 * 	示例:
	 * 		role:内场运维,外场运维
	 * 		dept:第一包，第二包，第三包
	 * 	自己部门或角色表示方式 role:self , dept:self
	 * 	起草人用户、部门或角色表示方式 role:draft,dept:draft
	 * 	传多个条件时 后面的条件作为前面传参的限制条件
	 * 	示例：
	 *      role:内场运维，dept:第一包 表示选择第一包的内场运维角色的所有用户
	 * @param name 单参数情况
	 * @return
	 */
	public String common(String name) {

		List<String> usernameList = new ArrayList<>();
		//name解析
		String[] array = name.split(":");
		if(null == array && array.length!=2){
			throw new BusinessException("表达式有误");
		}
		//做表达式解析
		//为角色的情况
		if(array[0].equals("role")){
			//对角色数据再次解析
			String[] roles = array[1].split(",");
			List<String> roleNameList = new ArrayList<String>(Arrays.asList(roles)) ;
			//通过角色名称查询角色id集合
			List<String> roleIdList = baseOrgRoleService.getIdByName(roleNameList);
			if(roleNameList.contains("self")){
				UserSession userSession = httpHelper.getUser(request);
				roleIdList.add(userSession.getRoleId());
			}
			//通过角色列表查询所有用户信息
			usernameList = sysUserService.findUsernameListByRoleList(roleIdList);
		}else if(array[0].equals("dept")){
			//对部门数据再次解析
			String[] depts = array[1].split(",");
			List<String> deptNameList = new ArrayList<String>(Arrays.asList(depts)) ;
			//通过部门名称查询部门id集合
			List<String> deptIdList =baseOrgService.getIdByName(deptNameList);
			if(deptNameList.contains("self")){
				UserSession userSession = httpHelper.getUser(request);
				deptIdList.add(userSession.getOrgId());
			}
			//通过角色列表查询所有用户信息
			usernameList = sysUserService.findUsernameListByDeptList(deptIdList);
		}
		return StringUtils.collectionToDelimitedString(usernameList ,",");
	}


	/**
	 * 通用选人方法 两个参数 限制关系
	 * @param firstParam
	 * @param secondParam
	 * @return
	 */
	public String common(String firstParam,String secondParam) {
		List<String> usernameList = new ArrayList<>();
		//参数1解析
		String[] firstParamArray = firstParam.split(":");
		if(null == firstParamArray && firstParamArray.length!=2){
			throw new BusinessException("表达式1有误！");
		}
		//参数2解析
		String[] secondParamArray = secondParam.split(":");
		if(null == secondParamArray && secondParamArray.length!=2){
			throw new BusinessException("表达式2有误！");
		}
		if(firstParamArray[0].equals(secondParamArray[0])){
			throw new BusinessException("表达式有误，两个参数写了同一条件！");
		}
		//两个表达式都正常的情况下 开始解析 先初始化两个list
		List<String> deptIdList = new ArrayList<>();
		List<String> roleIdList = new ArrayList<>();
		if(firstParamArray[0].equals("dept")){
			//对角色数据再次解析
			String[] depts = firstParamArray[1].split(",");
			List<String> deptNameList = new ArrayList<String>(Arrays.asList(depts)) ;
			//通过部门名称查询部门id集合
			deptIdList =baseOrgService.getIdByName(deptNameList);
			if(deptNameList.contains("self")){
				UserSession userSession = httpHelper.getUser(request);
				deptIdList.add(userSession.getOrgId());
			}
		}else{
			String[] roles = firstParamArray[1].split(",");
			List<String> roleNameList = new ArrayList<String>(Arrays.asList(roles)) ;
			//通过角色名称查询角色id集合
			roleIdList = baseOrgRoleService.getIdByName(roleNameList);
			if(roleNameList.contains("self")){
				UserSession userSession = httpHelper.getUser(request);
				roleIdList.add(userSession.getRoleId());
			}
		}
		if(secondParamArray[0].equals("role")){
			String[] roles = secondParamArray[1].split(",");
			List<String> roleNameList = new ArrayList<String>(Arrays.asList(roles)) ;
			//通过角色名称查询角色id集合
			roleIdList = baseOrgRoleService.getIdByName(roleNameList);
			if(roleNameList.contains("self")){
				UserSession userSession = httpHelper.getUser(request);
				roleIdList.add(userSession.getRoleId());
			}
		}else{
			//对角色数据再次解析
			String[] depts = secondParamArray[1].split(",");
			List<String> deptNameList = new ArrayList<String>(Arrays.asList(depts)) ;
			//通过部门名称查询部门id集合
			deptIdList =baseOrgService.getIdByName(deptNameList);
			if(deptNameList.contains("self")){
				UserSession userSession = httpHelper.getUser(request);
				deptIdList.add(userSession.getOrgId());
			}
		}
		usernameList = sysUserService.findUsernameListByDeptListAndRoleList(deptIdList,roleIdList);
		return StringUtils.collectionToDelimitedString(usernameList ,",");
	}


}
