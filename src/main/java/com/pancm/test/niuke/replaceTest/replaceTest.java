package com.pancm.test.niuke.replaceTest;
/**
 * 
* Title: replaceTest
* Description: 字符串替换测试
* Version:1.0.0  
* @author Administrator
* @date 2017-8-29
 */
public class replaceTest {

	/**
	 * 请实现一个函数，将一个字符串中的空格替换成“%20”。
	 * 例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
	 */
	public static void main(String[] args) {
		
		StringBuffer str=new StringBuffer("We Are Happy"); 
		
		System.out.println(replaceString(str));
		System.out.println(replaceString2(str));

	}
	
	/**
	 *  方法一：利用String的replace 直接替换
	 * @param str
	 * @return String
	 */
	public static String replaceString(StringBuffer str){
		if(str==null){
			return null;
		}
		return 	str.toString().replaceAll(" ", "%20");
	}

	/**
	 * 方法二:利用数组循环取出
	 * @param str
	 * @return
	 */
	public static String replaceString2(StringBuffer str){
		if(str==null){
			return null;
		}
		char []c =str.toString().toCharArray();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<c.length;i++){
			if(c[i]==' '){
				sb.append("%20");				
			}else{
				sb.append(c[i]);
			}		
		}
		return sb.toString();
	}
	
}
