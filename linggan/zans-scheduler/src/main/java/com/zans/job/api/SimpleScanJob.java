package com.zans.job.api;

import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.model.OpsJobTask;
import com.zans.job.strategy.IBalanceStrategy;
import com.zans.job.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 不分片，随机分配
 * @author xv
 * @since 2020/5/8 18:45
 */
@Slf4j
@Component("SimpleScanJob")
public class SimpleScanJob extends ScanJob {

    @Resource(name = "randomBalance")
    IBalanceStrategy balanceStrategy;

    @Override
    public Map<NodeVO, List<Long>> dispatchTask(OpsJob job, OpsJobExecution execution, List<NodeVO> nodeList) {
        OpsJobTask task = new OpsJobTask(execution);
        this.taskService.save(task);

        Map<NodeVO, List<Long>> result = balanceStrategy.doBalance(Arrays.asList(task.getTaskId()), nodeList);
        return result;
    }

    @Override
    public Map<NodeVO, List<Long>> dispatchExistedTask(OpsJob job, OpsJobExecution execution, List<OpsJobTask> taskList, List<NodeVO> nodeList) {
        List<Long> idList = this.convertToIds(taskList);
        Map<NodeVO, List<Long>> result = balanceStrategy.doBalance(idList, nodeList);
        return result;
    }
}
