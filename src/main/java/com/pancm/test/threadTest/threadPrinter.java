package com.pancm.test.threadTest;
/**
 * @author ZERO
 * @Data 2017-5-27 下午4:27:21
 * @Description 线程安全测试
 */
public class threadPrinter implements Runnable {

	 private String name;     
	    private Object prev;     
	    private Object self;     
	    
	    private threadPrinter(String name, Object prev, Object self) {     
	        this.name = name;     
	        this.prev = prev;     
	        this.self = self;     
	    }     
	    
	    @Override    
	    public void run() {     
	        int count = 10;     
	        while (count > 0) {     
	            synchronized (prev) {     
	                synchronized (self) {     
	                    System.out.print(name);     
	                    count--;    
	                    self.notify();     
	                }     
	                try {     
	                    prev.wait();     
	                } catch (InterruptedException e) {     
	                    e.printStackTrace();     
	                }     
	            }     
	    
	        }     
	    }     
	    
	    public static void main(String[] args) throws Exception {     
	        Object a = new Object();     
	        Object b = new Object();     
	        Object c = new Object();     
	        threadPrinter pa = new threadPrinter("A", c, a);     
	        threadPrinter pb = new threadPrinter("B", a, b);     
	        threadPrinter pc = new threadPrinter("C", b, c);     
	             
	             
	        new Thread(pa).start();  
	        Thread.sleep(100);  //确保按顺序A、B、C执行  
	        new Thread(pb).start();  
	        Thread.sleep(100);    
	        new Thread(pc).start();     
	        Thread.sleep(100);    
	        }     
}
