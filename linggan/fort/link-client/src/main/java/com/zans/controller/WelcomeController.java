package com.zans.controller;


import com.alibaba.fastjson.JSONObject;
import com.zans.utils.CmdHelper;
import com.zans.utils.FileHelper;
import com.zans.utils.GetProperties;
import com.zans.vo.ApiResult;
import com.zans.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping
@Slf4j
public class WelcomeController {

    @Value("${spring.profiles.active}")
    String active;
    private final static String  VERSION_NAME = "version.txt";


    @RequestMapping(value = "/version", method = {RequestMethod.GET})
    public JSONObject version(HttpServletRequest request){
        Map<String, String> map = GetProperties.getAppSettings();
        JSONObject result = new JSONObject();
        result.put("git_branch", map.get("git_branch"));
        result.put("build_time", map.get("build_time"));
        result.put("git_commit", map.get("git_commit"));
        result.put("app_name", map.get("build_app"));
        result.put("profile", active);
        result.put("version", FileHelper.readResourcesFile(VERSION_NAME));

        return result;
    }

    /**
    * @Author beixing
    * @Description  用户登录之后创建映射关系
    * @Date  2021/8/16
    * @Param
    * @return
    **/
    @RequestMapping(value = "/createLink", method = {RequestMethod.POST})
    public ApiResult createLink(@RequestBody LoginVO loginVO){
        String driveName = loginVO.getDriveName();
        String linkName = loginVO.getLinkName();
        try {
           boolean flag = CmdHelper.createLink(linkName,driveName);
           if(!flag){
               return ApiResult.error("创建盘符映射失败!");
           }
        } catch (IOException | InterruptedException e) {
            log.error("创建失败,请求参数:{},",loginVO,e);
            return ApiResult.error("创建失败!");
        }
        return ApiResult.success();
    }

    /**
     * @Author beixing
     * @Description  用户退出之后删除映射关系
     * @Date  2021/8/16
     * @Param
     * @return
     **/
    @RequestMapping(value = "/delLink", method = {RequestMethod.POST})
    public ApiResult delLink(@RequestBody LoginVO loginVO){
        String linkName = loginVO.getLinkName();
        try {
            //清除
            boolean  flag = CmdHelper.delLink(linkName);
            if(!flag){
                return ApiResult.error("删除文件链接路径失败!");
            }
        } catch (IOException | InterruptedException e) {
            log.error("销毁失败！请求参数:{}",e);
            return ApiResult.error("销毁失败!");
        }
        return ApiResult.success();
    }

}
