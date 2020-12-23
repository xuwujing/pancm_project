package com.pancm.test.tcpTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: Tcp链接测试
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/4/9 0009
 */
public class TcpConTest {

    public static void main(String[] args) {
        String ipAddress = "192.168.56.1";

        try {
            System.out.println(ping(ipAddress));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static boolean ping(String ipAddress) throws Exception {
        int  timeOut =  3000 ;  //超时应该在3钞以上
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }

    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        // 将要执行的ping命令,此命令是windows格式的命令
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
        // Linux命令如下
        // String pingCommand = "ping" -c " + pingTimes    + " -w " + timeOut + ipAddress;
        try {
            // 执行命令并获取输出
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line;
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }
            // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == pingTimes;
        } catch (Exception e) {
             e.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResult(String line) {  // System.out.println("控制台输出的结果为:"+line);
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }

}
