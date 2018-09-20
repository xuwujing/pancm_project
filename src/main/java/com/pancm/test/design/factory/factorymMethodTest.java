package com.pancm.test.design.factory;

/**
 * 
* Title: factorymMethodTest
* Description: 最原始的工厂模式测试   
* Version:1.0.0  
* @author pancm
* @date 3017年10月13日
 */
public class factorymMethodTest {
	
	public static void main(String[] args) {
		//玩游戏
		Game3 game=Play3.playGame3(DNF3.class);
	}
}

	//定义一个抽象事物
	interface Game3{		 
	};
	//具体事物
	class DNF3 implements Game3{
	};
	//具体事物
	class LOL3 implements Game3{
	};
	
	//工厂类
	class Play3{
		 //玩游戏
		 public static Game3 playGame3(Class<? extends Game3> c){
			 try{
				 return (Game3)c.newInstance();
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 return null;
		 }
	}