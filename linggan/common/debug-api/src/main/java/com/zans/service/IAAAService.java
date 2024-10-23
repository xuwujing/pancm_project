package com.zans.service;

import com.zans.vo.ApiResult;

/**
 * @author beixing
 * @Title: debug-api
 * @Description: aaa系列的实现类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/19
 */
public interface IAAAService {

     /**
     * @Author beixing
     * @Description  radius的qz数据同步到guard的endpoint表
     * @Date  2022/3/22
     * @Param
     * @return
     **/
     ApiResult syncQzTask();

     /**
      * @Author beixing
      * @Description  radius的acct数据同步到redis的acct表
      * @Date  2022/3/22
      * @Param
      * @return
      **/
     ApiResult syncAcctTask();


     /**
     * @Author beixing
     * @Description  同步endpoint表的数据
     * @Date  2022/3/23
     * @Param
     * @return
     **/
     ApiResult syncEndpointTask();

     /**
     * @Author beixing
     * @Description  nas同步
     * @Date  2022/3/23
     * @Param
     * @return
     **/
     ApiResult syncNasTask();

}
