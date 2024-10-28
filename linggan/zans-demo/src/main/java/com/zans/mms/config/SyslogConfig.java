package com.zans.mms.config;

import com.zans.mms.syslog.SyslogServerEventHandler;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.server.*;
import org.productivity.java.syslog4j.server.impl.net.udp.UDPNetSyslogServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.Snmp;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.SocketAddress;

/**
 * syslog config
 * @author xv
 * @since 2022/6/28 15:37
 */
//@Configuration
public class SyslogConfig {

    /**
     * 监听IP和端口
     */
    @Value("${syslog.listen.port}")
    private Integer port;

    private static Logger log = LoggerFactory.getLogger("syslog");

    @Autowired
    private SyslogServerSessionEventHandlerIF syslogServerEventHandler;

    @Bean
    public void initSyslog() throws Exception {

        log.info("initSyslog");
        new Thread(() -> {
            // 服务端
            SyslogServerIF serverInstance = SyslogServer.getInstance(SyslogConstants.UDP);
            UDPNetSyslogServerConfig serverConfig = (UDPNetSyslogServerConfig) serverInstance.getConfig();
            serverConfig.setHost("192.168.6.150");
            serverConfig.setPort(port);
            // 防止数据过大被截取导致不完整
//            serverConfig.setMaxMessageSize(SyslogConstants.SYSLOG_BUFFER_SIZE * 10);
            serverConfig.addEventHandler(syslogServerEventHandler);
            // 启动服务端
            serverInstance.run();
        }).start();
    }
}
