package com.pancm.test.mapTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
		
		getMap1();
	}

	
	private static void getMap1(){
		Map<Integer,Integer> map1 =new HashMap<Integer,Integer>();
		Map<Integer,Integer> map2 =new HashMap<Integer,Integer>();
		map1.put(1, 1);
		map1.put(2, 2);
		map1.put(3, 3);
		map2.put(11, 11);
		map2.put(22, 22);
		map2.put(33, 33);
		Map<Integer,Map<Integer,Integer>> map3 =new HashMap<Integer,Map<Integer,Integer>>();
		map3.put(1, map1);
		map3.put(2, map2);
		
		System.out.println("map3:"+map3);
		StringBuffer sb=new StringBuffer();
		for(Entry<Integer,Map<Integer,Integer>> entry:map3.entrySet()){
			sb.append(entry.getKey());
			sb.append(":");
			Map<Integer,Integer> map4=entry.getValue();
				for(Entry<Integer,Integer> entry1:map4.entrySet()){
					sb.append(entry1.getKey());
					sb.append("_");
					sb.append(entry1.getValue());
					sb.append(",");
				}
			
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		System.out.println("sb:"+sb);
	}
}
