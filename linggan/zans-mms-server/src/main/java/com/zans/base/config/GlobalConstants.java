package com.zans.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class GlobalConstants {


    public static String INTERCEPTOR_ALL = "/**";

    public static String USER_VERIFY_JWT = "jwt";

    public static long JWT_EXPIRE_TIME;
    public static long APP_JWT_EXPIRE_TIME;

    /**
     * 项目环境  武汉交管、襄阳公安
     */
    public static String PROJECT_ENV = "whjg";
    /**
     * 武汉交管
     */
    public static String PROJECT_ENV_WHJG = "whjg";
    /**
     * 襄阳公安
     */
    public static String PROJECT_ENV_XYGA = "xyga";


    @Value("${security.jwt.expire-time:900000}")
    private long jwtExpireTime;

    @Value("${security.jwt.app-expire-time:900000}")
    private long appJwtExpireTime;

    @Value("${api.upload.max-size:5}")
    private int uploadMaxSize;

    @Value("${spring.profiles.active}")
    String active;

    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;


    public static String UPLOAD_FOLDER;


    public  static ConcurrentMap<String, DataSource> dataSourceConcurrentMap = new ConcurrentHashMap<>();


    @PostConstruct
    private void init(){
        log.info("The following profiles are active:{}",active);

        UPLOAD_FOLDER = uploadFolder;

        JWT_EXPIRE_TIME = jwtExpireTime;
        APP_JWT_EXPIRE_TIME = appJwtExpireTime;
        UPLOAD_FILE_MAX_SIZE = uploadMaxSize * 1024 * 1024L;


    }






    public static String DEFAULT_SERVER_CONTEXT = "/";




    public static String SYS_DICT_KEY_MAINTAIN_STATUS = "maintain_status";

    /**
     * 巡检结果
     */
    public static String SYS_DICT_KEY_PATROL_CHECK_RESULT = "patrol_check_result";


    /**
     * 项目名称
     */
    public static String SYS_DICT_KEY_PROJECT_NAME = "project_name";

    /**
     * 巡检项目
     */
    public static String SYS_DICT_KEY_ITEM_CLASSIFICATION = "item_classification";


    /**
     * 通用的启用、禁用状态
     */
    public static Integer STATUS_ENABLE = 1;
    public static Integer STATUS_DISABLE = 0;



    /**
     * 登陆连续错误的重试次数
     */
    public static int LOGIN_MAX_ATTEMPT = 10;
    /**
     * 连续请求次数
     */
    public static int REQ_MAX_ATTEMPT = 3;

    public static int RESP_CODE_SUCCESS = 0;


    public static String MODULE_DEVICE = "device";


    public static int SYS_MODULE_MENU_TYPE_FOLDER = 1;
    public static int SYS_MODULE_MENU_TYPE_MENU = 2;
    public static int SYS_MODULE_MENU_TYPE_OPERATION = 3;

    public static String MENU_HOME_PATH = "/home";
    /**
     * 个人中心
     */
    public static String MENU_PERSON_PATH = "/person/";






    /**
     * 权限
     */
    public static int MIN_PERMISSION_ID = 1000;

    /**
     * session 中的 key
     */
    public static String SESSION_KEY_UID = "uid";  // 自增长字段
    public static String SESSION_KEY_USER_ID = "userid";  //用户ID,例如：beipeng
    public static String SESSION_KEY_USER_NAME = "username";  //用户ID,例如：beipeng
    public static String SESSION_KEY_PASSPORT = "passport";  //用户ID,例如：beipeng
    public static String SESSION_KEY_NICK_NAME = "nickname"; //用户ID,例如：王家发
    public static String SESSION_KEY_USER_PHONE = "phone"; //用户手机
    public static String SESSION_KEY_ROLE_ID = "roleid";  //角色ID
    public static String SESSION_KEY_ROLE_NAME = "rolename";  //角色名称
    public static String SESSION_KEY_ORG_ID = "orgid";       //机构ID
    public static String SESSION_KEY_ORG_NAME = "orgname";   //机构名称
    public static String SESSION_KEY_TOKEN = "token";
    public static String SESSION_KEY_AREA_ID_STR = "areaIdStr";///用户层的区域id字符串，逗号分割
    public static String SESSION_KEY_JURISDICTION_ID = "jurisdictionId";//用户的权限id

    /**
     * 开发环境
     */
    public static String APP_ENV_LOCAL = "local";
    public static String APP_ENV_DEV = "dev";
    public static String APP_ENV_PRE = "pre";
    public static String APP_ENV_PROD = "prod";



    /**
     * 文件最大大小，5M
     */
    public static long UPLOAD_FILE_MAX_SIZE = 5 * 1024 * 1024L;



    /**
     * 程序版本
     */
    public static final String VERSION = "V1.1.1";

    public static final String VERSION_NAME = "version.txt";





    public static String MODULE_PATROL_ASSET_STATUS = "patrol_asset_status";
    public static String MODULE_PATROL_STATUS = "patrol_status";

    // 报价表大类字典表
    public static String MODULE_DEVICE_CATEGORY = "device_category";

    // 视频诊断故障类型
    public static String VIDEO_FAULT_TYPE = "faultType";

    // 视频诊断故障类型结果
    public static String VIDEO_FAULT_TYPE_RESULT = "faultTypeResult";

    // 视频诊断故障整体结果
    public static String VIDEO_DIAGNOSIS_RESULT = "diagnosisResult";


    /**
     * 设备类型
     */
    public static String BAYONET = "卡口";
    public static String MONITOR = "监控";
    public static String ELECTRIC_POLICE = "电警";
    public static String ELECTRONIC_LABEL = "汽车电子标识";
    public static String TRAFFIC_SIGNAL = "红绿灯信号机";


    /**
     * 舆情投诉字典
     */
    public static String AREA_ID = "area_id";
    public static String PO_EVENT = "po_event";
    public static String EVENT_SOURCE = "event_source";
    public static String PO_TYPE = "po_type";
    public static String REASON = "reason";
    public static String REPAIR_STATUS = "repair_status";
    public static String DUTY_CONTACT = "duty_contact";
    public static String PO_DEVICE_TYPE = "po_device_type";
    public static String FAULT_PHENOMENON="fault_phenomenon";


}

