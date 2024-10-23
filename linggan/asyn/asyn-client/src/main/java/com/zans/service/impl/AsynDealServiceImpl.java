package com.zans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zans.dao.AsynConfigDao;
import com.zans.dao.AsynTaskRecordDao;
import com.zans.model.AsynConfig;
import com.zans.model.AsynTaskRecord;
import com.zans.service.IAsynDealService;
import com.zans.util.RedisUtil;
import com.zans.vo.ApiResult;
import com.zans.vo.AsynRecordReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author beixing
 * @Title: asyn-client
 * @Description: 异步处理接口
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/22
 */
@Service
@Slf4j
public class AsynDealServiceImpl implements IAsynDealService {
    @Resource
    private AsynConfigDao asynConfigDao;

    @Resource
    private AsynTaskRecordDao asynTaskRecordDao;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public ApiResult receive(AsynRecordReqVO asynRecordReqVO) {
        String taskId = getTaskId();
        log.info("taskId:{},请求参数:{}",taskId,asynRecordReqVO);
        String projectId = asynRecordReqVO.getTargetProjectId();
        AsynConfig asynConfig = asynConfigDao.queryByProjectId(projectId);
        if(asynConfig == null){
            return ApiResult.error("该项目编号未配置！");
        }
        String targetProjectId = asynRecordReqVO.getTargetProjectId();
        String queueName = asynConfig.getQueueName();
        redisUtil.leftPush(queueName,taskId);
        AsynTaskRecord asynTaskRecord = new AsynTaskRecord();
        asynTaskRecord.setTaskId(taskId);
        asynTaskRecord.setSourceProjectId(asynRecordReqVO.getSourceProjectId());
        asynTaskRecord.setTargetProjectId(targetProjectId);
        asynTaskRecord.setReqData(asynRecordReqVO.toString());
        asynTaskRecord.setQueueName(queueName);
        asynTaskRecordDao.insert(asynTaskRecord);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId",taskId);
        log.info("taskId:{},已写入队列!",taskId);
        return ApiResult.success(jsonObject);
    }

    private String getTaskId() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }


}
