package com.pancm.test.design.interpreter;

/**
* @Title: InterpreterTest
* @Description: 解释器模式
* 解释器模式（Interpreter Pattern）提供了评估语言的语法或表达式的方式，它属于行为型模式。
* 这种模式实现了一个表达式接口，该接口解释一个特定的上下文。这种模式被用在 SQL 解析、符号处理引擎等。
* 比如正则表达式
* @Version:1.0.0  
* @author pancm
* @date 2018年8月8日
*/
public class InterpreterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*
		 * 基本使用
		 * 使用不同的解释器，输出的结果不同
		 */
		Context context = new Context();
		context.setId(1);
		context.setName("xuwujing");
		AbstractExpreeion aExpreeion=new OneExpreeion();
		AbstractExpreeion aExpreeion2=new TwoExpreeion();
		aExpreeion.interpert(context);
		aExpreeion2.interpert(context);
		
		
		
		
		/*
		 
		 应用实例：编译器、运算表达式计算。

		优点： 1、可扩展性比较好，灵活。 2、增加了新的解释表达式的方式。 3、易于实现简单文法。
		
		缺点： 1、可利用场景比较少。 2、对于复杂的文法比较难维护。 3、解释器模式会引起类膨胀。 4、解释器模式采用递归调用方法。
		
		使用场景： 1、可以将一个需要解释执行的语言中的句子表示为一个抽象语法树。 2、一些重复出现的问题可以用一种简单的语言来进行表达。 3、一个简单语法需要解释的场景。
		 
		 */
		
		
	}

}

class Context{
	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/** 
	 * 
	 */
	@Override
	public String toString() {
		return "Context [id=" + id + ", name=" + name + "]";
	}
	
}

/*
 * 定义一个抽象类
 */
abstract class AbstractExpreeion{
	abstract void interpert(Context context);
}

/*
 * 一个解释器
 */
class OneExpreeion extends AbstractExpreeion{
	@Override
	void interpert(Context context) {
		System.out.println("one:"+context.toString());
	}
}

/*
 * 另一个解释器
 */
class TwoExpreeion extends AbstractExpreeion{
	@Override
	void interpert(Context context) {
		System.out.println("two:"+context.toString());
	}
}



