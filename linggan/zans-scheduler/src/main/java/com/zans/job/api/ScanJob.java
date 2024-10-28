package com.zans.job.api;

import com.zans.base.util.DateHelper;
import com.zans.base.util.StringHelper;
import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.model.OpsJobTask;
import com.zans.job.service.*;
import com.zans.job.strategy.RandomBalanceStrategy;
import com.zans.job.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.zans.job.config.JobConstants.*;

/**
 * @author xv
 * @since 2020/5/6 19:31
 */
@Slf4j
public abstract class ScanJob implements BaseJob {

    private static String SCAN_JOB_PREFIX = "scan#";

    @Autowired
    protected IJobService jobService;

    @Autowired
    protected INodeService nodeService;

    @Autowired
    protected IExecutionService executionService;

    @Autowired
    protected ITaskService taskService;

    @Autowired
    protected IRemoteService remoteService;

    @Autowired
    protected RandomBalanceStrategy randomBalanceStrategy;

    private int aliveJobCount;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        OpsJob job = parseJob(context);
        if (job == null) {
            return;
        }
        log.info("{}|扫描任务#{} start|", job.getJobId(), job.getJobName());

        int jobTimeoutMinutes = 0;
        if (job.getTimeout() == null) {
            jobTimeoutMinutes = JOB_EXECUTION_TIMEOUT_MINUTES;
        } else {
            jobTimeoutMinutes = job.getTimeout();
        }

//        List<NodeVO> nodeList = nodeService.getDispatchNodes(job.getPriority());
        List<NodeVO> nodeList = nodeService.getDispatchNodes(job.getPriority(),job.getJobType());
        if (nodeList == null || nodeList.size() == 0) {
            log.info("{}|扫描任务#{}，没有可用的执行节点|", job.getJobId(), job.getJobName());
            return;
        }

        Map<NodeVO, List<Long>> assignResult = null;
        OpsJobExecution execution = null;
        // check execution
        List<OpsJobExecution> executionList = executionService.getRunningExecutionByJob(job.getJobId());
        if (executionList != null && executionList.size() > 0) {
            // 获得最大的那个
            execution = executionList.get(0);
            // 判断本次执行是否结束
            execution = executionService.checkExecution(execution.getExecId());
            if (execution.getJobStatus() >= EXECUTION_DONE) {
                // 任务结束
                Pair<Map<NodeVO, List<Long>>, OpsJobExecution> ret = this.doNewExecution(job, nodeList);
                assignResult = ret.getValue0();
                execution = ret.getValue1();
            } else {
                // 超时，负责兜底
                Date startTime = execution.getStartTime();
                if (DateHelper.isTimeout(startTime, jobTimeoutMinutes)) {
                    log.info("{}|扫描任务#{}执行超时，执行编号#{}, 开始时间#{}, 结束本次执行，启动新任务|",
                            job.getJobId(), job.getJobName(), execution.getExecId(), DateHelper.getDateTime(startTime));
                    execution.setTimeoutStatus();
                    executionService.update(execution);
                    execution = null;

                    Pair<Map<NodeVO, List<Long>>, OpsJobExecution> ret = this.doNewExecution(job, nodeList);
                    assignResult = ret.getValue0();
                    execution = ret.getValue1();
                } else {
                    List<OpsJobTask> unAllocTasks = taskService.getUnAllocTasksOfExecution(execution.getExecId());
                    if (unAllocTasks == null || unAllocTasks.size() == 0) {
                        log.info("{}|扫描任务#{}执行中，跳过此次调度|", job.getJobId(), job.getJobName());
                        return;
                    }
                    log.info("{}|扫描任务#{}, 执行编号#{}, 加载上次分配失败任务#{}|",
                            job.getJobId(), job.getJobName(), execution.getExecId(), unAllocTasks.size());
                    assignResult = this.dispatchExistedTask(job, execution, unAllocTasks, nodeList);
                }
            }
        } else {
            Pair<Map<NodeVO, List<Long>>, OpsJobExecution> ret = this.doNewExecution(job, nodeList);
            assignResult = ret.getValue0();
            execution = ret.getValue1();
        }

        if (assignResult == null) {
            log.error("{}|扫描任务#{}, 分配失败，执行编号#{}|", job.getJobId(), job.getJobName(), execution.getExecId());
            execution.setAssignErrorStatus();
            executionService.update(execution);
            return;
        }
        if (assignResult.size() == 0) {
            log.error("{}|扫描任务#{}, 没有新数据，执行编号#{}|", job.getJobId(), job.getJobName(), execution.getExecId());
            execution.setNoDataStatus();
            executionService.update(execution);
            return;
        }

        this.assignTaskToExecutor(job, execution, assignResult, true);

        execution.setStartTime(new Date());
        execution.setJobStatus(EXECUTION_START);
        execution.setShardingNodeNum(assignResult.size());
        executionService.update(execution);


    }

    private Pair<Map<NodeVO, List<Long>>, OpsJobExecution> doNewExecution(OpsJob job, List<NodeVO> nodeList) {
        OpsJobExecution execution = new OpsJobExecution(job);
        executionService.save(execution);
        log.info("{}|扫描任务#{}, 执行编号#{}, 新任务|", job.getJobId(), job.getJobName(), execution.getExecId());
        Map<NodeVO, List<Long>> result = this.dispatchTask(job, execution, nodeList);
        return new Pair<>(result, execution);
    }

    /**
     * 创建调度及任务
     * @param job
     * @param nodeList
     * @return null，分配失败；size==0, 没有数据
     */
    public abstract Map<NodeVO, List<Long>> dispatchTask(OpsJob job, OpsJobExecution execution, List<NodeVO> nodeList);

    /**
     * 创建调度及任务
     * @param job
     * @param nodeList
     * @return null，分配失败；size==0, 没有数据
     */
    public abstract Map<NodeVO, List<Long>> dispatchExistedTask(OpsJob job, OpsJobExecution execution,
                                                                List<OpsJobTask> taskList, List<NodeVO> nodeList);

    /**
     * 分配任务给节点
     * @param job 任务
     * @param execution 执行
     * @param assignResult 分配结果
     * @param failover 分配失败的任务，转移到其它可用的节点上
     */
    protected void assignTaskToExecutor(OpsJob job, OpsJobExecution execution,
                                        Map<NodeVO, List<Long>> assignResult, boolean failover) {
        String round = failover ? "1st Round#" : "2nd Round#";
        log.info("{}|扫描任务#{}, 执行编号#{}, 分配任务{}#{}|",
                job.getJobId(), job.getJobName(), execution.getExecId(), round, assignResult.size());
        List<NodeVO> deadNodes = new LinkedList<>();
        List<NodeVO> aliveNodes = new LinkedList<>();
        List<Long> failedTasks = new LinkedList<>();

        for(NodeVO node : assignResult.keySet()) {
            List<Long> taskIds = assignResult.get(node);
            boolean alive = remoteService.checkNodeAlive(node.getIp(), node.getPort(), REMOTE_ACCESS_REPEAT);
            if (!alive) {
                deadNodes.add(node);
                failedTasks.addAll(taskIds);
                continue;
            }
            aliveNodes.add(node);
            String nodeId = node.getNodeId();
            for(Long taskId : taskIds) {
                boolean done = remoteService.sendNewTask(node.getIp(), node.getPort(), taskId, REMOTE_ACCESS_REPEAT);
                if (done) {
                    this.taskService.assignTaskToNode(taskId, nodeId, ALLOC_DONE);
                } else {
                    failedTasks.add(taskId);
                }
            }
        }
        if (failedTasks.size() == 0) {
            log.info("{}|扫描任务#{}, 执行编号#{}, 分配成功, {}|",
                    job.getJobId(), job.getJobName(), execution.getExecId(), round);
            return;
        }
        log.error("{}|失败节点, {}#{}|",job.getJobId(), round, deadNodes.size());

        // 分配失败的逻辑
        if (failover && aliveNodes.size() > 0) {
            log.info("{}|任务分配#{},失败转移#{}, 可用节点{}|",job.getJobId(), round, failedTasks.size(), aliveNodes.size());
            Map<NodeVO, List<Long>> reAssignResult = randomBalanceStrategy.doBalance(failedTasks, aliveNodes);
            if (reAssignResult != null && reAssignResult.size() != 0) {
                this.assignTaskToExecutor(job, execution, reAssignResult, false);
            } else {
                log.info("{}|任务分配#{}，失败#{}|",job.getJobId(), round, failedTasks.size());
                updateFailedTask(failedTasks);
            }
        } else {
            log.info("{}|任务分配#{}，失败#{}|", job.getJobId(),round, failedTasks.size());
            updateFailedTask(failedTasks);
        }
    }

    /**
     * 设置分配失败的任务状态
     * @param tasks
     */
    private void updateFailedTask(List<Long> tasks) {
        for(Long taskId : tasks) {
            this.taskService.assignTaskToNode(taskId, null, ALLOC_FAIL);
        }
    }

    /**
     * 根据 name 解析 jobId
     * @param context
     * @return
     */
    protected OpsJob parseJob(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        if (jobDetail == null || jobDetail.getKey() == null || jobDetail.getKey().getName() == null) {
            log.error("error job#{}", jobDetail);
            return null;
        }
        // 用name 处理，group 用于分类
        String name = jobDetail.getKey().getName();
        if (!name.startsWith(SCAN_JOB_PREFIX)) {
            log.error("error job name#{}", name);
            return null;
        }
        Integer jobId = StringHelper.getIntValue(name.replace(SCAN_JOB_PREFIX, ""));
        if (jobId == null) {
            log.error("error job name#{}", name);
            return null;
        }
        OpsJob job = jobService.getById(jobId);
        if (job == null) {
            log.error("{}|scan job is null|", jobId);
            return null;
        }
        if (job.getEnable() == null || job.getEnable() == 0) {
            log.error("{}|scan job is disable, {}|", jobId, job);
            return null;
        }
        return job;
    }

    protected List<Long> convertToIds(List<OpsJobTask> taskList) {
        List<Long> taskIds = new ArrayList<>(taskList.size());
        for(OpsJobTask task : taskList) {
            taskIds.add(task.getTaskId());
        }
        return taskIds;
    }

}
