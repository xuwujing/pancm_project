package com.zans.mms.service.impl;

import com.zans.mms.dao.guard.OpsJobExecutionMapper;
import com.zans.mms.service.IOpsJobExecutionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qiyi
 * @Title: zans-demo
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/10
 */
@Service
public class OpsJobExecutionImpl implements IOpsJobExecutionImpl {

    @Autowired(required = false)
    private OpsJobExecutionMapper opsJobExecutionMapper;


    @Override
    public Integer count() {
        return opsJobExecutionMapper.count();
    }
}
