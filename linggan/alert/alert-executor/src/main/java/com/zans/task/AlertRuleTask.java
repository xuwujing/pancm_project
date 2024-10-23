package com.zans.task;

import com.zans.commons.contants.Constants;
import com.zans.commons.utils.GetSpringBeanUtil;
import com.zans.commons.utils.HttpClientUtil;
import com.zans.commons.utils.MyTools;
import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertServerBean;
import com.zans.pojo.JobBean;
import com.zans.service.IRuleService;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.zans.contants.AlertConstants.*;

/**
 * @Author pancm
 * @Description 告警规则处理线程
 * @Date 2020/9/3
 * @Param
 * @return
 **/
@Slf4j
public class AlertRuleTask implements Runnable {


    private IRuleService iRuleService;

    private JobBean jobBean;

    public AlertRuleTask(JobBean jobBean) {
        this.jobBean = jobBean;
        iRuleService = GetSpringBeanUtil.getBean(IRuleService.class);
    }


    @Override
    public void run() {
        /*
         *  根据告警规则ID以及数据来源进行处理
         **/
        int job_id = jobBean.getJob_id();
        long task_id = jobBean.getTask_id();
        log.info("job_id:{},task_id:{},线程启动！", job_id, task_id);
        try {
            String data = jobBean.getJob_data();
            AlertRuleBean alertRuleBean = MyTools.toBean(data, AlertRuleBean.class);
            Long ruleId = alertRuleBean.getId();
            Boolean flag = alertRuleCacheStatus.get(ruleId);
            if (flag == null || !flag) {
                log.warn("job_id:{},task_id:{},策略ID:{}已被禁用或删除！不执行该规则！", job_id, task_id, ruleId);
                return;
            }
            alertRuleBean = alertRuleCache.get(ruleId);
            long datasource = alertRuleBean.getAlert_datasource();
            AlertServerBean alertServerBean = alertServerCache.get(datasource);
            iRuleService.deal(jobBean, alertServerBean, alertRuleBean);
            finish(task_id);
        } catch (Exception e) {
            log.error("job_id:{},task_id:{},线程任务处理失败！原因是:", job_id, task_id, e);
            return;
        }
        log.warn("job_id:{},task_id:{},任务执行完毕！", job_id, task_id);
    }

    /**
     * @return void
     * @Author pancm
     * @Description 任务处理完之后进行回调
     * @Date 2020/9/12
     * @Param [task_id]
     **/
    private void finish(long task_id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("node_id", NODE_ID);
        map.put("task_id", task_id);
        String url = getUrl(map);
        String data = HttpClientUtil.post(url);
        ApiResult apiResult = MyTools.toBean(data, ApiResult.class);
        if (!(0 == apiResult.getCode())) {
            log.error("task_id:{},任务回调失败!失败原因:{}!失败数据:{}!", task_id, apiResult.getMessage(), MyTools.toString(apiResult));
        }
    }

    private String getUrl(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        //构建请求参数
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> e : map.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
        }
        return getUrl() + "?" + sb.toString();
    }

    private String getUrl() {
        String url = HTTP + REMOTE_IP + Constants.TIME_SEPARATOR + REMOTE_PORT + Constants.SLASH_SIGN + REMOTE_CONTEXT + FIN_URI;
        return url;
    }

}
