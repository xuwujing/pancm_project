package com.zans.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/1
 */
public class GetPlayBackRoleId {

    private static final Integer size = 4;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String get(){
        StringBuilder roleId = new StringBuilder();
        roleId.append(simpleDateFormat.format(new Date()));
        roleId.append(randomNum(size));
        return roleId.toString();
    }

    /**
     * 生成随机数
     */
    public static String randomNum(Integer size) {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int number = random.nextInt(10);
            stringBuilder.append(number + "");
        }
        return stringBuilder.toString();
    }
}
