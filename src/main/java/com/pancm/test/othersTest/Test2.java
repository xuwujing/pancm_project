package com.pancm.test.othersTest;

import com.pancm.util.IPWhiteCheck;

/**
 * The type Test 2.
 *
 * @author pancm
 * @Title: Test2
 * @Description: ip白名单配置
 * @Version:1.0.0
 * @date 2018年7月10日
 */
public class Test2 {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		String ip="192.169.0.10";
		String ipWhiteConfig="192.169.0.1-192.169.0.11";
		String ip2="192.169.0.10";
		String ipWhiteConfig2="192.169.1.*";
		String ipWhiteConfig3="192.169.1.*|192.169.0.1-192.169.0.11";
		String []ips=ipWhiteConfig3.split("\\|");
		boolean falg=false;
		for(String i:ips){
			falg=IPWhiteCheck.checkLoginIP(ip2, i);
			if(falg){
				break;
			}
		}
		System.out.println("是否通过2："+falg);
		
		System.out.println("是否通过："+IPWhiteCheck.checkLoginIP(ip, ipWhiteConfig));
		System.out.println("是否通过："+IPWhiteCheck.checkLoginIP(ip2, ipWhiteConfig2));
//		System.out.println("是否通过："+IPWhiteCheck.checkLoginIP(ip2, ipWhiteConfig3));
		
		
		String time1="04:38:00";
		String time2="05:00:00";
		System.out.println(time1.compareTo(time2));
		System.out.println(time2.compareTo(time1));
	}
}
