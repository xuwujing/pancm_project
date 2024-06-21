package com.pancm.test.design.builder;

/**
 * The type Builder test.
 *
 * @author pancm
 * @Title: BuilderTest
 * @Description: 建造者模式   将一个复杂的构建与其表示相分离，使得同样的构建过程可以创建不同的表示。
 * @Version:1.0.0
 * @date 2018年8月7日
 */
public class BuilderTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		FoodStore foodStore=new FoodStore();
		Meal meal=foodStore.createBreakfast(new Breakfast());
		Meal meal2=foodStore.createBreakfast(new Lunch());
		System.out.println("小明早上吃的是:"+meal.getFood()+",喝的饮料是:"+meal.getDrinks());
		System.out.println("小明中午吃的是:"+meal2.getFood()+",喝的饮料是:"+meal2.getDrinks());
		
	}

}

/**
 * The type Meal.
 *
 * @author pancm
 * @Title: Breakfast
 * @Description: 定义一份餐点   分为吃的和喝的
 * @Version:1.0.0
 * @date 2018年8月7日
 */
class Meal{
	private String food;
	private String drinks;

    /**
     * Gets food.
     *
     * @return the food
     */
    public String getFood() {
		return food;
	}

    /**
     * Sets food.
     *
     * @param food the food
     */
    public void setFood(String food) {
		this.food = food;
	}

    /**
     * Gets drinks.
     *
     * @return the drinks
     */
    public String getDrinks() {
		return drinks;
	}

    /**
     * Sets drinks.
     *
     * @param drinks the drinks
     */
    public void setDrinks(String drinks) {
		this.drinks = drinks;
	}
}

/**
 * The interface Builder food.
 *
 * @author pancm
 * @Title: IBuilderFood
 * @Description: 定义一个食物接口
 * @Version:1.0.0
 * @date 2018年8月7日
 */
interface IBuilderFood{
    /**
     * Build food.
     */
    void buildFood();

    /**
     * Build drinks.
     */
    void buildDrinks();

    /**
     * Create meal meal.
     *
     * @return the meal
     */
    Meal createMeal();
}

/**
 * The type Breakfast.
 *
 * @author pancm
 * @Title: Breakfast
 * @Description:定义一份早餐
 * @Version:1.0.0
 * @date 2018年8月7日
 */
class Breakfast implements IBuilderFood{
    /**
     * The Meal.
     */
    Meal meal;

    /**
     * Instantiates a new Breakfast.
     */
    public Breakfast(){
		meal=new Meal();
	}
	
	@Override
	public void buildFood() {
		meal.setFood("煎饼");
	}

	@Override
	public void buildDrinks() {
		meal.setDrinks("豆浆");	
	}
	
	@Override
	public Meal createMeal() {
		return meal;
	}
}

/**
 * The type Lunch.
 *
 * @author pancm
 * @Title: Lunch
 * @Description: 定义一份午餐
 * @Version:1.0.0
 * @date 2018年8月15日
 */
class Lunch implements IBuilderFood{
    /**
     * The Meal.
     */
    Meal meal;

    /**
     * Instantiates a new Lunch.
     */
    public Lunch(){
		meal=new Meal();
	}
	
	@Override
	public void buildFood() {
		meal.setFood("盒饭");
	}

	@Override
	public void buildDrinks() {
		meal.setDrinks("果汁");	
	}
	
	@Override
	public Meal createMeal() {
		return meal;
	}
}

/**
 * The type Food store.
 *
 * @author pancm
 * @Title: FoodStore
 * @Description: 定义一个餐点   导演者
 * @Version:1.0.0
 * @date 2018年8月15日
 */
class FoodStore{
    /**
     * Create breakfast meal.
     * @param bf the bf
     * @return the meal
     */
    public Meal createBreakfast(IBuilderFood bf){
		bf.buildDrinks();
		bf.buildFood();
		return bf.createMeal();
	}
}

