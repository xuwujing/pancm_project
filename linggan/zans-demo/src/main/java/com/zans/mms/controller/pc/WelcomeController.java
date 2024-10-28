package com.zans.mms.controller.pc;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.guard.AlertRuleMapper;
import com.zans.mms.service.IDbVersionService;
import com.zans.mms.util.GetProperties;
import com.zans.mms.vo.DbVersionVO;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(value = "/hello", tags = {"/hello ~ 欢迎页，测试用户登陆状态"})
@RestController
@RequestMapping("/")
@Slf4j
public class WelcomeController {

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    private IDbVersionService dbVersionService;

    @Value("${spring.profiles.active}")
    String active;
    private final static String VERSION_NAME = "version.txt";

    private static long count = 1;


    @ApiOperation(value = "/hello", notes = "无参数，返回 hello world | 当前时间 | 用户登陆信息")
    @RequestMapping(value = "/hello", method = {RequestMethod.GET})
    public ApiResult sayHello(HttpServletRequest request) {
        String now = DateHelper.getNow();
        UserSession userSession = httpHelper.getUser(request);
        Map<String, Object> result = MapBuilder.getBuilder().put("hello", "world")
                .put("now", DateHelper.getNow())
                .put("user", userSession)
                .build();
        return ApiResult.success(result);
    }


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
        DbVersionVO dbVersionVO = dbVersionService.queryNewOne();
        if (dbVersionVO != null) {
            result.put("db_version", dbVersionVO.getVersion());
            result.put("db_remark", dbVersionVO.getRemark());
            result.put("db_name", "guard_scan_demo");
        }
        return result;
    }


    @ApiOperation(value = "/getAlert", notes = "告警信息")
    @RequestMapping(value = "/getAlert", method = {RequestMethod.GET})
    public ApiResult getAlert(HttpServletRequest request) {
        String msg = null;
        AlertRecordRespVO alertRecordRespVO = alertRuleMapper.getAlertLastRecord();
        if (alertRecordRespVO != null) {
            msg = alertRecordRespVO.getNoticeInfo();
            alertRuleMapper.updateIsRead(alertRecordRespVO.getId());
        }
        return ApiResult.success(msg);
    }


    @Resource
    private AlertRuleMapper alertRuleMapper;
}
