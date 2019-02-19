package com.pancm.test.codetest;

import java.util.HashMap;
import java.util.Map;

/**
* @Title: TestHashMap
* @Description: hashmap源码理解
* @since jdk 1.8
* @Version:1.0.0  
* @author pancm
* @date 2019年2月18日
*/
public class TestHashMap {

	
	public static void main(String[] args) {
		
		/*
		  HashMap提供了三个构造函数：

	      HashMap()：构造一个具有默认初始容量 (16) 和默认加载因子 (0.75) 的空 HashMap。
	
	      HashMap(int initialCapacity)：构造一个带指定初始容量和默认加载因子 (0.75) 的空 HashMap。
	
	      HashMap(int initialCapacity, float loadFactor)：构造一个带指定初始容量和加载因子的空 HashMap。
		  
		  初始容量是创建哈希表时的容量，加载因子是哈希表在其容量自动增加之前可以达到多满的一种尺度。
		  
		  
		 */
		
		
		
		/*
		 * 性能测试
		 */
		long startime= System.currentTimeMillis();
		Map<String, Object> map=new HashMap<String, Object>();
		for(int i=0;i<1000000;i++) {
			map.put("a"+i, "b"+i);
		}
		
		for(int i=100;i<200;i++) {
			map.get("a"+i);
		}
		long endTime=System.currentTimeMillis();
		
		System.out.println("用时:"+ (endTime-startime)+"ms");
		
	}
	
}
