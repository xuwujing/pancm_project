package com.zans.strategy;

import com.zans.vo.ApiResult;
import com.zans.vo.CustomReqVO;

/**
 * @author beixing
 * @Title: custom-server
 * @Description: sql转换策略
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/4/19
 */
public interface ISqlTransformStrategy {

    String getSql(QueryReqVO queryReqVO);


    /**
    * @Author beixing
    * @Description  通用初始化
    * @Date  2022/5/7
    * @Param
    * @return
    **/
    ApiResult init(String module);



    ApiResult query(CustomReqVO reqVO);



}
