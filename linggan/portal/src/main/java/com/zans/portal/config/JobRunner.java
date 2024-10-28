package com.zans.portal.config;

import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.portal.dao.AssetBranchSnapShootMapper;
import com.zans.portal.service.IAssessService;
import com.zans.portal.service.IAssetBranchService;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.ISwitcherService;
import com.zans.portal.vo.asset.req.AssetBranchSnapShootReqVO;
import com.zans.portal.vo.asset.resp.AssetBranchSnapShootRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zans.portal.config.GlobalConstants.debugMap;

/**
 * @author pancm
 * @Title: portal
 * @Description: 定时任务执行
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/20
 */
@Component
@Slf4j
public class JobRunner implements CommandLineRunner {

    @Autowired
    private AssetBranchSnapShootMapper mapper;

    @Autowired
    IAssetBranchService assetBranchService;

    @Value("${spring.profiles.active}")
    String active;

    @Value("${api.snapshoot.flag:0}")
    String snapShootRunFlag;

    @Value("${assessJob.flag:1}")
    String assessJobFlag;

    private final static String VERSION_NAME = "version.txt";
    private long count;

    @Autowired
    ISwitcherService switchService;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    IAssessService assessService;

    ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    @Override
    public void run(String... args) throws Exception {
        log.info("The following profiles are active:{}", active);
        if ("1".equals(snapShootRunFlag)) {
            snapShootRunTask();
        }
        reSplitVlanInfo();
        clear();
        if("1".equals(assessJobFlag)){
            assessJob();
        }
        log.info("The program started successfully! Current Version:{}", FileHelper.readResourcesFile(VERSION_NAME));
    }




    private void clear() {
        Runnable runnable = () -> {
            if(!debugMap.isEmpty()){
                String time = debugMap.get(1);
                String now = DateHelper.getNow();
                if(now.compareTo(time)>-1){
                    debugMap.clear();
                    GlobalConstants.DEBUG_SCHEMA = 0;
                    log.info("已关闭调试模式!");
                }
            }
        };
        service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * @return void
     * @Author pancm
     * @Description 快照的定时任务执行器
     * @Date 2020/10/20
     * @Param []
     **/
    private void snapShootRunTask() {
        AssetBranchSnapShootReqVO assetBranchSnapShootReqVO = new AssetBranchSnapShootReqVO();
        assetBranchSnapShootReqVO.setEnable(1);
        Runnable runnable = () -> {
            try {
                List<AssetBranchSnapShootRespVO> respVOList = mapper.getAssetBranchSnapShoot(assetBranchSnapShootReqVO);
                if (respVOList == null || respVOList.size() == 0) {
                    if (count % 60 == 0) {
                        log.info("snapShootRunTask sleep");
                    }
                    count++;
                    return;
                }
                respVOList.forEach(res -> {
                    Integer execEnable = res.getExecEnable();
                    String time = res.getSnapShootTime();
                    String now = getNow();
                    AssetBranchSnapShootReqVO shootReqVO = new AssetBranchSnapShootReqVO();
                    shootReqVO.setId(res.getId());
                    if (now.compareTo(time) > -1 && execEnable == 0) {
                        shootReqVO.setExecStartTime(getNow());
                        mapper.update(shootReqVO);
                        //模拟执行计划

                        assetBranchService.statisticsAssetBranchAll(time);

                        //进行执行任务，执行完毕之后就更新状态和值
                        log.info("snapShootRunTask exec success");
                        shootReqVO.setExecEnable(1);
                        shootReqVO.setExecStartTime(null);
                        shootReqVO.setExecEndTime(getNow());
                        mapper.update(shootReqVO);
                    } else if (now.compareTo(time) < 0 && execEnable == 1) {
                        shootReqVO.setExecEnable(0);
                        mapper.update(shootReqVO);
                        log.info("snapShootRunTask setExecEnable(0) success");
                    }
                    if (count % 60 == 0) {
                        log.info("snapShootRunTask sleep");
                    }
                    count++;
                });

            } catch (Exception e) {
                log.error("snapShootRunTask error{}:", e);
            }
        };
        service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.MINUTES);
        log.info("snapShootRunTask is running");
    }

    private void reSplitVlanInfo() {
        service.schedule(() -> switchService.splitVlanInfoFromConfig(), 3, TimeUnit.DAYS);
    }

    /**
     * 考核任务执行计划任务
     */
    private void assessJob() {
        Runnable runnable = () -> {
            log.info("assessService.calculateByDay()");
            assessService.calculateByDay();
        };
        service.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.MINUTES);
    }

    private String getNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }


    /**
     * 登出处理
     */
    @Scheduled(cron = "0/15 * * * * ?")
    public void statisticalOffline() {
        logOperationService.statisticalOffline();
    }
}
