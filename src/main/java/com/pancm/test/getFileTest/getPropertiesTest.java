package com.pancm.test.getFileTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: getPropertiesTest
 * Description:读取项目文件的数据
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年9月22日
 */
public class getPropertiesTest {
	//当前应用程序的默认配置，test.properties的
	private static Map<String,String> appSettings = new HashMap<String,String>();
	private static final Logger log = LoggerFactory.getLogger(getPropertiesTest.class);
	private final String FILE_NAME = "test.properties";
	/**
	 * 初始化系统默认参数
	 */
	private getPropertiesTest(){
		init();
	}
	
	private  void init(){
		InputStream in = null;
		try{
			//获取resource中的配置
			in=getPropertiesTest.class.getClassLoader().getResourceAsStream(FILE_NAME);
			//获取项目同级的配置
			in = new FileInputStream(new File(FILE_NAME));
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

	private Map<String, Object> getVersion() {
		Map<String, Object> versionMap = new HashMap<>();
		try (InputStream in = Files.newInputStream(Paths.get(FILE_NAME))) {
			Properties prop = new Properties();
			prop.load(in);
			prop.forEach((key, value) -> versionMap.put((String) key, value));
		} catch (IOException e) {
			log.error("读取文件失败: " + FILE_NAME, e);
		}
		return versionMap;
	}

    /**
     * Gets app settings.
     *
     * @return the app settings
     */
    public synchronized static Map<String, String> getAppSettings() {
		if(null == appSettings || appSettings.isEmpty()){
			new getPropertiesTest();
		}
		return appSettings;
	}

    /**
     * Sets app settings.
     *
     * @param appSettings the app settings
     */
    public synchronized static void setAppSettings(Map<String, String> appSettings) {
		if(null == appSettings || appSettings.isEmpty()){
			new getPropertiesTest();
		}
		getPropertiesTest.appSettings = appSettings;
	}
	
}
