package com.zans.mms.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author beixing
 * @Title: zans-demo
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/6/29
 */
@Slf4j
public class WsSessionCacheManager {
    /**
     * 保存连接 session 的地方
     */
    private static ConcurrentHashMap<String, Session> SESSION_POOL = new ConcurrentHashMap<>();
    public static final String SESSION_KEY = "ws";

    /**
     * 添加 session
     *
     * @param key
     */
    public static void add(String key, Session session) {
        // 添加 session
        SESSION_POOL.put(key, session);
    }

    /**
     * 删除 session,会返回删除的 session
     *
     * @param key
     * @return
     */
    public static Session remove(String key) {
        // 删除 session
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并同步关闭连接
     *
     * @param key
     */
    public static void removeAndClose(String key) {
       Session session = remove(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close();
            } catch (IOException e) {
                // todo: 关闭出现异常处理
                log.error("session关闭异常!",e);
            }
        }
    }

    /**
     * 获得 session
     *
     * @param key
     * @return
     */
    public static Session get(String key) {
        // 获得 session
        return SESSION_POOL.get(key);
    }

    /**
     * 获得 session
     *
     * @param
     * @return
     */
    public static boolean sendMsg(String key,String msg) {
        Session session =  SESSION_POOL.get(key);
        if(session==null ||!session.isOpen()){
            log.error("================发送消息发现session失效!==========================");
            return false;
        }
        try {
            log.info("ws发送消息:{}",msg);
            session.getBasicRemote().sendText(msg);
            return true;
        } catch (IOException e) {
            log.error("ws发送消息失败!",e);
        }

        return false;
    }

}
