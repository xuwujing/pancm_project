package com.zans.service.impl;

import com.zans.dao.SysConstantDao;
import com.zans.service.IAlertService;
import com.zans.util.MyTools;
import com.zans.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.config.Constants.*;
import static com.zans.config.SQLConstants.*;

/**
 * @author beixing
 * @Title: debug-api
 * @Description: 基础类实现方法
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/19
 */
@Service
public class AAABaseServiceImpl {

    @Resource
    protected JdbcTemplate jdbcTemplate;

    @Resource
    @Qualifier("radiusDataSource")
    protected JdbcTemplate radiusJdbcTemplate;

    @Autowired
    private IAlertService alertService;

    @Resource
    protected SysConstantDao sysConstantDao;



    protected Map<String, Object> findRadiusEndpointByMac(String mac) {
        String sql = String.format(SELECT_SQL_RADIUS_ENDPOINT, mac);
        Map<String, Object> map = queryForMap(sql);
        return map;
    }

    protected Map<String, Object> findRadiusEndpointProfileById(int id) {
        String sql = String.format(SELECT_SQL_RADIUS_ENDPOINT_PROFILE_BY_ID, id);
        Map<String, Object> map = queryForMap(sql);
        return map;
    }

    protected long saveRadiusEndpoint(String mac, int policy) {
        String sql = String.format(INSERT_SQL_RADIUS_ENDPOINT, mac, policy, QZ_REASON_NEW_ASSET_STR, ENDPOINT_SOURCE_ARP_RADIUS_YES);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return ps;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        long id = keyHolder.getKey().longValue();
        return id;
    }


    /**
     * @return
     * @Author beixing
     * @Description 更新policy的状态
     * @Date 2022/3/21
     * @Param
     **/
    protected void updateRadiusEndpoint(long id, int policy) {
        String sql = String.format(UPDATE_SQL_RADIUS_ENDPOINT_POLICY, id, policy);
        jdbcTemplate.execute(sql);
        return;
    }

    /**
     * @return
     * @Author beixing
     * @Description
     * @Date 2022/3/21
     * @Param
     **/
    protected void saveRadiusEndpointProfile(long id, String mac, String ip, String nasPortId,
                                             String nasIpAddress, String swIp, String replyMessage,
                                             String filterId, String time, String acctSessionId, String vlan) {
        String sql = String.format(INSERT_SQL_RADIUS_ENDPOINT_PROFILE, id, mac, ip, nasPortId, nasIpAddress, swIp, replyMessage,
                filterId, time, acctSessionId, vlan);
        jdbcTemplate.execute(sql);
        return;
    }


    /**
     * @return
     * @Author beixing
     * @Description
     * @Date 2022/3/21
     * @Param
     **/
    protected void addQueueIpData(String queueName, String ip, String params) {
        String sql = String.format(SELECT_SQL_QUEUE_IP_DATA, queueName, ip, 0);
        Map<String, Object>  map = queryForMap(sql);
        if (MyTools.isNotEmpty(map)) {
            return;
        }
        String saveSql = String.format(INSERT_SQL_QUEUE_IP_DATA, queueName, ip, params, 0);
        jdbcTemplate.execute(saveSql);
        return;
    }


    /**
     * @return
     * @Author beixing
     * @Description 获取同步的id数据
     * @Date 2022/3/19
     * @Param
     **/
    protected String getSyncStartIndex(String markName, String destName) {
        String sql = String.format(SELECT_SQL_SYNC_JOB, SERVER_ID, markName, destName);
        Map<String, Object> object = queryForMap(sql);
        String startIndex = "";
        if (MyTools.isEmpty(object)) {
//            log.warn("markName:{},destName:{}不存在,进行创建！", markName, destName);
            String createSql = String.format(INSERT_SQL_SYNC_JOB, SERVER_ID, markName, destName);
            jdbcTemplate.execute(createSql);
            object = queryForMap(sql);
        }
        startIndex = (String) object.get("sync_index");
        return startIndex;
    }

    /**
     * @return
     * @Author beixing
     * @Description 获取同步的id数据
     * @Date 2022/3/19
     * @Param
     **/
    protected String getSyncEndIndex(String markName, String type) {
        String sql = String.format(SELECT_SQL_TABLE_MAX_KEY, AAA_TIMESTAMP_COLUMN, markName);
        Map<String, Object> object = new HashMap<>();
        if (SYNC_DIRECTION_GUARD_TO_RADIUS.equals(type)) {
            object =queryForMap(sql);
        } else {
            object = radiusQueryForMap(sql);
        }
        String endIndex = "";
        if (MyTools.isEmpty(object)) {
//            log.error("markName:{},查询为空！请检查表中数据！");
            return null;
        }
        endIndex = String.valueOf(object.get("mark"));
        return endIndex;
    }

    /**
     * @return
     * @Author beixing
     * @Description 获取同步的数据
     * @Date 2022/3/19
     * @Param
     **/
    protected List<Map<String, Object>> getSyncData(String markName, String destName, String type) {
        String startIndex = getSyncStartIndex(markName, destName);
        String endIndex = getSyncEndIndex(markName, type);
        if (MyTools.isEmpty(endIndex)) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> mapList = getSyncData(markName, type, startIndex, endIndex);
        return mapList;
    }


    /**
     * @return
     * @Author beixing
     * @Description 更新同步的数据
     * @Date 2022/3/19
     * @Param
     **/
    protected void updateSyncData(String markName, String destName,String index) {
        String sql = String.format(UPDATE_SQL_SYNC_JOB, index,SERVER_ID,markName,  destName);
        jdbcTemplate.execute(sql);
        return ;
    }

    /**
     * @return
     * @Author beixing
     * @Description 获取资产黑名单的数据
     * @Date 2022/3/19
     * @Param
     **/
    protected List<String> getMacBlackList() {
        String sql = SELECT_SQL_ASSET_BLACK_lIST;
        List<String> stringList = new ArrayList<>();
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        if (MyTools.isNotEmpty(mapList)) {
            mapList.forEach(stringObjectMap -> {
                stringList.add((String) stringObjectMap.get("mac"));
            });
        }
        return stringList;
    }

    /**
     * @return
     * @Author beixing
     * @Description mac黑名单的校验
     * @Date 2022/3/19
     * @Param
     **/
    protected boolean isMacValid(String mac, List<String> list) {
        if (MyTools.isEmpty(list)) {
            return true;
        }
        if (list.contains(mac)) {
            return false;
        }
        return true;
    }

    /**
     * @return
     * @Author beixing
     * @Description ip 白名单前缀的校验
     * @Date 2022/3/19
     * @Param
     **/
    protected boolean isIpValid(String ip) {
        List<String> whiteList = getWhiteListPre();
        if (MyTools.isEmpty(whiteList)) {
            return true;
        }
        for (String s : whiteList) {
            if (ip.startsWith(s)) {
                return true;
            }
        }
        return false;
    }


    protected List<String> getWhiteListPre() {
        String sql = String.format(SELECT_SQL_SYS_CONSTANT, ASSET_WHITE_LIST);
        List<String> stringList = new ArrayList<>();
        Map<String, Object> map  = queryForMap(sql);
        if (map.get("value") == null) {
            return stringList;
        }
        String value = (String) map.get("value");
        String[] values = value.split(COMMA_SIGN);
        if (values != null && values.length > 0) {
            for (String s : values) {
                stringList.add(s);
            }
        }
        return stringList;
    }


    protected String getVlan(String nasPortId) {
        String vlan = "";
        String[] parts = nasPortId.split(TIME_SEPARATOR2);
        for (String part : parts) {
            String[] pair = part.split(EQUAL_SIGN);
            if (pair.length > 1 && pair[0].startsWith("vlan")) {
                vlan = pair[1];
            }
        }
        return vlan;
    }


    /**
     * @return
     * @Author beixing
     * @Description 获取同步的数据
     * @Date 2022/3/19
     * @Param
     **/
    protected List<Map<String, Object>> getSyncData(String markName, String type, String startIndex, String endIndex) {
        List<Map<String, Object>> mapList = null;
        if (SYNC_DIRECTION_GUARD_TO_RADIUS.equals(type)) {
            mapList = getTableData(markName, startIndex, endIndex);
        } else {
            mapList = getRadiusTableData(markName, startIndex, endIndex);
        }
        return mapList;
    }


    /**
     * 获取guard_scan的数据
     *
     * @param tableName
     * @param startIndex
     * @param endIndex
     * @return
     */
    private List<Map<String, Object>> getTableData(String tableName, String startIndex, String endIndex) {
        String sql = pageSql(tableName, startIndex, endIndex);
        return jdbcTemplate.queryForList(sql);
    }


    private List<Map<String, Object>> getRadiusTableData(String tableName, String startIndex, String endIndex) {
        String sql = pageSql(tableName, startIndex, endIndex);
        return radiusJdbcTemplate.queryForList(sql);
    }

    private String pageSql(String tableName, String startIndex, String endIndex) {
        String sql = String.format(SELECT_SQL_TABLE_NAME, tableName, AAA_TIMESTAMP_COLUMN, endIndex);
        if (MyTools.isNotEmpty(startIndex)) {
            sql += " and " + AAA_TIMESTAMP_COLUMN + ">" + "'" + startIndex + "'";
        }
        sql += " order by " + AAA_TIMESTAMP_COLUMN + " limit " + SQL_LIMIT;
        return sql;
    }


    protected ApiResult sendAlertNewEndpoint(String nasName, String nasIp, String ip, String mac) {
        Map<String, Object> map = new HashMap();
        alertService.sendAlertNewEndpoint(ALERT_RULE_ADD,map);
        return ApiResult.success();
    }

    /**
    * @Author beixing
    * @Description 针对jdbcTemplate.queryForMap方法查不到报错改进
    * @Date  2022/3/21
    * @Param
    * @return
    **/
    protected Map<String, Object> queryForMap(String  sql){
        Map<String, Object> map = new HashMap<>();
        try {
            map = jdbcTemplate.queryForMap(sql);
        }catch (EmptyResultDataAccessException e){

        }
        return map;
    }

    /**
     * @Author beixing
     * @Description 针对jdbcTemplate.queryForMap方法查不到报错改进
     * @Date  2022/3/21
     * @Param
     * @return
     **/
    protected Map<String, Object> radiusQueryForMap(String  sql){
        Map<String, Object> map = new HashMap<>();
        try {
            map = radiusJdbcTemplate.queryForMap(sql);
        }catch (EmptyResultDataAccessException e){

        }
        return map;
    }
}
