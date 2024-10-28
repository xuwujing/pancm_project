package com.zans.mms.config;

import com.zans.base.util.StringHelper;
import com.zans.mms.model.SysConstant;
import com.zans.mms.service.ISysConstantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class PortalGlobalConstants {
    @Autowired
    ISysConstantService sysConstantService;


    public static String RSA_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt8efEMmn1CaPUehdf9m2Qoj7MFjK7xVFSxVFPTIstjL1U11KzeS4hfg2jSrrTfxdYU3hdpfwhIf0gobfUsgewTkvN9ilpy3/PHjhgEmR1nmtfHppAzMlYSOZzEOowCGquf3cDw07P1+HpDy2nKwgSQ1YnvULrrW/jwpDwZOgk8JaG2c4QyzxSXx7OhWnVp5/E0cnx5rYfI6nbgmuugIJU3lqzMKnRxsQHDw92XyfdAO7nFuZtADYh1X0vqZ3IhORsstzeNXULc2x7PzBp9fHoY4k+zVdOPfVTGUckJ5ONWq3IGojPYkiK/CW4ay4ksSQ3NKv/JdPBBUq4Otb1N1S7QIDAQAB";

    public static String INTERCEPTOR_ALL = "/**";

    public static String USER_VERIFY_JWT = "jwt";

    public static long JWT_EXPIRE_TIME;

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

    /**
     * 孝感交管
     */
    public static String PROJECT_ENV_XGJG = "xgjg";


    @Value("${security.jwt.expire-time:900000}")
    private long jwtExpireTime;

    @Value("${api.upload.max-size:5}")
    private int uploadMaxSize;


    public  static ConcurrentMap<String, DataSource> dataSourceConcurrentMap = new ConcurrentHashMap<>();


    @PostConstruct
    private void init(){
        SysConstant projectEnv = sysConstantService.findConstantByKey("project_env");
        if (null != projectEnv) {
            PROJECT_ENV = projectEnv.getConstantValue();
        }

        log.info("PROJECT_ENV :{}",PROJECT_ENV);


//        initRadiusServer();

        JWT_EXPIRE_TIME = jwtExpireTime;
        UPLOAD_FILE_MAX_SIZE = uploadMaxSize * 1024 * 1024L;
        OP_LOG_KEY_MAP = new ConcurrentHashMap<>(200);

        OP_LOG_KEY_MAP.put("id", "主键");
        OP_LOG_KEY_MAP.put("arp_id", "主键");
        OP_LOG_KEY_MAP.put("area", "区域编号");
        OP_LOG_KEY_MAP.put("areaId", "区域编号");
        OP_LOG_KEY_MAP.put("area_id", "区域编号");
        OP_LOG_KEY_MAP.put("area_name", "区域名称");
        OP_LOG_KEY_MAP.put("company", "公司");
        OP_LOG_KEY_MAP.put("ipAddr", "IP地址");
        OP_LOG_KEY_MAP.put("ip", "IP地址");
        OP_LOG_KEY_MAP.put("mac", "MAC地址");
        OP_LOG_KEY_MAP.put("macAddr", "MAC地址");

        // module = 3
        OP_LOG_KEY_MAP.put("allocId", "设备分配主键");
        OP_LOG_KEY_MAP.put("authStatus", "审批状态");
        OP_LOG_KEY_MAP.put("deviceModelBrand", "设备品牌");
        OP_LOG_KEY_MAP.put("deviceModelDes", "设备型号");
        OP_LOG_KEY_MAP.put("deviceModelType", "设备类型");
        OP_LOG_KEY_MAP.put("deviceTypeId", "设备类型编号");
        OP_LOG_KEY_MAP.put("deviceType", "设备类型编号");
        OP_LOG_KEY_MAP.put("linkPerson", "联系人");
        OP_LOG_KEY_MAP.put("gateway", "网关");
        OP_LOG_KEY_MAP.put("mask", "掩码");
        OP_LOG_KEY_MAP.put("pointName", "点位名");
        OP_LOG_KEY_MAP.put("projectName", "项目名称");
        OP_LOG_KEY_MAP.put("theDate", "分配日期");
        OP_LOG_KEY_MAP.put("vlan", "VLAN");

        // module = 4
        OP_LOG_KEY_MAP.put("deal", "处理方式");
        OP_LOG_KEY_MAP.put("deal_mark", "处理意见");

        // module = 7
        OP_LOG_KEY_MAP.put("device_des", "设备型号");
        OP_LOG_KEY_MAP.put("device_name", "设备名称");
        OP_LOG_KEY_MAP.put("device_type", "设备类型");
        OP_LOG_KEY_MAP.put("mute", "哑终端");
        OP_LOG_KEY_MAP.put("person", "联系人");
        OP_LOG_KEY_MAP.put("phone", "联系电话");

        // module = 8
        OP_LOG_KEY_MAP.put("authMac", "认证MAC");
        OP_LOG_KEY_MAP.put("authPerson", "申请人");
        OP_LOG_KEY_MAP.put("authTime", "申请时间");
        OP_LOG_KEY_MAP.put("authPermitPerson", "审批人");
        OP_LOG_KEY_MAP.put("authPermitTime", "审批时间");
        OP_LOG_KEY_MAP.put("authMark", "审批意见");

        // module = 9
        OP_LOG_KEY_MAP.put("allocDay", "分配日期");
        OP_LOG_KEY_MAP.put("createPerson", "创建人");
        OP_LOG_KEY_MAP.put("createTime", "创建时间");
        OP_LOG_KEY_MAP.put("insertStatus", "导入状态");
        OP_LOG_KEY_MAP.put("validStatus", "校验状态");
        OP_LOG_KEY_MAP.put("updateTime", "更新时间");

        //
        OP_LOG_KEY_MAP.put("file_path", "文件路径");
        OP_LOG_KEY_MAP.put("error", "错误");
        OP_LOG_KEY_MAP.put("valid", "合规");

        //设备类型
        OP_LOG_KEY_MAP.put("type_id", "设备类别编号");
        OP_LOG_KEY_MAP.put("type_name", "类型名称");
        OP_LOG_KEY_MAP.put("template", "模板地址");

        //交换机管理
        OP_LOG_KEY_MAP.put("sw_host", "登陆地址");
        OP_LOG_KEY_MAP.put("sys_name", "交换机名称");
        OP_LOG_KEY_MAP.put("sw_type", "交换机类型");
        OP_LOG_KEY_MAP.put("protocol", "管理协议");
        OP_LOG_KEY_MAP.put("telnet_port", "telnet端口");
        OP_LOG_KEY_MAP.put("ssh_port", "ssh端口");
        OP_LOG_KEY_MAP.put("sw_account", "登录账号");
        OP_LOG_KEY_MAP.put("sw_password", "登录密码");
        OP_LOG_KEY_MAP.put("sw_community", "SNMP团体名");
        OP_LOG_KEY_MAP.put("sw_snmp_version", "SNMP版本");
        OP_LOG_KEY_MAP.put("radius_config", "Radius认证");
        OP_LOG_KEY_MAP.put("brand", "品牌");
        OP_LOG_KEY_MAP.put("version", "型号");
        OP_LOG_KEY_MAP.put("model", "处理器");
        OP_LOG_KEY_MAP.put("nas_ip", "nas ip");
        OP_LOG_KEY_MAP.put("secret", "nas密码");
        OP_LOG_KEY_MAP.put("short_name", "nas简称");
        OP_LOG_KEY_MAP.put("nas_type", "nas类型");
        OP_LOG_KEY_MAP.put("test_type", "测试类型");
        OP_LOG_KEY_MAP.put("nas", "关联的Nas集合");

        //Radius服务
        OP_LOG_KEY_MAP.put("server_id", "Radius服务id");
        OP_LOG_KEY_MAP.put("server_name", "server名称");
        OP_LOG_KEY_MAP.put("server_desc", "server描述");
        OP_LOG_KEY_MAP.put("sql_driver", "数据库类型");
        OP_LOG_KEY_MAP.put("sql_host", "主机IP");
        OP_LOG_KEY_MAP.put("sql_port", "端口");
        OP_LOG_KEY_MAP.put("sql_user", "账号");
        OP_LOG_KEY_MAP.put("sql_passwd", "密码");
        OP_LOG_KEY_MAP.put("enable", "能否可用");
        OP_LOG_KEY_MAP.put("service_host", "服务IP地址");
        OP_LOG_KEY_MAP.put("service_auth_port", "服务认证端口");
        OP_LOG_KEY_MAP.put("service_accounting_port", "服务计费端口");
        OP_LOG_KEY_MAP.put("service_secret", "服务密钥");
        OP_LOG_KEY_MAP.put("sql_db", "数据库名称");

        //区域管理
        OP_LOG_KEY_MAP.put("region", "区划id");
        OP_LOG_KEY_MAP.put("admin_area_name", "行政区划");

        //区域网络初始化
        OP_LOG_KEY_MAP.put("device_type_id", "设备类型id");
        OP_LOG_KEY_MAP.put("device_type_name", "设备类型名称");
        OP_LOG_KEY_MAP.put("ip_addr", "ip地址");
        OP_LOG_KEY_MAP.put("mask", "掩码");
        OP_LOG_KEY_MAP.put("gateway", "网关");
        OP_LOG_KEY_MAP.put("vlan", "vlan");

        //区域接入点 radius_nas
        OP_LOG_KEY_MAP.put("nas_sw_ip", "交换机地址");
        OP_LOG_KEY_MAP.put("delete_status", "状态");
        OP_LOG_KEY_MAP.put("short_name", "接入点名称");
        OP_LOG_KEY_MAP.put("nas_type", "nas类型");

        //系统字典表
        OP_LOG_KEY_MAP.put("dict_key", "常量类型主键");
        OP_LOG_KEY_MAP.put("module_id", "模块id");
        OP_LOG_KEY_MAP.put("short_name", "常量类型名称");
        OP_LOG_KEY_MAP.put("module_name", "模块名称");
        OP_LOG_KEY_MAP.put("table_name", "表名");
        OP_LOG_KEY_MAP.put("column_name", "列名");
        OP_LOG_KEY_MAP.put("dict_name", "字典类型名称");

        //系统字典详情表
        OP_LOG_KEY_MAP.put("item_key", "类型主键");
        OP_LOG_KEY_MAP.put("item_value", "类型value");
        OP_LOG_KEY_MAP.put("ordinal", "排序");
        OP_LOG_KEY_MAP.put("class_type", "数值类型");

        //常量表
        OP_LOG_KEY_MAP.put("constant_key", "常量key");
        OP_LOG_KEY_MAP.put("status", "是否可用");
        OP_LOG_KEY_MAP.put("comment", "备注");
        OP_LOG_KEY_MAP.put("constant_value", "常量值");

        //t_device_model_official
        OP_LOG_KEY_MAP.put("brand_id", "公司id");
        OP_LOG_KEY_MAP.put("model_code", "设备型号");
        OP_LOG_KEY_MAP.put("model_des", "设备名称");
        OP_LOG_KEY_MAP.put("remark", "备注信息");

        //sys_brand
        OP_LOG_KEY_MAP.put("confirm", "是否允许被删除");

        //ip_segment
        OP_LOG_KEY_MAP.put("segment", "IP地址段必填");
        OP_LOG_KEY_MAP.put("segment_short", "IP地址段必填");
        OP_LOG_KEY_MAP.put("tag", "标签");
        OP_LOG_KEY_MAP.put("ip_count", "ip数量");

        //用户管理
        OP_LOG_KEY_MAP.put("role_name", "角色名称");
        OP_LOG_KEY_MAP.put("lock_status", "锁定状态");
        OP_LOG_KEY_MAP.put("role", "角色id");
        OP_LOG_KEY_MAP.put("enable_name", "是否可用");
        OP_LOG_KEY_MAP.put("lock_status_name", "锁定状态名称");
        OP_LOG_KEY_MAP.put("user_name", "用户名");
        OP_LOG_KEY_MAP.put("department_name", "所在部门名称");
        OP_LOG_KEY_MAP.put("nick_name", "昵称");
        OP_LOG_KEY_MAP.put("department", "所在部门");

        //IP准入
        OP_LOG_KEY_MAP.put("contractor", "承建单位");
        OP_LOG_KEY_MAP.put("contractor_person", "承建联系人");
        OP_LOG_KEY_MAP.put("device_model_type", "设备类别");
        OP_LOG_KEY_MAP.put("device_model_brand", "设备品牌");
        OP_LOG_KEY_MAP.put("project_status", "项目维护状态");
        OP_LOG_KEY_MAP.put("device_model_des", "设备型号");
        OP_LOG_KEY_MAP.put("point_name", "点位名称");
        OP_LOG_KEY_MAP.put("project_name", "项目名称");
        OP_LOG_KEY_MAP.put("the_date", "分配日期");
        OP_LOG_KEY_MAP.put("maintain_phone", "维护电话");
        OP_LOG_KEY_MAP.put("vlan", "vlan");
        OP_LOG_KEY_MAP.put("auth_status", "入网认证状态");
        OP_LOG_KEY_MAP.put("contractor_phone", "承建电话");
        OP_LOG_KEY_MAP.put("link_count", "链路数量");
        OP_LOG_KEY_MAP.put("build_day", "建设时间");
        OP_LOG_KEY_MAP.put("project_type", "项目类型");
        OP_LOG_KEY_MAP.put("name", "名称");
        OP_LOG_KEY_MAP.put("fileName", "文件名");

        //license 字段
        OP_LOG_KEY_MAP.put("macadress", "设备识别号");
        OP_LOG_KEY_MAP.put("producttype", "授权类型");
        OP_LOG_KEY_MAP.put("productname", "产品类型");
        OP_LOG_KEY_MAP.put("productowner", "版权所有");
        OP_LOG_KEY_MAP.put("channel", "来源渠道");
        OP_LOG_KEY_MAP.put("totol", "授权终端总量");
        OP_LOG_KEY_MAP.put("alreadyQty", "已使用授权数量");
        OP_LOG_KEY_MAP.put("copyright", "有效期");
        OP_LOG_KEY_MAP.put("mac_addr", "mac地址");

        //行政区划
        OP_LOG_KEY_MAP.put("parent_id", "父级id");
        OP_LOG_KEY_MAP.put("region_id", "区域id");
        OP_LOG_KEY_MAP.put("region_name", "区域名称");

        //模块管理
        OP_LOG_KEY_MAP.put("route", "路由");
        OP_LOG_KEY_MAP.put("visible", "是否可见");
        OP_LOG_KEY_MAP.put("menu_type", "菜单类型");
        OP_LOG_KEY_MAP.put("icon", "图标");


        //模块管理
        OP_LOG_KEY_MAP.put("setting_name", "设置名称");
        OP_LOG_KEY_MAP.put("setting_type", "设置类型");
        OP_LOG_KEY_MAP.put("setting_value", "设置值");
        OP_LOG_KEY_MAP.put("setting_key", "设置键");
        OP_LOG_KEY_MAP.put("setting_around", "值范围");

        // 驼峰自动转下划线
        for(String key : OP_LOG_KEY_MAP.keySet()) {
            String value = OP_LOG_KEY_MAP.get(key);
            OP_LOG_KEY_MAP.put(StringHelper.camelCaseToUnderScore(key), value);
        }

        TASK_LOG_KEY_MAP = new ConcurrentHashMap<>(200);
        TASK_LOG_KEY_MAP.put("thread_name", "线程名称");
        TASK_LOG_KEY_MAP.put("scan_count", "扫描数量");
        TASK_LOG_KEY_MAP.put("alive_not_count", "离线数");
        TASK_LOG_KEY_MAP.put("new_count", "新增数");
        TASK_LOG_KEY_MAP.put("change_count", "变更数");
        TASK_LOG_KEY_MAP.put("nochange_count", "不变数");

        // task01
        TASK_LOG_KEY_MAP.put("index", "序号");
        TASK_LOG_KEY_MAP.put("area_id", "区域");
        TASK_LOG_KEY_MAP.put("sw_ip", "交换机网关");
        TASK_LOG_KEY_MAP.put("switcher", "交换机型号");
        TASK_LOG_KEY_MAP.put("snmpwalk_result", "SNMP扫描数");
        TASK_LOG_KEY_MAP.put("snmpwalk_command", "SNMP命令");


        //检疫区自定义列
        CUSTOM_COLUMN_MAP = new ConcurrentHashMap<>(64);

        CUSTOM_COLUMN_MAP.put("ip_addr","IP地址");
        CUSTOM_COLUMN_MAP.put("username","MAC地址");
        CUSTOM_COLUMN_MAP.put("point_name","点位名称");
        CUSTOM_COLUMN_MAP.put("area_name","所属区域");
        CUSTOM_COLUMN_MAP.put("device_type_name","设备类型");
        CUSTOM_COLUMN_MAP.put("alive_qz_name","在线状态");
        CUSTOM_COLUMN_MAP.put("company","厂商");
        CUSTOM_COLUMN_MAP.put("create_time","入网时间");

        CUSTOM_COLUMN_MAP.put("nas_ip_address","NAS地址");
//        CUSTOM_COLUMN_MAP.put("nas_name","NAS名称");
//        CUSTOM_COLUMN_MAP.put("nas_port_type","NAS类型");
        CUSTOM_COLUMN_MAP.put("nas_port_id","NAS接口");
//        CUSTOM_COLUMN_MAP.put("called_station_id","NAS-MAC");
        CUSTOM_COLUMN_MAP.put("alive_name","是否入网");
        CUSTOM_COLUMN_MAP.put("project_name","项目名称");
        CUSTOM_COLUMN_MAP.put("contractor","承建单位");
        CUSTOM_COLUMN_MAP.put("contractor_person","联系人");
        CUSTOM_COLUMN_MAP.put("contractor_phone","联系电话");
        CUSTOM_COLUMN_MAP.put("brand_name","设备品牌");
        CUSTOM_COLUMN_MAP.put("cur_model_des","设备型号");
        CUSTOM_COLUMN_MAP.put("server_os","系统版本");

    }





    private String getMySqlIp(String mysqlDb,String mysqlIp, Integer mysqlPort) {
        String mysqlUrl = "jdbc:mysql://%s?autoReconnect=true&failOverReadOnly=false&characterEncoding=utf-8&useUnicode=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true";
        String url = mysqlIp.concat(":").concat(String.valueOf(mysqlPort)).concat("/").concat(mysqlDb);
        mysqlUrl = String.format(mysqlUrl,url);
        return mysqlUrl;
    }

    //检疫区自定义列
    public static Map<String, String> CUSTOM_COLUMN_MAP;

    public static Map<String, String> OP_LOG_KEY_MAP;

    public static Map<String, String> TASK_LOG_KEY_MAP;

    public static String DEFAULT_SERVER_CONTEXT = "/";

    /**
     * t_arp.dis_status 状态
     */
    public static String MODULE_ARP_DIS_STATUS = "dis_status";
    public static String MODULE_ARP_ALIVE = "alive";
    public static String MODULE_ARP_MUTE = "mute";
    public static String MODULE_ARP_CHANGE_TYPE = "change_type";
    public static String MODULE_ARP_IP_STATUS = "ip_status";
    public static String MODULE_ARP_MODEL_LEVEL = "model_level";
    public static String MODULE_ARP_RISK_TYPE = "risk_type";
    public static String MODULE_ARP_CONFIRM_STATUS = "arp_confirm_status";


    public static String EXCELCONTENTTYPE_97OR2003 = "application/vnd.ms-excel";
    public static String EXCELCONTENTTYPE_2007 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * 数据字典 是否可用
     */
    public static String SYS_DICT_KEY_ENABLE = "enable";
    public static String SYS_DICT_KEY_ENABLE_STATUS = "enable_status";

    /**
     * 数据字典  资产录入来源
     */
    public static String SYS_DICT_KEY_SOURCE = "source";

    public static String SYS_DICT_KEY_MAINTAIN_STATUS = "maintain_status";

    public static String SWITCHER_TYPE = "SW_TYPE";

    public static String SWITCHER_PROTOCOL = "sw_protocol";


    public static int ARP_ALIVE_ONLINE = 1;
    public static int ARP_ALIVE_OFFLINE = 2;


    /**
     * t_arp.confirm_status
     */
    public static int CONFIRM_STATUS_INIT = 0;
    public static int CONFIRM_STATUS_EDIT = 1;
    public static int CONFIRM_STATUS_DONE = 2;


    public static int ARP_IP_STATUS_NORMAL = 0;
    /**
     *
     */
    public static int ARP_IP_STATUS_CHANGE_INIT = 1;
    public static int ARP_IP_STATUS_CHANGE_BLOCK = 2;
    public static int ARP_IP_STATUS_CHANGE_PASS = 3;
    public static int ARP_IP_STATUS_CHANGE_WHITELIST = 99;

    /**
     * t_ip_all.source
     */
    public static int IP_ALL_SOURCE_INIT = 0; //系统生成
    public static int IP_ALL_SOURCE_MANUAL = 1; //手工录入
    public static int IP_ALL_SOURCE_IMPORT = 2; //导入



    /**
     * asset.source
     */
    public static int ASSET_SOURCE_MANUAL= 4;
    public static int ASSET_SOURCE_IMPORT = 5;

    public static int ASSET_MAINTAINSTATUS_NORMAL = 1;


    /**
     * t_ip_all.ip_used
     */
    public static int IP_ALL_IP_NO_USE = 0;
    public static int IP_ALL_IP_USED = 1;

    /**
     * 区域级别：1，2，3
     */
    public static int REGION_LEVEL_ONE = 1;
    public static int REGION_LEVEL_TWO = 2;
    public static int REGION_LEVEL_THREE = 3;

    /**
     * 风险类型
     */
    public static int RISK_PRIVATE = 1;
    public static int RISK_FORGE = 2;
    public static int RISK_REPLACE = 3;
    public static int RISK_EXCEPTION = 4;

    public static String[] HOME_RISK_TYPE_INDEX = {"no_risk", "private", "forge", "replace", "exception"};

    /**
     * t_ip_all
     */
    public static String MODULE_IP_ALL_AUTH_STATUS = "auth_status";
    public static String MODULE_IP_ALL_PROJECT_STATUS = "project_status";

    /**
     * 3，否决；4，驳回
     */
    public static int IP_AUTH_INIT = 0;
    public static int IP_AUTH_SUBMIT = 1;
    public static int IP_AUTH_PASS = 2;
    public static int IP_AUTH_REJECT = 3;
    public static int IP_AUTH_REJECT_AGAIN = 4;


    public static String MODULE_USER_LOCK = "lock_status";
    public static String MODULE_USER_DEPARTMENT = "department";
    public static String MODULE_ENABLE_STATUS = "enable";

    /**
     * 通用的启用、禁用状态
     */
    public static int STATUS_ENABLE = 1;
    public static int STATUS_DISABLE = 0;

    public static int USER_LOCK_NONE = 0;
    public static int USER_LOCK_ADMIN = 1;
    public static int USER_LOCK_PASSWORD_ERROR = 2;

    /**
     * 登陆连续错误的重试次数
     */
    public static int LOGIN_MAX_ATTEMPT = 10;

    public static int RESP_CODE_SUCCESS = 0;


    /**
     * t_deny
     */
    public static String MODULE_DENY_REASON = "deny_reason";
    public static String MODULE_DENY_DEAL = "deny_deal";

    public static int DENY_REASON_INIT = 0;
    public static int DENY_REASON_UNKNOWN = 1;
    public static int DENY_REASON_MAC = 4;
    public static int DENY_REASON_ARP_OUT = 5;
    public static int DENY_REASON_PORT = 6;
    public static int DENY_REASON_DEVICE_MODEL = 9;

    public static int DENY_DEAL_INIT = 0;
    public static int DENY_DEAL_BLOCK = 2;
    public static int DENY_DEAL_BLOCK_LATEST = 5;
    public static int DENY_DEAL_PASS = 3;
    public static int DENY_DEAL_WHITELIST = 4;
    public static int DENY_DEAL_WHITELIST_REMOVE = 6;


    /**
     * 日志的操作
     */
    public static String MODULE_OP_LOG_MODULE = "log_module";

    public static Integer LOG_MODULE_USER = 1;
    public static Integer LOG_MODULE_ASSET = 2;
    public static Integer LOG_MODULE_IP = 3;
    public static Integer LOG_MODULE_CHANGE_DEAL = 4;
    public static Integer LOG_MODULE_MODEL = 5;
    public static Integer LOG_MODULE_WHITE_IP = 6;
    public static Integer LOG_MODULE_WHITE_MAC = 7;
    public static Integer LOG_MODULE_IP_AUTH = 8;
    public static Integer LOG_MODULE_IP_ALLOC = 9;
    public static Integer LOG_MODULE_NETWORK = 10;
    public static Integer LOG_MODULE_IP_SEGMENT = 11;
    public static Integer LOG_MODULE_DEVICE_TYPE = 12;
    public static Integer LOG_MODULE_SWITCH = 13;
    public static Integer LOG_MODULE_RADIUS = 14;
    public static Integer LOG_MODULE_AREA = 15;
    public static Integer LOG_MODULE_SYSCONSTANT = 16;
    public static Integer LOG_MODULE_SYSSTTING = 17;
    public static Integer LOG_MODULE_MODULE = 18;
    public static Integer LOG_MODULE_REGION = 19;
    public static Integer LOG_MODULE_MAC = 20;
    public static Integer LOG_MODULE_CUSTOM_FIELD = 21;
    public static Integer LOG_MODULE_DEPARTMENT = 22;
    public static Integer LOG_MODULE_SWITCH_BRANCH = 23;

    public static String LOG_OPERATION_ADD = "添加";
    public static String LOG_OPERATION_EDIT = "修改";
    public static String LOG_OPERATION_DELETE = "删除";
    public static String LOG_OPERATION_ENABLE = "变更状态";
    public static String LOG_OPERATION_IMPORT = "导入数据";

    public static String LOG_OPERATION_AUTH_SUBMIT = "认证申请";
    public static String LOG_OPERATION_AUTH_APPROVE = "认证审核";
    public static String LOG_OPERATION_AUTH_REJECT_ANGIN = "认证驳回";
    public static String LOG_OPERATION_BLOCK = "阻断";
    public static String LOG_OPERATION_PASS = "放行";

    public static String LOG_OPERATION_WHITELIST = "加入白名单";
//    public static String LOG_OPERATION_WHITELIST_DELETE = "删除白名单";

    public static String LOG_OPERATION_USER_CHANGE_PASSWORD = "变更用户密码";

    public static String LOG_RESULT_SUCCESS = "完成";
    public static String LOG_RESULT_PASS = "通过";
    public static String LOG_RESULT_REJECT = "驳回";
    public static String LOG_RESULT_FAIL = "失败";


    /**
     * t_device_model_scan
     */
    public static String MODULE_MODEL_SCAN_SOURCE = "insert_source";
    public static String MODULE_MODEL_SCAN_CONFIRM = "confirm";

    public static int MODEL_SCAN_SOURCE_MANUAL = 0;
    public static int MODEL_SCAN_SOURCE_PROTOCOL = 1;

    public static int MODEL_SCAN_CONFIRM_NO = 0;
    public static int MODEL_SCAN_CONFIRM_YES = 1;

    public static String MODULE_ROLE = "role";
    public static String MODULE_DEVICE = "device";
    public static String MODULE_DEVICE_TEMPLATE = "device_template";
    public static String MODULE_AREA = "area";
    public static String MODULE_REGION_FIRST = "region_first";
    public static String MODULE_REGION_SECOND = "region_second";
    public static String MODULE_REGION_THIRD = "region_third";
    public static String MODULE_MODULE = "module";

    public static String MODULE_SWITCHER = "switcher";

    public static String MODULE_SWITCHER_PORTAL = "portol";

    public static String INIT_DATA_TABLE = "table";

    public static String DEPARTMENT_TREE = "department_tree";
    /**
     *  sys_constant_item   dict_key
     */
    public static String MODULE_CUSTOM_FIELD_MODULE = "custom_module";

    /**
     * log_task
     */
    public static String MODULE_LOG_TASK_STATUS = "task_status";
    public static String MODULE_TASK = "task";


    public static String MODULE_AUTH_REPLY = "reply_status";

    public static int SYS_MODULE_MENU_TYPE_FOLDER = 1;
    public static int SYS_MODULE_MENU_TYPE_MENU = 2;
    public static int SYS_MODULE_MENU_TYPE_OPERATION = 3;

    public static String MENU_HOME_PATH = "/";
    /**
     * 个人中心
     */
    public static String MENU_PERSON_PATH = "/person/";

    public static String MENU_MAINTAIN_PATH = "/maintain";

    public static String MENU_ASSET_PATH = "/asset";



    /**
     * sys_msg
     */
    public static String MODULE_MSG_READED= "msg_readed";
    public static int SYS_MSG_READED_NO = 0;
    public static int SYS_MSG_READED_YES = 1;

    /**
     * 群发的收件人
     */
    public static int SYS_MSG_RECEIVER_ALL = 0;
    public static String SYS_MSG_RECEIVER_ALL_NAME = "ALL";

    /**
     * 权限
     */
    public static int MIN_PERMISSION_ID = 1000;

    /**
     * session 中的 key
     */
    public static String SESSION_KEY_USER_ID = "uid";
    public static String SESSION_KEY_PASSPORT = "passport";
    public static String SESSION_KEY_NICK_NAME = "nickname";
    public static String SESSION_KEY_TOKEN = "token";

    /**
     * 开发环境
     */
    public static String APP_ENV_LOCAL = "local";
    public static String APP_ENV_DEV = "dev";
    public static String APP_ENV_PRE = "pre";
    public static String APP_ENV_PROD = "prod";

    /**
     * t_ip_alloc.insert_db
     */
    public static String MODULE_IP_ALLOC_INSERT_STATUS = "ip_alloc_insert_status";

    public static String MODULE_IP_ALLOC_VALID_STATUS = "ip_alloc_valid_status";


    /**
     * network_project
     */
    public static String MODULE_NETWORK_TYPE = "network_project_type";
    public static String MODULE_NETWORK_STATUS = "network_project_status";

    /**
     * 设备类型，服务器
     */
    public static int DEVICE_TYPE_SERVER = 10;


    /**
     * 文件最大大小，5M
     */
    public static long UPLOAD_FILE_MAX_SIZE = 5 * 1024 * 1024L;

    /**
     * IP分配，需要写入excel 的4列，对应 IpJkVO 的4个属性名
     */
    public static String[] IP_ASSIGN_COLUMNS = {"ipAddr", "mask", "vlan", "gateway"};

    public static String MODULE_RADIUS_MAC_INSERT_TYPE = "radius_mac_insert_type";
    public static int RADIUS_MAC_INSERT_TYPE_APPROVE = 1;
    public static int RADIUS_MAC_INSERT_TYPE_SYSTEM = 2;

    public static String MODULE_DELETE = "delete";
    public static int DELETE_DONE = 1;
    public static int DELETE_NOT = 0;

    public static int BRAND_HUAWEI = 1;
    public static int BRAND_H3C = 2;
    public static int BRAND_CISCO = 3;

    public static String MODULE_NETWORK_IP_ALLOC = "network_ip_alloc";
    public static int NETWORK_IP_ALLOC_POOL = 0;
    public static int NETWORK_IP_ALLOC_RESERVE = 1;
    public static int NETWORK_IP_ALLOC_DONE = 2;


    public static int NETWORK_PROJECT_IP_ASSIGN_AGREE = 1;
    public static int NETWORK_PROJECT_IP_ASSIGN_REJECT = 0;

    /**
     * network_project.project_status
     * 0 新申请
     * 1 附件已提交
     * 2 自动校验通过，
     * 3 人工审核通过
     * 4 IP已分配
     * 5 IP分配确认
     * 6 已派单
     * 7 超期预警
     * 8 已上线
     */
    public static int NETWORK_PROJECT_STATUS_NEW = 0;
    public static int NETWORK_PROJECT_STATUS_UPLOAD = 1;
    public static int NETWORK_PROJECT_STATUS_CHECK = 2;
    public static int NETWORK_PROJECT_STATUS_APPROVE = 3;
    public static int NETWORK_PROJECT_STATUS_IP_ASSIGN = 4;
    public static int NETWORK_PROJECT_STATUS_IP_ASSIGN_CONFIRM = 5;
    public static int NETWORK_PROJECT_STATUS_DISPATCH = 6;
    public static int NETWORK_PROJECT_STATUS_OVERDUE = 7;
    public static int NETWORK_PROJECT_STATUS_COMPLETE = 8;

    public static int RADIUS_ACCESS_POLICY_BLOCK = 0;
    public static int RADIUS_ACCESS_POLICY_QZ = 1;
    public static int RADIUS_ACCESS_POLICY_PASS = 2;


    /**
     * 告警中心
     **/
    public static String MODULE_ALERT_LEVEL = "alert_level";
    public static String MODULE_ALERT_TYPE = "alert_type";
    public static String MODULE_ALERT_INDEX = "alert_index";
    public static String MODULE_DEAL_STATUS = "deal_status";
    public static String MODULE_DISPOSE_TYPE = "dispose_status";


    /**
     * 检疫区自定义列
     */
    public static String CUSTOM_COLUMN = "custom_column";

    /**
     * 构建单位
     */
    public static String BUILD_UNIT = "build_unit";


    /**
     * 程序版本
     */
    public static final String VERSION = "V2.0.7";


    /**
     * 地图缩放级别
     */
    public static final String MAP_ZOOM_LEVEL = "zoom_level";

}

