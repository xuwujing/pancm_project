package com.zans.controller;

import com.alibaba.fastjson.JSONObject;
import com.zans.strategy.ISqlTransformStrategy;
import com.zans.util.FileHelper;
import com.zans.util.GetProperties;
import com.zans.vo.ApiResult;
import com.zans.vo.CustomReqVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {


    @Value("${spring.profiles.active}")
    String active;

    private final static String VERSION_NAME = "version.txt";

    @ApiOperation(value = "/version", notes = "版本信息")
    @RequestMapping(value = "/version", method = {RequestMethod.GET})
    public JSONObject version(HttpServletRequest request) {
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


    @ApiOperation(value = "/list", notes = "用户查询")
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody CustomReqVO reqVO, HttpServletRequest request) {
        String module = "t_user_list";
        reqVO.setModule(module);
        ApiResult apiResult = sqlTransformStrategy.query(reqVO);
        return apiResult;
    }

    @ApiOperation(value = "/init", notes = "初始化")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    public ApiResult init(HttpServletRequest request) {
        String module = "t_user_list";
        ApiResult apiResult = sqlTransformStrategy.init(module);
        return apiResult;
    }


    @Autowired
    ISqlTransformStrategy sqlTransformStrategy;


}
