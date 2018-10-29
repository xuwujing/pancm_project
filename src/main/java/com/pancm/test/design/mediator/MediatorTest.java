package com.pancm.test.design.mediator;

/**
 * @Title: MediatorTest
 * @Description: 中介者模式 中介者模式（Mediator Pattern）是用来降低多个对象和类之间的通信复杂性。
 *               这种模式提供了一个中介类，该类通常处理不同类之间的通信，并支持松耦合，使代码易于维护。中介者模式属于行为型模式。
 *               用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。
 * @Version:1.0.0
 * @author pancm
 * @date 2018年8月8日
 */
public class MediatorTest {

	public static void main(String[] args) {
		
		/*
		 * 基本使用
		 */
		Student student1 =new Student("zhangsan");
		Student student2 =new Student("lisi");
		Student student3 =new Student("xuwujing");
		student1.showMessage("hello!");
		student2.showMessage("hi!");
		student3.showMessage("bye bye!");
		
		/*
		 * 优点： 1、降低了类的复杂度，将一对多转化成了一对一。 2、各个类之间的解耦。 3、符合迪米特原则。
			缺点：中介者会庞大，变得复杂难以维护。
		 */
		
	}

}

class Student {
	private String name;
	
	public Student(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void showMessage(String msg) {
		ChatRoom.showMessage(this, msg);
	}
	
}

class ChatRoom{
	public static void showMessage(Student student,String msg){
		System.out.println(student.getName()+":"+msg);
	}
}
