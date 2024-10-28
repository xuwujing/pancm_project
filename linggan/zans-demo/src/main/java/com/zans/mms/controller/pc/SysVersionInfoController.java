package com.zans.mms.controller.pc;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zans.base.annotion.Record;
import com.zans.base.vo.ApiResult;
import com.zans.mms.service.ISysVersionInfoService;

import com.zans.mms.util.SSHConnectionTest;
import com.zans.mms.vo.SysServerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.zans.base.constant.SystemConstant.*;
import static com.zans.mms.util.SshTest.collet;
import static com.zans.mms.util.SshTest.execShell;

@Api(value = "/appVerify", tags = {"应用程序自检"})
@RestController
@RequestMapping("/appVerify")
@Slf4j
public class SysVersionInfoController {

    @Autowired
    ISysVersionInfoService sysVersionInfoService;


    @ApiOperation(value = "/list", notes = "检查结果")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ApiResult list() {
        return ApiResult.success(sysVersionInfoService.getVerifyResult());
    }

    @ApiOperation(value = "/reVerify", notes = "重新检查")
    @RequestMapping(value = "/reVerify", method = {RequestMethod.GET})
    public ApiResult reVerify() {
        sysVersionInfoService.reVerify();
        return ApiResult.success();
    }

    ///2021-08-09 暂不用 停止单个或多个服务 后续确认再删除
//    @ApiOperation(value = "/stopAndStartService", notes = "启动停止服务")
//    @RequestMapping(value = "/stopAndStartService", method = {RequestMethod.POST})
//    public ApiResult stopAndStartService(@RequestBody List<SysServerVO> serverVOList) {
//        sysVersionInfoService.stopAndStartService(serverVOList);
//        return ApiResult.success();
//    }

    @ApiOperation(value = "/stopAndStartAllService", notes = "停止All服务")
    @RequestMapping(value = "/stopAndStartAllService", method = {RequestMethod.GET})
    public ApiResult stopAndStartAllService() {
        log.info("begin -> stop service");
        sysVersionInfoService.stopAndStartAllService();
        log.info("end -> stop service");
        return ApiResult.success();
    }

    /**
     * 停止2.20堡垒机服务
     *
     * @return
     */
    @RequestMapping(value = "stopOrStartGuacamole", method = RequestMethod.GET)
    public ApiResult stopOrStartGuacamole(@RequestParam("status") String status) {
        return sysVersionInfoService.stopAndStartGuacamole(status);
    }

    @RequestMapping(value = "trigToGuacamole",method = RequestMethod.GET)
    public ApiResult trigToGuacamole(){
        return sysVersionInfoService.trigToGuacamole();
    }
}
