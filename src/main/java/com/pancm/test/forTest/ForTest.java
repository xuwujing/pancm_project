package com.pancm.test.forTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type For test.
 *
 * @author pancm
 * @Title: ForTest
 * @Description: 循环测试
 * @Version:1.0.0
 * @date 2017年9月29日
 */
public class ForTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		  /*
		   * 阿里巴巴开发手册中的遍历移除测试
		   */
		 iteratorTest();
		forEachTest2();
		forEachTest();
	 }

	private static void forEachTest2() {
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("4");
		list.forEach(s -> {
			if("2".equals(s)){
				list.add(s);
			}
		});
		System.out.println(list);

	}

	private static void forEachTest(){
		 List<String> list = new ArrayList<String>();
		 list.add("1");
		 list.add("2");
		 System.out.println("list1:"+list);
		 for (String item : list) {
			 //如果是1就不会出现
		   if ("2".equals(item)) {
		    list.remove(item);
		    //加上break就不会抛异常
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
