package com.zans.dao;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: job查询dao
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */
public interface JobDao {

    /**
     * @Author pancm
     * @Description job任务查询
     * @Date  2020/9/2
     * @Param [taskId]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> queryJob(int taskId) throws Exception;


    /**
     * @Author pancm
     * @Description job任务查询
     * @Date  2020/9/2
     * @Param [taskId]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> queryJobId(String jobName) throws Exception;

	int updateByQuery(String tableName, Map<String, Object> jobUpdateMap, Map<String, Object> jobWhereMap) throws Exception;
}
