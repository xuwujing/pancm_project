package com.zans.portal.controller;

import com.zans.base.vo.ApiResult;
import com.zans.portal.service.ISysVersionInfoService;
import com.zans.portal.util.HttpHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author beixing
 * @Title: 系统服务控制层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 14:25:24
 */
@RestController
@RequestMapping("/system/version/")
public class SysVersionInfoController {
    /**
     * 服务对象
     */
    @Autowired
    private ISysVersionInfoService sysVersionInfoService;

    @Autowired
    private HttpHelper httpHelper;



    /**
     * 分页查询
     */
    @ApiOperation(value = "查询", notes = "查询")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult list() {
        return sysVersionInfoService.list();
    }

    /**
     * 详情查询
     */
    @ApiOperation(value = "详情", notes = "详情")
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ApiResult view(@RequestParam("id") Long id) {
        return ApiResult.success(sysVersionInfoService.queryById(id));
    }
}
