package com.pancm.test.ptTest;

/**
 * 
* Title: intTest
* Description:int和Integer 
* Version:1.0.0  
* @author pancm
* @date 2017年10月12日
 */
public class intTest {

	public static void main(String[] args) {
		Integer i=new Integer(10);
		Integer j=new Integer(10);
		compareTo(i,j);
		compareTo(127,127);
		compareTo(128,128);
		
	}
    
	public static void compareTo(Integer i,Integer j){
		System.out.println(i==j); 
		System.out.println(i.equals(j)); 
	}
}
