package com.pancm.test.design.factory;


/**
 * Title: enumFactoryTest
 * Description: 枚举工厂类测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年10月13日
 */
public class enumFactoryTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Game game=GameS.DNF.playGame();
		game.play();
	}

}

/**
 * The enum Game s.
 */
enum GameS{
    /**
     * Dnf game s.
     */
//定义能玩的游戏
	DNF,
    /**
     * Lol game s.
     */
    LOL;

    /**
     * Play game game.
     *
     * @return the game
     */
//玩游戏
	public Game  playGame(){
		switch(this){
		case DNF:
			return new DNF();
		case LOL:
			return new LOL();
		default:
			throw new AssertionError("无效的参数类型");
		}
		
	}
	
}