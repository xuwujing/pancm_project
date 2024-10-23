package com.zans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zans.dao.AsynConfigDao;
import com.zans.dao.AsynTaskRecordDao;
import com.zans.model.AsynTaskRecord;
import com.zans.service.IAsynDealService;
import com.zans.vo.ApiResult;
import com.zans.vo.AsynTaskRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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



    @Override
    public ApiResult queryById(String taskId) {
        AsynTaskRecordVo asynTaskRecordVo =asynTaskRecordDao.queryByTaskId(taskId);
        return ApiResult.success(asynTaskRecordVo);
    }

    @Override
    public ApiResult query() {
        List<AsynTaskRecord> taskRecordList = asynTaskRecordDao.queryAll(new AsynTaskRecord());
        return ApiResult.success(taskRecordList);
    }

    @Override
    public ApiResult statistics() {
        JSONObject ref = new JSONObject();
        Map<String,Integer> statistics = asynTaskRecordDao.statistics();
        List<Map<String,Integer>> executorTime = asynTaskRecordDao.executorTime();

        ref.put("total",statistics);
        ref.put("time",executorTime);

        return ApiResult.success(ref);
    }

    @Override
    public ApiResult statisticsNowDay() {
        JSONObject ref = new JSONObject();
        Map<String,Integer> statistics = asynTaskRecordDao.statisticsNowDay();
        List<Map<String,Integer>> executorTime = asynTaskRecordDao.executorTimeByNowDay();
        ref.put("total",statistics);
        ref.put("time",executorTime);
        return ApiResult.success(ref);
    }

    private String getTaskId() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }


}
