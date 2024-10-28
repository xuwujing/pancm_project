package com.zans.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author pancm
 * @Title: GlobalConstants
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
public class GlobalConstants {


    public static final long SLEEP_TIME=30;

    public static  long SLEEP_TIME_QUEUE=10;

    public static  long SLEEP_TIME_RECOVER=60;

    public static  long SLEEP_TIME_AGG=60;

    public static  String AES_KEY="Lgwy@1234!@#$%^&";


    public static final int DEL_DAY=14;


    public static final String REDIS_QUEUE="message";

    public static  boolean IS_INIT = false;

    // 任务完成数量
    public static AtomicLong TASK_SUC_COUNT = new AtomicLong(0);



    public  static ThreadPoolExecutor tpx = new ThreadPoolExecutor(20, 100, 3,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000),
            Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardOldestPolicy());



    public static String DRIVER= "com.mysql.jdbc.Driver";
    public static String DRIVER_CLASSNAME = ".datasource.driverClassName";
    public static String URL_NAME = ".datasource.url";
    public static String USER_NAME = ".datasource.username";
    public static String PWD_NAME = ".datasource.password";





}
