package com.pancm.test.design.decorator;

/**
 * The type Decorator test.
 *
 * @author pancm
 * @Title: DecoratorTest
 * @Description: 装饰器模式  动态地给一个对象添加一些额外的职责。就增加功能来说，装饰器模式相比生成子类更为灵活。 比如一个人，可以穿不同的装饰，外套、T恤、短裤、西服等等 人是不变的Component抽象构件角色：真实对象和装饰对象有相同的接口。这样，客户端对象就能够以与真实对象相同的方式同装饰对象交互。ConcreteComponent具体构件角色（真实对象）：io流中的FileInputStream、　　　　FileOutputStreamDecorator装饰角色：持有一个抽象构件的引用。装饰对象接受所有客户端的请求，并把这些请求转发给真实的对象。这样，就能在真实对象调用前后增加新的功能。ConcreteDecorator具体装饰角色：负责给构件对象增加新的责任。
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class DecoratorTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		//组装模型
		Model gundam=new GUNDAM();
		Model mrgu=new MrGu();
		gundam.assemble();
		mrgu.assemble();
		
		//组装模型并添加武器
		Model gModel=new LightSaber(new GUNDAM());
		gModel.assemble();
		Model mModel=new RocketLauncher(new MrGu());
		mModel.assemble();
		
		/*
		 * 
		组装一个高达模型
		组装一个扎古模型
		组装一个高达模型
		添加光剑
		组装一个扎古模型
		添加火箭筒
		 */
	}
}


/**
 * The interface Model.
 */
interface Model{
    /**
     * Assemble.
     */
    void  assemble();
}

/**
 * The type Gundam.
 */
class GUNDAM implements Model{
	@Override
	public void  assemble() {
		System.out.println("组装一个高达模型");
	}
}

/**
 * The type Mr gu.
 */
class MrGu implements Model{
	@Override
	public void  assemble() {
		System.out.println("组装一个扎古模型");
	}
}

/**
 * The type Add extra.
 */
//添加额外的功能
//装饰器
abstract class  AddExtra implements Model{
    /**
     * The Model.
     */
    protected  Model model;

    /**
     * Instantiates a new Add extra.
     *
     * @param model the model
     */
    public AddExtra(Model model){
		this.model=model;
	}
	public  void assemble(){
		model.assemble();
	}
}

/**
 * The type Light saber.
 */
//添加光剑
class LightSaber extends AddExtra{

    /**
     * Instantiates a new Light saber.
     *
     * @param model the model
     */
    public LightSaber(Model model) {
		super(model);
	}
	
	public  void assemble(){
		model.assemble();
		addLightSaber();
	}

    /**
     * Add light saber.
     */
    public void addLightSaber(){
		System.out.println("添加光剑");
	}
}


/**
 * The type Rocket launcher.
 */
//添加火箭筒
class RocketLauncher extends AddExtra{

    /**
     * Instantiates a new Rocket launcher.
     *
     * @param model the model
     */
    public RocketLauncher(Model model) {
		super(model);
	}
	
	public  void assemble(){
		model.assemble();
		addRocketLauncher();
	}

    /**
     * Add rocket launcher.
     */
    public void addRocketLauncher(){
		System.out.println("添加火箭筒");
	}
}





