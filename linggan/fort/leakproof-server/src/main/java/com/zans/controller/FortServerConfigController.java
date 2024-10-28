package com.zans.controller;

import com.zans.config.RedisUtil;
import com.zans.dao.SysUserMapper;
import com.zans.model.SysUser;
import com.zans.model.UserSession;
import com.zans.utils.ApiResult;
import com.zans.utils.HttpHelper;
import com.zans.vo.WebSocketSessionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.zans.constant.SystemConstant.ipInUserMap;
import static com.zans.constant.SystemConstant.webSocketSession;


/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/25
 */
@RestController
@RequestMapping
@Slf4j
public class FortServerConfigController {

    @Resource
    SysUserMapper sysUserMapper;

    @Autowired
    protected HttpHelper httpHelper;

    @Autowired
    RedisUtil redisUtil;

    @GetMapping("/webSocket/close/{ip}")
    public ApiResult close(@PathVariable String ip, HttpServletRequest request) {
        log.info("调用");
        ipInUserMap.put(ip, false);
        WebSocketSessionVO socketSessionVO = webSocketSession.get(ip);
        if (socketSessionVO == null) {
            log.info(String.format("该ip:%s 已退出！", ip));
            return ApiResult.success();
        }
        socketSessionVO.setExpireTime(System.currentTimeMillis());
        webSocketSession.put(ip, socketSessionVO);
        log.info("ip:{}结束远程,heartbeat", ip);
        redisUtil.del(ip + "-" + getUserSession(request).getUserName());
        return ApiResult.success();
    }

    @GetMapping("/getUser")
    public ApiResult getUser(@RequestParam("ip") String ip) {
        SysUser currentUser = sysUserMapper.findCurrentUser(ip);
        return ApiResult.success(currentUser == null ? new SysUser() : currentUser);
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }
}
