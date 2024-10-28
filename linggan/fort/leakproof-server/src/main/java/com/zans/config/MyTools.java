package com.zans.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @Title: MyTools
 * @Description:常用工具类
 * @Version:1.0.1
 * @author pancm
 * @date 2017年9月26日
 */
public final class MyTools {
	/** 时间格式包含毫秒 */
	private static final String sdfm = "yyyy-MM-dd HH:mm:ss.SSS";
	/** 普通的时间格式 */
	private static final String sdf = "yyyy-MM-dd HH:mm:ss";
	/** 时间戳格式 */
	private static final String sd = "yyyyMMddHHmmss";
	private static final String ym= "yyyyMM";

	/** 检查是否为整型 */
	private static  Pattern p=Pattern.compile("^\\d+$");

	private static final Pattern pattern = Pattern.compile("\\{(.*?)\\}");

	/**
	 * 判断String类型的数据是否为空
	 * null,""," " 为true
	 * "A"为false
	 *
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return (null == str || str.trim().length() == 0);
	}
	public static <T> T requireNonEmpty(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        if(obj instanceof String){
        	if(((String)obj).trim().length()<=0)
        		throw new NullPointerException(message);
        }
        return obj;
    }
	/**
	 * 判断String类型的数据是否为空
	 * null,"", " " 为false
	 * "A", 为true
	 *
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断list类型的数据是否为空
	 * null,[] 为 true
	 *
	 * @return boolean
	 */
	public static boolean isEmpty(List<?> list) {
		return (null == list || list.size() == 0);
	}

	/**
	 * 判断list类型的数据是否为空
	 * null,[] 为 false
	 *
	 * @return boolean
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}

	/**
	 * 判断Map类型的数据是否为空
	 * null,[] 为true
	 *
	 * @return boolean
	 */
	public static boolean isEmpty(Map<?,?> map) {
		return (null == map || map.size()==0);
	}

	/**
	 * 判断map类型的数据是否为空
	 * null,[] 为 false
	 *
	 * @return boolean
	 */
	public static boolean isNotEmpty(Map<?,?> map) {
		return !isEmpty(map);
	}

	/**
	 * 判断JSONObject类型的数据是否为空
	 * null,[] 为true
	 *
	 * @return boolean
	 */
	public static boolean isEmpty(JSONObject json) {
		return (null == json || json.size()==0);
	}

	/**
	 * 判断json类型的数据是否为空
	 * null,[] 为 false
	 *
	 * @return boolean
	 */
	public static boolean isNotEmpty(JSONObject json) {
		return !isEmpty(json);
	}

	/**
	 * 字符串反转
	 * 如:入参为abc，出参则为cba
	 * @param str
	 * @return
	 */
	public static String reverse(String str) {
        if(isEmpty(str)){
            return str;
        }
        return reverse(str.substring(1)) + str.charAt(0);
    }
	/**
	 * 获取当前long类型的的时间
	 *
	 * @return long
	 */
	public static long getNowLongTime() {
		return System.currentTimeMillis();
	}
	/**
	 * long类型的时间转换成 yyyyMMddHHmmss String类型的时间
	 *
	 * @param lo long类型的时间
	 * @return
	 */
	public static String longTime2StringTime(long lo) {
		return longTime2StringTime(lo, sd);
	}
	/**
	 * long类型的时间转换成自定义时间格式
	 *
	 * @param lo     long类型的时间
	 * @param format  时间格式
	 * @return String
	 */
	public static String longTime2StringTime(long lo, String format) {
		return new SimpleDateFormat(format).format(lo);
	}
	/**
	 * 获取设置的时间
	 * @param hour 时
	 * @param minute 分
	 * @param second 秒
	 * @return
	 */
	 @SuppressWarnings("static-access")
	public static Date getSetTime(int hour,int minute,int second){
		 Calendar calendar = Calendar.getInstance();
		 calendar.set(calendar.HOUR_OF_DAY, hour); // 控制时
		 calendar.set(calendar.MINUTE, minute); // 控制分
		 calendar.set(calendar.SECOND, second); // 控制秒
		return calendar.getTime();
	 }
	/**
	 *  String类型的时间转换成 long
	 * @param
	 * @return String
	 * @throws ParseException
	 */
	 public static long stringTime2LongTime(String time,String format) throws ParseException{
		 if(isEmpty(format)){
			 format=sdf;
		 }
		 if(isEmpty(time)){
			 time=getNowTime(format);
		 }
		 SimpleDateFormat sd= new SimpleDateFormat(format);
		 Date date=sd.parse(time);
		 return date.getTime();
		}

	 /**
	  * 时间补全
	  * 例如将2018-04-04补全为2018-04-04 00:00:00.000
	  * @param time   补全的时间
	  * @return
	  */
	 public static String complementTime(String time){
		return complementTime(time,sdfm,1);

	 }

	 /**
	  * 时间补全
	  * 例如将2018-04-04补全为2018-04-04 00:00:00.000
	  * @param time   补全的时间
	  * @param format 补全的格式
	  * @param type   类型  1:起始;2:终止
	  * @return
	  */
	 public static String complementTime(String time,String format,int type){
		 if(isEmpty(time)||isEmpty(format)){
			 return null;
		 }
		int tlen= time.length();
		int flen= format.length();
		int clen=flen-tlen;
		if(clen<=0){
			return time;
		}
		StringBuffer sb=new StringBuffer(time);
		if(clen==4){
			if(type==1){
				sb.append(".000");
			}else{
				sb.append(".999");
			}
		}else if(clen==9){
			if(type==1){
				sb.append(" 00:00:00");
			}else{
				sb.append(" 23:59:59");
			}
		}else if(clen==13){
			if(type==1){
				sb.append(" 00:00:00.000");
			}else{
				sb.append(" 23:59:59.999");
			}
		}
		return sb.toString();

	 }


	/**
	 * 获取当前String类型的的时间 使用默认格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @return String
	 */
	public static String getNowTime() {
		return getNowTime(sdf);
	}

	/**
	 * 获取当前String类型的的时间(自定义格式)
	 * @param format  时间格式
	 * @return String
	 */
	public static String getNowTime(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * 获取当前Timestamp类型的的时间
	 * @return Timestamp
	 */
	public static Timestamp getTNowTime() {
		return new Timestamp(getNowLongTime());
	}

	/**
	 * 获取当前Timestamp类型的的时间
	 * @return Timestamp
	 */
	public static long stringToTimestamp(String time) {
		return  Timestamp.valueOf(time).getTime();
	}


	/**
	 * 获取当前类型时间的日
	 * @return Timestamp
	 */
	public static int getDD(String time,String format) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(format);
		Date d = format1.parse(time);
		Calendar ca = Calendar.getInstance();  //定义一个Calendar 对象
		ca.setTime(d);//设置时间
		return  ca.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取的String类型的当前时间并更改时间
	 * @param number  要更改的的数值
	 * @param format  更改时间的格式  如yyyy-MM-dd HH:mm:ss
	 * @param type   更改时间的类型    时:h; 分:m ;秒:s
	 * @return  String
	 */
	public static String changeTime(int number,String format,String type) {
		return changeTime(number,format,type,"");
	}

	/**
	 * 获取的String类型时间并更改时间
	 * @param number 要更改的的数值
     * @param format 更改时间的格式
	 * @param type   更改时间的类型 。时:h; 分:m ;秒:s
	 * @param time	  更改的时间       没有则取当前时间
	 * @return String
	 */
	public static String changeTime(int number,String format,String type,String time) {
		if(isEmpty(time)){ //如果没有设置时间则取当前时间
			time=getNowTime(format);
		}
		SimpleDateFormat format1 = new SimpleDateFormat(format);
		Date d=null;
		try {
			d = format1.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar ca = Calendar.getInstance();  //定义一个Calendar 对象
		if(ca!=null)ca.setTime(d);//设置时间
		if("h".equals(type)){
			ca.add(Calendar.HOUR, number);//改变时
		}else if("m".equals(type)){
			ca.add(Calendar.MINUTE, number);//改变分
		}else if("s".equals(type)){
			ca.add(Calendar.SECOND, number);//改变秒
		}
		String backTime = format1.format(ca.getTime());  //转化为String 的格式
		return backTime;
	}
	public  static Date string2Time(String dateString,String format) throws ParseException
	{
		 SimpleDateFormat dateFormat;
		  dateFormat = new SimpleDateFormat(format);//设定格式
		 // dateFormat.setLenient(false);
		  Date timeDate = dateFormat.parse(dateString);//util类型
		  return timeDate;
	}
	 /**
     * 获取区间时间段的随机日期
     *
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @param format
     *  		  时间格式
     * @return
     */

    public static String randomDate(String beginDate, String endDate,String format) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat(format);
            Date start = format1.parse(beginDate);// 构造开始日期
            Date end = format1.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime()) ;
            return format1.format(new Date(date)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }


	/**
	 * 获取几天之前的时间
	 *
	 * @since 1.8
	 * @param day
	 * @return
	 */
	public static String getMinusDays(int day) {
		return getMinusDays(day, sdf);
	}

	/**
	 * 获取几天之前的时间
	 *
	 * @since 1.8
	 * @param day
	 * @param format
	 * @return
	 */
	public static String getMinusDays(int day, String format) {
		return LocalDateTime.now().minusDays(day).format(DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 增加月份
	 *
	 * @param time
	 *            格式为yyyy-MM-dd
	 * @param month
	 *            增加月份
	 * @return
	 */
	public static String addPlusMonths(String time, int month) {
		return LocalDate.parse(time).plusMonths(month).toString();
	}

	/**
	 * 增加天数
	 *
	 * @param time
	 *            格式为yyyy-MM-dd
	 * @param day
	 *            增加天数
	 * @return
	 */
	public static String addPlusDay(String time, int day) {
		return LocalDate.parse(time).plusDays(day).toString();
	}

	/**
	 * 当前时间增加分钟
	 *
	 * @return
	 */
	public static LocalDateTime addPlusMinutes(int minutes) {
		return LocalDateTime.now().plusMinutes(minutes);
	}

	/**
	 * 获取几天前的数据
	 * @param time
	 *            格式为yyyy-MM-dd
	 * @param
	 *
	 * @return
	 */
	public static String getMinusDay(String time, int day) {
		return LocalDate.parse(time).minusDays(day).toString();
	}

	/**
	 * 获取几天前的数据
	 * @param time
	 *            格式为yyyy-MM-dd
	 * @param
	 *
	 * @return
	 */
	public static String getMinusDay2(String time, int day) {
		return LocalDateTime.parse(time).minusDays(day).toString();
	}
	/**
	 * 时间相比得月份 如果是201711和201801相比，返回的结果是2 前面的时间要小于后面的时间
	 *
	 * @param month
	 *            格式为yyyyMM
	 * @param toMonth
	 *            格式为yyyyMM
	 * @since jdk 1.8
	 * @return
	 */
	public static int diffMonth(String month, String toMonth) {
		int year1 = Integer.parseInt(month.substring(0, 4));
		int month1 = Integer.parseInt(month.substring(4, 6));
		int year2 = Integer.parseInt(toMonth.substring(0, 4));
		int month2 = Integer.parseInt(toMonth.substring(4, 6));
		LocalDate ld1 = LocalDate.of(year1, month1, 01);
		LocalDate ld2 = LocalDate.of(year2, month2, 01);
		return Period.between(ld1, ld2).getMonths();
	}

	/**
	 * 时间相比得天数 如果是20190831和20190901相比，返回的结果是1前面的时间要小于后面的时间
	 *
	 * @param month
	 *            格式为yyyyMMdd
	 * @param toMonth
	 *            格式为yyyyMMdd
	 * @since jdk 1.8
	 * @return
	 */
	public static int diffDay(String month, String toMonth) {
		int year1 = Integer.parseInt(month.substring(0, 4));
		int month1 = Integer.parseInt(month.substring(4, 6));
		int day1 = Integer.parseInt(month.substring(6, 8));
		int year2 = Integer.parseInt(toMonth.substring(0, 4));
		int month2 = Integer.parseInt(toMonth.substring(4, 6));
		int day2 = Integer.parseInt(toMonth.substring(6, 8));
		LocalDate ld1 = LocalDate.of(year1, month1, day1);
		LocalDate ld2 = LocalDate.of(year2, month2, day2);
		return Period.between(ld1, ld2).getDays();
	}

	/**
	 * 两个日期带时间比较
	 * 第二个时间大于第一个则为true，否则为false
	 * @param
	 * @return boolean
	 * @throws ParseException
	 */
    public static boolean isCompareDay(String time1,String time2,String format) {
		  if(isEmpty(format)){//如果没有设置格式使用默认格式
				format=sdf;
			}
		    SimpleDateFormat s1 = new SimpleDateFormat(format);
			Date t1=null;
			Date t2 =null;
			try {
				t1 = s1.parse(time1);
				t2=s1.parse(time2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(t1!=null&&t2!=null){
				return t2.after(t1);//当 t2 大于 t1 时，为 true，否则为 false
			}
			return false;
	    }

	/**
     * 判断是否为整型
     * @param
     * @return boolean
     */
    public static boolean isInteger(String str) {
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 自定义位数产生随机数字
	 * @param
	 * @return String
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
    * @param len  长度
    * @return String
    */
    public static String random2(int len){
    	int random= Integer.parseInt(random(5));
        Random rd = new Random(random);
        final int  maxNum = 62;
        StringBuffer sb = new StringBuffer();
        int rdGet;//取得随机数
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A','B','C','D','E','F','G','H','I','J','K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y' ,'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        int count=0;
        while(count < len){
            rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1
            if (rdGet >= 0 && rdGet < str.length) {
                sb.append(str[rdGet]);
                count ++;
            }
        }
        return sb.toString();
    }


	/**
	 * 获取本机ip
	 *
	 * @return String
	 * @throws UnknownHostException
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
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				String name = intf.getName();
				if (!name.contains("docker") && !name.contains("lo")) {
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
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
		System.out.println("IP:"+ip);
		return ip;
	}

	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 *
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 *
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
	 * 192.168.1.100
	 *
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
	 * @param
	 * @return String
	 */
	public static String toString(Object obj){
		 return JSON.toJSONString(obj);
	}


	/**
	 *  JSON 转换为 JavaBean
	 * @param json
	 * @param t
	 * @return <T>
	 */
	public static <T> T toBean(JSONObject json, Class<T> t){
		 return JSON.toJavaObject(json,t);
	}

	/**
	 *  JSON 字符串转换为 JavaBean
	 * @param str
	 * @param t
	 * @return <T>
	 */
	public static <T> T toBean(String str,Class<T> t){
		 return JSON.parseObject(str,  t);
	}


	/**
	 * base64 加密
	 *
	 * @param str
	 * @return
	 */
	public static String base64EnStr(String str) throws UnsupportedEncodingException {
		return  base64EnStr(str,null);
	}

	/**
	 * base64 加密
	 *
	 * @param str
	 * @return
	 */
	public static String base64EnStr(String str,String charsetName) throws UnsupportedEncodingException {
		if(isEmpty(charsetName)){
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
		return base64DeStr(str,null);

	}

	/**
	 * base64解密
	 *
	 * @param encodeStr
	 * @return
	 */
	public static String base64DeStr(String encodeStr,String charsetName) throws UnsupportedEncodingException {
		if(isEmpty(charsetName)){
			charsetName = "UTF-8";
		}
		byte[] decodeStr =  Base64.getDecoder().decode(encodeStr);
		return new String(decodeStr,charsetName);
	}


	/**
	 * JSON 字符串 转换成JSON格式
	 * @param
	 * @return JSONObject
	 */
	public static JSONObject toJson(String str){
		if(isEmpty(str)){
			return new JSONObject();
		}
		return JSON.parseObject(str);

	}

	/**
	 *  JavaBean 转化为JSON
	 * @param t
	 * @return
	 */
	public static JSONObject toJson(Object t){
		if(null==t||"".equals(t)){
			return new JSONObject();
		}
		return (JSONObject) JSON.toJSON(t);
	}

	/**
	 *  list 转化为JSONArray
	 * @param
	 * @return
	 */
	public static JSONArray toJSONArray(List list){
		return JSONArray.parseArray(JSON.toJSONString(list));
	}


	/**
	 *   JSONArray转化为list
	 * @param
	 * @return
	 */
	public static <T> List<T> toList(JSONArray jsonArray,Class<T> clazz){
		return JSONObject.parseArray(jsonArray.toJSONString(), clazz);
	}

	/**
	 * JSON 字符串转换为 HashMap
	 *
	 * @param json
	 *            - String
	 * @return Map
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
     * @param m
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static String toString(Map m) {
        return JSONObject.toJSONString(m);
    }

    public static String toString(Object obj,String dataFormat) {
        return JSON.toJSONStringWithDateFormat(obj, dataFormat, SerializerFeature.WriteDateUseDateFormat);
    }


      /**
       *  String转换为数组
       * @param text
       * @return
       */
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);
    }

    /**
     *  String转换为数组
     * @param text
     * @return
     */
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }


	public static JSONArray toJSONArray(String json) {
    	return JSONArray.parseArray(json);
	}

	/**
	 * name1=value1&name2=value2格式的数据转换成json数据格式
	 * @param str
	 * @return
	 */
    public static JSONObject str2Json(String str){
    	if(isEmpty(str)){
    		return new JSONObject();
    	}
    	JSONObject json=new JSONObject();
    	String [] str1=str.split("&");
    	String str3="",str4="";
    	if(null==str1||str1.length==0){
    		return new JSONObject();
    	}
    	for(String str2:str1){
    		str3=str2.substring(0, str2.lastIndexOf("="));
    		str4=str2.substring(str2.lastIndexOf("=")+1, str2.length());
    		json.put(str3, str4);
    	}
		return json;
    }


    /**
	 * json数据格式 转换成name1=value1&name2=value2格式
	 * @param
	 * @return
	 */
    @SuppressWarnings("rawtypes")
	public static String json2Str(JSONObject json){
    	 if(isEmpty(json)){
    		return null;
    	 }
    	  StringBuffer sb=new StringBuffer();
		  Iterator it=json.entrySet().iterator(); //定义迭代器
		  while(it.hasNext()){
			 Entry  er= (Entry) it.next();
			 sb.append(er.getKey());
			 sb.append("=");
			 sb.append(er.getValue());
			 sb.append("&");
		  }
		  sb.delete(sb.length()-1, sb.length()); //去掉最后的&
		return sb.toString();
    }


    /**
	 * 将JDBC查询的数据转换成List类型
	 * @param
	 * @return List
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List convertList(ResultSet rs) throws SQLException {
        if(null==rs){
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
     * @param message
     * @return
     */
    public static String md5Encode(String message) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    message.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        int length=32 - md5code.length();
        for (int i = 0; i <length ; i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
    /**
     * 输入流转字符串
     */
	 public static String inputstr2Str(InputStream in, String encode)
	   {
	       StringBuffer sb = new StringBuffer();
	       byte[] b = new byte[1024];
	       int len = 0;
	       try
	       {
	    	  // 默认以utf-8形式
	           if (encode == null || encode.equals(""))encode = "utf-8";
	           while ((len = in.read(b)) != -1)
	           	 {
	               sb.append(new String(b, 0, len, encode));
	            }
	           return sb.toString();
	       }
	       catch (IOException e)
	       {
	           e.printStackTrace();
	       }catch (NullPointerException e) {
	    	   return "";
	       }
	       return "";
	   }

	public static String formatTime(String time) {
		String ym = time.substring(4, 7).replaceAll("-", "");
		return ym;
	}

	public static String formatTime2(String time) {
		String ym = time.substring(0, 10).replaceAll("-", "");
		return ym;
	}
	/**
	 * 十进制转二进制
	 *
	 * @param n the n
	 * @return string
	 */
	public static String decToBinary(long n) {
		String str = "";
		while (n != 0) {
			str = n % 2 + str;
			n = n / 2;
		}
		return str;
	}



	/**
	 * @Author pancm
	 * @Description  获取月份的天数
	 * @Date  2019/7/12
	 * @Param [ym2]
	 * @return int
	 **/
	public static int getMonthDays(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(ym);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(time));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	/**
	 * @Author pancm
	 * @Description 得到流水号的月份
	 * @Date  2019/7/29
	 * @Param [ptmsgid]
	 * @return java.lang.String
	 **/
	public  static String  getPtmsgidMM(long ptmsgid){
		long m = (ptmsgid>>60)&15;
		String mm = String.valueOf(m);
		if(m<10){
			mm="0"+mm;
		}
		return mm;
	}

	/**
	 * @Author pancm
	 * @Description 得到流水号的月份和天数
	 * @Date  2019/7/29
	 * @Param [ptmsgid]
	 * @return java.lang.String
	 **/
	public  static String  getPtmsgidMMdd(long ptmsgid) throws ParseException {
		long m = (ptmsgid>>60)&15;
		long y = (ptmsgid>>55)&31;

		if(m==0){
			m=12;
		}
		if(y<=0){
			m=m-1;
			String m2=String.valueOf(m);
			m2="0"+m2;
			y = getMonthDays(getNowTime("yyyy")+m2);
		}
		String mm = String.valueOf(m);
		String yy = String.valueOf(y);
		if(y<10){
			yy="0"+yy;
		}
		if(m<10){
			mm="0"+mm;
		}

		return mm+yy;
	}

	/**
	 * @Author pancm
	 * @Description 得到流水号的月份和天数
	 * @Date  2019/7/29
	 * @Param [ptmsgid]
	 * @return java.lang.String
	 **/
	public  static String  getPtmsgidMMdd2(long ptmsgid) throws ParseException {
		long m = (ptmsgid>>60)&15;
		long y = (ptmsgid>>55)&31;
		String mm = String.valueOf(m);
		String yy = String.valueOf(y);
		if(y<10){
			yy="0"+yy;
		}
		if(m<10){
			mm="0"+mm;
		}
		return mm+yy;
	}




	/**
	 * 格式化字符串 字符串中使用{key}表示占位符
	 * @param sourStr 需要匹配的字符串
	 * @param param   参数集
	 * @return
	 */
	public synchronized static String format(String sourStr, Map<String, Object> param) {
		String tagerStr = sourStr;
		if (param == null)
			return tagerStr;
		try {
			Matcher matcher = pattern.matcher(tagerStr);
			while (matcher.find()) {
				String key = matcher.group();
				String keyclone = key.substring(1, key.length() - 1).trim();
				Object value = param.get(keyclone);
				if (value != null)
					tagerStr = tagerStr.replace(key, value.toString());
			}
		} catch (Exception e) {
			return null;
		}
		return tagerStr;
	}

	public  static String format(String sourStr, JSONObject param) {
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

	public static <T> List<T> toList(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}

	/**
	 * @return java.lang.String
	 * @Author pancm
	 * @Description 获取截取字符串后面的值
	 * @Date 2020/7/30
	 * @Param [str, separator]
	 **/
	public static String substringAfter(final String str, final String separator) {
		if (StringUtils.isEmpty(str)) {
			return "";
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

	/**
	 * @return java.lang.String
	 * @Author pancm
	 * @Description 获取截取字符串前面的值
	 * @Date 2020/7/30
	 * @Param [str, separator]
	 **/
	public static String substringBefore(final String str, final String separator) {
		if (StringUtils.isEmpty(str)) {
			return "";
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



	/**
	 * @Author pancm
	 * @Description 得到map的差集
	 * @Date  2020/7/30
	 * @Param [bigMap, smallMap]
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	public  static Map<String, Object> getDifferenceMap(Map<String, Object> bigMap, Map<String, Object> smallMap) {
		Set<String> bigMapKey = bigMap.keySet();
		Set<String> smallMapKey = smallMap.keySet();
		Set<String> differenceSet = Sets.difference(bigMapKey, smallMapKey);
		Map<String, Object> result = Maps.newHashMap();
		for (String key : differenceSet) {
			result.put(key, bigMap.get(key));
		}
		return result;
	}

	/**
	 * 本方法的测试示例
	 * @param args
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
		/*
		 * String 和List 空数据判断
		 */
		String str1="";
		String str2=" ";
		String str3=null;
		String str4="a";
		List list=null;
		List<String> list2=new ArrayList<String>();
		List<Object> list3=new ArrayList<Object>();
		list3.add("a");

		System.out.println("str1 :"+isEmpty(str1));		//str1 :true
		System.out.println("str2 :"+isEmpty(str2));		//str2 :true
		System.out.println("str3 :"+isEmpty(str3));		//str3 :true
		System.out.println("str4 :"+isEmpty(str4));		//str4 :false
		System.out.println("list :"+isEmpty(list));		//list :true
		System.out.println("list2 :"+isEmpty(list2));	//list2 :true
		System.out.println("list3 :"+isEmpty(list3)); //list3 :false


		/*
		 *  时间
		 */
		long start=getNowLongTime();
		System.out.println("getNowTime():"+getNowTime());		//getNowTime():2017-09-26 17:46:44
		System.out.println("getNowLongTime():"+getNowLongTime());  //getNowLongTime():1506419204920
		System.out.println("getNowTime(sdfm):"+getNowTime(sdfm)); //getNowTime(sdfm):2017-09-26 17:46:44 920
		System.out.println("当时时间向前推移30秒:"+ changeTime(-30,sdf,"s"));            //2017-09-26 17:46:14
		System.out.println("时间比较:"+isCompareDay(getNowTime(sdfm),changeTime(-30,sdf,"s"),"")); //时间比较:false
		System.out.println("getTNowTime():"+getTNowTime());	//getTNowTime():2017-09-26 17:46:44.921
		System.out.println("LongTime2StringTime():"+longTime2StringTime(start, sd)); //LongTime2StringTime():20170926174644
		System.out.println("时间转成时间戳:"+stringToTimestamp(getNowTime())); //1567219006000

		System.out.println("当前时间增加30分钟:"+addPlusMinutes(30));



		/*
		 * 整型判断
		 */
		String st="258369";
		String st2="258369A!@";
		String st3="258  369 ";
		System.out.println("st:"+isInteger(st));  //st:true
		System.out.println("st2:"+isInteger(st2)); //st2:false
		System.out.println("st3:"+isInteger(st3)); //st3:false

		/*
		 * 字符串反转
		 */
		String re="abcdefg";
		System.out.println("字符串反转:"+reverse(re)); //字符串反转:gfedcba


		/*
		 * 本机IP
		 */
		try {
			System.out.println("本机IP:"+getLocalHostIp()); //本机IP:192.168.1.111
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		/*
		 * 随机数
		 */

		System.out.println("6位随机数:"+random(6));	//6位随机数:222488
		System.out.println("10位随机数:"+random2(10));  //10位随机数:ZwW0pmofjW

		/*
		 * JSON数据转换
		 */

		String value="name1=value1&name2=value2&name3=value3";
		JSONObject json=new JSONObject();
		json.put("name1", "value1");
		json.put("name2", "value2");
		json.put("name3", "value3");
		System.out.println("value:"+value);				  //value:name1=value1&name2=value2&name3=value3
		System.out.println("str2Json:"+str2Json(value));  //str2Json:{"name1":"value1","name2":"value2","name3":"value3"}
		System.out.println("json:"+json.toJSONString());  //json:{"name1":"value1","name2":"value2","name3":"value3"}
		System.out.println("json2Str:"+json2Str(json));  //json2Str:name3=value3&name1=value1&name2=value2

		String jsonString=json.toJSONString();
		System.out.println("jsonString:"+jsonString);		//{"name1":"value1","name2":"value2","name3":"value3"}
		System.out.println("toJson(jsonString):"+toJson(jsonString)); //toJson(jsonString):{"name1":"value1","name2":"value2","name3":"value3"}

		System.out.println("long TO String"+longTime2StringTime(32472115200L));
		System.out.println("long TO String"+longTime2StringTime(1513330097L));

		String time1="2018-04-04";
		String time2="2018-04-04 14:48:00";
		String time3="2018-08-04 14:48:00.000";
		String time4="2018-11-04 14:48:00.000";
		System.out.println("时间补全:"+complementTime(time1,sdfm,1));
		System.out.println("时间补全:"+complementTime(time2,sdfm,2));
		System.out.println("时间补全:"+complementTime(time3,sdfm,1));
		System.out.println("时间格式改变1:"+formatTime(time3));
		System.out.println("时间格式改变2:"+formatTime(time4));
		System.out.println("时间格式改变3:"+getDD(time4,sdfm));
		System.out.println("时间格式改变4:"+formatTime2(time4));
		System.out.println("时间格式改变5:"+addPlusDay(time1,1));
		System.out.println("时间格式改变5:"+addPlusDay(time1,-1));
		System.out.println("时间比较:"+diffDay("20190831","20190901"));
		System.out.println("时间比较:"+diffDay("20190831","20190902"));
		System.out.println("获取几天前的时间:"+getMinusDays(7,sdfm));


		System.out.println("测试："+string2Time("2018-05-15 14:10:43.000066","yyyy-MM-dd HH:mm:ss.SSSSSS"));

		System.out.println(randomDate("2018-05-15 14:10:43","2018-05-17 14:10:43","yyyy-MM-dd HH:mm:ss"));




		String template ="" +
				"告警程序: alert_manage\n" +
				"告警级别: { alter_level }\n" +
				"告警策略: { strategy_name }\n" +
				"告警类型: { alter_type }\n" +
				"告警指标: { alter_index }\n" +
				"告警内容: 警告: {username} 设备已经掉线 {doc_count} 次了\n ";
		Map<String, Object> templateMap = new LinkedHashMap<>();
		templateMap.put("alter_level", "重要");
		templateMap.put("strategy_name", "用户掉线告警");
		templateMap.put("alter_type", "应用-功能告警");
		templateMap.put("alter_index", "准入");
		templateMap.put("username", "123456");
		templateMap.put("doc_count", "11");

		System.out.println(format(template,templateMap));
		String template2 = "警告: {pass} 设备入网路径变更！原地址:{ip_addr},当前地址:{cur_ip_addr} !";
		Map<String, Object> templateMap2 = new LinkedHashMap<>();
		templateMap2.put("pass","e0508b2c6f06");
		templateMap2.put("ip_addr","192.168.8.13");
		templateMap2.put("cur_ip_addr","192.168.8.14");
		templateMap2.put("xxx","123");
		templateMap2.put("xxx2","456");

		System.out.println(format(template2,templateMap2));



		System.out.println(StringUtils.trimAllWhitespace(" 11 22 "));
		System.out.println(StringUtils.trimWhitespace( " 11 22 "));


		String ss =" 警告: 27.16.251.6 地址接入了不同的设备，原设备:00 24 ac 6b 69 21,当前设备:00 24 ac 6b 3d 35!";
		String macP = "([A-Fa-f0-9]{2} ){5}[A-Fa-f0-9]{2}";
		Pattern macPattern = Pattern.compile(macP);
		Matcher matcher = macPattern.matcher(ss);
		while (matcher.find()) {
			String key = matcher.group();
			System.out.println(key);
		}

		String dd = "0 0/3 * * * ?";
		String dd2 = "* * 1/4 * * ? *";
		System.out.println(StringUtils.trimAllWhitespace(dd).length());
		System.out.println(StringUtils.trimAllWhitespace(dd2).length());
		if(StringUtils.trimAllWhitespace(dd2).length()>8){
			System.out.println(dd2.indexOf("*"));
			String a = dd2.substring(1).substring(0, dd2.length() - 3);
			System.out.println(a);
			String b = "0".concat(a);
			System.out.println(StringUtils.trimAllWhitespace(b).length());
			System.out.println(dd);
			System.out.println(b);
			System.out.println(a.equals(dd));
		}


		String msg = "警告: 0050c242e636 设备已经掉线了！最后在线时间:2020-09-11 12:07:15！已离线:49662 分钟！";
		String msg1= substringAfter(msg,"已离线:");
		String msg0= substringBefore(msg,"已离线:");
		System.out.println(msg0);
		System.out.println(msg1);
		String msg2= substringBefore(msg1," 分钟！");
		System.out.println(msg2);
		Long min = Long.valueOf(msg2);
		Long hour = Long.valueOf(msg2);
		String msg3 = millisToStringShort(min);
		System.out.println(msg0.concat("已离线:").concat(msg3));

	}
	public static StringBuffer millisToStringShort2(long l){
		StringBuffer sb=new StringBuffer();
		long millis=1;
		long seconds=1000*millis;
		long minutes=60*seconds;
		long hours=60*minutes;
		long days=24*hours;
		if(l/days>=1)
			sb.append((int)(l/days)+"天");
		if(l%days/hours>=1)
			sb.append((int)(l%days/hours)+"小时");
		if(l%days%hours/minutes>=1)
			sb.append((int)(l%days%hours/minutes)+"分钟");
		if(l%days%hours%minutes/seconds>=1)
			sb.append((int)(l%days%hours%minutes/seconds)+"秒");
		if(l%days%hours%minutes%seconds/millis>=1)
			sb.append((int)(l%days%hours%minutes%seconds/millis)+"毫秒");
		return sb;
	}

	public static String millisToStringShort(long l){
		StringBuffer sb=new StringBuffer();

		long minutes=1;
		long hours=60*minutes;
		long days=24*hours;
		if(l/days>=1)
			sb.append((int)(l/days)+"天");
		if(l%days/hours>=1)
			sb.append((int)(l%days/hours)+"小时");
		if(l%days%hours/minutes>=1)
			sb.append((int)(l%days%hours/minutes)+"分钟");

		return sb.toString();
	}

}
