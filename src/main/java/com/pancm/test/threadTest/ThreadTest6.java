package com.pancm.test.threadTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Thread test 6.
 *
 * @author pancm
 * @Title: ThreadTest6
 * @Description:
 * @Version:1.0.0
 * @date 2018年7月23日
 */
public class ThreadTest6 {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Map<String, Object> map=new HashMap<String,Object>();	
		List<Integer> l=new ArrayList<>();
		Thread99 t9=new Thread99();
		Thread t=new Thread(t9);
		
	}
}


/**
 * The type Thread 99.
 */
class Thread99 implements Runnable{

	@Override
	public void run() {
		System.out.println("====");	
	}
}



