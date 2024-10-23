package com.zans.wechat.message_push.controller;

import com.zans.wechat.message_push.vo.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

@RestController
public class TestController {

    /**
     * 用于测试是否部署成功
     * @return
     */
    @RequestMapping("/test")
    public ApiResult test(){
        return ApiResult.success("连接成功，当前时间为"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
