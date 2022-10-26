package com.pancm.test.wechat;

import com.alibaba.fastjson.JSONObject;
import com.pancm.test.httpTest.HttpClientUtil;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/1/28
 */
public class GetOpenidAndUnionId {

    private static final String url = "https://api.weixin.qq.com/sns/jscode2session";

    public static void main(String[] args) {
        try {
            sendMsg();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void sendMsg() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("","");
        String wxspAppid = "";
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "";
        //授权（必填）
        String grant_type = "authorization_code";
        String code = "";
        //请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;

        HttpClientUtil.get(url,jsonObject);



    }


}
