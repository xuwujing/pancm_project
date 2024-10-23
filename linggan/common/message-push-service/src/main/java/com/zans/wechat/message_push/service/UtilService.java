package com.zans.wechat.message_push.service;

import com.zans.wechat.message_push.vo.ApiResult;

public interface UtilService {
    ApiResult getSessionKey(String appid, String js_code);
}
