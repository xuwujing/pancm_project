package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dto.workflow.*;
import com.zans.mms.vo.ticket.AppPendingApprovalReqVO;
import com.zans.mms.vo.ticket.AppPendingApprovalRespVO;


import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工作流及工单集成service
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/25
 */
public interface ITicketWorkflowService {

	/**
	 * 启动工单流程
	 * @param username 用户名
	 * @return
	 */
	TaskRepDto startTicketFlow(String username, String businessKey, Map<String,Object> param,String workflowId);


	/**
	 * 完成工单任务
	 * @param circulationTaskDto 任务流转实体
	 * @return
	 */
	TaskRepDto circulationTicketTask(CirculationTaskDto circulationTaskDto);


	/**
	 * 查询我的工单任务
	 * @param username 用户名
	 * @return
	 */
	List<TicketWorkflowDto> myTicketFlowList(String username,String workflowId);

	/**
	 * 查询我的工单任务
	 * @param username 用户名
	 * @param workflowIdList 流程文件id列表
	 * @return
	 */
	List<TicketWorkflowDto> myTicketFlowList(String username,List<String> workflowIdList);


/*
	*/
/**
	 * 查询我的工单任务 分页
	 * @param username 用户名
	 * @param workflowIdList 流程文件id列表
	 * @return
	 *//*

	PageResult<TicketWorkflowDto> myTicketFlowList(String username, List<String> workflowIdList, Integer pageNum, Integer pageSize);
*/


	/**
	 * 查询我的工单任务 分页 2021/10/14改造
	 * @param reqVO 查询传参
	 * @return
	 */
	PageResult<AppPendingApprovalRespVO> myTicketFlowList(AppPendingApprovalReqVO reqVO);


	/**
	 * 根据实例id查询任务信息
	 * @param processInstanceId
	 * @return
	 */
	TaskRepDto getByInstanceId(String processInstanceId);


	TicketWorkflowDto getByIdAndTaskId(String id,String taskId);




	/**
	 * 我的工单已办列表 分页 2021/10/14改造
	 * @param reqVO
	 * @return
	 */
	PageResult<AppPendingApprovalRespVO> myApprovedTicketList(AppPendingApprovalReqVO reqVO);

	/**
	 * 我的工单所有流程查询
	 * @param userName
	 * @param workflowIdList
	 * @return
	 */
	List<TicketWorkflowDto> myTicketList(String userName, List<String> workflowIdList);


	/**
	 * 我的工单所有流程查询 分页
	 * @param userName
	 * @param workflowIdList
	 * @return
	 */
	PageResult<TicketWorkflowDto> myTicketList(String userName, List<String> workflowIdList,Integer pageNum ,Integer pageSize);

	/**
	 * 我的所有
	 * @param reqVO
	 * @return
	 */
	PageResult<AppPendingApprovalRespVO> myAllTicketList(AppPendingApprovalReqVO reqVO);



	/**
	 * 根据任务id拿到活动id
	 * @param taskId
	 * @return
	 */
	String getActivityIdByTaskId(String taskId);

	/**
	 * 根据任务id拿到起草人用户名
	 * @param taskId
	 * @return
	 */
	String getDraftByTaskId(String taskId);


	/**
	 * 根据任务id查询唯一待办人
	 * @param taskId
	 * @return
	 */
	String getAssigneeByTaskId(String taskId);


	/**
	 * 个任待审批数量查询
	 * @param username
	 * @param workflowIdList
	 * @return
	 */
	Integer myTicketFlowCount(String username, List<String> workflowIdList);

	/**
	 * 删除流程实例
	 * @param precessInstanceId
	 * @return
	 */
	Boolean deleteProcessInstance(String precessInstanceId);

	/**
	 * 获取一条待审批数据
	 * @param id
	 * @return
	 */
	List<AppPendingApprovalRespVO>  getOnePendingTicket(Long id);
}
