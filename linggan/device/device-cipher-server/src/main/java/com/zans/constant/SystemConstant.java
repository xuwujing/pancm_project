package com.zans.constant;

import com.zans.vo.WebSocketSessionVO;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
public class SystemConstant {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String RESERVE_DATE_FORMAT = "yyyyMMddHHmmssSSS";

    public static final String APPOINTMENT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String SECURE_RANDOM = "SHA1PRNG";

    /**
     * 状态  0:申请中，
     * 1通过 ,
     * 2:否决，
     * 3:待开始，
     * 4:已开始，
     * 5:已结束
     */
    public static final Integer APPLYING = 0;

    public static final Integer PASS_APPLY = 1;

    public static final Integer VETO_APPLY = 2;

    public static final Integer TO_START = 3;

    public static final Integer UNDERWAY = 4;

    public static final Integer FINISHED = 5;

    public static final String PROPOSER = "默认申请人";

    public static final String APPROVER = "默认审批人";

    public static final String RESERVE_STATUS_TIMEOUT = "时间已过";

    public static final String RESERVE_STATUS_FULL = "预约已满";

    public static final String RESERVE_STATUS = "可预约";

    public static final Long TIMESTAMP = (long) 3600000;

    public static final Integer DAY_RESERVE_NUM_MAX = 16;

    public static final String RESERVE_FINISH = "已结束";

    public static final String RESERVE_NO_BEGIN = "未开始";

    public static final String RESERVE_UNDERWAY = "剩余";

    public static final String TIME_HOUR_MIN = ":00:00";

    public static final Integer ARRAY_INDEX_FIRST = 0;

    public static final Integer ARRAY_INDEX_SECOND = 1;

    public static final Integer ARRAY_LENGTH = 2;

    public static ConcurrentHashMap<String, WebSocketSessionVO> webSocketSession = new ConcurrentHashMap<>();

    public static final Integer TO_BE_DECODE = 0;

    public static final Integer DECODING = 1;

    public static final Integer DECODING_SUCCESS = 2;

    public static final String LGWY = "lgwy";

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String SYSTEM = "system";

    public static final Integer CONNECT_PORT = 3389;

    public static final String BASE_PATH = "/home/userfile/";

    public static final String SFTP_HOST = "192.168.10.90";

    public static final Integer SFTP_PORT = 21;

    public static final String SFTP_USERNAME = "root";

    public static final String SFTP_PASSWORD = "lgwy@2020";

    public static final String DOWNLOAD_BASE_PATH = "http://192.168.10.90:8900/";

    public static final String SALT = "yhcGnNnQdpHY";

    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";//手机号正则

    public static final String IP_REGEX = "^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$";

    public static final String PWD_STRONG_REGEX = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\\W_]).{6,10}";

    public static final Integer PWD_STRONG_TRUE = 1;

    public static final Integer PWD_STRONG_FALSE = 0;

    public static final String PWD_REGEX_CAPITAL_LETTER = "(?=.*[A-Z])";

    public static final String PWD_REGEX_LOWERCASE_LETTER = "(?=.*[a-z])";

    public static final String PWD_REGEX_NUM = "(?=.*[0-9])";

    public static final String PWD_REGEX_SPECIAL_CHARACTER = "(?=.*[!@#$%^&*_])";

    public static final String CRON_SEVEN_DAY = "0 0 0 1/7 * ?";

    public static final String CRON_ONE_MOUTH = "0 0 0 * * ?";

    public static final String CRON_THREE_MOUTH = "0 0 0 * 1/3 ?";

    public static final String CRON_HALF_YEAR = "0 0 0 * 1/6 ?";


}
