package com.zans.wechat.message_push.service.impl;

import com.zans.wechat.message_push.dao.ConfigDao;
import com.zans.wechat.message_push.dao.TokenModelDao;
import com.zans.wechat.message_push.model.AccessToken;
import com.zans.wechat.message_push.model.Config;
import com.zans.wechat.message_push.model.TokenModel;
import com.zans.wechat.message_push.service.TokenModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TokenModelServiceImpl implements TokenModelService {

    @Autowired
    private TokenModelDao tokenModelDao;

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void delete() {
        tokenModelDao.delete();
    }

    @Override
    public void insert(TokenModel tokenModel) {
        tokenModelDao.insert(tokenModel);
    }

    @Override
    public String getByAppid(String appid) {
        return tokenModelDao.getByAppid(appid);
    }

    @Override
    public void tokenSchedule() {
        //去数据库中轮询获取为公众号的配置信息列表，然后通过这些列表去轮询插入token 首先删除原先token值
        tokenModelDao.delete();
        //获取公众号配置列表
        List<Config> configs = configDao.getMpConfig();
        for (Config config : configs) {
            AccessToken token = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + config.getAppid() + "&secret=" + config.getSercet(), AccessToken.class);
            String accessToken = token.getAccess_token();
            TokenModel tokenModel = new TokenModel();
            tokenModel.setAccessToken(accessToken);
            tokenModel.setAppid(config.getAppid());
            tokenModel.setDeadline(new Date());
            //数据插入
            tokenModelDao.insert(tokenModel);
            log.info("appid为"+config.getAppid()+"的公众号token已刷新，token为"+accessToken);
        }
    }
}
