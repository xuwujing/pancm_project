package com.zans.dao;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警规则接口
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/24
 */
public interface AlertDao {

    /**
     * @Author pancm
     * @Description 查看告警相关服务信息
     * @Date  2020/9/7
     * @Param []
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> queryAlertServer() throws Exception;

    /**
     * @Author pancm
     * @Description 查询告警规则信息
     * @Date  2020/9/7
     * @Param [lastUpdateTime]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> queryAlertRule(String lastUpdateTime) throws Exception;

    /**
     * @Author pancm
     * @Description 单条数据写入
     * @Date  2020/9/7
     * @Param [tableName, map]
     * @return boolean
     **/
    boolean insert(String tableName,Map<String,Object> map) throws Exception;

    /**
     * @Author pancm
     * @Description 批量数据写入
     * @Date  2020/9/7
     * @Param [tableName, data]
     * @return boolean
     **/
    boolean insertAll(String tableName,List<Map<String,Object>> data) throws Exception;


    /**
     * @Author pancm
     * @Description 批量插入/更新
     * @Date  2020/10/19
     * @Param [tableName, data]
     * @return boolean
     **/
    boolean replaceAll(String tableName,List<Map<String,Object>> data) throws Exception;



    /**
     * @Author pancm
     * @Description 直接执行sql
     * @Date  2020/9/9
     * @Param [tableName, data]
     * @return int
     **/
    int executeUpdate(String sql) throws Exception;

    /**
     * @Author pancm
     * @Description 批量执行
     * @Date  2020/10/22
     * @Param [sql]
     * @return int
     **/
    int[] updateBatch(List<String> sqlS) throws Exception;



    /**
     * @Author pancm
     * @Description 查询告警记录聚合信息
     * @Date  2020/9/7
     * @Param [lastUpdateTime]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> agg(String lastUpdateTime) throws Exception;


    /**
     * @Author pancm
     * @Description sql 查询
     * @Date  2020/8/24
     * @Param [sql]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> query(String sql) throws Exception;

    /**
     * @Author pancm
     * @Description 根据条件进行查询
     * @Date  2020/10/22
     * @Param [sql, whereMap]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> query(String tableName, Map<String, Object> whereMap) throws Exception;

    /**
     * @Author pancm
     * @Description
     * @Date  2020/10/22
     * @Param [tableName, whereMap, valueMap]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    int updateByQuery(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap) throws Exception;

}
