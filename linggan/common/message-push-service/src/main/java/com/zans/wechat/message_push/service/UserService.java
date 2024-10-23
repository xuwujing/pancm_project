package com.zans.wechat.message_push.service;

import com.zans.wechat.message_push.model.User;

import java.util.List;

public interface UserService {
    String getOpenid(String unionid);

    Boolean insertList(List<User> users);

    void userSchedule();

    List<String> getUnionid();
}
