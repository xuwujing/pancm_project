package com.pancm.test.design.command;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command test.
 *
 * @author pancm
 * @Title: CommandTest
 * @Description:命令模式 命令模式 （Command Pattern）是一种数据驱动的设计模式，它属于行为型模式。请求以命令的形式包裹在对象中，并传给调用对象。调用对象寻找可以处理该命令的合适的对象，并把该命令传给相应的对象，该对象执行命令.核心:将一个请求封装成一个对象，从而可以用不同的请求对客户进行参数化。
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class CommandTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		
		/*
		 * 基本使用
		 * 所需的角色
		  1.命令对象：通过接口或抽象类声明实现的方法。
 		  2.命令执行对象：实现命令对象的方法，并将一个接收者和动作进行绑定，调用接收者相应的操作。
 		  3.命令请求对象：用于执行这个请求，可以动态的对命令进行控制。
 		  
 		 核心: 
 		  命令模式就是将一组对象的相似行为，进行了抽象，将调用者与被调用者之间进行解耦，提高了应用的灵活性。命令模式将调用的目标对象的一些异构性给封装起来，通过统一的方式来为调用者提供服
		 */
		
		
		String name = "xuwujing";
		Student student = new  Student();
		Command command1 = new LiTeacher(student);
		Command command2 = new WangTeacher(student);
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

/**
 * The type Student.
 */
//定义一个学生
class Student{

    /**
     * Clean class room.
     *
     * @param name the name
     */
    void cleanClassRoom(String name){
		System.out.println(name+" 开始打扫教室...");
	}

    /**
     * Do home work.
     *
     * @param name the name
     */
    void doHomeWork(String name){
		System.out.println(name+" 开始做作业...");
	}
}

/**
 * The type Command.
 */
//定义一个命令抽象类
abstract class Command{
    /**
     * The Student.
     */
    protected Student student;

    /**
     * Instantiates a new Command.
     *
     * @param student the student
     */
    public Command(Student student){
		this.student = student;
	}

    /**
     * Execute.
     *
     * @param name the name
     */
//执行方法
	abstract void execute(String name);
}

/**
 * The type Li teacher.
 */
//将一个接收者和动作进行绑定，调用接收者相应的操作
class LiTeacher extends Command{
    /**
     * Instantiates a new Li teacher.
     *
     * @param student the student
     */
    public LiTeacher(Student student) {
		super(student);
	}
	@Override
	void execute(String name) {
		student.cleanClassRoom(name);
	}
}

/**
 * The type Wang teacher.
 */
//将一个接收者和动作进行绑定，调用接收者相应的操作
class WangTeacher extends Command{
    /**
     * Instantiates a new Wang teacher.
     *
     * @param student the student
     */
    public WangTeacher(Student student) {
		super(student);
	}
	@Override
	void execute(String name) {
		student.doHomeWork(name);
	}
}


/**
 * The type Invoker.
 */
//用于执行这个请求
class Invoker {
	private List<Command> commands = new ArrayList<Command>();

    /**
     * Sets command.
     *
     * @param command the command
     */
//添加这个命令
	public void setCommand(Command command) {
		//设置执行命令的条件
		if(commands.size()>0) {
			System.out.println("不执行 WangTeacher 的命令!");
		}else {
			commands.add(command);
		}
	}

    /**
     * Execute command.
     *
     * @param name the name
     */
//执行这个命令
	public void executeCommand(String name) {
		commands.forEach(command->{
			command.execute(name);
		});
	}

    /**
     * Undo command.
     *
     * @param command the command
     */
//撤销这个命令
	public void undoCommand(Command command) {
		commands.remove(command);
		System.out.println("撤销该命令!");
	}
	
}