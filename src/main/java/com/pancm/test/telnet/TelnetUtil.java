package com.pancm.test.telnet;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/12/1
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Miracle Luna
 * @version 1.0
 * @date 2019/12/16 16:11
 */
public class TelnetUtil {

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
//        String hostname = "14.215.178.37";
        String hostname = "www.baidu.com";    // hostname 可以是主机的 IP 或者 域名
        int port = 80;
        int timeout = 200;
        boolean isConnected = telnet(hostname, port, timeout);
        System.out.println("telnet "+ hostname + " " + port + "\n==>isConnected: " + isConnected);
    }
}
