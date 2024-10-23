package com.zans.commons.config;

import com.zans.commons.utils.TransferUtils;
import com.zans.commons.utils.YmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.zans.commons.contants.Constants.APP_FILE_NAME;

/**
 *
* Title: getProperties
* Description: 获取配置文件
* Version:1.0.0
* @author pancm
* @date 2018年1月4日
 */
public class GetProperties {
	private static Map<String,String> appSettings = new HashMap<String,String>();
	private static final Logger LOG = LoggerFactory.getLogger(GetProperties.class);
	private String pathName="application.properties";
	/**
	 * 初始化系统默认参数
	 */
	private GetProperties(){
		init();
	}
	public static void refresh() {
		new GetProperties();
	}
	private  void init(){
		InputStream in = null;
		try{
			YmlUtil.setYmlFile(new File(APP_FILE_NAME));
			Map<String,String> map = TransferUtils.yml2Properties(APP_FILE_NAME);
			appSettings.putAll(map);
			//获取resource中的配置
//			in=InsertTopologyConfig.class.getClassLoader().getResourceAsStream(pathName);
			//获取项目同级的配置
//			in=new FileInputStream(new File(APP_FILE_NAME));
//			Properties prop = new Properties();
//			prop.load(in);

//			Set<Entry<Object, Object>> buf = prop.entrySet();
//			Iterator<Entry<Object, Object>> it = buf.iterator();
//			while(it.hasNext()){
//				Entry<Object, Object> t = it.next();
//				appSettings.put((String)t.getKey(), (String)t.getValue());
//			}
		}catch(Exception e){
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
	 * @param
	 * @return
	 */
	public synchronized static Map<String, String> getAppSettings() {
		if(null == appSettings || appSettings.isEmpty()){
				new GetProperties();
		}
		return appSettings;
	}

	public synchronized static void setAppSettings(Map<String, String> appSettings,String name) {
		if(null == appSettings || appSettings.isEmpty()){
			new GetProperties();
		}
		GetProperties.appSettings = appSettings;
	}

	/**
	 * 方法测试
	 * @param args
	 */
	  public static void main(String[] args) {
		  Map<String, String> conf = GetProperties.getAppSettings();
		  System.out.println("jdbc账号:"+conf.get("username"));
	  }

}
