package com.zans.task;

import com.zans.config.AsynConstants;
import com.zans.config.ExecuteStatusEnum;
import com.zans.config.SpringBeanFactory;
import com.zans.dao.AsynTaskRecordDao;
import com.zans.model.AsynTaskRecord;
import com.zans.netty.NettyChannelMap;
import com.zans.util.RedisUtil;
import com.zans.vo.AsynReqVo;
import com.zans.vo.AsynTaskRecordVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

import static com.zans.config.AsynConstants.STATE_HANDLER_SUC;

/**
 * @author beixing
 * @Title: asyn-server
 * @Description: queue队列处理
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/23
 */
@Slf4j
public class QueueTask extends Thread{

    private String queueName = "simple-queue";


    private RedisUtil redisUtil;

    private AsynTaskRecordDao asynTaskRecordDao;

    public QueueTask(String queueName){
        this.queueName = queueName;
        this.redisUtil = (RedisUtil) SpringBeanFactory.getBean("redisUtil");
        this.asynTaskRecordDao = (AsynTaskRecordDao) SpringBeanFactory.getBean("asynTaskRecordDao");
    }


    public void run() {
        log.info("消费队列:{} 任务启动成功!", queueName);
        while (AsynConstants.IS_ENABLED) {
            String taskId = null;
            try {
                taskId = redisUtil.leftPop(queueName);
                if (StringUtils.isEmpty(taskId)) {
                    TimeUnit.SECONDS.sleep(1);
                    continue;
                }
                AsynTaskRecordVo asynTaskRecordVo = asynTaskRecordDao.queryByTaskId(taskId);
                if (asynTaskRecordVo == null) {
                    log.warn("消费队列:{},taskId#:{},未配置！不执行!", queueName,taskId);
                    continue;
                }
                AsynTaskRecord asynTaskRecord = new AsynTaskRecord();
                asynTaskRecord.setTaskId(asynTaskRecordVo.getTaskId());
                asynTaskRecord.setExecuteStatus(ExecuteStatusEnum.RUNNING.getStatus());
                asynTaskRecord.setConsumerTime("");
                asynTaskRecordDao.update(asynTaskRecord);
                String name = asynTaskRecordVo.getTargetProjectId();
                Channel channel = NettyChannelMap.getChannelByName(name);
                if (channel != null && channel.isActive()) {
                    AsynReqVo.AsynReqVoMsg build = AsynReqVo.AsynReqVoMsg.newBuilder()
                            .setState(STATE_HANDLER_SUC)
                            .setTaskId(taskId)
                            .setData(asynTaskRecordVo.getReqData())
                            .build();
                    channel.writeAndFlush(build);
                    log.info("消费队列:{},taskId:{} 发送给：{} 成功!", queueName,taskId, name);
                } else {
                    String failReason = String.format("name:%s,还未启动，taskId:%s,后续需要重发!", name, taskId);
                    log.warn(failReason);
                    updateFailData(taskId, failReason);
                }
            } catch (Exception e) {
                log.error("处理失败！", e);
                updateFailData(taskId, e.getMessage());
            }
        }

    }

    private void updateFailData(String taskId, String failReason) {
        AsynTaskRecord asynTaskRecord = new AsynTaskRecord();
        asynTaskRecord.setTaskId(taskId);
        asynTaskRecord.setRetryCount(1);
        asynTaskRecord.setFailTime("");
        asynTaskRecord.setFailReason(failReason);
        asynTaskRecord.setExecuteStatus(ExecuteStatusEnum.FAIL.getStatus());
        asynTaskRecordDao.update(asynTaskRecord);
    }


}
