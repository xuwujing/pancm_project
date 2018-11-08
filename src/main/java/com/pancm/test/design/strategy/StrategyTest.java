package com.pancm.test.design.strategy;

/**
 * @Title: StrategyTest
 * @Description: 策略模式 在策略模式（Strategy Pattern）中，一个类的行为或其算法可以在运行时更改。
 *               这种类型的设计模式属于行为型模式。 在策略模式中，我们创建表示各种策略的对象和一个行为随着策略对象改变而改变的 context
 *               对象。 策略对象改变 context 对象的执行算法。
 * @Version:1.0.0
 * @author pancm
 * @date 2018年8月8日
 */
public class StrategyTest {

	public static void main(String[] args) {

		/*
		 * 基本使用
		 */
		 Context context = new Context(new OperationAdd());    
	      System.out.println("10 + 5 = " + context.executeStrategy(10, 5));
	 
	      context = new Context(new OperationSub());      
	      System.out.println("10 - 5 = " + context.executeStrategy(10, 5));
	 
	      context = new Context(new OperationMul());    
	      System.out.println("10 * 5 = " + context.executeStrategy(10, 5));
	
	      context = new Context(new OperationDiv());    
	      System.out.println("10 / 5 = " + context.executeStrategy(10, 5));
	
	      /*
	       	优点： 1、算法可以自由切换。
	       	 	2、避免使用多重条件判断。 
	       	 	3、扩展性良好。

			缺点： 1、策略类会增多。 
				2、所有策略类都需要对外暴露。

			使用场景： 1、如果在一个系统里面有许多类，它们之间的区别仅在于它们的行为，那么使用策略模式可以动态地让一个对象在许多行为中选择一种行为。 
					2、一个系统需要动态地在几种算法中选择一种。 
					3、如果一个对象有很多的行为，如果不用恰当的模式，这些行为就只好使用多重的条件选择语句来实现。
	       
	       */
	}
}

//定义一个策略
interface Strategy {
	int doOperation(int num1, int num2);
}

//定义一个加法 
class OperationAdd implements Strategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 + num2;
	}
}

//定义一个减法
class OperationSub implements Strategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 - num2;
	}
}

//定义一个乘法
class OperationMul implements Strategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 * num2;
	}
}

//定义一个除法
class OperationDiv implements Strategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 / num2;
	}
}


class Context {
	private Strategy strategy;

	public Context(Strategy strategy) {
		this.strategy = strategy;
	}

	public int executeStrategy(int num1, int num2) {
		return strategy.doOperation(num1, num2);
	}
}