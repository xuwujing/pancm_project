package com.zans.mms.service;

import java.util.Map;

public interface IWeChatService {

    /**
     * 获取微信小程序appid
     *
     * @return
     */
    String getWxMiniAppId();


    /**
     * 根据wx.login的code获取 openid,sessionkey等
     *
     * @param loginCode wx.login的code
     * @return
     */
    Map getWxMiniAuthInfo(String loginCode);




}
