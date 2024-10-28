package com.zans.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author xv
 * @since 2020/4/17 15:32
 */
@Slf4j
public class TelnetHelper {

    private TelnetClient telnet = new TelnetClient("VT100");

    private InputStream in;

    private PrintStream out;

    private static final String DEFAULT_AIX_PROMPT = "#";
    private static final String ENTER_COMMAND_ARROW = ">";
    private static final String ENTER_COMMAND_BRACKETS = "]";
    private static final String ENTER="\n";


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

    public TelnetHelper(String ip, String user, String password) {
        this.ip = ip;
        this.port = 23;
        this.user = user;
        this.password = password;
    }

    public TelnetHelper(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * @return boolean 连接成功返回true，否则返回false
     */
    private boolean connect() {

        boolean isConnect = true;

        try {

            telnet.connect(ip, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            telnet.setKeepAlive(true);
            String s = readUntil("Username:");
            write(user);
            System.out.println(s);
            s =  readUntil("Password:");
            write(password);
            System.out.println(s);
            String msg = readUntil(ENTER_COMMAND_ARROW);
            System.out.print(msg);

            msg = sendSysCommand("system-view");
            System.out.print(msg);

            msg = sendSysCommand("display mac-address blackhole");
            System.out.print(msg);

        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();
            return isConnect;
        }
        return isConnect;
    }

    public boolean doBlackHole(String mac) {
        boolean isConnect = true;
        try {
            telnet.connect(ip, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            telnet.setKeepAlive(true);
            String s = readUntil("Username:");
            write(user);
            System.out.println(s);
            s =  readUntil("Password:");
            write(password);
            System.out.println(s);
            String msg = readUntil(ENTER_COMMAND_ARROW);
            System.out.print(msg);

            msg = sendSysCommand("system-view");
            System.out.print(msg);

            msg = sendSysCommand("mac-address blackhole " + mac);
            System.out.print(msg);

            msg = sendCommand("quit");
            System.out.print(msg);

            msg = sendCommand("save \n y");
            System.out.print(msg);
        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();
            return isConnect;
        }
        return isConnect;

    }

    public boolean undoBlackHole(String mac) {
        boolean isConnect = true;
        try {
            telnet.connect(ip, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            telnet.setKeepAlive(true);
            String s = readUntil("Username:");
            write(user);
            System.out.println(s);
            s =  readUntil("Password:");
            write(password);
            System.out.println(s);
            String msg = readUntil(ENTER_COMMAND_ARROW);
            System.out.print(msg);

            msg = sendSysCommand("system-view");
            System.out.print(msg);

            msg = sendSysCommand("undo mac-address blackhole " + mac);
            System.out.print(msg);

            msg = sendCommand("quit");
            System.out.print(msg);

            msg = sendCommand("save \n y");
            System.out.print(msg);
        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();
            return isConnect;
        }
        return isConnect;

    }

    public void su(String user, String password) {
        try {
            write("su" + " - " + user);
            readUntil("Password:");
            write(password);
            readUntil(DEFAULT_AIX_PROMPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                //System.out.print(ch);// ---需要注释掉
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
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


    private void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNowDate() {
        this.connect();
        String nowDate = this.sendCommand("date|awk '{print $2,$3,$4}'");
        String[] temp = nowDate.split("\r\n");
        // 去除命令字符串
        if (temp.length > 1) {
            nowDate = temp[0];
        } else {
            nowDate = "";
        }
        this.disconnect();
        return nowDate;
    }

    /**
     * 测试telnet 机器端口的连通性
     * @param hostname
     * @param port
     * @param timeout
     * @return
     */
    public static boolean telnet(String hostname, int port, int timeout){
        Socket socket = new Socket();
        boolean isConnected = false;
        try {
            socket.connect(new InetSocketAddress(hostname, port), timeout); // 建立连接
            isConnected = socket.isConnected(); // 通过现有方法查看连通状态
//            System.out.println(isConnected);    // true为连通
        } catch (IOException e) {
            System.out.println("false");        // 当连不通时，直接抛异常，异常捕获即可
        }finally{
            try {
                socket.close();   // 关闭连接
            } catch (IOException e) {
                System.out.println("false");
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


            host = "27.12.254.254";
            account = "admin";
            password = "whjgj@2017";

            host = "27.11.254.254";
            account = "admin";
            password = "Whjgj@2017";

            boolean telnet1 = telnet(host, port, 5000);
            System.out.println(telnet1);

            TelnetHelper telnet = new TelnetHelper(host, port, account, password);
//            System.setOut(new PrintStream("d:/telnet.txt"));
//            telnet.connect();
            String mac = "aaaa-aaaa-aaaa";
//            telnet.doBlackHole(mac);
            boolean b = telnet.undoBlackHole(mac);
            System.out.println(b);
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
