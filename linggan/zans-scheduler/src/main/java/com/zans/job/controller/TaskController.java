package com.zans.job.controller;

import com.zans.base.controller.BaseController;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.job.model.OpsJobTask;
import com.zans.job.model.OpsNode;
import com.zans.job.service.IExecutionService;
import com.zans.job.service.ITaskService;
import com.zans.job.vo.job.JobReqVO;
import com.zans.job.vo.job.JobRespVO;
import com.zans.job.vo.task.TaskReqVO;
import com.zans.job.vo.task.TaskRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.job.config.JobConstants.TASK_DONE;

/**
 * @author xv
 * @since 2020/5/9 0:06
 */
@Api(value = "/task", tags = {"/task ~ 任务控制器"})
@Slf4j
@RestController
@RequestMapping(value = "/task")
@Validated
public class TaskController extends BaseController {

    @Autowired
    ITaskService taskService;

    @Autowired
    IExecutionService executionService;

    @ApiOperation(value = "/finish", notes = "节点续租")
    @RequestMapping(value = "/finish", method = {RequestMethod.POST})
    public ApiResult finish(@NotEmpty(message = "节点ID必填") @RequestParam("node_id") String nodeId,
                            @NotNull(message = "任务ID必填") @RequestParam("task_id") Long taskId,
                            HttpServletRequest request) {
        log.info("task#{} finished, node#{}", taskId, nodeId);
        OpsJobTask taskDb = taskService.getById(taskId);
        if (taskDb == null) {
            return ApiResult.error("任务ID不存在#" + taskId);
        }
        taskDb.setFinishStatus();
        taskService.update(taskDb);
        // update job_execution.job_status
        executionService.checkExecution(taskDb.getExecId());

        return ApiResult.success();
    }

    @ApiOperation(value = "/list", notes = "task列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "TaskReqVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody TaskReqVO req) {
        super.checkPageParams(req, "task_id desc");

        PageResult<TaskRespVO> jobPage = taskService.getTaskPage(req);
        List<TaskRespVO> list = jobPage.getList();
        for (TaskRespVO task : list) {
            String startTime = task.getStartTime();
            String finishTime = task.getFinishTime();
            if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(finishTime)) {
                long start = DateHelper.parseDatetime(startTime).getTime();
                long finish = DateHelper.parseDatetime(finishTime).getTime();
                long time = (finish - start) / 1000;
                String second = new JobController().secondToTime(String.valueOf(time));
                task.setExecuteTime(second);
            }
        }
        jobPage.setList(list);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("table", jobPage)
                .build();
        return ApiResult.success(result);
    }

}
