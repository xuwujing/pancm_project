package com.zans.dao.impl;

import com.zans.commons.utils.DBUtil;
import com.zans.commons.utils.MyTools;
import com.zans.dao.AlertDao;

import java.util.List;
import java.util.Map;

import static com.zans.commons.contants.Constants.MYSQL_ALERT;
import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警规则数据实现类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/24
 */
public class AlertDaoImpl implements AlertDao {

    DBUtil dbUtil =null;
    public AlertDaoImpl(){
        dbUtil = new DBUtil(MYSQL_ALERT);
    }

    @Override
    public List<Map<String, Object>> queryAlertServer() throws Exception {
        return dbUtil.executeQuery(SELECT_SQL_SERVER);
    }

    @Override
    public List<Map<String, Object>> queryAlertRule(String lastUpdateTime) throws Exception {
        String sql =SELECT_SQL_RULE;
        String where = " rule_status =1 ";
        if (MyTools.isNotEmpty(lastUpdateTime)) {
            where += " and update_time >'" + lastUpdateTime + "'";
        }
        sql = String.format(sql, where);
        return dbUtil.executeQuery(sql);
    }

    @Override
    public boolean insert(String tableName,Map<String, Object> valueMap) throws Exception {
        dbUtil.insert(tableName, valueMap);
        return true;
    }

    @Override
    public boolean insertAll(String tableName, List<Map<String,Object>> data) throws Exception {
        dbUtil.insertAll(tableName,data);
        return true;
    }

    /**
     * @param tableName
     * @param data
     * @return boolean
     * @Author pancm
     * @Description 进行更新
     * @Date 2020/10/19
     * @Param [tableName, data]
     */
    @Override
    public boolean replaceAll(String tableName, List<Map<String, Object>> data) throws Exception {
        dbUtil.replaceAll(tableName,data);
        return true;
    }

    @Override
    public int executeUpdate(String sql) throws Exception {
        return dbUtil.executeUpdate(sql);
    }

    /**
     * @param sqlS
     * @return int
     * @Author pancm
     * @Description 批量执行
     * @Date 2020/10/22
     * @Param [sql]
     */
    @Override
    public int[] updateBatch(List<String> sqlS) throws Exception {
        return dbUtil.updateBatch(sqlS);
    }

    /**
     * @param lastUpdateTime
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author pancm
     * @Description 查询告警记录聚合信息
     * @Date 2020/9/7
     * @Param [lastUpdateTime]
     */
    @Override
    public List<Map<String, Object>> agg(String lastUpdateTime) throws Exception {
        String sql = SELECT_SQL_AGG;
        String where = " 1=1 ";
        if(MyTools.isNotEmpty(lastUpdateTime)){
            where = "  a.update_time >'" + lastUpdateTime + "'";
        }
        sql = String.format(sql, where);
        return dbUtil.executeQuery(sql);
    }

    /**
     * @param sql
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author pancm
     * @Description sql 查询
     * @Date 2020/8/24
     * @Param [sql]
     */
    @Override
    public List<Map<String, Object>> query(String sql) throws Exception {
        return dbUtil.query(sql);
    }

    /**
     * @param tableName
     * @param whereMap
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author pancm
     * @Description 根据条件进行查询
     * @Date 2020/10/22
     * @Param [sql, whereMap]
     */
    @Override
    public List<Map<String, Object>> query(String tableName, Map<String, Object> whereMap) throws Exception {
        return dbUtil.query(tableName,whereMap);
    }

    /**
     * @param tableName
     * @param whereMap
     * @param valueMap
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author pancm
     * @Description
     * @Date 2020/10/22
     * @Param [tableName, whereMap, valueMap]
     */
    @Override
    public int updateByQuery(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap) throws Exception {
        return dbUtil.update(tableName,valueMap,whereMap);
    }


}
