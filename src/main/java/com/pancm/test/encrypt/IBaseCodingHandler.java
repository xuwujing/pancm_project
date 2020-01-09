package com.pancm.test.encrypt;

/**
 * @author pancm
 * @Title: mboss
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/1/9
 */
public  interface IBaseCodingHandler {

     String getToken(String userid, String pwd, String time);

     boolean matchesToken(String rawPassword, String encodedPassword);

     String getEncode(String key, String rawPassword, String time);

     String getDecode(String key, String encodedPassword, String time);


}
