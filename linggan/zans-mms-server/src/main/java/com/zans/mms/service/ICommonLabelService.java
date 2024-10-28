package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.model.CommonLabel;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/13
 */
public interface ICommonLabelService {
	ApiResult create(CommonLabel commonLabel);

	ApiResult update(CommonLabel commonLabel);

	ApiResult delete(CommonLabel commonLabel);

	ApiResult list(CommonLabel commonLabel);

}
