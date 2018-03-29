package com.pancm.test.threeCharactersTest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
* @Title: polymorphicTest
* @Description:
* 多态测试 
* @Version:1.0.0  
* @author pancm
* @date 2018年3月27日
 */
public class polymorphicTest {

	public static void main(String[] args) {
		System.out.println(getNowTime());
		System.out.println(getNowTime("yyyy-MM-dd HH:mm:ss SSS"));
	}
	
	public static String getNowTime() {
		return getNowTime("yyyy-MM-dd HH:mm:ss");
	}

	public static String getNowTime(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}
	
}
