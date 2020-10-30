package com.pancm.test.utilTest;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Ip To Mac util.
 *
 * @author WangYong
 * @Title: IpToMacUtil
 * @Description: 将IP地址转换成MAC地址
 * @Version:1.0.0
 * @date 2020年10月19日
 */
public class IpUtil {
    
    public static void main(String[] args) {
       long mac = ipToLong("127.0.0.1");
       System.out.println(mac);
    }

    /**
     * 将IP转换成MAC地址(系统设定的转换规则)
     * 
     * @param ipAddr IP地址 如：127.0.0.1
     * @return
     */
    public static String ipToMac(String ipAddr){
        List<String> macStrList = new ArrayList<String>(6);
        macStrList.add("ff");
        macStrList.add("ff");
        if (StringUtils.isNotBlank(ipAddr)){
            String[] ipArray = ipAddr.split("\\.");
            if (ipArray.length == 4){
                for (String eIp : ipArray){
                    String h = Integer.toHexString(Integer.valueOf(eIp));
                    if (h.length() == 1){
                        h = "0"+h;
                    }
                    macStrList.add(h.toLowerCase());
                }
                return StringUtils.join(macStrList, " ");
            }
        }
        return null;
    }
    
    /**
     * 将ip地址转换成Long数据
     * @param ipAddr
     * @return
     */
    public static long ipToLong(String ipAddr){
        long ipLong = 0;
        if (StringUtils.isNotBlank(ipAddr)){
            String[] ipArray = ipAddr.split("\\.");
            if (ipArray.length == 4){
                return (Long.valueOf(ipArray[0]) << 24) 
                        + (Long.valueOf(ipArray[1]) << 16)
                        + (Long.valueOf(ipArray[2]) << 8)
                        + Long.valueOf(ipArray[3]);
            }
        }
        return ipLong;
    }

    
}
