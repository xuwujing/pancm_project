package com.zans.commons.utils;

import com.zans.commons.config.GetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * Title: getProperties
 * Description: 获取配置文件
 * Version:1.0.0
 *
 * @author pancm
 * @date 2018年1月4日
 */
public class GetProperties2 {
	private static Map<String,String> appSettings = new HashMap<String,String>();
	private static final Logger LOG = LoggerFactory.getLogger(GetProperties.class);
	private String pathName="build.properties";
	/**
	 * 初始化系统默认参数
	 */
	private GetProperties2(){
		init();
	}

	private  void init(){
		InputStream in = null;
		try{
			//获取resource中的配置
			in=this.getClass().getClassLoader().getResourceAsStream(pathName);
			//获取项目同级的配置
//			in=new FileInputStream(new File(pathName));
			Properties prop = new Properties();
			prop.load(in);
			Set<Entry<Object, Object>> buf = prop.entrySet();
			Iterator<Entry<Object, Object>> it = buf.iterator();
			while(it.hasNext()){
				Entry<Object, Object> t = it.next();
				appSettings.put((String)t.getKey(), (String)t.getValue());
			}
		}catch(IOException e){
			LOG.error("加载系统参数失败!",e);
		}finally{
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("加载系统参数失败!",e);
				}
			}
		}
	}

    /**
     * 获取配置文件
     *
     * @param
     * @return app settings
     */
    public synchronized static Map<String, String> getAppSettings() {
		if(null == appSettings || appSettings.isEmpty()){
				new GetProperties2();
		}
		return appSettings;
	}

    /**
     * Sets app settings.
     *
     * @param appSettings the app settings
     * @param name        the name
     */
    public synchronized static void setAppSettings(Map<String, String> appSettings,String name) {
		if(null == appSettings || appSettings.isEmpty()){
			new GetProperties2();
		}
		GetProperties2.appSettings = appSettings;
	}

    /**
     * 方法测试
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		  Map<String, String> conf = GetProperties.getAppSettings();
		  System.out.println(conf);
	  }

}
