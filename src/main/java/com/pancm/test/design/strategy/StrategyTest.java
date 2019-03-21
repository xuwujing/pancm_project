package com.pancm.test.design.strategy;

/**
 * The type Strategy test.
 *
 * @author pancm
 * @Title: StrategyTest
 * @Description: 策略模式 在策略模式（CalculateStrategy Pattern）中，一个类的行为或其算法可以在运行时更改。               这种类型的设计模式属于行为型模式。 在策略模式中，我们创建表示各种策略的对象和一个行为随着策略对象改变而改变的 context               对象。 策略对象改变 context 对象的执行算法。
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class StrategyTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

		/*
		 * 基本使用
		 * 
		 角色
		   1，环境角色(Context)：持有一个策略类的引用，提供给客户端使用。
	
	　　 	   2，抽象策略角色(Strategy)：这是一个抽象角色，通常由一个接口或抽象类实现。此角色给出所有的具体策略类所需的接口。
	
	　　	   3，具体策略角色(ConcreteStrategy)：包装了相关的算法或行为。
		 * 
		 */
			
		  /*
		   * 计算加减乘除
		   */
		   int a=4,b=2;
		  CalculatorContext context = new CalculatorContext(new OperationAdd());    
	      System.out.println("a + b = "+context.executeStrategy(a, b));
	 
	      CalculatorContext context2 = new CalculatorContext(new OperationSub());      
	      System.out.println("a - b = "+context2.executeStrategy(a, b));
	 
	      CalculatorContext context3 = new CalculatorContext(new OperationMul());    
	      System.out.println("a * b = "+context3.executeStrategy(a, b));
	
	      CalculatorContext context4 = new CalculatorContext(new OperationDiv());    
	      System.out.println("a / b = "+context4.executeStrategy(a, b));
	
	      
	      /*
	        a + b = 6
			a - b = 2
			a * b = 8
			a / b = 2
	       */
	      
	      
	      /*
       	  	优点： 1、扩展性好，可以在不修改对象结构的情况下，为新的算法进行添加新的类进行实现；
       	 	     2、灵活性好，可以对算法进行自由切换；

			缺点： 1、使用策略类变多，会增加系统的复杂度。 
				 2、客户端必须知道所有的策略类才能进行调用

			使用场景： 1、如果在一个系统里面有许多类，它们之间的区别仅在于它们的行为，那么使用策略模式可以动态地让一个对象在许多行为中选择一种行为。 
					2、一个系统需要动态地在几种算法中选择一种。 
					3、如果一个对象有很多的行为，如果不用恰当的模式，这些行为就只好使用多重的条件选择语句来实现。
	       
	       */
	}
}

/**
 * The interface Calculate strategy.
 */
//定义一个策略
interface CalculateStrategy {
    /**
     * Do operation int.
     *
     * @param num1 the num 1
     * @param num2 the num 2
     * @return the int
     */
    int doOperation(int num1, int num2);
}

/**
 * The type Operation add.
 */
//定义一个加法
class OperationAdd implements CalculateStrategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 + num2;
	}
}

/**
 * The type Operation sub.
 */
//定义一个减法
class OperationSub implements CalculateStrategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 - num2;
	}
}

/**
 * The type Operation mul.
 */
//定义一个乘法
class OperationMul implements CalculateStrategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 * num2;
	}
}

/**
 * The type Operation div.
 */
//定义一个除法
class OperationDiv implements CalculateStrategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 / num2;
	}
}

/**
 * The type Calculator context.
 */
//定义一个环境
class  CalculatorContext {
	private CalculateStrategy strategy;

    /**
     * Instantiates a new Calculator context.
     *
     * @param strategy the strategy
     */
    public CalculatorContext(CalculateStrategy strategy) {
		this.strategy = strategy;
	}

    /**
     * Execute strategy int.
     *
     * @param num1 the num 1
     * @param num2 the num 2
     * @return the int
     */
    public int executeStrategy(int num1, int num2) {
		return strategy.doOperation(num1, num2);
	}
}



