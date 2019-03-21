package com.pancm.test.design.factory;

/**
 * Title: factorymMethodTest
 * Description: 最原始的工厂模式测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 3017年10月13日
 */
public class factorymMethodTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		//玩游戏
		Game3 game=Play3.playGame3(DNF3.class);
		game.play();
	}
}

/**
 * The interface Game 3.
 */
//定义一个抽象事物
	interface Game3{
    /**
     * Play.
     */
    void play();
	};

/**
 * The type Dnf 3.
 */
//具体事物
	class DNF3 implements Game3{

		@Override
		public void play() {
			System.out.println("正在玩DNF...");
		}
	};

/**
 * The type Lol 3.
 */
//具体事物
	class LOL3 implements Game3{

		@Override
		public void play() {
			System.out.println("正在玩LOL...");
		}
	};

/**
 * The type Play 3.
 */
//工厂类
	class Play3{
    /**
     * Play game 3 game 3.
     *
     * @param c the c
     * @return the game 3
     */
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