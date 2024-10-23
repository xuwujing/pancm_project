package com.zans.controller;

import com.zans.service.IAsynDealService;
import com.zans.vo.ApiResult;
import com.zans.vo.AsynRecordReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/asyn")
@Slf4j
public class AsynController {

    @Autowired
    private IAsynDealService asynDealService;


    @PostMapping("/receive")
    public ApiResult receive(@RequestBody AsynRecordReqVO asynRecordReqVO, HttpServletRequest request){
        return asynDealService.receive(asynRecordReqVO);
    }





}
