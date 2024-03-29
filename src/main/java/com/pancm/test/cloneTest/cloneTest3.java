package com.pancm.test.cloneTest;

/**
 * Title: cloneTest1
 * Description: 浅克隆测试3
 * Version:1.0.0
 *
 * @author Administrator
 * @date 2017 -7-24
 */

//该对象继承Cloneable
 class Address3 implements Cloneable {  
	private String add;

    /**
     * Gets add.
     *
     * @return the add
     */
    public String getAdd() {
         return add;  
     }

    /**
     * Sets add.
     *
     * @param add the add
     */
    public void setAdd(String add) {
         this.add = add;  
     }   
     
     @Override 
     public Object clone(){
    	 Address3 addr=null;
    	 try{
    		 addr=(Address3)super.clone();
    	 }catch(CloneNotSupportedException e){
    		 e.printStackTrace();
    	 }
    	return addr;
     }
 }

/**
 * The type Student 3.
 */
//此对象依旧继承 Cloneable
 class Student3 implements Cloneable{  
     private int number;  
   
     private Address3 addr;

    /**
     * Gets addr.
     *
     * @return the addr
     */
    public Address3 getAddr() {
         return addr;  
     }

    /**
     * Sets addr.
     *
     * @param addr the addr
     */
    public void setAddr(Address3 addr) {
         this.addr = addr;  
     }

    /**
     * Gets number.
     *
     * @return the number
     */
    public int getNumber() {
         return number;  
     }

    /**
     * Sets number.
     *
     * @param number the number
     */
    public void setNumber(int number) {
         this.number = number;  
     }  
       
     @Override  
     public Object clone() {  
         Student3 stu = null;  
         try{  
             stu = (Student3)super.clone();   //浅复制 
         }catch(CloneNotSupportedException e) {  
             e.printStackTrace();  
         }  
         stu.addr = (Address3)addr.clone();   //深度复制 
         return stu;  
     }  
 }

/**
 * The type Clone test 3.
 */
public class cloneTest3 {
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]) {
           
         Address3 addr = new Address3();  
         addr.setAdd("杭州市");  
         Student3 stu1 = new Student3();  
         stu1.setNumber(123);  
         stu1.setAddr(addr);  
         Student3 stu2 = (Student3)stu1.clone();  
          
          //学生1:123,地址:杭州市
         System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());  
         //学生2:123,地址:杭州市
         System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());  
         
         addr.setAdd("武汉市");
         //学生1:123,地址:武汉市
         System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());  
         //学生1:123,地址:杭州市
         System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());  
     /**
      * 是浅复制只是复制了addr变量的引用，并没有真正的开辟另一块空间，将值复制后再将引用返回给新对象。
		所以，为了达到真正的复制对象，而不是纯粹引用复制。
      */
     
     }  
}  
