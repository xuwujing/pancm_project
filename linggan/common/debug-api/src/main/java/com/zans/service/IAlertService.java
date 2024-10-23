package com.zans.service;

import com.zans.vo.ApiResult;

import java.util.Map;

/**
 * @author beixing
 * @Title: debug-api
 * @Description: 告警
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/22
 */
public interface IAlertService {

    /**
    * @Author beixing
    * @Description  新设备入网
    * @Date  2022/3/22
    * @Param
    * @return
    **/
    ApiResult sendAlertNewEndpoint(int ruleRecover, Map<String,Object> map);


    /**
    * @Author beixing
    * @Description  基线变更
    * @Date  2022/3/22
    * @Param
    * @return
    **/
    ApiResult sendAlertBaselineChange(int ruleRecover,Map<String,Object> map);

    /**
    * @Author beixing
    * @Description  ip冲突
    * @Date  2022/3/22
    * @Param
    * @return
    **/
    ApiResult sendAlertIpClash(int ruleRecover,Map<String,Object> map);

}
