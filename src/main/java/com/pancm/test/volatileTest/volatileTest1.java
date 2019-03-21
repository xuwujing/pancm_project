package com.pancm.test.volatileTest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Title: volatileTest1
 * Description: volatile关键字的测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年9月21日
 */
public class volatileTest1 {
    /**
     * The Inv.
     */
    public volatile int inv = 0; //使用volatile 保证了可见性，其他线程也可以查看更新之后的值
    /**
     * The Ins.
     */
    public  int ins = 0;

    /**
     * The Inl.
     */
    public  int inl = 0;
    /**
     * The Lock.
     */
    Lock lock = new ReentrantLock();

    /**
     * Increase.
     */
    public void increase() {
	        inv++;
	    }

    /**
     * Insrease.
     */
    public synchronized void insrease() { //使用synchronized，可以保证原子性
	        ins++;
	    }

    /**
     * Inlrease.
     */
    public void inlrease(){    //使用loca 也可以保证
	    	 lock.lock();
	         try {
	             inl++;
	         } finally{
	             lock.unlock();
	         }
	    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		volatileTs();	
		synTs();
		lockTs();
	   }

    /**
     * 使用 volatile 是无法保证 原子性的
     * 因为 自增操作不是原子性操作
     */
    public static void volatileTs(){
		 final volatileTest1 test = new volatileTest1();
	        for(int i=0;i<10;i++){
	            new Thread(){
	                @Override
					public void run() {
	                    for(int j=0;j<1000;j++) {
							test.increase();
						}
	                };
	            }.start();
	        }
	        while(Thread.activeCount()>1) {
				Thread.yield();
			}
	        System.out.println(test.inv); //数据小于 10000  例如:9303,9068
	}

    /**
     * 使用synchronized，可以保证原子性
     */
    public static void synTs(){
		 final volatileTest1 test = new volatileTest1();
	        for(int i=0;i<10;i++){
	            new Thread(){
	                @Override
					public void run() {
	                    for(int j=0;j<1000;j++) {
							test.insrease();
						}
	                };
	            }.start();
	        }
	        while(Thread.activeCount()>1) {
				Thread.yield();
			}
	        System.out.println(test.ins); // 10000   保证了原子性
	}

    /**
     * 使用lock，可以保证原子性
     */
    public static void lockTs(){
		 final volatileTest1 test = new volatileTest1();
	        for(int i=0;i<10;i++){
	            new Thread(){
	                @Override
					public void run() {
	                    for(int j=0;j<1000;j++) {
							test.inlrease();
						}
	                };
	            }.start();
	        }
	        while(Thread.activeCount()>1) {
				Thread.yield();
			}
	        System.out.println(test.inl); //10000  保证了原子性
	}
	
	
	}

