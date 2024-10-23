package com.zans.controller;

import com.alibaba.fastjson.JSONObject;
import com.zans.util.FileHelper;
import com.zans.util.GetProperties;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@Slf4j
public class HomeController  {


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    @Qualifier("redisAcctTemplate")
    private RedisTemplate acctTemplate;

    @Resource
    @Qualifier("redisAliveTemplate")
    private RedisTemplate aliveTemplate;

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
        return result;
    }

    @ApiOperation(value = "/test", notes = "测试")
    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public String test(HttpServletRequest request){
        String key = "1";
        String value = "hello";
        redisTemplate.opsForValue().set(key, value);
        System.out.println("==========="+redisTemplate.opsForValue().get(key));
        acctTemplate.opsForValue().set(key, value);
        System.out.println("============"+acctTemplate.opsForValue().get(key));
        aliveTemplate.opsForValue().set(key, value);
        System.out.println("============"+aliveTemplate.opsForValue().get(key));
        key = "t#";
        value = "d#";
        acctTemplate.opsForHash().increment(key,value,1L);
        System.out.println("++ "+acctTemplate.opsForHash().get(key,value));
        acctTemplate.opsForHash().increment(key,value,1L);
        System.out.println("++ "+acctTemplate.opsForHash().get(key,value));
        String name ="test";
        Map<String, Object> map = new HashMap<>();
        map.put("mac","12");
        map.put("ip","192.168");
        acctTemplate.opsForHash().putAll(name,map);
        System.out.println("++ "+acctTemplate.opsForHash().entries(name));
        return value;
    }




}
