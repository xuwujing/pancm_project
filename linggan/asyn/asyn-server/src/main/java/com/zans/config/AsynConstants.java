package com.zans.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author pancm
 * @Title:
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
public class AsynConstants {

    public static final int STATE_HEARTBEAT = 0;
    public static final int STATE_RECEIVE = 1;
    public static final int STATE_HANDLER_SUC = 2;
    public static final int STATE_HANDLER_ERR = 3;


    public static boolean IS_ENABLED = true;

    public static ThreadPoolExecutor tpx = new ThreadPoolExecutor(10, 50, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());

}
