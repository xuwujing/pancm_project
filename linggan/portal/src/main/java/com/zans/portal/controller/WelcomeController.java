package com.zans.portal.controller;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.TraceDataDao;
import com.zans.portal.model.TraceData;
import com.zans.portal.service.IDbVersionService;
import com.zans.portal.util.GetProperties;
import com.zans.portal.util.HttpHelper;
import com.zans.portal.vo.DbVersionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.debugMap;

@Api(value = "/hello", tags = {"/hello ~ 欢迎页，测试用户登录状态"})
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
    private final static String  VERSION_NAME = "version.txt";

    @ApiOperation(value = "/hello", notes = "无参数，返回 hello world | 当前时间 | 用户登录信息")
    @RequestMapping(value = "/hello", method = {RequestMethod.GET})
    public ApiResult sayHello(HttpServletRequest request){
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
    public JSONObject version(HttpServletRequest request){
        Map<String, String> map = GetProperties.getAppSettings();
        JSONObject result = new JSONObject();
        result.put("git_branch", map.get("git_branch"));
        result.put("build_time", map.get("build_time"));
        result.put("git_commit", map.get("git_commit"));
        result.put("app_name", map.get("build_app"));
        result.put("profile", active);
        result.put("version", FileHelper.readResourcesFile(VERSION_NAME));
        DbVersionVO  dbVersionVO =  dbVersionService.queryNewOne();
        if(dbVersionVO!=null){
            result.put("db_version", dbVersionVO.getVersion());
            result.put("db_remark", dbVersionVO.getRemark());
            result.put("db_name", "guard_scan");
        }
        return result;
    }

    @RequestMapping(value = "/constant/debug", method = {RequestMethod.GET})
    public ApiResult debug(HttpServletRequest request){
        return ApiResult.success(GlobalConstants.DEBUG_SCHEMA);
    }


    @RequestMapping(value = "/constant/debug/{enable}", method = {RequestMethod.GET})
    public ApiResult debugEnable(@PathVariable Integer enable){
        GlobalConstants.DEBUG_SCHEMA = enable;
        if(enable == 1){
            debugMap.put(enable, LocalDateTime.now().minusMinutes(30).toString());
        }
        return ApiResult.success(GlobalConstants.DEBUG_SCHEMA);
    }

    @RequestMapping(value = "/constant/debug/list", method = {RequestMethod.GET})
    public ApiResult debugList(){
        List<TraceData> traceData =traceDataDao.queryByTraceId(null);
        return ApiResult.success(traceData);
    }

    @RequestMapping(value = "/constant/debug/list/{traceId}", method = {RequestMethod.GET})
    public ApiResult debugList(@PathVariable String traceId){
        List<TraceData> traceData =traceDataDao.queryByTraceId(traceId);
        return ApiResult.success(traceData);
    }



    @Resource
    private TraceDataDao traceDataDao;

}
