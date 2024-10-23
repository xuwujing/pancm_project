package com.zans.config;

import javax.sql.DataSource;
import java.util.Map;
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

    public static final String TIME_SEPARATOR2 = ";";


    public static final String EQUAL_SIGN = "=";

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
     * 换行符
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

    public static ConcurrentMap<String, Map<String, String>> mysqlCache = new ConcurrentHashMap<>();
    public  static ConcurrentMap<String, DataSource> dataSourceConcurrentMap = new ConcurrentHashMap<>();

    public static String DRIVER= "com.mysql.jdbc.Driver";
    public static String DRIVER_CLASSNAME = ".datasource.driverClassName";
    public static String URL_NAME = ".datasource.url";
    public static String USER_NAME = ".datasource.username";
    public static String PWD_NAME = ".datasource.password";
    /**
     * ------------------------ AAA 系列的常量类 start------------------------------------
     */

    public static int ALERT_RULE_ADD = 0;
    public static int ALERT_RULE_RECOVER = 1;

    public static int ALIVE_ONLINE = 1;
    public static int ALIVE_OFFLINE = 2;

    public static boolean ENABLE_UPDATE_ENDPOINT = true;

    public static String ALIVE_MODE_ARP = "arp";
    public static String ALIVE_MODE_ACCT = "radacct";
    public static String ALIVE_MODE_PING = "ping";

    public static String ALERT_RULE_RECOVER_URI = "/alert/record/recover";
    public static String ALERT_RULE_ADD_URI = "/alert/record/add";


    public static int SERVER_ID = 1;
    public static int SQL_LIMIT = 1000;
    public static int ACCESS_POLICY_BLOCK = 0;
    public static int ACCESS_POLICY_QZ = 1;
    public static int POLICY_PASS = 2;
    public static int POLICY_SPECIAL = 3;

    public static int ENDPOINT_SOURCE_ARP_RADIUS_YES = 1;
    public static int ENDPOINT_SOURCE_ARP_RADIUS_NO = 2;
    public static int ENDPOINT_SOURCE_VPN = 3;
    public static int ENDPOINT_SOURCE_MANUAL = 4;
    public static int ENDPOINT_SOURCE_IMPORT = 5;

    /**
     * `qz_enable` int(11) DEFAULT '0' COMMENT '0放行;1进检疫',
     * `asset_enable` int(11) DEFAULT '0' COMMENT '0不进资产;1进资产，并且建立基线',
     */
    public static int NETWORK_NAS_ASSET_ENABLE = 1;
    public static int NETWORK_NAS_ASSET_UNABLE = 0;
    public static int NETWORK_NAS_QZ_ENABLE = 1;
    public static int NETWORK_NAS_QZ_UNABLE = 0;

    /** 绑定状态 */
    public static int BASELINE_BIND = 1;
    public static int BASELINE_UNBIND = 0;


    public static String SYNC_DIRECTION_GUARD_TO_RADIUS = "pub";
    public static String SYNC_DIRECTION_RADIUS_TO_GUARD = "collect";
    public static String AAA_TIMESTAMP_COLUMN = "update_time";

    public static String AAA_SYNC_QZ_MARK_NAME = "radius_qz";
    public static String AAA_SYNC_QZ_DEST_NAME = "radius_endpoint";
    public static String AAA_SYNC_QZ_TYPE = SYNC_DIRECTION_RADIUS_TO_GUARD;

    public static String AAA_SYNC_ACCT_MARK_NAME = "radacct";
    public static String AAA_SYNC_ACCT_DEST_NAME = "radius_acct";
    public static String AAA_SYNC_ACCT_TYPE = SYNC_DIRECTION_RADIUS_TO_GUARD;

    public static String AAA_SYNC_ENDPOINT_MARK_NAME = "radius_endpoint";
    public static String AAA_SYNC_ENDPOINT_DEST_NAME = "radius_endpoint";
    public static String AAA_SYNC_ENDPOINT_TYPE = SYNC_DIRECTION_GUARD_TO_RADIUS;

    public static String AAA_SYNC_NAS_MARK_NAME = "network_nas";
    public static String AAA_SYNC_NAS_DEST_NAME = "nas";
    public static String AAA_SYNC_NAS_TYPE = SYNC_DIRECTION_GUARD_TO_RADIUS;


    public static String QZ_REASON_NEW_ASSET_STR = "全新入网设备";
    public static String QZ_REASON_BASELINE_CHANGE_STR = "扫描发现基线变更";
    public static String QZ_REASON_BASELINE_EQ_STR = "基线变更后,改回原基线";


    public static String QUEUE_DEVICE_INSIGHT_PASS = "device_insight_pass";
    public static String QUEUE_DEVICE_INSIGHT_QZ = "device_insight_qz";
    public static String QUEUE_DEVICE_INSIGHT_VPN = "device_insight_vpn";
    public static String QUEUE_DEVICE_INSIGHT_ASSET = "device_insight_asset";
    public static String QUEUE_DEVICE_INSIGHT_ARP = "device_insight_arp";


    public static String ALIVE_TYPE_ENDPOINT = "endpoint";
    public static String ALIVE_TYPE_ASSET_STATIC = "asset";
    public static String ALIVE_TYPE_KEY = "row_type";

    /**  ------------------------ AAA 系列的常量类 end------------------------------------         */

    /**
     * ------------------------ 常量类 start------------------------------------
     */
    public static String NETWORK_API_HOST = "network_api:host";
    //准入模式
    public static String ACCESS_MODE = "access:access_mode";

    public static String ASSET_WHITE_LIST = "access:white_list";

    public static String ALERT_BASELINE = "alert:baseline";

    public static String ALERT_API_HOST = "alert_api:host";

    public static String ALIVE_MODE = "alive_mode";
    /**
     * ------------------------常量类 end------------------------------------
     */


    public static String ACCESS_ACCESS_MODE_ACL = "acl";

    public static String API_SW_ARP = "/api/sw/arp";
    public static String API_SW_INTERFACE = "/api/sw/interface";
    public static String API_SW_LLDP = "/api/sw/lldp";
    public static String API_SW_BRIEF = "/api/sw/brief";
    public static String API_DEVICE_HIK_SEARCH = "/api/device/hik/search";
    public static String API_SCAN_PORT = "/api/scan/port";

    public static String API_SW_SYSTEM_INFO = "/api/sw/systemInfo";
    public static String API_SW_INTERFACE_OPEN_STATUS = "/api/sw/interfaceOpenStatus";
    public static String API_SW_LLDP_LOCCHASSISID = "/api/sw/lldpLocChassisId";
    public static String API_SW_MAC = "/api/sw/mac";

}
