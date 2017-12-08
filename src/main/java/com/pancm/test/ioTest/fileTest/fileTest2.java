package com.pancm.test.ioTest.fileTest;

import java.io.File;
import java.io.IOException;

/**
 * 
* Title: fileTest
* Description:  IO中File类的创建和删除测试
* Version:1.0.0  
* @author panchengming
 */
public class fileTest2 {
	/**
	 * 在开发程序时，运行环境不同，路径分隔符也不同。
	 * 所以应该让该程序具有可以移植性。
	 * 那么让操作系统自己选择分隔符。
	 * 使用File.separator可以获取分割符。
	 */
	private final static String windPath="e:\\test\\test.txt";  //windows路径
	private final static String linuxPath="e://test//test.txt";  //linux路径
	private final static String path="e:"+File.separator+ "test"+File.separator+"test.txt";  //通用路径
	
	public static void main(String[] args) {
		createNewFile(path);   	//创建文件
		deleteFile(path);		//删除文件
	}
	
	/**
	 * 创建文件
	 * @param path
	 */
	public static void createNewFile(String path){
		File file=new File(path); //实例化File文件，给出路径
		try {
			file.createNewFile();  //创建文件
			System.out.println("创建成功");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除文件
	 * @param path
	 */
	public static void deleteFile(String path){
		File file=new File(path); //实例化File文件，给出路径
		if(file.exists()){ //如果存在则删除
			file.delete();
			System.out.println("该文件成功删除！");
		}else{
			System.out.println("该文件不存在！");
		}
	}
	
	
	
}
