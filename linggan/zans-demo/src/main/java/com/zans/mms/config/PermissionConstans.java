package com.zans.mms.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
* @Title: PermissionConstans
* @Description: 权限常量类
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/9/21
*/
public class PermissionConstans {
    /**
     *   111：工单管理-查看
     *   99：巡检日志
     *   98：巡检任务
     *   86：设备上报
     *   147：app工单管理-查看
     *   86：设备上报
     **/
    public static final int PERM_ID_PC_TICKET = 111;
    public static final int PERM_ID_PC_PATROL_LOG = 99;
    public static final int PERM_ID_PC_PATROL_TASK = 98;
    public static final int PERM_ID_DEVICE_REPORT = 86;
    public static final int PERM_ID_HOME = 57;

    public static final int PERM_ID_APP_TICKET = 147;
    /**
     * 巡检点位
      */
    public static final int PERM_ID_APP_PATROL = 16;
    /**
     * 巡检全景
      */
    public static final int PERM_ID_APP_PATROL_TOP_AGG = 15;
    public static final int PERM_ID_APP_HOME_TICKET = 152;
    public static final int PERM_ID_APP_HOME_PATROL = 153;
    public static final int PERM_ID_APP_DEVICE_REPORT = 7;

    /**
     * 全部
     */
    public static final int PERM_ALL = 1;
    /**
     * 分配给自己单位
     */
    public static final int PERM_ORG = 2;

    /**
     * 个人
     */
    public static final int PERM_SELF = 4;

    /**
     * 工单未分配
     */
    public static final int PERM_UNALLOCATED_TICKET = 8;


    /**
     * 数据权限id集合
     * UPDATE sys_permission set data_perm_status=1 WHERE perm_id in (111,99,98,86,147,16,15,7)
     */
    public static final Set<Integer> DATA_PERM_SET = new HashSet<Integer>(){{
        add(PERM_ID_PC_TICKET);
        add(PERM_ID_PC_PATROL_LOG);
        add(PERM_ID_PC_PATROL_TASK);
        add(PERM_ID_DEVICE_REPORT);
        add(PERM_ID_APP_TICKET);
        add(PERM_ID_APP_PATROL);
        add(PERM_ID_APP_PATROL_TOP_AGG);
//        add(PERM_ID_APP_HOME_TICKET);
//        add(PERM_ID_APP_HOME_PATROL);
        add(PERM_ID_APP_DEVICE_REPORT);
//        add(PERM_ID_HOME);
    }};

    /**
     * 数据权限id集合
     */
    public static final Map<String,Object> DATA_PERM_MAP = new HashMap<String,Object>(){{
        put("",1);

    }};

    public static final int PERM_CACHE_YES = 1;
    public static final int PERM_CACHE_NO = 0;
    public static final int PERM_CACHE_NULL = -1;
}
