package com.pancm.test.redisTest;

import redis.clients.jedis.Jedis;

/**
 * 
* Title: redisStringTest
* Description: redis存储string示例
* Version:1.0.0  
* @author Administrator
* @date 2017-8-19
 */
public class redisStringTest {
     public static void main(String[] args){
    	 //连接本地的 Redis 服务
         Jedis jedis = new Jedis("127.0.0.1");
         System.out.println("连接成功");
         //设置 redis 字符串数据
         jedis.set("rst", "redisStringTest");
         // 获取存储的数据并输出
         System.out.println("redis 存储的字符串为: "+ jedis.get("rst"));
     }
}
