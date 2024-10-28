package com.zans.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description : 点对点消息  //描述
 */
@Slf4j
@ServerEndpoint("/client/{userId}")
@Component
public class SendMessage {

    /** 记录当前在线连接数 */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static volatile ConcurrentHashMap<String, SendMessage> socket = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (socket.containsKey(userId)) {
            socket.remove(userId);
            socket.put(userId, this);
        } else {
            socket.put(userId, this);
            onlineCount.incrementAndGet(); // 在线数加1
        }
        try {
            sendMessage("连接成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("用户：{}加入连接，当前在线人数为：{}", userId, onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        if (socket.containsKey(userId)) {
            socket.remove(userId);
            // 在线数减1
            onlineCount.decrementAndGet();
        }
        log.info("用户：{}退出，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if (message != null && !message.isEmpty()) {
            JSONObject object = JSON.parseObject(message);
            object.put("userId", userId);
            String toUserId = object.getString("toUserId");
            if (message != null && !message.isEmpty() && socket.containsKey(toUserId)) {
                socket.get(toUserId).sendMessage(object.toJSONString());
            } else {
                log.error("请求的用户:{}不在该服务器上", toUserId);
            }
        }

        log.info("服务端收到客户端[{}]的消息:{}", userId, message);
    }

    @OnError
    public void onError(Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}
