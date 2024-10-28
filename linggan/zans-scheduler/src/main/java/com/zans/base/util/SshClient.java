package com.zans.base.util;

import com.jcraft.jsch.*;
import com.zans.job.service.impl.UpgradeServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xv
 * @since 2020/4/17 17:06
 */
@Slf4j
public class SshClient {

    private static Session getSession(String ip, String username, String password, Integer port, boolean debug) {
        Session session = null;
        JSch jSch = new JSch();
        if (debug) {
            JSch.setLogger(new JschLogger());
        }
        try {
            if (port != null) {
                session = jSch.getSession(username, ip, port.intValue());
            } else {
                session = jSch.getSession(username, ip);
            }
            //关闭主机密钥检查，否则会导致连接失败
            session.setConfig("StrictHostKeyChecking", "no");

            if (password != null) {
                session.setPassword(password);
            }
            //打开会话，并设置超时时间
            session.setTimeout(60 * 1000);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }

    /**
     * 获取sftp连接
     */
    public static ChannelSftp getChannelSftp(Session session) {
        ChannelSftp channel = null;
        try {
            //打开通道，设置通道类型
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static Boolean upload(String ip, String username, String password, Integer port,
                                 String srcPath, String destPath) {
        Boolean flag = false;
        Session session = getSession(ip, username, password, port, false);
        ChannelSftp channelSftp = getChannelSftp(session);
        try {
            InputStream inputStream = new FileInputStream(srcPath);
            //上传
            channelSftp.put(inputStream,destPath);
            channelSftp.quit();
            channelSftp.exit();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channelSftp.disconnect();
            session.disconnect();
        }
        return flag;
    }


    /**
     * 打开exec通道
     */
    public static ChannelShell getChannelShell(Session session) {
        ChannelShell channel = null;
        try {
            //设置通道类型
            channel = (ChannelShell) session.openChannel("shell");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static boolean doBlackHole(String host, String account, String password, Integer port, String mac) {
        String[] blackHoleCommands = {"system-view",  "mac-address blackhole " + mac, "quit", "quit"};
        String msg = execShell(host, account, password, port, blackHoleCommands, true);
        return (msg != null && msg.contains(blackHoleCommands[1]));
    }

    public static boolean undoBlackHole(String host, String account, String password, Integer port, String mac) {
        String[] blackHoleCommands = {"system-view", "undo mac-address blackhole " + mac, "quit", "quit"};
        String msg = execShell(host, account, password, port, blackHoleCommands);
        return (msg != null && msg.contains(blackHoleCommands[1]));
    }

    public static String execShell(String host, String account, String password, Integer port, String[] commands) {
        return execShell(host, account, password, port, commands, false);
    }

    public static String execShell(String host, String account, String password, Integer port, String[] commands, boolean debug) {
        if (commands == null || commands.length == 0) {
            return null;
        }

        Session session = getSession(host, account, password, port, debug);
        if (!session.isConnected()) {
            return null;
        }

        ChannelShell channel = null;
        StringBuilder builder = new StringBuilder();
        try {
            channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
            InputStream inputStream = channel.getInputStream();
            OutputStream outputStream = channel.getOutputStream();
            for(String cmd : commands) {
                String command = cmd + " \n\r";
                outputStream.write(command.getBytes());
                // 每个命令之间，要有间隔
                Thread.sleep(1000);
            }
            // flush 只能发送一次，ssh-server根据此命令 disconnect
            outputStream.flush();
            Thread.sleep(2000);
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String msg = null;
            while((msg = in.readLine())!=null){
                builder.append(msg).append("\n");
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            session.disconnect();
        }
        return builder.toString();
    }

    public static List<String> getBlackHoleList(String host, String account, String password, Integer port) {
        String[] blackHoleCommands = {
                "system-view",
                "user-interface vty 0 4",
                "screen-length 0",
                "display mac-address blackhole",
                "screen-length 24",
                "return",
                "quit"};
        String msg = execShell(host, account, password, port, blackHoleCommands, true);
        String[] lines = msg.split("\n");
        boolean start = false;
        List<String> macList = new LinkedList<>();
        String endIndex = "Total items displayed = ";
        String macIndex = "blackhole";
        int total = 0;
        for(String line : lines) {
            if (start && line.contains(macIndex)) {
                String mac = line.substring(0, 14);
                macList.add(mac);
            }

            if (line.contains(blackHoleCommands[3])) {
                start = true;
            }
            if (line.startsWith(endIndex)) {
                total = StringHelper.getIntValue(line.substring(endIndex.length()).trim());
                break;
            }

        }
        return macList;
    }

    public static String getCurrentConfig(String host, String account, String password, Integer port) {
        String[] configCommands = {
                "system-view",
                "user-interface vty 0 4",
                "screen-length 0",
                "display current-configuration",
                "screen-length 24",
                "return",
                "quit"};
        String msg = execShell(host, account, password, port, configCommands, true);
        String[] lines = msg.split("\n");
        boolean start = false;
        List<Object> configList = new LinkedList<>();
        for(String line : lines) {
            if (line.contains(configCommands[3])) {
                start = true;
            }
            if ("return".equals(line)) {
                break;
            }
            if (start) {
                configList.add(line);
            }
        }
        return StringHelper.joinList(configList, "\n");
    }

    public static String exeCommand(String host, String account, String password, int port, String command) throws JSchException, IOException {

        Session session = getSession(host, account, password, port, false);

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        channelExec.disconnect();
        session.disconnect();

        return builder.toString();
    }

    public static String[] exeCommands(String host, String account, String password, int port, String[] commands) throws JSchException, IOException {
        String[] resp = new String[commands.length];
        for(int i=0; i<commands.length; i++) {
            resp[i] = exeCommand(host, account, password, port, commands[i]);
        }
        return resp;
    }

    private static class JschLogger implements com.jcraft.jsch.Logger{

        @Override
        public boolean isEnabled(int level) {
            return true;
        }

        @Override
        public void log(int level, String message) {
            log.info(String.format("[JSCH --> %s]", message));
        }
    }

    public static List<String> generateMac() {
        String[] macArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e"};
        List<String> macList = new LinkedList<>();
        String macPrefix = "aaaa-aaaa-aa";
        for (int i=0; i<macArray.length; i++) {
            for (int j=0; j<macArray.length; j++) {
                macList.add(macArray[i] + macArray[j]);
            }
        }
        return macList;
    }




    public static void main(String[] args) throws Exception{
        Integer port = 22;
        String ip = "10.0.6.212";
        String account = "root";
        String password = "2Ey2gwqyKOb1wTWS";

        String program = "rad_api";
        String command = "ls";
        String achieve = "zans_run";
        String srcFolder = "D:\\02.gitlab\\zans_install\\release\\dist\\";
        String srcPath = srcFolder + achieve;
        String destFolder = "/home/release/dist/";
        String destPath = destFolder + achieve;
        try {
            String resp = "";
            command = "supervisorctl stop " + program;
            Boolean aBoolean = new UpgradeServiceImpl().execEnableCommand(ip, account, password, port, "aa", 1);
            resp = SshClient.exeCommand(ip, account, password, port, command);
            System.out.println(command);
            System.out.println(resp);

            command = "upload";
            System.out.println(srcPath);
            System.out.println(destPath);
            boolean r = SshClient.upload(ip, account, password, port, srcPath, destPath);
            System.out.println(command);
            System.out.println(r);

            String[] commands = {
                    "chmod +x " + destPath,
                    "supervisorctl start " + program
            };

            String[] respArray = SshClient.exeCommands(ip, account, password, port, commands);
            System.out.println(Arrays.toString(commands));
            System.out.println(Arrays.toString(respArray));


        } catch (Exception ex) {
            log.error("command error#"+ command, ex);
        }
    }

}
