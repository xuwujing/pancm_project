package com.zans.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.commons.config.GetProperties;
import com.zans.commons.contants.Constants;
import com.zans.commons.utils.GetSpringBeanUtil;
import com.zans.commons.utils.HttpClientUtil;
import com.zans.commons.utils.MyTools;
import com.zans.commons.utils.RedisUtil;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.pojo.AlertRecordBean;
import com.zans.pojo.AlertRuleBean;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.commons.contants.Constants.LEFT_PTS_SIGN;
import static com.zans.commons.contants.Constants.RIGHT_PTS_SIGN;
import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警记录聚合线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/20
 */
@Slf4j
public class AlertLoopTask extends Thread {

    private AlertDao alertDao;


    private long ruleId;

    private  String url = GetProperties.getAppSettings().getOrDefault("reqLoopUri", "http://27.10.30.52:21013/api/sw/loop");

    private Map<String, Object> map;

    private RedisUtil redisUtil;

    private  String eth = "eth";

    public AlertLoopTask(Map<String, Object> map,Long ruleId) {
        this.map = map;
        this.ruleId = ruleId;
        alertDao = new AlertDaoImpl();
        this.redisUtil = GetSpringBeanUtil.getBean(RedisUtil.class);
    }


    public void run() {
        try {
            log.info("线程id:{},发起请求！请求的交换机ip:{}",super.getId(),map.get("ip"));
            long startTime = System.currentTimeMillis();
            String result = HttpClientUtil.get(url, map);
            long endTime = System.currentTimeMillis();
            log.info("线程id:{},结束请求!请求的交换机ip:{},请求成环检测接口耗时:{} ms",super.getId(),map.get("ip"),(endTime-startTime));
            ApiResult apiResult = MyTools.toBean(result, ApiResult.class);
            if (apiResult.getCode() != 0) {
                log.error("请求环路接口失败！请求url:{},请求参数:{},失败原因:{}", url, map, apiResult.getMessage());
                return;
            }
            JSONArray jsonArray = MyTools.toJson(apiResult.getData()).getJSONArray("data");
            if (jsonArray == null || jsonArray.size() == 0) {
                return;
            }
            log.info("线程id:{},请求的交换机ip:{},拉取的数据条数:{}",super.getId(),map.get("ip"),jsonArray.size());
            String sql =String.format(DELETE_SQL_LOOP,map.get("ip"));
            alertDao.executeUpdate(sql);
            List<Map<String, Object>> saveList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                Map<String, Object> objectMap = (Map<String, Object>) jsonArray.get(i);
                String mac = (String) objectMap.get("mac");
                String port = (String) objectMap.get("interface");
                List<String> interfaceList = (List<String>) objectMap.get("interface_list");
                if (interfaceList != null && interfaceList.size() > 1) {
                    String[] interfaceS = String.join(Constants.COMMA_SIGN, interfaceList).split(Constants.COMMA_SIGN);
                    String eth1 = interfaceS[0].substring(0,3).toLowerCase();
                    if(!(eth1.indexOf(eth)>-1)){
                        continue;
                    }
                    int k =1;
                    for (int i1 = 1; i1 < interfaceS.length; i1++) {
                        String ethX =  interfaceS[i1].substring(0,3).toLowerCase();
                        if(!(ethX.indexOf(eth)>-1)){
                            continue;
                        }
                        k++;
                    }
                    if(k<2){
                        continue;
                    }
                    Map<String, Object> saveMap = new HashMap<>();
                    Map<String, Object> whereMap = new HashMap<>();
                    saveMap.put("ip", map.get("ip"));
                    saveMap.put("mac", mac);
                    saveMap.put("port", port);
                    saveMap.put("list_port", String.join(Constants.COMMA_SIGN, interfaceList));
                    saveMap.put("update_time",MyTools.getNowTime());
                    whereMap.put("ip", map.get("ip"));
                    whereMap.put("mac", mac);
                    save(TABLE_ALERT_LOOP,saveMap,whereMap);
                    saveList.add(saveMap);
                }
            }
            if (saveList.size() > 0) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ip",map.get("ip"));
                send(jsonObject);
                log.info("线程id:{},成环交换机ip:{},已写入:{}条数据！", super.getId(),map.get("ip"), saveList.size());
            }
        } catch (Exception e) {
            log.error("运行失败！请求url:{},请求参数:{},原因是:",url, map, e);
        }
    }


    private void save(String tableName,Map<String, Object> valueMap,Map<String, Object> whereMap) throws Exception {
        List<Map<String,Object>> mapList = alertDao.query(tableName,whereMap);
        if(MyTools.isEmpty(mapList)){
            alertDao.insert(tableName,valueMap);
        }else {
            alertDao.updateByQuery(tableName,valueMap,whereMap);
        }
    }

    private void send(JSONObject jsonObject) throws Exception {
        AlertRuleBean alertRuleBean = alertRuleCache.get(ruleId);
        String keyWorld = alertRuleBean.getAlert_keyword();
        String business = jsonObject.getString(keyWorld);
        String businessId = getBusinessId(ruleId, business);
        String querySql = alertRuleBean.getQuery_sql();
        String queryDetailSql = alertRuleBean.getQuery_detail_sql();
        String sql = String.format(querySql, business);
        String sqlDetail = String.format(queryDetailSql, business);
        String noticeInfo = "";
        List<Map<String, Object>> resultList = alertDao.query(sql);
        List<Map<String, Object>> resultList2 = alertDao.query(sqlDetail);
        if (MyTools.isNotEmpty(resultList)) {
            Map<String, Object> whereMap = new HashMap<>();
            for (Map<String, Object> map : resultList) {
                map.put("business_id", businessId);
                map.put("keyword_value", business);
                map.put("rule_id", ruleId);
                Object obj = map.get("point_name");
                if (obj == null) {
                    obj = "点位缺失";
                }
                jsonObject.put("point_name", obj);
                noticeInfo = getInfo(jsonObject, alertRuleBean);
                map.put("notice_info", noticeInfo);
                map.put("record_source", 0);
                map.put("update_time",MyTools.getNowTime());
                map.put("deal_status",0);
                map.put("dispose_status",0);
                whereMap.put("business_id", businessId);
                save(TABLE_ALERT_RULE_ORIGINAL, map, whereMap);
                whereMap.clear();
            }
        }

        if (MyTools.isNotEmpty(resultList2)) {
            Map<String, Object> whereMap = new HashMap<>();
            for (Map<String, Object> map : resultList2) {
                map.put("business_id", businessId);
                map.put("rule_id", ruleId);
                whereMap.put("business_id", businessId);
                save(TABLE_ALERT_RULE_ORIGINAL_DETAIL, map, whereMap);
                whereMap.clear();
            }
        }
        log.info("策略id:{},成环检测已写入:{}条数据!",ruleId,resultList.size());
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

    public static void main(String[] args) {
        Map<String, Object> map2 = new HashMap<>();
        map2.put("ip", "27.27.250.99");
        long ruleId = 15;
        AlertLoopTask alertLoopTask = new AlertLoopTask(map2,ruleId);
        alertLoopTask.run();
    }

}
