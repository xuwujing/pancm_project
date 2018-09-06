package com.pancm.test.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.pancm.test.pojoTest.User;

/**
 * @Title: StreamTest
 * @Description: Stream测试用例
 * 流的操作类型分为两种：

Intermediate：一个流可以后面跟随零个或多个 intermediate 操作。其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用。
这类操作都是惰性化的（lazy），就是说，仅仅调用到这类方法，并没有真正开始流的遍历。
Terminal：一个流只能有一个 terminal 操作，当这个操作执行后，流就被使用“光”了，无法再被操作。
所以这必定是流的最后一个操作。
Terminal 操作的执行，才会真正开始流的遍历，并且会生成一个结果，或者一个 side effect。
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
		test2();
		test3();
	}

	

	/**
	 * 简单实用
	 */
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

		
		// 使用stream.filter ()过滤一列表，并.findAny().orElse 
		// 遍历该list，查询数据，如果查不到，就返回 找不到!
		String result3 =list.stream().filter(str -> "李四".equals(str)).findAny().orElse("找不到!");
		String result4 =list.stream().filter(str -> "李二".equals(str)).findAny().orElse("找不到!");
		
		System.out.println("stream 过滤之后 2:"+result3);
		System.out.println("stream 过滤之后 3:"+result4);
	}
	
	
	/**
	 * 基本使用
	 */
	private static void test2() {
		
		/*
		 * 构造流的几种方式
		 */
		Stream stream = Stream.of("a", "b", "c");
		String [] strArray = new String[] {"a", "b", "c"};
		stream = Stream.of(strArray);
		stream = Arrays.stream(strArray);
		List<String> list = Arrays.asList(strArray);
		stream = list.stream();
		
		
		/*
		 *  流之间的相互转化
		 *  一个 Stream 只可以使用一次，这段代码为了简洁而重复使用了数次，因此会抛出异常
		 */
		try {
			Stream<String> stream2=Stream.of("a", "b", "c");
			// 转换成 Array
			String[] strArray1 = stream2.toArray(String[]::new);
			
			// 转换成 Collection
			List<String> list1 = stream2.collect(Collectors.toList());
			List<String> list2 = stream2.collect(Collectors.toCollection(ArrayList::new));
			
			Set set1 = stream2.collect(Collectors.toSet());
			Stack stack1 = stream2.collect(Collectors.toCollection(Stack::new));
			
			// 转换成 String
			String str = stream.collect(Collectors.joining()).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		/*
		 * 汇总操作
		 */
		List<User> lists=new ArrayList<User>();
		lists.add(new User(6, "张三"));
		lists.add(new User(2, "李四"));
		lists.add(new User(3, "王五"));
		lists.add(new User(1, "张三"));
		//计算这个list中出现 "张三" id的值
		int sum =lists.stream()
                .filter(u -> "张三".equals(u.getName()))
                .mapToInt(u -> u.getId())
                .sum();
		
		System.out.println("计算结果:"+sum); //7
		
		
		/*
		 * 数值类型的流
		 * 包括IntStream, LongStream和DoubleStream 
		 */
		System.out.println("遍历输出该数组的数据:");
		IntStream.of(new int[]{1, 2, 3, 4}).forEach(System.out::println);
		System.out.println("查询范围在 2-3(2<=i<3)之间的数据:");
		IntStream.range(2, 3).forEach(System.out::println);
		System.out.println("查询范围在2-3(2<=i<=3)之间的数据:");
		IntStream.rangeClosed(2, 3).forEach(System.out::println);
		
		
		/*      stream中的 map使用                   */
		
		/*
		 * 转换大写
		 */
		List<String> list3 = Arrays.asList("zhangSan", "liSi", "wangWu");
		System.out.println("转换之前的数据:"+list3);//转换之前的数据:[zhangSan, liSi, wangWu]
		List<String> list4 = list3.stream().map(String::toUpperCase).collect(Collectors.toList());
		System.out.println("转换之后的数据:"+list4); //转换之后的数据:[ZHANGSAN, LISI, WANGWU]
		
		
		/*
		 * 获取平方
		 */
		List<Integer> list5=Arrays.asList(new Integer[]{1,2,3,4,5});
		List<Integer> list6 = list5.stream().map(n -> n * n).collect(Collectors.toList());
		System.out.println("平方的数据:"+list6);//转换之前的数据:[1, 4, 9, 16, 25]
		
		
		/*
		 * flatMap 一对多
		 * 得到多个数组里面的数字 
		 */
		Stream<List<Integer>> inputStream = Stream.of(
				 Arrays.asList(1),
				 Arrays.asList(2, 3),
				 Arrays.asList(4, 5, 6)
				 );
		Stream<Integer> outputStream = inputStream.flatMap((childList) -> childList.stream());
		System.out.println("打印 stream中的数字:"); 
		outputStream.forEach(System.out::println);
		
		
		/*
		 *  得到一段句子中的单词
		 */
		String worlds="Never let success get to your head and never let failure get to your heart.";
		List<String> list7=new ArrayList<>();
		list7.add(worlds);
		List<String> list8= list7.stream().flatMap(str -> Stream.of(str.split(" ")))
				.filter(world->world.length()>0).collect(Collectors.toList());
		System.out.println("单词:");
		list8.forEach(System.out::println);
		
		
		/*
		 *  peek 对每个元素执行操作并返回一个新的 Stream
		 */
		System.out.println("peek使用:");
		Stream.of("one", "two", "three", "four")
		 .filter(e -> e.length() > 3)
		 .peek(e -> System.out.println("转换之前: " + e))
		 .map(String::toUpperCase)
		 .peek(e -> System.out.println("转换之后: " + e))
		 .collect(Collectors.toList());
		
		
		
	}
	
	
	/**
	 *  一些关联使用
	 */
	private static void test3() {
		
		/*
		 *  Optional
		 */
		String strA = " abcd ", strB = null;
		System.out.println("数据校验开始...");
		print(strA);
		print("");
		print(strB);
		getLength(strA);
		getLength("");
		getLength(strB);
		System.out.println("数据校验结束...");
		
		/*
		 * reduce
		 * 主要作用是把 Stream 元素组合起来。
		 */
		// 字符串连接，concat = "ABCD"
		String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
		System.out.println("字符串拼接:"+concat);
		// 求最小值，minValue = -3.0
		double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
		System.out.println("最小值:"+minValue);
		// 求和，sumValue = 10, 有起始值
		int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
		System.out.println("求和:"+sumValue);
		// 求和，sumValue = 10, 无起始值
		sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
		System.out.println("求和:"+sumValue);
		// 过滤，字符串连接，concat = "ace"
		concat = Stream.of("a", "B", "c", "D", "e", "F").
		 filter(x -> x.compareTo("Z") > 0).
		 reduce("", String::concat);
		System.out.println("过滤和字符串连接:"+concat);
		
		
		
	}
	
	public static void print(String text) {
		 // jdk1.8之前的写法
//		 if (text != null) {
//			 System.out.println(text);
//		 }
		 // jdk1.8的写法
		 Optional.ofNullable(text).ifPresent(System.out::println);
	}
	
	public static void getLength(String text) {
		 // jdk1.8之前的写法
		// return if (text != null) ? text.length() : -1;
		 // jdk1.8的写法
		int i=Optional.ofNullable(text).map(String::length).orElse(-1);
		System.out.println("数据:"+i);
	};
}
