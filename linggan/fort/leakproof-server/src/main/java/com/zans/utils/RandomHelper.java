package com.zans.utils;

import java.util.Date;
import java.util.Random;

public class RandomHelper {
    /**
     * 生成随机ID
     * @return
     */
    public static String generateId(){
        return DateHelper.getDateTime(new Date()) + getRandomStr(8,"NUMBER");
    }

    /**
     * 取随机数(0-1)
     *
     * @return
     */
    public static double getRandom() {
        Random rd = new Random();
        return rd.nextDouble();
    }

    /**
     * 取min 到 max 之间的随机整数
     *
     * @param min
     * @param max
     * @return
     */
    public static long getRandom(long min, long max) {
        if (min < 0 || max <= 0) {
            return 0;
        }
        if ((max - min) < 0) {
            return 0;
        }
        if (max == min) {
            return min;
        }
        max++;
        Random rd = new Random();
        long tmp = rd.nextInt((int) (max - min));
        return tmp + min;
    }

    public static String getRandom(int size) {
        if (size <= 0) {
            return null;
        }
        double tmp = getRandom();
        tmp = Math.pow(10, size) * tmp;
        StringBuilder result = new StringBuilder(String.valueOf((int) tmp));
        while (result.length() < size) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    /**
     * 取指定长度的随机数
     * @author wuxiao
     * @return
     */
    public static String getRandomStr(Integer len, String format) {
        String chars;
        switch (format.toUpperCase()) {
            case "CHAR" :
                chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                break;
            case "NUMBER" :
                chars = "0123456789";
                break;
            case "PNUMBER" :
                chars = "123456789";
                break;
            default :
                chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                break;
        }

        StringBuilder string = new StringBuilder();
        Random rd = new Random();
        while ( string.length() < len ){
            int pos = Math.abs(rd.nextInt()) % chars.length();
            string.append(chars.substring(pos, pos + 1));
        }
        return string.toString();
    }
}
