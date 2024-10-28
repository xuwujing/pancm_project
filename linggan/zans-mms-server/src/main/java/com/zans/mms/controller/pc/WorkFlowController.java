package com.zans.mms.controller.pc;

import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.dto.workflow.*;
import com.zans.mms.model.Ticket;
import com.zans.mms.service.ITicketService;
import com.zans.mms.service.IWorkFlowService;
import com.zans.mms.util.FlowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工作流转接口
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/14
 */
@RestController
@RequestMapping("constant")
@Api(tags = "工作流")
@Validated
@Slf4j
public class WorkFlowController extends BaseController {

	@Autowired
	IWorkFlowService workFlowService;

	@Autowired
	HttpHelper httpHelper;

	@Autowired
	HistoryService historyService;

	@Autowired
	TaskService taskService;

	@Autowired
	ProcessEngine processEngine;


	@Autowired
	FlowUtils flowUtils;

	@Autowired
	ITicketService ticketService;




	/***
	 * 流程图查看
	 * @param response
	 * @param instanceId 流程实例id
	 * @throws Exception 异常
	 */
	@RequestMapping(value="traceProcess",method=RequestMethod.GET)
	public void traceProcess(HttpServletResponse response, @RequestParam("processInstanceId")String instanceId) throws Exception{
		InputStream in = flowUtils.getResourceDiagramInputStream(instanceId);
		ServletOutputStream output = response.getOutputStream();
		IOUtils.copy(in, output);
	}


	/**
	 * 通过流程实例id  查看审批历史
	 * @param processInstanceId
	 * @return
	 */
	@GetMapping("getHistory")
	public ApiResult getHistory(@RequestParam("processInstanceId") String processInstanceId){
		return ApiResult.success(workFlowService.getHisTaskListByProcessInstanceId(processInstanceId));
	}


	/**
	 * 通过流程实例id获取评论
	 * @param processInstanceId 流程实例id
	 * @return
	 */
	@GetMapping("getComment")
	public ApiResult getCommentList(@RequestParam("processInstanceId") String processInstanceId){
		return ApiResult.success(workFlowService.getCommentByInstanceId(processInstanceId));
	}


	@PostMapping("myDispatchList")
	public ApiResult myDispatchList(HttpServletRequest request){
		UserSession userSession = httpHelper.getUser(request);
		return ApiResult.success(workFlowService.myFlowList(userSession.getUserName(),"acceptance"));
	}





	/******************测试代码开始**********************************/
	@RequestMapping("startDispatch")
	public ApiResult startDispatch(HttpServletRequest request){
		UserSession userSession = httpHelper.getUser(request);
		String userName = userSession.getUserName();
		Map<String,Object> map = new HashMap<>();
		map.put("money",50000);
		TaskRepDto taskRepDto = workFlowService.startFlow(userName, "362", "dispatch", map);
		return ApiResult.success(taskRepDto);
	}

	@RequestMapping("circulationDispatch")
	public ApiResult circulationDispatch(HttpServletRequest request,@RequestBody CirculationTaskDto circulationTaskDto){
		UserSession userSession = httpHelper.getUser(request);
		String userName = userSession.getUserName();
		circulationTaskDto.setWorkflowId("dispatch");
		circulationTaskDto.setUsername(userName);
		TaskRepDto taskRepDto = workFlowService.circulationTask(circulationTaskDto);
		return ApiResult.success(taskRepDto);
	}




	/**
	 * 查询全部流程
	 */
	@RequestMapping("myAllFlow")
	public ApiResult myAllFlow(HttpServletRequest request){
		return ApiResult.success(ticketService.myTicketList(httpHelper.getUser(request).getUserName()));
	}




	/****************测试代码结束************************************/
}
