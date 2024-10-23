package com.zans.config;

/**
 * @author pancm
 * @Title: Constans
 * @Description: 常量类
 * @Version:1.0.0
 * @date 2018年3月23日
 */
public class Constants {


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

    public static final String SLASH_SIGN = "/";

    /**
     *  换行符
     **/
    public static final String WRAP_SIGN = "\n";

    /**
     * 下划线分隔符
     */
    public static final String UNDER_LINED_SIGN = "_";

    public static final String LEFT_PTS_SIGN = "{";

    public static final String RIGHT_PTS_SIGN = "}";


    public static final String CODING_UTF8 = "UTF-8";


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


    public static final String DEFAULT_TIME = "2000-01-01T00:00:00.000";


    /**
     * 默认时间格式:yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static final String JUDGE_GT = ">";
    public static final String JUDGE_GTE = ">=";
    public static final String JUDGE_LT = "<";
    public static final String JUDGE_LTE = "<=";
    public static final String JUDGE_TERM = "=";


    public static final String MYSQL_ALERT = "alert";

    public static final String MYSQL_JOB = "job";


//    public static final String APP_FILE_NAME = "application.properties";
    public static final String APP_FILE_NAME = "application.yml";



    /**
     * 程序版本
     */
    public static final String VERSION = "V1.0.1";

    public static int CODE_ERROR_UNKNOWN = 5000;
    public static int CODE_SUCCESS = 0;




    //压缩包密码
    public static  String ZIP_PWD = "$ZSfdg78963&*!";


    public static  String CMD_STOP = "supervisorctl stop ";
    public static  String CMD_BAK = "cp /home/release/%s /home/release/%s_bak";
    public static  String CMD_REPLACE = "mv %s/portal.jar /home/release/%s";
    public static  String CMD_START = "supervisorctl start ";


    public static  String NAME_PORTAL = "portal/portal.jar";
    public static  String NAME_ALERT = "alert.jar";

    public static  String HOME = "/home/release/%s";

    public static  String ANSIBLE_PLAYBOOK = "ansible-playbook deploy.yml -i hosts.yml --extra-vars '%s'";

}
