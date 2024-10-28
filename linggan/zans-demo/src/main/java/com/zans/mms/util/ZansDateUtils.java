package com.zans.mms.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 零感网御 日期时间处理工具类
 */
public class ZansDateUtils {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATETIME_FORMAT_SSS = "yyyy-MM-dd HH:mm:ss:SSS";

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static String now() {
        return DateFormatUtils.format(new Date(), DATETIME_FORMAT);
    }

    public static String nowToSss() {
        return DateFormatUtils.format(new Date(), DATETIME_FORMAT_SSS);
    }

    public static String timeToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static String ToDhMs(String time) {

        try {
            long newTime = Long.parseLong(time);
            long day = newTime / (60 * 60 * 24 * 1000);
            long hour = (newTime - 60 * 60 * 24 * 1000 * day) / (60 * 60 * 1000);
            long min = (newTime - 60 * 60 * 24 * 1000 * day - 60 * 60 * 1000 * hour) / (60 * 1000);
            long s = (newTime - 60 * 60 * 24 * 1000 * day - 60 * 60 * 1000 * hour - 60 * 1000 * min) / 1000;
            long ss = (newTime - 60 * 60 * 24 * 1000 * day - 60 * 60 * 1000 * hour - 60 * 1000 * min - 1000 * s);
            if (day != 0) {
                return day + "天" + hour + "时" + min + "分" + s + "秒" + ss + "毫秒";
            } else if (hour != 0) {
                return hour + "时" + min + "分" + s + "秒" + ss + "毫秒";
            } else if (min != 0) {
                return min + "分" + s + "秒" + ss + "毫秒";
            } else if (s != 0) {
                return s + "秒" + ss + "毫秒";
            } else {
                return ss + "毫秒";
            }
        } catch (NumberFormatException numberFormatException) {
            return time + ":时间戳转换异常";
        }
    }

    public static String stampToTime(String stamp) {
        String sd = "";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_SSS);
        sd = sdf.format(new Date(Long.parseLong(stamp))); // 时间戳转换日期
        return sd;
    }
}
