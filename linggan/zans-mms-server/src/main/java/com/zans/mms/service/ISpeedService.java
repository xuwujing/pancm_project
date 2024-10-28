package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.model.Speed;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:上传进度服务
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/27
 */
public interface ISpeedService {
	ApiResult getById(Long id);

	void insertOne(Speed speed);
}
