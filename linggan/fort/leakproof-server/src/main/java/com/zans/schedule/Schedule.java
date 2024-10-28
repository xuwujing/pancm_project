package com.zans.schedule;

import com.zans.dao.FortReserveMapper;
import com.zans.model.FortReserve;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import static com.zans.constant.SystemConstant.*;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/3
 */
@Configuration
@EnableScheduling
@Slf4j
public class Schedule {

    @Autowired
    private FortReserveMapper fortReserveMapper;

    /*
    改为在websocketTunnel初始化
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("poolScheduler");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    private static ScheduledFuture<?> future;


    /**
     * 8-23点的每小时检测是否有待审批的过期
     */
    @Scheduled(cron = "0 0 9-23 * * ?")
    public void rejectOutTimeReserve(){
        ExecutorService exec = Executors.newFixedThreadPool(3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FortReserve fortReserve = new FortReserve();
        fortReserve.setStatus(APPLYING);
        List<FortReserve> select = fortReserveMapper.select(fortReserve);
        for (FortReserve reserve : select) {
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        if (reserve.getEndTime().compareTo(simpleDateFormat.format(new Date()))<=0){
                            reserve.setStatus(VETO_APPLY);
                            reserve.setApprover(SYSTEM);
                            reserve.setApproveReason("预约时间已到,管理员未审批,系统自动驳回");
                            reserve.setApproveTime(simpleDateFormat.format(new Date()));
                            fortReserveMapper.updateByPrimaryKeySelective(reserve);
                        }
                    } catch (Exception e) {
                        log.info("scheduled,error:{}",e);
                    }
                }
            };
            exec.execute(run);
        }
        exec.shutdown();
    }

}
