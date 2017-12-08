package com.pancm.test.factorymMethodTest;

/**
 * 
* Title: factorymMethodTest
* Description: 最原始的工厂模式测试   
* Version:1.0.0  
* @author pancm
* @date 2017年10月13日
 */
public class factorymMethodTest {
	
	public static void main(String[] args) {
		//玩游戏
		Game game=Play.playGame(DNF.class);
	}
}

	//定义一个抽象事物
	interface Game{		 
	};
	//具体事物
	class DNF implements Game{
	};
	//具体事物
	class LOL implements Game{
	};
	
	//工厂类
	class Play{
		 //玩游戏
		 public static Game playGame(Class<? extends Game> c){
			 try{
				 return (Game)c.newInstance();
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 return null;
		 }
	}