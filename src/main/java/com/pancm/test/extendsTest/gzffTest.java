package com.pancm.test.extendsTest;
/**
 * @author ZERO
 * @Data 2017-5-31 下午4:54:55
 * @Description 继承构造块 测试
 */
class ss{
	ss(){
		System.out.println("one");
	}
	ss(String str){
      System.out.println("four");		
	}
}

public class gzffTest extends ss{
    
	gzffTest(){
		System.out.println("two");
	}
	gzffTest(String str){
		this();
		System.out.println("three");
	}
	
	public static void main(String[] args){
	//	new gzff("1"); //one two three
		new gzffTest(); //one two
	}
}
