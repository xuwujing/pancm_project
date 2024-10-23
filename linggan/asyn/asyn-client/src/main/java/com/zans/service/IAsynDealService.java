package com.zans.service;

import com.zans.vo.ApiResult;
import com.zans.vo.AsynRecordReqVO;

/**
 * @author beixing
 * @Title: asyn-client
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/22
 */
public interface IAsynDealService {


    ApiResult receive(AsynRecordReqVO asynRecordReqVO);


}
