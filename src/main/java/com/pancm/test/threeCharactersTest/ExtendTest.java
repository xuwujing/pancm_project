package com.pancm.test.threeCharactersTest;


/**
 * The type Extend test.
 *
 * @author pancm
 * @Title: ExtendTest
 * @Description: 继承测试
 * @Version:1.0.0
 * @date 2018年3月27日
 */
public class ExtendTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Cat cat=new Cat();
        Dog dog=new Dog();
        cat.eat();
        cat.sleep("cat");
        cat.climbTree();
        dog.eat("dog");
        dog.sleep("dog");
    }
}

/**
 * The type Animal.
 */
class  Animal{
    /**
     * Eat.
     *
     * @param name the name
     */
    public void eat(String name){
        System.out.println(name+"正在吃东西...");
    }

    /**
     * Sleep.
     *
     * @param name the name
     */
    public void sleep(String name){
        System.out.println(name+"正在睡觉...");
    }
}

/**
 * The type Cat.
 */
class Cat extends Animal{
    private String name="Cat";

    /**
     * Eat.
     */
    public void eat(){
        super.eat(name);
        System.out.println(name+"吃完了");
    }

    /**
     * Sleep.
     */
    public void sleep(){
        this.sleep(name);
    }
    
    public void sleep(String name){
        System.out.println(name+"刚刚睡觉!");
    }

    /**
     * Climb tree.
     */
    public void climbTree(){
        System.out.println(name+"正在爬树!");
    }
}

/**
 * The type Dog.
 */
class Dog extends Animal{
    
}