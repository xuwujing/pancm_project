package com.zans.service;

import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertServerBean;
import com.zans.pojo.JobBean;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警规则执行业务层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
public interface IRuleService {
     /**
      *
      * @param jobBean
      * @param alertServerBean
      * @param bean
      */
     void deal(JobBean jobBean, AlertServerBean alertServerBean, AlertRuleBean bean);

     void recover(AlertServerBean alertServerBean, long ruleId);
}
