package com.pancm.test.system;

/**
* @Title: SystemTest
* @Description: 系统相关的类
* @Version:1.0.0  
* @author pancm
* @date 2019年1月16日
*/
public class SystemTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//获取当前的cpu数
		int cup = Runtime.getRuntime().availableProcessors();
		System.out.println("当前cup个数:"+cup);
		//获取当前路径
		String path= System.getProperty("user.dir");
		System.out.println("获取当前路径:"+path);
		//获取当前操作系统
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("获取当前系统:"+os);
	}

}
