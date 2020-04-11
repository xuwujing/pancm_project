package com.pancm.test.tcpTest;

import java.net.InetAddress;

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
        String ipAddress = "";
    }



    public static boolean ping(String ipAddress) throws Exception {
        int  timeOut =  3000 ;  //超时应该在3钞以上
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }

}
