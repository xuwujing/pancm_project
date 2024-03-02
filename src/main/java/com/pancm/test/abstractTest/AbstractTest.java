package com.pancm.test.abstractTest;

/**
 * The type Abstract test.
 *
 * @author ZERO
 * @Data 2017 -6-1 下午3:24:47
 * @Description abstract 抽象类测试
 */
public class AbstractTest{
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		//这句会报错，因为抽象类不能实例化
		// Animal a=new Animal();
		Animal a = new Dog();
		a.show();
	}
}

/**
 * The type Animal.
 */
abstract class Animal{
    /**
     * Show.
     */
    abstract void show();
    /**
     * Print.
     */
    public void print(){
		System.out.println("Animal");
	}
}

/**
 * The type Dog.
 */
class Dog extends Animal{
	@Override
	void show() {
		System.out.println("This is Dog!");
	}
}

