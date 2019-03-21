package com.pancm.test.othersTest;

/**
 * Title: basicTest
 * Description:
 * 基本数据类型转换测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 2018年3月18日
 */
public class basicTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		
		Integer a=127;
		Integer b=127;
		Integer c=128;
		Integer d=128;
		System.out.println(a == b);
		System.out.println(a.equals(b));
		System.out.println(c == d);
		System.out.println(c.equals(d));
		
		
//		test1();
//		test2();
//		test3();
		test4();
//		test13();
	}
	
	private static void test1(){
		long bye=127;
		System.out.println("bye:"+bye);
	}
	
	private static void test2(){
		char a=1;
		char b = 'a';
		System.out.println("bye:"+a);
	}
	private static void test3(){
		short a=1;
		byte b=2;
		int c = 3;
		long d = 4;
		System.out.println(getType(a));
		System.out.println(getType(b));
		System.out.println(getType(c));
		System.out.println(getType(d));
		System.out.println(getType(a+b));
		System.out.println(getType(a+c));
		System.out.println(getType(c+d));
	}
	
	private static void test4(){
		short a=1,b=2;
		byte c=3,d=4;
		System.out.println(getType(a+b));
		System.out.println(getType(c+d));
	}
	
	private static void test11(){
		int i=10;
		float j=i;
		System.out.println("i:"+i+",j:"+j);
	}

	private static void test12(){
		int i=127;
		int j=128;
		byte bye=(byte)i;
		byte bye2=(byte)j;
		System.out.println("i:"+i+",bye:"+bye);
		System.out.println("j:"+j+",bye2:"+bye2);
	}
	
	private static void test13(){
		double d=10.1111115;
		float f=(float) d;
		System.out.println("f:"+f+",d:"+d);
	}

    /**
     * Get type string.
     *
     * @param o the o
     * @return the string
     */
//获取变量类型方法
	public static String getType(Object o){ 
		return o.getClass().toString(); 
	} 
}
