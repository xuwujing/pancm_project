package com.zans.job.controller;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.FileHelper;
import com.zans.job.service.IDbVersionService;
import com.zans.job.utils.GetProperties;
import com.zans.job.vo.DbVersionVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/")
@Slf4j
public class WelcomeController {



    @Autowired
    private IDbVersionService dbVersionService;

    @Value("${spring.profiles.active}")
    String active;
    private final static String  VERSION_NAME = "version.txt";



    @ApiOperation(value = "/version", notes = "版本信息")
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
        DbVersionVO dbVersionVO =  dbVersionService.queryNewOne();
        if(dbVersionVO!=null){
            result.put("db_version", dbVersionVO.getVersion());
            result.put("db_remark", dbVersionVO.getRemark());
            result.put("db_name", "guard_job");
        }
        return result;
    }
}
