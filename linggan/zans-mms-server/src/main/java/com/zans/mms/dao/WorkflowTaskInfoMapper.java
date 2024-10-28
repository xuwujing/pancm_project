package com.zans.mms.dao;


import com.zans.base.vo.SelectVO;
import com.zans.mms.dto.workflow.BusinessStatusInfoDto;
import com.zans.mms.dto.workflow.TaskInfoDetailDto;
import com.zans.mms.dto.workflow.TicketWorkflowDto;
import com.zans.mms.model.WorkflowTaskInfo;
import com.zans.mms.vo.ticket.AppPendingApprovalReqVO;
import com.zans.mms.vo.ticket.AppPendingApprovalRespVO;
import com.zans.mms.vo.ticket.TicketWorkFlowRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/8
 */
@Repository
public interface WorkflowTaskInfoMapper extends Mapper<WorkflowTaskInfo> {

	String getByBusinessIdAndWorkflowId(String businessId, String workflowId);


	List<TicketWorkFlowRespVO> getByWorkflowId(Integer status, String workflowId,Integer threshold);


	List<BusinessStatusInfoDto> getTicketBusinessStatusNameList();

	String getBusinessKeyByProcessInstanceId(String processInstanceId);

	/**
	* @Author beiming
	* @Description
	* @Date  5/14/21
	* @Param
	* @return
	**/
    List<String> getUserIdList(String taskId);

	/**
	 * 获取拥有审批权限的用户名列表
	 * @param taskId
	 * @return
	 */
	List<String> getUserIdListByTaskId(String taskId);

	List<String> getUserByAssigneeList(@Param("assigneeList") List<String> assigneeList);

	String getRoleNameByStatusCode(@Param("opStatus") Integer opStatus,@Param("workflowId") String workflowId);

	List<SelectVO> getDispatchInit();

	List<SelectVO> getAcceptanceInit();

	String getProcessInstanceByIdAndType(@Param("id") Long id,@Param("type") String type);

	List<String> getUserByApproved(@Param("usernameList") List<String> usernameList);

	void deleteByBusinessIdAndType(@Param("id") Long id,@Param("type") String type);

	String getDefaultComment(@Param("workflowId") String workflowId,@Param("status") Integer status);

	List<AppPendingApprovalRespVO> getMyFlowList(AppPendingApprovalReqVO reqVO);

	List<AppPendingApprovalRespVO> myApprovedTicketList(AppPendingApprovalReqVO reqVO);

	List<AppPendingApprovalRespVO> myAllTicketList(AppPendingApprovalReqVO reqVO);

	List<AppPendingApprovalRespVO>  getOnePendingTicket(@Param("id") Long id);
}
