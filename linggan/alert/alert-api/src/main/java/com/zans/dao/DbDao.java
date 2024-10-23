package com.zans.dao;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: mysql通用执行dao
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/24
 */
public interface DbDao {

    /**
     * @Author pancm
     * @Description sql 查询
     * @Date  2020/8/24
     * @Param [sql]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> query(String sql) throws Exception;

    boolean executeUpdate(String sql) throws Exception;



}
