package com.zans.mms.config;

/**
 * @author beiso
 * @Description: //TODO
 * @date 2021/3/29 15:01
 */
public class PatrolConstants {

    /**
     * 1天
     */
    public static final int REMAINING_DAY_ONE = 1;

    /**
     * 2天
     */
    public static final int REMAINING_DAY_TWO = 2;

    /**
     * 2+天以上
     */
    public static final int REMAINING_DAY_TWOPLUS = 3;



    public static final int MAX_RADIUS = 950000;
    public static final int MIN_RADIUS = 300;
    public static final String FORMAT_FUNC_POINT = "point(%s,%s)";


    public static final String PATROL_NOT_START_STR = "未开始";
    public static final String PATROL_FINISH_STR = "已结束";
    public static final String PATROL_RUNNING_STR = "进行中";

    public static final String PATROL_START_TIME_SUFFIX = " 00:00:00";
    public static final String PATROL_END_TIME_SUFFIX = " 23:59:59";


    /**
     * 巡检状态-完成（app巡检全景）
     */
    public static final Integer STATUS_COMPLETED_DONE = 1;
    /**
     * 巡检状态-接近完成（app巡检-全景）
     */
    public static final Integer STATUS_COMPLETED_NEARLY = 2;
    /**
     * 巡检状态-仅完成一部分
     */
    public static final Integer STATUS_COMPLETED_FAR = 3;
    /**
     * 仅完成一部分的比例值
     */
    public static final double VALUE_COMPLETED_NEARLY = 0.7;

    /**
     * 默认半径
     */
    public static final int DEFAULT_APP_SEARCH_RADIUS = 300;



    /**
     * 强行添加点位(移除了其他子集中的点位，并添加到自己子集)
     */
    public static final Integer POINT_SUBSET_ADD_FORCE = 1;
    /**
     * 忽略已存在的
     */
    public static final Integer POINT_SUBSET_ADD_IGNORE = 2;

    /**
     * 后台返回未打卡的 10条数据
     */
    public static final Integer PATROL_LIMIT_FLAG = 1;

    /**
     * 未巡检打卡
     */
    public static final Integer PATROL_CHECK_STATUS_NO_DONE = 0;

    /**
     * 后台返回打卡点位距离flag
     */
    public static final Integer PATROL_GIS_FLAG = 1;

    /**
     * 移除数据权限标识
     */
    public static final Integer PATROL_REMOVE_DATA_PERM_FLAG = 1;


}
