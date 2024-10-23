package com.zans.wechat.message_push.dao;

import com.zans.wechat.message_push.model.Config;
import com.zans.wechat.message_push.model.PushLog;
import com.zans.wechat.message_push.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConfigDao {

    boolean insert(Config config);

    boolean changeStatus(@Param("appid") String id,@Param("enableStatus") int status);

    String getAppsecret(@Param("appid") String appid);

    void insertLog(PushLog pushLog);

    Boolean insertUser(User user);

    Boolean update(Config config);

    List<Config> getConfigs();

    List<Config> getConfig(String appid);

    int getUniqueAppid(String appid);

    Config getConfigByAppid(String appid);

    List<Config> getMpConfig();
}
