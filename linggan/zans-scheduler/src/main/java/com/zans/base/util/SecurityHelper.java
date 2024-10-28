package com.zans.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityHelper {

    private static final String SALT = "0fdfa5e5a19bedae487a9d3ae7c24708";

    public static String getMd5(String str) {
        try {
            // 创建具有指定算法名称的摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组更新摘要
            md.update(str.getBytes());
            // 进行哈希计算并返回一个字节数组
            byte[] mdBytes = md.digest();
            StringBuilder hash = new StringBuilder();
            //循环字节数组
            for (byte mdByte : mdBytes) {
                int temp;
                //如果有小于0的字节,则转换为正数
                if (mdByte < 0) {
                    temp = 256 + mdByte;
                } else {
                    temp = mdByte;
                }
                if (temp < 16) {
                    hash.append("0");
                }
                //将字节转换为16进制后，转换为字符串
                hash.append(Integer.toString(temp, 16));
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5WithSalt(String numStr, final String saltInput) {
        String salt = saltInput;
        if (StringHelper.isBlank(salt)) {
            salt = SALT;
        }
        return getMd5(getMd5(numStr) + salt);
    }

}


