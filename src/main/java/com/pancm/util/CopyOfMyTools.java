package com.pancm.util;

import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import net.sf.json.xml.XMLSerializer;


/**
 * 简单方法集合
 *
 * @author ZERO
 */
public class CopyOfMyTools implements java.io.Serializable{
     
	private static final long serialVersionUID = 1L;


    /**
     * 判断String类型的数据是否为空
     *
     * @param data the data
     * @return boolean boolean
     */
    public static boolean isNull(String data){
		if(null==data||"".equals(data)){
			return true;
		}
		return false;
	}

    /**
     * 判断list类型的数据是否为空
     *
     * @param list the list
     * @return boolean boolean
     */
    @SuppressWarnings("rawtypes")
	public static boolean isNull(List list){
		if(null==list||list.size()==0){
			return true;
		}
		return false;
	}

    /**
     * 字符串反转
     *
     * @param str the str
     * @return string
     */
    public static String reverse(String str) {
        if(str == null || str.length() <= 1){ 
            return str;
        }
        return reverse(str.substring(1)) + str.charAt(0);
    }

    /**
     * 获取当前long类型的的时间
     *
     * @return long long
     */
    public static long getNowLongTime(){
	   return System.currentTimeMillis();
	}

    /**
     * long类型的时间转换成自定义时间格式
     *
     * @param lo     the lo
     * @param format the format
     * @return String string
     */
    public static String LongTime2StringTime(long lo,String format){
		 if(isNull(format)){
			 format="yyyy-MM-dd HH:mm:ss";
		 }
		 return new SimpleDateFormat(format).format(lo);
	 }

    /**
     * String类型的时间转换成 long
     *
     * @param time   the time
     * @param format the format
     * @return String long
     * @throws ParseException the parse exception
     */
    public static long StringTime2LongTime(String time,String format) throws ParseException{
		 if(isNull(format)){
			 format="yyyy-MM-dd HH:mm:ss";
		 }
		 if(isNull(time)){
			 time=new SimpleDateFormat(format).format(new Date());
		 }
		 SimpleDateFormat sd= new SimpleDateFormat(format);
		 Date date=sd.parse(time);
		 return date.getTime();
		}

    /**
     * 获取当前String类型的的时间(自定义格式)
     *
     * @param format the format
     * @return String string
     */
    public static String getNowTime(String format){
		if(isNull(format)){//如果没有设置格式使用默认格式
			format="yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(format).format(new Date());
	}

    /**
     * 获取当前Timestamp类型的的时间
     *
     * @return Timestamp timestamp
     */
    public static Timestamp getTNowTime(){
		return Timestamp.valueOf(getNowTime(""));
	}


    /**
     * 获取的String类型时间加上增加的分钟自定义格式
     *
     * @param mi     the mi
     * @param time   the time
     * @param format the format
     * @return String time addmi
     */
    public static String getTimeAddmi(int mi,String time,String format) {
		if(isNull(format)){//如果没有设置格式使用默认格式
			format="yyyy-MM-dd HH:mm:ss";
		}
		if(isNull(time)){ //如果没有设置时间取当前时间
			time=new SimpleDateFormat(format).format(new Date());
		}
		SimpleDateFormat format1 = new SimpleDateFormat(format);
		Date d=null;
		try {
			d = format1.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}    
		Calendar ca = Calendar.getInstance();  //定义一个Calendar 对象
		ca.setTime(d);//设置时间
		ca.add(Calendar.MINUTE, mi);//增加分钟
		String backTime = format1.format(ca.getTime());  //转化为String 的格式
		return backTime;
	}


    /**
     * 两个日期带时间比较
     *
     * @param time1  the time 1
     * @param time2  the time 2
     * @param format the format
     * @return boolean boolean
     * @throws Exception the exception
     */
    public static boolean compareDay(String time1,String time2,String format) throws Exception {
		  if(isNull(format)){//如果没有设置格式使用默认格式
				format="yyyy-MM-dd HH:mm:ss";
			}
		    SimpleDateFormat s1 = new SimpleDateFormat(format);
			Date t1=s1.parse(time1);
			Date t2=s1.parse(time2);
			return t2.after(t1);//当 t2 大于 t1 时，为 true，否则为 false
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
     * 获取本机ip
     *
     * @return String local host ip
     * @throws UnknownHostException the unknown host exception
     */
    public static String getLocalHostIp() throws  UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}


    /**
     * 获取本机port
     *
     * @param request the request
     * @return int local port
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static int getLocalPort(HttpServletRequest request) throws  UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		return request.getLocalPort();
	}

    /**
     * 获取项目名称
     *
     * @param request the request
     * @return String project name
     * @throws UnsupportedEncodingException
     */
    public static String getProjectName(HttpServletRequest request) {
		return request.getContextPath();
	}


    /**
     * 获取客户端ip
     *
     * @param request the request
     * @return String client ip
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String getClientIp(HttpServletRequest request) throws  UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		return request.getRemoteAddr();
	}

    /**
     * 获取客户端port
     *
     * @param request the request
     * @return int client port
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static int getClientPort(HttpServletRequest request) throws  UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		return request.getRemotePort();
	}

    /**
     * 获取客户端主机ip
     *
     * @param request the request
     * @return String client host ip
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String getClientHostIp(HttpServletRequest request) throws  UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		return request.getRemoteHost();
	}

    /**
     * 获取请求集合
     *
     * @param request the request
     * @return the parameter names
     * @throws IOException the io exception
     */
    @SuppressWarnings("rawtypes")
   public static JSONObject  getParameterNames(HttpServletRequest request) throws IOException{
	   JSONObject json=new JSONObject();
	    Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();  
			json.put(paraName, request.getParameter(paraName));
		}
		return json;
   }

	
	
	
	
  
    /** 
     * xml转json
     *  
     * @param String 
     *          
     * @return json 
     */ 
//    public static JSONObject xml2JSON(String sr){
//    	  XMLSerializer xmlSerializer = new XMLSerializer(); 
//		  return (JSONObject) xmlSerializer.read(sr); 
//    }

    /**
     * 判断是否为数字
     *
     * @param str the str
     * @return boolean boolean
     */
    public static boolean isNumber(String str) {
		String regEx="^[0-9]+$";
	    Pattern p=Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}


    /**
     * 判断是否为整型
     *
     * @param str the str
     * @return boolean boolean
     */
    public static boolean isInteger(String str) {
		String regEx="^\\d+$";
	    Pattern p=Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

    /**
     * 判断是否为手机号
     *
     * @param mobiles the mobiles
     * @return boolean boolean
     */
    public static boolean isMobileNO(String mobiles){
    	String regEx="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    	Pattern p = Pattern.compile(regEx);  
    	Matcher m = p.matcher(mobiles);  	  
    	return m.find();   
    	}


    /**
     * 改变协议进制
     *
     * @param prop the prop
     * @return byte byte
     */
    public static byte getType(int prop){
		return (byte) (prop >>56 &0xff); 
	}

    /**
     * MD5加密
     *
     * @param message the message
     * @return string
     */
    public static String encode(String message) {
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
     * Get image str string.
     *
     * @param fileurl the fileurl
     * @return the string
     */
// 图片转化成base64字符串
	public static String GetImageStr(String fileurl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		String imgFile = fileurl;// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

    /**
     * Generate image boolean.
     *
     * @param imgStr   the img str
     * @param filepath the filepath
     * @return the boolean
     */
// base64字符串转化成图片
	public static boolean GenerateImage(String imgStr, String filepath) {
		// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null){ // 图像数据为空
			System.out.println("imgStr: kong");
			return false;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			String imgFilePath = filepath;// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			System.out.println("base64完毕!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
    
}
