package com.pancm.test.threadTest;

/**
 * @Title: YaildTest
 * @Description: 线程中的 Yaild方法测试
 * @Version:1.0.0
 * @author pancm
 * @date 2018年5月22日
 */
public class YieldTest {
	public static void main(String[] args) {
		Test1 t1 = new Test1("张三");
		Test1 t2 = new Test1("李四");
		t1.start();
		t2.start();
	}
}

class Test1 extends Thread {
	public Test1(String name) {
		super(name);
	}
	@Override
	public void run() {
        System.out.println(this.getName() + " 线程运行开始!");  
		for (int i = 1; i <= 5; i++) {
            System.out.println("子线程"+this.getName() + "运行 : " + i);  
			// 当为3的时候，让出资源
			if (i == 3) {
				this.yield();
			}
		}
        System.out.println(this.getName() + " 线程运行结束!");  
	}
}