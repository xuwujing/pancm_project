package com.pancm.test.othersTest;

import com.pancm.util.IPWhiteCheck;

/**
 * 
* @Title: Test2
* @Description:
* ip白名单配置 
* @Version:1.0.0  
* @author pancm
* @date 2018年7月10日
 */
public class Test2 {
	public static void main(String[] args) {
		String ip="192.169.0.10";
		String ipWhiteConfig="192.169.0.1-192.169.0.11";
		String ip2="192.169.1.10";
		String ipWhiteConfig2="192.169.1.*";
		System.out.println("是否通过："+IPWhiteCheck.checkLoginIP(ip, ipWhiteConfig));
		System.out.println("是否通过："+IPWhiteCheck.checkLoginIP(ip2, ipWhiteConfig2));
	}
}
