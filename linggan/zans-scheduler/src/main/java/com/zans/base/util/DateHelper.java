package com.zans.base.util;


import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * JDK8 的 日期格式化，线程安全
 */
@Slf4j
public class DateHelper {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static DateTimeFormatter DAY_FORMATTER_SHORT = DateTimeFormatter.ofPattern("yyyyMMdd");

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


    public static Date addMinutes(Date dateInput, int minute) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateInput);
            calendar.add(Calendar.MINUTE, minute);
            return calendar.getTime();
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean isTimeout(Date dateInput, int minute) {
        Date now = new Date();
        Date timeout = addMinutes(dateInput, minute);
        return timeout.getTime() < now.getTime();
    }

//    public static void main(String[] args) {
//        String input1 = "2020-05-13 20:00:00";
//        log.info("{}, timeout#{}", input1, isTimeout(parseDatetime(input1), 80));
//        log.info("{}, timeout#{}", input1, isTimeout(parseDatetime(input1), 45));
//        log.info("{}, timeout#{}", input1, isTimeout(parseDatetime(input1), 48));
//    }
}
