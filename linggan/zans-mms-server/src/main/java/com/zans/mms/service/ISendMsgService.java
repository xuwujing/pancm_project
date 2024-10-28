package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.vo.push.DropLine;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/10
 */
public interface ISendMsgService{

	ApiResult sendCommonMsg(DropLine dropLine);


}
