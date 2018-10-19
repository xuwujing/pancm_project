package com.pancm.test.calculate;

import java.math.BigInteger;

/**
 * @Title: CalculateTest
 * @Description: 运算符相关类
 * @Version:1.0.0
 * @author pancm
 * @date 2018年10月18日
 */
public class CalculateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//自增运算符测试
		test1();
		//运算符优先级测试
		test2();
		//位运算符
		test3();
		//赋值运算符
		test4();
	}

	private static void test4() {
		
	}

	private static void test3() {
		
	}
	
	private static void test2() {
		int a = 2+3*4;
		int b = 2>>3;
		int c = a++*3-b--;
		
		System.out.println("运算符优先级测试开始");
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println("运算符优先级测试结束");
		/*
		 * 最高优先级的运算符在的表的最上面，最低优先级的在表的底部。
		 类别	操作符	关联性
		后缀	() [] . (点操作符)	左到右
		一元	+ + - ！〜	从右到左
		乘性 	* /％	左到右
		加性 	+ -	左到右
		移位 	>> >>>  << 	左到右
		关系 	>> = << = 	左到右
		相等 	==  !=	左到右
		按位与	＆	左到右
		按位异或	^	左到右
		按位或	|	左到右
		逻辑与	&&	左到右
		逻辑或	| |	左到右
		条件	？：	从右到左
		赋值	= + = - = * = / =％= >> = << =＆= ^ = | =	从右到左
		逗号	，	左到右
		 */
		
	}

	private static void test1() {
		 int a=1,z=1;
		 int b = a++;
		 int c = ++a;
		 int x = 2*++a;
	     int y = 2*b++;
		System.out.println("自增运算符测试开始");
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println(z++);
		System.out.println(++z);
		System.out.println(x);
		System.out.println(y);
		System.out.println("自增加运算符测试结束");

	}

	
	
	
	private static void test10() {
		int i = 16;
		// 16转换的二进制数据
		int j = 10000;
		// 8转换的二进制数据
		int k = 1000;
		// 4转换的二进制数据
		int m = 100;
		// 32转换的二进制数据
		int n = 100000;
		System.out.println("--" + (j & i));
		System.out.println("--" + (k & i));
		System.out.println("--" + (m & i));
		System.out.println("--" + (n & i));
		System.out.println("--" + decimal2Binary(i));
		System.out.println("--" + biannary2Decimal(n));
		//十进制转二进制
		System.out.println("--" + Integer.toBinaryString(i));
	    /*
	     *  1. BigInteger的构造函数 
		    BigInteger(String src)默认参数字符串为10进制数值 
		    BigInteger(String src, int x)第2个参数x是指定第一个参数src的进制类型 
		    2. toString方法 
		    toString()默认把数值按10进制数值转化为字符串。 
		    toString(int x)把数值按参数x的进制转化为字符串
	     */
		System.out.println("--" + new BigInteger(String.valueOf(i)).toString(2));
		//二进制转十进制
		System.out.println("--" + new BigInteger(String.valueOf(j),2).toString());
	}
	
	/**
	 * 十进制转二进制
	 */
	public static String decimal2Binary(int de) {
		String numstr = "";
		while (de > 0) {
			int res = de % 2; // 除2 取余数作为二进制数
			numstr = res + numstr;
			de = de / 2;
		}
		return numstr;
	}

	/**
	 * 将二进制转换为10进制
	 * @param bi ：待转换的二进制
	 * @return
	 */
	public static Integer biannary2Decimal(int bi) {
		String binStr = bi + "";
		Integer sum = 0;
		int len = binStr.length();
		for (int i = 1; i <= len; i++) {
			// 第i位 的数字为：
			int dt = Integer.parseInt(binStr.substring(i - 1, i));
			sum += (int) Math.pow(2, len - i) * dt;
		}
		return sum;
	}

}
