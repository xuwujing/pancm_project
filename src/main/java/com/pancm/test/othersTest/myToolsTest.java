package com.pancm.test.othersTest;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author ZERO
 * @Data 2017-5-15 上午10:51:13
 * @Description 一些方法测试
 */
public class myToolsTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		/**
		 * 空数据判断
		 */
        String str="";
        String str1=null;
        String str2="abc";
        String str3=" ";
        System.out.println(isNull(str));//true
        System.out.println(isNull(str1));//true
        System.out.println(isNull(str2));//false
        System.out.println(isNull(str3));//false
        
        List list=new ArrayList();
        List list1=null;
        List list2=new ArrayList();
        list2.add("a");
        System.out.println(isNull(list));//true
        System.out.println(isNull(list1));//true
        System.out.println(isEmpty(list1));//true
 //       System.out.println(isEmpty1(list1));//直接报错
        System.out.println(isNull(list2));//false
        
        /**
         * 时间相关
         */
        System.out.println(getNowTime(""));  //2015-10-01 11:08:44
        System.out.println(getNowTime("yyyyMMdd"));//20151001
        System.out.println(Calendar.getInstance().getTime());
        System.out.println(new Date());
        System.out.println(getNowTime(""));  //2015-10-01 11:59:24
        System.out.println(getTimeAddmi(10,null,null));//2015-10-01 12:09:24
        System.out.println(getTimeAddmi(20,"2015-10-01 11:08:44","yyyy-MM-dd HH:mm:ss"));//2015-10-01 11:28:44
	    
        System.out.println(compareDay("2015-10-01 12:09:24","2015-10-01 11:59:24",null));//false
        System.out.println(compareDay("20151001115924","20151001120924","yyyyMMddHHmmss"));//true
	 
        /**
         *  键值和空值
         */
        JSONObject json=new JSONObject();
	    json.put("one", "eno");
	    json.put("two", "");
        System.out.println(containkey(json,"one"));
        System.out.println(containkey(json,"q"));
        System.out.println(containkey(json,"two"));
        System.out.println(json.getString("d"));
        System.out.println(json.get("d"));
        
        /**
         * 格式
         */
        double d=5.32642;
        String df="#.00";
        System.out.println(doubleformat(d, df));
        
        /**
         * Math
         */
        System.out.println("Math.pow :"+ Math.pow(2, 10)); //2的10次方
        
        /**
         * 替换
         */
        String str4="abcdef";
        System.out.println(repaceString1(str4,"pp",2));//abppef
        System.out.println(repaceString1(str4,"kk",3));//abckkf
        System.out.println(repaceString2(str4,"pp",2));//abppef
 	    System.out.println(repaceString2(str4,"kk",3));//abckkf
 	    
 	  /**
 	   * 移位运算符  
 	   */
 	  int a=10;
 	  int b=a<<3;
 	  int c=a>>1;
 	  System.out.println("a:"+a); //10
 	  System.out.println("a*8:"+a*8); //80
 	  System.out.println("a<<3:"+b);//80 a*2^3  
 	  System.out.println("a/2:"+a/2);//5  
 	  System.out.println("a>>1:"+c);//5  a/2^1  
 	    
 	  /**
       * 包含
       */
		String aa="Hello World";
	    String dd="Hello";
	    String cc="HELLO";
	    System.out.println(dd.equals(cc)); //false
	    System.out.println(dd.equalsIgnoreCase(cc)); //true
	    System.out.println(aa.contains(dd));  //true
	 
	  /**
	   * 循环问题  
	   */
 	  print(100,3,5);
 	  
 	  JSONObject json1=new JSONObject();
 	  Map map =new HashMap();
 	  map.put("amount", "20");
 	  json1.put("data", map);
 	  System.out.println(json1);
 	  System.out.println(json1.getString("data"));
 //	  System.out.println(getData(json1,"data","amount"));
 	
 	  /**
 	   * 时间格式转换
 	   */
 	  long l=System.currentTimeMillis();
 	  String time=getNowTime();
 	  System.out.println("格式时间转long类型:"+getNowTime1("yyyy-MM-dd HH:mm:ss",time));
 	  System.out.println("时间:"+getTime(l));
 	  System.out.println("时间戳2:"+new Date().getTime());
 	  System.out.println("时间戳3:"+Calendar.getInstance().getTimeInMillis());
 	  
 	  String oldstr="abcd";
 	  System.out.println("反转:"+reverse(oldstr));
	}
  
	/**
	 * 判断String类型的数据是否为空
	 * @return Boolean
	 */
	public static boolean isNull(String data){
		return null==data||"".equals(data);
	}
	
	/**
	 * 判断list类型的数据是否为空
	 * @return Boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNull(List list){
		return list==null||list.size()==0;
	}
	
	public static boolean isEmpty(List list){
		return list==null||list.size()==0;
	}
	
	
	public static String getNowTime(String format){
		if(null==format||"".equals(format)){//如果没有设置格式使用默认格式
			format="yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(format).format(new Date());
	}
	
	
	public static String getTimeAddmi(int mi,String time,String format) {
		if(null==format||"".equals(format)){//如果没有设置格式使用默认格式
			format="yyyy-MM-dd HH:mm:ss";
		}
		if(null==time||"".equals(time)){ //如果没有设置时间取当前时间
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
	
	
	  public static boolean compareDay(String time1,String time2,String format) throws Exception {
		  if(null==format||"".equals(format)){//如果没有设置格式使用默认格式
				format="yyyy-MM-dd HH:mm:ss";
			}
		    SimpleDateFormat s1 = new SimpleDateFormat(format);
			Date t1=s1.parse(time1);
			Date t2=s1.parse(time2);
			return t2.after(t1);//当 t2 大于 t1 时，为 true，否则为 false
	    }
	
	 public static Object containkey(JSONObject json,String key){
		  if(json.containsKey(key)){
			  if(!isNull(json.getString(key))){
				  return  json.get(key);
			  }
		  }
		 return null;
		 
	 }
	  
	 //格式化double 并四舍五入
	 public static  double doubleformat(double db,String format){
		 DecimalFormat df = new DecimalFormat(format); //定义格式
		 return Double.parseDouble(df.format(db));//你要格式化的数字
	 }
	  
	 public static String repaceString1(String a,String b,int t){     
         return a.substring(0,t)+b+a.substring(t+b.length(),a.length());
   } 
  
	 public static String repaceString2(String a,String b,int t){     
	        StringBuilder  sb = new StringBuilder(a);  
	        sb.replace(t, t+b.length(), b);   
	        return sb.toString();   
	    } 

	 
	 public static String getTime(long lo){
		 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lo);
	 }
	 
	 public static String getNowTime(){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
	 
	 public static long  getNowTime1(String format,String time) throws ParseException{
		 SimpleDateFormat sd= new SimpleDateFormat(format);
		 Date a=sd.parse(time);
		 long l=a.getTime();
		 return l;
		}
	 /**
	  * 
	  * @param a
	  * 
	  * @param b
	  * @param c
	  */
	 
	public static  void print(int a,int b,int c){
	    if(a<0||b<0||c<0){
	    	System.out.println(" ");
	    }else {
	    	for(int i=1;i<=a;i++){
	    		if(i%b==0){
	    			if(i%c==0){
	    				System.out.println("FizzBuzz");
	    			}else{
	    				System.out.println("Fizz");   
	    			}			    			
	    		}else if(i%c==0){
	    			System.out.println("Buzz");   
	    		}else{
	    			System.out.println(i);
	    		}
		
	    	}
	    }
	}
	 
	public static String getData(JSONObject json,String key,String key1){
		String re="";
		if(json.containsKey(key)){//判断是否有该key
			if(json.getString(key).endsWith("}")&&json.getString(key).startsWith("{")){
				re=JSON.parseObject(json.getString(key)).getString(key1);
			}else{
				re=json.getString(key);
			}
		}
		return re;	
	}
	
	/**
	 * 字符串反转
	 * @param originStr
	 * @return
	 */
	public static String reverse(String originStr) {
        if(originStr == null || originStr.length() <= 1){ 
            return originStr;
        }
        return reverse(originStr.substring(1)) + originStr.charAt(0);
    }
	
}
