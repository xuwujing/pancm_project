package com.zans.dao;

import com.alibaba.fastjson.JSONArray;
import com.zans.pojo.RuleBean;

import java.io.IOException;

/**
 * @Author pancm
 * @Description Es查询dao
 * @Date  2020/9/1
 * @Param
 * @return
 **/
public interface EsDao {

    JSONArray query(RuleBean bean) throws IOException;

}
