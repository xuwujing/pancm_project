package com.zans.config;

import com.zans.dao.FortServerConfigDao;
import com.zans.vo.FortServerConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.zans.service.impl.FortReserveServiceImpl.isConnect;

//@Component
@Slf4j
public class GuacamoleInitConfig {

    @Resource
    private FortServerConfigDao fortServerConfigDao;

    @Value("${guacamole.hostname:192.168.2.20}")
    private String hostname;

    @Value("${guacamole.port:4822}")
    private Integer guacamolePort;

    @PostConstruct
    public void guacamoleConfig() {
        int i = Runtime.getRuntime().availableProcessors();//CPU
        final ThreadPoolExecutor tpe = new ThreadPoolExecutor(i, i, 3,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
        List<FortServerConfigVO> fortServerConfigs = fortServerConfigDao.queryAll(new FortServerConfigVO());
        log.info("fortServerConfigSize:{}", fortServerConfigs.size());
        for (FortServerConfigVO fortServerConfig : fortServerConfigs) {
            tpe.submit(() -> {
                Integer port = 0;
                try {
                    port = Integer.parseInt(fortServerConfig.getServerPort());
                } catch (Exception e) {
                    log.info("serverPort:{}-error:{}", fortServerConfig.getServerPort(), e);
                }
                try {
                    if (isConnect(fortServerConfig.getServerIp(), port)) {
                        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                                new InetGuacamoleSocket(hostname, guacamolePort),
                                getConfiguration(fortServerConfig),
                                getInformation()
                        );
                        new SimpleGuacamoleTunnel(socket);
                        log.info(fortServerConfig.getServerIp() + "已初始化启动");
                    }
                } catch (GuacamoleException e) {
                    log.info("guacamoleError-serverHost:{},error:{}", fortServerConfig.getServerIp(),e);
                }
            });
        }
        if (!tpe.isShutdown()) {
            tpe.shutdown();
        }
    }

    public GuacamoleClientInformation getInformation() {
        GuacamoleClientInformation guacamoleClientInformation = new GuacamoleClientInformation();
        guacamoleClientInformation.setOptimalScreenHeight(768);
        guacamoleClientInformation.setOptimalScreenWidth(1024);
        return guacamoleClientInformation;
    }

    public GuacamoleConfiguration getConfiguration(FortServerConfigVO fortServerConfig) {
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol("rdp"); // 远程连接协议
        configuration.setParameter("hostname", fortServerConfig.getServerIp());
        configuration.setParameter("port", fortServerConfig.getServerPort());
        configuration.setParameter("username", fortServerConfig.getServerAccount());
        configuration.setParameter("password", fortServerConfig.getServerPwd());
        configuration.setParameter("ignore-cert", "true");
        return configuration;
    }

}
