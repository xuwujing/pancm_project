package com.pancm.test.design.responsibility;

/**
 * The type Responsibility test.
 *
 * @author pancm
 * @Title: ResponsibilityTest
 * @Description: 责任链模式 顾名思义，责任链模式（Chain of Responsibility Pattern）为请求创建了一个接收者对象的链。这种模式给予请求的类型，对请求的发送者和接收者进行解耦。这种类型的设计模式属于行为型模式。在这种模式中，通常每个接收者都包含对另一个接收者的引用。如果一个对象不能处理该请求，那么它会把相同的请求传给下一个接收者，依此类推。
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class ResponsibilityTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		
		/*
		 * 最基本的使用
		 */
		
		// 组装责任链		
		Handler handler1 = new ConcreteHandler();		
		Handler handler2 = new ConcreteHandler();		
		handler1.setSuccessor(handler2);		
		// 提交请求		
		handler1.handleRequest();
		
		
		/*
		 * 通过条件判断是否能够处理，符合就行处理，否则就转交给下一个进行处理
		 */
		String name = "xuwujing";
		String something = "去聚餐";
		String something2 = "去旅游";
		Learder learder1 =new Supervisor(name, something);
		Learder learder2 =new BranchManager(name, something);
		Learder learder3 =new GeneralManager(name, something);
		learder1.setLearder(learder2);
		learder2.setLearder(learder3);
		learder1.handler(1);
		
		Learder learder4 =new Supervisor(name, something2);
		Learder learder5 =new BranchManager(name, something2);
		Learder learder6 =new GeneralManager(name, something2);
		learder4.setLearder(learder5);
		learder5.setLearder(learder6);
		learder4.handler(0);
		
		/*
		 * 优点： 1、降低耦合度。它将请求的发送者和接收者解耦。 2、简化了对象。使得对象不需要知道链的结构。 3、增强给对象指派职责的灵活性。通过改变链内的成员或者调动它们的次序，允许动态地新增或者删除责任。 4、增加新的请求处理类很方便。

		缺点： 1、不能保证请求一定被接收。 2、系统性能将受到一定影响，而且在进行代码调试时不太方便，可能会造成循环调用。 3、可能不容易观察运行时的特征，有碍于除错。
		 */
	}

}

/**
 * The type Handler.
 */
// 定义一个抽象类
abstract class Handler {
    /**
     * The Successor.
     */
    protected Handler successor;

    /**
     * 示意处理请求的方法，虽然这个示意方法是没有传入参数的 但实际是可以传入参数的，根据具体需要来选择是否传递参数
     */
    public abstract void handleRequest();

    /**
     * 取值方法  @return the successor
     */
    public Handler getSuccessor() {
		return successor;
	}

    /**
     * 赋值方法，设置后继的责任对象  @param successor the successor
     */
    public void setSuccessor(Handler successor) {
		this.successor = successor;
	}

}

/**
 * The type Concrete handler.
 */
// 具体的业务
class ConcreteHandler extends Handler {
	@Override
	public void handleRequest() {
		/** * 判断是否有后继的责任对象 如果有，就转发请求给后继的责任对象 如果没有，则处理请求 */
		if (getSuccessor() != null) {
			System.out.println("放过请求");
			getSuccessor().handleRequest();
		} else {
			System.out.println("处理请求");
		}
	}
}


/**
 * The type Learder.
 */
//定义一个抽象的领导
abstract class Learder{
    /**
     * The Learder.
     */
    protected Learder learder;

    /**
     * Set learder.
     *
     * @param learder the learder
     */
    protected void setLearder(Learder learder){
		this.learder=learder;
	}

    /**
     * Get learder learder.
     *
     * @return the learder
     */
    protected Learder getLearder(){
		return learder;
	}

    /**
     * Handler.
     *
     * @param level the level
     */
    abstract void handler(int  level);
}

/**
 * The type Supervisor.
 */
//主管
class Supervisor extends Learder{
	 private String name;
	 private String something;

    /**
     * Instantiates a new Supervisor.
     *
     * @param name      the name
     * @param something the something
     */
    public Supervisor(String name,String something) {
		this.name=name;
		this.something=something;
	}
	
	@Override
	void handler(int level) {
		//如果级别在自己的处理范围之内
		if(level>1){
			System.out.println("主管同意了  "+name+"所述的<"+something+">事情!");
		}else{
			System.out.println("主管未能处理  "+name+"所述的<"+something+">事情!转交给上级!");
			getLearder().handler(level);
		}
	}
}

/**
 * The type Branch manager.
 */
//部门经理
class BranchManager extends Learder{
	 private String name;
	 private String something;

    /**
     * Instantiates a new Branch manager.
     *
     * @param name      the name
     * @param something the something
     */
    public BranchManager(String name,String something) {
		this.name=name;
		this.something=something;
	}
	
	@Override
	void handler(int level) {
		boolean flag=true;
		//如果级别在自己的处理范围之内
		if(level>0){
			//这就就直接设置同意了
			if(flag){
				System.out.println("部门经理同意了  "+name+"所述的<"+something+">事情!");
			}else{
				System.out.println("部门经理不同意  "+name+"所述的<"+something+">事情!");
			}
		}else{
			System.out.println("部门经理未能处理  "+name+"所述的<"+something+">事情!转交给上级!");
			getLearder().handler(level);
		}
	}
}

/**
 * The type General manager.
 */
//总经理
class GeneralManager extends Learder{
	 private String name;
	 private String something;

    /**
     * Instantiates a new General manager.
     *
     * @param name      the name
     * @param something the something
     */
    public GeneralManager(String name,String something) {
		this.name=name;
		this.something=something;
	}
	
	@Override
	void handler(int level) {
		boolean flag=false;
		//如果级别在自己的处理范围之内
		if(level>-1){
			//这就就直接设置不同意了
			if(flag){
				System.out.println("总经理同意了  "+name+"所述的<"+something+">事情!");
			}else{
				System.out.println("总经理不同意  "+name+"所述的<"+something+">事情!");
			}
			
		}else{
			System.out.println("总经理未能处理  "+name+"所述的<"+something+">事情!转交给上级!");
			getLearder().handler(level);
		}
	}
}

