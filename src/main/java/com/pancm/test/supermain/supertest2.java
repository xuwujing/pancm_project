package com.pancm.test.supermain;
/**
 * @author ZERO
 * @Data 2017-5-24 下午4:37:04
 * @Description 
 */
class Person {
	 
    public static void prt(String s) {
        System.out.println(s);
    }
 
    Person() {
        prt("A Person.");
    }
 
    Person(String name) {
        prt("A person name is:" + name);
 
    }
}
 
public class supertest2 extends Person {
    supertest2() {
        super(); // 调用父类构造函数（1）
        prt("A chinese.");// (4)
    }
 
    supertest2(String name) {
        super(name);// 调用父类具有相同形参的构造函数（2） 调用了Person中的Person(String name)方法
        prt("his name is:" + name);
    }
 
    supertest2(String name, int age) {
        this(name);// 调用当前具有相同形参的构造函数（3） 调用了supertest2(String name)
        prt("his age is:" + age);
    }
 
    public static void main(String[] args) {
        supertest2 cn = new supertest2(); // A Person.   A chinese.
        cn = new supertest2("kevin");    //A person name is:kevin   his name is:kevin
        cn = new supertest2("kevin", 22);//A person name is:kevin  his name is:kevin  his age is:22
    }
}
