package com.zans.config;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author pancm
 * @Title: Constans
 * @Description: 常量类
 * @Version:1.0.0
 * @date 2018年3月23日
 */
public class Constants {


    /**
     * 时间分隔符
     */
    public static final String TIME_SEPARATOR = ":";

    /**
     * 逗号分隔符
     */
    public static final String COMMA_SIGN = ",";
    /**
     * 中文逗号分隔符
     */
    public static final String COMMA_SIGN_CH = "，";

    /**
     * 横线分隔符
     */
    public static final String LINE_SIGN = "-";

    public static final String LINE_SIGN2 = "--";

    /**
     * 下划线分隔符
     */
    public static final String UNDER_LINED_SIGN = "_";
    /**
     * 时间格式化字符串 yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 时间格式化字符串1 yyyyMMddHHmm
     */
    public static final String TIME_FORMAT1 = "yyyyMMddHHmm";

    /**
     * 时间格式化字符串2 yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式化字符串3 yyyy-MM-dd
     */
    public static final String TIME_FORMAT3 = "yyyy-MM-dd";

    /**
     * 时间格式化字符串4 yyyyMMdd
     */
    public static final String TIME_FORMAT4 = "yyyyMMdd";

    /**
     * 时间格式化字符串5 yyyyMMddHHmmssSSS
     */
    public static final String TIME_FORMAT5 = "yyyyMMddHHmmssSSS";

    /**
     * 时间格式化字符串 yyyy-MM-dd HH:mm:ss.SSSSSSS
     */
    public static final String TIME_FORMAT6 = "yyyy-MM-dd HH:mm:ss.SSSSSSS";

    /**
     * 时间格式化字符串 yyyyMM
     */
    public static final String TIME_FORMAT7 = "yyyyMM";

    /**
     * 时间格式化字符串HH:mm:ss
     */
    public static final String TIME_FORMAT8 = "HH:mm:ss";


    public static final String DEFAULT_TIME = "1900-01-01 00:00:00.000";


    /**
     * 默认时间格式:yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 数据库相关配置
     */
    public static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    public static String DB_NUM = "num";
    public static String MYSQL_PATH = "mysqlPath";
    public static String URL_NAME = "url";
    public static String DB_NAME = "dbName";
    public static String USERNAME = "username";
    public static String PWD_NAME = "password";
    public static String TABLE_NAME = "tableName";
    public static String TARGET_PATH = "targetPath";
    public static String SOURCE_PATH = "sourcePath";
    public static String EXECUTE_TIME = "executeTime";
    public static String IS_START_EXECUTE = "isStartExecute";
    public static String IS_INCREMENT = "isIncrement";
    public static String SOURCE_PATH_PREFIX = "sourcePathPrefix";


    public static String OS = "os";
    public static String PORT = "port";
    public static String HOST_NAME = "hostName";

    public static String BACKUP_DAY = "backUpDay";

    /** mysqldump的名称，mysql的名称 */
    public static String DUMP_NAME = "mysqldump";
    public static String MYSQL_NAME = "mysql";






    /**
     * 程序版本
     */
    public static final String VERSION = "V1.0.1";








    /** 数据库连接池*/
    public  static ConcurrentMap<Integer, DataSource> dataSourceConcurrentMap = new ConcurrentHashMap<>();
    /** 数据库对应的连接状态 */
    public  static ConcurrentMap<Integer, Boolean> dataSourceStatus = new ConcurrentHashMap<>();
    /** 数据库对应的IP */
    public  static ConcurrentMap<Integer, String> dataSourceIp = new ConcurrentHashMap<>();
    /** 数据库的名称 */
    public  static ConcurrentMap<Integer, String> dataSourceName = new ConcurrentHashMap<>();






}
