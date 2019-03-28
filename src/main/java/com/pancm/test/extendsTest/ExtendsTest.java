package com.pancm.test.extendsTest;

/**
 * The type Extends test.
 *
 * @author ZERO
 * @Data 2017 -6-2 上午11:46:45
 * @Description 继承测试 经典题
 */
public class ExtendsTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        A a1 = new A();  
        A a2 = new B();  
        B b = new B();  
        C c = new C();  
        D d = new D();  
          
        System.out.println("1--" + a1.show(b));   //A and A
        System.out.println("2--" + a1.show(c));   //A and A
        System.out.println("3--" + a1.show(d));   //A and D
        System.out.println("4--" + a2.show(b));   //B and A
        System.out.println("5--" + a2.show(c));   //B and A
        System.out.println("6--" + a2.show(d));   //A and D
        System.out.println("7--" + b.show(b));    //B and B
        System.out.println("8--" + b.show(c));    //B and B
        System.out.println("9--" + b.show(d));    //A and D    
    }  
}

/**
 * The type A.
 */
class A {
    /**
     * Show string.
     *
     * @param obj the obj
     * @return the string
     */
    public String show(D obj) {
        return ("A and D");  
    }

    /**
     * Show string.
     *
     * @param obj the obj
     * @return the string
     */
    public String show(A obj) {
        return ("A and A");  
    }   
  
}

/**
 * The type B.
 */
class B extends A{
    /**
     * Show string.
     *
     * @param obj the obj
     * @return the string
     */
    public String show(B obj){
        return ("B and B");  
    }  
      
    public String show(A obj){  
        return ("B and A");  
    }   
}

/**
 * The type C.
 */
class C extends B{
  
}

/**
 * The type D.
 */
class D extends B{
  
}  

