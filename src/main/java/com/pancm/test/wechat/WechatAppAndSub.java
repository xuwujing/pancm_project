package com.pancm.test.wechat;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 *  微信小程序和公众号相关代码
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/2/5
 */
public class WechatAppAndSub {

    public static void main(String[] args) {
        /**
         * @Author pancm
         * 任务一:公众号(服务号)关联的小程序进行消息推送
         * 操作流程:
         * 一阶段
         * 1、注册微信公众号。
         * 2、注册微信小程序 (可以从公众号中快速生成。)
         * 3、注册微信开放平台。
         *
         * 二阶段
         * 1、认证公众号 (注意首先要认证公众号，300认证费。)
         * 2、从公众号中关联认证小程序(否则单独认证又要收300的认证费用，比较费钱。)
         * 3、认证微信开放平台（300认证费。)
         *
         * 三阶段
         * 1、将微信公众号和微信小程序绑定到微信开放平台下。
         * 2、配置ip白名单（获取对外ip地址网站：http://myip.kkcha.com/
         *
         *
         *
         *
         **/


    }








    /**
     * @Description 微信公众号的配置
     **/
    private static final String WX_GZH_APP_ID = "wx6b57c8667d246332";
    private static final String WX_GZH_SECRET = "f4736e1ed9b9d895f58976feb7e2af8b";
    private static final String WX_GZH_GRANT_TYPE = "client_credential";

    /**
     * @Description 微信小程序的配置
     **/
    private static final String WX_XCX_APP_ID = "wxe677fdbc70d44156";
    private static final String WX_XCX_SECRET = "87c5581ff979a38767a3df1d40a7ec41";


    private static final String WX_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    private static final String WX_TMP_SEND_URL = "https://mp.weixin.qq.com/advanced/tmplmsg";

    /** 根据订阅消息进行推送 */
    private static final String WX_SUB_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/bizsend";

    /** 根据token获取openid */
    private static final String WX_GET_OPENID_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=";







}
