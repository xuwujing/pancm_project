package com.zans.job.service;

import com.zans.job.model.OpsJobTask;

import java.util.List;

/**
 * @author xv
 * @since 2020/5/7 18:46
 */
public interface IRemoteService {

    /**
     * 检查节点是否有响应
     * @param ip
     * @param port
     * @param repeat
     * @return
     */
    boolean checkNodeAlive(String ip, Integer port, int repeat);

    /**
     * 发送新任务
     * @param ip
     * @param port
     * @param taskId
     * @param repeat
     * @return
     */
    boolean sendNewTask(String ip, Integer port, Long taskId, int repeat);

    /**
     * 调用客户端的 sharding方法
     * @param ip
     * @param port
     * @param executionId
     * @param repeat
     * @return
     */
    boolean doSharding(String ip, Integer port, Long executionId, int repeat);

    boolean test(String ip, Integer port, int second);
}
