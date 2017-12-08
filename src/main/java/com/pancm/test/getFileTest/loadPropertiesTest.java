package com.pancm.test.getFileTest;

import java.util.Map;

/**
 * 
* Title: loadPropertiesTest
* Description: 读取文件数据测试
* Version:1.0.0  
* @author pancm
* @date 2017年9月22日
 */
public class loadPropertiesTest {
	public static void main(String[] args) {
		Map<String, String> conf=getPropertiesTest.getAppSettings();
		System.out.println("本机IP:"+conf.get("localhost"));
		System.out.println("tomcat 端口:"+conf.get("tomcatPort"));
	}

}
