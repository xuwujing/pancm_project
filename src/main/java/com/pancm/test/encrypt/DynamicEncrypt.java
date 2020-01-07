package com.pancm.test.encrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: 动态MD5加密
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/1/7
 */
public class DynamicEncrypt {

    public static void main(String[] args) {
        //spring security中的BCryptPasswordEncoder方法采用SHA-256 +随机盐+密钥对密码进行加密。SHA系列是Hash算法，不是加密算法，使用加密算法意味着可以解密（这个与编码/解码一样），但是采用Hash处理，其过程是不可逆的。
        //
        //（1）加密(encode)：注册用户时，使用SHA-256+随机盐+密钥把用户输入的密码进行hash处理，得到密码的hash值，然后将其存入数据库中。
        //
        //（2）密码匹配(matches)：用户登录时，密码匹配阶段并没有进行密码解密（因为密码经过Hash处理，是不可逆的），而是使用相同的算法把用户输入的密码进行hash处理，得到密码的hash值，然后将其与从数据库中查询到的密码hash值进行比较。如果两者相同，说明用户输入的密码正确。
        //
        //这正是为什么处理密码时要用hash算法，而不用加密算法。因为这样处理即使数据库泄漏，黑客也很难破解密码（破解密码只能用彩虹表）。
        //
        //
        //
        //任何应用考虑到安全，绝不能明文的方式保存密码。密码应该通过哈希算法进行加密。有很多标准的算法比如SHA或者MD5，结合salt(盐)是一个不错的选择。 Spring Security 提供了BCryptPasswordEncoder类,实现Spring的PasswordEncoder接口使用BCrypt强哈希方法来加密密码。
        //
        //BCrypt强哈希方法 每次加密的结果都不一样。
        //最优解利用 spring security 的 BCryptPasswordEncoder 工具类
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String msg="123";
        String msg2= bCryptPasswordEncoder.encode(msg);
        System.out.println(msg2);
        System.out.println(bCryptPasswordEncoder.upgradeEncoding(msg2));
        System.out.println(bCryptPasswordEncoder.matches(msg,msg2));
        /*
           第一次
            $2a$10$yrk2FTos4ctSq1jCoxF3NubJ9XEkEVFxOmoMH76JAlY4RJZg.PlHq
            false
            true
            第二次
            $2a$10$F9xLCxu2eXmDTClqtKRZXeETXeYvCcQ2/FOvNRyZn8e5jYVIBehoS
            false
            true
         **/
    }

}
