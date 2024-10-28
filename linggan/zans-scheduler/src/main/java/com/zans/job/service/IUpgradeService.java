package com.zans.job.service;

import com.jcraft.jsch.JSchException;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.model.OpsJobTask;
import com.zans.job.model.OpsNode;
import com.zans.job.service.bean.BaseApp;
import com.zans.job.vo.node.NodeVO;

import java.io.IOException;

/**
 * 升级程序
 * @author xv
 * @since 2020/5/14 19:45
 */
public interface IUpgradeService {

    /**
     * 新建一次任务执行
     * @return
     */
    OpsJobExecution createUpgradeExecution();

    /**
     * 升级程序
     * @param node
     * @param app
     * @param task
     * @return
     */
    boolean upgrade(NodeVO node, BaseApp app, OpsJobTask task);

    /**
     * 验证app 是否合法
     * @param appName
     * @return
     */
    BaseApp getAppByName(String appName);

    Boolean execEnableCommand(String ip, String account, String password, int port, String appName, Integer enbale) throws IOException, JSchException;

}
