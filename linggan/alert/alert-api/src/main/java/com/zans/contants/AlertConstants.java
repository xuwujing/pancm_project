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

    public static  String ES_IP = "192.168.20.8";
    public static  int ES_PORT = 9200;





    public static String JOB_URl="http://localhost:8765";
    public static String JOB_URI_SAVE="insertOrUpdate";
    public static String JOB_URI_ENABLE="enable";





    public static final long SLEEP_TIME=30;

    public static  long SLEEP_TIME_QUEUE=10;

    public static  long SLEEP_TIME_RECOVER=60;

    public static  long SLEEP_TIME_AGG=60;

    public static  String AES_KEY="Lgwy@1234!@#$%^&";


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

    public  static ThreadPoolExecutor alTpx = new ThreadPoolExecutor(3, 100, 3,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5000),
            Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardOldestPolicy());


    public static String DRIVER= "com.mysql.jdbc.Driver";
    public static String DRIVER_CLASSNAME = ".datasource.driverClassName";
    public static String URL_NAME = ".datasource.url";
    public static String USER_NAME = ".datasource.username";
    public static String PWD_NAME = ".datasource.password";

    public static String TABLE_ALERT_RULE="alert_rule";
    public static  String TABLE_ALERT_RULE_STRATEGY = "alert_rule_strategy";

    public static  String TABLE_OPS_JOB = "ops_job";
    /** 原始记录表*/
    public static  String TABLE_ALERT_RULE_ORIGINAL = "alert_rule_original";
    public static  String TABLE_ALERT_RULE_ORIGINAL_DETAIL = "alert_rule_original_detail";
    public static  String TABLE_ALERT_RULE_ORIGINAL_DEL = "alert_rule_original_del";
    public static  String TABLE_ALERT_IP_CLASH = "alert_ip_clash";
    /**历史数据表*/
    public static  String TABLE_ALERT_RULE_ORIGINAL_DETAIL_HIS = "alert_rule_original_detail_his";
    public static  String TABLE_ALERT_RULE_ORIGINAL_HIS = "alert_rule_original_his";
    /** 触发过告警的表*/
    public static  String TABLE_ALERT_RULE_TRIGGER = "alert_rule_trigger";
    public static  String TABLE_ALERT_RULE_RECORD_TMP = "alert_rule_record_tmp";
    public static  String TABLE_ALERT_RULE_RECORD_DEL = "alert_rule_record_del";
    public static  String TABLE_ALERT_LOOP = "alert_loop";
    public static  String TABLE_RADIUS_ENDPOINT = "radius_endpoint";

    public static final String SELECT_SQL_SERVER = "SELECT server_id,server_name,server_ip,server_port,server_url," +
            " server_type,server_account,server_pwd,server_enable,server_desc,update_time FROM alert_server " +
            " WHERE  server_enable =1";
    public static final String SELECT_SQL_RULE = " select ars.*,a.`alert_dsl`,a.`alert_keyword`,a.`alert_datasource`,a.`alert_template`,a.`query_sql`,a.`query_detail_sql`,a.`query_detail_sql` ,a.`rule_status` \n" +
            "from alert_rule_strategy ars left join alert_rule a ON a.id = ars.`rule_id` where a.`rule_status` = 1 and  %s  order by ars.update_time asc ";

    public static final String DEL_SQL_TMP = " delete from alert_rule_record_tmp WHERE  create_time > DATE_SUB(NOW(), INTERVAL %d DAY) ";
    public static final String SELECT_SQL_JOB = " SELECT b.job_id,b.job_name,b.job_type,b.job_data,b.enable, a.task_id FROM ops_job_task a INNER JOIN ops_job b ON a.job_id=b.job_id where a.task_id=%s";

    public static final String SELECT_SQL_JOB_ID= " SELECT job_id FROM ops_job  where job_name = '%s' limit 1";

    public static final String SELECT_SQL_AGG = "SELECT a.business_id,a.strategy_id,a.keyword_value,a.`notice_info`,a.`record_source`,a.`update_time`,t.alert_level FROM alert_rule_original a " +
            " INNER JOIN (SELECT MAX(o.id) AS id,MAX(b.`alert_level`) AS alert_level FROM alert_rule_original o, alert_rule b  GROUP BY o.keyword_value ) t ON a.`id` = t.id" +
            " WHERE  %s AND a.deal_status = 0  ORDER BY a.`update_time` DESC";


    public static final String SELECT_SQL_TRIGGER = "SELECT  keyword_value  FROM alert_rule_trigger where rule_id = %d ";

    public static final String SELECT_SQL_TRIGGER_GROUP = " SELECT strategy_id FROM alert_rule_trigger GROUP BY strategy_id";

    public static final String DELETE_SQL_TRIGGER = " delete from alert_rule_trigger where strategy_id = %d and keyword_value = '%s' ";

    public static final String DELETE_SQL_LOOP = " DELETE FROM alert_loop WHERE ip =  '%s' ";

    public static final String DELETE_SQL_RULE_RECORD = " delete from alert_rule_record where rule_id = %d and keyword_value = '%s' ";

    public static final String DELETE_SQL_RULE_ORIGINAL = " delete from alert_rule_original where strategy_id = %d and keyword_value = '%s' ";

    public static final String UPDATE_SQL_RULE_ORIGINAL = " UPDATE alert_rule_original SET deal_status = 2, dispose_status = 2,recover_status=2,recover_time=NOW() WHERE  strategy_id = %d AND keyword_value = '%s' ";


    public static final String SELECT_SQL_RULE_RECORD = " SELECT * from alert_rule_record where rule_id = %d and keyword_value in %s ";

    public static final String SELECT_SQL_RULE_ORIGINAL = " SELECT * from alert_rule_original where strategy_id = %d and keyword_value in %s ";

    public static final String SELECT_SQL_SQL_RULE_ORIGINAL = " SELECT * FROM alert_rule_original " +
            " WHERE keyword_value = '%s' ORDER BY create_time DESC LIMIT 1";


    public static final String SELECT_STRATEGY_LIST="select s.* from alert_rule_strategy s left join alert_rule r on s.rule_id = r.id where r.job_status=0";

    public static final String SELECT_SQL_ASSET_GROUP="select ip_addr as ip from asset_guard_line_asset where guard_line_id = %d";

}
