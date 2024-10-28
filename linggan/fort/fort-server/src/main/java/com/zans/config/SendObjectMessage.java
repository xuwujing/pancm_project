package com.zans.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description : 群发消息  //描述
 */
@Slf4j
@ServerEndpoint("/result")
@Component
public class SendObjectMessage {

    /** 记录当前在线连接数 */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static CopyOnWriteArraySet<SendObjectMessage> socket = new CopyOnWriteArraySet<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        socket.add(this);
        onlineCount.incrementAndGet(); // 在线数加1
        try {
            sendMessage("连接成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("有新连接加入，当前在线人数为：{}", onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        socket.remove(this);
        // 在线数减1
        onlineCount.decrementAndGet();
        log.info("用户退出，当前在线人数为：{}", onlineCount.get());
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
            for (SendObjectMessage sendObjectMessage : socket) {
                sendObjectMessage.sendMessage(message);
            }
        }
        log.info("服务端收到客户端的消息:{}", message);
    }

    @OnError
    public void onError(Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message) throws IOException {
        //如果开启@Async异步需要加锁，否则就会报错
        synchronized (session){
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 自定义群发
     */
    public static void sendInfo(String message){
        if (CollectionUtils.isEmpty(socket)){
            return;
        }
        if (message != null && message.isEmpty()) {
            return;
        }

        for (SendObjectMessage sendObjectMessage : socket) {
            try {
                sendObjectMessage.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
