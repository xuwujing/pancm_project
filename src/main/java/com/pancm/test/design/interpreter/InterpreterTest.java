package com.pancm.test.design.interpreter;

/**
 * The type Interpreter test.
 *
 * @author pancm
 * @Title: InterpreterTest
 * @Description: 解释器模式 解释器模式（Interpreter Pattern）是类的行为模式。给定一个语言之后，解释器模式可以定义出其文法的一种表示，并同时提供一个解释器。客户端可以使用这个解释器来解释这个语言中的句子。 比如正则表达式
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class InterpreterTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		
		/*
		 * 基本使用
		  使用不同的解释器，输出的结果不同
		
	       （1）抽象表达式(Expression)角色：声明一个所有的具体表达式角色都需要实现的抽象接口。这个接口主要是一个interpret()方法，称做解释操作。

	　　（2）终结符表达式(Terminal Expression)角色：实现了抽象表达式角色所要求的接口，主要是一个interpret()方法；文法中的每一个终结符都有一个具体终结表达式与之相对应。比如有一个简单的公式R=R1+R2，在里面R1和R2就是终结符，对应的解析R1和R2的解释器就是终结符表达式。
	
	　　（3）非终结符表达式(Nonterminal Expression)角色：文法中的每一条规则都需要一个具体的非终结符表达式，非终结符表达式一般是文法中的运算符或者其他关键字，比如公式R=R1+R2中，“+"就是非终结符，解析“+”的解释器就是一个非终结符表达式。
	
	　　（4）环境(Context)角色：这个角色的任务一般是用来存放文法中各个终结符所对应的具体值，比如R=R1+R2，我们给R1赋值100，给R2赋值200。这些信息需要存放到环境角色中，很多情况下我们使用Map来充当环境角色就足够了。
			 
		 
		 */
		Context context = new Context();
		context.setId(1);
		context.setName("xuwujing");
		AbstractExpreeion aExpreeion=new OneExpreeion();
		AbstractExpreeion aExpreeion2=new TwoExpreeion();
		aExpreeion.interpert(context);
		aExpreeion2.interpert(context);
		
		String word = "好好学习，天天向上!";
		Expreeion expreeion =new  BaiduExpreeion();
		Expreeion expreeion2 =new  YouDaoExpreeion();
		Expreeion expreeion3 =new  XuWuJingExpreeion();
		expreeion.interpert(word);
		expreeion2.interpert(word);
		expreeion3.interpert(word);
		
		
		/*
		 输出结果：
		 百度翻译：好好学习，天天向上! 的英文是  Study hard.
		有道翻译：好好学习，天天向上! 的英文是  study hard and make progress every day
		xuwujing翻译：好好学习，天天向上! 的英文是  Good good study, day day up.
		 
		 */
		
		/*
		 
		 应用实例：编译器、运算表达式计算。

		优点： 1、可扩展性比较好，灵活。 2、增加了新的解释表达式的方式。 3、易于实现简单文法。
		
		缺点： 1、可利用场景比较少。 2、对于复杂的文法比较难维护。 3、解释器模式会引起类膨胀。 4、解释器模式采用递归调用方法。
		
		使用场景： 1、可以将一个需要解释执行的语言中的句子表示为一个抽象语法树。 2、一些重复出现的问题可以用一种简单的语言来进行表达。 3、一个简单语法需要解释的场景。
		 
		 */
		
		
	}

}

/**
 * The type Context.
 */
class Context{
	private Integer id;
	private String name;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
		return id;
	}

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
		this.id = id;
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
	 * 
	 */
	@Override
	public String toString() {
		return "Context [id=" + id + ", name=" + name + "]";
	}
	
}

/**
 * The type Abstract expreeion.
 */
/*
 * 定义一个抽象类
 */
abstract class AbstractExpreeion{
    /**
     * Interpert.
     *
     * @param context the context
     */
    abstract void interpert(Context context);
}

/**
 * The type One expreeion.
 */
/*
 * 一个解释器
 */
class OneExpreeion extends AbstractExpreeion{
	@Override
	void interpert(Context context) {
		System.out.println("one:"+context.toString());
	}
}

/**
 * The type Two expreeion.
 */
/*
 * 另一个解释器
 */
class TwoExpreeion extends AbstractExpreeion{
	@Override
	void interpert(Context context) {
		System.out.println("two:"+context.toString());
	}
}


/**
 * The interface Expreeion.
 */
/*
   * 定义一个表达式，有一个解释的方法
 */
interface Expreeion{
    /**
     * Interpert.
     *
     * @param word the word
     */
    void interpert(String word);
}

/**
 * The type Baidu expreeion.
 */
class  BaiduExpreeion implements Expreeion{
    /**
     * The Str.
     */
    String str ="好好学习，天天向上!";
	@Override
	public void interpert(String word) {
		//如果是这句就翻译
		if(str.equals(word)) {
			System.out.println("百度翻译："+word+" 的英文是  Study hard.");
		}
	}
}

/**
 * The type You dao expreeion.
 */
class  YouDaoExpreeion implements Expreeion{
    /**
     * The Str.
     */
    String str ="好好学习，天天向上!";
	@Override
	public void interpert(String word) {
		//如果是这句就翻译
		if(str.equals(word)) {
			System.out.println("有道翻译："+word+" 的英文是  study hard and make progress every day");
		}
	}
}

/**
 * The type Xu wu jing expreeion.
 */
class  XuWuJingExpreeion implements Expreeion{
    /**
     * The Str.
     */
    String str ="好好学习，天天向上!";
	@Override
	public void interpert(String word) {
		//如果是这句就翻译
		if(str.equals(word)) {
			System.out.println("xuwujing翻译："+word+" 的英文是  Good good study, day day up.");
		}
	}
}
