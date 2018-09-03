package com.pancm.test.getFileTest;

import java.util.Map;
import java.util.ResourceBundle;

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
		getFileData();
	}

	private static void getFileData() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("test");
		System.out.println("本机IP:"+resourceBundle.getString("localhost"));
		System.out.println("tomcat 端口:"+resourceBundle.getString("tomcatPort"));
		
	} 
	
}
