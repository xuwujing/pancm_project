package com.zans.wechat.message_push.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class WxTemplate implements Serializable  {

    /**
     * 模板消息id
     */
    private String template_id;
    /**
     * 用户openId
     */
    private String touser;
    /**
     * URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）
     */
    private String url;
    /**
     * 标题颜色
     */
    private String topcolor;
    /**
     * 详细内容
     */
    private Map<String,Map<String,String>> data;


}
