package com.pancm.test.niuke.reverseTest;
/**
 * 
* Title: reverseTest
* Description: 字符串反转
* Version:1.0.0  
* @author Administrator
* @date 2017-8-31
 */
public class reverseTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str="hello xiao xi";
		System.out.println(reverseString(str));
	}

	public static String reverseString(String str){
		if(str==null||str.length()<=1){
			return str;
		}	
		return reverseString(str.substring(1))+str.charAt(0);
	}
	
}
