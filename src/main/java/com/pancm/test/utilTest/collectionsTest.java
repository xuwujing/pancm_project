package com.pancm.test.utilTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.HashBag;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.apache.commons.collections.list.LazyList;

/**
 * 
* Title: collections
* Description: Collections 工具包测试
* Version:1.0.0  
* @author pancm
* @date 2017年11月1日
 */
public class collectionsTest {

	public static void main(String[] args) {
		bagTest();
		lazyTest();
		bidimapTest();
	}
	
	/**
	 * Bag 测试
	 * 主要测试重复元素的统计
	 */
	@SuppressWarnings("deprecation")
	private static void bagTest(){
		//定义4种球
		Bag box=new HashBag(Arrays.asList("red","blue","black","green"));
		System.out.println("box.getCount():"+box.getCount("red"));//box.getCount():1
		box.add("red", 5);//红色的球增加五个
		System.out.println("box.size():"+box.size());	//box.size():9
		System.out.println("box.getCount():"+box.getCount("red"));//box.getCount():6
	}
	
	/**
	 * Lazy测试
	 * 需要该元素的时候，才会生成
	 */
	@SuppressWarnings("unchecked")
	private static void lazyTest(){
		List<String> lazy=LazyList.decorate(new ArrayList<>(), new Factory() {
			@Override
			public Object create() {
				return "Hello";
			}
		}); 
		//访问了第三个元素，此时0和1位null
		//get几就增加了几加一 ， 输出依旧是 Hello 
		String str=lazy.get(2);
		System.out.println("str:"+str);//str:Hello 
		//加入的第四个元素
		lazy.add("world");
		//元素总个为4个
		System.out.println("lazy.size():"+lazy.size());//lazy.size():4
	}
	
	/**
	 * 双向Map
	 * 唯一的key和map，可以通过键或值来操作
	 * 比如删除、查询等
	 */
	private static void bidimapTest(){
		BidiMap map=new TreeBidiMap();
		map.put(1, "a");
		map.put(2, "b");
		map.put(3, "c");
		System.out.println("map:"+map);	//map:{1=a, 2=b, 3=c}
		System.out.println("map.get():"+map.get(2)); //map.get():b
		System.out.println("map.getKey():"+map.getKey("a")); //map.getKey():1
		System.out.println("map.removeValue():"+map.removeValue("c")); //map.removeValue():3
		System.out.println("map:"+map);	//map:{1=a, 2=b}
	}
	
	
}
