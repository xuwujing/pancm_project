package com.zans.wechat.message_push.service;

import com.zans.wechat.message_push.model.Config;
import com.zans.wechat.message_push.model.PushLog;
import com.zans.wechat.message_push.model.User;

import java.util.List;

public interface ConfigService {
    boolean insert(Config config);

    boolean changeStatus(String appid,int status);

    String getAppsecret(String appid);

    void insertLog(PushLog pushLog);

    Boolean insertUser(User user);

    Boolean update(Config config);

    List<Config> getConfigs();

    List<Config> getConfig(String appid);

    Boolean getUniqueAppid(String appid);

    Config getConfigByAppid(String appid);

    List<Config> getMpConfig();
}
