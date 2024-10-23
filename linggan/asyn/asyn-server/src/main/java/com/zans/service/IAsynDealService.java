package com.zans.service;

import com.zans.vo.ApiResult;

/**
 * @author beixing
 * @Title: asyn-client
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/22
 */
public interface IAsynDealService {



    ApiResult queryById(String taskId);

    ApiResult query();

    ApiResult statistics();

    ApiResult statisticsNowDay();

}
