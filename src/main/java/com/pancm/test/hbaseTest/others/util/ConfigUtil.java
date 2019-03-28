package com.pancm.test.hbaseTest.others.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置管理辅助类
 */
public class ConfigUtil {
	@SuppressWarnings("rawtypes")
	private static final Map<String, Map> sysConfig = new HashMap<String, Map>();
	private static ConfigUtil instance = null;

	private ConfigUtil() {
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new ConfigUtil();
		}
	}

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ConfigUtil getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

    /**
     * Gets config val.
     *
     * @param key            the key
     * @param propertiesName the properties name
     * @return the config val
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String getConfigVal(String key, String propertiesName) {
		if (sysConfig.size() != 0) {
			if (sysConfig.get(propertiesName) != null) {
				return sysConfig.get(propertiesName).get(key) == null ? "": sysConfig.get(propertiesName).get(key).toString();
			}
		}
		InputStream inputStream = ReadProperties.class.getClassLoader().getResourceAsStream(propertiesName);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map conf = new HashMap();
		conf.putAll(p);
		sysConfig.put(propertiesName, conf);
		return conf.get(key) == null ? "" : conf.get(key).toString();
	}
}
