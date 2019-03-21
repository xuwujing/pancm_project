package com.pancm.test.threadTest;

import java.util.Random;

/**
 * The type Priority test.
 *
 * @author pancm
 * @Title: PriorityTest
 * @Description: 线程优先级测试
 * @Version:1.0.0
 * @date 2018年5月27日
 */
public class PriorityTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Test3 t1 = new Test3("张三");
		Test3 t2 = new Test3("李四");
		t1.setPriority(Thread.MIN_PRIORITY);
		t2.setPriority(Thread.MAX_PRIORITY);
		t1.start();
		t2.start();
	}
}

/**
 * The type Test 3.
 */
class Test3 extends Thread {
    /**
     * Instantiates a new Test 3.
     *
     * @param name the name
     */
    public Test3(String name) {
		super(name);
	}
	@Override
	public void run() {
        System.out.println(this.getName() + " 线程运行开始!");  
		for (int i = 1; i <= 5; i++) {
            System.out.println("子线程"+this.getName() + "运行 : " + i); 
            try {  
                sleep(new Random().nextInt(10));  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            } 
		}
        System.out.println(this.getName() + " 线程运行结束!");  
	}
}

