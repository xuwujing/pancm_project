package com.pancm.test.supermain;
/**
 * @author ZERO
 * @Data 2017-5-24 下午4:29:22
 * @Description 
 */
public class supertest1 {
 	static class Father {
		  Father(){
			System.out.println("father");
			  };
		  Father(int age){
			System.out.println("father is "+age); 
		  };
		}

 	static	class Son {
		  Son(){
			  super();
			  System.out.println("son");
			  };
		}
		
		public static  void main(String[] args){
			Father fa= new  Father();
			Son so=new Son();
		}
		
		
}

