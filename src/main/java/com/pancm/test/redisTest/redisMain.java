package com.pancm.test.redisTest;

import redis.clients.jedis.Jedis;

/**
 * 
* Title: redisMain
* Description: 连接redis服务
* Version:1.0.0  
* @author Administrator
* @date 2017-8-19
 */
public class redisMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 连接到本地的 redis服务
		Jedis jedis=new Jedis("127.0.0.1");
		System.out.println("连接成功");
	       //查看服务是否运行
	    System.out.println("服务正在运行: "+jedis.ping());
	}

}
