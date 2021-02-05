package com.pancm.test.wechat;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 *  微信小程序和公众号相关代码
 *
 *  微信api:https://developers.weixin.qq.com/doc/offiaccount/WeChat_Invoice/Nontax_Bill/API_list.html#1.1
 *
 *  参考链接: https://blog.csdn.net/qq_40649119/article/details/90204601
 *
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
         * 实现流程:
         * 1.获取token(WX_TOKEN_URL)，并且需要将token存起来,定时去刷新;
         * 2.调用
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

    /**
     * @Author pancm
     * @Description 获取token get请求
     * 需要这三个参数:
     *
     *  grant_type:client_credential(固定写死)
     *  appid: 公众号的appid,登录后台进行查看获取
     *  secret: 公众号的secret
     *
     *
     *
     * @Date  2021/2/5
     * @Param
     * @return
     **/
    private static final String WX_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

//    private static final String WX_TMP_SEND_URL = "https://mp.weixin.qq.com/advanced/tmplmsg";

    /**
     * @Author pancm
     * @Description 根据模板发送消息
     * 微信api: https://mp.weixin.qq.com/advanced/tmplmsg?action=faq&token=947042953&lang=zh_CN
     * 传输字段:
     * access_token: 从获取token里面取
     * {
     * "touser":"OPENID"(从这个WX_GET_OPENID_URL接口里面拿到数据),
     * "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
     * "url":"http://weixin.qq.com/download",
     * "topcolor":"#FF0000",
     * "data":{
     * "User": {
     * "value":"黄先生",
     * "color":"#173177"
     * },
     * "Date":{
     * "value":"06月07日 19时24分",
     * "color":"#173177"
     * },
     * "CardNumber":{
     * "value":"0426",
     * "color":"#173177"
     * },
     * "Type":{
     * "value":"消费",
     * "color":"#173177"
     * },
     * "Money":{
     * "value":"人民币260.00元",
     * "color":"#173177"
     * },
     * "DeadTime":{
     * "value":"06月07日19时24分",
     * "color":"#173177"
     * },
     * "Left":{
     * "value":"6504.09",
     * "color":"#173177"
     * }
     * }
     * }
     *
     *
     * @Date  2021/2/5
     * @Param
     * @return
     **/
    private static final String WX_TMP_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";



    /** 根据订阅消息进行推送 */
    private static final String WX_SUB_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/bizsend";

    /** 根据token获取openid */
    private static final String WX_GET_OPENID_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=";







}
