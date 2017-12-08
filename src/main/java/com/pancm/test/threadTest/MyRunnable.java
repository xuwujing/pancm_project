package com.pancm.test.threadTest;
/**
 * @author ZERO
 * @Data 2017-5-24 下午2:29:39
 * @Description 
 */
public class MyRunnable implements Runnable{
	private int i = 0;
	
	@Override
	public void run() {
	  for (i = 0; i < 100; i++) {
		System.out.println("MyRunnable:"+Thread.currentThread().getName() + " " + i);
	   }	
	}
  
}
