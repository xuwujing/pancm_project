/**
 * 
 */
package com.pancm.test.design.factory;

/**
* @Title: Test2
* @Description: 
* 工厂方法模式测试
* @Version:1.0.0  
* @author pancm
* @date 2018年7月23日
*/
public class Test2 {

	public static void main(String[] args) {
	}

}

/**
 * 定义一个接口
 */
interface Game2{
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

class DNF2 implements Game2{
	@Override
	public void play() {
		System.out.println("正在玩DNF...");
	}	
}