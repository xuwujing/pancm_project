package com.zans.base.util;


import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * JDK8 的 日期格式化，线程安全
 */
@Slf4j
public class DateHelper {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static DateTimeFormatter DAY_FORMATTER_SHORT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static DateTimeFormatter DAY_FORMATTER_DOWNLOAD_SUFFIX = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private static DateTimeFormatter DAY_FORMATTER_BUSINESS_SUFFIX = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";

    private static String getDateTime(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    public static String getDateTime(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        return getDateTime(ts.toLocalDateTime());
    }

    public static Date parseDay(String s) {
        return convertToDate(LocalDate.parse(s, DAY_FORMATTER).atStartOfDay());
    }

    public static Date parseDatetime(String s) {
        return convertToDate(LocalDateTime.parse(s, DATE_FORMATTER));
    }

    public static Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static String getDateTime(Date date) {
        LocalDateTime ldt = convertToLocalDateTime(date);
        return getDateTime(ldt);
    }

    public static String getDateTime(java.sql.Date date) {
        if (date == null) {
            return null;
        }
        return getDateTime(date.toLocalDate());
    }

    public static LocalDate date2LocalDate(Date date) {
        if(null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        if(null == date) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDate转Date
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if(null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date localDate2DateTime(LocalDateTime localDateTime) {
        if(null == localDateTime) {
            return null;
        }
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
       return Date.from(zdt.toInstant());
    }

    /**
     * 在日期上加减天数
     * @param date 2020-11-18 15:53:27
     * @param days + or -
     * @return
     */
    public static Date plusDays(Date date,int days){
        Period period = Period.ofDays(days);
        return localDate2DateTime(date2LocalDateTime(date).plus(period));
    }

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        LocalDateTime ldt = convertToLocalDateTime(date);
        switch (format) {
            case "yyyy-MM-dd HH:mm:ss":
                return ldt.format(DATE_FORMATTER);
            case "yyyy-MM-dd":
                return ldt.format(DAY_FORMATTER);
            case "yyyyMMdd":
                return ldt.format(DAY_FORMATTER_SHORT);
            default:
                try {
                    return ldt.format(DateTimeFormatter.ofPattern(format));
                } catch (Exception ex) {
                    log.error("format error#" + format, ex);
                    return date.toString();
                }
        }
    }

    private static LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static String getDateTime(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    public static String getDay(LocalDateTime date) {
        return date.format(DAY_FORMATTER);
    }

    public static String getNow() {
        LocalDateTime ldt = LocalDateTime.now();
        return getDateTime(ldt);
    }

    public static String getToday() {
        LocalDateTime ldt = LocalDateTime.now();
        return getDay(ldt);
    }

    public static String getTodayShort() {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.format(DAY_FORMATTER_SHORT);
    }

    public static String correctEndTime(String input) {
        return input + " 23:59:59";
    }

    public static String getDownloadFileSuffix(){
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.format(DAY_FORMATTER_DOWNLOAD_SUFFIX);
    }

    public static String calculateIntervalTime(Date date){
        if (date == null){
            return "";
        }
        long intervalTime = System.currentTimeMillis()-date.getTime();
        if (intervalTime <= 0){
            return "";
        }
        int seconds = (int)(intervalTime/1000);
        int d = seconds / (60 * 60 * 24);
        int h = (seconds - (60 * 60 * 24 * d)) / 3600;
        int m = (seconds - 60 * 60 * 24 * d - 3600 * h) / 60;
        int s = seconds - 60 * 60 * 24 * d - 3600 * h - 60 * m;
        return d + "天" + h + "时" + m + "分" + s + "秒";
    }

    public static Integer calculateIntervalTimeTwo(Date date){
        if (date == null){
            return 0;
        }
        long intervalTime = System.currentTimeMillis()-date.getTime();
        if (intervalTime <= 0){
            int seconds = (int)(-intervalTime/1000);
            int d = seconds / (60 * 60 * 24);
            int h = (seconds - (60 * 60 * 24 * d)) / 3600;
            return 1+d;
        }
        int seconds = (int)(intervalTime/1000);
        int d = seconds / (60 * 60 * 24);
        int h = (seconds - (60 * 60 * 24 * d)) / 3600;
        return -(1+d) ;
    }

    public static String jointDate(String startStr,String endStr){
        return startStr + endStr;
    }

    public static void main(String[] args) {
        Date date=new Date();
//        date.setTime(System.currentTimeMillis()-1000*60*60*24*2);
//        System.out.println(calculateIntervalTime(date));
        System.out.println(formatDate(plusDays(date,-1),"yyyy-MM-dd HH:mm:ss"));
    }

}
