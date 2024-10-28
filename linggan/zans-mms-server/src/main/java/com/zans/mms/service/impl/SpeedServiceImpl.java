package com.zans.mms.service.impl;

import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.SpeedDao;
import com.zans.mms.model.Speed;
import com.zans.mms.service.ISpeedService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:上传进度逻辑层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/27
 */
@Service("speedService")
public class SpeedServiceImpl implements ISpeedService {

	@Resource
	private SpeedDao speedDao;

	@Override
	public ApiResult getById(Long id) {
		return ApiResult.success(speedDao.selectByPrimaryKey(id));
	}

	@Override
	public void insertOne(Speed speed) {
		speedDao.insert(speed);
	}
}
