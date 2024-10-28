package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.vo.wechat.WeChatPushReqVO;

/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description: 微信请求接口
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/3/11
 */
public interface IWeChatReqService {


    ApiResult setConfig();



    String getSessionKey(String jsCode);


    String decryptMsg(String encryptedData,String sessionKey,String iv);



    ApiResult weChatPush(WeChatPushReqVO weChatPushReqVO);

    ApiResult weChatPushWorkFLow(WeChatPushReqVO weChatPushReqVO);

}
