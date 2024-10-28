package com.zans.base.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;

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

       System.out.println(calcIpSegment("27.23.16.30", "255.255.255.254"));
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

    /**
     * 根据子网掩码计算ip所属网段范围
     * @param ip
     * @param mask
     * @return
     */
    public static Pair<String,String> calcIpSegment(String ip,String mask){
        if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(mask)){
            String[] ipArray = ip.trim().split("\\.");
            String[] maskArray = mask.trim().split("\\.");
            List<String> startArray = Lists.newArrayList("0","0","0","0");
            List<String> endArray = Lists.newArrayList("255","255","255","255");
            if (ipArray.length == 4 && maskArray.length == 4){
                for (int i =0 ;i < 4 ;i++){
                    if("255".equalsIgnoreCase(maskArray[i])){
                        startArray.set(i, ipArray[i]);
                        endArray.set(i, ipArray[i]);
                    }else{
                        Integer ips = Integer.valueOf(ipArray[i]) & Integer.valueOf(maskArray[i]);
                        startArray.set(i, ips.toString());
                        Integer step = Integer.valueOf(maskArray[i]) ^ 255;
                        Integer end = ips + step;
                        endArray.set(i, end.toString());
                        break;
                    }
                }
                return new Pair<String,String>(StringUtils.join(startArray, "."), StringUtils.join(endArray, "."));
            }
        }
        return new Pair<String,String>(null, null);
    }
}
