package com.zans.portal.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;


@Slf4j
public class TelnetUtil {

    private TelnetClient telnet = new TelnetClient("VT100");

    private InputStream in;

    private PrintStream out;

    private static final String DEFAULT_AIX_PROMPT = "#";
    private static final String ENTER_COMMAND_ARROW = ">";
    private static final String ENTER_COMMAND_BRACKETS = "]";
    private static final String ENTER = "\n";

    private static final int READ_TIME_OUT = 2;


    /**
     * telnet 端口
     */
    private int port;

    /**
     * 用户名
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    /**
     * IP 地址
     */
    private String ip;

    public TelnetUtil(String ip, String user, String password) {
        this.ip = ip;
        this.port = 23;
        this.user = user;
        this.password = password;
    }

    public TelnetUtil(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }


    public boolean test(String mac) {
        boolean isConnect = true;
        try {
            telnet.connect(ip, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            telnet.setKeepAlive(true);
            String s = readUntil("Username:");
            write(user);
            System.out.println("1:"+s);
            s =  readUntil("Password:");
            write(password);
            System.out.println("2:"+s);
            String msg = readUntil(ENTER_COMMAND_ARROW);
            System.out.print("3:"+msg);

            msg = sendSysCommand("system-view");
            System.out.print("4:"+msg);

            msg = sendSysCommand("dis mac-address " + mac);
            System.out.print("5:"+msg);

            msg = sendCommand("quit");
            System.out.print("6:"+msg);

//            msg = sendCommand("save \n y");
//            System.out.print("7:"+msg);
        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();
            return isConnect;
        }
        return isConnect;

    }

    public boolean login() {
        try {
            telnet.connect(ip, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            telnet.setKeepAlive(true);
            telnet.setConnectTimeout(2000);
            String s = readUntil("Username:");
            write(user);
            /// 这里打印密码是不太安全的，现因排查效率，先加上，后续稳定之后再删除
            log.info("ip:{},账号:{},密码:{}.访问信息:{}",ip,user,password,s);
            s = readUntil("Password:");
            write(password);
            log.info("账号:{},密码:{}.登录访问信息:{}",user,password,s);
            readUntil(ENTER_COMMAND_ARROW);
            return true;
        } catch (IOException e) {
            log.error("登录失败！登录ip:{},账号:{},密码:{}",ip,user,password);
        }
        return false;
    }




    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            long startTime = System.currentTimeMillis();
            while (true) {
                //System.out.print(ch);// ---需要注释掉
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                if ((System.currentTimeMillis() - startTime) > (READ_TIME_OUT * 1000)) {
                    log.info("账号:{},执行命令访问超时!",user);
                    return sb.toString();
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            log.error("读取命令字符串失败!截取字符串:{],原因是:", pattern, e);
        }
        return null;
    }

    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            log.error("写入失败!写入值:{},", value, e);
        }
    }

    public String sendCommand(String command) {
        write(command);
        return readUntil(ENTER_COMMAND_ARROW);
    }

    public String sendSysCommand(String command) {
        write(command);
        return readUntil(ENTER_COMMAND_BRACKETS);
    }


    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
           log.error("断开连接失败!");
        }
    }

    /**
     * 测试telnet 机器端口的连通性
     *
     * @param hostname
     * @param port
     * @param timeout
     * @return
     */
    public static boolean telnet(String hostname, int port, int timeout) {
        Socket socket = new Socket();
        boolean isConnected = false;
        try {
            socket.connect(new InetSocketAddress(hostname, port), timeout); // 建立连接
            isConnected = socket.isConnected(); // 通过现有方法查看连通状态
        } catch (IOException e) {
            log.error("连接失败！连接地址:{},原因是:{}",hostname,e);
        } finally {
            try {
                socket.close();   // 关闭连接
            } catch (IOException e) {
                log.error("连接失败！连接地址:{},原因是:{}",hostname,e);
            }
        }
        return isConnected;
    }


    public static void main(String[] args) {
        try {
            System.out.println("启动Telnet...");
            String host = "27.23.254.254";
            int port = 23;
            String account = "whsjgj";
            String password = "Whsjgj@2017";

            host = "192.168.8.12";
            account = "admin1";
            password = "admin111";




            boolean telnet1 = telnet(host, port, 5000);
            System.out.println(telnet1);
            TelnetUtil telnet = new TelnetUtil(host, port, account, password);
            String mac = "4ced-fbc6-0893";
//            telnet.test(mac);

            boolean flag = telnet.login();
            System.out.println("登录状态:"+flag);
            telnet.sendSysCommand("system-view");
            String command = "dis mac-address "+ mac;
            String msg1 = telnet.sendSysCommand(command);
            System.out.println("返回结果:"+msg1);
            String quitCommand = "quit";
            String msg2 = telnet.sendCommand(quitCommand);
            System.out.println("返回结果2:"+msg2);
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
