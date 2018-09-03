package com.pancm.test.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Title: StreamTest
 * @Description: Stream测试用例
 * @Version:1.0.0
 * @author pancm
 * @date 2018年9月3日
 */
public class StreamTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test1();
	}

	private static void test1() {
		/*
		 * 普通的方式过滤
		 */
		List<String> list = Arrays.asList("张三", "李四", "王五");
		System.out.println("过滤之前:" + list);
		List<String> result = new ArrayList<>();
		for (String str : list) {
			if (!"李四".equals(str)) {
				result.add(str);
			}
		}
		System.out.println("过滤之后:" + result);

		/*
		 * stream 过滤
		 */
		List<String> result2 = list.stream().filter(str -> !"李四".equals(str)).collect(Collectors.toList());
		System.out.println("stream 过滤之后:" + result2);
		//另一种方式输出
		result2.forEach(System.out::println);

		
		// 使用stream.filter ()过滤一列表，并.findAny().orElse (null)返回一个对象的条件。
		// 遍历该list，查询数据，如果查不到，就返回null
		String result3 =list.stream().filter(str -> "李四".equals(str)).findAny().orElse(null);
		
		System.out.println("stream 过滤之后 2:"+result3);
		
		
	}

	private static void test3() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");
		map.put("e", "5");
		map.put("f", "6");

	}

}
