package com.zans.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author beixing
 * @Title: file-client
 * @Description: windows的cmd创建工具类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/16
 */
@Slf4j
public class CmdHelper {

    public static boolean testMount(String ip) throws IOException, InterruptedException {
        List<String> stringList = new ArrayList<>();
        stringList.add("cmd.exe");
        stringList.add("/c");
        stringList.add("showmount");
        stringList.add("-e");
        stringList.add(ip);
        String resultInfo = execCmd(stringList);
        if (resultInfo.contains("导出列表")) {
            return true;
        }
        return false;
    }

    /**
    * @Author beixing
    * @Description  创建挂载，挂载的地址和盘符
    * @Date  2021/8/16
    * @Param
    * @return
    **/
    public static boolean createMount(String url,String driverName) throws IOException, InterruptedException {
        List<String> stringList = new ArrayList<>();
        stringList.add("cmd.exe");
        stringList.add("/c");
        stringList.add("mount");
        stringList.add(url);
        stringList.add(driverName);
        String resultInfo = execCmd(stringList);
        if (resultInfo.contains("成功")) {
            return true;
        }
        return false;
    }

    /**
     * @Author beixing
     * @Description  删除映射关系，挂载的地址和盘符
     * @Date  2021/8/16
     * @Param
     * @return
     **/
    public static boolean delMount(String driverName) throws IOException, InterruptedException {
        List<String> stringList = new ArrayList<>();
        stringList.add("cmd.exe");
        stringList.add("/c");
        stringList.add("net");
        stringList.add("use");
        stringList.add(driverName);
        stringList.add("/del");
        String resultInfo = execCmd(stringList);
        if (resultInfo.contains("删除")) {
            return true;
        }
        return false;
    }

    /**
     * @Author beixing
     * @Description  删除映射关系，挂载的地址和盘符
     * @Date  2021/8/16
     * @Param
     * @return
     **/
    public static boolean delLink(String linkName) throws IOException, InterruptedException {
        List<String> stringList = new ArrayList<>();
        stringList.add("cmd.exe");
        stringList.add("/c");
        stringList.add("rd");
        stringList.add("C:\\Users\\Administrator\\Desktop\\"+linkName);
        stringList.add("/s/q");
        String resultInfo = execCmd(stringList);
        return true;
    }

    /**
     * @Author beixing
     * @Description  创建链接
     * @Date  2021/8/16
     * @Param
     * @return
     **/
    public static boolean createLink(String linkName,String driverName) throws IOException, InterruptedException {
        List<String> stringList = new ArrayList<>();
        stringList.add("cmd.exe");
        stringList.add("/c");
        stringList.add("mklink");
        stringList.add("/d");
        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");
//        String userName = "admin";
        log.info("当前电脑的用户名:{}",userName);
//        stringList.add("C:\\Users\\"+userName+"\\Desktop\\"+linkName);
        stringList.add(linkName);
        stringList.add(driverName);
        String resultInfo = execCmd(stringList);
        if (resultInfo.contains("创建")) {
            return true;
        }
        return false;
    }

    public static String execCmd(List<String> stringList) throws IOException, InterruptedException {

        ProcessBuilder builder1 = new ProcessBuilder();
        builder1.command(stringList);
        builder1.redirectErrorStream(true);
        Process p1 = builder1.start();
        p1.waitFor(3, TimeUnit.SECONDS);
        StringBuffer sbf = new StringBuffer();
        String line = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream(), "gb2312"));
        while ((line = br.readLine()) != null) {
            sbf.append(line);
            sbf.append(" ");
        }
        String resultInfo = sbf.toString();
        log.info("result:{}" , resultInfo);
        p1.destroy();
        return resultInfo;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        String user = "张三";
        String ip = "192.168.6.191";
        String url = "\\\\192.168.6.191\\data";
        String drive = "z:";
        String linkName = user +"_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        System.out.println(testMount(ip));
        System.out.println(createMount(url,drive));
        System.out.println(createLink(linkName,drive));
//        System.out.println(delMount(drive));
    }
}
