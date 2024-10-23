package com.zans.task;

import com.zans.commons.utils.MyTools;
import com.zans.contants.AlertConstants;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.pojo.AlertRuleBean;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: alert-executor
 * @Description: 告警记录恢复管理线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/22
 */
@Slf4j
public class AlertRecoverManageTask extends Thread{

    private long count;

    private AlertDao alertDao = new AlertDaoImpl();


    public void run() {
        while (true) {
            try {
                count++;
                if(count%600==0){
                    log.info("告警记录恢复处理线程休眠！");
                }
                TimeUnit.SECONDS.sleep(AlertConstants.SLEEP_TIME_RECOVER);
                List<Map<String,Object>> mapList = alertDao.query(SELECT_SQL_TRIGGER_GROUP);
                if(MyTools.isNotEmpty(mapList)){
                    for (int i = 0; i < mapList.size(); i++) {
                        Map<String,Object> map = mapList.get(i);
                        if(map.get("rule_id") == null){
                            continue;
                        }
                        Long ruleId = (Long) map.get("rule_id");
                        AlertRuleBean alertRuleBean = alertRuleCache.get(ruleId);
                        Integer autoRecover = alertRuleBean.getAuto_recover();
                        if( autoRecover !=null && autoRecover == 1) {
                            reTpx.execute(new AlertRecoverTask(ruleId));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("告警记录恢复处理任务失败！",e);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e1) {
                    log.error("告警记录恢复处理暂停失败！", e1);
                }
                continue;
            }
        }
    }
}
