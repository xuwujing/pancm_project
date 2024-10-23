package com.zans.controller;

import com.zans.service.IReceiveService;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: job任务控制层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/1
 */
@RestController
@RequestMapping
@Slf4j
public class JobController {

    @Autowired
    IReceiveService iReceiveService;

    /**
     * @Author pancm
     * @Description 存活接口确认
     * @Date  2020/9/1
     * @Param []
     * @return com.zans.vo.ApiResult
     **/
    @PostMapping("/node/alive")
    public ApiResult alive(){
        return ApiResult.success();
    }

    @GetMapping("/node/alive")
    public ApiResult alive2(){
        return ApiResult.success();
    }
    /**
     * @Author pancm
     * @Description 任务调度接收接口
     * @Date  2020/9/1
     * @Param [task_id]
     * @return com.zans.vo.ApiResult
     **/
    @PostMapping("/task/receive")
    public ApiResult receive(@RequestParam(value = "task_id") Integer task_id ){
        log.info("task id:{}",task_id);
        ApiResult apiResult = iReceiveService.addTask(task_id);
        return apiResult;
    }

    @PostMapping("/task/finish")
    public ApiResult finish(){
        return ApiResult.success();
    }

}
