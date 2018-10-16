package com.pancm.test.redisTest;

import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * 
* Title: redisStringTest
* Description: redis存储list示例
* Version:1.0.0  
* @author Administrator
* @date 2017-8-19
 */
public class redisListTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 //连接本地的 Redis 服务
        Jedis jedis = new Jedis("127.0.0.1");
        System.out.println("连接成功");
        //存储到列表中
        jedis.lpush("list", "redis");
        jedis.lpush("list", "java");
        jedis.lpush("list", "mysql");
        //获取存储的数据并输出
        List<String> list=jedis.lrange("list", 0, 2);
        for(int i=0,j=list.size();i<j;i++){
        	System.out.println("list的输出结果:"+list.get(i));
        	
        }

	}

}
