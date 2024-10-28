package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.vo.segment.SegmentVO;

/**
 * @author qitian
 * @Title: portal
 * @Description:ip地址池管理业务层接口
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
public interface INetworkIpSegmentService {
	ApiResult ipSegmentList();

	ApiResult ipSegmentView(String ip);
}
