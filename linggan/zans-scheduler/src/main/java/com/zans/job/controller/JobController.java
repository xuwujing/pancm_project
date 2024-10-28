package com.zans.job.controller;

import com.zans.base.controller.BaseController;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.job.api.BaseJob;
import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.model.OpsJobTask;
import com.zans.job.service.IExecutionService;
import com.zans.job.service.IJobService;
import com.zans.job.service.ITaskService;
import com.zans.job.vo.JobAndTrigger;
import com.zans.job.vo.JobInfo;
import com.zans.job.service.IJobAndTriggerService;
import com.zans.job.utils.DateUnit;
import com.zans.job.utils.SpringUtil;
import com.zans.job.vo.execution.ExecuReqVO;
import com.zans.job.vo.execution.ExecuRespVO;
import com.zans.job.vo.job.JobReqVO;
import com.zans.job.vo.job.JobRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.quartz.DateBuilder.futureDate;

/**
 * @author xv
 * @since 2020/5/6 16:01
 */
@Api(value = "/job", tags = {"/job ~ 任务控制器"})
@Slf4j
@RestController
@RequestMapping(value = "job")
public class JobController extends BaseController {

    @Autowired
    private IJobAndTriggerService jobAndTriggerService;

    @Autowired
    private IJobService jobService;

    @Autowired
    private IExecutionService executionService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private DateUnit dateUnit;





    /**
     * 添加任务
     *
     * @param jobInfo
     * @throws Exception
     */
    @PostMapping(value = "/add")
    public void addJob(@RequestBody JobInfo jobInfo) throws Exception {
        if ("".equals(jobInfo.getJobName()) || "".equals(jobInfo.getJobClassName()) ||
                "".equals(jobInfo.getJobGroupName()) || "".equals(jobInfo.getCronExpression())) {
            return;
        }
        log.info("job#{}", jobInfo);
        if (jobInfo.getTimeType() == null) {
            addCronJob(jobInfo);
        } else {
            addSimpleJob(jobInfo);
        }

    }

    /**
     * CronTrigger
     *
     * @param jobInfo
     * @throws Exception
     */
    private void addCronJob(JobInfo jobInfo) throws Exception {

        // 启动调度器
        scheduler.start();
        JobDetail jobDetail = parseJobDetail(jobInfo);
        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().
                withIdentity(jobInfo.getJobName(), jobInfo.getJobGroupName())
                .withSchedule(scheduleBuilder)
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("创建定时任务失败:{}" , jobInfo,e);
            throw new Exception("创建定时任务失败");
        }
    }

    private JobDetail parseJobDetail(JobInfo jobInfo) throws Exception {
        JobBuilder builder = JobBuilder.newJob(getClass(jobInfo.getJobClassName()).getClass())
                .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroupName());
        if (jobInfo.getExtra() != null && jobInfo.getExtra().size() > 0) {
            for (String key : jobInfo.getExtra().keySet()) {
                String val = jobInfo.getExtra().get(key);
                builder.usingJobData(key, val);
            }
        }
        //构建job信息
        return builder.build();
    }

    /**
     * Simple Trigger
     *
     * @param jobInfo
     * @throws Exception
     */
    private void addSimpleJob(JobInfo jobInfo) throws Exception {
        // 启动调度器
        scheduler.start();
        //构建job信息
        JobDetail jobDetail = parseJobDetail(jobInfo);
        DateBuilder.IntervalUnit verDate = dateUnit.verification(jobInfo.getTimeType());
        SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroupName())
                .startAt(futureDate(Integer.parseInt(jobInfo.getCronExpression()), verDate))
                .forJob(jobInfo.getJobName(), jobInfo.getJobGroupName())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, simpleTrigger);
        } catch (SchedulerException e) {
            log.error("创建定时任务失败" , e);
            throw new Exception("创建定时任务失败");
        }
    }

    /**
     * 暂停任务
     *
     * @param jobName
     * @param jobGroupName
     * @throws Exception
     */
    @PostMapping(value = "/pause")
    public void pauseJob(@RequestParam(value = "jobName") String jobName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        log.info("{} {}#{}", jobName, jobGroupName, "pause");
        jobPause(jobName, jobGroupName);
    }

    private void jobPause(String jobName, String jobGroupName) throws Exception {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 恢复任务
     *
     * @param jobName
     * @param jobGroupName
     * @throws Exception
     */
    @PostMapping(value = "/resume")
    public void resumeJob(@RequestParam(value = "jobName") String jobName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        log.info("{} {}#{}", jobName, jobGroupName, "resume");
        jobResume(jobName, jobGroupName);
    }

    private void jobResume(String jobName, String jobGroupName) throws Exception {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 更新任务
     *
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    @PostMapping(value = "/reschedule")
    public void rescheduleJob(@RequestParam(value = "jobName") String jobName,
                              @RequestParam(value = "jobGroupName") String jobGroupName,
                              @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        jobReschedule(jobName, jobGroupName, cronExpression);
    }

    private void jobReschedule(String jobName, String jobGroupName, String cronExpression) throws Exception {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败" , e);
            throw new Exception("更新定时任务失败");
        }
    }

    /**
     * 删除任务
     * 删除操作前应该暂停该任务的触发器，并且停止该任务的执行
     *
     * @param jobName
     * @param jobGroupName
     * @throws Exception
     */
    @PostMapping(value = "/delete")
    public void deleteJob(@RequestParam(value = "jobName") String jobName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        jobDelete(jobName, jobGroupName);
    }

    public void jobDelete(String jobName, String jobGroupName) throws Exception {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 查询任务
     *
     * @return
     */
    @GetMapping(value = "/query")
    public Map<String, Object> queryJob() {
        List<JobAndTrigger> jobAndTrigger = jobAndTriggerService.getJobAndTriggerDetails();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("JobAndTrigger", jobAndTrigger);
        map.put("number", jobAndTrigger.size());
        return map;
    }

    /**
     * 根据类名称，通过反射得到该类，然后创建一个BaseJob的实例。
     * 由于NewJob和HelloJob都实现了BaseJob，
     * 所以这里不需要我们手动去判断。这里涉及到了一些java多态调用的机制
     *
     * @param className
     * @return
     * @throws Exception
     */
    public static BaseJob getClass(String className) throws Exception {
        BaseJob baseJob = (BaseJob) SpringUtil.getBean(className);
        return baseJob;
    }

    public void startJob(Integer enable, Integer jobId, JobInfo info) throws Exception {
        if (jobId == null) {
            addCronJob(info);
        } else {
            //enable = 1启用状态，需要重启
            if (enable == 1) {
                rescheduleJob(info.getJobName(), info.getJobGroupName(), info.getCronExpression());
            } else {
                addCronJob(info);
            }
        }
    }

    @ApiOperation(value = "/list", notes = "任务列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "JobReqVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody JobReqVO req) {
        super.checkPageParams(req, "job_id desc");

        PageResult<JobRespVO> jobPage = jobService.getJobPage(req);
        List<JobRespVO> list = jobPage.getList();
        for (JobRespVO res : list) {
            String second = secondToTime(String.valueOf(res.getTimeout()));
            res.setTimeoutstr(second);
        }

        Map<String, Object> result = MapBuilder.getBuilder()
                .put("table", jobPage)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/view", notes = "任务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "ExecuReqVO", paramType = "body")
    })
    @RequestMapping(value = "/view", method = {RequestMethod.POST})
    public ApiResult<JobRespVO> view(@RequestBody ExecuReqVO req) {
        OpsJob job = jobService.getById(req.getJobId());
        if (job == null) {
            return ApiResult.error("当前任务不存在#" + req.getJobId());
        }

        super.checkPageParams(req, "exec_id desc");
        PageResult<ExecuRespVO> executePage = executionService.getExecutePage(req);
        List<ExecuRespVO> list = executePage.getList();
        for (ExecuRespVO respVO : list) {
            Date finishTime = respVO.getFinishTime();
            Date startTime = respVO.getStartTime();
            if (finishTime != null && startTime != null) {
                String seconds = String.valueOf( (finishTime.getTime() - startTime.getTime())/ 1000 );
                String second = secondToTime(seconds);
                respVO.setExecuteTime(second);
            }
        }
        executePage.setList(list);
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("job", job)
                .put("executeTable", executePage)
                .build();
        return ApiResult.success(map);
    }

    @ApiOperation(value = "/insertOrUpdate", notes = "新增/修改Job")
    @ApiImplicitParam(name = "mergeVO", value = "新增/修改Job", required = true, dataType = "JobRespVO", paramType = "body")
    @RequestMapping(value = "/insertOrUpdate", method = {RequestMethod.POST})
    public ApiResult insert(@Valid @RequestBody JobReqVO mergeVO, HttpServletRequest request) throws Exception {
        Integer jobId = mergeVO.getJobId();
        String jobName = mergeVO.getJobName();
//        validAndSetCorn(mergeVO);
        int count = jobService.getJobByJobNameAndId(jobName, jobId);
        if (count > 0) {
            return ApiResult.error("任务名称已存在#" + jobName);
        }
        OpsJob job = new OpsJob();
        job.setEnable(0);
        if (jobId == null) {
            BeanUtils.copyProperties(mergeVO, job);
            job.setRunCount(0);
            job.setRecordCount(0);
            jobService.insert(job);
        } else {
            job = jobService.getById(jobId);
            job = JobRespVO.init(job, mergeVO);
        }
        //调用quartz
        JobInfo info = JobInfo.setJobInfo(job);
        startJob(job.getEnable(), jobId, info);
        job.setEnable(1);
        jobService.update(job);
        return ApiResult.success(MapBuilder.getSimpleMap("id", job.getJobId())).appendMessage("请求成功");
    }

    private void validAndSetCorn(@RequestBody @Valid JobReqVO mergeVO) {
        String cron  = mergeVO.getCronExpression();
        int length = StringUtils.trimAllWhitespace(cron).length();
        if(length == 9){
            String a = cron.substring(1).substring(0, cron.length() - 3);
            cron = "0".concat(a);
            mergeVO.setCronExpression(cron);
        }
    }

    @ApiOperation(value = "/enable", notes = "0:禁用;1:启动;2:停止、3:恢复")
    @RequestMapping(value = "/enable", method = {RequestMethod.POST})
    public ApiResult enable(@RequestParam(value = "jobId", required = true) Integer jobId,
                            @RequestParam(value = "enable", required = true) Integer enable,
                            HttpServletRequest request) throws Exception {
        OpsJob job = jobService.getById(jobId);
        if (job == null) {
            return ApiResult.error("任务不存在#" + jobId);
        }
        JobInfo info = JobInfo.setJobInfo(job);
        switch (enable) {
            case 0:
                jobDelete(info.getJobName(), info.getJobGroupName());
                break;
            case 1:
                startJob(job.getEnable(), jobId, info);
                break;
            case 2:
                jobPause(info.getJobName(), info.getJobGroupName());
                break;
            case 3:
                jobResume(info.getJobName(), info.getJobGroupName());
                enable = 1;
                break;
            default:
                return ApiResult.error("参数异常enable#" + enable);
        }
        job.setEnable(enable);
        jobService.update(job);
        return ApiResult.success();
    }


    @ApiOperation(value = "/deleteJob", notes = "删除Job")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/deleteJob", method = {RequestMethod.POST})
    public ApiResult deleteJob(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        OpsJob job = jobService.getById(id);
        if (job.getEnable() != 0) {
            return ApiResult.error("当前状态无法删除，enable#" + job.getEnable());
        }
        jobService.delete(job);

        //删除execution
        List<OpsJobExecution> execution = executionService.getExecutionByJob(job.getJobId());
        executionService.deleteAll(execution);

        //删除task
        List<OpsJobTask> tasks = taskService.findByJobId(id);
        for (OpsJobTask task : tasks) {
            task.setEnable(1);
            taskService.update(task);
        }
        return ApiResult.success().appendMessage("删除成功");
    }

    public String secondToTime(String durationSeconds) {
        if (StringUtils.isEmpty(durationSeconds) || durationSeconds.equals("0")) {
            return "0";
        }
        Integer second = Integer.parseInt(durationSeconds);
        long d = second / (60 * 60 * 24);
        long h = (second - (60 * 60 * 24 * d)) / 3600;
        long m = (second - 60 * 60 * 24 * d - 3600 * h) / 60;
        long s = second - 60 * 60 * 24 * d - 3600 * h - 60 * m;
        String result = (d == 0 ? "" : d + "天") + (h == 0 ? "" : h + "时") + (m == 0 ? "" : m + "分") + (s == 0 ? "" : s + "秒");
        return result;
    }

}
