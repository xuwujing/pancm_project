package com.zans.wechat.message_push.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zans.wechat.message_push.model.WxTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 公众号推送工具类
 */
public class PushUtil {
/*    *//**
     * 暂时将变量写死  后期进行数据库获取
     *//*
    public  final static String appID= "wx9e2b630df13c305d";
    public final static String appsecret="6587406ee7ed0f9c4d02d3d29f48dd91";
    public final static String openId="o2yjU6EHUqJLwnZlXvEkxzcYY92I";
    public final static String templateId="XHuUoKbJJuhbiIgiQr93R47LKrZBYhzawsJmDjaui8Y";

    public static void main(String[] args) {
        Map<String,Map<String, String>> params = new HashMap<>();
        Map<String, String> name = new HashMap<>();
        name.put("value","100");
        params.put("name",name);
        Map<String, String> location = new HashMap<String, String>();
        location.put("value","东土大唐");
        params.put("location",location);
        Map<String, String> company = new HashMap<String, String>();
        company.put("value","程序猿");
        params.put("company",company);
        new PushUtil().send(templateId,appID,openId,params);
    }*/


    public JSONObject send(String templateId, String appid, String openId, Map<String,Map<String,String>> params, String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        //AccessToken token = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appID+"&secret="+appsecret,AccessToken.class);
        //String accessToken=token.getAccess_token();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken;
        WxTemplate temp = new WxTemplate();
        temp.setUrl("");
        temp.setTouser(openId);
        temp.setTopcolor("#FF0000");
        temp.setTemplate_id(templateId);
        temp.setData(params);
        String jsonString= JSON.toJSONString(temp);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        JSONObject jsonObject = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken, request, JSONObject.class);
        return jsonObject;


    }


}
