package com.zans.job.service.impl;

import com.zans.base.util.MapBuilder;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.model.OpsJobTask;
import com.zans.job.model.OpsNode;
import com.zans.job.service.*;
import com.zans.job.service.bean.BaseApp;
import com.zans.job.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xv
 * @since 2020/5/12 17:13
 */
@Service
@Slf4j

public class AsyncServiceImpl implements IAsyncService {

    @Autowired
    ITaskService taskService;

    @Autowired
    IExecutionService executionService;

    @Autowired
    IRemoteService remoteService;

    @Autowired
    IUpgradeService upgradeService;

    @Async
    @Override
    public void sendUnFinishedTaskToNode(OpsNode node) {
        log.info("async sendUnFinishedTaskToNode");
        List<OpsJobTask> taskList = taskService.getRunningTasksOfNode(node.getNodeId());
        String ip = node.getIp();
        Integer port = node.getPort();
        for(OpsJobTask task : taskList) {
            Long taskId = task.getTaskId();
            boolean r = remoteService.sendNewTask(ip, port, taskId, 1);
            log.info("async {}:{}.{}#{}", ip, port, taskId, r);
        }
        log.info("async sendUnFinishedTaskToNode done#{}", taskList.size());
    }

    @Async
    @Override
    public void upgradeApp(List<NodeVO> nodeList, BaseApp app) {
        OpsJobExecution execution = upgradeService.createUpgradeExecution();
        log.info("升级应用#{}, 执行编号#{}， 节点#{}", app.getId(), execution.getExecId(), nodeList.size());
        for (NodeVO node : nodeList) {
            OpsJobTask task = new OpsJobTask(execution);
            task.doStart();
            task.doAlloc(node.getNodeId());
            // 设置任务的输入参数
            String params = MapBuilder.getBuilder().put("node", node).put("app", app).toJsonString();
            task.setParams(params);

            taskService.save(task);

            log.info("升级应用#{}, 节点#{} ......", app.getId(), node.getNodeId());
            boolean done = upgradeService.upgrade(node, app, task);
            if (done) {
                task.setFinishStatus();
            } else {
                task.setFailStatus();
            }
            log.info("升级应用#{}, 节点#{} {}", app.getId(), node.getNodeId(), (done ? "成功" : "失败"));
            taskService.update(task);
        }
        executionService.checkExecution(execution.getExecId());
    }
}
