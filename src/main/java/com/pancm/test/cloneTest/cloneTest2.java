package com.pancm.test.cloneTest;
/**
 * 
* Title: cloneTest1
* Description: 浅克隆测试2
* Version:1.0.0  
* @author Administrator
* @date 2017-7-24
 */
//该对象不继承Cloneable
 class Address  {  
	    private String add;  
	  
    public String getAdd() {  
         return add;  
     }  
   
     public void setAdd(String add) {  
         this.add = add;  
     }         
 }  
   
 //此对象依旧继承 Cloneable
 class Student2 implements Cloneable{  
     private int number;  
   
     private Address addr;  
       
     public Address getAddr() {  
         return addr;  
     }  
   
     public void setAddr(Address addr) {  
         this.addr = addr;  
     }  
   
     public int getNumber() {  
         return number;  
     }  
   
     public void setNumber(int number) {  
         this.number = number;  
     }  
       
     @Override  
     public Object clone() {  
         Student2 stu = null;  
         try{  
             stu = (Student2)super.clone();  
         }catch(CloneNotSupportedException e) {  
             e.printStackTrace();  
         }  
         return stu;  
     }  
 }  
 
 public class cloneTest2 {  
     public static void main(String args[]) {  
           
         Address addr = new Address();  
         addr.setAdd("杭州市");  
         Student2 stu1 = new Student2();  
         stu1.setNumber(123);  
         stu1.setAddr(addr);  
         Student2 stu2 = (Student2)stu1.clone();  
          
          //学生1:123,地址:杭州市
         System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());  
         //学生2:123,地址:杭州市
         System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());  
         
         addr.setAdd("深圳市");
         //学生1:123,地址:深圳市
         System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());  
         //学生1:123,地址:深圳市
         System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());  
     /**
      * 是浅复制只是复制了addr变量的引用，并没有真正的开辟另一块空间，将值复制后再将引用返回给新对象。
		所以，为了达到真正的复制对象，而不是纯粹引用复制。
      */
     
     }  
}  
