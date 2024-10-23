package com.zans.config;

import com.zans.commons.config.ConnectionManager;
import com.zans.commons.config.GetProperties;
import com.zans.commons.contants.Constants;
import com.zans.commons.utils.AESUtil;
import com.zans.commons.utils.HttpClientUtil;
import com.zans.commons.utils.MyTools;
import com.zans.dao.AlertDao;
import com.zans.dao.impl.AlertDaoImpl;
import com.zans.pojo.AlertServerBean;
import com.zans.task.AlertRecoverManageTask;
import com.zans.task.RefreshConfigTask;
import com.zans.vo.ApiResult;
import com.zans.vo.node.NodeRegVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zans.commons.contants.Constants.MYSQL_ALERT;
import static com.zans.commons.contants.Constants.MYSQL_JOB;
import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title:
 * @Description: 启动之后初始化
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/1
 */
@Component
@Slf4j
public class Runner implements CommandLineRunner {

    @Value("${app.name}")
    private String name;
    @Value("${app.version}")
    private String version;
    @Value("${app.node_id}")
    private String node_id;
    @Value("${app.node_type}")
    private String node_type;
    @Value("${server.port}")
    private int port;
    @Value("${app.remote-server-ip}")
    private String remote_ip;
    @Value("${app.remote-server-port}")
    private String remote_port;
    @Value("${app.remote-server-context}")
    private String remote_context;

    @Value("${app.node_local:}")
    private String node_local;


    private AlertDao dbDao;


    @Override
    public void run(String... args){
        initProperties();
        initServer();
        register();
    }

    /**
     * @return void
     * @Author pancm
     * @Description 配置初始化
     * @Date 2020/9/1
     * @Param []
     **/
    private void initProperties() {
        mysqlCache.put(MYSQL_ALERT, GetProperties.getAppSettings());
        mysqlCache.put(MYSQL_JOB, GetProperties.getAppSettings());
        REMOTE_IP = remote_ip;
        REMOTE_PORT = remote_port;
        REMOTE_CONTEXT = remote_context;
        NODE_ID = node_id;

        SLEEP_TIME_RECOVER = Long.valueOf(GetProperties.getAppSettings().getOrDefault("recover.time", "60"));
        SLEEP_TIME_AGG = Long.valueOf(GetProperties.getAppSettings().getOrDefault("agg.time", "60"));
        SLEEP_TIME_QUEUE = Long.valueOf(GetProperties.getAppSettings().getOrDefault("pull.queue.time", "60"));
        AES_KEY = GetProperties.getAppSettings().getOrDefault("aes.key", "Lgwy@1234!@#$%^&");
        new ConnectionManager(MYSQL_ALERT);
        new ConnectionManager(MYSQL_JOB);
    }

    /**
     * @return void
     * @Author pancm
     * @Description 服务初始化
     * @Date 2020/9/1
     * @Param []
     **/
    private void initServer() {
        dbDao = new AlertDaoImpl();
        try {
            List<Map<String, Object>> alertServerList = dbDao.queryAlertServer();
            if (MyTools.isEmpty(alertServerList)) {
                log.error("告警服务配置表无数据！不启动本服务！");
                System.exit(0);
            }
            log.info("alert_server:{}", alertServerList);
            //将告警服务的信息进行初始化并添加到缓存中
            for (int i = 0; i < alertServerList.size(); i++) {
                Map<String, Object> map = alertServerList.get(i);
                AlertServerBean alertServerBean = MyTools.toBean(MyTools.toString(map), AlertServerBean.class);
                int type = alertServerBean.getServer_type();
                if (type == 0) {
                    Map<String, String> stringMap = new HashMap<>();
                    String serverName = alertServerBean.getServer_name();
                    stringMap.put(getMySqlName(serverName, USER_NAME), alertServerBean.getServer_account());
                    // 2021-2-20 数据库密码进行解密
                    String pwd = alertServerBean.getServer_pwd();
                    String de_pwd = AESUtil.desEncrypt(pwd,AES_KEY,AES_KEY).trim();
                    if(MyTools.isEmpty(de_pwd)){
                        de_pwd = pwd;
                    }
                    stringMap.put(getMySqlName(serverName, PWD_NAME), de_pwd);

                    stringMap.put(getMySqlName(serverName, URL_NAME), alertServerBean.getServer_url());
                    stringMap.put(getMySqlName(serverName, DRIVER_CLASSNAME), DRIVER);
                    mysqlCache.put(alertServerBean.getServer_name(), stringMap);
                    new ConnectionManager(serverName);
                } else if (type == 1) {
                    ES_IP = alertServerBean.getServer_ip();
                    ES_PORT = alertServerBean.getServer_port();
                }
                alertServerCache.put(alertServerBean.getServer_id(), alertServerBean);
            }
//            delTmp();
            new RefreshConfigTask().startTask();
            new AlertRecoverManageTask().start();
        } catch (Exception e) {
            log.error("初始化服务配置异常:", e);
        }
    }

    /**
     * @return void
     * @Author pancm
     * @Description 定期删除临时表数据
     * @Date 2020/9/9
     * @Param []
     **/
    private void delTmp() {
        Runnable runnable = () -> {
            String delTmpSql = String.format(DEL_SQL_TMP, DEL_DAY);
            try {
                dbDao.executeUpdate(delTmpSql);
                log.info("删除{}天前的 {} 表数据成功！", DEL_DAY, TABLE_ALERT_RULE_RECORD_TMP);
            } catch (Exception e) {
                log.error("临时表数据删除失败！删除语句:{},原因:", delTmpSql, e);
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.DAYS);
    }


    /**
     * @return void
     * @Author pancm
     * @Description 服务注册
     * @Date 2020/9/2
     * @Param []
     **/
    private void register() {
        NodeRegVO nodeRegVO = new NodeRegVO();
        try {
            if (MyTools.isEmpty(node_local)) {
                nodeRegVO.setIp(MyTools.getLocalIP());
            } else {
                nodeRegVO.setIp(node_local);
            }
            nodeRegVO.setName(name);
            nodeRegVO.setNodeId(node_id);
            nodeRegVO.setNodeType(node_type);
            nodeRegVO.setPort(port);
            nodeRegVO.setBuildVersion(version);
            log.info("注册地址:{},数据:{}", getRegisterUrl(), MyTools.toString(nodeRegVO));
            String data = null;

            data = HttpClientUtil.post(getRegisterUrl(), MyTools.toString(nodeRegVO));
            ApiResult apiResult = MyTools.toBean(data, ApiResult.class);
            if (apiResult.getCode() != 0) {
                log.error("注册失败！程序退出！原因是:{}", apiResult.getStackTrace());
                System.exit(0);
                return;
            }
        } catch (Exception e) {
            log.error("注册失败！程序退出！原因是:{}", e);
            System.exit(0);
            return;
        }
        log.info("注册:{} 服务成功！", remote_ip);
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取注册服务地址
     * @Date 2020/9/3
     * @Param []
     **/
    private String getRegisterUrl() {
        String url = HTTP + remote_ip + Constants.TIME_SEPARATOR + remote_port + Constants.SLASH_SIGN + remote_context + REG_URI;
        return url;
    }


    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取mysql名称
     * @Date 2020/9/2
     * @Param [name, name2]
     **/
    private String getMySqlName(String name, String name2) {
        return name.concat(name2);
    }
}
