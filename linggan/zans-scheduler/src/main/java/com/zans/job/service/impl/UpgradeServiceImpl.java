package com.zans.job.service.impl;

import com.jcraft.jsch.JSchException;
import com.zans.base.util.SshClient;
import com.zans.base.util.StringHelper;
import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.model.OpsJobTask;
import com.zans.job.model.OpsNode;
import com.zans.job.service.IExecutionService;
import com.zans.job.service.IJobService;
import com.zans.job.service.IUpgradeService;
import com.zans.job.service.bean.BaseApp;
import com.zans.job.service.bean.RadApiApp;
import com.zans.job.service.bean.ScanApp;
import com.zans.job.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static com.zans.job.config.JobConstants.JOB_ID_UPGRADE;
import static com.zans.job.config.JobConstants.LINE_SEPARATOR;

/**
 * @author xv
 * @since 2020/5/14 19:47
 */
@Slf4j
@Service
public class UpgradeServiceImpl implements IUpgradeService {

    @Value("${upgrade.src.folder}")
    String srcFolder;

    @Autowired
    ScanApp scanApp;

    @Autowired
    RadApiApp radApiApp;

    @Autowired
    IJobService jobService;

    @Autowired
    IExecutionService executionService;

    @Override
    public BaseApp getAppByName(String appName) {
        log.info("app#{}, scan#{}",  appName, scanApp);
        if (scanApp.getId().equals(appName)) {
            return scanApp;
        } else if (radApiApp.getId().equals(appName)) {
            return radApiApp;
        } else {
            log.error("未知应用#{}", appName);
            return null;
        }
    }

    @Override
    public boolean upgrade(NodeVO node, BaseApp app, OpsJobTask task) {

        String appName = app.getId();
        String ip = node.getIp();
        Integer port = node.getSshPort();
        String account = "root";
        String password = node.getRootPassword();

        if (StringHelper.isBlank(srcFolder)) {
            String error = "srcFolder为空";
            log.error(error);
            task.setError(error);
            return false;
        }
        if (!srcFolder.endsWith("\\") && !srcFolder.endsWith("/")) {
            srcFolder = srcFolder + "/";
        }

        String srcPath = srcFolder + app.getFile();
        File file = new File(srcPath);
        if (!file.exists()) {
            String error = "src文件不存在#" + srcPath;
            log.error(error);
            task.setError(error);
            return false;
        }

        String destFolder = app.getFolder();
        if (StringHelper.isBlank(destFolder)) {
            String error = "app.folder为空";
            log.error(error);
            task.setError(error);
            return false;
        }


        if (!destFolder.endsWith("\\") && !destFolder.endsWith("/")) {
            destFolder = destFolder + "/";
        }
        String destPath = destFolder + app.getFile();

        String command = "";
        String resp = "";
        StringBuilder builder = new StringBuilder();
        try {

            command = "supervisorctl stop " + appName;
            this.execCommand(ip, account, password, port, command, builder);

            boolean r = this.execUpload(ip, account, password, port, srcPath, destPath, builder);
            if (!r) {
                task.setContent(builder.toString());
                task.setError("上传文件失败");
                return false;
            }

            command = "chmod +x " + destPath;
            this.execCommand(ip, account, password, port, command, builder);

            command = "supervisorctl start " + appName;
            this.execCommand(ip, account, password, port, command, builder);
            task.setContent(builder.toString());
            return true;
        } catch (Exception ex) {
            task.setContent(builder.toString());
            task.setError(StringHelper.getErrorInfoFromException(ex));
            log.error("command error#"+ command, ex);
        }
        return false;
    }

    @Override
    public Boolean execEnableCommand(String ip, String account, String password, int port, String appName, Integer enbale) throws IOException, JSchException {
        StringBuilder builder = new StringBuilder();

        String command = "";
        if (enbale == 1) {
            command = "supervisorctl start " + appName;
        } else {
            command = "supervisorctl stop " + appName;
        }
        this.execCommand(ip, account, password, port, command, builder);
        return true;
    }

    private void execCommand(String ip, String account, String password, int port, String command,
                             StringBuilder builder) throws JSchException, IOException {
        builder.append(command).append(LINE_SEPARATOR);
        String resp = SshClient.exeCommand(ip, account, password, port, command);
        builder.append(resp).append(LINE_SEPARATOR);
    }

    private boolean execUpload(String ip, String account, String password, int port, String srcPath, String destPath,
                             StringBuilder builder) throws JSchException, IOException {
        String command = "upload";
        builder.append(command).append(LINE_SEPARATOR);
        builder.append(srcPath).append(LINE_SEPARATOR);
        builder.append(destPath).append(LINE_SEPARATOR);
        boolean r = SshClient.upload(ip, account, password, port, srcPath, destPath);
        builder.append(r).append(LINE_SEPARATOR);
        return r;
    }

    @Override
    public OpsJobExecution createUpgradeExecution() {
        OpsJob job = jobService.getById(JOB_ID_UPGRADE);
        OpsJobExecution execution = new OpsJobExecution(job);
        executionService.save(execution);
        return execution;
    }
}
