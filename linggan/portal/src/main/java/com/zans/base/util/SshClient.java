package com.zans.base.util;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static com.zans.base.config.BaseConstants.SEPARATOR_LINE;

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

    public static Boolean upload(String ip, String username, String password, Integer port, String srcPath, String desPath) {
        Boolean flage = false;
        Session session = getSession(ip, username, password, port, false);
        ChannelSftp channelSftp = getChannelSftp(session);
        try {
            InputStream inputStream = new FileInputStream(srcPath);
            //上传
            channelSftp.put(inputStream, desPath);
            channelSftp.quit();
            channelSftp.exit();
            flage = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channelSftp.disconnect();
            session.disconnect();
        }
        return flage;
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
        String[] blackHoleCommands = {"system-view", "mac-address blackhole " + mac, "quit", "quit"};
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
            for (String cmd : commands) {
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
            while ((msg = in.readLine()) != null) {
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
        for (String line : lines) {
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




    /**
     * 连接
     */
    public static Boolean collet(String account, String password, String host, int port, int timeout) {
        JSch jsch = new JSch();
        Session session;
        ChannelShell channelShell;
        PrintWriter printWriter;
        InputStream inputStream;
        OutputStream outputStream;
        try {
            session = jsch.getSession(account, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            channelShell = (ChannelShell) session.openChannel("shell");
            //从远端到达的数据都能从这个流读取到
            inputStream = channelShell.getInputStream();
            channelShell.setPty(true);
            channelShell.connect();
            // 写入该流的数据都将发送到远程端
            outputStream = channelShell.getOutputStream();
            // 使用PrintWriter 就是为了使用println 这个方法好处就是不需要每次手动给字符加\n
            printWriter = new PrintWriter(outputStream);
            Thread.sleep(timeout);
            System.err.println(host + "Connection Successed");
            return true;
        } catch (JSchException | IOException | InterruptedException ex) {
            ex.printStackTrace();
            System.err.println(host + "Connection Failed");
        }
        return false;
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
        if (msg == null) {
            return null;
        }
        String[] lines = msg.split("\n");
        boolean start = false;
        List<Object> configList = new LinkedList<>();
        for (String line : lines) {
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
        return StringHelper.joinCollection(configList, SEPARATOR_LINE);
    }

    public static String exeCommand(String host, int port, String account, String password, String command) throws JSchException, IOException {

        Session session = getSession(host, account, password, port, true);

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

    private static class JschLogger implements com.jcraft.jsch.Logger {

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
        for (int i = 0; i < macArray.length; i++) {
            for (int j = 0; j < macArray.length; j++) {
                macList.add(macArray[i] + macArray[j]);
            }
        }
        return macList;
    }

    public static void main(String[] args) throws Exception {
        Boolean root = collet("root", "2Ey2gwqyKOb1wTWS", "10.0.6.231", 22, 5000);
        System.out.println(root);

        Integer port = 22;
        String host = "27.23.254.254";
        host = "27.26.254.254";
        host = "27.24.254.254";
        host = "27.25.254.254";
        String account = "whsjgj";
        String password = "Whsjgj@2017";

//        host = "27.11.254.254";
//        account = "admin";
//        password = "Whjgj@2017";
//
//
//        account = "admin";
//        password = "whjgj@2017";
//        host = "27.10.254.254";
//        host = "27.12.254.254";
//        host = "27.13.254.254";
//        host = "27.14.254.254";
//        host = "27.19.254.254";
//        host = "27.15.254.254";
//        host = "27.35.254.254";
//        host = "27.37.254.254";


        String config = getCurrentConfig(host, account, password, port);
        System.out.println(config);
        List<String> blackHoleList = getBlackHoleList(host, account, password, port);
        System.out.println(blackHoleList.size());
        for (String mac : blackHoleList) {
            System.out.println(mac);
        }

//        for (String mac : blackHoleList){
//            boolean isDone = undoBlackHole(host, account, password, port, mac);
//            log.info("undo blackhole {}#{}", mac, isDone);
//            Thread.sleep(5000);
//        }
    }


}
