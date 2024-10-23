package com.zans.wechat.message_push.service.impl;

import com.zans.wechat.message_push.dao.ConfigDao;
import com.zans.wechat.message_push.model.Config;
import com.zans.wechat.message_push.model.PushLog;
import com.zans.wechat.message_push.model.User;
import com.zans.wechat.message_push.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ConfigServiceImpl  implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public boolean insert(Config config) {
        config.setEnableStatus(1);
        config.setIntervalPullTime(30);
        config.setGroupId(1L);
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());
        return configDao.insert(config);
    }

    @Override
    public boolean changeStatus(String appid, int status) {
        return configDao.changeStatus(appid, status);
    }

    @Override
    public String getAppsecret(String appid) {

        return configDao.getAppsecret(appid);
    }

    @Override
    public void insertLog(PushLog pushLog) {
        configDao.insertLog(pushLog);
    }

    @Override
    public Boolean insertUser(User user) {
        return configDao.insertUser(user);
    }

    @Override
    public Boolean update(Config config) {
        config.setUpdateTime(new Date());
        return configDao.update(config);
    }

    @Override
    public List<Config> getConfigs() {
        return configDao.getConfigs();
    }

    @Override
    public   List<Config> getConfig(String appid) {
        return configDao.getConfig(appid);
    }

    @Override
    public Boolean getUniqueAppid(String appid) {
        return configDao.getUniqueAppid(appid)>0;
    }

    @Override
    public Config getConfigByAppid(String appid) {
        return configDao.getConfigByAppid(appid);
    }

    @Override
    public List<Config> getMpConfig() {
        return configDao.getMpConfig();
    }
}
