package com.pancm.test.ioTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
* @Title: Test
* @Description: 
* io字符流和字节流测试
* @Version:1.0.0  
* @author pancm
* @date 2018年6月11日
*/
public class Test {

	public static void main(String[] args)  {
		try {
//			test();
//			test2();
//			test3();
//			test4();
//			test5();
//			test6();
			test7();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	/**
	 * 字节流
	 * 创建一个文件并新增一条记录
	 * @throws IOException
	 */
	private static void test2() throws IOException {
		String path="E:/test/hello.txt";
		String str="hello world";
		//创建一个文件并向文件中写数据 需要文件夹存在
		OutputStream output = new FileOutputStream(path);
		output.write(str.getBytes());
		output.close();
	}	
	
	/**
	 * 字节流
	 * 读取文件中的记录
	 * @throws IOException
	 */
	private static void test3() throws IOException {
		String path="E:/test/hello.txt";
		//从文件读取数据
		InputStream input = new FileInputStream(path);
	    int size = input.available();
	    StringBuffer sb=new StringBuffer();
		for(int i=0;i<size;i++){
			sb.append((char)input.read());
		}
		System.out.println("输出:"+sb);
		input.close();
		/*
		 *  输出:hello world
		 */
	}	
	
	/**
	 * 字节流
	 * 创建一个文件并读取记录 防止乱码
	 * @throws IOException
	 */
	private static void test4() throws IOException {
		String path="E:/test/hello.txt";
		String str="你好!";
		//创建一个文件并向文件中写数据
		OutputStream output = new FileOutputStream(path);
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(str);
		
		writer.close();
		output.close();
		
		//从文件读取数据
		InputStream input = new FileInputStream(path);
		InputStreamReader reader = new InputStreamReader(input, "UTF-8");
	    StringBuffer sb=new StringBuffer();
		while(reader.ready()){
			sb.append((char)reader.read());
		}
		
		System.out.println("输出:"+sb);
		
		input.close();
		reader.close();
	}	
	
	
	/**
	 * 字符流
	 * @throws IOException
	 */
	private static void test5() throws IOException {
		   String str;
		    // 使用 System.in 创建 BufferedReader 
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    System.out.println("输入字符, 输入 'quit' 退出。");
		    // 读取字符
		    do {
		       str=br.readLine();
		       System.out.println("您输入的字符是:"+str);
		    } while(!str.equals("quit"));
		    br.close();
	}
	
	/**
	 * 字符流
	 * 写入和读取文件
	 * @throws IOException
	 */
	private static void test6() throws IOException {
		//创建要操作的文件路径和名称  
        //其中，File.separator表示系统相关的分隔符，Linux下为：/  Windows下为：\\  
        String path ="E:/test2/hello.txt";
        String str="hello world";
        FileWriter fw = new FileWriter(path);  
        //以path为路径创建一个新的FileWriter对象  
        //如果需要追加数据，而不是覆盖，则使用FileWriter（path，true）构造方法  
        //将字符串写入到流中，\r\n表示换行想有好的  
        fw.write(str);  
        fw.close();  
        
        FileReader fr = new FileReader(path);  
        StringBuffer sb=new StringBuffer();
  		while(fr.ready()){
  			sb.append((char)fr.read());
  		}
        System.out.println("输出:"+sb.toString());
        fr.close();
	}
	
	
	/**
	 * 字符流
	 * 写入和读取文件
	 * @throws IOException
	 */
	private static void test7() throws IOException {
		//创建要操作的文件路径和名称  
        String path ="E:/test2/hello.txt";
        String str="你好!";
        FileWriter fw = new FileWriter(path);  
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write(str);  
        bw.close();
        fw.close();  
        
        FileReader fr = new FileReader(path);  
        BufferedReader br=new BufferedReader(fr);
        StringBuffer sb=new StringBuffer();
  		while(br.ready()){
  			sb.append((char)br.read());
  		}
        System.out.println("输出:"+sb.toString());
        br.close();
        fr.close();
	}
	
	
	
	/**
	 * 新建文件夹和文件
	 * @throws IOException
	 */
	private static void test10() throws IOException {
		String path="E:/test/test2";
		File f = new File(path);
		//创建文件夹
		f.mkdir();
		String path2="E:/test/test2/test2.txt";
		File f2 = new File(path2);
		//创建一个文本
		f2.createNewFile();
		System.out.println("==="+f2.getName());
		System.out.println("==="+f2.getParent());
		//获取当前路径
		System.out.println("==="+f2.getPath());
		System.out.println("==="+f2.getAbsolutePath());
		System.out.println("==="+f2.getCanonicalPath());
		
		
	}	
	
}
