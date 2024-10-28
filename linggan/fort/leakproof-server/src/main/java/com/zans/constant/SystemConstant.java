package com.zans.constant;

import com.zans.vo.WebSocketSessionVO;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
@Slf4j
public class SystemConstant {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String RESERVE_DATE_FORMAT = "yyyyMMddHHmmssSSS";

    public static final String APPOINTMENT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String SECURE_RANDOM = "SHA1PRNG";

    public static String SEPARATOR_LINE = "\n";

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

    public static final Integer HAS_DECODING = 0;

    public static final Integer NO_DECODE = 1;

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

    public static final String SFTP_HOST = "192.168.10.90";

    public static final Integer SFTP_PORT = 21;

    public static final String SFTP_USERNAME = "root";

    public static final String SFTP_PASSWORD = "lgwy@2020";

    public static final String DURATION = "Duration";//分割出时间

    public static final String COLON = ":";//

    public static final String POINT = ".";//

    public static ConcurrentHashMap<String, Boolean> ipInUserMap = new ConcurrentHashMap<>();

    public static String START_AGENT = "http://%s:54321/api/startEdr";

    public static String STOP_AGENT = "http://%s:54321/api/stopEdr";


    //解码服务命令
    public static String DECODE_BASE_CMD1 = "decode_base_cmd_1";

    public static String DECODE_BASE_CMD2 = "decode_base_cmd_2";

    public static String DECODE_GUAC_M4V = "decode_video_guac_m4v";

    public static String DECODE_WATER_MARL_INFO = "decode_water_mark_info";

    public static String DECODE_WATER_MARL_CMD = "decode_decode_water_mark";

    public static String DECODE_M4V_MP4 = "decode_video_m4v_mp4";

    public static String DECODE_VIDEO_SIZE = "decode_video_size";

    public static String DECODE_VIDEO_LENGTH = "decode_video_length";


}
