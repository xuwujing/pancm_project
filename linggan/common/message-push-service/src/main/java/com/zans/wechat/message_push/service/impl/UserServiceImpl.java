package com.zans.wechat.message_push.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.wechat.message_push.dao.ConfigDao;
import com.zans.wechat.message_push.dao.TokenModelDao;
import com.zans.wechat.message_push.dao.UserDao;
import com.zans.wechat.message_push.model.Config;
import com.zans.wechat.message_push.model.User;
import com.zans.wechat.message_push.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenModelDao tokenModelDao;

    @Override
    public String getOpenid(String unionid) {
        return userDao.getOpenid(unionid);
    }

    @Override
    public Boolean insertList(List<User> users) {
        for (User user:users) {
            userDao.insert(user);
        }


        return true;
    }

    @Override
    public void userSchedule() {
        try {
            //获取公众号配置列表
            List<Config> configs = configDao.getMpConfig();
            for (Config config:configs) {
                queryUserInfo(config.getAppid());
            }
        }catch (Exception e){

        }

    }

    @Override
    public List<String> getUnionid() {
        return userDao.getUnionid();
    }


    public JSONObject getUserListParamJson(String appid, String token){
        //通过appid获得配置信息，如果配置信息中有nextopenid，则在url中添加next_openid参数
        Config config = configDao.getConfigByAppid(appid);

        StringBuffer url = new StringBuffer("https://api.weixin.qq.com/cgi-bin/user/get?access_token="+token);
        /**
         * url请求获取的数据格式如下：
         * {
         *     "total": 11,
         *     "count": 11,
         *     "data": {
         *         "openid": [
         *             "o_1Nd6Q3ztmHMk9HUmaFc4qHnCSQ",
         *             "o_1Nd6bB6_xejBanGkS8GY3kuKa8",
         *             "o_1Nd6Vk-C8CZI6wUAiTDte5EvtY",
         *             "o_1Nd6XJnjpNJGU80Sx6F6KuDzFo",
         *             "o_1Nd6UQu0yhKbByvHs7uetUSRPo",
         *             "o_1Nd6SVhPdgF2PZojl6kJiZ3mPc",
         *             "o_1Nd6Qq1WlV_x-RRMUyaWxymoew",
         *             "o_1Nd6WHg8rvP6Gv8GCSvyhsVLFg",
         *             "o_1Nd6TRkTipRT0HdkyVzE-Ec0OI",
         *             "o_1Nd6dl56NLWIm9yp5tYi0djY2I",
         *             "o_1Nd6XI-htBjCex1AZWr7vUbxOU"
         *         ]
         *     },
         *     "next_openid": "o_1Nd6XI-htBjCex1AZWr7vUbxOU"
         * }
         */

        if(null!=config.getNextOpenid()){
            url.append("&next_openid="+config.getNextOpenid());
        }
        //通过accessToken批量获取公众号用户的openid
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();

        String result  = restTemplate.getForObject(url.toString(), String.class);

        JSONObject jsonObject = JSONObject.parseObject(result);
        //获取json中的next_openid
        String next_openid=jsonObject.getString("next_openid");
        //进行数据库写入next_openid
        config.setNextOpenid(next_openid);
        configDao.update(config);

        List<String> openids = jsonObject.getJSONObject("data").getObject("openid",List.class);
        for (String openid:openids) {
            JSONObject u = new JSONObject();
            u.put("openid", openid);
            u.put("lang", "zh_CN");
            array.add(u);
        }
        obj.put("user_list",array);
        return obj;
    }




    public void queryUserInfo(String appid) {
        List<Config> configs = configDao.getMpConfig();
        //通过appid查询配置中是否有nextopenid，如果有 则添加nextopenid参数
        //String token = "42_JPn25WCmCZAu7E_QhMamRtMmlSJovIyHB1ZXoSeTUonfbWxgUQaT3-Q7Wg0oyJ8CjdEbP-O2bIWFHysQxH3zNjnCNRFHaOoIuahVMp3eMc9jG2lNQRWMocyoekuV6uZbMw7kt9RH7CSXYAuOLYXgADATGF";
        String url = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=%s";
        String token = tokenModelDao.getByAppid(appid);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String content = getUserListParamJson(appid,token).toJSONString();

        log.info(content);

        HttpEntity<String> request = new HttpEntity<>(content, headers);

        String queryUrl = String.format(url, token);
        log.info("queryUrl:{}", queryUrl);
        ResponseEntity<String> postForEntity = restTemplate.postForEntity(queryUrl, request, String.class);

        String body = postForEntity.getBody();

        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray jsonArray = jsonObject.getJSONArray("user_info_list");
        List<User> users = jsonArray.toJavaList(User.class);
        List<User> userList = new ArrayList<>();
        for (User user : users) {
            user.setCreator("admin");
            user.setAppid(appid);
            user.setCreate_time(new Date());
            user.setUpdate_time(new Date());
            user.setEnable_status(1);
            userList.add(user);
        }

        //进行数据库写入
        userDao.insertList(userList);



    }
}
