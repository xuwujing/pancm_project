package com.pancm.test.threadTest;

import org.junit.Test;

/**
 * The type Thread test 2.
 *
 * @author ZERO
 * @Data 2017 -5-18 上午9:47:55
 * @Description 线程测试
 */
public class threadTest2 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
       
		
	}

    /**
     * Create thread 1.
     */
    @Test
    public void createThread1(){
        Thread t1 = new Thread(){
            public void run(){
                    System.out.println("创建线程的方式1");
            }
        };
        t1.start();
    }

    /**
     * Create thread 2.
     */
    @Test
    public void createThread2(){
        Thread t2 = new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("创建线程的方式2");
            }

        });
        t2.start();
    }
}
