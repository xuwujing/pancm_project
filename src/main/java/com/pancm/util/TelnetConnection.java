package com.pancm.util;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;

/**
 * @author XUQIANG
 *
 */
public class TelnetConnection {




    private TelnetClient telnet = null;

    private String prompt = "#$>]";
    private String loginPrompt = "login";

    private String usernamePrompt = "Username:";
    private String passwordPrompt = "Password:";


    private InputStream in;
    private PrintStream out;

    public TelnetConnection(String host, int port) {
        if(telnet == null) {
            telnet = new TelnetClient();
            try {
                telnet.connect(host, port);
                in = telnet.getInputStream();
                out = new PrintStream(telnet.getOutputStream());
            } catch (SocketException e) {
//                log.logForException(TelnetConnection.class, e.getMessage());
            } catch (IOException e) {
//                log.logForException(TelnetConnection.class, e.getMessage());
            }
        }
    }

    /**
     * 登录到远程机器中<br>
     * 说明：在登录前，先确认输入用户名的提示符，如果不是Username：，需要设置该值，使用setUsernamePrompt(prompt)；<br>
     *       第二，确认输入密码时的提示符，如果不是Password：，需要设置该值,使用setPasswordPrompt(prompt)；<br>
     *       第三，确认登录后查看是否有登录后的提示信息：如：%Apr 17 04:26:32:256 2000 Quidway SHELL/5/LOGIN:- 1 - admin(191.168.2.227) in unit1 login <br>
     *             如果末尾不是login,需要指定最后一个单词，使用setLoginPrompt(prompt)。
     如果没有登录提示，设置setLoginPrompt(null);
     *       第四，执行命令时，如果提示符不是 #、$、>、]中的一个，也需要指定最后一个符号，使用setPrompt(prompt).
     */
    public void login(String username, String password, String prompt) {
        //处理命令行的提示字符
        if(prompt != null && !"".equals(prompt)) {
            this.prompt = prompt;
        }
        readUntil(this.usernamePrompt);
        write(username);
        readUntil(this.passwordPrompt);
        write(password);
        readUntil(this.prompt);
        if(this.loginPrompt != null)
        readUntil(this.loginPrompt);
    }


    /** * 读取分析结果 * * @param pattern * @return */
    public String readUntil(String pattern) {
        StringBuffer sb = new StringBuffer();

        try {
            int len = 0;
            while((len = in.read()) != -1) {
                sb.append((char)len);
                if(pattern.indexOf((char)len) != -1 || sb.toString().endsWith(pattern)) {
                    return sb.toString();
                }
            }
        } catch (IOException e) {
//            log.logForException(TelnetConnection.class, e.getMessage());
        }

        return "";
    }


    /** * 写操作 * * @param value */
    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
//            log.logForException(TelnetConnection.class, e.getMessage());
        }
    }

    /** * 向目标发送命令字符串 * * @param command * @return */
    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil(prompt + "");
        } catch (Exception e) {
//            log.logForException(TelnetConnection.class, e.getMessage());
        }
        return "";
    }

    /** * 关闭连接 */
    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
//            log.logForException(TelnetConnection.class, e.getMessage());
        }
    }

    /**
     * @return the prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * @param prompt the prompt to set
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * @return the usernamePrompt
     */
    public String getUsernamePrompt() {
        return usernamePrompt;
    }

    /**
     * @param usernamePrompt the usernamePrompt to set
     */
    public void setUsernamePrompt(String usernamePrompt) {
        this.usernamePrompt = usernamePrompt;
    }

    /**
     * @return the passwordPrompt
     */
    public String getPasswordPrompt() {
        return passwordPrompt;
    }

    /**
     * @param passwordPrompt the passwordPrompt to set
     */
    public void setPasswordPrompt(String passwordPrompt) {
        this.passwordPrompt = passwordPrompt;
    }

    /**
     * @return the loginPrompt
     */
    public String getLoginPrompt() {
        return loginPrompt;
    }

    /**
     * @param loginPrompt the loginPrompt to set
     */
    public void setLoginPrompt(String loginPrompt) {
        this.loginPrompt = loginPrompt;
    }

    /**
     * 关闭打开的连接
     * @param telnet
     */
    public void close(TelnetClient telnet) {
        if(telnet != null) {
            try {
                telnet.disconnect();
            } catch (IOException e) {
//                log.logForException(TelnetConnection.class, e.getMessage());
            }
        }

        if(this.telnet != null) {
            try {
                this.telnet.disconnect();
            } catch (IOException e) {
//                log.logForException(TelnetConnection.class, e.getMessage());
            }
        }
    }



    public static void main(String[] args) {
        try {
            System.out.println("启动Telnet...");
            String ip = "192.168.8.12";
            int port = 23;
            String user = "admin1";
            String password = "admin111";
            TelnetConnection telnet = new TelnetConnection(ip, port);
            telnet.setPrompt("<lab2>");
            telnet.login(user, password, "");
            telnet.setPrompt("[lab2]");
            String r1 = telnet.sendCommand("dis mac-address");//display snmp-agent local-engineid
            System.out.println("显示结果");
            System.out.println(r1);


            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
