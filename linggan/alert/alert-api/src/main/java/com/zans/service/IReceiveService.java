package com.zans.service;

import com.zans.vo.AlertRuleStrategyReqVO;
import com.zans.vo.ApiResult;
import com.zans.vo.node.AlertRecordVO;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 处理业务类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/1
 */
public interface IReceiveService {


    ApiResult addRecord(AlertRecordVO alertRecordVO);

    ApiResult recoverRecord(AlertRecordVO alertRecordVO);

    ApiResult strategySave(AlertRuleStrategyReqVO reqVO);


	ApiResult changeStatus(AlertRuleStrategyReqVO reqVO);
}
