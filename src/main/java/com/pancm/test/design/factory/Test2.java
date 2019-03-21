/**
 * 
 */
package com.pancm.test.design.factory;

/**
 * The type Test 2.
 *
 * @author pancm
 * @Title: Test2
 * @Description: 工厂方法模式测试
 * @Version:1.0.0
 * @date 2018年7月23日
 */
public class Test2 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
	}

}

/**
 * 定义一个接口
 */
interface Game2{
    /**
     * Play.
     */
    void play();
}

/**
 * 定义一个实现类
 */
class LOL2 implements Game2{
	@Override
	public void play() {
		System.out.println("正在玩LOL...");
	}	
}

/**
 * The type Dnf 2.
 */
class DNF2 implements Game2{
	@Override
	public void play() {
		System.out.println("正在玩DNF...");
	}	
}