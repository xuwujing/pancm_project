package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.model.Gear;
import com.zans.mms.vo.gear.GearReqVO;

/**
 * @author qitian
 * @Title: zans-demo
 * @Description:一机一档逻辑控制层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/29
 */
public interface IGearService {

	/**
	 * 一机一档列表
	 * @param req
	 * @return
	 */
	ApiResult list(GearReqVO req);

	void saveOrUpdate(Gear gear);

	Gear getById(Long id);

	void delete(Long id);
}
