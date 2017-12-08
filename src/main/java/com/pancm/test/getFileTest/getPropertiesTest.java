package com.pancm.test.getFileTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
* Title: getPropertiesTest
* Description:读取项目文件的数据 
* Version:1.0.0  
* @author pancm
* @date 2017年9月22日
 */
public class getPropertiesTest {
	//当前应用程序的默认配置，test.properties的
	private static Map<String,String> appSettings = new HashMap<String,String>();
	private static final Logger log = LoggerFactory.getLogger(getPropertiesTest.class);
	/**
	 * 初始化系统默认参数
	 */
	private getPropertiesTest(){
		init();
	}
	
	private  void init(){
		InputStream in = null;
		try{
			in = new FileInputStream(new File("test.properties"));
			Properties prop = new Properties();
			prop.load(in);
			Set<Entry<Object, Object>> buf = prop.entrySet();
			Iterator<Entry<Object, Object>> it = buf.iterator();
			while(it.hasNext()){
				Entry<Object, Object> t = it.next();
				appSettings.put((String)t.getKey(), (String)t.getValue());
			}
		}catch(IOException e){
			log.error("加载系统参数失败");
		}finally{
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public synchronized static Map<String, String> getAppSettings() {
		if(null == appSettings || appSettings.isEmpty()){
			new getPropertiesTest();
		}
		return appSettings;
	}

	public synchronized static void setAppSettings(Map<String, String> appSettings) {
		if(null == appSettings || appSettings.isEmpty()){
			new getPropertiesTest();
		}
		getPropertiesTest.appSettings = appSettings;
	}
	
}
