package com.pancm.test.junitTest;

import org.junit.Test;

/**
 * @author ZERO
 * @Data 2017-5-18 上午10:05:43
 * @Description 
 */
public class junittest1 {
	
	@Test
	public int add(int a,int b){
		return a+b;		
	}
	
	@Test
	public int minus(int a,int b){
		return a-b;		
	}
	
	@Test
	public boolean compare(int a,int b){
		return a>b?true:false;		
	}
	
	
}
