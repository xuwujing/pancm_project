package com.zans.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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


    //24h
    @Value("${security.jwt.expire-time:86400000}")
    private long jwtExpireTime;

    @Value("${security.jwt.app-expire-time:86400000}")
    private long appJwtExpireTime;

    @Value("${api.upload.max-size:5}")
    private int uploadMaxSize;

    @Value("${spring.profiles.active}")
    String active;

//    @Value("${api.export.folder}")
    String exportFolder = "/home/release/file/export/";

//    @Value("${api.upload.folder}")
    String uploadFolder = "/home/release/file/upload/";


    public static String UPLOAD_FOLDER;



    @PostConstruct
    private void init(){
        log.info("The following profiles are active:{}",active);

        UPLOAD_FOLDER = uploadFolder;

        JWT_EXPIRE_TIME = jwtExpireTime;
        APP_JWT_EXPIRE_TIME = appJwtExpireTime;
        UPLOAD_FILE_MAX_SIZE = uploadMaxSize * 1024 * 1024L;


    }









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
    public static String SESSION_KEY_IS_ADMIN = "isAdmin";
    public static String SESSION_KEY_AREA_ID_STR = "areaIdStr";///用户层的区域id字符串，逗号分割

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

    /**
     * 文件客户端登录和登出
     *
     */
    public static  String FILE_URL_LOGIN = "http://%s:54321/api/login";

    public static  String FILE_URL_LOGOUT = "http://%s:54321/api/logout";




}

