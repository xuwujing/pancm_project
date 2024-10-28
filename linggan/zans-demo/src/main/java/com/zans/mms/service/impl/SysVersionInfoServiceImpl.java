package com.zans.mms.service.impl;

import com.zans.base.util.HttpClientUtil;
import com.zans.base.util.SshClient;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.guard.SysVersionInfoDao;
import com.zans.mms.model.SysVersionInfo;
import com.zans.mms.service.IRadiusEndPointService;
import com.zans.mms.service.ISysVersionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.zans.base.constant.SystemConstant.*;
import static com.zans.mms.util.SshTest.execShell;

@Slf4j
@Service
public class SysVersionInfoServiceImpl implements ISysVersionInfoService {

    @Autowired
    SysVersionInfoDao sysVersionInfoDao;

    @Autowired
    IRadiusEndPointService radiusEndPointService;

    ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public List<SysVersionInfo> getVerifyResult() {
        return sysVersionInfoDao.queryAllInfo();
    }

    @Override
    public void reVerify() {
        sysVersionInfoDao.resetVerifySuccess();
        List<SysVersionInfo> all = sysVersionInfoDao.queryAllInfo();
        for (SysVersionInfo info : all) {
            int verify = 0;
            if (verifyApp(info.getServerUrl())) {
                verify = 1;
            }
            sysVersionInfoDao.updateVerifySuccess(info.getId(), verify);
        }
    }

    private boolean verifyApp(String url) {
        boolean ok = false;
        if (StringUtils.hasText(url)) {
            try {
                String body = HttpClientUtil.get(url);
                ok = body.contains("version");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ok;
    }

    ///2021-08-09 暂不用 停止单个或多个服务 后续确认再删除
//    @Override
//    public void stopAndStartService(List<SysServerVO> serverVOList) {
//        if (serverVOList == null || serverVOList.size() == 0) {
//            return;
//        }
//        for (SysServerVO sysServerVO : serverVOList) {
//            SysVersionInfo info = sysVersionInfoDao.findByIpProjectName(sysServerVO.getServerIp(), sysServerVO.getProjectName());
//            String[] commands = this.jointCommands(info.getProjectName());
//            String execShell = SshClient.execShell(info.getServerIp(), info.getServiceAccount(), info.getServicePassword(),
//                    Integer.valueOf(info.getServicePort()), commands, true);
//            log.info("stopAndStartService execShell#{}", execShell);
//        }
//
//    }

    @Override
    public void stopAndStartAllService() {
        List<SysVersionInfo> infoList = sysVersionInfoDao.queryAllServerIp();
        if (infoList == null || infoList.size() == 0) {
            return;
        }
        List<SysVersionInfo> newInfoList = infoList.stream().sorted(Comparator.comparing(SysVersionInfo::getServerIp).reversed()).collect(Collectors.toList());
        //2.4最后shutdown
        for (SysVersionInfo info : newInfoList) {
            String shellResult = "";
            log.info(info.getServerIp() + "begin");
            if ("192.168.6.14".equals(info.getServerIp())) {
                //将所有supervisorctl管理的除zans-demo的进程停止
                shutdownHost(info);
            } else {
                String[] commands = this.jointCommands(null);
                shellResult = SshClient.execShell(info.getServerIp(), info.getServiceAccount(), info.getServicePassword(),
                        Integer.valueOf(info.getServicePort()), commands, true);
            }
            log.info(info.getServerIp() + " stopAllService shellResult#{}", shellResult);
        }
    }

    @Override
    public ApiResult stopAndStartGuacamole(String status) {
        String guacamoleStatus = "";
        if (!StringUtils.isEmpty(status)){
            guacamoleStatus = status;
        }else {
            guacamoleStatus = "on".equals(checkStatus()) ? "off" : "on";
        }
        Integer port = 22;

        String[] configCommands = {
                "vim-cmd vmsvc/power." + guacamoleStatus + " 15", "exit"};//15     guacamo-2.20   [datastore2] guacamo-2.20/guacamo-2.20.vmx
        try {
            String msg = execShell(HOST, USERNAME, PASSWORD, port, configCommands, true);
            log.info("msg:{}", msg);
            return ApiResult.success("guacamole->" + ("on".equals(guacamoleStatus) ? "开启" : "关闭") + "成功");
        } catch (Exception e) {
            log.error("socket连接失败-error:{}", e);
            return ApiResult.error("socket连接失败");
        }
    }

    @Override
    public ApiResult trigToGuacamole() {
        List<SysVersionInfo> infoList = sysVersionInfoDao.queryAllServerIp();
        List<SysVersionInfo> collect = infoList.stream().sorted(Comparator.comparing(SysVersionInfo::getServerIp)).collect(Collectors.toList());
        Set<String> stopProgram = getStopProgram(collect);
        log.info("stop program:{}",stopProgram);
        stopProgram(stopProgram, collect);
        return ApiResult.success("切换中！此过程大概需要5秒");
    }

    public String checkStatus() {
        Integer port = 22;
        //        String[] configCommands = {
//                "vim-cmd vmsvc/getallvms"};
        String[] configCommands = {
                "vim-cmd vmsvc/power.getstate 15", "exit"};//15     guacamo-2.20   [datastore2] guacamo-2.20/guacamo-2.20.vmx
        //vim-cmd vmsvc/power.off 15
//        String[] configCommands = {
//                "vim-cmd vmsvc/getallvms"};
        String msg = "";
        try {
            msg = execShell(HOST, USERNAME, PASSWORD, port, configCommands, true);
        } catch (Exception e) {
            log.error("socket连接失败-error:{}", e);
            e.printStackTrace();
        }
        String[] split = msg.split("\n");
        for (String s : split) {
            if (s.startsWith("Powered")) {
                String[] split1 = s.split("\\s+");
                return split1[1];
            }
        }
        return null;
    }

    private String[] jointCommands(String projectName) {
        if (StringHelper.isBlank(projectName)) {
            String[] commands = {"systemctl stop redis", "service mysqld stop", "supervisorctl stop all", "shutdown -s -t 10",
                    "exit "};
            log.info("projectName is blank and commands is {}", commands);
            return commands;
        }
        String[] commands = {"systemctl stop redis", "service mysqld stop", "supervisorctl stop " + projectName, "shutdown -s -t 10",
                "exit "};
        log.info("jointCommands   commands#{}", commands);
        return commands;
    }

    private void shutdownHost(SysVersionInfo info) {
        String shellResult = "";
        //将所有supervisorctl管理的除zans-demo的进程停止
        String[] SupStatusCommands = {"supervisorctl status", "exit "};
        String SupStatusResult = SshClient.execShell(info.getServerIp(), info.getServiceAccount(), info.getServicePassword(),
                Integer.valueOf(info.getServicePort()), SupStatusCommands, true);
        String[] split = SupStatusResult.split("\n");
        Map<String, String> taskMap = new HashMap<>();
        first:
        for (int i = 0; i < split.length; i++) {
            if (split[i].trim().startsWith("[root") && split[i].trim().endsWith("supervisorctl status")) {
                for (int i1 = i; i1 < split.length; i1++) {
                    String[] task = split[i1 + 1].split("\\s+");
                    if (split[i1 + 1].startsWith("[root")) {
                        break first;
                    }
                    //要停止的服务中排除自己
                    if (!"zans-demo".equals(task[0])) {
                        taskMap.put("supervisorctl stop " + task[0], task[1]);
                    }
                }
            }
        }
        String[] BaseCommands = {"systemctl stop redis", "service mysqld stop", "exit "};
        String[] supCommands = taskMap.keySet().toArray(new String[taskMap.size() + 1]);
        supCommands[taskMap.size()] = "exit ";

        String[] endCommands = {"shutdown -s -t 10", "exit "};
        //逻辑 先停止除了zans-demo之外所有的supervisor 再执行初始化数据 再停止redis mysql  最后shutdown
        shellResult = SshClient.execShell(info.getServerIp(), info.getServiceAccount(), info.getServicePassword(),
                Integer.valueOf(info.getServicePort()), supCommands, true);
        log.info(info.getServerIp() + "->stopAllService shellResult->stop supervisor#{}", shellResult);
        radiusEndPointService.initData();
        log.info(info.getServerIp() + "->初始化数据");
        shellResult = SshClient.execShell(info.getServerIp(), info.getServiceAccount(), info.getServicePassword(),
                Integer.valueOf(info.getServicePort()), BaseCommands, true);
        log.info(info.getServerIp() + "->stopAllService shellResult->stop Redis MySql#{}", shellResult);
        log.info(info.getServerIp() + "->stopAllService shellResult->ready to shutdown#{}", shellResult);
        //最后一步异步执行
        executorService.execute(() -> {
            SshClient.execShell(info.getServerIp(), info.getServiceAccount(), info.getServicePassword(),
                    Integer.valueOf(info.getServicePort()), endCommands, true);
        });
    }

    public Set<String> getStopProgram(List<SysVersionInfo> infoList) {
        for (SysVersionInfo info : infoList) {
            if ("192.168.6.14".equals(info.getServerIp())) {
                //将所有supervisorctl管理的除zans-demo的进程停止
                String[] SupStatusCommands = {"supervisorctl status", "exit "};
                String SupStatusResult = SshClient.execShell(info.getServerIp(), info.getServiceAccount(), info.getServicePassword(),
                        Integer.valueOf(info.getServicePort()), SupStatusCommands, true);
                String[] split = SupStatusResult.split("\n");
                Map<String, String> taskMap = new HashMap<>();
                Set<String> set = new HashSet<>();
                source:
                for (int i = 0; i < split.length; i++) {
                    if (split[i].trim().startsWith("[root") && split[i].trim().endsWith("supervisorctl status")) {
                        for (int j = i + 1; j < split.length; j++) {
                            if (split[j].contains("pid")) {
                                String[] data = split[j].split("\\s+");
                                if ("RUNNING".equals(data[1]) && !"zans-demo".equals(data[0])) {
                                    set.add("supervisorctl stop " + data[0]);
                                }
                            }
                            if (split[j].startsWith("[root")) {
                                break source;
                            }
                        }
                    }
                }
                return set;
            }
        }
        return new HashSet();
    }

    public void stopProgram(Set<String> stopProgram, List<SysVersionInfo> info) {
        stopProgram.add("systemctl stop redis mysqld");//2.4 关闭mysql redis
        stopProgram.add("exit");
        List<String> data = new ArrayList<>();
        for (SysVersionInfo sysVersionInfo : info) {
            if ("192.168.6.14".equals(sysVersionInfo.getServerIp())){
                data = new ArrayList<>(stopProgram);
            }else if ("192.168.6.16".equals(sysVersionInfo.getServerIp())){
                Collections.addAll(data,"supervisorctl stop all","systemctl stop redis mysqld nginx","exit");
            }else {
                Collections.addAll(data,"supervisorctl stop all","systemctl stop nginx","exit");//2.5只有一个nginx
            }
            String[] objects = data.toArray(new String[]{});
            executorService.execute(()->{
            String SupStatusResult = SshClient.execShell(sysVersionInfo.getServerIp(), sysVersionInfo.getServiceAccount(), sysVersionInfo.getServicePassword(),
                    Integer.valueOf(sysVersionInfo.getServicePort()), objects, true);
            log.info("stopProgram:{}",SupStatusResult);
        });
        }
    }

    public static void main(String[] args) {
        //将所有supervisorctl管理的除zans-demo的进程停止
        String[] SupStatusCommands = {"supervisorctl status", "exit "};
        String SupStatusResult = SshClient.execShell("192.168.6.14", "root", "Admin#12$34!",
                22, SupStatusCommands, true);
        String[] split = SupStatusResult.split("\n");
        Map<String, String> taskMap = new HashMap<>();
        Set<String> set = new HashSet<>();
        source:
        for (int i = 0; i < split.length; i++) {
            if (split[i].trim().startsWith("[root") && split[i].trim().endsWith("supervisorctl status")) {
                for (int j = i + 1; j < split.length; j++) {
                    if (split[j].contains("pid")) {
                        String[] data = split[j].split("\\s+");
                        if ("RUNNING".equals(data[1]) && !"zans-demo".equals(data[0])) {
                            set.add("supervisorctl stop " + data[0]);
                        }
                    }
                    if (split[j].startsWith("[root")) {
                        break source;
                    }
                }
            }
        }
        System.out.println(set);
    }
}
