package com.zans.controller;

import com.zans.service.IAAAService;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
* @Title: AAAController
* @Description: aaa系列的控制层
* @Version:1.0.0
* @Since:jdk1.8
* @author beixing
* @Date  2022/3/19
**/
@RestController
@RequestMapping("/aaa")
@Slf4j(topic = "aaa")
public class AAAController {

    @Autowired
    private IAAAService aaaService;


    @RequestMapping(value = "/sync/qz", method = {RequestMethod.GET})
    public ApiResult syncQz(HttpServletRequest request){
        return aaaService.syncQzTask();
    }

    @RequestMapping(value = "/sync/acct", method = {RequestMethod.GET})
    public ApiResult syncAcct(HttpServletRequest request){
        return aaaService.syncAcctTask();
    }

    @RequestMapping(value = "/sync/endpoint", method = {RequestMethod.GET})
    public ApiResult syncEndpoint(HttpServletRequest request){
        return aaaService.syncEndpointTask();
    }

    @RequestMapping(value = "/sync/nas", method = {RequestMethod.GET})
    public ApiResult syncNasTask(HttpServletRequest request){
        return aaaService.syncNasTask();
    }





}
