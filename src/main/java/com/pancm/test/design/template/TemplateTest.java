package com.pancm.test.design.template;

/**
 * @Title: TemplateTest
 * @Description: 模板模式（Template Pattern）中，一个抽象类公开定义了执行它的方法的方式/模板。
 *               它的子类可以按需要重写方法实现，但调用将以抽象类中定义的方式进行。 这种类型的设计模式属于行为型模式。
 *               定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。
 *               模板方法使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。
 * @Version:1.0.0
 * @author pancm
 * @date 2018年8月8日
 */
public class TemplateTest {

	public static void main(String[] args) {

		/*
		 * 基本使用
		 */
		Game game = new Cricket();
		game.play();
		System.out.println();
		game = new Football();
		game.play();

		/*
		 * 优点： 1、封装不变部分，扩展可变部分。 2、提取公共代码，便于维护。 3、行为由父类控制，子类实现。
		 * 缺点：每一个不同的实现都需要一个子类来实现，导致类的个数增加，使得系统更加庞大。 使用场景： 1、有多个子类共有的方法，且逻辑相同。
		 * 2、重要的、复杂的方法，可以考虑作为模板方法。 注意事项：为防止恶意操作，一般模板方法都加上 final 关键词。
		 * 
		 */

	}

}

//定义一个游戏
abstract class Game {
	abstract void initialize();

	abstract void startPlay();

	abstract void endPlay();

	// 模板
	public final void play() {
		// 初始化游戏
		initialize();
		// 开始游戏
		startPlay();
		// 结束游戏
		endPlay();
	}
}

//扩展游戏类
class Cricket extends Game {

	@Override
	void endPlay() {
		System.out.println("Cricket Game Finished!");
	}

	@Override
	void initialize() {
		System.out.println("Cricket Game Initialized! Start playing.");
	}

	@Override
	void startPlay() {
		System.out.println("Cricket Game Started. Enjoy the game!");
	}
}

class Football extends Game {

	@Override
	void endPlay() {
		System.out.println("Football Game Finished!");
	}

	@Override
	void initialize() {
		System.out.println("Football Game Initialized! Start playing.");
	}

	@Override
	void startPlay() {
		System.out.println("Football Game Started. Enjoy the game!");
	}
}
