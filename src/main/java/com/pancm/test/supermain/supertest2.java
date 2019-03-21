package com.pancm.test.supermain;

/**
 * The type Person.
 *
 * @author ZERO
 * @Data 2017 -5-24 下午4:37:04
 * @Description
 */
class Person {

    /**
     * Prt.
     *
     * @param s the s
     */
    public static void prt(String s) {
        System.out.println(s);
    }

    /**
     * Instantiates a new Person.
     */
    Person() {
        prt("A Person.");
    }

    /**
     * Instantiates a new Person.
     *
     * @param name the name
     */
    Person(String name) {
        prt("A person name is:" + name);
 
    }
}

/**
 * The type Supertest 2.
 */
public class supertest2 extends Person {
    /**
     * Instantiates a new Supertest 2.
     */
    supertest2() {
        super(); // 调用父类构造函数（1）
        prt("A chinese.");// (4)
    }

    /**
     * Instantiates a new Supertest 2.
     *
     * @param name the name
     */
    supertest2(String name) {
        super(name);// 调用父类具有相同形参的构造函数（2） 调用了Person中的Person(String name)方法
        prt("his name is:" + name);
    }

    /**
     * Instantiates a new Supertest 2.
     *
     * @param name the name
     * @param age  the age
     */
    supertest2(String name, int age) {
        this(name);// 调用当前具有相同形参的构造函数（3） 调用了supertest2(String name)
        prt("his age is:" + age);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        supertest2 cn = new supertest2(); // A Person.   A chinese.
        cn = new supertest2("kevin");    //A person name is:kevin   his name is:kevin
        cn = new supertest2("kevin", 22);//A person name is:kevin  his name is:kevin  his age is:22
    }
}
