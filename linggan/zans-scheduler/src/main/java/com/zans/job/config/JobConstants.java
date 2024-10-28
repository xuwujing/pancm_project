package com.zans.job.config;

/**
 * @author xv
 * @since 2020/5/7 18:28
 */
public class JobConstants {
    /** */
    public static String NODE_TYPE_SCAN = "scan";
    public static String NODE_TYPE_RAD_API = "rad_api";
    public static String NODE_TYPE_ALERT = "alert";
    public static String NODE_TYPE_JOB = "job";
    public static String NODE_TYPE_portal = "portal";

    public static int REMOTE_ACCESS_REPEAT = 3;

    /**
     * 休眠时间
     */
    public static int SLEEP_DEFAULT_SECONDS = 5;

    public static int NODE_DEFAULT_WEIGHT = 50;


    /**
     * ops_job.job_type
     * 0, executor_trigger，执行器定时
     * 1, server_trigger，服务器定时
     * 2，upgrade, 升级，很特殊，务必成功，后台不断重试；
     * 3. executor_ops：应用安装；应用启动/停止；服务器重启；
     */
    public static int JOB_TYPE_EXECUTOR = 0;

    /**
     * 应用升级任务
     */
    public static int JOB_ID_UPGRADE = 39;

    /**
     * ops_job_execution.job_status
     */
    public static int EXECUTION_NOT = 0;
    public static int EXECUTION_START = 1;
    public static int EXECUTION_DONE = 2;
    public static int EXECUTION_TIMEOUT = 3;
    public static int EXECUTION_ERROR = 4;

    /**
     * ops_job_task.alloc_status
     */
    public static int ALLOC_NOT = 0;
    public static int ALLOC_FAIL = 1;
    public static int ALLOC_DONE = 2;

    /**
     * ops_job_task.task_status
     */
    public static int TASK_NOT = 0;
    public static int TASK_START = 1;
    public static int TASK_DONE = 2;
    public static int TASK_FAIL = 3;

    public static int JOB_EXECUTION_TIMEOUT_MINUTES = 30;

    public static String LINE_SEPARATOR = "\n";

    public static int PRIORITY_ZERO = 0;

    public static final String VERSION = "V2.1.2";

}
