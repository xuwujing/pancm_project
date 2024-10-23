package com.zans.config;

/**
 * @author beixing
 * @Title: debug-api
 * @Description: SQL常量类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/19
 */
public final class SQLConstants {

    /**
     * -------------- aaa系列 end ------------------------
     */
    //查询所有的资产黑名单mac数据
    public static String SELECT_SQL_ASSET_BLACK_lIST = "select mac as mac from asset_blacklist where delete_status = 0";

    public static String SELECT_SQL_SYS_CONSTANT = " select  constant_value as value from sys_constant where constant_key = '%s'";

    // 查询设备数据
    public static String SELECT_SQL_RADIUS_ENDPOINT = " select * from radius_endpoint where mac='%s'";

    public static String INSERT_SQL_RADIUS_ENDPOINT = " insert into radius_endpoint(mac, access_policy,auth_mark," +
            "  source) VALUES ('%s','%s','%s','%s')";

    public static String UPDATE_SQL_RADIUS_ENDPOINT_POLICY = " update radius_endpoint set access_policy='%s' where id='%s'";

    public static String UPDATE_SQL_RADIUS_ENDPOINT_BASE_IP = " update radius_endpoint set base_ip='%s'  where mac='%s'";


    public static String INSERT_SQL_RADIUS_ENDPOINT_PROFILE = " insert into radius_endpoint_profile(endpoint_id, mac, " +
            "          cur_ip_addr, cur_nas_port_id, cur_nas_ip_address, cur_sw_ip, " +
            "            reply_message, filter_id, acct_update_time, acct_session_id, cur_vlan) values " +
            "              ('%s','%s', '%s','%s','%s','%s',  '%s','%s','%s','%s', '%s')";

    public static String SELECT_SQL_RADIUS_ENDPOINT_PROFILE_BY_ID = " select p.*, e.access_policy from radius_endpoint_profile p " +
            " inner join radius_endpoint e on e.id=p.endpoint_id  where p.endpoint_id='%s'";

    public static String SELECT_SQL_RADIUS_ENDPOINT_PROFILE_BY_MAC = " select p.*, e.access_policy from radius_endpoint_profile p " +
            " inner join radius_endpoint e on e.id=p.endpoint_id  where p.mac='%s'";




    public static String SELECT_SQL_NETWORK_NAS = "SELECT  nn.qz_enable, nn.asset_enable FROM   network_nas nn " +
            "  WHERE  nn.delete_status=0 and nn.nas_ip='%s'  ORDER BY nn.weight asc  LIMIT 1";



    public static String SELECT_SQL_QUEUE_IP_DATA = "select id from queue_ip where name='%s' and ip_addr='%s' and status='%s'";
    public static String INSERT_SQL_QUEUE_IP_DATA = "insert into queue_ip(name, ip_addr, params, status) values('%s', '%s', '%s', '%s')";



    public static String SELECT_SQL_TABLE_NAME = "select * from %s where %s<='%s'";

    public static String SELECT_SQL_TABLE_MAX_KEY = "select max(%s) as mark  from %s";


    public static String SELECT_SQL_SYNC_JOB = "select * from radius_sync_job where server_id= '%s' and table_name='%s' and dest_name='%s'";

    public static String INSERT_SQL_SYNC_JOB = "insert into radius_sync_job(job_name, server_id, table_name, dest_name) values ('%s', '%s', '%s', '%s')";

    public static String UPDATE_SQL_SYNC_JOB = "update radius_sync_job set sync_index='%s' where server_id='%s' and table_name='%s' and dest_name='%s'";


    public static String SELECT_SQL_ASSET_BASELINE = " SELECT * FROM asset_baseline  WHERE ip_addr='%s' limit 1";

    public static String UPDATE_SQL_ASSET_BASELINE = " update asset_baseline set nas_ip='%s',nas_port_id='%s',vlan='%s',mac='%s',bind_status='%s' where ip_addr='%s'";

    public static String INSERT_SQL_ASSET_BASELINE = " insert into asset_baseline(ip_addr, nas_ip, nas_port_id,  vlan, mac,bind_status) " +
            "   values('%s','%s','%s', '%s','%s','%s')";


    public static String SELECT_SQL_ASSET_PROFILE = " select p.asset_id as id, p.* from asset_profile p where p.ip_addr='%s'";


    public static String INSERT_SQL_ASSET_PROFILE = " insert into asset_profile(ip_addr,cur_mac,cur_sw_ip, creator_id, update_id," +
            "              source) values('%s', '%s','%s','%s','%s', '%s')";

    public static String UPDATE_SQL_ASSET_PROFILE = " update asset_profile set cur_mac='%s',cur_sw_ip='%s' where asset_id='%s'";


    public static String SELECT_SQL_ALIVE_HEARTBEAT = "  SELECT * FROM alive_heartbeat  WHERE mac= '%s' limit 1";

    public static String INSERT_SQL_ALIVE_HEARTBEAT = " insert into alive_heartbeat(mac, ip_addr, alive, alive_last_time) " +
            " values(%s,%s,%s,%s)";


    public static String SELECT_SQL_NAS = "select id from nas where nasname='%s'";

    public static String INSERT_SQL_NAS = "insert into radius_sync_job(job_name, server_id, table_name, dest_name) values ('%s', '%s', '%s', '%s')";

    public static String UPDATE_SQL_NAS = "update nas set secret='%s', shortname='%s', type='%s', description='%s' where nasname='%s'";

    public static String DELETE_SQL_NAS = " delete from nas where nasname='%s'";



    /**    ------ aaa radius系列 start  ------- */

    public static String SELECT_SQL_RADIUS_RADIUS_ENDPOINT = "  SELECT * FROM alive_heartbeat  WHERE mac= '%s' limit 1";


    public static String INSERT_SQL_RADIUS_RADIUS_ENDPOINT = " insert into radius_endpoint(username, pass, access_policy, base_ip," +
            " delete_status, source) VALUES  (%s,%s,%s,%s,%s,%s)";

    public static String UPDATE_SQL_RADIUS_RADIUS_ENDPOINT = " update radius_endpoint set access_policy=%s, base_ip=%s," +
            " delete_status=%s, source=%s where username=%s";



    /**    ------ aaa radius系列 end  ------- */


    /**  ------------- aaa系列 end  --------------------- */

    /**
     * -------------aaa系列 start  ---------------------
     */

    public static String SELECT_SQL = "";


    /**  -------------aaa系列 end  --------------------- */


    /**  -------------network 系列 start  --------------------- */


    /**  -------------network 系列 end  --------------------- */


}
