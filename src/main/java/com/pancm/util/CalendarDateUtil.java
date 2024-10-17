package com.pancm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The type Date tools.
 *
 * @author pancm
 * @Title: DateTools
 * @Description: 日期工具类
 * @Version:1.0.0
 * @date 2018年11月21日
 * @since jdk1.8
 */
public class CalendarDateUtil {

    /**
     * The constant LOGGER.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CalendarDateUtil.class);

    /**
     * 要用到的DATE Format的定义.
     */
    public static final String DATE_FORMAT_DATE_ONLY = "yyyy-MM-dd";
    /**
     * The constant DATE_FORMAT_DATETIME.
     */
    public static final String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * The constant DATE_FORMAT_DATETIME14.
     */
    public static final String DATE_FORMAT_DATETIME14 = "yyyyMMddHHmmss";

    /**
     * The constant SHORTDATEFORMAT.
     */
    public static final String SHORT_DATE_FORMAT = "yyyyMMdd";

    /**
     * The constant HMS_FORMAT.
     */
    public static final String HM_FORMAT = "HH:mm";

    /**
     * The constant HMS_FORMAT.
     */
    public static final String HMS_FORMAT = "HH:mm:ss";

    private static final ZoneId UTC_ZONE = ZoneOffset.UTC.normalized();

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    private static final Integer QUARTER_FIRST = 1;

    private static final Integer QUARTER_SECOND = 2;

    private static final Integer QUARTER_THIRD = 3;

    private static final Integer QUARTER_FOURTH = 4;

    /**
     * 把字符串转成日期类型.
     * 输入的日期格式:yyyy-MM-dd HH:mm:ss
     *
     * @param str 日期字符串
     * @return 转换后的日期 local date time
     * @throws ParseException 异常
     * @see LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(final String str) throws ParseException {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * @Author pancm
     * @Description 计算相差的月份，如果是月初和月末，则相差月份+1
     * @Date  2023/6/27
     * @Param [startDate, endDate]
     * @return long
     **/
    public static long getMonthDifference(Date startDate, Date endDate) {
        // 将Date转换为LocalDate
        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 获取两个日期之间的差距
        Period period = Period.between(localStartDate, localEndDate);

        // 如果开始日期是月初且结束日期是月末，则相差月份+1
        if (localStartDate.getDayOfMonth() == 1 && localEndDate.lengthOfMonth() == localEndDate.getDayOfMonth()) {
            period = period.plusMonths(1);
        }
        // 计算相差的月份
        int monthDifference = period.getYears() * 12 + period.getMonths();
        return monthDifference;
    }
    /**
     * Gets date yyyy.
     *
     * @return the date yyyy
     * @throws ParseException the parse exception
     */
    public static Date getDateYYYY() throws  ParseException{
        LocalDateTime localDateTime = parseLocalDateTime(getCurrentDateTime());
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * Parse date string.
     *
     * @param date the date
     * @return the string
     */
   public static String parseDate(Date date) {
        //Convert Date object to Instant object
        Instant instant = date.toInstant();
        //Get the default timezone
        ZoneId zone = ZoneId.systemDefault();
        //Convert Instant object to LocalDateTime object
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        //Format the LocalDateTime object
        return formatLocalDateTime(localDateTime);
    }


    /**
     * 把字符串转成特定格式的日期类型.
     * 输入的日期格式:yyyy-MM-dd HH:mm:ss
     *
     * @param str        日期字符串
     * @param dateFormat 日期字符串
     * @return 转换后的日期 local date time
     * @throws ParseException 转换异常
     * @see LocalDateTime
     */
   public static LocalDateTime parseLocalDateTime(final String str, final String dateFormat) throws ParseException {
        //Create a LocalDateTime object from the given string and date format
        LocalDateTime localDateTime = LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
        //Create a DateTimeFormatter object from the given date format
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern(dateFormat);
        //Return the LocalDateTime object parsed using the given date format
        return LocalDateTime.parse(localDateTime.format(ofPattern), ofPattern);
    }

    /**
     * 把字符串转成日期类型.
     * 输入的日期格式:yyyy-MM-dd
     *
     * @param str 日期字符串
     * @return 转换后的日期 local date time
     * @throws ParseException 异常
     * @see LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime10(final String str) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY);
        LocalDateTime time = LocalDateTime.from(LocalDate.parse(str, formatter).atStartOfDay());
        return time;
    }

    /**
     * 把字符串转成日期类型.
     * 输入的日期格式:yyyy-MM-dd
     *
     * @param str 日期字符串
     * @return 转换后的日期 local date
     * @throws ParseException 异常
     * @see LocalDateTime
     */
    public static LocalDate parseLocalDate(final String str) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY);
        return LocalDate.parse(str, formatter);
    }

    /**
     * 把字符串转成日期类型.
     *
     * @param str    日期字符串
     * @param format 日期格式
     * @return 转换后的日期 local date
     * @throws ParseException 异常
     * @see LocalDateTime
     */
    public static LocalDate parseLocalDate(final String str, final String format) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(str, formatter);
    }

    /**
     * 获得当前的日期毫秒.
     *
     * @return 当前毫秒数 long
     */
    public static long nowTimeMillis() {
        return Clock.systemDefaultZone().millis();
    }

    /**
     * 获取从1970年到现在的秒数.
     *
     * @return 秒数 long
     */
    public static long nowEpochSecond() {
        return Clock.systemDefaultZone().instant().getEpochSecond();
    }

    /**
     * 获得当前的时间戳.
     *
     * @return 时间点 instant
     */
    public static Instant nowTimestamp() {
        return Instant.now(Clock.systemDefaultZone());
    }

    /**
     * yyyy-MM-dd 当前日期.
     *
     * @return 当前日期 yyyy-MM-dd
     */
    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
    }

    /**
     * 获取当前日期时间 yyyy-MM-dd HH:mm:ss.
     *
     * @return 获取当前日期时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * 获取当前日期时间.
     *
     * @param format 格式字符串
     * @return 获取当前日期时间 current date time
     */
    public static String getCurrentDateTime(final String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取当前时间 HH:mm:ss.
     *
     * @return 获取当前时间 HH:mm:ss
     */
    public static String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(HMS_FORMAT));
    }

    /**
     * yyyy-MM-dd 格式化传入日期.
     *
     * @param date 日期
     * @return yyyy -MM-dd 日期
     */
    public static String formatDate(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
    }

    /**
     * 将localDateTime 格式化成特定格式的字符串.
     *
     * @param time       时间
     * @param dateFormat 格式化
     * @return String string
     */
    public static String formatTime(final LocalTime time, final String dateFormat) {
        return time.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * yyyyMMdd 格式化传入日期.
     *
     * @param date 传入的日期
     * @return yyyyMMdd 字符串
     */
    public static String formatDateToyyyyMMdd(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(SHORT_DATE_FORMAT));
    }


    /**
     * 将localDateTime 格式化成yyyy-MM-dd HH:mm:ss.
     *
     * @param dateTime 时间
     * @return String string
     */
    public static String formatLocalDateTime(final LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }
    /**
     * 将localDateTime 格式化成特定格式的字符串.
     *
     * @param dateTime   时间
     * @param dateFormat 格式化
     * @return string string
     */
    public static String formatLocalDateTime(final LocalDateTime dateTime, final String dateFormat) {
        return dateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * yyyy-MM-dd HH:mm:ss.
     * 时间点转换成日期字符串
     *
     * @param instant 时间点.
     * @return 日期时间 yyyy-MM-dd HH:mm:ss
     * @throws ParseException 异常
     */
    public static String parseInstantToDataStr(final Instant instant) throws ParseException {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * 得到时间戳格式字串.
     *
     * @param date 长日期
     * @return UTC 格式的时间戳字符串
     */
    public static String getTimeStampStr(final LocalDateTime date) {
        return date.toInstant(ZoneOffset.UTC).toString();
    }

    /**
     * 计算 second 秒后的时间.
     *
     * @param date   长日期
     * @param second 需要增加的秒数
     * @return 增加后的日期 local date time
     */
    public static LocalDateTime addSecond(final LocalDateTime date, final int second) {
        return date.plusSeconds(second);
    }

    /**
     * 计算 minute 分钟后的时间.
     *
     * @param date   长日期
     * @param minute 需要增加的分钟数
     * @return 增加后的日期 local date time
     */
    public static LocalDateTime addMinute(final LocalDateTime date, final int minute) {
        return date.plusMinutes(minute);
    }

    /**
     * 计算 hour 小时后的时间.
     *
     * @param date 长日期
     * @param hour 增加的小时数
     * @return 增加后的日期 local date time
     */
    public static LocalDateTime addHour(final LocalDateTime date, final int hour) {
        return date.plusHours(hour);
    }

    /**
     * 计算 day 天后的时间.
     *
     * @param date 长日期
     * @param day  增加的天数
     * @return 增加后的日期 local date time
     */
    public static LocalDateTime addDay(final LocalDateTime date, final int day) {
        return date.plusDays(day);
    }

    /**
     * 计算 month 月后的时间.
     *
     * @param date  长日期
     * @param month 需要增加的月数
     * @return 增加后的日期 local date time
     */
    public static LocalDateTime addMoth(final LocalDateTime date, final int month) {
        return date.plusMonths(month);
    }

    /**
     * 计算 year 年后的时间.
     *
     * @param date 长日期
     * @param year 需要增加的年数
     * @return 增加后的日期 local date time
     */
    public static LocalDateTime addYear(final LocalDateTime date, final int year) {
        return date.plusYears(year);
    }

    /**
     * 得到day的起始时间点.
     * 一天开始的时间为　0:0:0
     *
     * @param date 短日期
     * @return yyyy -MM-dd HH:mm:ss 字符串
     */
    public static LocalDateTime getDayStart(final LocalDate date) {
        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        return localDateTime;
    }

    /**
     * 得到day的结束时间点。.
     * 一天结束的时间为 23:59:59
     *
     * @param date date 短日期
     * @return yyyy -MM-dd HH:mm:ss 字符串
     */
    public static LocalDateTime getDayEnd(final LocalDate date) {
        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        return localDateTime;
    }

    /**
     * 根据日期字符串获取时间戳.
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return 时间信息 instant
     * @throws ParseException 异常
     */
    public static Instant parseDataStrToInstant(final String dateStr) throws ParseException {
        return parseLocalDateTime(dateStr).toInstant(ZoneOffset.UTC);
    }

    /**
     * 两个日期是否为同一天.
     *
     * @param date1 时间
     * @param date2 时间
     * @return boolean boolean
     */
    public static boolean isSameDate(final LocalDateTime date1, final LocalDateTime date2) {
        String date1Str = date1.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
        String date2Str = date2.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
        return date1Str.equals(date2Str);
    }

    /**
     * 取得两个日期之间相差的年数.
     * getYearsBetween
     *
     * @param t1 开始时间
     * @param t2 结果时间
     * @return t1到t2间的年数 ，如果t2在 t1之后，返回正数，否则返回负数
     */
    public static long getYearsBetween(final LocalDate t1, final LocalDate t2) {
        return t1.until(t2, ChronoUnit.YEARS);
    }

    /**
     * 取得两个日期之间相差的日数.
     *
     * @param t1 开始日期
     * @param t2 结束日期
     * @return t1到t2间的日数 ，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getDaysBetween(final LocalDate t1, final LocalDate t2) {
        return t1.until(t2, ChronoUnit.DAYS);
    }

    /**
     * 取得两个日期之间相差的月数.
     *
     * @param t1 开始日期
     * @param t2 结束日期
     * @return t1到t2间的日数 ，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getMonthsBetween(final LocalDate t1, final LocalDate t2) {
        return t1.until(t2, ChronoUnit.MONTHS);
    }

    /**
     * 取得两个日期之间相差的小时数.
     *
     * @param t1 开始长日期
     * @param t2 结束长日期
     * @return t1到t2间的日数 ，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getHoursBetween(final LocalDateTime t1, final LocalDateTime t2) {
        return t1.until(t2, ChronoUnit.HOURS);
    }

    /**
     * 取得两个日期之间相差的秒数.
     *
     * @param t1 开始长日期
     * @param t2 结束长日期
     * @return t1到t2间的日数 ，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getSecondsBetween(final LocalDateTime t1, final LocalDateTime t2) {
        return t1.until(t2, ChronoUnit.SECONDS);
    }

    /**
     * 取得两个日期之间相差的分钟.
     *
     * @param t1 开始长日期
     * @param t2 结束长日期
     * @return t1到t2间的日数 ，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getMinutesBetween(final LocalDateTime t1, final LocalDateTime t2) {
        return t1.until(t2, ChronoUnit.MINUTES);
    }

    /**
     * 获取今天是星期几.
     *
     * @return 返回今天是周几 week
     * @see DayOfWeek
     */
    public static DayOfWeek getWeek() {
        return LocalDate.now().getDayOfWeek();
    }

    /**
     * 判断时间是否在指定的时间段之类.
     *
     * @param date  需要判断的时间
     * @param start 时间段的起始时间
     * @param end   时间段的截止时间
     * @return true or false
     */
    public static boolean isBetween(final LocalDateTime date, final LocalDateTime start, final LocalDateTime end) {
        if (date == null || start == null || end == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        return date.isAfter(start) && date.isBefore(end);
    }

    /**
     * 判断时间是否在指定的时间段之类.
     *
     * @param date  需要判断的时间
     * @param start 时间段的起始时间
     * @param end   时间段的截止时间
     * @return true or false
     */
    public static boolean isBetween(final LocalTime date, final LocalTime start, final LocalTime end) {
        if (date == null || start == null || end == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        return date.isAfter(start) && date.isBefore(end);
    }

    /**
     * 得到传入日期,周起始时间.
     * 这个方法定义:周一为一个星期开始的第一天
     *
     * @param date 日期
     * @return 返回一周的第一天 week start
     */
    public static LocalDate getWeekStart(final LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * 得到当前周截止时间.
     * 这个方法定义:周日为一个星期开始的最后一天
     *
     * @param date 日期
     * @return 返回一周的最后一天 week end
     */
    public static LocalDate getWeekEnd(final LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    }

    /**
     * 得到month的终止时间点..
     *
     * @param date 日期
     * @return 传入的日期当月的结束日期 month end
     */
    public static LocalDate getMonthEnd(final LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 得到当月起始时间.
     *
     * @param date 日期
     * @return 传入的日期当月的开始日期 month start
     */
    public static LocalDate getMonthStart(final LocalDate date) {

        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 得到当前年起始时间.
     *
     * @param date 日期
     * @return 传入的日期当年的开始日期 year start
     */
    public static LocalDate getYearStart(final LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 得到当前年最后一天.
     *
     * @param date 日期
     * @return 传入的日期当年的结束日期 year end
     */
    public static LocalDate getYearEnd(final LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 取得季度第一天.
     *
     * @param date 日期
     * @return 传入的日期当季的开始日期 season start
     */
    public static LocalDate getSeasonStart(final LocalDate date) {
        return getSeasonDate(date)[0];
    }

    /**
     * 取得季度最后一天.
     *
     * @param date 日期
     * @return 传入的日期当季的结束日期 season end
     */
    public static LocalDate getSeasonEnd(final LocalDate date) {
        return getSeasonDate(date)[2].with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 取得季度月的第一天.
     *
     * @param date 日期
     * @return 返回一个当前季度月的数组 local date [ ]
     */
    public static LocalDate[] getSeasonDate(final LocalDate date) {
       LocalDate[] season = new LocalDate[3];
        int nSeason = getSeason(date);
        int year = date.getYear();
        if (nSeason == QUARTER_FIRST) {
            // 第一季度
            season[0] = LocalDate.of(year, Month.JANUARY, 1);
            season[1] = LocalDate.of(year, Month.FEBRUARY, 1);
            season[2] = LocalDate.of(year, Month.MARCH, 1);
        } else if (nSeason == QUARTER_SECOND) {
            // 第二季度
            season[0] = LocalDate.of(year, Month.APRIL, 1);
            season[1] = LocalDate.of(year, Month.MAY, 1);
            season[2] = LocalDate.of(year, Month.JUNE, 1);
        } else if (nSeason == QUARTER_THIRD) {
            // 第三季度
            season[0] = LocalDate.of(year, Month.JULY, 1);
            season[1] = LocalDate.of(year, Month.AUGUST, 1);
            season[2] = LocalDate.of(year, Month.SEPTEMBER, 1);
        } else if (nSeason == QUARTER_FOURTH) {
            // 第四季度
            season[0] = LocalDate.of(year, Month.OCTOBER, 1);
            season[1] = LocalDate.of(year, Month.NOVEMBER, 1);
            season[2] = LocalDate.of(year, Month.DECEMBER, 1);
        }
        return season;
    }

    /**
     * 判断当前期为每几个季度.
     *
     * @param date 日期
     * @return 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     */
    public static int getSeason(final LocalDate date) {

        int season = 0;

        Month month = date.getMonth();

        switch (month) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
                season = 1;
                break;
            case APRIL:
            case MAY:
            case JUNE:
                season = 2;
                break;
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                season = 3;
                break;
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 获取当前时间的前多少,yyyy-MM-dd.
     *
     * @param days 天数
     * @return yyyy -MM-dd
     */
    public static String subDays(final int days) {
        return LocalDate.now().minusDays(days).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
    }

    /**
     * 判断开始时间和结束时间，是否超出了当前时间的一定的间隔数限制 如：开始时间和结束时间，不能超出距离当前时间90天.
     *
     * @param startDate 开始时间
     * @param endDate   结束时间按
     * @param interval  间隔数
     * @return true or false
     */
    public static boolean isOverIntervalLimit(final LocalDate startDate, final LocalDate endDate, final int interval) {
        return getDaysBetween(startDate, endDate) >= interval;
    }

    /**
     * 获取昨天的日期 格式串:yyyy-MM-dd.
     *
     * @return yyyy -MM-dd
     */
    public static String getYesterday() {
        return getYesterday(LocalDate.now());
    }

    /**
     * 获取昨天的日期 格式串:yyyy-MM-dd.
     *
     * @param date 时间
     * @return yyyy -MM-dd
     */
    public static String getYesterday(final LocalDate date) {
        return date.minusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
    }

    /**
     * 上月第一天.
     *
     * @return 日期 previous month first day
     */
    public static LocalDate getPreviousMonthFirstDay() {

        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 上月最后一天.
     *
     * @return 日期 previous month last day
     */
    public static LocalDate getPreviousMonthLastDay() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获得当天0点时间.
     *
     * @return 日期 timesmorning
     */
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得当天近一周.
     *
     * @return 日期 a week from now
     */
    public static LocalDateTime getAWeekFromNow() {
        LocalDateTime date = LocalDateTime.now();
        //7天前
        return date.plusWeeks(-1);
    }

    /**
     * 获得当天近一月.
     *
     * @return 日期 a month from now
     */
    public static LocalDateTime getAMonthFromNow() {
        LocalDateTime date = LocalDateTime.now();
        //一个月前
        return date.plusMonths(-1);
    }

    /**
     * 获得当天近一年.
     *
     * @return 日期 a year from now
     */
    public static LocalDateTime getAYearFromNow() {
        LocalDateTime date = LocalDateTime.now();
        //一年前
        return date.plusYears(-1);
    }


    /**
     * 获取utc时间.
     *
     * @return LocalDateTime utc local date time
     */
    public static LocalDateTime getUTCLocalDateTime() {
        return LocalDateTime.now(UTC_ZONE);
    }


    /**
     * 将当前时区时间转成UTC时间.
     *
     * @param dateTime 时间
     * @return LocalDateTime local date time
     */
    public static LocalDateTime toUTCDateTime(final LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            Instant instant = dateTime.toInstant(DEFAULT_ZONE.getRules().getOffset(dateTime));
            return LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), ZoneOffset.UTC);
        }
    }


    /**
     * 将UTC时间转成当前时区时间.
     *
     * @param dateTime 时间
     * @return LocalDateTime local date time
     */
    public static LocalDateTime toDefaultDateTime(final LocalDateTime dateTime) {
        return dateTime == null ? null : LocalDateTime.ofInstant(dateTime.toInstant(ZoneOffset.UTC), DEFAULT_ZONE);
    }


    /**
     * 将数据库时间改成当前时区时间.
     *
     * @param timestamp 时间
     * @return time local date time
     * @date 2018 /5/9 18:25
     */
    public static LocalDateTime toDefaultDateTime(final Timestamp timestamp) {
        return timestamp == null ? null : toDefaultDateTime(timestamp.toLocalDateTime());
    }

    /**
     *    将 Date 转换为 LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        // 获取系统默认时区
        ZoneId zoneId = ZoneId.systemDefault();
        // 将 Date 转换为 Instant
        Instant instant = date.toInstant();
        // 将 Instant 转换为 LocalDateTime
        return LocalDateTime.ofInstant(instant, zoneId);
    }
    /**
     * 格式化日期范围
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 格式化后的日期范围
     */
    public static String formatDateRange(Date startTime, Date endTime) {
        return formatDateRange(convertDateToLocalDateTime(startTime), convertDateToLocalDateTime(endTime));
    }


    public static String formatDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M月d日", Locale.CHINESE);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate startDate = startTime.toLocalDate();
        String startDateStr = startDate.format(dateFormatter);
        String startTimeStr = startTime.format(timeFormatter);
        String endTimeStr = endTime.format(timeFormatter);
        String suffix; // 后缀部分
        // 判断是否是今天或明天
        if (startDate.equals(today)) {
            suffix = "（今天）";
        } else if (startDate.equals(tomorrow)) {
            suffix = "（明天）";
        } else {
            // 如果不是今天或明天，显示星期几
            String weekDay = startDate.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.CHINESE);
            suffix = "（" + weekDay + "）";
        }

        // 返回完整的格式化字符串
        return startDateStr + suffix + " " + startTimeStr + " - " + endTimeStr;
    }


    private static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public static void main(String[] args) {


        // 测试代码
        LocalDateTime startTimeToday = LocalDateTime.now().withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTimeToday = startTimeToday.plusMinutes(30);

        LocalDateTime startTimeTomorrow = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTimeTomorrow = startTimeTomorrow.plusMinutes(30);

        LocalDateTime startTimeOtherDay = LocalDateTime.now().plusDays(3).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTimeOtherDay = startTimeOtherDay.plusMinutes(30);

        System.out.println(formatDateRange(startTimeToday, endTimeToday)); // 今天的时间段
        System.out.println(formatDateRange(startTimeTomorrow, endTimeTomorrow)); // 明天的时间段
        System.out.println(formatDateRange(startTimeOtherDay, endTimeOtherDay)); // 其他日期的时间段


    }


}
