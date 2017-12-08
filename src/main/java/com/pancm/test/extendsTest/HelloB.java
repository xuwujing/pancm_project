package com.pancm.test.extendsTest;
/**
 * @author ZERO
 * @Data 2017-5-31 下午3:51:59
 * @Description static块,构造方法，构造块
 */


class HelloA {

    public HelloA() {
        System.out.println("HelloA"); //5
    }
    
    { System.out.println("I'm A class"); } //4
    
    static { System.out.println("static A"); } //1

}
public class HelloB extends HelloA{
    public HelloB() {
        System.out.println("HelloB");  //7
    }
    
    { System.out.println("I'm B class"); }  //6
    
    static { System.out.println("static B"); }  //2
    
    public static void main(String[] args) {
      //  new HelloA();  extends HelloA
    	new HelloB();   //3
        /*
         * 总结:创建对象时构造器的调用顺序是：先初始化静态成员，然后调用父类构造器，再初始化非静态成员，最后调用自身构造器。
         */
    	
    	
    	
        System.out.println(getValue(2)); //10
}
  
    public static int getValue(int i) {
        int result = 0;
//        switch (i) {
//        case 1:
//            result = result + i;
//        case 2:
//            result = result + i * 2; //因为没有break，所以会继续执行
//        case 3:
//            result = result + i * 3;
//        }
        return result;
    }
}
