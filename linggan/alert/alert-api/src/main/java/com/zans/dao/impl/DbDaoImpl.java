package com.zans.dao.impl;

import com.zans.commons.utils.DBUtil;
import com.zans.dao.DbDao;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/24
 */
public class DbDaoImpl implements DbDao {

    DBUtil dbUtil =null;

    public DbDaoImpl(String name){
        dbUtil = new DBUtil(name);
    }

    @Override
    public List<Map<String, Object>> query(String sql) throws Exception {
        return dbUtil.query(sql);
    }

    @Override
    public boolean executeUpdate(String sql) throws Exception {
        dbUtil.executeUpdate(sql);
        return true;
    }

}
