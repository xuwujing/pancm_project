package com.pancm.test.hbaseTest.others.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 文件读取辅助类
 */
public class ReadProperties {

    /**
     * Gets property from configuration.
     *
     * @param fileName     文件名
     * @param key          key
     * @param defaultValue NULL的时候默认返回值
     * @return property from configuration
     */
    public static Object getPropertyFromConfiguration(String fileName, String key, Object defaultValue) {
        InputStream inputStream = ReadProperties.class.getClassLoader()
                .getResourceAsStream(fileName);
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e) {

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {

            }
        }
        return p.getProperty(key) == null ? defaultValue : p.getProperty(key);
    }

    /**
     * TODO 读取配置文件的相关项
     *
     * @param fileName the file name
     * @return properties
     */
    public static Properties loadConfiguration(String fileName) {
        InputStream inputStream = ReadProperties.class.getClassLoader().getResourceAsStream(fileName);
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e) {
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
        return p;
    }

}
