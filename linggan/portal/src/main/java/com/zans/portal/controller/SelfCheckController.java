package com.zans.portal.controller;

import com.zans.base.vo.ApiResult;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.SysConstantMapper;
import com.zans.portal.model.SysConstant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "/selfCheck", tags = { "/selfCheck ~ 程序自检" })
@RestController
@RequestMapping("/selfCheck")
@Validated
@Slf4j
public class SelfCheckController  extends BasePortalController{
    
    @Autowired
    SysConstantMapper sysConstantMapper;



    @ApiOperation(value = "/version", notes = "获得当前版本号")
    @RequestMapping(value = "/version", method = {RequestMethod.GET})
    public ApiResult version() {
        log.info("获取portal当前发布版本号");
        return ApiResult.success(GlobalConstants.VERSION);
    }

    @ApiOperation(value = "/checkScanDB", notes = "检测连接guard_scan库是否正确")
    @RequestMapping(value = "/checkScanDB", method = {RequestMethod.GET})
    public ApiResult checkScanDB(){
        try {
            SysConstant cons = sysConstantMapper.findConstantByKey("license");
            if (cons != null){
                return ApiResult.success();
            }else{
                return ApiResult.error("数据库连接异常");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.error(e.getMessage());
        }
    }


}
