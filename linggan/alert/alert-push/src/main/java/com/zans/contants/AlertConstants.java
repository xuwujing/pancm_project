package com.zans.contants;

import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertServerBean;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
public class AlertConstants {

    public static  String ES_IP = "192.168.8.78";
    public static  int ES_PORT = 9200;


    public static  String REMOTE_IP = "10.0.6.212";
    public static  String REMOTE_CONTEXT = "job/api";
    public static  String REMOTE_PORT = "29527";
    public static  String NODE_ID = "alert.45.9527.6953142354";


    public static final String HTTP="http://";
    public static final String REG_URI="/executor/register";
    public static final String FIN_URI="/task/finish";




    /**
     * ES相关常量类
     **/
    public static final String ES_URI="/radius_acct_log/_search";
    public static final String ES_AGG="aggregations";
    public static final String ES_GROUP="group";
    public static final String ES_BUCKETS="buckets";
    public static final String ES_DISTINCT_GROUP="dist_group";
    public static final String ES_AGG_KEY="key";
    public static final String ES_AGG_COUNT="doc_count";

    public static final String ES_SCRIPT="params.groupCount %s  %d ";
    public static final String ES_HAVING="having";
    public static final String ES_GROUP_NAME = "groupCount";
    public static final String ES_GROUP_COUNT ="_count";


    public static final String ES_HIT ="hits";
    public static final String ES_FIELDS ="fields";
    public static final String ES_SORT ="sort";


    public static  String ES_INDEX = "radius_acct_log";
//    public static  String ES_INDEX = "t_ip";



    /**
     * prometheus相关常量类
     **/
    public static final String PTS_DATA="data";
    public static final String PTS_RESULT="result";
    public static final String PTS_METRIC="metric";
    public static final String PTS_INSTANCE="instance";
    public static final String PTS_DEVICE="device";
    public static final String PTS_VALUE="value";
    public static final String PTS_DEFAULT="rootfs";

    public static final String PTS_URI="/api/v1/query?query=";


    public static final long SLEEP_TIME=30;

    public static  long SLEEP_TIME_QUEUE=10;

    public static  long SLEEP_TIME_RECOVER=60;

    public static  long SLEEP_TIME_AGG=60;


    public static final int DEL_DAY=14;


    public static final String REDIS_QUEUE="message";

    public static  boolean IS_INIT = false;



    public static ConcurrentMap<Long, AlertServerBean> alertServerCache = new ConcurrentHashMap<>();

    public static ConcurrentMap<String, Map<String, String>> mysqlCache = new ConcurrentHashMap<>();

    /** 规则表缓存 */
    public static ConcurrentMap<Long, AlertRuleBean> alertRuleCache = new ConcurrentHashMap<>();
    /** 规则表对应的状态*/
    public static ConcurrentMap<Long, Boolean> alertRuleCacheStatus = new ConcurrentHashMap<>();

    public static ConcurrentMap<Long, LocalDateTime> alertRuleCacheTime = new ConcurrentHashMap<>();

    public static ConcurrentMap<Long, AtomicInteger> alertRuleCachePeriod = new ConcurrentHashMap<>();


    public  static ConcurrentMap<String, DataSource> dataSourceConcurrentMap = new ConcurrentHashMap<>();

    public  static ThreadPoolExecutor tpx = new ThreadPoolExecutor(20, 50, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
            Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardOldestPolicy());

    public  static ThreadPoolExecutor reTpx = new ThreadPoolExecutor(20, 50, 30,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
            Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardOldestPolicy());

    public static String DRIVER= "com.mysql.jdbc.Driver";
    public static String DRIVER_CLASSNAME = ".datasource.driverClassName";
    public static String URL_NAME = ".datasource.url";
    public static String USER_NAME = ".datasource.username";
    public static String PWD_NAME = ".datasource.password";


    public static  String TABLE_ALERT_RULE = "alert_rule";
    /** 原始记录表*/
    public static  String TABLE_ALERT_RULE_ORIGINAL = "alert_rule_original";
    /** 触发过告警的表*/
    public static  String TABLE_ALERT_RULE_TRIGGER = "alert_rule_trigger";
    public static  String TABLE_ALERT_RULE_RECORD = "alert_rule_record";
    public static  String TABLE_ALERT_RULE_RECORD_TMP = "alert_rule_record_tmp";
    public static  String TABLE_ALERT_RULE_RECORD_DEL = "alert_rule_record_del";
    public static  String TABLE_RADIUS_ENDPOINT = "radius_endpoint";

    public static final String SELECT_SQL_SERVER = "SELECT server_id,server_name,server_ip,server_port,server_url," +
            " server_type,server_account,server_pwd,server_enable,server_desc,update_time FROM alert_server " +
            " WHERE  server_enable =1";
    public static final String SELECT_SQL_RULE = " SELECT * FROM alert_rule WHERE %S ORDER BY UPDATE_TIME ASC";
    public static final String DEL_SQL_TMP = " delete from alert_rule_record_tmp WHERE  create_time > DATE_SUB(NOW(), INTERVAL %d DAY) ";
    public static final String SELECT_SQL_JOB = " SELECT b.job_id,b.job_name,b.job_type,b.job_data,b.enable, a.task_id FROM ops_job_task a INNER JOIN ops_job b ON a.job_id=b.job_id where a.task_id=%s";


    public static final String SELECT_SQL_AGG = "SELECT a.business_id,a.rule_id,a.keyword_value,a.`notice_info`,a.`record_source`,a.`create_time`,t.alert_level FROM alert_rule_original a " +
            " INNER JOIN (SELECT MAX(o.id) AS id,MAX(b.`alert_level`) AS alert_level FROM alert_rule_original o, alert_rule b  GROUP BY o.keyword_value ) t ON a.`id` = t.id" +
            " WHERE  %s ORDER BY a.`create_time` DESC";


    public static final String SELECT_SQL_TRIGGER = "SELECT  keyword_value  FROM alert_rule_trigger where rule_id = %d ";

    public static final String SELECT_SQL_TRIGGER_GROUP = " SELECT rule_id FROM alert_rule_trigger GROUP BY rule_id";

    public static final String DELETE_SQL_TRIGGER = " delete from alert_rule_trigger where rule_id = %d and keyword_value = '%s' ";

    public static final String DELETE_SQL_RULE_RECORD = " delete from alert_rule_record where rule_id = %d and keyword_value = '%s' ";

    public static final String DELETE_SQL_RULE_ORIGINAL = " delete from alert_rule_original where rule_id = %d and keyword_value = '%s' ";


    public static final String SELECT_SQL_RULE_RECORD = " SELECT * from alert_rule_record where rule_id = %d and keyword_value in %s ";



}
