package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSON;
import com.zans.base.vo.ApiResult;
import com.zans.mms.model.WechatConfig;
import com.zans.mms.service.IWeChatReqService;
import com.zans.mms.service.IWeChatService;
import com.zans.mms.service.IWechatConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("weChatService")
public class WeChatServiceImpl implements IWeChatService {

    //写死的appid和秘钥 2021/6/3修改为从数据库查询 修改为getWxMiniAppid和getWxMiniSecret方法
    /*private final String wxMiniAppId = "wxe677fdbc70d44156";

    private final String wxMiniSecret = "87c5581ff979a38767a3df1d40a7ec41";*/

    /**
     * appid: 'wxe677fdbc70d44156',
     * secret: '87c5581ff979a38767a3df1d40a7ec41'
     */
    //private final String wxHttpAuthUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxMiniAppId + "&secret=" + wxMiniSecret +  "&js_code=%s&grant_type=authorization_code";

    @Autowired
    private IWeChatReqService weChatReqService;

    @Autowired
    private IWechatConfigService wechatConfigService;


    private Map wxHttpAuthUrl(int time, String jsCode) {
//        String url = String.format(wxHttpAuthUrl, jsCode);
//        log.info("GET/{} - {}", time, url);
//        RestTemplateHelper rest = new RestTemplateHelper();
        try {
//            String content = rest.getForStr(url);
            String content =  weChatReqService.getSessionKey(jsCode);
            log.info("RES/{} - {}", time, content);
//            JSONObject jsonObject = JSONObject.parseObject(content);
            ApiResult apiResult =     JSON.parseObject(content, ApiResult.class);
            if(apiResult == null || apiResult.getCode()!=0){
                return null;
            }
            Map map =JSON.parseObject(apiResult.getData().toString(), HashMap.class);
            if (map != null && !map.isEmpty()) {
                return map;
            }
        } catch (Exception e) {
            log.error("getWxAuthInfo", e);
        }
        return null;
    }


    @Override
    public String getWxMiniAppId() {
        return getWxMiniAppid();
    }

    @Override
    public Map getWxMiniAuthInfo(String loginCode) {
        Map wxInfoMap = wxHttpAuthUrl(1, loginCode);
        if (wxInfoMap != null && !wxInfoMap.isEmpty()) {
            return wxInfoMap;
        }
        return wxHttpAuthUrl(2, loginCode); // 重试用一次
    }


    public String getWxMiniAppid(){
        return wechatConfigService.queryById(1L).getMpAppid();
    }

    public String getWxMiniSecret(){
        return  wechatConfigService.queryById(1L).getMpSecret();
    }
}
