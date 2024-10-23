package com.zans.wechat.message_push.service;

import com.zans.wechat.message_push.model.TokenModel;

public interface TokenModelService {
    void delete();

    void insert(TokenModel tokenModel);

    String getByAppid(String appid);

    void tokenSchedule();
}
