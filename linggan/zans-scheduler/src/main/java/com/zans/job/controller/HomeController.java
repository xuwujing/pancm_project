package com.zans.job.controller;

import com.zans.base.vo.ApiResult;
import com.zans.job.service.*;
import com.zans.job.vo.common.CircleUnit;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/***
 * web端首页报表接口
 */
@Slf4j
@RestController
@RequestMapping(value = "home")
public class HomeController {

    @Autowired
    IJobService jobService;

    @ApiOperation(value = "/jobCount", notes = "任务数")
    @RequestMapping(value = "/jobCount", method = {RequestMethod.GET})
    public ApiResult jobCount(HttpServletRequest request) {
        List<CircleUnit> result = jobService.getJobCount();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/jobPieChart", notes = "任务饼图")
    @RequestMapping(value = "/jobPieChart", method = {RequestMethod.GET})
    public ApiResult jobPieChart(HttpServletRequest request) {
        List<CircleUnit> result = jobService.getJobPieChart();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/executes", notes = "任务执行次数图表")
    @RequestMapping(value = "/executes", method = {RequestMethod.GET})
    public ApiResult executes(HttpServletRequest request) {
        List<CircleUnit> result = jobService.executes();
        return ApiResult.success(result);
    }


}
