package com.pancm.test.abstractTest;
/**
 * @author ZERO
 * @Data 2017-6-1 下午3:24:47
 * @Description  abstract 抽象类测试
 */
public class AbstractTest{
	public static void main(String[] args) {
		//这句会报错，因为抽象类不能实例化
		// Animal a=new Animal();
		Animal a = new Dog();
		a.show();
	}
}
abstract class Animal{
	abstract void show();
	public void print(){
		System.out.println("Animal");
	}
}
class Dog extends Animal{
	@Override
	void show() {
		System.out.println("This is Dog!");
	}
}

