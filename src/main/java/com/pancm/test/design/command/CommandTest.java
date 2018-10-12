package com.pancm.test.design.command;

import java.util.ArrayList;
import java.util.List;

/**
* @Title: CommandTest
* @Description:命令模式 
  命令模式（Command Pattern）是一种数据驱动的设计模式，它属于行为型模式。
  请求以命令的形式包裹在对象中，并传给调用对象。调用对象寻找可以处理该命令的合适的对象，并把该命令传给相应的对象，该对象执行命令.
  核心:将一个请求封装成一个对象，从而可以用不同的请求对客户进行参数化。
* @Version:1.0.0  
* @author pancm
* @date 2018年8月8日
*/
public class CommandTest {

	public static void main(String[] args) {
		
		/*
		 * 基本使用
		 */
		String name = "xuwujing";
		Person person = new  Person();
		Command command1 = new ACommand(person);
		Command command2 = new BCommand(person);
		Invoker invoker =new Invoker();
		invoker.setCommand(command1);
		invoker.setCommand(command2);
		invoker.executeCommand(name);
		
		/*
		 * 优点： 1、降低了系统耦合度。 2、新的命令可以很容易添加到系统中去。
		         缺点：使用命令模式可能会导致某些系统有过多的具体命令类。
		 */
	}

}

//定义一个人，有一些方法
class Person{
	void eatFood(String name){
		System.out.println(name+"在吃饭");
	}
	void running(String name){
		System.out.println(name+"在跑步");
	}
}

//定义一个命令抽象类
abstract class Command{
	protected Person person;
	public Command(Person person){
		this.person = person;
	}
	//执行方法
	abstract void execute(String name);
}

//将一个接收者和动作进行绑定，调用接收者相应的操作
class ACommand extends Command{
	public ACommand(Person person) {
		super(person);
	}
	@Override
	void execute(String name) {
		person.eatFood(name);
	}
}

//将一个接收者和动作进行绑定，调用接收者相应的操作
class BCommand extends Command{
	public BCommand(Person person) {
		super(person);
	}
	@Override
	void execute(String name) {
		person.running(name);
	}
}


//用于执行这个请求
class Invoker {
	private List<Command> commands = new ArrayList<Command>();
	public void setCommand(Command command) {
		commands.add(command);
	}
	public void executeCommand(String name) {
		commands.forEach(command->{
			command.execute(name);
		});
	}
}