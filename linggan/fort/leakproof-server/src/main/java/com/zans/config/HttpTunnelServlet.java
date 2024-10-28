package com.zans.config;

import com.zans.service.IFortServerConfigService;
import com.zans.vo.FortServerConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.servlet.GuacamoleHTTPTunnelServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;


@WebServlet(urlPatterns = "/tunnel")
@Slf4j
public class HttpTunnelServlet extends GuacamoleHTTPTunnelServlet {


    private ApplicationConfig applicationConfig = (ApplicationConfig) SpringBeanFactory.getBean("applicationConfig");


    private IFortServerConfigService fortServerConfigService = (IFortServerConfigService) SpringBeanFactory.getBean("fortServerConfigService");

	/**
	 *
	 */
	private static final long serialVersionUID = -3224942386695394818L;

	@Override
	protected GuacamoleTunnel doConnect(HttpServletRequest request) throws GuacamoleException {
        log.info("request req:{}",request.getParameterMap());
        String hostname = applicationConfig.getHostname(); //guacamole server地址
        int port = applicationConfig.getPort(); //guacamole server端口
        String ip = request.getParameter("ip");
        FortServerConfigVO fortServerConfigVO = fortServerConfigService.queryByIp(ip);
        if(fortServerConfigVO == null){
            log.error("该ip:{}未配置！",ip);
            throw new GuacamoleException("not found ip:"+ip);
        }
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol("rdp"); // 远程连接协议
        configuration.setParameter("hostname", fortServerConfigVO.getServerIp());
        configuration.setParameter("port", fortServerConfigVO.getServerPort());
        configuration.setParameter("username", fortServerConfigVO.getServerAccount());
        configuration.setParameter("password", fortServerConfigVO.getServerPwd());
        configuration.setParameter("ignore-cert", "true");

        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket(hostname, port),
                configuration
        );

        GuacamoleTunnel tunnel = new SimpleGuacamoleTunnel(socket);
        return tunnel;
	}
}
