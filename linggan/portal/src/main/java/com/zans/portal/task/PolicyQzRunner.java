package com.zans.portal.task;


import com.zans.base.util.StringHelper;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.SysConstant;
import com.zans.portal.service.ISysConstantService;
import com.zans.portal.service.IVulTaskService;
import com.zans.portal.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zans.portal.config.GlobalConstants.OTHER_MISSION;

/**
 * create by: beiming
 * create time: 2021/10/13 18:32
 * 生成检疫区第三方漏扫任务队列
 */
@Component
@Slf4j
public class PolicyQzRunner implements CommandLineRunner {
    @Value("${spring.profiles.active}")
    String active;

    @Value("${other.qz.scan-minutes:5}")
    String otherQzScanMinutes;

    @Value("${other.qz.one-scan-minutes:1}")
    String otherQzOneScanMinutes;

    @Value("${other.qz.run-flag:0}")
    String qzRunFlag;

    @Value("${other.qz.sql-limit:60}")
    String sqlLimit;

    @Autowired
    ISysConstantService constantService;

    ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

    @Autowired
    IVulTaskService vulTaskService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        log.info("The following profiles are active:{}",active);
//        SysConstant constant = constantService.findConstantByKey(OTHER_MISSION);
//        if (constant ==null || constant.getConstantValue().equals("0")){
//            log.info("portal:other_mission   is null-------------------");
//            return;
//        }
//        generateQzOtherTask();
//        getQzOtherTaskRet();


    }

    private void generateQzOtherTask(){
        Runnable runnable = () ->{
            Integer limit = Integer.parseInt(sqlLimit);

            Integer policy = GlobalConstants.RADIUS_ACCESS_POLICY_QZ;
            List<String> list = vulTaskService.getVulTaskIpAddr(policy,limit);
            for (String ipAddr : list) {
                try {
//                    Thread.sleep(Long.parseLong(otherQzOneScanMinutes) *60*1000L);
                    Boolean b = vulTaskService.addVulTask(ipAddr,policy,Boolean.FALSE);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            log.info("generateQzOtherTask is finish...");
        };
        //下一次漏扫间隔2小时(可配置)开始新一轮循环。
        service.scheduleAtFixedRate(runnable, 1, Long.parseLong(otherQzScanMinutes), TimeUnit.SECONDS);
//        service.scheduleAtFixedRate(runnable, 1, 2, TimeUnit.MINUTES);
        log.info("generateQzOtherTask is running");

    }

    private void getQzOtherTaskRet() {
        Runnable runnable = () -> {
            vulTaskService.deleteRetGetCountMore();
            Integer policy = 1;
            try {
                vulTaskService.getVulTaskRet("4", policy);
            } catch (Exception e){
                e.printStackTrace();
            }

            for (int i = 0; i < 50; i++) {
                String jobId = redisUtil.leftPop(GlobalConstants.QZ_SUCCESS_QUEUE);
                if(StringHelper.isBlank(jobId)){
                    //如果队列无数据跳出
                    break;
                }

                try {
                    Boolean b = vulTaskService.getVulTaskRet(jobId, policy);
                    if (!b){
                        //没有获取到扫描漏洞结果,放入队尾,重新消费
                        redisUtil.lSet(GlobalConstants.QZ_SUCCESS_QUEUE, jobId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("getQzOtherTaskRet error#",e.getMessage());
                    //没有获取到扫描漏洞结果,放入队尾,重新消费
                    redisUtil.lSet(GlobalConstants.QZ_SUCCESS_QUEUE, jobId);
                }
            }
        };
        //下一次漏扫间隔2小时(可配置)开始新一轮循环。
        service.scheduleAtFixedRate(runnable, 1, 30, TimeUnit.SECONDS);
        log.info("getQzOtherTaskRet is running");

    }
}
