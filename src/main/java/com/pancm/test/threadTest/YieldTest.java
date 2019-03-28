package com.pancm.test.threadTest;

/**
 * The type Yield test.
 *
 * @author pancm
 * @Title: YaildTest
 * @Description: 线程中的 Yaild方法测试
 * @Version:1.0.0
 * @date 2018年5月22日
 */
public class YieldTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Test1 t1 = new Test1("张三");
		Test1 t2 = new Test1("李四");
		
		new Thread(t1).start();
		new Thread(t2).start();
	}
}

/**
 * The type Test 1.
 */
class Test1 implements Runnable {
	private String name;

    /**
     * Instantiates a new Test 1.
     *
     * @param name the name
     */
    public Test1(String name) {
		this.name=name;
	}
	@Override
	public void run() {
        System.out.println(name + " 线程运行开始!");  
		for (int i = 1; i <= 5; i++) {
            System.out.println("子线程"+name+ "运行 : " + i);  
			// 当为3的时候，让出资源
			if (i == 3) {
				Thread.yield();
			}
		}
        System.out.println(name + " 线程运行结束!");  
	}
}