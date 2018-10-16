package com.pancm.test.redisTest;

import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * 
* Title: redisStringTest
* Description: redis存储set示例
* Version:1.0.0  
* @author Administrator
* @date 2017-8-19
 */
public class redisSetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Jedis jedis=new Jedis("127.0.0.1");
		System.out.println("连接成功");
		//存储数据
		jedis.sadd("setTest1", "abc");
		jedis.sadd("setTest1", "abcd");
		jedis.sadd("setTest1", "abcde");
		//获取数据并输出
		Set<String> keys=jedis.keys("*");
	//	Set<String> keys=jedis.smembers("setTest1");
		//定义迭代器输出
		Iterator<String> it=keys.iterator();
		while(it.hasNext()){
			String key=it.next();
			System.out.println(key);   
		}	
	}

}
