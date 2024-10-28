package com.zans.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zans.utils.CmdHelper;
import com.zans.utils.FileHelper;
import com.zans.utils.GetProperties;
import com.zans.utils.HttpClientUtil;
import com.zans.vo.ApiResult;
import com.zans.vo.EdrVO;
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
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping
@Slf4j
public class WelcomeController {

    @Value("${spring.profiles.active}")
    private String active;
    @Value("${request.link}")
    private String linkUrl;

    private String createLink = "/api/createLink";

    private final static String VERSION_NAME = "version.txt";

    private static Map<String , LoginVO> loginVOMap = new ConcurrentHashMap<>();

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

    /**
     * @return
     * @Author beixing
     * @Description 用户登录之后创建映射关系
     * @Date 2021/8/16
     * @Param
     **/
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ApiResult login(@RequestBody LoginVO loginVO) {
        log.info("登录请求参数:{}",loginVO);
        String user = loginVO.getUser();
        String ip = loginVO.getIp();
        String url = loginVO.getUrl();
        String driveName = loginVO.getDriveName();
        String linkName = loginVO.getLinkName();
        if(loginVOMap.get(user)!=null){
            return ApiResult.error(5000,"用户已登录");
        }
        loginVOMap.put(user,loginVO);
        try {
            boolean flag = CmdHelper.testMount(ip);
            if (!flag) {
                return ApiResult.error("连接文件存储服务器失败!");
            }
            //要用非管理员创建
            flag = CmdHelper.createMount(url, driveName);
            if (!flag) {
                return ApiResult.error("创建服务映射失败!");
            }
            String reqUrl = linkUrl + createLink;
            try {
                String result = HttpClientUtil.post(reqUrl,loginVO.toString());
                ApiResult apiResult = JSON.parseObject(result, ApiResult.class);
                if(apiResult.getCode()!=0){
                    log.warn("请求接口失败！接口返回参数:{}",apiResult);
                    return apiResult;
                }
            } catch (Exception e) {
                log.error("创建盘符映射失败!请求地址:{},请求参数:{}",reqUrl,loginVO,e);
                return ApiResult.error("创建盘符映射失败!");
            }

            //必须要用管理员创建
//            flag = CmdHelper.createLink(linkName, driveName);
//            if (!flag) {
//                return ApiResult.error("创建盘符映射失败!");
//            }
        } catch (IOException | InterruptedException e) {
            log.error("创建失败,请求参数:{},", loginVO, e);
            return ApiResult.error("创建失败!");
        }
        return ApiResult.success();
    }

    /**
     * @return
     * @Author beixing
     * @Description 用户退出之后删除映射关系
     * @Date 2021/8/16
     * @Param
     **/
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    public ApiResult logout(@RequestBody LoginVO loginVO) {
        log.info("登出请求参数:{}",loginVO);
        String user = loginVO.getUser();
        String linkName = loginVO.getLinkName();
        String drive = loginVO.getDriveName();

        try {
            loginVOMap.remove(user);
            //清除
            boolean flag = CmdHelper.delMount(drive);
            if (!flag) {
                return ApiResult.error("连接文件存储服务器失败!");
            }
            flag = CmdHelper.delLink(linkName);
            if (!flag) {
                return ApiResult.error("删除文件链接路径失败!");
            }
        } catch (IOException | InterruptedException e) {
            log.error("销毁失败！请求参数:{}", loginVO,e);
            return ApiResult.error("销毁失败!");
        }
        return ApiResult.success();
    }



    /**
     * @Author beixing
     * @Description  用户登录之后关闭水印服务
     * @Date  2021/8/16
     * @Param
     * @return
     **/
    @RequestMapping(value = "/stopEdr", method = {RequestMethod.POST})
    public ApiResult stopEdr(@RequestBody EdrVO edrVO){
        log.info("停止服务请求参数:{}",edrVO);
        String cmd = "stop";
        try {
            CmdHelper.run(edrVO.getPath(),edrVO.getName(),cmd);
        } catch (IOException | InterruptedException e) {
            log.error("执行失败,请求参数:{},",edrVO,e);
            return ApiResult.error("执行失败!");
        }
        return ApiResult.success();
    }

    /**
     * @Author beixing
     * @Description  用户登录之后启动服务
     * @Date  2021/8/16
     * @Param
     * @return
     **/
    @RequestMapping(value = "/startEdr", method = {RequestMethod.POST})
    public ApiResult startEdr(@RequestBody EdrVO edrVO){
        log.info("启动服务请求参数:{}",edrVO);
        String cmd = "start";
        try {
            CmdHelper.run(edrVO.getPath(),edrVO.getName(),cmd);
        } catch (IOException | InterruptedException e) {
            log.error("执行失败,请求参数:{},",edrVO,e);
            return ApiResult.error("执行失败!");
        }
        return ApiResult.success();
    }
}
