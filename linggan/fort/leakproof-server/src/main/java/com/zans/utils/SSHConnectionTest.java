package com.zans.utils;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;

/**
 * @author xianlei
 * @desc SSHConnectionTest.java
 * @date 2018年3月29日下午5:53:24
 */
@Slf4j
public class SSHConnectionTest {
    //远程主机的ip地址
    private String ip;
    //远程主机登录用户名
    private String username;
    //远程主机的登录密码
    private String password;
    //设置ssh连接的远程端口
    public static final int DEFAULT_SSH_PORT = 22;
    //保存输出内容的容器
    private ArrayList<String> stdout;

    class MyUserInfo implements UserInfo {

        @Override
        public String getPassphrase() {
            // TODO Auto-generated method stub
            log.info("MyUserInfo.getPassphrase()");
            return null;
        }

        @Override
        public String getPassword() {
            // TODO Auto-generated method stub
            log.info("MyUserInfo.getPassword()");
            return null;
        }

        @Override
        public boolean promptPassphrase(String arg0) {
            // TODO Auto-generated method stub
            log.info("MyUserInfo.promptPassphrase()");
            log.info(arg0);
            return false;
        }

        @Override
        public boolean promptPassword(String arg0) {
            // TODO Auto-generated method stub
            log.info("MyUserInfo.promptPassword()");
            log.info(arg0);
            return false;
        }

        @Override
        public boolean promptYesNo(String arg0) {
            // TODO Auto-generated method stub'
            log.info("MyUserInfo.promptYesNo()");
            log.info(arg0);
            if (arg0.contains("The authenticity of host")) {
                return true;
            }
            return true;
        }

        @Override
        public void showMessage(String arg0) {
            // TODO Auto-generated method stub
            log.info("MyUserInfo.showMessage()");
        }

    }

    /**
     * 初始化登录信息
     *
     * @param ip
     * @param username
     * @param password
     */
    public SSHConnectionTest(final String ip, final String username, final String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        stdout = new ArrayList<String>();
    }

    /**
     * 执行shell命令
     *
     * @param command
     * @return
     */
    public int execute(final String command) throws JSchException, IOException {
        int returnCode = 0;
        JSch jsch = new JSch();
        MyUserInfo userInfo = new MyUserInfo();
        //创建session并且打开连接，因为创建session之后要主动打开连接
        Session session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.setUserInfo(userInfo);
        session.connect();

        //begin
//        Channel shell = session.openChannel("shell");
//        ChannelShell channelShell = (ChannelShell) shell;
//        channelShell.connect();
////        InputStream inputStream = channelShell.getInputStream();
//        OutputStream outputStream = channelShell.getOutputStream();
//        String cmd = command;
//        outputStream.write(cmd.getBytes());
//        outputStream.flush();
        //end

//        Channel channel = session.openChannel("shell");
        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setCommand(command);

        channelExec.setInputStream(null);
        BufferedReader input = new BufferedReader(new InputStreamReader
                (channelExec.getInputStream()));

        channelExec.connect();

        log.info("The remote command is :" + command);

        //接收远程服务器执行命令的结果
        String line;
        while ((line = input.readLine()) != null) {
            stdout.add(line);
        }
        input.close();

        // 得到returnCode
        if (channelExec.isClosed()) {
            returnCode = channelExec.getExitStatus();
        }
        // 关闭通道
        channelExec.disconnect();
        //关闭session
        session.disconnect();
        return returnCode;
    }

    /**
     * get stdout
     *
     * @return
     */
    public ArrayList<String> getStandardOutput() {
        return stdout;
    }

    public static void main(final String[] args) {
        String password = "lgwy@2020";
        String username = "root";
        String ip = "192.168.10.32";
        long l = System.currentTimeMillis();
//        String password = "root1213";
//        String username = "root";
//        String ip = "192.168.9.40";
        SSHConnectionTest shell = new SSHConnectionTest(ip, username, password);
        try {
            shell.execute("ping 192.168.10.32 -c3");
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> stdout = shell.getStandardOutput();
        for (String str : stdout) {
            log.info(str);
        }
        long l1 = System.currentTimeMillis();
        log.info("time:{}",l1 - l);
    }
}
