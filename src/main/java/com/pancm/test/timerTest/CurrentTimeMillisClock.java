package com.pancm.test.timerTest;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: 单例模式的时钟，可以获取当前的时间戳 性能较高
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/12/9
 */
public class CurrentTimeMillisClock {
    private volatile long now;

    private CurrentTimeMillisClock() {
        this.now = System.currentTimeMillis();
        scheduleTick();
    }

    private void scheduleTick() {
        new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "current-time-millis");
            thread.setDaemon(true);
            return thread;
        }).scheduleAtFixedRate(() -> {
            now = System.currentTimeMillis();
        }, 1, 1, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return now;
    }

    public static CurrentTimeMillisClock getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CurrentTimeMillisClock INSTANCE = new CurrentTimeMillisClock();
    }

    public static void main(String[] args) throws InterruptedException {
        getTime();
        Thread.sleep(500);
        getTime();
        Thread.sleep(200);
        getTime();
        Thread.sleep(2000);
        getTime();
    }

    private static void getTime() {
        System.out.println(CurrentTimeMillisClock.getInstance().now());
        System.out.println(System.currentTimeMillis());
    }
}
