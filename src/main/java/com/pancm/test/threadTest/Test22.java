package com.pancm.test.threadTest;

import java.util.HashMap;
import java.util.Map;


/**
 * The type Test 22.
 */
public class Test22 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Map<Integer,Integer> map=new HashMap<Integer,Integer>();
		map.put(0, 0);
		map.put(1, 1);
		for(Integer type:map.keySet()){
			Thread3 t3=new Thread3(type);
			t3.start();
		}
	}
}

/**
 * The type Thread 3.
 */
class Thread3 extends Thread{

	private int type;

    /**
     * Instantiates a new Thread 3.
     *
     * @param type the type
     */
    public Thread3(int type){
		this.type=type;
	}
	 
	 @Override
     public void run() {
		 if(type==0){
			 //连接mysql
			 System.out.println("线程ID:"+getId()+"连接mysql");
		 }else if(type==1){
			 //连接oracle
			 System.out.println("线程ID:"+getId()+"连接oracle");
		 }
    }
	
	 
	 
	 
}