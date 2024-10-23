package com.zans.dao.impl;

import com.zans.commons.utils.DBUtil;
import com.zans.dao.JobDao;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.zans.commons.contants.Constants.MYSQL_JOB;
import static com.zans.contants.AlertConstants.SELECT_SQL_JOB;
import static com.zans.contants.AlertConstants.SELECT_SQL_JOB_ID;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */
@Slf4j
public class JobDaoImpl implements JobDao {

    DBUtil dbUtil =null;

    public JobDaoImpl() {
        dbUtil = new DBUtil(MYSQL_JOB);
    }


    @Override
    public List<Map<String,Object>> queryJob(int taskId) throws Exception {
        String sql = String.format(SELECT_SQL_JOB,taskId);
        return dbUtil.query(sql);
    }

    @Override
    public List<Map<String, Object>> queryJobId(String jobName) throws Exception {
        String sql = String.format(SELECT_SQL_JOB_ID,jobName);
        log.info("sql:{}",sql);
        return  dbUtil.query(sql);
    }

    @Override
    public int updateByQuery(String tableName, Map<String, Object> jobUpdateMap, Map<String, Object> jobWhereMap) throws Exception{
        return dbUtil.update(tableName,jobUpdateMap,jobWhereMap);
    }
}
