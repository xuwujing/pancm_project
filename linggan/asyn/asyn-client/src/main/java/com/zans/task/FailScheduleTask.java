package com.zans.task;

import com.zans.config.ExecuteStatusEnum;
import com.zans.dao.AsynTaskRecordDao;
import com.zans.model.AsynTaskRecord;
import com.zans.util.MyTools;
import com.zans.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author beixing
 * @Title: FailScheduleTask
 * @Description: 查询失败的数据，丢进失败队列运行
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/12/1
 **/
@Configuration
@EnableScheduling
@Slf4j
public class FailScheduleTask {

    @Resource
    private AsynTaskRecordDao asynTaskRecordDao;

    @Autowired
    private RedisUtil redisUtil;

    private  String queueName ="fail-queue";

    @Scheduled(cron = "0/30 * * * * ?")
    private void configureTasks() {
        AsynTaskRecord asynTaskRecord = new AsynTaskRecord();
        asynTaskRecord.setExecuteStatus(ExecuteStatusEnum.FAIL.getStatus());
        List<AsynTaskRecord> taskRecordList = asynTaskRecordDao.queryAll(asynTaskRecord);
        if (MyTools.isEmpty(taskRecordList)) {
            return;
        }
        for (AsynTaskRecord taskRecord : taskRecordList) {
            String taskId = taskRecord.getTaskId();
            redisUtil.leftPush(queueName,taskId);
            log.info("taskId:{},已写入失败队列!",taskId);
        }

    }

}
