package com.zans.task;

import com.zans.commons.utils.MyTools;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.pojo.AlertRuleBean;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.zans.commons.contants.Constants.DEFAULT_TIME;
import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警策略管理线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/1
 */
@Slf4j
public class ManageTask extends Thread {


    private AlertDao dbDao;

    private long count;

    private String lastUpdateTime;

    public ManageTask() {
        dbDao = new AlertDaoImpl();
    }


    public void run() {
        /*
         * 定时查询告警规则记录表，并根据告警规则的更新时间来进行数据判断处理
         **/
        while (true) {
            try {
                List<Map<String, Object>> mapList = dbDao.queryAlertRule(lastUpdateTime);
                if (MyTools.isEmpty(mapList)) {
                    TimeUnit.SECONDS.sleep(SLEEP_TIME);
                    count++;
                    if(count%10==0){
                        log.info("策略表暂无数据更新，进行休眠！");
                    }
                    continue;
                }
                handle(mapList);
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            } catch (Exception e) {
                log.error("加载线程运行失败！原因是:", e);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e1) {
                    log.error("线程暂停失败！", e1);
                }
                continue;
            }
        }
    }


    /**
     * @Author pancm
     * @Description 根据告警规则数据处理
     * @Date  2020/9/1
     * @Param [mapList]
     * @return void
     **/
    private void handle(List<Map<String, Object>> mapList)  {
        /*
         *  根据告警最后的更新时间进行判断，如果表的时间比当前时间大，
         *  说明有数据变动，则根据变动的信息进行处理
         **/
        String maxTime = null;
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> objectMap = mapList.get(i);
            String updateTime = objectMap.get("update_time").toString();
            AlertRuleBean alertRuleBean = MyTools.toBean(MyTools.toString(objectMap), AlertRuleBean.class);
            long id = alertRuleBean.getId();
            if (lastUpdateTime == null || lastUpdateTime.compareTo(updateTime) < 0) {
                if (maxTime == null || maxTime.compareTo(updateTime) < 0) {
                    maxTime = updateTime;
                }
                if (alertRuleCache.get(id) != null) {
                    if (!alertRuleBean.getRule_status().equals(alertRuleCache.get(id).getRule_status())) {
                        log.warn("策略ID:{}的状态发生改变，原有状态:{},目前状态:{},进行添加或移除!",id,alertRuleCache.get(id).getRule_status(),alertRuleBean.getRule_status());
                        //如果最新的状态是启用的话,则进行启动线程并添加缓存，否则停止线程并除去缓存
                        if (alertRuleBean.getRule_status() == 1) {
                            alertRuleCacheStatus.put(id, true);
                            alertRuleCache.put(id, alertRuleBean);
                        } else {
                            alertRuleCacheStatus.put(id, false);
                            alertRuleCache.put(id, alertRuleBean);
                        }
                        continue;
                    }
                    log.warn("策略ID:{}的数据变动!",id);
                    alertRuleCache.put(id, alertRuleBean);
                    alertRuleCacheTime.put(id, LocalDateTime.parse(DEFAULT_TIME));
                    continue;
                }
                alertRuleCacheStatus.put(id, true);
                alertRuleCache.put(id, alertRuleBean);
                alertRuleCacheTime.put(id, LocalDateTime.parse(DEFAULT_TIME));
                alertRuleCachePeriod.put(id,new AtomicInteger(0));
            }
        }
        lastUpdateTime=maxTime;
    }


}
