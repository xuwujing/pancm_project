package com.zans.service;

import com.alibaba.fastjson.JSONArray;
import com.zans.pojo.AlertRuleBean;
import com.zans.pojo.AlertServerBean;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: Prometheus 实现业务代码
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */
public interface IPrometheusDealService {

    /**
     * @Author pancm
     * @Description Prometheus 告警规则数据实现
     * @Date  2020/9/3
     * @Param [alertServerBean, bean]
     * @return com.alibaba.fastjson.JSONArray
     **/
    JSONArray analyzePrometheus(AlertServerBean alertServerBean, AlertRuleBean bean);
}
