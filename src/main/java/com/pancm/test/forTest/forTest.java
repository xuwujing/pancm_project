package com.pancm.test.forTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
* Title: forTest
* Description: 循环测试 
* Version:1.0.0  
* @author pancm
* @date 2017年9月29日
 */
public class forTest {
	 public static void main(String[] args) {
		 iteratorTest();
		 forEachTest();
	 }
	 private static void forEachTest(){
		 List<String> list = new ArrayList<String>();
		 list.add("1");
		 list.add("2");
		 System.out.println("list1:"+list);
		 for (String item : list) {
		   if ("2".equals(item)) {
		    list.remove(item);
//		    break; 
		 }
	   } 
		System.out.println("list2:"+list);
	 } 
	 private static void iteratorTest(){
		 List<String> list = new ArrayList<String>();
		 list.add("1");
		 list.add("2");
		 System.out.println("list3:"+list);
		 Iterator<String> iterator = list.iterator();
		 while (iterator.hasNext()) {
			 String item = iterator.next();
			 if ("2".equals(item)) {
				 iterator.remove();
			 }
		 }
		 System.out.println("list4:"+list);
	   } 
	 
	 
}
