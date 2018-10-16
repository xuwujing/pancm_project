package com.pancm.test.cloneTest;
/**
 * 
* Title: Student
* Description: 浅克隆测试1
* Version:1.0.0  
* @author Administrator
* @date 2017-7-24
 */

//被复制的类需要实现Cloneable接口 重写Object方法
class Student implements Cloneable{  
    private int number;  

    public int getNumber() {  
        return number;  
    }  

    public void setNumber(int number) {  
        this.number = number;  
    }  

    @Override  
    public Object clone() {  
        Student stu = null;  
        try{  
            stu = (Student)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return stu;  
    }  
}  
public class cloneTest1 {  
    public static void main(String args[]) {  
        Student stu1 = new Student();  
        stu1.setNumber(12345);  
        Student stu2 = (Student)stu1.clone();  

        System.out.println("学生1:" + stu1.getNumber());  //12345 
        System.out.println("学生2:" + stu2.getNumber());  //12345
        
        stu2.setNumber(54321);  
        //说明拷贝成功
        System.out.println("学生1:" + stu1.getNumber());  //12345
        System.out.println("学生2:" + stu2.getNumber());  //54321
        
        System.out.println(stu1==stu2);//false
    }  
}  