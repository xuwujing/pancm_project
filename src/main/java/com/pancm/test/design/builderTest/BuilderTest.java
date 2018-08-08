package com.pancm.test.design.builderTest;

/**
* @Title: BuilderTest
* @Description: 
* 建造者模式
* @Version:1.0.0  
* @author pancm
* @date 2018年8月7日
*/
public class BuilderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

/**
 * 
* @Title: Breakfast
* @Description: 
* 定义一份早餐
* 分为吃的和喝的
* @Version:1.0.0  
* @author pancm
* @date 2018年8月7日
 */
class Breakfast{
	private String food;
	private String drinks;
	public void setFood(String food) {
		this.food = food;
	}
	public void setDrinks(String drinks) {
		this.drinks = drinks;
	}
	
		
}

/**
 * 
* @Title: IBuilderFood
* @Description: 
* 定义一个食物接口
*     有豆浆、牛奶、煎饼和三明治
* @Version:1.0.0  
* @author pancm
* @date 2018年8月7日
 */
interface IBuilderFood{
	void buildSandwich();
	void buildPancake();
	void buildMilk();
	void buildSoybeanMilk();
	Breakfast createBreakfast();
}

/**
 * 
* @Title: ChinaBreakfast
* @Description:定义一个早餐店 
* @Version:1.0.0  
* @author pancm
* @date 2018年8月7日
 */
class BreakfastBars implements IBuilderFood{
	Breakfast breakfast;
	
	public BreakfastBars(){
		breakfast=new Breakfast();
	}
	
	@Override
	public void buildSandwich() {
		breakfast.setDrinks("");
	}

	@Override
	public void buildPancake() {
		// TODO Auto-generated method stub
	}

	@Override
	public void buildMilk() {
		// TODO Auto-generated method stub
	}

	@Override
	public void buildSoybeanMilk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Breakfast createBreakfast() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
