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

	public static void main(String[] args) {
		//普通的HashMap 
		Map<String,String> map=new HashMap<>();
		
		//上锁的HashMap
		Map<String,String> sMap=Collections.synchronizedMap(new HashMap());
	}

}
