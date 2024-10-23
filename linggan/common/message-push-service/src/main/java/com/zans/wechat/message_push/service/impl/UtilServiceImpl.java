package com.zans.wechat.message_push.service.impl;

import com.zans.wechat.message_push.dao.ConfigDao;
import com.zans.wechat.message_push.service.ConfigService;
import com.zans.wechat.message_push.service.UtilService;
import com.zans.wechat.message_push.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UtilServiceImpl implements UtilService {

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ApiResult getSessionKey(String appid, String js_code) {
        //先通过appid去获取秘钥
        String secret = configDao.getAppsecret(appid);
        //调用微信接口获取openid，sessionid和sessionkey
        String url="https://api.weixin.qq.com/sns/jscode2session?appid="+appid+ "&secret="+secret+"&js_code="+ js_code +"&grant_type=authorization_code";
        String userInfo = restTemplate.getForObject(url, String.class);
        return ApiResult.success(userInfo);
    }


}
