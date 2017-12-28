package com.pancm.test.mapTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
* Title: mapTest
* Description:集合map测试 
* Version:1.0.0  
* @author pancm
* @date 2017年11月16日
 */
public class mapTest {
	private static Map<String,String> map = new HashMap<String,String>();
	
	public static Map<String,String> getHashMap(){
		if(map == null || map.isEmpty()){
			map = new HashMap<String,String>();
		}
		return  map;
	}
	
	public static void main(String[] args) {
		//普通的HashMap 
		Map<String,String> map=mapTest.getHashMap();
		map.put("a", "1");
		map.put("b", "1");
		map.put("c", "e");
		System.out.println(map.toString());
		Map<String,String> map1=mapTest.getHashMap();
		System.out.println("-----"+map1.get("a"));
		
		//上锁的HashMap
		Map<String,String> sMap=Collections.synchronizedMap(new HashMap());
	}

}
