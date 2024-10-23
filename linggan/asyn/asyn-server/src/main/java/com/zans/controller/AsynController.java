package com.zans.controller;

import com.zans.service.IAsynDealService;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asyn")
@Slf4j
public class AsynController {

    @Autowired
    private IAsynDealService asynDealService;




    @GetMapping("/query/{taskId}")
    public ApiResult queryByTaskId(@PathVariable String taskId){
        return asynDealService.queryById(taskId);
    }


    @GetMapping("/query")
    public ApiResult query(){
        return asynDealService.query();
    }

    /**
    * @Author beixing
    * @Description  统计
    * @Date  2021/12/16
    * @Param
    * @return
    **/
    @GetMapping("/statistics")
    public ApiResult statistics(){
        return asynDealService.statistics();
    }

    /**
     * @Author beixing
     * @Description  统计今日
     * @Date  2021/12/16
     * @Param
     * @return
     **/
    @GetMapping("/statistics/nowDay")
    public ApiResult statisticsNowDay(){
        return asynDealService.statisticsNowDay();
    }


}
