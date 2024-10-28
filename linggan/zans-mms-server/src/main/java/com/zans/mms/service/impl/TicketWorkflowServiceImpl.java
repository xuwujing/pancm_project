package com.zans.mms.service.impl;

import com.zans.base.util.StringHelper;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.WorkflowTaskInfoMapper;
import com.zans.mms.dto.workflow.*;
import com.zans.mms.model.WorkflowTaskInfo;
import com.zans.mms.service.ITicketService;
import com.zans.mms.service.ITicketWorkflowService;
import com.zans.mms.service.IWorkFlowService;
import com.zans.mms.vo.ticket.AppPendingApprovalReqVO;
import com.zans.mms.vo.ticket.AppPendingApprovalRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/25
 */
@Service("ticketWorkflowService")
@Slf4j
public class TicketWorkflowServiceImpl implements ITicketWorkflowService {


	@Autowired
	IWorkFlowService workFlowService;

	@Autowired
	ITicketService ticketService;

	@Resource
	WorkflowTaskInfoMapper workflowTaskInfoMapper;

	/**
	 * 启动工单流程
	 * @param username 用户名
	 * @return
	 */
	@Override
	public TaskRepDto startTicketFlow(String username, String businessKey, Map<String,Object> param,String workflowId) {
		//调用工作流的发起流程方法
		TaskRepDto taskRepDto = workFlowService.startFlow(username,businessKey,workflowId,param);
		CirculationTaskDto circulationTaskDto = new CirculationTaskDto();
		circulationTaskDto.setUsername(username);
		circulationTaskDto.setWorkflowId(workflowId);
		circulationTaskDto.setTaskId(taskRepDto.getTaskId());
		circulationTaskDto.setMsg("同意");
		circulationTaskDto.setIsAgree("1");
		TaskRepDto taskRep = this.circulationTicketTask(circulationTaskDto);
		//调用时应对实体进行判空
		return taskRep;
	}

	/**
	 * 工单任务流转
	 * @param completeTaskVO 完成任务参数实体
	 * @return
	 */
	@Override
	public TaskRepDto circulationTicketTask(CirculationTaskDto completeTaskVO)  {
		return workFlowService.circulationTask(completeTaskVO);
	}

	/**
	 * 我的工单任务待办列表
	 * @param username 用户名
	 * @return
	 */
	@Override
	public List<TicketWorkflowDto> myTicketFlowList(String username,String workflowId) {
		List<TaskInfoDetailDto> taskInfoDetailDtoList = workFlowService.myFlowList(username, workflowId);
		List<TicketWorkflowDto> ticketWorkflowDtoList = new ArrayList<>();
		for(TaskInfoDetailDto taskInfoDetailDto : taskInfoDetailDtoList){
			//拿到实例id
			String processInstanceId = taskInfoDetailDto.getProcessInstanceId();
			String businessKey = workFlowService.getBusinessKeyByProcessInstanceId(processInstanceId);
			//通过实例id查询数据实体 但是写的这个方法可能不能满足显示需要的数据
			TicketWorkflowDto ticketWorkflowDto=ticketService.getById(businessKey);
			//数据实体添加任务信息
			if(null != ticketWorkflowDto){
				ticketWorkflowDto.setTaskId(taskInfoDetailDto.getTaskId());
				ticketWorkflowDto.setTaskName(taskInfoDetailDto.getTaskName());
				ticketWorkflowDto.setDescription(taskInfoDetailDto.getDescription());
				ticketWorkflowDto.setWorkflowId(taskInfoDetailDto.getWorkflowId());
				//返回数据
				ticketWorkflowDtoList.add(ticketWorkflowDto);
			}
		}
		return ticketWorkflowDtoList;
	}

	/**
	 * 我的待办列表通用接口
	 * @param username 用户名
	 * @param workflowIdList 流程文件id列表
	 * @return
	 */
	@Override
	public List<TicketWorkflowDto> myTicketFlowList(String username, List<String> workflowIdList) {
		List<TaskInfoDetailDto> taskInfoDetailDtoList = workFlowService.myFlowList(username, workflowIdList);
		List<TicketWorkflowDto> ticketWorkflowDtoList = new ArrayList<>();
		for(TaskInfoDetailDto taskInfoDetailDto : taskInfoDetailDtoList){
			//拿到实例id
			String processInstanceId = taskInfoDetailDto.getProcessInstanceId();
			String businessKey = workFlowService.getBusinessKeyByProcessInstanceId(processInstanceId);
			//通过实例id查询数据实体 但是写的这个方法可能有问题
			TicketWorkflowDto ticketWorkflowDto=ticketService.getById(businessKey);
			//数据实体添加任务信息
			if(null != ticketWorkflowDto){
				ticketWorkflowDto.setTaskId(taskInfoDetailDto.getTaskId());
				ticketWorkflowDto.setTaskName(taskInfoDetailDto.getTaskName());
				ticketWorkflowDto.setDescription(taskInfoDetailDto.getDescription());
				ticketWorkflowDto.setWorkflowId(taskInfoDetailDto.getWorkflowId());
				//返回数据
				ticketWorkflowDtoList.add(ticketWorkflowDto);
			}
		}
		return ticketWorkflowDtoList;
	}

/*

	*/
/**
	 * 我的待办列表通用接口 分页
	 * @param username 用户名
	 * @param workflowIdList 流程文件id列表
	 * @return
	 *//*

	@Override
	public PageResult<TicketWorkflowDto>  myTicketFlowList(String username, List<String> workflowIdList, Integer pageNum, Integer pageSize) {
		PageResult<TaskInfoDetailDto> taskInfoDetailPageDto = workFlowService.myFlowList(username, workflowIdList,pageNum,pageSize);
		List<TicketWorkflowDto> ticketWorkflowDtoList = new ArrayList<>();
		for(TaskInfoDetailDto taskInfoDetailDto : taskInfoDetailPageDto.getList()){
			//拿到实例id
			String processInstanceId = taskInfoDetailDto.getProcessInstanceId();
			//通过实例id拿到业务主键 通过主键查询实体
			String businessKey = workFlowService.getBusinessKeyByProcessInstanceId(processInstanceId);
			//通过实例id查询数据实体 但是写的这个方法可能有问题
			TicketWorkflowDto ticketWorkflowDto=ticketService.getById(businessKey);
			//数据实体添加任务信息
			if(null != ticketWorkflowDto){
				ticketWorkflowDto.setTaskId(taskInfoDetailDto.getTaskId());
				ticketWorkflowDto.setTaskName(taskInfoDetailDto.getTaskName());
				ticketWorkflowDto.setDescription(taskInfoDetailDto.getDescription());
				ticketWorkflowDto.setWorkflowId(taskInfoDetailDto.getWorkflowId());
				//返回数据
				ticketWorkflowDtoList.add(ticketWorkflowDto);
			}
		}
		PageResult<TicketWorkflowDto> page= new PageResult(taskInfoDetailPageDto.getTotalNum(),ticketWorkflowDtoList,taskInfoDetailPageDto.getPageSize(),taskInfoDetailPageDto.getPageNum());
		return page;
	}
*/


	/**
	 * 我的待办列表通用接口 分页
	 * @param reqVO 查询传参
	 * @return
	 */
	@Override
	public PageResult<AppPendingApprovalRespVO>  myTicketFlowList(AppPendingApprovalReqVO reqVO) {
		return  workFlowService.myFlowList(reqVO);
	}

	@Override
	public List<AppPendingApprovalRespVO>  getOnePendingTicket(Long id) {
		return workFlowService.getOnePendingTicket(id);
	}

	/**
	 *根据实例id查询任务信息
	 * @param processInstanceId 实例id
	 * @return
	 */
	@Override
	public TaskRepDto getByInstanceId(String processInstanceId) {
		return workFlowService.getTaskInfoByProcessInstanceId(processInstanceId);
	}

	/**
	 * 获取工单信息详情
	 * @param id 工单id
	 * @param taskId 任务id
	 * @return
	 */
	@Override
	public TicketWorkflowDto getByIdAndTaskId(String id, String taskId) {
		TicketWorkflowDto ticketWorkflowDto = ticketService.getById(id);
		TaskRepDto taskRepDto = workFlowService.getTaskInfoByTaskId(taskId);
		ticketWorkflowDto.setTaskId(taskId);
		ticketWorkflowDto.setTaskName(taskRepDto.getTaskName());
		ticketWorkflowDto.setDescription(taskRepDto.getDescription());
		ticketWorkflowDto.setWorkflowId(taskRepDto.getWorkflowId());
		return ticketWorkflowDto;
	}





	/**
	 * 我的工单已办列表 分页 2021/10/14 改造
	 * @param reqVO
	 * @return
	 */
	@Override
	public PageResult<AppPendingApprovalRespVO> myApprovedTicketList(AppPendingApprovalReqVO reqVO) {
		return   workFlowService.myApprovedTicketList(reqVO);
	}


	/**
	 * 我的所有流程任务信息 2021/10/14 改造
	 * @param reqVO
	 * @return
	 */
	@Override
	public PageResult<AppPendingApprovalRespVO> myAllTicketList(AppPendingApprovalReqVO reqVO) {
		return   workFlowService.myAllTicketList(reqVO);
	}

	/**
	 * 我的工单列表查询
	 * @param userName 用户名
	 * @param workflowIdList 流程文件id列表
	 * @return
	 */
	@Override
	public List<TicketWorkflowDto> myTicketList(String userName, List<String> workflowIdList) {
		List<String> processInstanceIdList=workFlowService.getMyAllProcessInstanceIdList(userName,workflowIdList);
		List<TicketWorkflowDto> ticketWorkflowDtoList = new ArrayList<>();
		//通过流程实例id 查询工单数据
		for (String processInstanceId : processInstanceIdList){
			log.info("processInstanceId{}",processInstanceId);
			String businessKey = workFlowService.getBusinessKeyByProcessInstanceId(processInstanceId);
			TicketWorkflowDto ticketWorkflowDto = ticketService.getById(businessKey);
			//通过流程实例id查询任务信息 不存在则为办结
			TaskRepDto taskRepDto = getByInstanceId(processInstanceId);
			//如果任务信息为空 说明已办结
			if(null == taskRepDto){
				ticketWorkflowDto.setTaskName("完成");
				ticketWorkflowDto.setDescription("完成");
			}else {
				ticketWorkflowDto.setTaskId(taskRepDto.getTaskId());
				ticketWorkflowDto.setTaskName(taskRepDto.getTaskName());
				ticketWorkflowDto.setDescription(taskRepDto.getDescription());
				ticketWorkflowDto.setWorkflowId(taskRepDto.getWorkflowId());
			}
			ticketWorkflowDtoList.add(ticketWorkflowDto);
		}
		return ticketWorkflowDtoList;
	}

	/**
	 * 我的工单列表查询 分页
	 * @param userName 用户名
	 * @param workflowIdList 流程文件id列表
	 * @return
	 */
	@Override
	public PageResult<TicketWorkflowDto> myTicketList(String userName, List<String> workflowIdList, Integer pageNum, Integer pageSize) {
		PageResult<String> processInstanceIdList=workFlowService.getMyAllProcessInstanceIdList(userName,workflowIdList,pageNum,pageSize);
		List<TicketWorkflowDto> ticketWorkflowDtoList = new ArrayList<>();
		for (String processInstanceId:processInstanceIdList.getList()) {
			log.info("processInstanceId{}",processInstanceId);
			String businessKey = workFlowService.getBusinessKeyByProcessInstanceId(processInstanceId);
			TicketWorkflowDto ticketWorkflowDto = ticketService.getById(businessKey);
			//再根据流程实例id 查询任务数据
			TaskRepDto taskRepDto = workFlowService.getTaskInfoByProcessInstanceId(processInstanceId);
			if(null !=taskRepDto){
				ticketWorkflowDto.setTaskId(taskRepDto.getTaskId());
				ticketWorkflowDto.setTaskName(taskRepDto.getTaskName());
				ticketWorkflowDto.setDescription(taskRepDto.getDescription());
				ticketWorkflowDto.setWorkflowId(taskRepDto.getWorkflowId());
			}else{
				ticketWorkflowDto.setTaskName("完成");
				ticketWorkflowDto.setDescription("完成");
			}
			ticketWorkflowDtoList.add(ticketWorkflowDto);
		}
		return new PageResult<>(processInstanceIdList.getTotalNum(),ticketWorkflowDtoList,processInstanceIdList.getPageSize(),processInstanceIdList.getPageNum());
	}



	/**
	 * 根据任务id拿到活动id
	 * @param taskId
	 * @return
	 */
	@Override
	public String getActivityIdByTaskId(String taskId) {
		return workFlowService.getActivityIdByTaskId(taskId);
	}

	/**
	 * 根据任务id拿到起草人用户名
	 * @param taskId
	 * @return
	 */
	@Override
	public String getDraftByTaskId(String taskId) {
		return workFlowService.getDraftByTaskId(taskId);
	}

	/**
	 * 根据任务id获取唯一审批人
	 * @param taskId
	 * @return
	 */
	@Override
	public String getAssigneeByTaskId(String taskId) {
		return workFlowService.getTaskInfoByTaskId(taskId).getAssignee();
	}

	/**
	 * 个任待审批数量查询
	 * @param username
	 * @param workflowIdList
	 * @return
	 */
	@Override
	public Integer myTicketFlowCount(String username, List<String> workflowIdList) {
		return workFlowService.myTicketFlowCount(username,workflowIdList);
	}

	/**
	 * 通过流程id删除一个实例
	 * @param precessInstanceId
	 * @return
	 */
	@Override
	public Boolean deleteProcessInstance(String precessInstanceId) {
		return workFlowService.deleteProcessInstance(precessInstanceId);
	}


}
