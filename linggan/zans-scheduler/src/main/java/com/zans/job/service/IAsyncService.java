package com.zans.job.service;

import com.zans.job.model.OpsNode;
import com.zans.job.service.bean.BaseApp;
import com.zans.job.vo.node.NodeVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xv
 * @since 2020/5/12 17:10
 */

public interface IAsyncService {

    /**
     * 恢复节点上的所有未完成的任务
     * @param node
     */
    void sendUnFinishedTaskToNode(OpsNode node);

    /**
     * 升级程序
     * @param nodeList
     * @param app
     */
    void upgradeApp(List<NodeVO> nodeList, BaseApp app);
}
