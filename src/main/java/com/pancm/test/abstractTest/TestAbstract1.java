package com.pancm.test.abstractTest;
/**
 * @author ZERO
 * @Data 2017-6-1 下午3:24:47
 * @Description  abstract 抽象类
 */
public class TestAbstract1{
	//抽象类： 不能手动创建对象
	public static void main(String[] args) {
		// Animal a=new Animal(); //error
		Animal a = null;
		a = new Dog();
	}
}
abstract class Animal{
}
class Dog extends Animal{
}
