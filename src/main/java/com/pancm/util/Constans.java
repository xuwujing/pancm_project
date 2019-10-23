package com.pancm.util;

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
public class Constans {


    /**
     * 分隔符
     */
    public static final String SEPERATOR = "|";

    /**
     * 符号"|"的拆分符
     */
    public static final String SEPERATOR_SPLIT = "\\|";

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
     * 项目名称
     */
    public static final String PROJECT_NAME = "dpas_mt_mig";

    /**
     * 程序版本
     */
    public static final String VERSION = "V1.0.0.1";

    /**
     * 程序信息
     */
    public static final String VERSION_MAG = "报表二次合并上传服务";

    public final static String tableName="queryTableName";
    public final static String wtableName="writeTableName";
    public final static String sourceField="queryField";
    public final static String targetField="writeField";

    public final static String  keyField = "SID";

    /** 线程休眠时间 单位秒*/
    public final static long  sleeptime = 10;



    /** 数据库连接池*/
    public  static ConcurrentMap<Integer, DataSource> dataSourceConcurrentMap = new ConcurrentHashMap<>();
    /** 数据库对应的连接状态 */
    public  static ConcurrentMap<Integer, Boolean> dataSourceStatus = new ConcurrentHashMap<>();
    /** 数据库对应的IP */
    public  static ConcurrentMap<Integer, String> dataSourceIp = new ConcurrentHashMap<>();
    /** 数据库的名称 */
    public  static ConcurrentMap<Integer, String> dataSourceName = new ConcurrentHashMap<>();

    /** 目标源表和对应写入的表 */
    public  static ConcurrentMap<String, String> tableNameMap = new ConcurrentHashMap<>();
    /** 表名对应的查询字段 */
    public  static ConcurrentMap<String, String> tableNameAndField = new ConcurrentHashMap<>();
    /** 表名对应的写入字段 */
    public  static ConcurrentMap<String, String> tableNameAndField2 = new ConcurrentHashMap<>();




}
