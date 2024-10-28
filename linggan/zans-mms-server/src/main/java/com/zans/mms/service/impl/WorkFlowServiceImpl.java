package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.exception.BusinessException;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.BusinessTableInfoMapper;
import com.zans.mms.dao.WorkflowTaskInfoMapper;
import com.zans.mms.dto.workflow.*;
import com.zans.mms.model.WorkflowTaskInfo;
import com.zans.mms.service.ITicketService;
import com.zans.mms.service.IWorkFlowService;
import com.zans.mms.vo.asset.AssetResVO;
import com.zans.mms.vo.ticket.AppPendingApprovalReqVO;
import com.zans.mms.vo.ticket.AppPendingApprovalRespVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/19
 */
@Service("workFlowService")
@Slf4j
public class WorkFlowServiceImpl implements IWorkFlowService {

	@Autowired
	RepositoryService rep;


	@Autowired
	ProcessEngine processEngine;


	@Autowired
	ITicketService ticketService;

	@Autowired
	IdentityService identityservice;

	@Autowired
	RuntimeService runtimeService;


	@Autowired
	TaskService taskService;



	@Autowired
	HistoryService historyService;


	@Resource
	BusinessTableInfoMapper businessTableInfoMapper;

	@Resource
	WorkflowTaskInfoMapper workflowTaskInfoMapper;

	@Autowired
	private RepositoryService repositoryService;









	/**************************通用***************************/

	/**
	 * 任务流转
	 * @param completeTaskVO 任务流转接收实体
	 * @return
	 */
	@Override
	public TaskRepDto circulationTask(CirculationTaskDto completeTaskVO){
		if(StringUtils.isEmpty(completeTaskVO.getUsername())){
			throw new BusinessException("username不存在！");
		}
		if(StringUtils.isEmpty(completeTaskVO.getWorkflowId())){
			throw new BusinessException("workflowId不能为空!");
		}
		log.info("任务id{}",completeTaskVO.getTaskId());
		Task task = taskService.createTaskQuery().taskId(completeTaskVO.getTaskId()).singleResult();
		if (null == task) {
			throw new BusinessException("任务不存在或已完成！");
		}
		String statusCode=null;
		//任务对象  获取流程实例Id
		String processInstanceId = task.getProcessInstanceId();
		//通过流程实例id 拿到业务主键
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String businessKey= pi.getBusinessKey();
		//设置审批人的userId
		Authentication.setAuthenticatedUserId(completeTaskVO.getUsername());
		//添加评论记录
		taskService.addComment(completeTaskVO.getTaskId(), processInstanceId, completeTaskVO.getMsg());
		log.info("-----------完成任务操作 开始----------");
		log.info("任务Id=" + completeTaskVO.getTaskId());
		log.info("负责人用户名=" + completeTaskVO.getUsername());
		log.info("流程实例id=" + processInstanceId);
		//完成办理
		//有分支情况 需要传参数
		Map<String, Object> map = new HashMap<>();
		map.put("isAgree", completeTaskVO.getIsAgree());
		try {
			taskService.claim(completeTaskVO.getTaskId(),completeTaskVO.getUsername());
			taskService.complete(completeTaskVO.getTaskId(), map);
		}catch (Exception e){
			throw new BusinessException("您无权限对此流程进行审批!");
		}

		log.info("-----------完成任务操作 结束----------");
		Task nextTask= taskService.createTaskQuery().processInstanceId(processInstanceId).executionId(task.getExecutionId()).singleResult();

		//todo 判断是否为自己能审批，第一思路，添加下一步审批人角色id做判断 解决bug：单人审批多步


		maintenanceTaskId(nextTask.getId(),processInstanceId,completeTaskVO.getWorkflowId(),businessKey,"circulation");
		if(null == nextTask){
			//任务办结时调用
			statusCode = this.updateCompleteStatus(completeTaskVO.getWorkflowId(), businessKey);
			throw new BusinessException("任务已办结！");
		}else{
			try {
				statusCode=this.updateStatus(nextTask.getId(),completeTaskVO.getWorkflowId(),businessKey);
			}catch (Exception e){
				throw  new BusinessException("业务状态修改失败,请检查配置是否正确");
			}
		}
		TaskRepDto taskRepDto = new TaskRepDto();
		taskRepDto.setTaskId(nextTask.getId());
		taskRepDto.setTaskName(nextTask.getName());
		taskRepDto.setDescription(nextTask.getDescription());
		taskRepDto.setAssignee(nextTask.getAssignee());
		taskRepDto.setProcessInstanceId(nextTask.getProcessInstanceId());
		taskRepDto.setWorkflowId(nextTask.getProcessDefinitionId());
		taskRepDto.setBusinessStatusCode(statusCode);
		taskRepDto.setBeforeTaskName(task.getName());
		return taskRepDto;
	}




	/**
	 * 启动流程
	 * @param username 用户名
	 * @param workFlowId 流程id
	 * @return
	 */
	@Override
	public TaskRepDto startFlow(String username,String businessKey, String workFlowId,Map<String, Object> param) {
		//先对workFlowId判空
		if(StringUtils.isEmpty(workFlowId)){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(username)){
			throw new BusinessException("用户名不能为空!");
		}
		//对业务主键判空
		if(StringUtils.isEmpty(businessKey)){
			throw new BusinessException("业务主键不能为空!");
		}
		//设置流程起草人
		identityservice.setAuthenticatedUserId(username);
		//防止传空
		if(null == param){
			param= new HashMap<>();
		}
		param.put("draft", username);
		//开启流程
		ProcessInstance instance = null;
		try {
			 instance=runtimeService.startProcessInstanceByKey(workFlowId,businessKey,param);
		}catch (Exception e){
			throw new BusinessException("workFlowId不存在，流程发起失败!");
		}
		Task nextTask= taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
		String statusCode=null;
		TaskRepDto taskRepDto =getTaskInfoByProcessInstanceId(instance.getId());
		//做taskId维护
		maintenanceTaskId(nextTask.getId(),instance.getId(),workFlowId,businessKey,"start");
		if(null == nextTask){
			//任务办结时调用
			try {
				statusCode = this.updateCompleteStatus(workFlowId, businessKey);
			}catch (Exception e){
				throw  new BusinessException("业务状态修改失败,请检查配置是否正确");
			}
		}else {
			//进行流程状态修改
			try {
				statusCode=this.updateStatus(nextTask.getId(),workFlowId,businessKey);
			}catch (Exception e){
				throw  new BusinessException("业务状态修改失败,请检查配置是否正确");
			}
		}
		taskRepDto.setBusinessStatusCode(statusCode);
		//调用通过流程实例id拿到下个任务实体数据的方法

		return taskRepDto;
	}

	/**
	 * 启动流程
	 * @param username 用户名
	 * @param workFlowId 流程id
	 * @return
	 */
	@Override
	public TaskRepDto startFlow(String username, String businessKey, String workFlowId) {
		return startFlow(username, businessKey, workFlowId, new HashMap<String, Object>());
	}


	/**
	 * 获取我的待办列表
	 * @param username 用户名
	 * @param workflowId 流程id
	 * @return
	 */
	@Override
	public List<TaskInfoDetailDto> myFlowList(String username, String workflowId) {
		//先对workFlowId判空
		if(StringUtils.isEmpty(workflowId)){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(username)){
			throw new BusinessException("用户名不能为空!");
		}
		List<Task> taskList=null;
		// 查询我的待办任务
		try{
			taskList = taskService
					//创建任务查询对象
					.createTaskQuery()
					/** 查询条件（where部分） */
					.processDefinitionKey(workflowId)
					.taskCandidateOrAssigned(username)
					/** 排序 */
					.orderByTaskCreateTime().asc()// 使用创建时间的升序排列
					/** 返回结果集 */
					.list();// 返回列表
		}catch (Exception e){
			throw new BusinessException("workflowId不存在");
		}

		List<TaskInfoDetailDto> taskInfoDetailDtoList = new ArrayList<>();
		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				TaskInfoDetailDto taskInfoDetailDto = new TaskInfoDetailDto();
				taskInfoDetailDto.setTaskId(task.getId());
				taskInfoDetailDto.setTaskName(task.getName());
				taskInfoDetailDto.setDescription(task.getDescription());
				taskInfoDetailDto.setAssignee(task.getAssignee());
				taskInfoDetailDto.setCreateTime(task.getCreateTime());
				taskInfoDetailDto.setProcessInstanceId(task.getProcessInstanceId());
				taskInfoDetailDto.setWorkflowId(task.getProcessDefinitionId());
				taskInfoDetailDtoList.add(taskInfoDetailDto);
			}
		}
		return taskInfoDetailDtoList;

	}

	@Override
	public List<TaskInfoDetailDto> myFlowList(String username, List<String> workflowIdList) {
		//先对workFlowId判空
		if(null==workflowIdList||workflowIdList.size()==0){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(username)){
			throw new BusinessException("用户名不能为空!");
		}
		List<Task> taskList=null;
		// 查询我的待办任务
		try{
			taskList = taskService
					//创建任务查询对象
					.createTaskQuery()
					/** 查询条件（where部分） */
					.processDefinitionKeyIn(workflowIdList)
					.taskCandidateOrAssigned(username)
					/** 排序 */
					.orderByTaskCreateTime().asc()// 使用创建时间的升序排列
					/** 返回结果集 */
					.list();// 返回列表
		}catch (Exception e){
			throw new BusinessException("workflowId不存在");
		}

		List<TaskInfoDetailDto> taskInfoDetailDtoList = new ArrayList<>();
		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				TaskInfoDetailDto taskInfoDetailDto = new TaskInfoDetailDto();
				taskInfoDetailDto.setTaskId(task.getId());
				taskInfoDetailDto.setTaskName(task.getName());
				taskInfoDetailDto.setDescription(task.getDescription());
				taskInfoDetailDto.setAssignee(task.getAssignee());
				taskInfoDetailDto.setCreateTime(task.getCreateTime());
				taskInfoDetailDto.setProcessInstanceId(task.getProcessInstanceId());
				taskInfoDetailDto.setWorkflowId(task.getProcessDefinitionId());
				taskInfoDetailDtoList.add(taskInfoDetailDto);
			}
		}
		return taskInfoDetailDtoList;
	}

	/**
	 * 我的待办列表 分页
	 * @param username 用户名
	 * @param workflowIdList 流程id列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	@Override
	public PageResult<TaskInfoDetailDto> myFlowList(String username, List<String> workflowIdList, Integer pageNum, Integer pageSize) {
		//分页 给pageNum和pageSize默认值
		if(null==pageNum||pageNum==0){
			pageNum=1;
		}
		if(null==pageSize||pageSize==0){
			pageSize=10;
		}
		//先对workFlowId判空
		if(null==workflowIdList||workflowIdList.size()==0){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(username)){
			throw new BusinessException("用户名不能为空!");
		}
		int startIndex=(pageNum-1)*pageSize;
		List<Task> taskList=null;
		// 查询我的待办任务

		taskList = taskService
				//创建任务查询对象
				.createTaskQuery()
				*//** 查询条件（where部分） *//*
				.processDefinitionKeyIn(workflowIdList)
				.taskCandidateOrAssigned(username)
				*//** 排序 *//*
				.orderByTaskCreateTime().desc()// 使用创建时间的升序排列
				*//** 返回结果集 *//*
				.listPage(startIndex, pageSize);// 返回列表
		long count = taskService
				//创建任务查询对象
				.createTaskQuery()
				*//** 查询条件（where部分） *//*
				.processDefinitionKeyIn(workflowIdList)
				.taskCandidateOrAssigned(username)
				*//** 排序 *//*
				.orderByTaskCreateTime().desc()// 使用创建时间的升序排列
				*//** 返回结果集 *//*
				.count();
		List<TaskInfoDetailDto> taskInfoDetailDtoList = new ArrayList<>();
		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				TaskInfoDetailDto taskInfoDetailDto = new TaskInfoDetailDto();
				taskInfoDetailDto.setTaskId(task.getId());
				taskInfoDetailDto.setTaskName(task.getName());
				taskInfoDetailDto.setDescription(task.getDescription());
				taskInfoDetailDto.setAssignee(task.getAssignee());
				taskInfoDetailDto.setCreateTime(task.getCreateTime());
				taskInfoDetailDto.setProcessInstanceId(task.getProcessInstanceId());
				taskInfoDetailDto.setWorkflowId(task.getProcessDefinitionId());
				taskInfoDetailDtoList.add(taskInfoDetailDto);
			}
		}
		PageResult pageResult = new PageResult(count,taskInfoDetailDtoList,pageSize,pageNum);
		return pageResult;
	}*/




	/**
	 * 我审批完成的流程列表查看
	 * @param username 用户名
	 * @param workflowId 流程id
	 * @return
	 */
	@Override
	public List<String> myApprovalFlowList(String username, String workflowId) {
		//先对workFlowId判空
		if(StringUtils.isEmpty(workflowId)){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(username)){
			throw new BusinessException("用户名不能为空!");
		}
		List<HistoricTaskInstance> historicTaskInstanceList = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.taskAssignee(username)
				.processDefinitionKey(workflowId)
				.finished()
				.list();
		List<String> processInstanceIdList = new ArrayList<>();
		if (historicTaskInstanceList != null && historicTaskInstanceList.size() > 0) {
			for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
				if(!processInstanceIdList.contains(historicTaskInstance.getProcessInstanceId())){
					processInstanceIdList.add(historicTaskInstance.getProcessInstanceId());
				}
			}
		}
		return processInstanceIdList;
	}

	/**
	 * 查询
	 * @param username 用户名
	 * @return
	 */
	@Override
	public List<String> myApprovalFlowList(String username,List<String> workflowIdList) {
		//先对workFlowId判空
		if(null == workflowIdList ||  workflowIdList.size()==0){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(username)){
			throw new BusinessException("用户名不能为空!");
		}
		List<HistoricTaskInstance> historicTaskInstanceList = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.taskAssignee(username)
				.processDefinitionKeyIn(workflowIdList)
				.finished()
				.list();
		List<String> processInstanceIdList = new ArrayList<>();
		if (historicTaskInstanceList != null && historicTaskInstanceList.size() > 0) {
			for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
				if(!processInstanceIdList.contains(historicTaskInstance.getProcessInstanceId())){
					processInstanceIdList.add(historicTaskInstance.getProcessInstanceId());
				}
			}
		}
		return processInstanceIdList;
	}



	/**
	 * 查询已办列表 分页
	 * @param username 用户名
	 * @return
	 */
	@Override
	public PageResult<String> myApprovalFlowList(String username,List<String> workflowIdList,Integer pageNum,Integer pageSize) {
		if(null==pageNum||pageNum==0){
			pageNum=1;
		}
		if(null==pageSize||pageSize==0){
			pageSize=10;
		}
		int startIndex=(pageNum-1)*pageSize;
		//先对workFlowId判空
		if(null == workflowIdList ||  workflowIdList.size()==0){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(username)){
			throw new BusinessException("用户名不能为空!");
		}
		List<HistoricTaskInstance> historicTaskInstanceList = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.taskAssignee(username)
				.processDefinitionKeyIn(workflowIdList)
				.finished()
				.listPage(startIndex,pageSize);
		long count = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.taskAssignee(username)
				.processDefinitionKeyIn(workflowIdList)
				.finished()
				.count();
		List<String> processInstanceIdList = new ArrayList<>();
		if (historicTaskInstanceList != null && historicTaskInstanceList.size() > 0) {
			for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
				if(!processInstanceIdList.contains(historicTaskInstance.getProcessInstanceId())){
					processInstanceIdList.add(historicTaskInstance.getProcessInstanceId());
				}
			}
		}
		PageResult pageResult = new PageResult(count,processInstanceIdList,pageSize,pageNum);
		return pageResult;
	}

	/**
	 * 通过流程实例id 拿到任务信息
	 * @param instanceId
	 * @return
	 */
	@Override
	public TaskRepDto getTaskInfoByProcessInstanceId(String instanceId) {
		Task nextTask = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
		if(null == nextTask){
			return null;
		}
		TaskRepDto taskRepDto = new TaskRepDto();
		taskRepDto.setTaskId(nextTask.getId());
		taskRepDto.setTaskName(nextTask.getName());
		taskRepDto.setDescription(nextTask.getDescription());
		taskRepDto.setAssignee(nextTask.getAssignee());
		taskRepDto.setProcessInstanceId(nextTask.getProcessInstanceId());
		taskRepDto.setWorkflowId(nextTask.getProcessDefinitionId());
		return taskRepDto;

	}

	/**
	 * 根据流程实例id查询任务id
	 * @param instanceId
	 * @return
	 */
	@Override
	public String getTaskIdByInstanceId(String instanceId) {
		return taskService.createTaskQuery().processInstanceId(instanceId).singleResult().getId();
	}


	/**
	 * 根据任务id获取任务信息
	 * @param taskId 任务id
	 * @return
	 */
	@Override
	public TaskRepDto getTaskInfoByTaskId(String taskId) {
		if(StringUtils.isEmpty(taskId)){
			return null;
		}
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskRepDto taskRepDto = new TaskRepDto();
		taskRepDto.setTaskId(task.getId());
		taskRepDto.setTaskName(task.getName());
		taskRepDto.setDescription(task.getDescription());
		taskRepDto.setAssignee(task.getAssignee());
		taskRepDto.setProcessInstanceId(task.getProcessInstanceId());
		taskRepDto.setWorkflowId(task.getProcessDefinitionId());
		return taskRepDto;
	}

	/**
	 * 根据实例id查询评论数据
	 * @param processInstanceId 流程实例id
	 * @return
	 */
	@Override
	public List<CommentDto> getCommentByInstanceId(String processInstanceId) {
		List<Comment> commentList=taskService.getProcessInstanceComments(processInstanceId);
		List<CommentDto> commentDtoList = new ArrayList<>();
		for(Comment comment : commentList){
			CommentDto commentDto = new CommentDto();
			commentDto.setMsg(comment.getFullMessage());
			commentDto.setUsername(comment.getUserId());
			/**
			 * 然后用用户名查询用户信息 这一步在工作流中做 还是在业务中做
			 */
			commentDtoList.add(commentDto);
		}
		return commentDtoList;
	}

	/**
	 * 通过流程实例id查询历史任务
	 * @param processInstanceId 流程实例id
	 * @return
	 */
	@Override
	public List<TaskInfoDetailDto> getHisTaskListByProcessInstanceId(String processInstanceId) {
		List<HistoricTaskInstance> historicTaskInstanceList = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();
		List<TaskInfoDetailDto> taskInfoDetailDtoList = new ArrayList<>();
		if (historicTaskInstanceList != null && historicTaskInstanceList.size() > 0) {
			for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
					TaskInfoDetailDto taskInfoDetailDto = new TaskInfoDetailDto();
					taskInfoDetailDto.setTaskId(historicTaskInstance.getId());
					taskInfoDetailDto.setProcessInstanceId(historicTaskInstance.getProcessInstanceId());
					taskInfoDetailDto.setTaskName(historicTaskInstance.getName());
					taskInfoDetailDto.setAssignee(historicTaskInstance.getAssignee());
					taskInfoDetailDto.setCreateTime(historicTaskInstance.getEndTime());
					taskInfoDetailDto.setDescription(historicTaskInstance.getDescription());
					taskInfoDetailDto.setWorkflowId(historicTaskInstance.getProcessDefinitionId());
					taskInfoDetailDtoList.add(taskInfoDetailDto);
			}

		}
		return taskInfoDetailDtoList;
	}

	/**
	 * 获取我的所有流程数据
	 * @param userName
	 * @param workflowIdList
	 * @return
	 */
	@Override
	public List<String> getMyAllProcessInstanceIdList(String userName, List<String> workflowIdList) {
		List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().involvedUser(userName).processDefinitionKeyIn(workflowIdList).list();
		List<String> processInstanceIdList = new ArrayList<>();
		if(null != historicProcessInstanceList && historicProcessInstanceList.size()>0){
			for(HistoricProcessInstance historicProcessInstance : historicProcessInstanceList){
				String id = historicProcessInstance.getId();
				processInstanceIdList.add(id);
			}
		}
		return processInstanceIdList;
	}

	/**
	 * 获取我的所有流程数据 分页
	 * @param userName 用户名
	 * @param workflowIdList 流程文件id列表
	 * @param pageNum 页码
	 * @param pageSize 页大小
	 * @return
	 */
	@Override
	public PageResult<String> getMyAllProcessInstanceIdList(String userName, List<String> workflowIdList, Integer pageNum, Integer pageSize) {
		if(null==pageNum||pageNum==0){
			pageNum=1;
		}
		if(null==pageSize||pageSize==0){
			pageSize=10;
		}
		int startIndex=(pageNum-1)*pageSize;
		Set set = new HashSet(workflowIdList);
		//List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processDefinitionKeys(set).involvedUser(userName).listPage(startIndex,pageSize);
		List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().involvedUser(userName).processDefinitionKeyIn(workflowIdList).listPage(startIndex,pageSize);
		long count=historyService.createHistoricProcessInstanceQuery().involvedUser(userName).processDefinitionKeyIn(workflowIdList).count();
		// long count = runtimeService.createProcessInstanceQuery().processDefinitionKeys(set).involvedUser(userName).count();
		List<String> processInstanceIdList = new ArrayList<>();
		if(null != historicProcessInstanceList && historicProcessInstanceList.size()>0){
			for(HistoricProcessInstance processInstance : historicProcessInstanceList){
				String id = processInstance.getId();
				processInstanceIdList.add(id);
			}
		}
		return new PageResult<String>(count,processInstanceIdList,pageSize,pageNum);
	}

	/**
	 * 通过流程实例id获取业务主键
	 * @param processInstanceId
	 * @return
	 */
	@Override
	public String getBusinessKeyByProcessInstanceId(String processInstanceId) {
		String businessKey=workflowTaskInfoMapper.getBusinessKeyByProcessInstanceId(processInstanceId);
		return businessKey;
	}

	/**
	 * 我的已办列表 分页
	 * @param userName
	 * @param workflowIdList
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageResult<TaskInfoDetailDto> myApprovalList(String userName, List<String> workflowIdList, Integer pageNum, Integer pageSize) {
		if(null==pageNum||pageNum==0){
			pageNum=1;
		}
		if(null==pageSize||pageSize==0){
			pageSize=10;
		}
		int startIndex=(pageNum-1)*pageSize;
		//先对workFlowId判空
		if(null == workflowIdList ||  workflowIdList.size()==0){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(userName)){
			throw new BusinessException("用户名不能为空!");
		}
		List<HistoricTaskInstance> historicTaskInstanceList = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.taskAssignee(userName)
				.processDefinitionKeyIn(workflowIdList)
				.orderByTaskCreateTime().desc()// 使用创建时间的升序排列
				.finished()

				.listPage(startIndex,pageSize);
		long count = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.taskAssignee(userName)
				.processDefinitionKeyIn(workflowIdList)
				.orderByTaskCreateTime().desc()// 使用创建时间的升序排列
				.finished()
				.count();
		List<TaskInfoDetailDto> taskInfoDetailDtoList = new ArrayList<>();
		if (historicTaskInstanceList != null && historicTaskInstanceList.size() > 0) {
			for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
				TaskInfoDetailDto taskInfoDetailDto = new TaskInfoDetailDto();
				taskInfoDetailDto.setTaskId(historicTaskInstance.getId());
				taskInfoDetailDto.setTaskName(historicTaskInstance.getName());
				taskInfoDetailDto.setDescription(historicTaskInstance.getDescription());
				taskInfoDetailDto.setAssignee(historicTaskInstance.getAssignee());
				taskInfoDetailDto.setCreateTime(historicTaskInstance.getCreateTime());
				taskInfoDetailDto.setProcessInstanceId(historicTaskInstance.getProcessInstanceId());
				taskInfoDetailDto.setWorkflowId(historicTaskInstance.getProcessDefinitionId());
				taskInfoDetailDtoList.add(taskInfoDetailDto);
			}
		}
		PageResult pageResult = new PageResult(count,taskInfoDetailDtoList,pageSize,pageNum);
		return pageResult;
	}

	/**
	 * 我的所有流程列表 分页
	 * @param userName
	 * @param workflowIdList
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageResult<TaskInfoDetailDto> myAllFlowList(String userName, List<String> workflowIdList, Integer pageNum, Integer pageSize) {
		//分页 给pageNum和pageSize默认值
		if(null==pageNum||pageNum==0){
			pageNum=1;
		}
		if(null==pageSize||pageSize==0){
			pageSize=10;
		}
		//先对workFlowId判空
		if(null==workflowIdList||workflowIdList.size()==0){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(userName)){
			throw new BusinessException("用户名不能为空!");
		}
		int startIndex=(pageNum-1)*pageSize;
		List<HistoricProcessInstance> historicProcessInstanceList = historyService
				.createHistoricProcessInstanceQuery()
				.involvedUser(userName)
				.processDefinitionKeyIn(workflowIdList)
				.orderByProcessInstanceStartTime().desc()
				.listPage(startIndex,pageSize);
		long count = historyService
				.createHistoricProcessInstanceQuery()
				.involvedUser(userName)
				.processDefinitionKeyIn(workflowIdList)
				.orderByProcessInstanceStartTime().desc()
				.count();
		List<TaskInfoDetailDto> taskInfoDetailDtoList = new ArrayList<>();
		if (historicProcessInstanceList != null && historicProcessInstanceList.size() > 0) {
			for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
				Task task = taskService.createTaskQuery().processInstanceId(historicProcessInstance.getId()).singleResult();
				TaskInfoDetailDto taskInfoDetailDto = new TaskInfoDetailDto();
				if(null!=task){
					taskInfoDetailDto.setTaskId(task.getId());
					taskInfoDetailDto.setTaskName(task.getName());
					taskInfoDetailDto.setDescription(task.getDescription());
					taskInfoDetailDto.setAssignee(task.getAssignee());
					taskInfoDetailDto.setCreateTime(task.getCreateTime());
					taskInfoDetailDto.setProcessInstanceId(task.getProcessInstanceId());
					taskInfoDetailDto.setWorkflowId(task.getProcessDefinitionId());
					taskInfoDetailDtoList.add(taskInfoDetailDto);
				}
			}
		}
		PageResult pageResult = new PageResult(count,taskInfoDetailDtoList,pageSize,pageNum);
		return pageResult;
	}

	/**
	 * 通过任务id获取活动id
	 * @param taskId
	 * @return
	 */
	@Override
	public String getActivityIdByTaskId(String taskId) {
		if(StringUtils.isEmpty(taskId)){
			return null;
		}
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(null==task){
			return null;
		}
		return task.getTaskDefinitionKey();
	}

	/**
	 * 根据任务id拿到起草人
	 * @param taskId
	 * @return
	 */
	@Override
	public String getDraftByTaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(null==task){
			return null;
		}
		String processInstanceId = task.getProcessInstanceId();
		//根据流程实例id拿到起草人
		String startUserId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getStartUserId();
		return startUserId;
	}


	/**
	 * 业务状态修改
	 * @param taskId 任务id
	 * @param workflowId   流程文件id
	 */
	private String updateStatus(String taskId, String workflowId,String businessKey) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		BusinessTableInfoDto businessTableInfoDto=businessTableInfoMapper.getTableInfoByWorkflowId(workflowId);
		//拿到状态编码
		String statusCode = businessTableInfoMapper.getStatusByWorkflowIdAndTaskName(workflowId,task.getName());

		if(StringHelper.isEmpty(statusCode)){
			statusCode="20";
		}
		if(Integer.parseInt(statusCode)>-1){
			try {
				businessTableInfoMapper.updateStatus(businessTableInfoDto.getBusinessTableName(),businessTableInfoDto.getBusinessTableStatusField(),statusCode,businessKey);
			}catch (Exception e){
				throw new BusinessException("修改业务状态失败");
			}
			return statusCode;
		}else {
			return null;
		}

	}

	/**
	 * 任务办结状态修改
	 * @param businessKey 业务主键id
	 * @param workflowId 流程文件id
	 */
	private String updateCompleteStatus(String workflowId,String businessKey) {
		BusinessTableInfoDto businessTableInfoDto=businessTableInfoMapper.getTableInfoByWorkflowId(workflowId);
		//拿到状态编码  办结字段可配置为常量
		String statusCode = businessTableInfoMapper.getStatusByWorkflowIdAndTaskName(workflowId,"办结");
		if(Integer.parseInt(statusCode)>-1) {
			try {
				businessTableInfoMapper.updateStatus(businessTableInfoDto.getBusinessTableName(), businessTableInfoDto.getBusinessTableStatusField(), statusCode, businessKey);
			} catch (Exception e) {
				throw new BusinessException("修改业务状态失败");
			}
			return statusCode;
		}else {
			return null;
		}

	}


	/**
	 * 任务id维护方法
	 * @param taskId 任务id
	 * @param processInstanceId 流程实例id
	 * @param workflowId 流程文件id
	 * @param businessKey 业务主键
	 */
	public void maintenanceTaskId(String taskId,String processInstanceId,String workflowId,String businessKey,String operation){
		WorkflowTaskInfo workflowTaskInfo = new WorkflowTaskInfo();
		workflowTaskInfo.setTaskId(taskId);
		workflowTaskInfo.setProcessInstanceId(processInstanceId);
		if("start".equals(operation)){
			workflowTaskInfo.setCreateTime(new Date(System.currentTimeMillis()-1000));
		}else{
			workflowTaskInfo.setCreateTime(new Date());
		}
		workflowTaskInfo.setType(workflowId);
		workflowTaskInfo.setBusinessId(businessKey);
		workflowTaskInfoMapper.insert(workflowTaskInfo);
	}

	/**
	 * 个人待审批数量查询
	 * @param username
	 * @param workflowIdList
	 * @return
	 */
	@Override
	public Integer myTicketFlowCount(String username, List<String> workflowIdList) {
		Long count = taskService
				//创建任务查询对象
				.createTaskQuery()
				/** 查询条件（where部分） */
				.processDefinitionKeyIn(workflowIdList)
				.taskCandidateOrAssigned(username)
				/** 排序 */
				.orderByTaskCreateTime().desc()// 使用创建时间的升序排列
				/** 返回结果集 */
				.count();
		return count.intValue();
	}

	/**
	 * 删除一个流程实例
	 * @param precessInstanceId
	 * @return
	 */
	@Override
	public Boolean deleteProcessInstance(String precessInstanceId) {
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processInstanceId(precessInstanceId).list();
		if (processInstances.isEmpty()) {
			// 历史流程实例
			List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(precessInstanceId).list();
			if (historicProcessInstances.isEmpty()) {
				String msg = "No process instances with the ID: " + precessInstanceId;
				log.error(msg);
				return false;
			}
			historyService.deleteHistoricProcessInstance(precessInstanceId);
			return true;
		}
		runtimeService.deleteProcessInstance(precessInstanceId, "删除原因");
		return true;
	}


	/**************************通用************************************/

	/**---------------------------21021/10/14改造----------------------------------**/
	/**
	 * 我的待办列表 分页 2021/10/14改造
	 * @param reqVO 查询传参
	 * @return
	 */
	@Override
	public PageResult<AppPendingApprovalRespVO> myFlowList(AppPendingApprovalReqVO reqVO) {
		//先对workFlowId判空
		if(StringHelper.isEmpty(reqVO)){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(reqVO.getAssign())){
			throw new BusinessException("用户名不能为空!");
		}
		if(null==reqVO.getPageNum()){
			reqVO.setPageNum(1);
		}
		if(null==reqVO.getPageSize()){
			reqVO.setPageSize(99999);
		}
		Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
		// 查询我的待办任务
		List<AppPendingApprovalRespVO> ticketWorkflowDtoList = workflowTaskInfoMapper.getMyFlowList(reqVO);
		return new PageResult<AppPendingApprovalRespVO>(page.getTotal(), ticketWorkflowDtoList, reqVO.getPageSize(), reqVO.getPageNum());
	}

	@Override
	public List<AppPendingApprovalRespVO>  getOnePendingTicket(Long id) {
		return workflowTaskInfoMapper.getOnePendingTicket(id);
	}

	/**
	 * 2021/10/14改造 我的已审批列表
	 * @param reqVO
	 * @return
	 */
	@Override
	public PageResult<AppPendingApprovalRespVO> myApprovedTicketList(AppPendingApprovalReqVO reqVO) {
		//先对workFlowId判空
		if(StringHelper.isEmpty(reqVO)){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(reqVO.getAssign())){
			throw new BusinessException("用户名不能为空!");
		}
		Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
		// 查询我的待办任务
		List<AppPendingApprovalRespVO> ticketWorkflowDtoList = workflowTaskInfoMapper.myApprovedTicketList(reqVO);
		return new PageResult<AppPendingApprovalRespVO>(page.getTotal(), ticketWorkflowDtoList, reqVO.getPageSize(), reqVO.getPageNum());

	}

	/**
	 * 2021/10/14改造 我的全部审批列表
	 * @param reqVO
	 * @return
	 */
	@Override
	public PageResult<AppPendingApprovalRespVO> myAllTicketList(AppPendingApprovalReqVO reqVO) {
		//先对workFlowId判空
		if(StringHelper.isEmpty(reqVO)){
			throw new BusinessException("workflowId不能为空!");
		}
		//在对用户名判空
		if(StringUtils.isEmpty(reqVO.getAssign())){
			throw new BusinessException("用户名不能为空!");
		}
		Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
		// 查询我的待办任务
		List<AppPendingApprovalRespVO> ticketWorkflowDtoList = workflowTaskInfoMapper.myAllTicketList(reqVO);
		return new PageResult<AppPendingApprovalRespVO>(page.getTotal(), ticketWorkflowDtoList, reqVO.getPageSize(), reqVO.getPageNum());

	}

	/**---------------------------21021/10/14改造----------------------------------**/
}
