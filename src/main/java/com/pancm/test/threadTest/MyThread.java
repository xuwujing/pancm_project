package com.pancm.test.threadTest;
/**
 * @author ZERO
 * @Data 2017-5-24 下午2:20:29
 * @Description 
 */
public class MyThread extends Thread{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	

	}

	private int i = 0;
	 
	 @Override
     public void run() {
        for (i = 0; i < 100; i++) {
             System.out.println("MyThread:"+Thread.currentThread().getName() + " " + i);
         }
    }
	
	 
	 
	 
}
