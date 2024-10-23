package com.zans.service.impl;

import com.zans.commons.config.GetProperties;
import com.zans.commons.utils.MyTools;
import com.zans.commons.utils.YmlUtil;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.service.IAlertAggService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.contants.AlertConstants.TABLE_ALERT_RULE_RECORD;
import static com.zans.contants.AlertConstants.TABLE_RADIUS_ENDPOINT;

/**
 * @author pancm
 * @Title: alert-executor
 * @Description: 告警记录聚合线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/19
 */
@Service
@Slf4j
public class AlertAggServiceImpl implements IAlertAggService {

    private AlertDao alertDao = new AlertDaoImpl();

    private long count;

    private String lastUpdateTime = GetProperties.getAppSettings().getOrDefault(AGG_TIME_NAME,"");

    @Override
    public void aggRecord() {
        /*
         * 1.通过聚合语句查询记录的原始表得到聚合的数据;
         * 2.将聚合数据更新到记录表中
         *
         **/
        try {
            List<Map<String,Object>> mapList = alertDao.agg(lastUpdateTime);
            if(count%60==0){
                log.info("告警记录聚合线程休眠!");
            }
            count++;
            if(MyTools.isEmpty(mapList)){
                return;
            }
            Map<String, Object> whereMap = new HashMap<>();
            Map<String, Object> valueMap = new HashMap<>();
            for (Map<String, Object> map : mapList) {
                String keywordValue = (String) map.get("keyword_value");
                if (MyTools.isEmpty(keywordValue)) {
                    continue;
                }
                whereMap.put("keyword_value",keywordValue);
                valueMap.put("business_id",map.get("business_id"));
                valueMap.put("rule_id",map.get("rule_id"));
                valueMap.put("update_time",MyTools.getNowTime());
                valueMap.put("keyword_value",keywordValue);
                map.remove("alert_level");

                save(TABLE_ALERT_RULE_RECORD,valueMap,whereMap);
                valueMap.clear();
                whereMap.clear();
            }
            Object updateTime = mapList.get(0).get("update_time");
            if(updateTime == null){
                return;
            }
            lastUpdateTime = String.valueOf(updateTime);
            updateProperties(lastUpdateTime);
            log.info("告警记录聚合线程已处理了:{}条数据!",mapList.size());
        } catch (Exception e) {
            log.error("告警记录聚合线程处理失败!原因是:",e);
        }
    }

    private void updateProperties(String lastUpdateTime) throws IOException {
//        SetSystemProperty ssp = new SetSystemProperty();
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(AGG_TIME_NAME ,lastUpdateTime);
//        ssp.updateProperties(APP_FILE_NAME, map, null);
        YmlUtil.saveOrUpdateByKey(AGG_TIME_NAME,lastUpdateTime);
    }


    private void updateEndPoint(Map<String, Object> whereMap, Map<String, Object> valueMap, Map<String, Object> map, String keywordValue, int level, long ruleId) throws Exception {
        whereMap.put("pass",keywordValue);
        valueMap.put("risk_rank",level);
        valueMap.put("notice_info",map.get("notice_info"));
        valueMap.put("rule_id",ruleId);
        alertDao.updateByQuery(TABLE_RADIUS_ENDPOINT,valueMap,whereMap);
    }

    private void save(String tableName,Map<String, Object> valueMap,Map<String, Object> whereMap) throws Exception {
        List<Map<String,Object>> mapList = alertDao.query(tableName,whereMap);
        if(MyTools.isEmpty(mapList)){
            alertDao.insert(tableName,valueMap);
        }else {
            alertDao.updateByQuery(tableName,valueMap,whereMap);
        }
    }

    public static final String AGG_TIME_NAME = "agg.uptime";
}
