package com.zans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zans.commons.utils.MyTools;
import com.zans.commons.utils.RedisUtil;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.service.IAlertMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警内容推送业务
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
@Service
@Slf4j
public class AlertMsgServiceImpl implements IAlertMsgService {


    private AlertDao alertDao = new AlertDaoImpl();

    @Resource
    private RedisUtil redisUtil;


    @Override
    public void deal() {
        /*
         * 1.判断redis消息队列是否存在数据，不存在则不处理
         * 2.将redis消息队列的数据写入到告警临时记录表中,用作日后查看分析使用
         * 3.根据唯一ID查询redis，不存在数据则写入告警记录表中
         * 4.将告警内容以及过期时间写入redis中
         **/
        String data = redisUtil.leftPop(REDIS_QUEUE);
        if (MyTools.isEmpty(data)) {
            return;
        }
        List mapList2 = MyTools.toList(data, null);
        List<Map<String, Object>> mapList1 = mapList2;
        try {
//            alertDao.insertAll(TABLE_ALERT_RULE_RECORD_TMP, mapList1);
            log.info("此次拉取了:{}条数据", mapList1.size());
            for (int i = 0; i < mapList1.size(); i++) {
                Map<String, Object> objectMap = mapList1.get(i);
                String businessId = (String) objectMap.get("business_id");
                if (MyTools.isEmpty(businessId)) {
                    continue;
                }

                queryAndSave(objectMap, businessId);
//                valueMap.put("notice_info",objectMap.get("notice_info"));
//                save(TABLE_ALERT_RULE_ORIGINAL,valueMap,whereMap);
//                saveRedis(objectMap, businessId, ruleId);
            }
            log.info("此次拉取了:{}条数据!已处理完毕！", mapList1.size());
        } catch (Exception e) {
            log.error("推送线程处理失败!", e);
        }
    }

    private void saveRedis(Map<String, Object> objectMap, String businessId, String ruleId) throws Exception {
        Object object = redisUtil.get(businessId);
        Map<Object, Object> objectMap1 = MyTools.toMap(MyTools.toString(object));
        if (MyTools.isEmpty(objectMap1)) {
            alertDao.insert(TABLE_ALERT_RULE_RECORD_TMP, objectMap);
            int expire = (int) redisUtil.get(ruleId);
            redisUtil.set(businessId, objectMap, expire);
            log.info("策略ID:{},businessId:{},已更新到redis中!", ruleId, businessId);
        }
    }

    /**
     * @return boolean
     * @Author pancm
     * @Description 将查询的数据写入到记录表以及详情表中
     * @Date 2020/12/6
     * @Param [objectMap]
     **/
    private void queryAndSave(Map<String, Object> objectMap, String businessId) throws Exception {
        String querySql = (String) objectMap.getOrDefault("query_sql", "");
        String queryDetailSql = (String) objectMap.getOrDefault("query_detail_sql", "");
        String queryDetailExSql = (String) objectMap.getOrDefault("query_detail_ex_sql", "");
        String keywordValue = (String) objectMap.get("keyword_value");
        Integer ruleId = (Integer) objectMap.get("rule_id");
        JSONObject jsonObject = (JSONObject) objectMap.get("sql_json");
        if (MyTools.isNotEmpty(querySql)) {
            String sql = String.format(querySql, keywordValue);
            if (jsonObject != null) {
                objectMap.put("sql_json", jsonObject.toJSONString());
            }
            objectMap.put("deal_status", 0);
            objectMap.put("dispose_status", 0);
            save(objectMap, businessId, sql, TABLE_ALERT_RULE_ORIGINAL);
        } else {
            log.error("未知的规则类型或未配置查询语句！主键:{},数据:{}", businessId, objectMap);
            return;
        }

        if (MyTools.isNotEmpty(queryDetailSql)) {
            String sql = String.format(queryDetailSql, keywordValue);
            objectMap.remove("keyword_value");
            objectMap.remove("notice_info");
            objectMap.remove("sql_json");
            objectMap.remove("deal_status");
            objectMap.remove("dispose_status");
            save(objectMap, businessId, sql, TABLE_ALERT_RULE_ORIGINAL_DETAIL);
        }

        if (MyTools.isNotEmpty(queryDetailExSql)) {
            String baseMac = jsonObject.getString("base_mac");
            String curMac = jsonObject.getString("cur_mac");
            String ipAddr = jsonObject.getString("ip_addr");
            String queryBaseSql = String.format(queryDetailExSql, baseMac);
            String queryCurSql = String.format(queryDetailExSql, curMac);
            List<Map<String, Object>> baseMapList = alertDao.query(queryBaseSql);
            List<Map<String, Object>> curMapList = alertDao.query(queryCurSql);
            save(businessId, baseMac, baseMapList, ipAddr, 0);
            save(businessId, curMac, curMapList, ipAddr, 1);
        }

        Map<String, Object> whereMap = new HashMap<>();
        Map<String, Object> valueMap = new HashMap<>();
        whereMap.put("business_id", businessId);
        valueMap.put("rule_id", ruleId);
        valueMap.put("business_id", businessId);
        valueMap.put("keyword_value", keywordValue);
        valueMap.put("update_time", MyTools.getNowTime());
        save(TABLE_ALERT_RULE_TRIGGER, valueMap, whereMap);
    }


    private void save(String businessId, String curMac, List<Map<String, Object>> mapList, String ipAddr, int seq) throws Exception {
        if (MyTools.isNotEmpty(mapList)) {
            Map<String, Object> whereMap = new HashMap<>();
            for (Map<String, Object> map : mapList) {
                whereMap.put("business_id", businessId);
                whereMap.put("username", curMac);
                map.put("business_id", businessId);
                map.put("seq", seq);
                map.put("ip_addr", ipAddr);
                save(TABLE_ALERT_IP_CLASH, map, whereMap);
                whereMap.clear();
            }
        }
    }


    private void save(Map<String, Object> objectMap, String businessId, String sql, String tableName) throws Exception {
        List<Map<String, Object>> mapList = alertDao.query(sql);
        if (MyTools.isNotEmpty(mapList)) {
            Map<String, Object> whereMap = new HashMap<>();
            for (Map<String, Object> map : mapList) {
                map.putAll(objectMap);
                map.remove("query_sql");
                map.remove("query_detail_sql");
                map.remove("query_detail_ex_sql");
                whereMap.put("business_id", businessId);
                save(tableName, map, whereMap);
                whereMap.clear();
            }
        }
    }


    private void save(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap) throws Exception {
        List<Map<String, Object>> mapList = alertDao.query(tableName, whereMap);
        if (MyTools.isEmpty(mapList)) {
            alertDao.insert(tableName, valueMap);
        } else {
            alertDao.updateByQuery(tableName, valueMap, whereMap);
        }
    }

}

