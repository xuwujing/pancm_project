package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dto.workflow.*;
import com.zans.mms.vo.ticket.AppPendingApprovalReqVO;
import com.zans.mms.vo.ticket.AppPendingApprovalRespVO;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;

import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工作流封装层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/19
 */
public interface IWorkFlowService {




	/**
	 * 任务流转
	 * @param completeTaskVO 流转任务接收实体
	 * @return
	 */
	TaskRepDto circulationTask(CirculationTaskDto completeTaskVO);


	/**
	 * 启动流程
	 * @param username 用户名
	 * @param businessKey 业务主键
	 * @param workFlowId 流程id
	 * @param param 流程条件传参
	 * @return
	 */
	TaskRepDto  startFlow(String username, String businessKey, String workFlowId, Map<String, Object> param);



	/**
	 * 启动流程
	 * @param username 用户名
	 * @param businessKey 业务主键
	 * @param workFlowId 流程id
	 * @return
	 */
	TaskRepDto  startFlow(String username, String businessKey, String workFlowId);




	/**
	 * 我的流程列表
	 * @param username 用户名
	 * @param workflowId 流程id
	 * @return
	 */
	List<TaskInfoDetailDto> myFlowList(String username, String workflowId);


	/**
	 * 我的流程列表
	 * @param username 用户名
	 * @param workflowIdList 流程id列表
	 * @return
	 */
	List<TaskInfoDetailDto> myFlowList(String username, List<String> workflowIdList);

/*
	*/
/**
	 * 我的流程列表 分页
	 * @param username 用户名
	 * @param workflowIdList 流程id列表
	 * @return
	 *//*

	PageResult<TaskInfoDetailDto> myFlowList(String username, List<String> workflowIdList, Integer pageNum, Integer pageSize);
*/


	/**
	 * 我的流程列表 分页
	 * @param reqVO 查询传参
	 * @return
	 */
	PageResult<AppPendingApprovalRespVO> myFlowList(AppPendingApprovalReqVO reqVO);


	/**
	 * 我审批完成的流程列表
	 * @param username 用户名
	 * @param workflowId 流程id
	 * @return
	 */
	List<String> myApprovalFlowList(String username, String workflowId);

	/**
	 * 我审批完成的流程列表
	 * @param username 用户名
	 * @return
	 */
	List<String> myApprovalFlowList(String username,List<String> workflowIdList);


	/**
	 * 我审批完成的流程列表 分页
	 * @param username 用户名
	 * @return
	 */
	PageResult<String> myApprovalFlowList(String username,List<String> workflowIdList,Integer pageNum,Integer pageSize);

	/**
	 * 通过流程实例id拿任务信息数据
	 * @param instanceId
	 * @return
	 */
	TaskRepDto getTaskInfoByProcessInstanceId(String instanceId);

	/**
	 * 根据流程实例id查询任务id
	 * @param instanceId
	 * @return
	 */
	String getTaskIdByInstanceId(String instanceId);



	/**
	 * 根据任务id 拿任务信息
	 * @param taskId 任务id
	 * @return
	 */
	TaskRepDto getTaskInfoByTaskId(String taskId);


	/**
	 * 根据实例id查询评论数据
	 * @param processInstanceId 流程实例id
	 * @return
	 */
	List<CommentDto> getCommentByInstanceId(String processInstanceId);


	/**
	 * 通过实例id 查询历史任务数据
	 * @param processInstanceId
	 * @return
	 */
	List<TaskInfoDetailDto> getHisTaskListByProcessInstanceId(String processInstanceId);


	/**
	 * 查询我的所有工单任务 包含已完成和未完成
	 * @param userName
	 * @param workflowIdList
	 * @return
	 */
	List<String> getMyAllProcessInstanceIdList(String userName, List<String> workflowIdList);


	/**
	 * 查询我的所有工单任务 包含已完成和未完成 分页
	 * @param userName
	 * @param workflowIdList
	 * @return
	 */
	PageResult<String> getMyAllProcessInstanceIdList(String userName, List<String> workflowIdList,Integer pageNum,Integer pageSize);

	/**
	 * 通过流程实例id查询业务主键
	 * @param processInstanceId 流程实例id
	 * @return
	 */
	String getBusinessKeyByProcessInstanceId(String processInstanceId);

	/**
	 * 我的已办列表
	 * @param userName
	 * @param workflowIdList
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageResult<TaskInfoDetailDto> myApprovalList(String userName, List<String> workflowIdList, Integer pageNum, Integer pageSize);

	/**
	 * 我的所有任务信息 新版 分页 注意区分
	 * @param userName
	 * @param workflowIdList
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageResult<TaskInfoDetailDto> myAllFlowList(String userName, List<String> workflowIdList, Integer pageNum, Integer pageSize);


	/**
	 * 根据任务id拿到活动id
	 * @param taskId
	 * @return
	 */
	String getActivityIdByTaskId(String taskId);

	/**
	 * 根据任务id拿到起草人
	 * @param taskId
	 * @return
	 */
	String getDraftByTaskId(String taskId);

	/**
	 * 个人待审批数量
	 * @param username
	 * @param workflowIdList
	 * @return
	 */
	Integer myTicketFlowCount(String username, List<String> workflowIdList);

	/**
	 * 删除一个流程实例
	 * @param precessInstanceId
	 * @return
	 */
	Boolean deleteProcessInstance(String precessInstanceId);

	/**
	 * 2021/10/14改造 我的已审批列表
	 * @param reqVO
	 * @return
	 */
	PageResult<AppPendingApprovalRespVO> myApprovedTicketList(AppPendingApprovalReqVO reqVO);


	/**
	 * 2021/10/14改造 我的全部审批列表
	 * @param reqVO
	 * @return
	 */
	PageResult<AppPendingApprovalRespVO> myAllTicketList(AppPendingApprovalReqVO reqVO);

	/**
	 * 获取一条待审批数据
	 * @param id
	 * @return
	 */
	List<AppPendingApprovalRespVO>  getOnePendingTicket(Long id);
}
