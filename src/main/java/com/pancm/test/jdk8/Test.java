package com.pancm.test.jdk8;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
* @Title: Test
* @Description: 
* @Version:1.0.0  
* @author pancm
* @date 2018年6月21日
*/
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test();
	}
	
	/**
	 * 时间测试
	 */
	private static void test(){
		 LocalDateTime ldt = LocalDateTime.now();
		 System.out.println("当前年:"+ldt.getYear());   //2018
		 System.out.println("当前年份天数:"+ldt.getDayOfYear());//172 
		 System.out.println("当前月:"+ldt.getMonthValue());
		 System.out.println("当前时:"+ldt.getHour());
		 System.out.println("当前分:"+ldt.getMinute());
		 System.out.println("当前时间:"+ldt.toString());
		 System.out.println("格式化时间:"+ ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
		 System.out.println("前5天时间:"+ldt.minusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); //2018-06-16
		 System.out.println("后5天时间:"+ldt.plusDays(5));
		 System.out.println("指定2099年的当前时间:"+ldt.withYear(2099)); //2099-06-21T15:07:39.506
	}
}
