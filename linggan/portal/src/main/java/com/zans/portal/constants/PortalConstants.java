package com.zans.portal.constants;

import com.zans.portal.vo.log.WebSocketSessionVO;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiyi
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/7
 */
public class PortalConstants {

    public static final String PORTAL_LOG_OPERATION_BATCH_DELETE = "批量删除";

    public static final String PORTAL_LOG_OPERATION_BATCH = "批量处理";

    public static final String PORTAL_LOG_OPERATION_ONE_DELETE = "删除";

    public static final String PORTAL_LOG_OPERATION_ADD = "添加";

    public static final String PORTAL_LOG_OPERATION_EDIT = "修改";

    public static final String PORTAL_LOG_OPERATION_AUTH_SUBMIT = "认证申请";

    public static final String PORTAL_LOG_OPERATION_ENABLE = "变更状态";

    public static final String PORTAL_LOG_OPERATION_USER_CHANGE_PASSWORD = "用户更改密码";

    public static final String PORTAL_LOG_OPERATION_PASS = "放行";

    public static final String PORTAL_LOG_OPERATION_BLOCK = "阻断";

    public static final String PORTAL_LOG_OPERATION_IMPORT = "导入数据";

    public static final String PORTAL_LOG_OPERATION_EXPORT = "导出数据";

    public static final String PORTAL_LOG_TEMPLATE_DOWNLOAD = "模板下载";

    public static final String PORTAL_LOG_OPERATION_APPROVE = "认证审核";

    public static final String PORTAL_LOG_OPERATION_BATCH_JUDGE = "批量审核";

    public static final String PORTAL_LOG_OPERATION_LIST_CLEAR = "列表清空";

    public static final String PORTAL_LOG_OPERATION_REJECT_ANGIN = "认证驳回";

    public static final String PORTAL_LOG_OPERATION_WHITELIST = "加入白名单";

    public static final String PORTAL_LOG_OPERATION_WHITELIST_DELETE = "删除白名单";

    public static final String LOG_OPERATION_USER_CHANGE_PASSWORD = "变更用户密码";

    public static final String LOG_OPERATION_USER_LOGIN = "登录";

    public static final String LOG_OPERATION_USER_LOGOUT = "登出";

    public static final String LOG_OPERATION_UPDATE_SEQ = "更新排序";

    public static final String LOG_OPERATION_DISPOSE = "资产处置";

    public static final String LOG_OPERATION_CHOOSE_DEVICE_CACHE = "选择设备缓存";

    public static final String LOG_OPERATION_NO_CHOOSE_DEVICE_CACHE = "取消选择设备缓存";

    public static final String LOG_OPERATION_FORCE_OFFLINE = "强制下线";

    public static final String LOG_OPERATION_FORCE_ONLINE = "恢复入网";

    public static final String LOG_OPERATION_ACTIVATION = "激活";

    public static final String LOG_OPERATION_ITEM_MOVE = "排序移动";

    public static final String LOG_OPERATION_DOWNLOAD_ERROR_FILE = "下载上传失败文件";

    public static final String LOG_OPERATION_GROUP_UPDATE = "分组更新";

    public static final String PORTAL_MODULE_LOGIN = "登录";

    public static final String PORTAL_MODULE_EN_ABLE = "启用/禁用";

    public static final String PORTAL_MODULE_ASSET_MANAGE = "资产管理";

    public static final String PORTAL_MODULE_GUARD_LINE = "警卫线路";

    public static final String PORTAL_MODULE_ASSET_BRANCH = "资产子集";

    public static final String PORTAL_MODULE_ASSET_MAP = "资产地图";

    public static final String PORTAL_MODULE_CONSTANT = "系统";

    public static final String PORTAL_MODULE_CONSTANT_DICT = "系统字典表";

    public static final String PORTAL_MODULE_CUSTOM_FIELD = "自定义字段";

    public static final String PORTAL_MODULE_DEVICE_MODEL_OFFICIAL = "设备型号";

    public static final String PORTAL_MODULE_IP = "ip地址";

    public static final String PORTAL_MODULE_IP_SEGMENT = "ip地址段";

    public static final String PORTAL_MODULE_MAC = "MAC来源管理";

    public static final String PORTAL_MODULE_MODEL_SCAN = "设备端口模型管理";

    public static final String PORTAL_MODULE_MODULE = "模块管理";

    public static final String PORTAL_MODULE_PERMISSION = "权限管理";

    public static final String PORTAL_MODULE_RADIUS = "终端准入";

    public static final String PORTAL_MODULE_RADIUS_NAS = "radius-区域接入点";

    public static final String PORTAL_MODULE_QZ = "终端准入";

    public static final String PORTAL_MODULE_REGION = "行政区管理";

    public static final String PORTAL_MODULE_ROLE = "角色管理";

    public static final String PORTAL_MODULE_SWITCH_BRANCH = "交换机";

    public static final String PORTAL_MODULE_BRAND = "设备品牌";

    public static final String PORTAL_MODULE_ALERT = "告警";

    public static final String PORTAL_MODULE_ALERT_OFFLINE = "告警离线设备";

    public static final String PORTAL_MODULE_BASELINE = "基线数据";

    public static final String PORTAL_MODULE_USER = "用户";


    public static final String PORTAL_MODULE_TYPE_JUDGE = "judge";

    public static final String PORTAL_MODULE_TYPE_JUDGE_BATCH = "judge_batch";



    public static ConcurrentHashMap<String, WebSocketSessionVO> webSocketSession = new ConcurrentHashMap<>();

    //默认过期时间，单位秒
    public static final long WEBSOCKET_SESSION_EXPIRE_TIME = 20;

    public static final long SCHDULED_TASK_TIME = 21;

    public static final String ON_LINE_USER = "inLineUser-";
    //状态
    public static final String STATUS_SUCCESS = "成功";

    public static final String STATUS_FALSE = "失败";

    public static final String STATUS_SYSTEM_FALSE = "系统错误";

    public static final String STATUS_LOGIN_SUCCESS = "登录成功";

    public static final String STATUS_LOGOUT_SUCCESS = "登出成功";

    public static final String OP_PLATFORM = "PC";

    public static final String QZ_PROBE_IP = "scan:qz_probe_ip";

    public static final String PORTAL_MODULE_BASELINE_AREA = "基线数据分组规则";

    /**
     * CORE;DISTRIBUTE;ACCESS;核心，汇聚，接入
     */
    public static final String TOPO_TPYE_CORE = "CORE";
    public static final String TOPO_TPYE_DIST = "DISTRIBUTE";
    public static final String TOPO_TPYE_ACCESS = "ACCESS";
    public static final String TOPO_DATA_TPYE = "topy:data_type";

    /**
     * https://fgr44sks34.feishu.cn/docs/doccn5vtpIqBMGFmQivui19RVxb#
     * access_num=1
     */
    public static final Integer TOPO_DATA_TPYE_1 = 1;
    /**
     * access_num=2
     */
    public static final Integer TOPO_DATA_TPYE_2 = 2;
    /**
     * access_num<=12
     */
    public static final Integer TOPO_DATA_TPYE_3 = 3;
    /**
     * access_num>12
     */
    public static final Integer TOPO_DATA_TPYE_4 = 4;
    /**
     * aggregation_num<=3
     */
    public static final Integer TOPO_DATA_TPYE_5 = 5;
    /**
     * 4<=aggregation_num<=8
     */
    public static final Integer TOPO_DATA_TPYE_6 = 6;
    /**
     * aggregation_num>8
     */
    public static final Integer TOPO_DATA_TPYE_7 = 7;

}
