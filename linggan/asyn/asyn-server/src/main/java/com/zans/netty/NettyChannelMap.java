package com.zans.netty;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author beixing
 * @Title: asyn-server
 * @Description: channel通道的缓存map
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/23
 */
public class NettyChannelMap {

    public static int channelNum = 0;
    private static ConcurrentHashMap<String, Channel> channelHashMap = null;//concurrentHashmap以解决多线程冲突

    public static ConcurrentHashMap<String, Channel> getChannelHashMap() {
        return channelHashMap;
    }

    public static Channel getChannelByName(String name) {
        if (channelHashMap == null || channelHashMap.isEmpty()) {
            return null;
        }
        return channelHashMap.get(name);
    }

    public static void addChannel(String name, Channel channel) {
        if (channelHashMap == null) {
            channelHashMap = new ConcurrentHashMap<String, Channel>();
        }
        channelHashMap.put(name, channel);
        channelNum++;
    }

    public static void removeChannelByName(String name) {
        if (channelHashMap != null) {
            channelHashMap.remove(name);
        }
    }


}
