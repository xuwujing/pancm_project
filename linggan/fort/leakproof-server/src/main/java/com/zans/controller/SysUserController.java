package com.zans.controller;

import com.zans.model.UserSession;
import com.zans.service.ISysUserService;
import com.zans.utils.ApiResult;
import com.zans.utils.HttpHelper;
import com.zans.vo.NetDiskFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/18
 */
@RestController
@RequestMapping("user")
@Slf4j
public class SysUserController extends BaseController {

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    protected HttpHelper httpHelper;

    @RequestMapping(value = "myNetDisk", method = RequestMethod.POST)
    public ApiResult myNetDisk(@RequestBody NetDiskFileVO netDiskFileVO, HttpServletRequest request) {
        return sysUserService.myNetDisk(netDiskFileVO,request);
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) {
        sysUserService.download(request,response);
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }

}
