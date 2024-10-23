package com.zans.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.commons.contants.Constants;
import com.zans.commons.utils.MyTools;
import com.zans.contants.AlertConstants;
import com.zans.dao.AlertDao;
import com.zans.dao.JobDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.dao.impl.JobDaoImpl;
import com.zans.pojo.*;
import com.zans.service.IReceiveService;
import com.zans.task.AlertRuleTask;
import com.zans.vo.ApiResult;
import com.zans.vo.node.AlertRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 任务添加处理业务类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/1
 */
@Service
@Slf4j
public class ReceiveServiceImpl implements IReceiveService {

    private JobDao jobDao = new JobDaoImpl();
    private AlertDao alertDao = new AlertDaoImpl();
    private String message = "";

    @Override
    public ApiResult addTask(int taskId) {
        try {
            while (!AlertConstants.IS_INIT) {
                TimeUnit.SECONDS.sleep(1);
            }
            List<Map<String, Object>> mapList = jobDao.queryJob(taskId);
            if (MyTools.isEmpty(mapList)) {
                message = "taskId任务不存在!taskId:" + taskId;
                log.warn(message);
                return ApiResult.error(message);
            }
            JobBean jobBean = MyTools.toBean(MyTools.toString(mapList.get(0)), JobBean.class);
            if (jobBean.getEnable() == 0) {
                message = "任务已禁用!taskId:" + taskId;
                log.warn(message);
                return ApiResult.error(message);
            }
            tpx.execute(new AlertRuleTask(jobBean));
        } catch (Exception e) {
            log.error("添加task任务失败！taskId:{}", taskId, e);
            return ApiResult.error("添加task任务失败！");
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult record(AlertRecordVO alertRecordVO) {
        Long ruleId = alertRecordVO.getRuleId();
        List<Map<String, Object>> mapList = alertRecordVO.getMapList();
        AlertRuleBean alertRuleBean = alertRuleCache.get(ruleId);
        if (alertRuleBean == null) {
            return ApiResult.error("接口调用失败!告警规则ID不存在！");
        }
        int status = 0;
        if(alertRecordVO.getStatus() != null){
            status = alertRecordVO.getStatus();
        }
        try {
            deal(ruleId, mapList, alertRuleBean, status,alertRecordVO.getTemplateEnable());
            log.info("策略ID:{},接口处理成功!", ruleId);
        } catch (Exception e) {
            log.error("数据存储失败！原因是:", e);
            return ApiResult.error("接口调用失败!请求参数不符！");
        }

        return ApiResult.success();
    }

    private void deal(Long ruleId, List<Map<String, Object>> mapList, AlertRuleBean alertRuleBean, int status, Integer templateEnable) throws Exception {
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(mapList));
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> originalMap= mapList.get(i);
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String business = jsonObject.getString(alertRuleBean.getAlert_keyword());
            if(status == 1){
                String updateRuleOriginalSql = String.format(UPDATE_SQL_RULE_ORIGINAL, ruleId, business);
                alertDao.executeUpdate(updateRuleOriginalSql);
                log.info("策略ID:{},keyword:{},已进行数据恢复!",ruleId,business);
                continue;
            }
            String businessId = getBusinessId(ruleId, business);
            String noticeInfo = "";
            // 改规则只告警，不做其他操作
            if(alertRuleBean.getRule_type()!=null && alertRuleBean.getRule_type() == 10){
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> whereMap = new HashMap<>();
                map.put("business_id", businessId);
                map.put("keyword_value", business);
                map.put("rule_id", ruleId);
                noticeInfo = getInfo(jsonObject, alertRuleBean);
                map.put("notice_info", noticeInfo);
                map.put("record_source", 1);
                map.put("update_time",MyTools.getNowTime());
                map.put("deal_status",0);
                map.put("dispose_status",0);
                whereMap.put("business_id", businessId);
                map.putAll(originalMap);
                AlertRuleOriginal alertRuleOriginal = MyTools.toBean(JSONObject.toJSONString(map),AlertRuleOriginal.class);
                Map<String, Object> map1 = MyTools.toMap(alertRuleOriginal.toString());
                save(TABLE_ALERT_RULE_ORIGINAL, map1, whereMap);
                whereMap.clear();
                map.clear();
                map.put("business_id", businessId);
                map.put("rule_id", ruleId);
                whereMap.put("business_id", businessId);
                map.putAll(originalMap);
                AlertRuleOriginalDetail alertRuleOriginalDetail = MyTools.toBean(JSONObject.toJSONString(map),AlertRuleOriginalDetail.class);
                Map<String, Object> map2 = MyTools.toMap(alertRuleOriginalDetail.toString());
                save(TABLE_ALERT_RULE_ORIGINAL_DETAIL, map2, whereMap);
                whereMap.clear();
                return;
            }


            String querySql = alertRuleBean.getQuery_sql();
            String queryDetailSql = alertRuleBean.getQuery_detail_sql();
            String sql = String.format(querySql, business);
            String sqlDetail = String.format(queryDetailSql, business);
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
                    if(templateEnable ==null || templateEnable == 0){
                        jsonObject.put("point_name", obj);
                    }
                    noticeInfo = getInfo(jsonObject, alertRuleBean);
                    map.put("notice_info", noticeInfo);
                    map.put("record_source", 1);
                    map.put("update_time",MyTools.getNowTime());
                    map.put("deal_status",0);
                    map.put("dispose_status",0);
                    whereMap.put("business_id", businessId);
                    map.putAll(originalMap);
                    AlertRuleOriginal alertRuleOriginal = MyTools.toBean(JSONObject.toJSONString(map),AlertRuleOriginal.class);
                    Map<String, Object> map1 = MyTools.toMap(alertRuleOriginal.toString());
                    save(TABLE_ALERT_RULE_ORIGINAL, map1, whereMap);
                    whereMap.clear();
                }
            }else {
                log.warn("列表查询无数据！");
            }

            if (MyTools.isNotEmpty(resultList2)) {
                Map<String, Object> whereMap = new HashMap<>();
                for (Map<String, Object> map : resultList2) {
                    map.put("business_id", businessId);
                    map.put("rule_id", ruleId);
                    whereMap.put("business_id", businessId);
                    map.putAll(originalMap);
                    AlertRuleOriginalDetail alertRuleOriginalDetail = MyTools.toBean(JSONObject.toJSONString(map),AlertRuleOriginalDetail.class);
                    Map<String, Object> map2 = MyTools.toMap(alertRuleOriginalDetail.toString());
                    save(TABLE_ALERT_RULE_ORIGINAL_DETAIL, map2, whereMap);
                    whereMap.clear();
                }
            }else {
                log.warn("详情查询无数据！");
            }
        }
    }


    private void save(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap) throws Exception {
        List<Map<String, Object>> mapList = alertDao.query(tableName, whereMap);
        if (MyTools.isEmpty(mapList)) {
            alertDao.insert(tableName, valueMap);
            log.info("新增成功!tableName:{},新增参数:{}",tableName,valueMap);
        } else {
            alertDao.updateByQuery(tableName, valueMap, whereMap);
            log.info("修改成功!tableName:{},修改参数:{}",tableName,valueMap);
        }
    }

    /**
     * @return com.zans.pojo.AlertRecordBean
     * @Author pancm
     * @Description 得到告警内容信息
     * @Date 2020/9/3
     * @Param [alertRuleBean, jsonObject, businessId]
     **/
    private Map<String, Object> getData(AlertRuleBean alertRuleBean, JSONObject jsonObject, String businessId) {
        AlertRecordBean bean = new AlertRecordBean();
        String keywordValue = jsonObject.getString(alertRuleBean.getAlert_keyword());
        bean.setKeyword_value(keywordValue);
        bean.setRule_id(alertRuleBean.getId());
        bean.setBusiness_id(businessId);
        bean.setNotice_info(getInfo(jsonObject, alertRuleBean));
        bean.setQuery_sql(alertRuleBean.getQuery_sql());
        bean.setQuery_detail_sql(alertRuleBean.getQuery_detail_sql());
        bean.setRecord_source(1);
        return MyTools.toMap(bean.toString());
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
