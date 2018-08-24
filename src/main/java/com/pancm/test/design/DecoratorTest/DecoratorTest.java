package com.pancm.test.design.DecoratorTest;

/**
* @Title: DecoratorTest
* @Description: 装饰器模式
* 动态地给一个对象添加一些额外的职责。就增加功能来说，装饰器模式相比生成子类更为灵活。
* 
* @Version:1.0.0  
* @author pancm
* @date 2018年8月8日
*/
public class DecoratorTest {

	public static void main(String[] args) {
		
	}

}


interface Model{
	void walk();
}

class GUNDAM implements Model{

	@Override
	public void walk() {
		System.out.println("GUNDAM行走");
	}
}

//飞行装置
 class  FlyingDevice extends GUNDAM{
	protected  Model model;
	
	public FlyingDevice(Model model){
		this.model=model;
	}
	
	public void walk(){
		model.walk();
	}
}

 
 
