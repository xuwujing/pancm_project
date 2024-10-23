package com.zans.service;

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

    /**
     * @Author pancm
     * @Description 根据taskID进行处理
     * @Date  2020/9/3
     * @Param [taskId]
     * @return com.zans.vo.ApiResult
     **/
    ApiResult addTask(int taskId);


    ApiResult record(AlertRecordVO alertRecordVO);
}
