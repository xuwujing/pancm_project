package com.pancm.test.threeCharactersTest;

/**
 * The type Polymorphic test.
 *
 * @author pancm
 * @Title: PolymorphicTest
 * @Description: 多态测试
 * @Version:1.0.0
 * @date 2018年3月27日
 */
public class PolymorphicTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Animal2 animal = new Cat2();
		animal.eat();
	}
}

/**
 * The type Animal 2.
 */
class Animal2 {
	private String name = "Animal";

    /**
     * Eat.
     */
    public void eat() {
		System.out.println(name + "正在吃东西...");
		sleep();
	}

    /**
     * Sleep.
     */
    public void sleep() {
		System.out.println(name + "正在睡觉...");
	}
}

/**
 * The type Cat 2.
 */
class Cat2 extends Animal2 {
	private String name = "Cat";

    /**
     * Eat.
     *
     * @param name the name
     */
    public void eat(String name) {
		System.out.println(name + "吃完了");
		sleep();
	}

	public void sleep() {
		System.out.println(name + "正在睡觉");
	}
}
