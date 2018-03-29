package com.pancm.test.threeCharactersTest;


/**
 * 
* @Title: extendTest
* @Description:
* 继承测试 
* @Version:1.0.0  
* @author pancm
* @date 2018年3月27日
 */
public class extendTest {

	public static void main(String[] args) {
		A a=new A();
		A a1=new B();
		System.out.println(a.show());
		System.out.println(a1.show());
	}

}
class A {  
    public String show() {  
        return "A";  
    } 
    
}  
 class B extends A{  
    public String show(){  
        return "B";  
    }  
    
    public void show1(){  
       System.out.println(this.show());
    } 
    
    public void show2(){  
      System.out.println(super.show());
    } 
} 