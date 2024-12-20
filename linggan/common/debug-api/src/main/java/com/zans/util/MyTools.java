package com.zans.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type My tools.
 *
 * @author pancm
 * @Title: MyTools
 * @Description:常用工具类
 * @Version:1.0.1
 * @date 2017年9月26日
 */
public final class MyTools {
    /**
     * 时间格式包含毫秒
     */
    private static final String sdfm = "yyyy-MM-dd HH:mm:ss SSS";
    /**
     * 普通的时间格式
     */
    private static final String sdf = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间戳格式
     */
    private static final String sd = "yyyyMMddHHmmss";
    /**
     * 年月格式
     */
    private static final String ym = "yyyyMM";
    /**
     * 检查是否为整型
     */
    private static Pattern p = Pattern.compile("^\\d+$");


    private static Pattern linePattern = Pattern.compile("_([a-z])");

    private static Pattern humpPattern = Pattern.compile("\\B(\\p{Upper})(\\p{Lower}*)");

    /**
     * @return
     * @Author beixing
     * @Description equals方法实现
     * StringUtils copy
     * @Date 2021/3/31
     * @Param
     **/
    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 != null && cs2 != null) {
            return cs1 instanceof String && cs2 instanceof String ? cs1.equals(cs2) : regionMatches(cs1, false, 0, cs2, 0, Math.max(cs1.length(), cs2.length()));
        }
        return false;
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        if (str1 != null && str2 != null) {
            if (str1 == str2) {
                return true;
            }
            return str1.length() != str2.length() ? false : regionMatches(str1, true, 0, str2, 0, str1.length());

        }
        return str1 == str2;

    }


    private static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        } else {
            int index1 = thisStart;
            int index2 = start;
            int var8 = length;

            while (var8-- > 0) {
                char c1 = cs.charAt(index1++);
                char c2 = substring.charAt(index2++);
                if (c1 != c2) {
                    if (!ignoreCase) {
                        return false;
                    }
                    if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * 判断String类型的数据是否为空 null,""," " 为true "A"为false
     *
     * @param str the str
     * @return boolean boolean
     */
    public static boolean isEmpty(String str) {
        return (null == str || str.trim().length() == 0);
    }

    /**
     * 判断String类型的数据是否为空 null,"", " " 为false "A", 为true
     *
     * @param str the str
     * @return boolean boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断list类型的数据是否为空 null,[] 为 true
     *
     * @param list the list
     * @return boolean boolean
     */
    public static boolean isEmpty(List<?> list) {
        return (null == list || list.size() == 0);
    }

    /**
     * 判断list类型的数据是否为空 null,[] 为 false
     *
     * @param list the list
     * @return boolean boolean
     */
    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }

    /**
     * 判断Map类型的数据是否为空 null,[] 为true
     *
     * @param map the map
     * @return boolean boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (null == map || map.size() == 0);
    }

    /**
     * 判断map类型的数据是否为空 null,[] 为 false
     *
     * @param map the map
     * @return boolean boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断JSONObject类型的数据是否为空 null,[] 为true
     *
     * @param json the json
     * @return boolean boolean
     */
    public static boolean isEmpty(JSONObject json) {
        return (null == json || json.size() == 0);
    }

    /**
     * 判断json类型的数据是否为空 null,[] 为 false
     *
     * @param json the json
     * @return boolean boolean
     */
    public static boolean isNotEmpty(JSONObject json) {
        return !isEmpty(json);
    }


    /**
     * 是否是空的或为0，为null或空字符串或空列表或为0
     *
     * @param target
     * @return
     */
    public static boolean isEmpty(Object target) {
        if (target == null) {
            return true;
        }
        if (target.getClass().equals(Integer.class)) {
            return ((Integer) target) == 0;
        }
        if (target.getClass().equals(String.class)) {
            return ((String) target).isEmpty() || ((String) target).replaceAll("\\s", "").isEmpty();
        }
        if (target.getClass().equals(String[].class)) {
            return ((String[]) target).length == 0;
        }
        if (target instanceof List) {
            return ((List) target).isEmpty();
        }
        if (target instanceof Map) {
            return ((Map) target).isEmpty();
        }
        return false;
    }


    /**
     * @return int
     * @Author pancm
     * @Description char 类型转成int类型
     * @Date 2019/8/2
     * @Param []
     **/
    public static int charTransformInt(char c) {
        return Character.getNumericValue(c);
    }

    /**
     * 字符串反转 如:入参为abc，出参则为cba
     *
     * @param str the str
     * @return string
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return reverse(str.substring(1)) + str.charAt(0);
    }

    /**
     * 获取当前long类型的的时间
     *
     * @return long now long time
     */
    public static long getNowLongTime() {
        return System.currentTimeMillis();
    }

    /**
     * long类型的时间转换成 yyyyMMddHHmmss String类型的时间
     *
     * @param lo long类型的时间
     * @return string
     */
    public static String longTime2StringTime(long lo) {
        return longTime2StringTime(lo, sd);
    }

    /**
     * long类型的时间转换成自定义时间格式
     *
     * @param lo     long类型的时间
     * @param format 时间格式
     * @return String string
     */
    public static String longTime2StringTime(long lo, String format) {
        return new SimpleDateFormat(format).format(lo);
    }


    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取月份的第一天
     * @Date 2019/12/2
     * @Param [time] yyyy-MM-dd
     **/
    public static String getFirstDay(String time) {
        return LocalDate.parse(time).with(TemporalAdjusters.firstDayOfMonth()).toString();
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取月份的最后一天
     * @Date 2019/12/2
     * @Param [time] yyyy-MM-dd
     **/
    public static String getLastDay(String time) {
        return LocalDate.parse(time).with(TemporalAdjusters.lastDayOfMonth()).toString();
    }


    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取上个月份的最后一天
     * @Date 2019/12/2
     * @Param [time] yyyy-MM-dd
     **/
    public static String getAgoLastDay(String time) {
        return LocalDate.parse(time).with(TemporalAdjusters.firstDayOfMonth()).minusDays(1).toString();
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取下个月份的第一天
     * @Date 2019/12/2
     * @Param [time] yyyy-MM-dd
     **/
    public static String getOffFirstDay(String time) {
        return LocalDate.parse(time).with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).toString();
    }

    /**
     * 获取设置的时间
     *
     * @param hour   the hour
     * @param minute the minute
     * @param second the second
     * @return set time
     */
    @SuppressWarnings("static-access")
    public static Date getSetTime(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY, hour); // 控制时
        calendar.set(calendar.MINUTE, minute); // 控制分
        calendar.set(calendar.SECOND, second); // 控制秒
        return calendar.getTime();
    }

    /**
     * String类型的时间转换成 long
     *
     * @param time   the time
     * @param format the format
     * @return String long
     * @throws ParseException the parse exception
     */
    public static long stringTime2LongTime(String time, String format) throws ParseException {
        if (isEmpty(format)) {
            format = sdf;
        }
        if (isEmpty(time)) {
            time = getNowTime(format);
        }
        SimpleDateFormat sd = new SimpleDateFormat(format);
        Date date = sd.parse(time);
        return date.getTime();
    }

    /**
     * 格式化时间
     *
     * @param format1 之前的 时间格式
     * @param format2 之后的 时间格式
     * @param time    时间
     * @return String string
     * @throws ParseException the parse exception
     */
    public static String formatTime(String format1, String format2, String time) throws ParseException {
        SimpleDateFormat d1 = new SimpleDateFormat(format1);
        SimpleDateFormat d2 = new SimpleDateFormat(format2);
        time = d2.format(d1.parse(time));
        return time;
    }


    public static String addDay(String time) {
        time = formatTime1(time);
        time = addPlusDay(time, 1);
        time = formatTime2(time);
        return time;
    }

    public static String formatTime(String time) {
        String ym = time.substring(4, 7).replaceAll("-", "");
        return ym;
    }

    public static String formatTime1(String time) {
        String ym = time.substring(0, 4).concat("-").concat(time.substring(4, 6).concat("-")).concat(time.substring(6, 8));
        return ym;
    }

    public static String formatTime2(String time) {
        String ym = time.substring(0, 10).replaceAll("-", "");
        return ym;
    }

    /**
     * 增加天数
     *
     * @param time 格式为yyyy-MM-dd
     * @param
     * @return
     */
    public static String addPlusDay(String time, int day) {
        return LocalDate.parse(time).plusDays(day).toString();
    }

    /**
     * 时间补全 例如将2018-04-04补全为2018-04-04 00:00:00.000
     *
     * @param time 补全的时间
     * @return string
     */
    public static String complementTime(String time) {
        return complementTime(time, sdfm, 1);

    }

    /**
     * 时间补全 例如将2018-04-04补全为2018-04-04 00:00:00.000
     *
     * @param time   补全的时间
     * @param format 补全的格式
     * @param type   类型 1:起始;2:终止
     * @return string
     */
    public static String complementTime(String time, String format, int type) {
        if (isEmpty(time) || isEmpty(format)) {
            return null;
        }
        int tlen = time.length();
        int flen = format.length();
        int clen = flen - tlen;
        if (clen <= 0) {
            return time;
        }
        StringBuffer sb = new StringBuffer(time);
        if (clen == 4) {
            if (type == 1) {
                sb.append(".000");
            } else {
                sb.append(".999");
            }
        } else if (clen == 9) {
            if (type == 1) {
                sb.append(" 00:00:00");
            } else {
                sb.append(" 23:59:59");
            }
        } else if (clen == 13) {
            if (type == 1) {
                sb.append(" 00:00:00.000");
            } else {
                sb.append(" 23:59:59.999");
            }
        }
        return sb.toString();

    }

    /**
     * 获取当前String类型的的时间 使用默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @return String now time
     */
    public static String getNowTime() {
        return getNowTime(sdf);
    }

    /**
     * 获取当前String类型的的时间(自定义格式)
     *
     * @param format 时间格式
     * @return String now time
     */
    public static String getNowTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 获取当前Timestamp类型的的时间
     *
     * @return Timestamp t now time
     */
    public static Timestamp getTNowTime() {
        return new Timestamp(getNowLongTime());
    }

    /**
     * 获取的String类型的当前时间并更改时间
     *
     * @param number 要更改的的数值
     * @param format 更改时间的格式 如yyyy-MM-dd HH:mm:ss
     * @param type   更改时间的类型 时:h; 分:m ;秒:s
     * @return String string
     */
    public static String changeTime(int number, String format, String type) {
        return changeTime(number, format, type, "");
    }

    /**
     * 获取的String类型时间并更改时间
     *
     * @param number 要更改的的数值
     * @param format 更改时间的格式
     * @param type   更改时间的类型 。时:h; 分:m ;秒:s
     * @param time   更改的时间 没有则取当前时间
     * @return String string
     */
    public static String changeTime(int number, String format, String type, String time) {
        if (isEmpty(time)) { // 如果没有设置时间则取当前时间
            time = getNowTime(format);
        }
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        Date d = null;
        Calendar ca = null;
        String backTime = null;
        try {
            d = format1.parse(time);
            ca = Calendar.getInstance(); // 定义一个Calendar 对象
            ca.setTime(d);// 设置时间
            if ("h".equals(type)) {
                ca.add(Calendar.HOUR, number);// 改变时
            } else if ("m".equals(type)) {
                ca.add(Calendar.MINUTE, number);// 改变分
            } else if ("s".equals(type)) {
                ca.add(Calendar.SECOND, number);// 改变秒
            }
            backTime = format1.format(ca.getTime()); // 转化为String 的格式
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backTime;
    }

    /**
     * 两个日期带时间比较 第二个时间大于第一个则为true，否则为false
     *
     * @param time1  the time 1
     * @param time2  the time 2
     * @param format the format
     * @return boolean boolean
     * @throws ParseException
     */
    public static boolean isCompareDay(String time1, String time2, String format) {
        if (isEmpty(format)) {// 如果没有设置格式使用默认格式
            format = sdf;
        }
        SimpleDateFormat s1 = new SimpleDateFormat(format);
        Date t1 = null;
        Date t2 = null;
        try {
            t1 = s1.parse(time1);
            t2 = s1.parse(time2);
            return t2.after(t1);// 当 t2 大于 t1 时，为 true，否则为 false
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @return int
     * @Author pancm
     * @Description 获取月份的天数
     * @Date 2019/7/12
     * @Param [ym2]
     **/
    public static int getMonthDays(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(ym);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(time));
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取几天之前的时间
     *
     * @param day the day
     * @return minus days
     * @since 1.8
     */
    public static String getMinusDays(int day) {
        return getMinusDays(day, sdf);
    }

    /**
     * 获取几天之前的时间
     *
     * @param day    the day
     * @param format the format
     * @return minus days
     * @since 1.8
     */
    public static String getMinusDays(int day, String format) {
        return LocalDateTime.now().minusDays(day).format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取指定时间前几天的数据
     * @Date
     * @Param [day, time, format]
     **/
    public static String getMinusDays(int day, String time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format)).minusDays(day).format(DateTimeFormatter.ofPattern(format));
    }


    /**
     * 获取几天之后的时间
     *
     * @param day the day
     * @return plus days
     * @since 1.8
     */
    public static String getPlusDays(int day) {
        return getPlusDays(day, sdf);
    }

    /**
     * 获取几天之后的时间
     *
     * @param day    the day
     * @param format the format
     * @return plus days
     * @since 1.8
     */
    public static String getPlusDays(int day, String format) {
        return LocalDateTime.now().plusDays(day).format(DateTimeFormatter.ofPattern(format));
    }


    /**
     * 获取几天之后的时间
     *
     * @param month the month
     * @return plus months
     * @since 1.8
     */
    public static String getPlusMonths(int month) {
        return getPlusMonths(month, sdf);
    }

    /**
     * 获取几月之后的时间
     *
     * @param month  the month
     * @param format the format
     * @return plus months
     * @since 1.8
     */
    public static String getPlusMonths(int month, String format) {
        return LocalDateTime.now().plusMonths(month).format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 增加月份
     *
     * @param time  格式为yyyy-MM-dd
     * @param month 增加月份
     * @return string
     */
    public static String addPlusMonths(String time, int month) {
        return LocalDate.parse(time).plusMonths(month).toString();
    }

    /**
     * 时间相比得月份 如果是201711和201801相比，返回的结果是2 前面的时间要小于后面的时间
     *
     * @param month   格式为yyyyMM
     * @param toMonth 格式为yyyyMM
     * @return int
     * @since jdk 1.8
     */
    public static int diffMonth(String month, String toMonth) {
        int year1 = Integer.parseInt(month.substring(0, 4));
        int month1 = Integer.parseInt(month.substring(4, 6));
        int year2 = Integer.parseInt(month.substring(0, 4));
        int month2 = Integer.parseInt(month.substring(4, 6));
        LocalDate ld1 = LocalDate.of(year1, month1, 01);
        LocalDate ld2 = LocalDate.of(year2, month2, 01);
        return Period.between(ld1, ld2).getMonths();
    }

    /**
     * 判断是否为整型
     *
     * @param str the str
     * @return boolean boolean
     */
    public static boolean isInteger(String str) {
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 自定义位数产生随机数字
     *
     * @param count the count
     * @return String string
     */
    public static String random(int count) {
        char start = '0';
        char end = '9';
        Random rnd = new Random();
        char[] result = new char[count];
        int len = end - start + 1;
        while (count-- > 0) {
            result[count] = (char) (rnd.nextInt(len) + start);
        }
        return new String(result);
    }

    /**
     * 获取自定义长度的随机数(含字母)
     *
     * @param len 长度
     * @return String string
     */
    public static String random2(int len) {
        int random = Integer.parseInt(random(5));
        Random rd = new Random(random);
        final int maxNum = 62;
        StringBuffer sb = new StringBuffer();
        int rdGet;// 取得随机数
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9'};
        int count = 0;
        while (count < len) {
            rdGet = Math.abs(rd.nextInt(maxNum));// 生成的数最大为62-1
            if (rdGet >= 0 && rdGet < str.length) {
                sb.append(str[rdGet]);
                count++;
            }
        }
        return sb.toString();
    }

    /**
     * 获取本机ip
     *
     * @return String local host ip
     * @throws UnknownHostException the unknown host exception
     */
    public static String getLocalHostIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 获取本地IP地址
     *
     * @throws SocketException
     */
    public static String getLocalIP() throws UnknownHostException, SocketException {
        if (isWindowsOS()) {
            return InetAddress.getLocalHost().getHostAddress();
        } else {
            return getLinuxLocalIp();
        }
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本地Host名称
     */
    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                                System.out.println(ipaddress);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("获取ip地址异常");
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        System.out.println("IP:" + ip);
        return ip;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * Object 转换为 String
     *
     * @param obj the obj
     * @return String string
     */
    public static String toString(Object obj) {
        return JSON.toJSONString(obj);
    }


    public static String toString2(Object obj) {
        return toString(obj, sdf);
    }

    /**
     * Object 转换为 String
     * 不将时间格式转换为时间戳
     *
     * @param obj the obj
     * @return String string
     */
    public static String toString(Object obj, String format) {
        return JSON.toJSONStringWithDateFormat(obj, format, SerializerFeature.WriteDateUseDateFormat);
    }


    /**
     * JSON 转换为 JavaBean
     *
     * @param <T>  the type parameter
     * @param json the json
     * @param t    the t
     * @return <T>
     */
    public static <T> T toBean(JSONObject json, Class<T> t) {
        return JSON.toJavaObject(json, t);
    }

    /**
     * JSON 字符串转换为 JavaBean
     *
     * @param <T> the type parameter
     * @param str the str
     * @param t   the t
     * @return <T>
     */
    public static <T> T toBean(String str, Class<T> t) {
        return JSON.parseObject(str, t);
    }

    /**
     * JSON 字符串 转换成JSON格式
     *
     * @param str the str
     * @return JSONObject json object
     */
    public static JSONObject toJson(String str) {
        if (isEmpty(str)) {
            return new JSONObject();
        }
        return JSON.parseObject(str);

    }

    /**
     * JavaBean 转化为JSON
     *
     * @param t the t
     * @return json object
     */
    public static JSONObject toJson(Object t) {
        if (null == t || "".equals(t)) {
            return new JSONObject();
        }
        return (JSONObject) JSON.toJSON(t);
    }

    /**
     * JSON 字符串转换为 HashMap
     *
     * @param json - String
     * @return Map map
     */
    @SuppressWarnings("rawtypes")
    public static Map toMap(String json) {
        if (isEmpty(json)) {
            return new HashMap();
        }
        return JSON.parseObject(json, HashMap.class);
    }


    /**
     * 将map转化为string
     *
     * @param m the m
     * @return string
     */
    @SuppressWarnings("rawtypes")
    public static String toString(Map m) {
        return JSONObject.toJSONString(m);
    }

    /**
     * String转换为数组
     *
     * @param <T>  the type parameter
     * @param text the text
     * @return object [ ]
     */
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);
    }

    /**
     * String转换为数组
     *
     * @param <T>   the type parameter
     * @param text  the text
     * @param clazz the clazz
     * @return object [ ]
     */
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }


    /**
     * String转换为List
     *
     * @param text
     * @return
     */
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    /**
     * list 转化为JSONArray
     *
     * @param
     * @return
     */
    public static JSONArray toJSONArray(List list) {
        return JSONArray.parseArray(JSON.toJSONString(list));
    }


    /**
     * JSONArray转化为list
     *
     * @param
     * @return
     */
    public static <T> List<T> toList(JSONArray jsonArray, Class<T> clazz) {
        return JSONObject.parseArray(jsonArray.toJSONString(), clazz);
    }

    /**
     * name1=value1&name2=value2格式的数据转换成json数据格式
     *
     * @param str the str
     * @return json object
     */
    public static JSONObject str2Json(String str) {
        if (isEmpty(str)) {
            return new JSONObject();
        }
        JSONObject json = new JSONObject();
        String[] str1 = str.split("&");
        String str3 = "", str4 = "";
        if (null == str1 || str1.length == 0) {
            return new JSONObject();
        }
        for (String str2 : str1) {
            str3 = str2.substring(0, str2.lastIndexOf("="));
            str4 = str2.substring(str2.lastIndexOf("=") + 1, str2.length());
            json.put(str3, str4);
        }
        return json;
    }

    /**
     * json数据格式 转换成name1=value1&name2=value2格式
     *
     * @param json the json
     * @return string
     */
    @SuppressWarnings("rawtypes")
    public static String json2Str(JSONObject json) {
        if (isEmpty(json)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Iterator it = json.entrySet().iterator(); // 定义迭代器
        while (it.hasNext()) {
            Entry er = (Entry) it.next();
            sb.append(er.getKey());
            sb.append("=");
            sb.append(er.getValue());
            sb.append("&");
        }
        sb.delete(sb.length() - 1, sb.length()); // 去掉最后的&
        return sb.toString();
    }

    /**
     * 将JDBC查询的数据转换成List类型
     *
     * @param rs the rs
     * @return List list
     * @throws SQLException the sql exception
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List convertList(ResultSet rs) throws SQLException {
        if (null == rs) {
            return new ArrayList<>();
        }
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            JSONObject rowData = new JSONObject();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    /**
     * MD5加密
     *
     * @param message the message
     * @return string
     */
    public static String md5Encode(String message) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(message.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        int length = 32 - md5code.length();
        for (int i = 0; i < length; i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * base64 加密
     *
     * @param str
     * @return
     */
    public static String base64EnStr(String str) throws UnsupportedEncodingException {
        return base64EnStr(str, null);
    }

    /**
     * base64 加密
     *
     * @param str
     * @return
     */
    public static String base64EnStr(String str, String charsetName) throws UnsupportedEncodingException {
        if (isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        return Base64.getEncoder().encodeToString(str.getBytes(charsetName));
    }


    /**
     * base64 加密
     *
     * @param str
     * @return
     */
    public static String base64DeStr(String str) throws UnsupportedEncodingException {
        return base64DeStr(str, null);

    }

    /**
     * base64解密
     *
     * @param encodeStr
     * @return
     */
    public static String base64DeStr(String encodeStr, String charsetName) throws UnsupportedEncodingException {
        if (isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        byte[] decodeStr = Base64.getDecoder().decode(encodeStr);
        return new String(decodeStr, charsetName);
    }


    /**
     * 获取2^n-1的平方 n=1时返回1
     */
    public static int getNum(int num) {
        if (num == 1) {
            return 1;
        }
        return (int) Math.pow(2, num - 1);
    }


    /**
     * 匹配号段
     *
     * @param phone the phone
     * @return the int
     */
    public static int matchPhone(String phone) {
        // 移动的号段
        String str = "1340,1341,1342,1343,1344,1345,1346,1347,1348,135,136,137,138,139,147,148,150,151,152,154,157,158,159,165,1703,1705,1706,172,178,182,183,184,187,188,198";
        List<String> list = completionData(str);
        if (list.contains(phone)) {
            return 0;
        }
        // 联通的号段
        String str2 = "130,131,132,145,146,155,156,166,167,1704,1707,1708,1709,171,175,176,185,186";
        List<String> list2 = completionData(str2);
        if (list2.contains(phone)) {
            return 1;
        }
        // 电信的号段
        String str3 = "133,1349,149,153,1700,1701,1702,173,1740,177,180,181,189,191,199";
        List<String> list3 = completionData(str3);
        if (list3.contains(phone)) {
            return 21;
        }
        return 0;
    }

    /**
     * 补全号段
     *
     * @param str the str
     * @return the list
     */
    public static List<String> completionData(String str) {
        String[] strs = str.split(",");
        List<String> list = new ArrayList<String>();
        for (String s : strs) {
            if (s.length() == 3) {
                for (int i = 0; i < 10; i++) {
                    String s1 = s + i;
                    list.add(s1);
                }
            } else {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 十进制转二进制
     *
     * @param n the n
     * @return string
     */
    public static String decToBinary(int n) {
        String str = "";
        while (n != 0) {
            str = n % 2 + str;
            n = n / 2;
        }
        return str;
    }

    /**
     * 二进制转十进制
     *
     * @param cs the cs
     * @return int
     */
    public static int binaryToDec(char[] cs) {
        return binaryToDec(cs);
    }

    /**
     * 二进制转十进制
     *
     * @param cs the cs
     * @return int
     */
    public static int binaryToDec(String cs) {
        return new BigInteger(new String(cs), 2).intValue();
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param value 要转换的int值
     * @return byte数组 byte [ ]
     */
    public static byte[] intToBytes(int value) {
        byte[] byte_src = new byte[4];
        byte_src[3] = (byte) ((value & 0xFF000000) >> 24);
        byte_src[2] = (byte) ((value & 0x00FF0000) >> 16);
        byte_src[1] = (byte) ((value & 0x0000FF00) >> 8);
        byte_src[0] = (byte) ((value & 0x000000FF));
        return byte_src;
    }

    /**
     * byte 数组拼接 使用说明 byte []a= {1,2},b= {3,4}; 那么 byte []c=addBytes(a,b);
     * c={1,2,3,4};
     *
     * @param data1 the data 1
     * @param data2 the data 2
     * @return data1 与 data2拼接的结果
     */
    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }


    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取请求的body数据
     * @Date 2020/3/15
     * @Param [request]
     **/
    public static String ReadAsChars(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String ReadAsChars2(HttpServletRequest request) {
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = request.getInputStream();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                sb.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转驼峰 (Map)
     *
     * @param map
     * @return
     */
    public static Map<String, Object> lineToHump(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            StringBuffer sb = new StringBuffer();
            Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            Matcher matcher = linePattern.matcher(key);
            while (matcher.find()) {
                matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            }
            matcher.appendTail(sb);
            newMap.put(sb.toString(), entry.getValue());
        }
        return newMap;
    }

    /**
     * 下划线转驼峰(List)
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> lineToHumpList(List<Map<String, Object>> list) {
        List<Map<String, Object>> res = new ArrayList<>();
        list.stream().forEach(p -> {
            Map<String, Object> newMap = new HashMap<>();
            Iterator<Entry<String, Object>> it = p.entrySet().iterator();
            while (it.hasNext()) {
                StringBuffer sb = new StringBuffer();
                Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                Matcher matcher = linePattern.matcher(key);
                while (matcher.find()) {
                    matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
                }
                matcher.appendTail(sb);
                newMap.put(sb.toString(), entry.getValue());
            }
            res.add(newMap);
        });
        return res;
    }

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = humpPattern.matcher(str);
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 驼峰转下划线(Map)
     *
     * @param map
     * @return
     */
    public static Map<String, Object> humpToLine(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            StringBuffer sb = new StringBuffer();
            Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            Matcher matcher = humpPattern.matcher(key);
            while (matcher.find()) {
                matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
            }
            matcher.appendTail(sb);
            newMap.put(sb.toString(), entry.getValue());
        }
        return newMap;
    }


    /**
     * 驼峰转下划线(List)
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> humpToLineList(List<Map<String, Object>> list) {
        List<Map<String, Object>> res = new ArrayList<>();
        list.stream().forEach(p -> {
            Map<String, Object> newMap = new HashMap<>();
            Iterator<Entry<String, Object>> it = p.entrySet().iterator();
            while (it.hasNext()) {
                StringBuffer sb = new StringBuffer();
                Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                Matcher matcher = humpPattern.matcher(key);
                while (matcher.find()) {
                    matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
                }
                matcher.appendTail(sb);
                newMap.put(sb.toString(), entry.getValue());
            }
            res.add(newMap);
        });
        return res;
    }





    /**
     * <p>Gets the substring after the first occurrence of a separator.
     * The separator is not returned.</p>
     *
     * <p>A {@code null} string input will return {@code null}.
     * An empty ("") string input will return the empty string.
     * A {@code null} separator will return the empty string if the
     * input string is not {@code null}.</p>
     *
     * <p>If nothing is found, the empty string is returned.</p>
     *
     * <pre>
     * StringUtils.substringAfter(null, *)      = null
     * StringUtils.substringAfter("", *)        = ""
     * StringUtils.substringAfter(*, null)      = ""
     * StringUtils.substringAfter("abc", "a")   = "bc"
     * StringUtils.substringAfter("abcba", "b") = "cba"
     * StringUtils.substringAfter("abc", "c")   = ""
     * StringUtils.substringAfter("abc", "d")   = ""
     * StringUtils.substringAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring after the first occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringAfter(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(0, pos);
    }


    private static final Pattern pattern = Pattern.compile("\\{(.*?)\\}");


    /**
     * 格式化字符串 字符串中使用{key}表示占位符
     *
     * @param sourStr 需要匹配的字符串
     * @param param   参数集
     * @return
     */
    public static String format(String sourStr, Map<String, Object> param) {
        Matcher matcher = pattern.matcher(sourStr);
        while (matcher.find()) {
            String key = matcher.group();
            String keyClone = key.substring(1, key.length() - 1).trim();
            Object value = param.get(keyClone);
            if (value != null)
                sourStr = sourStr.replace(key, value.toString());
        }
        return sourStr;
    }

    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author pancm
     * @Description 得到map的差集
     * @Date 2020/7/30
     * @Param [bigMap, smallMap]
     **/
    private Map<String, Object> getDifferenceMap(Map<String, Object> bigMap, Map<String, Object> smallMap) {
        Set<String> bigMapKey = bigMap.keySet();
        Set<String> smallMapKey = smallMap.keySet();
        Set<String> differenceSet = Sets.difference(bigMapKey, smallMapKey);
        Map<String, Object> result = Maps.newHashMap();
        for (String key : differenceSet) {
            result.put(key, bigMap.get(key));
        }
        return result;
    }


    public static String millisToStringShort(long l) {
        StringBuffer sb = new StringBuffer();
        long minutes = 1;
        long hours = 60 * minutes;
        long days = 24 * hours;
        if (l / days >= 1)
            sb.append((int) (l / days) + "天");
        if (l % days / hours >= 1)
            sb.append((int) (l % days / hours) + "小时");
        if (l % days % hours / minutes >= 1)
            sb.append((int) (l % days % hours / minutes) + "分钟");

        return sb.toString();
    }

    public static String formatMacTo12Bit(String mac) {
        return mac.replace(" ", "")
                .replace("-", "")
                .replace(":", "")
                .toLowerCase();
    }




    //public static void main(String[] args) {
    //    String url = "https://xxx.com/cfes?c={campaign_name}&af_siteid={af_siteid}&clickid={clickid}&android_id={android_id}&advertising_id={advertising_id}&idfa={idfa}";
    //    Map<String, Object> map = new LinkedHashMap<>();
    //    map.put("campaign_name", "111");
    //    map.put("af_siteid", "222");
    //    map.put("clickid", "333");
    //    map.put("android_id", "444");
    //    map.put("advertising_id", "555");
    //    map.put("idfa", "");
    //    System.out.println(format(url, map));
    //}


    /**
     * 本方法的测试示例
     *
     * @param args the input arguments
     */
    @SuppressWarnings({"rawtypes"})
    public static void main(String[] args) throws IOException {
        String path = "D:\\service_diagnosis_api.sh";
        String path2 = "D:\\service_diagnosis_api.sh.zip";
        String fileName = "";
        String t = "123";

    }

}
