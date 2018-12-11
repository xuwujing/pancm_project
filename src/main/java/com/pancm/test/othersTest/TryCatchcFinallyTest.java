package com.pancm.test.othersTest;

import java.util.HashMap;
import java.util.Map;


/**
 * @Title: TryCatchcFinallyTest
 * @Description:
 * @Version:1.0.0
 * @author pancm
 * @date 2018年12月11日
 */
public class TryCatchcFinallyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// try finally
		System.out.println("====" + test1()); // ==== 1
		
		test2();

		test3();
		
		/*
		 
		 一般情况下，finally语句一定会执行！但是在以下4种特殊情况下，finally块不会被执行：
		1）在finally语句块中发生了异常。
		2）在前面的代码中用了System.exit()退出程序。
		3）程序所在的线程死亡。
		4）关闭CPU。
		 */
	}

	/**
	 * 
	 */
	private static void test3() {
		int i[] = {1}; 
		int j=0;
		Map<String, Object> map=null;
		while(j<2){
		try {
			//这里会抛出空指针异常
			map.put("1", 2);
			//这里会抛出数组下标越界异常
			System.out.println(i[j++]);
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("数组下标越界异常!");
		}catch (NullPointerException e) {
			System.out.println("空指针异常!");
			map =new HashMap<String, Object>();
		}finally {
			System.out.println("===");
		}
		}
			
	}

	/**
	 * 
	 */
	private static void test2() {
		int i[] = {1,2,3}; 	
		int j=0;
		while(j<4) {
			try {
				System.out.println(i[j++]);
				//如果写成一下这种就会造成死循环！！！
				// System.out.println(i[j]); j++
			}catch(Exception e){
				System.out.println("数组下标越界!");
			}finally {
				System.out.println("===");
			}
			
		}
		
	}

	/**
	 * 
	 */
	@SuppressWarnings("finally")
	private static int test1() {
		try {
			return 0;
		} finally {
			return 1;
		}

	}

}
