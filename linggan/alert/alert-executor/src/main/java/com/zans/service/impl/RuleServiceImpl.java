package com.zans.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zans.commons.contants.Constants;
import com.zans.commons.utils.MyTools;
import com.zans.commons.utils.RedisUtil;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.pojo.AlertRecordBean;
import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertServerBean;
import com.zans.pojo.JobBean;
import com.zans.service.IEsDealService;
import com.zans.service.IMySqlDealService;
import com.zans.service.IPrometheusDealService;
import com.zans.service.IRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.commons.contants.Constants.LEFT_PTS_SIGN;
import static com.zans.commons.contants.Constants.RIGHT_PTS_SIGN;
import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警规则执行业务层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
@Service
@Slf4j
@Scope(value = "prototype")
public class RuleServiceImpl implements IRuleService {

    @Autowired
    private IEsDealService iEsDealService;

    @Autowired
    private IMySqlDealService iMySqlDealService;

    @Autowired
    private IPrometheusDealService iPrometheusDealService;

    @Resource
    private RedisUtil redisUtil;


    private AlertDao alertDao = new AlertDaoImpl();


    @Override
    public void deal(JobBean jobBean, AlertServerBean alertServerBean, AlertRuleBean alertRuleBean) {
        /*
         * 1.根据告警规则数据源进行不同的数据处理
         * 2.如果达到了该告警规则的阈值，则将数据处理的结果将数据写入到redis的数据队列中
         **/
        JSONArray jsonArray = null;
        int job_id = jobBean.getJob_id();
        long task_id = jobBean.getTask_id();
        long ruleId = alertRuleBean.getId();
        try {
            int datasource = alertRuleBean.getAlert_datasource();
            if (datasource == 1) {
                jsonArray = iEsDealService.analyzeEs(alertServerBean, alertRuleBean);
            } else if (datasource == 2) {
                jsonArray = iMySqlDealService.analyzeMySql(alertServerBean, alertRuleBean);
            } else if (datasource == 3) {
                jsonArray = iPrometheusDealService.analyzePrometheus(alertServerBean, alertRuleBean);
            }
            if (jsonArray == null || jsonArray.size() == 0) {
                log.info("job_id:{},task_id:{},策略ID:{},无数据！", job_id, task_id, ruleId);
                return;
            }
            int period = alertRuleCachePeriod.get(ruleId).incrementAndGet();
            if (period >= alertRuleBean.getAlert_period()) {
                String keyWorld = alertRuleBean.getAlert_keyword();
                List<AlertRecordBean> mapList = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String business = jsonObject.getString(keyWorld);
                    String businessId = getBusinessId(ruleId, business);
                    AlertRecordBean recordBean = getData(alertRuleBean, jsonObject, businessId);
                    mapList.add(recordBean);
                }
                redisUtil.leftPush(REDIS_QUEUE, MyTools.toString(mapList));
                redisUtil.set(String.valueOf(ruleId), alertRuleBean.getAlert_expire());
                log.info("job_id:{},task_id:{},策略ID:{},条数:{},已写入到redis队列中!", job_id, task_id, ruleId,mapList.size());
                alertRuleCachePeriod.get(alertRuleBean.getId()).set(0);
                return;
            }
            log.info("job_id:{},task_id:{},策略ID:{},第 {} 次触发警告！警告阈值:{}!", job_id, task_id, ruleId, period, alertRuleBean.getAlert_period());
        } catch (Exception e) {
            log.error("任务执行出现了异常！job_id:{},task_id:{},策略ID:{}", job_id, task_id, ruleId, e);
            alertRuleCachePeriod.get(alertRuleBean.getId()).set(0);
        }
    }

    @Override
    public void recover(AlertServerBean alertServerBean, long ruleId) {
        /**
         * @Author pancm
         * @Description 告警记录恢复处理逻辑
         * 1.根据规则ID找到规则的执行语句，然后执行;
         * 2.根据规则ID找到告警触发表，找到触发告警的keywordValue;
         * 3.将两者的结果做差集，得到已经恢复的keywordValue;
         * 4.将恢复的keywordValue的数据在告警触发表，告警记录表中进行删除并将删除的记录写入到删除表中。
         * 5.执行完毕之后，该任务结束。
         **/
        AlertRuleBean alertRuleBean = alertRuleCache.get(ruleId);
        if (alertRuleBean == null) {
            return;
        }
        JSONArray jsonArray = null;
        int datasource = alertRuleBean.getAlert_datasource();
        try {
            if (datasource == 1) {
                jsonArray = iEsDealService.analyzeEs(alertServerBean, alertRuleBean);
            } else if (datasource == 2) {
                alertRuleBean.setSql_action(null);
                jsonArray = iMySqlDealService.analyzeMySql(alertServerBean, alertRuleBean);
            } else if (datasource == 3) {
                jsonArray = iPrometheusDealService.analyzePrometheus(alertServerBean, alertRuleBean);
            }
            String sql = String.format(SELECT_SQL_TRIGGER, ruleId);
            List<Map<String, Object>> existAlertList = alertDao.query(sql);
            if (MyTools.isEmpty(existAlertList)) {
                return;
            }
            List<String> existAlertListValue = Lists.newArrayList();
            List<String> curAlertListValue = Lists.newArrayList();
            getAlertList(alertRuleBean, jsonArray, existAlertList, existAlertListValue, curAlertListValue);
            if (MyTools.isEmpty(existAlertListValue)) {
                return;
            }
            log.info("ruleId:{} 的告警需要恢复的数据！{}", ruleId, existAlertListValue);
            List<String> delSql = Lists.newArrayList();
            List<String> selectSql = Lists.newArrayList();
            StringBuffer queryDel = new StringBuffer("(");
            getDelAndSelectSql(ruleId, existAlertListValue, delSql, selectSql, queryDel);
            dealSql(ruleId, delSql, selectSql, queryDel);
            log.info("ruleId:{} 的告警数据:{} 已完成记录恢复！", ruleId,existAlertListValue);
        } catch (Exception e) {
            log.error("ruleId:{} 的告警记录恢复处理失败！原因是:{}", ruleId, e);
        }
    }

    /**
     * @Author pancm
     * @Description 处理这些sql语句
     * @Date  2020/11/2
     * @Param [ruleId, delSql, selectSql, queryDel]
     * @return void
     **/
    private void dealSql(long ruleId, List<String> delSql, List<String> selectSql, StringBuffer queryDel) throws Exception {
        String delRecordSql = String.format(SELECT_SQL_RULE_RECORD, ruleId, queryDel.toString());
        List<Map<String, Object>> delRecordList = alertDao.query(delRecordSql);
        if (MyTools.isNotEmpty(delRecordList)) {
//            alertDao.insertAll(TABLE_ALERT_RULE_RECORD_DEL, delRecordList);
        }
        //批量删除
        alertDao.updateBatch(delSql);
//        for (String s : selectSql) {
//            List<Map<String, Object>> mapList = alertDao.query(s);
//            if (mapList == null || mapList.size() == 0) {
//                continue;
//            }
//            Map<String, Object> map = mapList.get(0);
//            Map<String, Object> whereMap = Maps.newHashMap();
//            whereMap.put("keyword_value",map.get("keyword_value"));
//            save(TABLE_ALERT_RULE_RECORD,map,whereMap);
//        }
    }


    private void save(String tableName,Map<String, Object> valueMap,Map<String, Object> whereMap) throws Exception {
        List<Map<String,Object>> mapList = alertDao.query(tableName,whereMap);
        if(MyTools.isEmpty(mapList)){
            alertDao.insert(tableName,valueMap);
        }
    }

    /**
     * @Author pancm
     * @Description 获取触发告警记录的list
     * @Date  2020/11/2
     * @Param [alertRuleBean, jsonArray, existAlertList, existAlertListValue, curAlertListValue]
     * @return void
     **/
    private void getAlertList(AlertRuleBean alertRuleBean, JSONArray jsonArray, List<Map<String, Object>> existAlertList, List<String> existAlertListValue, List<String> curAlertListValue) {
        existAlertList.forEach(map -> {
            map.forEach((k, v) -> {
                existAlertListValue.add((String) v);
            });
        });
        String keyWord = alertRuleBean.getAlert_keyword();
        if (MyTools.isNotEmpty(jsonArray)) {
            jsonArray.forEach(object -> {
                JSONObject jsonObject = (JSONObject) object;
                jsonObject.forEach((k, v) -> {
                    if (keyWord.equals(k)) {
                        curAlertListValue.add((String) v);
                    }
                });
            });
            existAlertListValue.removeAll(curAlertListValue);
        }
    }

    /**
     * @Author pancm
     * @Description 获取需要删除和查询的sql语句
     * @Date  2020/11/2
     * @Param [ruleId, existAlertListValue, delSql, selectSql, queryDel]
     * @return void
     **/
    private void getDelAndSelectSql(long ruleId, List<String> existAlertListValue, List<String> delSql, List<String> selectSql, StringBuffer queryDel) {
        for (int i = 0; i < existAlertListValue.size(); i++) {
            String keywordValue = existAlertListValue.get(i);
            String delTriggerSql = String.format(DELETE_SQL_TRIGGER, ruleId, keywordValue);
//            String delRuleRecordSql = String.format(DELETE_SQL_RULE_RECORD, ruleId, keywordValue);
            String updateRuleOriginalSql = String.format(UPDATE_SQL_RULE_ORIGINAL, ruleId, keywordValue);
            String selectRuleOriginalSql = String.format(SELECT_SQL_SQL_RULE_ORIGINAL, keywordValue);
            delSql.add(delTriggerSql);
//            delSql.add(delRuleRecordSql);
            delSql.add(updateRuleOriginalSql);
            selectSql.add(selectRuleOriginalSql);
            queryDel.append("'").append(keywordValue).append("'").append(",");
        }
        queryDel.deleteCharAt(queryDel.lastIndexOf(","));
        queryDel.append(")");
    }


    /**
     * @return com.zans.pojo.AlertRecordBean
     * @Author pancm
     * @Description 得到告警内容信息
     * @Date 2020/9/3
     * @Param [alertRuleBean, jsonObject, businessId]
     **/
    private AlertRecordBean getData(AlertRuleBean alertRuleBean, JSONObject jsonObject, String businessId) throws Exception {
        AlertRecordBean bean = new AlertRecordBean();
        String keywordValue = jsonObject.getString(alertRuleBean.getAlert_keyword());
        bean.setKeyword_value(keywordValue);
        bean.setRule_id(alertRuleBean.getId());
        bean.setBusiness_id(businessId);
        bean.setQuery_sql(alertRuleBean.getQuery_sql());
        bean.setQuery_detail_sql(alertRuleBean.getQuery_detail_sql());
        bean.setQuery_detail_ex_sql(alertRuleBean.getQuery_detail_ex_sql());
        bean.setSql_json(jsonObject);
        String info = getInfo(jsonObject, alertRuleBean);
        if (info.indexOf(LEFT_PTS_SIGN) > -1 || info.indexOf(RIGHT_PTS_SIGN) > -1) {
            log.error("策略ID:{},info:{},jsonObject:{},alert_template:{}", alertRuleBean.getId(), info, jsonObject, alertRuleBean.getAlert_template());
            throw new Exception("出现了未映射的数据！");
        }
        bean.setNotice_info(info);
        return bean;
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 组装告警内容信息
     * @Date 2020/9/3
     * @Param [jsonObject, bean]
     **/
    private String getInfo(JSONObject jsonObject, AlertRuleBean bean) {
        return MyTools.format(bean.getAlert_template(), jsonObject);
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 得到唯一主键
     * @Date 2020/9/3
     * @Param [ruleId, business]
     **/
    private String getBusinessId(long ruleId, String business) {
        return ruleId + Constants.LINE_SIGN + business;
    }


}

