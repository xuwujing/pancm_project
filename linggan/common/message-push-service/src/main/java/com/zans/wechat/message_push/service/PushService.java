package com.zans.wechat.message_push.service;

import com.zans.wechat.message_push.vo.ApiResult;

import java.util.List;
import java.util.Map;

public interface PushService {

    ApiResult send(String templateId, String appid, List<String> unionids, Map<String, Map<String, String>> params);
}
