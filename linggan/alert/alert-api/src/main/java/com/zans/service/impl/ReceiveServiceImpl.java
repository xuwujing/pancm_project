package com.zans.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.commons.contants.Constants;
import com.zans.commons.utils.CronUtil;
import com.zans.commons.utils.HttpClientUtil;
import com.zans.commons.utils.MyTools;
import com.zans.dao.AlertDao;
import com.zans.dao.JobDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.dao.impl.JobDaoImpl;
import com.zans.pojo.AlertRecordBean;
import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertRuleOriginal;
import com.zans.pojo.AlertRuleOriginalDetail;
import com.zans.service.IReceiveService;
import com.zans.vo.AlertRuleStrategyReqVO;
import com.zans.vo.ApiResult;
import com.zans.vo.JobReqVO;
import com.zans.vo.ScanJobDataVO;
import com.zans.vo.node.AlertRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private AlertDao alertDao = new AlertDaoImpl();

    private JobDao jobDao = new JobDaoImpl();


    @Override
    public ApiResult addRecord(AlertRecordVO alertRecordVO) {
        Long strategyId = alertRecordVO.getStrategyId();
        List<Map<String, Object>> mapList = alertRecordVO.getMapList();
        AlertRuleBean alertRuleBean = alertRuleCache.get(strategyId);
        if (alertRuleBean == null) {
            return ApiResult.error("接口调用失败!告警规则ID不存在！");
        }
        try {
            deal(strategyId, mapList, alertRuleBean);
            log.info("策略ID:{},接口处理成功!", strategyId);
        } catch (Exception e) {
            log.error("数据存储失败！原因是:", e);
            return ApiResult.error("接口调用失败!请求参数不符！");
        }

        return ApiResult.success();
    }

    @Override
    public ApiResult recoverRecord(AlertRecordVO alertRecordVO) {
        Long strategyId = alertRecordVO.getStrategyId();
        AlertRuleBean alertRuleBean = alertRuleCache.get(strategyId);
        if (alertRuleBean == null) {
            return ApiResult.error("接口调用失败!告警规则ID不存在！");
        }
        try {
            recover(alertRecordVO, strategyId, alertRuleBean);
        } catch (Exception e) {
            log.error("告警记录恢复失败！请求参数:{},原因:{}", alertRecordVO, e);
            return ApiResult.success();
        }
        return ApiResult.success();
    }

    @Override
    @Transactional
    public ApiResult strategySave(AlertRuleStrategyReqVO reqVO) {

        /*
         *  1.根据data_source判断调用job服务的请求参数( 为1 job_type=scan，job_data = json（strategyId，groupSql(SELECT_SQL_ASSET_GROUP),alert_threshold）)
         *  2. scan调用job接口需要填写job_module，用alert_rule的module，sharing默认1，sharing_num为2，update只更新corn;
         * */
        //先判断此规则是否需要生成job任务
        try {
            Map<String, Object> whereValue = new HashMap<>();
            whereValue.put("id", reqVO.getRule_id());
            List<Map<String, Object>> mapList = alertDao.query(TABLE_ALERT_RULE, whereValue);
            Integer dataSource = null;
            String module = null;
            if (MyTools.isNotEmpty(mapList)) {
                dataSource = (Integer) mapList.get(0).get("data_source");
                module = (String) mapList.get(0).get("module");
            }
            if (null == dataSource) {
                log.error("查询ruleId对应的dataSource失败！ruleId:{}", reqVO.getRule_id());
                return ApiResult.error("查询ruleId对应的dataSource失败！");
            }
            reqVO.setJobReqVO(null);
            Map<String, Object> saveMap = MyTools.toMap(reqVO.toString());
            //修改的时候  reqVO的id不为空
            if (null != reqVO.getId()) {
                Map<String, Object> whereMap = new HashMap<>();
                whereMap.put("id", reqVO.getId());
                saveMap.remove("id");
                alertDao.updateByQuery(TABLE_ALERT_RULE_STRATEGY, saveMap, whereMap);
                //通过job_name查询job的id
                List<Map<String, Object>> maps = jobDao.queryJobId(reqVO.getGroup_name());
                if (MyTools.isEmpty(maps)) {
                    log.error("根据jobName查询对应的jobId失败！");
                    return ApiResult.error("根据jobName查询对应的jobId失败！");
                }
                Integer job_id = (Integer) maps.get(0).get("job_id");
                //cronExpression秒转换为corn表达式
                String cronExpression = CronUtil.createLoopCronExpression(0, reqVO.getAlert_interval());
                Map<String, Object> jobUpdateMap = new HashMap<>();
                jobUpdateMap.put("cron_expression", cronExpression);
                Map<String, Object> jobWhereMap = new HashMap<>();
                jobWhereMap.put("job_id", job_id);
                jobDao.updateByQuery(TABLE_OPS_JOB, jobUpdateMap, jobWhereMap);
                log.info("修改ops_job成功!修改参数:{},修改条件:{}",jobUpdateMap, jobWhereMap);
                return ApiResult.success();
            }
            saveJob(reqVO, dataSource, module, saveMap);
        } catch (Exception e) {
            log.error("判断此规则是否需要生成job任务失败！原因是{}", e);
            return ApiResult.error("判断此规则是否需要生成job任务失败！");
        }

        return ApiResult.success();
    }

    private void saveJob(AlertRuleStrategyReqVO reqVO, Integer dataSource, String module, Map<String, Object> saveMap) throws Exception {
        alertDao.insert(TABLE_ALERT_RULE_STRATEGY, saveMap);
        Map<String, Object> whereMap = new HashMap<>();
        whereMap.put("rule_id", reqVO.getRule_id());
        whereMap.put("group_id", reqVO.getGroup_id());
        Map<String, Object> map = alertDao.queryOne(TABLE_ALERT_RULE_STRATEGY, whereMap);
        //新增的时候
        JobReqVO jobReqVO = new JobReqVO();
        jobReqVO.setJob_name(reqVO.getGroup_name());
        jobReqVO.setRemark(reqVO.getGroup_name());
        //设置jobData对象
        Long id = (Long) map.get("id");
        ScanJobDataVO scanJobDataVO = new ScanJobDataVO();
        String cronExpression = CronUtil.createLoopCronExpression(0, reqVO.getAlert_interval());
        jobReqVO.setCronExpression(cronExpression);
        jobReqVO.setSharding(0);
        jobReqVO.setTimeout(60);
        //如果datasource为1  scan  设置不同的默认值
        if (1 == dataSource) {
            scanJobDataVO.setStrategyId(id);
            scanJobDataVO.setAlertThreshold(reqVO.getAlert_threshold());
            scanJobDataVO.setGroupSql(String.format(SELECT_SQL_ASSET_GROUP, reqVO.getGroup_id()));
            jobReqVO.setJob_data(scanJobDataVO.toString());
            jobReqVO.setJob_type("scan");
            jobReqVO.setPriority(1);
            jobReqVO.setJob_module(module);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jobReqVO.setJob_data(jsonObject.toJSONString());
            jobReqVO.setJob_type("alert");
            jobReqVO.setPriority(11);
        }
        log.info("请求job服务的地址:{},请求参数:{}!", JOB_URl + JOB_URI_SAVE, jobReqVO);
        HttpClientUtil.post(JOB_URl + JOB_URI_SAVE, jobReqVO.toString());
    }

    @Override
    public ApiResult changeStatus(AlertRuleStrategyReqVO reqVO) {
        // 启用/禁用调用job的enable接口

        try {
            //先修改策略表的数据
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("strategy_status", reqVO.getRule_status());
            Map<String, Object> whereMap = new HashMap<>();
            whereMap.put("id", reqVO.getId());
            alertDao.updateByQuery(TABLE_ALERT_RULE_STRATEGY, updateMap, whereMap);

            //修改job表的状态
            //通过job_name查询job的id
            List<Map<String, Object>> maps = jobDao.queryJobId(reqVO.getGroup_name());
            if (MyTools.isEmpty(maps)) {
                log.error("根据jobName查询对应的jobId失败！");
                return ApiResult.error("根据jobName查询对应的jobId失败！");
            }
            Integer job_id = (Integer) maps.get(0).get("job_id");
            Integer enable = 3;
            if (reqVO.getRule_status() == 0) {
                enable = 2;
            }
            JobReqVO jobReqVO = new JobReqVO();
            jobReqVO.setJob_id(job_id);
            jobReqVO.setJob_status(reqVO.getRule_status().toString());
            log.info("请求job服务的地址:{},请求参数JobId:{},enable:{}!", JOB_URl + JOB_URI_ENABLE, job_id, enable);
            log.info(JOB_URl + JOB_URI_ENABLE + "?jobId=" + job_id + "&enable=" + enable);
            HttpClientUtil.post(JOB_URl + JOB_URI_ENABLE + "?jobId=" + job_id + "&enable=" + enable);

        } catch (Exception e) {
            log.error("状态修改失败！{}", e);
            return ApiResult.error("状态修改失败!");
        }

        return ApiResult.success();
    }

    private void recover(AlertRecordVO alertRecordVO, Long ruleId, AlertRuleBean alertRuleBean) throws Exception {
        List<Map<String, Object>> mapList = alertRecordVO.getMapList();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(mapList));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String business = jsonObject.getString(alertRuleBean.getAlert_keyword());
            alertRecover(ruleId, business);
        }
    }

    /**
     * 告警数据处理
     *
     * @param strategyId
     * @param mapList
     * @param alertRuleBean
     * @throws Exception
     */
    private void deal(Long strategyId, List<Map<String, Object>> mapList, AlertRuleBean alertRuleBean) throws Exception {
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(mapList));
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> originalMap = mapList.get(i);
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String business = jsonObject.getString(alertRuleBean.getAlert_keyword());
            Long ruleId = alertRuleBean.getRule_id();
            String businessId = getBusinessId(ruleId, alertRuleBean.getGroup_id(), business);
            String noticeInfo = "";
            // 改规则只告警，不做其他操作
            if (alertRuleBean.getRule_type() != null && alertRuleBean.getRule_type() == 10) {
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> whereMap = new HashMap<>();
                map.put("business_id", businessId);
                map.put("keyword_value", business);
                map.put("strategy_id", strategyId);
                map.put("rule_id", ruleId);
                noticeInfo = getInfo(jsonObject, alertRuleBean);
                map.put("notice_info", noticeInfo);
                map.put("update_time", MyTools.getNowTime());
                map.put("deal_status", 0);
                map.put("dispose_status", 0);
                //2021-10-12 新增逻辑，风险告警等级取记录的
                map.put("alert_level", originalMap.getOrDefault("alert_level", alertRuleBean.getAlert_level()));
                whereMap.put("business_id", businessId);
                map.putAll(originalMap);
                AlertRuleOriginal alertRuleOriginal = MyTools.toBean(JSONObject.toJSONString(map), AlertRuleOriginal.class);
                Map<String, Object> map1 = MyTools.toMap(alertRuleOriginal.toString());
                save(TABLE_ALERT_RULE_ORIGINAL, map1, whereMap);
                save(TABLE_ALERT_RULE_ORIGINAL_HIS, map1, whereMap);
                whereMap.clear();
                map.clear();
                map.put("business_id", businessId);
                map.put("strategy_id", strategyId);
                whereMap.put("business_id", businessId);
                map.putAll(originalMap);
                AlertRuleOriginalDetail alertRuleOriginalDetail = MyTools.toBean(JSONObject.toJSONString(map), AlertRuleOriginalDetail.class);
                Map<String, Object> map2 = MyTools.toMap(alertRuleOriginalDetail.toString());
                save(TABLE_ALERT_RULE_ORIGINAL_DETAIL, map2, whereMap);
                save(TABLE_ALERT_RULE_ORIGINAL_DETAIL_HIS, map2, whereMap);
                whereMap.clear();
                saveTrigger(strategyId, businessId, business);
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
                    map.put("strategy_id", strategyId);
                    map.put("rule_id", ruleId);
                    noticeInfo = getInfo(jsonObject, alertRuleBean);
                    map.put("notice_info", noticeInfo);
                    map.put("update_time", MyTools.getNowTime());
                    map.put("deal_status", 0);
                    map.put("dispose_status", 0);
                    map.put("notice_local", 1);
                    //2021-10-12 新增逻辑，风险告警等级取记录的
                    map.put("alert_level", originalMap.getOrDefault("alert_level", alertRuleBean.getAlert_level()));
                    whereMap.put("business_id", businessId);
                    map.putAll(originalMap);
                    AlertRuleOriginal alertRuleOriginal = MyTools.toBean(JSONObject.toJSONString(map), AlertRuleOriginal.class);
                    Map<String, Object> map1 = MyTools.toMap(alertRuleOriginal.toString());
                    save(TABLE_ALERT_RULE_ORIGINAL, map1, whereMap);
                    whereMap.clear();
                    saveTrigger(strategyId, businessId, business);
                }
            } else {
                log.warn("列表查询无数据！使用接口传输数据！");
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> whereMap = new HashMap<>();
                map.put("business_id", businessId);
                map.put("keyword_value", business);
                map.put("strategy_id", strategyId);
                map.put("rule_id", ruleId);
                noticeInfo = getInfo(jsonObject, alertRuleBean);
                map.put("notice_info", noticeInfo);
                map.put("update_time", MyTools.getNowTime());
                map.put("deal_status", 0);
                map.put("dispose_status", 0);
                //2021-10-12 新增逻辑，风险告警等级取记录的
                map.put("alert_level", originalMap.getOrDefault("alert_level", alertRuleBean.getAlert_level()));
                whereMap.put("business_id", businessId);
                map.putAll(originalMap);
                AlertRuleOriginal alertRuleOriginal = MyTools.toBean(JSONObject.toJSONString(map), AlertRuleOriginal.class);
                Map<String, Object> map1 = MyTools.toMap(alertRuleOriginal.toString());
                save(TABLE_ALERT_RULE_ORIGINAL, map1, whereMap);
                save(TABLE_ALERT_RULE_ORIGINAL_HIS, map1, whereMap);
                whereMap.clear();
                map.clear();
            }

            if (MyTools.isNotEmpty(resultList2)) {
                Map<String, Object> whereMap = new HashMap<>();
                for (Map<String, Object> map : resultList2) {
                    map.put("business_id", businessId);
                    whereMap.put("business_id", businessId);
                    map.putAll(originalMap);
                    AlertRuleOriginalDetail alertRuleOriginalDetail = MyTools.toBean(JSONObject.toJSONString(map), AlertRuleOriginalDetail.class);
                    Map<String, Object> map2 = MyTools.toMap(alertRuleOriginalDetail.toString());
                    save(TABLE_ALERT_RULE_ORIGINAL_DETAIL, map2, whereMap);
                    save(TABLE_ALERT_RULE_ORIGINAL_DETAIL_HIS, map2, whereMap);
                    whereMap.clear();

                }
            } else {
                log.warn("详情查询无数据！使用接口传输数据！");
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> whereMap = new HashMap<>();
                map.put("business_id", businessId);
                map.put("strategy_id", strategyId);
                whereMap.put("business_id", businessId);
                map.putAll(originalMap);
                AlertRuleOriginalDetail alertRuleOriginalDetail = MyTools.toBean(JSONObject.toJSONString(map), AlertRuleOriginalDetail.class);
                Map<String, Object> map2 = MyTools.toMap(alertRuleOriginalDetail.toString());
                save(TABLE_ALERT_RULE_ORIGINAL_DETAIL, map2, whereMap);
                save(TABLE_ALERT_RULE_ORIGINAL_DETAIL_HIS, map2, whereMap);
                whereMap.clear();
                saveTrigger(strategyId, businessId, business);
            }
        }
    }

    private void saveTrigger(Long ruleId, String businessId, String keywordValue) throws Exception {
        Map<String, Object> whereMap = new HashMap<>();
        Map<String, Object> valueMap = new HashMap<>();
        whereMap.put("business_id", businessId);
        valueMap.put("strategy_id", ruleId);
        valueMap.put("business_id", businessId);
        valueMap.put("keyword_value", keywordValue);
        valueMap.put("update_time", MyTools.getNowTime());
        save(TABLE_ALERT_RULE_TRIGGER, valueMap, whereMap);
    }


    private void alertRecover(Long ruleId, String business) throws Exception {
        String updateRuleOriginalSql = String.format(UPDATE_SQL_RULE_ORIGINAL, ruleId, business);
        String delTriggerSql = String.format(DELETE_SQL_TRIGGER, ruleId, business);
        alertDao.executeUpdate(updateRuleOriginalSql);
        alertDao.executeUpdate(delTriggerSql);
        log.info("策略ID:{},keyword:{},已进行数据恢复!", ruleId, business);
        return;
    }


    private void save(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap) throws Exception {
        Map<String, Object> map = alertDao.queryOne(tableName, whereMap);
        if (MyTools.isEmpty(map)) {
            alertDao.insert(tableName, valueMap);
            log.info("新增成功!tableName:{},新增参数:{}", tableName, valueMap);
            return;
        }
        Object obj = map.get("notice_time");
        if (obj != null) {
            String time = obj.toString();
            final boolean isFlag = MyTools.isCompareDay(MyTools.getNowTime(), time, Constants.TIME_FORMAT2);
            if (isFlag) {
                valueMap.put("notice_local", 0);
            }
        }
        alertDao.updateByQuery(tableName, valueMap, whereMap);
        log.info("修改成功!tableName:{},修改参数:{}", tableName, valueMap);

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
    private String getBusinessId(long ruleId, long groupId, String business) {
        return ruleId + Constants.LINE_SIGN + groupId + Constants.LINE_SIGN + business;
    }


}
