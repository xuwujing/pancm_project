package com.pancm.test.design.mediator;

/**
 * The type Mediator test.
 *
 * @author pancm
 * @Title: MediatorTest
 * @Description: 中介者模式 中介者模式（Mediator Pattern）是用来降低多个对象和类之间的通信复杂性。               这种模式提供了一个中介类，该类通常处理不同类之间的通信，并支持松耦合，使代码易于维护。中介者模式属于行为型模式。               用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class MediatorTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
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
		 * 基本角色
		 抽象中介者(Mediator): 。定义了同事对象到中介者对象之间的接口。
                      具体中介者(ConcreteMediator): 实现抽象中介者的方法，它需要知道所有的具体同事类，同时需要从具体的同事类那里接收信息，并且向具体的同事类发送信息。
                     抽象同事类(Colleague): 定义了中介者对象的接口，它只知道中介者而不知道其他的同事对象。
		具体同事类(ConcreteColleague) : 每个具体同事类都只需要知道自己的行为即可，但是他们都需要认识中介者。
		 * 
		 */
		
		JavaQQqun jq = new JavaQQqun();
        
		ZhangSan zs = new ZhangSan("张三", jq);
		XuWuJing xwj = new XuWuJing("xuwujing", jq);
		jq.setZs(zs);
		jq.setXwj(xwj);
        
		zs.talk("大家好！我是张三!");;
		xwj.talk("欢迎你！张三！");
		
		
		/*
		 * 优点： 1、降低了类的复杂度，将一对多转化成了一对一。 2、各个类之间的解耦。 3、符合迪米特原则。
			缺点：中介者会庞大，变得复杂难以维护。
		 */
		
	}

}

/**
 * The type Student.
 */
class Student {
	private String name;

    /**
     * Instantiates a new Student.
     *
     * @param name the name
     */
    public Student(String name) {
		super();
		this.name = name;
	}

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
		return name;
	}

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
		this.name = name;
	}

    /**
     * Show message.
     *
     * @param msg the msg
     */
    public void showMessage(String msg) {
		ChatRoom.showMessage(this, msg);
	}
	
}

/**
 * The type Chat room.
 */
class ChatRoom{
    /**
     * Show message.
     *
     * @param student the student
     * @param msg     the msg
     */
    public static void showMessage(Student student,String msg){
		System.out.println(student.getName()+":"+msg);
	}
}

/**
 * The interface Q qqun.
 */
//定义一个中介者 QQ群
interface QQqun {
    /**
     * Exchange.
     *
     * @param person  the person
     * @param message the message
     */
//提供一个交流的方法
	void exchange(Person person,String message);
}

/**
 * The type Person.
 */
//定义一个抽象同事类
abstract class Person{
    /**
     * The Name.
     */
    protected String name;
    /**
     * The Qun.
     */
    protected QQqun qun;

    /**
     * Instantiates a new Person.
     *
     * @param name the name
     * @param qun  the qun
     */
    Person(String name,QQqun qun){
        this.name = name;
        this.qun = qun;
    }
}

/**
 * The type Zhang san.
 */
class ZhangSan extends Person{

    /**
     * Instantiates a new Zhang san.
     *
     * @param name the name
     * @param qun  the qun
     */
    ZhangSan(String name, QQqun qun) {
		super(name, qun);
	}

    /**
     * Exchange.
     *
     * @param message the message
     */
    void exchange(String message){
		qun.exchange(this,message);
    }

    /**
     * Talk.
     *
     * @param message the message
     */
    void talk(String message){
        System.out.println(name +"说：" + message);
    }
}

/**
 * The type Xu wu jing.
 */
class XuWuJing extends Person{

    /**
     * Instantiates a new Xu wu jing.
     *
     * @param name the name
     * @param qun  the qun
     */
    XuWuJing(String name, QQqun qun) {
		super(name, qun);
	}

    /**
     * Exchange.
     *
     * @param message the message
     */
    void exchange(String message){
		qun.exchange(this,message);
    }

    /**
     * Talk.
     *
     * @param message the message
     */
    void talk(String message){
        System.out.println(name +"回应：" + message);
    }
}

/**
 * The type Java q qqun.
 */
//定义一个JavaQQ群
class JavaQQqun implements QQqun{
    private ZhangSan zs;
    private XuWuJing xwj;

    /**
     * Gets zs.
     *
     * @return the zs
     */
    public ZhangSan getZs() {
		return zs;
	}

    /**
     * Sets zs.
     *
     * @param zs the zs
     */
    public void setZs(ZhangSan zs) {
		this.zs = zs;
	}

    /**
     * Gets xwj.
     *
     * @return the xwj
     */
    public XuWuJing getXwj() {
		return xwj;
	}


    /**
     * Sets xwj.
     *
     * @param xwj the xwj
     */
    public void setXwj(XuWuJing xwj) {
		this.xwj = xwj;
	}


	@Override
	public void exchange(Person person, String message) {
			if(zs.equals(person)){
				zs.talk(message);
			}else if(xwj.equals(person)){
				xwj.talk(message);
			}
	}
}
