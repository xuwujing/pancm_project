package com.zans.job.api;

import com.zans.job.config.JobConstants;
import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.model.OpsJobTask;
import com.zans.job.service.INodeService;
import com.zans.job.service.IRemoteService;
import com.zans.job.strategy.IBalanceStrategy;
import com.zans.job.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.zans.job.config.JobConstants.REMOTE_ACCESS_REPEAT;

/**
 * @author xv
 * @since 2020/5/9 2:58
 */
@Slf4j
@Component("ShardingScanJob")
public class ShardingScanJob extends ScanJob {

    @Resource(name = "weightRoundRobin")
    IBalanceStrategy balanceStrategy;

    @Autowired
    IRemoteService remoteService;

    @Autowired
    INodeService nodeService;

    @Override
    public Map<NodeVO, List<Long>> dispatchTask(OpsJob job, OpsJobExecution execution, List<NodeVO> nodeList) {
        // 选出 master节点
        if (nodeList == null) {
            log.error("{}|没有可用的节点|",job.getJobId());
            return null;
        }

        NodeVO shardingNode = null;
        List<NodeVO> shardingNodes = nodeService.getDispatchNodes(JobConstants.PRIORITY_ZERO,job.getJobType());
        for(NodeVO vo : nodeList) {
            boolean alive = remoteService.checkNodeAlive(vo.getIp(), vo.getPort(), REMOTE_ACCESS_REPEAT);
            if (alive) {
                shardingNode = vo;
                break;
            }
        }
        if (shardingNode == null) {
            for(NodeVO vo : nodeList) {
                boolean alive = remoteService.checkNodeAlive(vo.getIp(), vo.getPort(), REMOTE_ACCESS_REPEAT);
                if (alive) {
                    shardingNode = vo;
                    break;
                }
            }
        }

        if (shardingNode == null) {
            log.error("{}|没有可用的节点|",job.getJobId());
            return null;
        }

        boolean done = remoteService.doSharding(shardingNode.getIp(), shardingNode.getPort(), execution.getExecId(), REMOTE_ACCESS_REPEAT);
        if (!done) {
            log.error("{}|请求分片失败，{}:{}, 执行ID#{}|",job.getJobId(), shardingNode.getIp(), shardingNode.getPort(), execution.getExecId());
            return null;
        }

        // 加载 Task
        List<OpsJobTask> taskList = taskService.getTasksOfExecution(execution.getExecId());

        log.info("{}|请求分片结果#{}, {}:{}, 执行ID#{}|",job.getJobId(), taskList.size(),
                shardingNode.getIp(), shardingNode.getPort(), execution.getExecId());


        List<Long> taskIds = this.convertToIds(taskList);
        execution.setShardingNum(taskList.size());
        Map<NodeVO, List<Long>> result = balanceStrategy.doBalance(taskIds, nodeList);
        return result;
    }

    @Override
    public Map<NodeVO, List<Long>> dispatchExistedTask(OpsJob job, OpsJobExecution execution, List<OpsJobTask> taskList, List<NodeVO> nodeList) {
        // 选出 master节点
        if (nodeList == null) {
            log.error("{}|没有可用的节点|",job.getJobId());
            return null;
        }
        List<Long> taskIds = this.convertToIds(taskList);
        Map<NodeVO, List<Long>> result = balanceStrategy.doBalance(taskIds, nodeList);
        return result;
    }
}