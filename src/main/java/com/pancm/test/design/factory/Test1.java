/**
 * 
 */
package com.pancm.test.design.factory;

/**
 * The type Test 1.
 *
 * @author pancm
 * @Title: Test1
 * @Description: 简单工厂模式测试
 * @Version:1.0.0
 * @date 2018年7月23日
 */
public class Test1 {
	private static final String LOL="LOL"; 
	private static final String DNF="DNF";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		/**
		 * 简单工厂模式
		 * 根据条件决定一个接口由哪个具体产品类来实现
		 * 优点:
		 * 缺点:扩展性差
		 */
		Game game= ComputerFactory.playGame(LOL);
		Game game2= ComputerFactory.playGame(DNF);
		game.play();
		game2.play();
		
		/**
		 * 工厂方法模式
		 * 
		 * 优点:扩展性高
		 * 缺点:增加了复杂度
		 */
		Game game3=new LOLFactory().playGame();
		Game game4=new DNFFactory().playGame();
		Game game5=new WOWFactory().playGame();
		game3.play();
		game4.play();
		game5.play();
		
		
		/**
		 * 抽象工厂模式
		 * 
		 * 优点:
		 * 
		 */
		ComputerFactory3 cf3=new PVPFactory();
		cf3.playGame().play();
		cf3.playGame2().play();
		ComputerFactory3 cf4=new PVEFactory();
		cf4.playGame().play();
		cf4.playGame2().play();
		
		
	}
	
	
	
	
}

/**
 * 定义一个接口
 */
interface Game{
    /**
     * Play.
     */
    void play();
}

/**
 * 定义一个实现类
 */
class LOL implements Game{
	@Override
	public void play() {
		System.out.println("正在玩LOL...");
	}	
}

/**
 * The type Dnf.
 */
class DNF implements Game{
	@Override
	public void play() {
		System.out.println("正在玩DNF...");
	}	
}

/**
 * The type Wow.
 */
class WOW  implements Game{
	@Override
	public void play() {
		System.out.println("正在玩WOW...");
	}	
}

/**
 * 定义一个电脑
 */
class ComputerFactory{
	private static final String LOL="LOL"; 
	private static final String DNF="DNF";

    /**
     * Play game game.
     *
     * @param game the game
     * @return the game
     */
//玩游戏
	 public static Game playGame(String game){
		 if(LOL.equalsIgnoreCase(game)){
			 return new LOL();
		 }else if(DNF.equalsIgnoreCase(game)){
			 return new DNF();
		 }
		 return null;
	 }	
}

/**
 * The interface Computer factory 2.
 */
interface ComputerFactory2{
    /**
     * Play game game.
     *
     * @return the game
     */
    Game playGame();
}

/**
 * The type Lol factory.
 */
class LOLFactory implements ComputerFactory2{
	@Override
	public Game playGame() {
		return new LOL();
	}
}

/**
 * The type Dnf factory.
 */
class DNFFactory implements ComputerFactory2{
	@Override
	public Game playGame() {
		return new DNF();
	}
}

/**
 * The type Wow factory.
 */
class WOWFactory implements ComputerFactory2{
	@Override
	public Game playGame() {
		return new WOW();
	}
}

/**
 * The interface Computer factory 3.
 */
interface ComputerFactory3{
    /**
     * Play game game.
     *
     * @return the game
     */
    Game playGame();

    /**
     * Play game 2 game.
     *
     * @return the game
     */
    Game playGame2();
}

/**
 * The type Pvp factory.
 */
class PVPFactory implements ComputerFactory3{

	@Override
	public Game playGame() {
		return new LOL();
	}

	@Override
	public Game playGame2() {
		return new WOW();
	}
	
}

/**
 * The type Pve factory.
 */
class PVEFactory implements ComputerFactory3{

	@Override
	public Game playGame() {
		return new DNF();
	}

	@Override
	public Game playGame2() {
		return new WOW();
	}
	
}


