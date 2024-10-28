package com.zans.service;


import com.zans.vo.ApiResult;
import com.zans.vo.DiscernRequestVO;

/**
 * @author beixing
 * @Title: 服务接口
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 17:54:32
 */
public interface IDiscernService {


    ApiResult discern(DiscernRequestVO discernRequestVO);


    /**
    * @Author beixing
    * @Description  报表分析
    * @Date  2021/7/28
    * @Param
    * @return
    **/
    ApiResult analyzeReport(String businessId);

    /**
    * @Author beixing
    * @Description  分析
    * @Date  2021/7/28
    * @Param
    * @return
    **/
    ApiResult analyze(String businessId);

    /**
    * @Author beixing
    * @Description  数据重置
    * @Date  2021/7/28
    * @Param
    * @return
    **/
    ApiResult reset();


    ApiResult matchType();



}
