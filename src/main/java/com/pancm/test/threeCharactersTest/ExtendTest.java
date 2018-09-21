package com.pancm.test.threeCharactersTest;


/**
 * 
* @Title: ExtendTest
* @Description:
* 继承测试 
* @Version:1.0.0  
* @author pancm
* @date 2018年3月27日
 */
public class ExtendTest {

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

class  Animal{
    public void eat(String name){
        System.out.println(name+"正在吃东西...");
    }
    public void sleep(String name){
        System.out.println(name+"正在睡觉...");
    }
}

class Cat extends Animal{
    private String name="Cat";
    public void eat(){
        super.eat(name);
        System.out.println(name+"吃完了");
    }
    public void sleep(){
        this.sleep(name);
    }
    
    public void sleep(String name){
        System.out.println(name+"刚刚睡觉!");
    }
    
    public void climbTree(){
        System.out.println(name+"正在爬树!");
    }
}

class Dog extends Animal{
    
}