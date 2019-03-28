package com.pancm.test.threadTest;

/**
 * The type Thead test 3.
 */
public class TheadTest3 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
		MyRunnable myRunnable=new MyRunnable();
		for(int i=1;i<=5;i++){
			Thread thread=new Thread(myRunnable);
			thread.setName("myRunnable-"+i);
			thread.start();
		}
		Thread.sleep(2000);
		myRunnable.set(true);
		Thread.sleep(3000);
		myRunnable.set(false);
		for(int i=1;i<=5;i++){
			MyThread myThread=new MyThread();
			myThread.setName("myThread-"+i);
			myThread.start();
		}
		
		
		System.out.println("结束...");
	}
	
	

}
