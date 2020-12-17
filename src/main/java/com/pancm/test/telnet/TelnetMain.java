package com.pancm.test.telnet;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/12/1
 */
public class TelnetMain {

    private TelnetClient telnet = new TelnetClient("VT100");

    private InputStream in;

    private PrintStream out;

    private static final String DEFAULT_AIX_PROMPT = "#";
    private static final String ENTER_COMMAND_ARROW = ">";
    private static final String ENTER_COMMAND_ARROW2 = ".";
    private static final String ENTER_COMMAND_ARROW3 = "<";
    private static final String ENTER_COMMAND_BRACKETS = "]";
    private static final String ENTER="\n";


    /**
     * telnet 端口
     */
    private String port;

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

    public TelnetMain(String ip, String user, String password) {
        this.ip = ip;
        this.port = String.valueOf(23);
        this.user = user;
        this.password = password;
    }

    public TelnetMain(String ip, String port, String user, String password) {
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

            telnet.connect(ip, Integer.parseInt(port));
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            telnet.setKeepAlive(true);
            write(password);
            String msg=readUntil(DEFAULT_AIX_PROMPT);
            System.out.println(msg);
            write("dis mac-address");
            msg=readUntil(DEFAULT_AIX_PROMPT);
            System.out.println(msg);
            msg=readUntil(DEFAULT_AIX_PROMPT);
            System.out.println(msg);
            write("display interface ");
            msg=readUntil(DEFAULT_AIX_PROMPT);
            System.out.println(msg);
            msg=readUntil(DEFAULT_AIX_PROMPT);
            System.out.println(msg);

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

//            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
//            StringBuffer sb=new StringBuffer();
//            while(reader.ready()){
//                sb.append((char)reader.read());
//            }
//            return  sb.toString();
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();

            while (true) {
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
        try {
            write(command);
            return readUntil(DEFAULT_AIX_PROMPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static void main(String[] args) {
        try {
            TelnetMain telnet = new TelnetMain("192.168.8.12", "admin1", "admin111");
//            telnet.sendCommand("dis mac-address");
            telnet.connect();
            System.setOut(new PrintStream("D:/telnet.txt"));
            telnet.disconnect();
            System.out.println("结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
