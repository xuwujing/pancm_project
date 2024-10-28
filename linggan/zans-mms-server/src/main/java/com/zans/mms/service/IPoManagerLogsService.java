package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.mms.model.Asset;
import com.zans.mms.model.PoManagerLogs;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description: 舆情日志
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/4
 */
public interface IPoManagerLogsService extends BaseService<PoManagerLogs> {

	void insertOne(PoManagerLogs poManagerLogs);
}
