package com.zans.mms.syslog;

import lombok.extern.slf4j.Slf4j;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.SyslogServerSessionEventHandlerIF;
import org.productivity.java.syslog4j.util.SyslogUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zans.mms.config.DemoKitConstants.*;

/**
 * syslog udp server
 * @author xv
 * @since 2022/6/28 16:35
 */
@Service
@Slf4j
public class SyslogServerEventHandler implements SyslogServerSessionEventHandlerIF {

    @Value("${syslog.filter.keyword}")
    private String filterKeyword;

    @Value("${syslog.filter.ip}")
    private String filterIp;

    @Override
    public void event(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
        //判断传输时间是否存在，不存在将现在的时间设置为传输时间
        String date = (event.getDate() == null ? new Date() : event.getDate()).toString();
        //将解析日志的生成端,<<3是要该数左移动三位计算
        String facility = SyslogUtility.getFacilityString(event.getFacility()<<3);
        //讲解析日志的级别，级别越大越低
        String level = SyslogUtility.getLevelString(event.getLevel());
        //获取当前的源设备IP
        String sourceIp = getIPAddress(socketAddress.toString());
        //获取到信息主体
        String msg = this.convertMessage(event.getMessage());

        if (!filterIp.equals(sourceIp)) {
//            log.info("接收到日志：ip#{}, ignore", sourceIP);
            return;
        }
//        System.out.println(msg);

        if (!msg.contains(filterKeyword)) {
//            log.info("接收到日志：ip#{}, ignore msg filterKeyword, {}", sourceIP, msg);
            return;
        }

        if (!msg.contains(INTERFACE_TURN)) {
//            log.info("接收到日志：ip#{}, ignore msg INTERFACE_TURN, {}", sourceIP, msg);
            return;
        }

        int idxBegin = msg.indexOf(filterKeyword);
        String prefix = msg.substring(idxBegin + filterKeyword.length());
        int idxEnd = prefix.indexOf(" ");
        String itf = prefix.substring(0, idxEnd);

        // TODO add business logic, to send websocket msg to front
        if (msg.contains(INTERFACE_UP)) {
            log.info("接收到日志：interface#{} UP, {}", itf, msg);
        } else if (msg.contains(INTERFACE_DOWN)) {
            log.info("接收到日志：interface#{} DOWN, {}", itf, msg);
        }

    }
    public String convertMessage(String message) {
        try {
            return new String(message.getBytes(),"UTF-8");
        } catch (Exception ex) {
            return "";
        }

    }

    /**
     * 获取到该字符串里的ip地址
     * @param bString
     * @return
     */
    private String getIPAddress(String bString) {
        String regEx="((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(bString);
        String result = "";
        while (m.find()) {
            result=m.group();
            break;
        }
        return result;
    }


    @Override
    public void initialize(SyslogServerIF syslogServer) {}
    @Override
    public void destroy(SyslogServerIF syslogServer) {}
    @Override
    public Object sessionOpened(SyslogServerIF syslogServer, SocketAddress socketAddress) {return null;}
    @Override
    public void exception(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress,Exception exception) {}
    @Override
    public void sessionClosed(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress,boolean timeout) {}
}

