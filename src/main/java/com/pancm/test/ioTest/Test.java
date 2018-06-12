package com.pancm.test.ioTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* @Title: Test
* @Description: 
* @Version:1.0.0  
* @author pancm
* @date 2018年6月11日
*/
public class Test {

	public static void main(String[] args)  {
		try {
			test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void test() throws IOException {
		   String str;
		    // 使用 System.in 创建 BufferedReader 
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    System.out.println("输入字符, 输入 'quit' 退出。");
		    // 读取字符
		    do {
		       str=br.readLine();
		       System.out.println("您输入的字符是:"+str);
		    } while(!str.equals("quit"));
	}
	
	

}
