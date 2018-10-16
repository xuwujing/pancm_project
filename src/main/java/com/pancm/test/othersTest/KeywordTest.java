package com.pancm.test.othersTest;

/**
 * 
* Title: keywordTest
* Description:
* 修饰符测试
* 主要分为  访问修饰符和非访问修饰符 
* Version:1.0.0  
* @author pancm
* @date 2018年3月19日
 */
public class KeywordTest {
    public  static int count=5;
    public KeywordTest(){
    }
    
	public static void main(String[] args) {
		
	}
	
	
	
	
	private void test1() {
		
	}

	private void test2() {

	}
	
	private void test3() {

	}
	
	
	private void test4() {

	}
	
	private class test{
		//修饰一个私有变量
		private int count=1;
		//修饰一个私有方法
		private int add(int i,int j){
			return i+j;
	   }
	}
}
