package com.zans.config;

import com.alibaba.fastjson.JSONObject;
import com.zans.schedule.MyRunnable;
import com.zans.utils.ApiResult;
import com.zans.vo.FortReserveVO;
import com.zans.vo.LastTimeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import static com.zans.constant.SystemConstant.CONNECT_PORT;
import static com.zans.constant.SystemConstant.ipInUserMap;
import static com.zans.service.impl.FortReserveServiceImpl.formatTime;
import static com.zans.service.impl.FortReserveServiceImpl.isConnect;


/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/30
 */

/**
 * 获取远程剩余时间
 */
@ServerEndpoint("/imserver/{ip}")
@Component
@Slf4j
public class WebSocketRemoteTime {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketRemoteTime> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private static Session session;
    /**
     * 接收ip
     */
    private static String ip = "";
    /**
     * 主机是否能够ping通
     */
    private static boolean pingSuccess = true;

    public static ScheduledFuture<?> future;

    public static ScheduledFuture<?> pingFuture;

    private RedisUtil redisUtil = (RedisUtil) SpringBeanFactory.getBean("redisUtil");

    private ThreadPoolTaskScheduler threadPoolTaskScheduler = (ThreadPoolTaskScheduler) SpringBeanFactory.getBean("taskScheduler");

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("ip") String ip) {
        this.session = session;
        this.ip = ip;
        if (webSocketMap.containsKey(ip)) {
            webSocketMap.remove(ip);
        }
        webSocketMap.put(ip, this);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            log.error("sleepError:{}",e);
//        }
        log.info("sengMsg DynamicTask.startCron()-ip:{}", ip);
        beginTask();//开启任务
        log.info("ping DynamicTask.startCron()-ip:{}", ip);
        beginPing();//开始ping 一旦ping的结果完全丢失 退出远程桌面
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(ip)) {
            webSocketMap.remove(ip);
            //从set中删除
            subOnlineCount();
        }


        //停止推送消息的定时任务
        if (future != null) {
            future.cancel(true);
        }
        log.info("sendMsg DynamicTask.stopCron()");
        //停止ping的定时任务
        if (pingFuture != null) {
            pingFuture.cancel(true);
        }
        log.info("ping DynamicTask.stopCron()");

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的ip
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        long expiredTime = redisUtil.getExpiredTime(ip);
        log.info("on-message-expired-time:{}", expiredTime);
        String msg = getMsg(expiredTime);
        try {
            sendMessage(msg);
        } catch (IOException e) {
            log.error("ip:" + ip + ",网络异常!!!!!!");
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.ip + ",原因:" + error);
        ipInUserMap.put(ip, false);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 每三秒检测一次
     */
    private void beginTask() {
        future = threadPoolTaskScheduler.schedule(new MyRunnable(), new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {//此处可以改为配置文件获取定时任务执行频率
                long expiredTime = redisUtil.getExpiredTime(ip);
//                log.info("beginTask-expired-time:{}", expiredTime);
                try {
                    sendMessage(getMsg(expiredTime));
                } catch (IOException e) {
                    log.error("推送信息失败!error:{}", e);
                }
                return new CronTrigger("*/1 * * * * ?").nextExecutionTime(triggerContext);
            }
        });
    }

    public void beginPing() {
        pingFuture = threadPoolTaskScheduler.schedule(new MyRunnable(), new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                if (!isConnect(ip, CONNECT_PORT)) {
                    pingSuccess = false;
                }
//                log.info("end-socket-pingSuccess->{}", pingSuccess);
                return new CronTrigger("*/3 * * * * ?").nextExecutionTime(triggerContext);//每三秒判断一次是否在线
            }
        });
    }

    public String getMsg(long expiredTime) {
        FortReserveVO fortReserveVO = new FortReserveVO();
//        log.info("getMsg | pingSuccess->{}", pingSuccess);
        try {
            if (expiredTime == -2) {//已过期
                log.info("预约过期");
                expiredTime = 0;
            }
            LastTimeVO lastTimeVO = formatTime(expiredTime);
            fortReserveVO.setLastTimeVO(lastTimeVO);
            fortReserveVO.setTakingTime(new SimpleDateFormat("HH:mm:ss").format(new Date(0, 0, 0, lastTimeVO.getHour(), lastTimeVO.getMinutes(), lastTimeVO.getSeconds())));
            fortReserveVO.setPingSuccess(expiredTime != 0 && pingSuccess);
        } catch (NullPointerException e) {
            LastTimeVO lastTimeVO = new LastTimeVO();
            lastTimeVO.setMinutes(0);
            lastTimeVO.setHour(0);
            lastTimeVO.setDay(0);
            lastTimeVO.setSeconds(0);
            fortReserveVO.setLastTimeVO(lastTimeVO);
            fortReserveVO.setTakingTime("00:00:00");
            fortReserveVO.setPingSuccess(false);
            log.error("enterRote-error:{}", e);
        }
        String msg = JSONObject.toJSONString(ApiResult.success(fortReserveVO));
//        log.info("redis-msg:{}", msg);
        return msg;
    }


    public static synchronized void subOnlineCount() {
        WebSocketRemoteTime.onlineCount--;
    }
}
