package com.zans.wechat.message_push.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.wechat.message_push.dao.ConfigDao;
import com.zans.wechat.message_push.dao.TokenModelDao;
import com.zans.wechat.message_push.dao.UserDao;
import com.zans.wechat.message_push.model.PushLog;
import com.zans.wechat.message_push.model.PushTemplate;
import com.zans.wechat.message_push.service.PushService;
import com.zans.wechat.message_push.util.PushUtil;
import com.zans.wechat.message_push.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class PushServiceImpl implements PushService {

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    TokenModelDao tokenModelDao;



    @Override
    public ApiResult send(String templateId, String appid, List<String> unionids, Map<String, Map<String, String>> params) {
        //根据传的appid去数据区中获取已启用配置的秘钥
        String accessToken=tokenModelDao.getByAppid(appid);
        log.info("token "+accessToken);
        //调用远程接口判断模板id是否存在
        JSONObject forObject = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=" + accessToken, JSONObject.class);
        if(!forObject.toString().contains(templateId)){
            return ApiResult.error(603,"模板不存在");
        }
        //初始化数据 数据总条数  失败条数 失败id集合
        int total = unionids.size();
        int failCount=0;
        List<String>failUnionList = new ArrayList<>();
        //日志数据插入
        String serialNum = UUID.randomUUID().toString().replace("-","");
        for (String unionid:unionids) {
            //先用unionid反查openid
            String openid=userDao.getOpenid(unionid);
            PushLog pushLog = new PushLog();
            pushLog.setReqData(forObject.toJSONString());
            pushLog.setSerialNum(serialNum);
            pushLog.setAppid(appid);
            pushLog.setOpenid(openid);
            pushLog.setReqTime(new Date());
            JSONObject jsonObject = new PushUtil().send(templateId, appid, openid, params,accessToken);
            int errorCode=jsonObject.getInteger("errcode");
            //如果发送模板消息的返回码不为0 则表示发送未成功
            if(errorCode!=0){
                failCount++;
                failUnionList.add(unionid);
            }
            //通过返回的结果判断是否成功 不成功failCount累加1 failUnionList添加一条数据
            pushLog.setRepTime(new Date());
            pushLog.setRepData(jsonObject.toString());
            configDao.insertLog(pushLog);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("failCount",failCount);
        map.put("failUnionList",failUnionList);
        return ApiResult.success(map);
    }
}
