package com.zans.dao.impl;

import com.zans.commons.utils.DBUtil;
import com.zans.dao.JobDao;

import java.util.List;
import java.util.Map;

import static com.zans.commons.contants.Constants.MYSQL_JOB;
import static com.zans.contants.AlertConstants.SELECT_SQL_JOB;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */

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
}
