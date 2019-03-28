package com.pancm.test.threadTest;

import java.util.Random;

/**
 * The type Notify test.
 *
 * @author pancm
 * @Title: NotifyTest
 * @Description: wait  和 notify测试
 * @Version:1.0.0
 * @date 2018年5月22日
 */
public class NotifyTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Test4 t1 = new Test4("张三");
		Test4 t2 = new Test4("李四");
		t1.start();
		t2.start();
	}
}

/**
 * The type Test 4.
 */
class Test4 extends Thread {
	private String name;

    /**
     * Instantiates a new Test 4.
     *
     * @param name the name
     */
    public Test4(String name) {
		super(name);
		this.name=name;
	}

	@Override
	public void run() {
		System.out.println(this.getName() + " 线程运行开始!");

		for (int i = 0; i < 5; i++) {
			System.out.println("子线程" + this.getName() + "运行 : " + i);
			try {
				sleep(new Random().nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				if("李四".equals(this.getName())){
					this.getName().wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.getName().notify();

		}
		System.out.println(this.getName() + " 线程运行结束!");
	}
}
