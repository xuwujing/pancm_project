package com.zans.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PingUtil {

    public static boolean ping(String ipAddress) throws Exception {
        int  timeOut =  3000 ;  //超时应该在3钞以上
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }

    public static void ping02(String ipAddress) throws Exception {
        String line = null;
        try {
            Process pro = Runtime.getRuntime().exec("ping " + ipAddress);
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    pro.getInputStream()));
            while ((line = buf.readLine()) != null)
                System.out.println(line);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")){
//            pingCommand = "ping  -c " + pingTimes + " -w " + timeOut + " "+ipAddress;
            pingCommand = "ping  -c " + pingTimes +  " "+ipAddress;
        }
        // Linux命令如下
    // String pingCommand = "ping" -c " + pingTimes + " -w " + timeOut + ipAddress;
        try {   // 执行命令并获取输出
            System.out.println(pingCommand);
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }   // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
//            return connectedCount == pingTimes;
            return connectedCount > 0;
        } catch (Exception ex) {
            ex.printStackTrace();   // 出现异常则返回假
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
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")){
            pattern = Pattern.compile("(ttl=\\d+)",    Pattern.CASE_INSENSITIVE);
        }

        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }

    public static List<String> fping(List<String> ipList, int pingTimes, int timeOut) {
        List<String> retList = new ArrayList<>();
        String pingCommand = "fping -c " + pingTimes    + " -t " + timeOut+" "+String.join(" ",ipList);
        System.out.println(pingCommand);

        ProcessBuilder builder1 = new ProcessBuilder("bash", "-c",pingCommand);
        builder1.redirectErrorStream(true);
        Process p2 = null;
        BufferedReader in = null ;
        try {
            String line = null;
            p2 = builder1.start();
            in = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            while ((line = in.readLine()) != null) {
//                System.out.println( line);
                String ret = getFpingCheckResult(line);
                if (ret != null && ret.length()>0){
                    retList.add(ret);
                }
            }
        } catch (IOException e) {
            log.error("fping失败!pingCommand:{}",pingCommand,e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error("fping关闭流失败!pingCommand:{}",pingCommand,e);
            }
        }
        p2.destroy();
        return retList;
    }

    private static String getFpingCheckResult(String line) {
        Pattern pattern = Pattern.compile("(xmt/rcv/%loss)");
        Matcher matcher = pattern.matcher(line);
//        System.out.println(line);
        while (matcher.find()) {
            //192.168.9.59 : xmt/rcv/%loss = 5/0/100%
            String[] loss = line.split(":");
            String ip = loss[0].replace(" ","");
            String[] lossArray = loss[1].split("=")[1].split("/");
            String rcv = lossArray[1].replace(" ","");
            return ip+";"+rcv;
        }
        return "";

    }

    public static void main(String[] args) throws Exception {
        String ipAddress = "192.168.9.59";
        String ipAddress2 = "192.168.9.51";
        String ipAddress3 = "192.168.9.52";
        List<String> ipList = Arrays.asList(ipAddress,ipAddress2,ipAddress3);
//        System.out.println(ping(ipAddress,5,5000));
//        ping02(ipAddress);
        System.out.println(fping(ipList, 5, 5000));
        System.out.println(System.getProperty("os.name").toLowerCase());
    }
}
