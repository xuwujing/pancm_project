package com.zans.controller;


import com.zans.service.IDiscernService;
import com.zans.utils.MyTools;
import com.zans.vo.ApiResult;
import com.zans.vo.DiscernRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author beixing
 * @Title: DiscernController
 * @Description: 分析识别控制层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/7/27
 **/
@RestController
@RequestMapping
@Slf4j
public class DiscernController {

    @Autowired
    private IDiscernService discernService;

    /**
     * @return
     * @Author beixing
     * @Description 根据ip分析数据
     * @Date 2021/7/27
     * @Param
     **/
    @RequestMapping(value = "/discern", method = {RequestMethod.POST})
    public ApiResult discern(@RequestBody String data) {
        log.info("req:{}", data);
        DiscernRequestVO discernRequestVO = MyTools.toBean(data, DiscernRequestVO.class);
        return discernService.discern(discernRequestVO);
    }

    /**
     * @return
     * @Author beixing
     * @Description 分析报表数据
     * @Date 2021/7/27
     * @Param
     **/
    @RequestMapping(value = "/analyzeReport", method = {RequestMethod.GET})
    public ApiResult analyzeReport(@RequestParam(value = "businessId",defaultValue = "") String businessId) {
        return discernService.analyzeReport(businessId);
    }


    /**
     * @return
     * @Author beixing
     * @Description 分析数据
     * @Date 2021/7/27
     * @Param
     **/
    @RequestMapping(value = "/analyze", method = {RequestMethod.GET})
    public ApiResult analyze(@RequestParam(value = "businessId",defaultValue = "") String businessId) {
        return discernService.analyze(businessId);
    }


    /**
     * @return
     * @Author beixing
     * @Description 重置分析的数据
     * @Date 2021/7/27
     * @Param
     **/
    @RequestMapping(value = "/reset", method = {RequestMethod.GET})
    public ApiResult reset() {
        return discernService.reset();
    }

    /**
     * @return
     * @Author beixing
     * @Description 根据型号来匹配规则得到类型
     * @Date 2021/7/27
     * @Param
     **/
    @RequestMapping(value = "/model/matchType", method = {RequestMethod.GET})
    public ApiResult matchType() {
        return discernService.matchType();
    }


}
