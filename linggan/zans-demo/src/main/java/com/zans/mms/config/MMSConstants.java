package com.zans.mms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class MMSConstants {



    public static  String MSG_FORMAT_GIS = "point(%s %s)";

    /**
     * 内场: 0301
     * 业主:0101
     * 监理:0501
     */
    public static final String INSIDE_ROLE = "0301";
    public static final String OUTFIELD_ROLE = "0302";
    public static final String SUPERVISION_ROLE = "0501";
    public static final String OWNER_ROLE = "0101";


    public static final int TICKET_TEMPLATE_CREATE = 11;
    public static final int TICKET_TEMPLATE_DISPATCH_SUBMIT = 12;
    public static final int TICKET_TEMPLATE_ACCEPT_SUBMIT = 13;
    public static final int PATROL_TEMPLATE_ACCEPT_UN_DONE = 21;


    /**
    *  日志模块
    **/
    public static final String LOG_OPERATION_SAVE = "新增";
    public static final String LOG_OPERATION_EDIT = "修改";
    public static final String LOG_OPERATION_DELETE = "删除";
    public static final String LOG_OPERATION_BATCH_EDIT = "批量修改";
    public static final String LOG_OPERATION_BATCH_DELETE = "批量删除";
    public static final String LOG_OPERATION_IMPORT = "导入";
    public static final String LOG_OPERATION_EXPORT = "导出";
    public static final String LOG_OPERATION_SUBMIT = "提交";
    public static final String LOG_OPERATION_VERIFY = "审批";
    public static final String LOG_OPERATION_REJECT = "退回";
    public static final String LOG_OPERATION_ENABLE = "变更状态";
    public static final String LOG_OPERATION_LOG_OUT = "退出登录";
    public static final String LOG_OPERATION_CLOCK_IN = "打卡";
    public static final String LOG_OPERATION_DOWNLOAD = "下载";
    public static final String LOG_OPERATION_BATCH_ADD = "批量新增";
    public static final String LOG_OPERATION_CHANGE_PROJECT_ID = "切换项目编号";
    public static final String LOG_OPERATION_JUDGE = "审核";
    public static final String LOG_OPERATION_BATCH_JUDGE = "批量审核";

    public static final String LOG_RESULT_SUCCESS = "成功";
    public static final String LOG_RESULT_FAIL = "失败";

    public static final String LOG_PC = "PC";
    public static final String LOG_APP = "APP";

    /**
    *  模块名称
    **/
    public static final String MODULE_SYSTEM_USER = "用户管理";
    public static final String MODULE_SYSTEM_ORG = "组织机构";
    public static final String MODULE_TICKET = "工单管理";
    public static final String MODULE_TICKET_DISPATCH = "派工单";
    public static final String MODULE_TICKET_ACCEPT = "验收单";
    public static final String MODULE_DEVICE_REPORT = "设备上报";
    public static final String MODULE_PATROL_SCHEME = "巡检计划";
    public static final String MODULE_ASSET = "资产管理";
    public static final String MODULE_ASSET_SUBSET = "资产子集管理";
    public static final String MODULE_POINT = "点位管理";
    public static final String MODULE_POINT_SUBSET = "点位子集管理";
    public static final String MODULE_TICKET_AUTO = "一键工单";
    public static final String MODULE_PROJECT = "项目管理";
    public static final String MODULE_MAC_WHITE_LIST = "mac白名单";
    public static final String MODULE_RADIUS_END_POINT = "radius";

    public static String MODULE_ENABLE_STATUS = "enable";
    public static Integer MODULE_TYPE_PC = 1;
    public static Integer MODULE_TYPE_APP = 2;

    public static final String MODULE_ROLE_SCHEME = "角色管理";

    /**
     * 扫描API
     */
    public static final String SCAN_API = "scan_api";

    /**
     * 一键ping uri
     */
    public static final String SCAN_STATS_PING_URI = "/scan/stats/ping";


    public static  final BigDecimal ONE_HUNDRED = new BigDecimal("100");


    /**
     * 修改gis权限路由
     */
    public static final String CHECK_GIS_PERM_STR = "/check/gis/perm";
    /**
     * 修改gis权限flag
     */
    public static final Integer CHECK_GIS_PERM_FLAG = 1;


}

