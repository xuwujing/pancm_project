package com.zans.controller;

import com.alibaba.fastjson.JSONObject;
import com.zans.config.MapDbProperties;
import com.zans.config.MapFileProperties;
import com.zans.util.FileHelper;
import com.zans.util.GetProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController  {


    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private MapDbProperties mapDbProperties;

    @Autowired
    private MapFileProperties mapFileProperties;


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

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public JSONObject test(HttpServletRequest request){
        JSONObject result = new JSONObject();
        result.put("mapDb", mapDbProperties.getInfo());
        result.put("mapFile", mapFileProperties.getInfo());
        return result;
    }

    /**
    * @Author beixing
    * @Description  数据恢复接口
    * @Date  2021/12/27
    * @Param
    * @return
    **/
    @RequestMapping(value = "/restore", method = {RequestMethod.GET})
    public JSONObject restore(HttpServletRequest request){
        JSONObject result = new JSONObject();
        result.put("mapDb", mapDbProperties.getInfo());
        result.put("mapFile", mapFileProperties.getInfo());
        return result;
    }

}
